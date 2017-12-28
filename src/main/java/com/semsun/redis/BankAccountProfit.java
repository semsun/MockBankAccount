package com.semsun.redis;

import com.semsun.bank.entity.account.BankAccountInfo;
import com.semsun.bank.entity.account.MainAccountInfo;
import com.semsun.bank.entity.io.AccountPay;
import com.semsun.bank.entity.io.AccountReceive;
import com.semsun.bank.entity.io.Transfer;

import redis.clients.jedis.Jedis;

public class BankAccountProfit {
	
	public static String ACCOUNT_PREFIX = "bank_account_info";
	public static String JOURNAL_PREFIX = "bank_account_journal";

	private static BankAccountProfit instance = null;
	
	public static BankAccountProfit getInstance() {
		if( null == instance ) {
			instance = new BankAccountProfit();
		}
		
		return instance;
	}
	
	/**
	 * 
	 * @param subAcc
	 * @param info
	 * @return 0:正确；其他失败
	 */
	public int createAccount(String mainAcc, String subAcc, String info) {
		String key = String.format("%s_%s", ACCOUNT_PREFIX, subAcc);
		String mainAccKey = String.format("%s_main_%s", ACCOUNT_PREFIX, mainAcc);

		Jedis cache = RedisCache.getRedisCache().getResource();
		
		if( cache.isConnected() && cache.exists(key) ) {
			return 3101;
		}
		
		if( !cache.exists(mainAccKey) ) {
			MainAccountInfo mainAccInfo = new MainAccountInfo();
			mainAccInfo.setBank("temp");
			mainAccInfo.setMainAccount(mainAcc);
			mainAccInfo.setAmount(0);
			cache.set(mainAccKey, mainAccInfo.toJson());
		}
		
		cache.set(key, info);
		
		return 0;
	}
	
	/**
	 * 保存附属账户信息
	 * @param subAcc
	 * @param info
	 */
	public void saveAccountInfo(String subAcc, String info) {
		String key = String.format("%s_%s", ACCOUNT_PREFIX, subAcc);
		
		Jedis cache = RedisCache.getRedisCache().getResource();
		cache.set(key, info);
	}
	
	/**
	 * 获取附属账户信息
	 * @param subAcc
	 * @return
	 */
	public String getAccountInfo(String subAcc) {
		String key = String.format("%s_%s", ACCOUNT_PREFIX, subAcc);

		Jedis cache = RedisCache.getRedisCache().getResource();
		
		return cache.get(key);
	}
	
	/**
	 * 保存附属账户信息
	 * @param subAcc
	 * @param info
	 */
	public void saveMainAccInfo(String mainAcc, String info) {
		String key = String.format("%s_main_%s", ACCOUNT_PREFIX, mainAcc);
		
		Jedis cache = RedisCache.getRedisCache().getResource();
		cache.set(key, info);
	}
	
	/**
	 * 获取主账户信息
	 * @param mainAcc
	 * @return
	 */
	public String getMainAccInfo(String mainAcc) {
		String key = String.format("%s_main_%s", ACCOUNT_PREFIX, mainAcc);

		Jedis cache = RedisCache.getRedisCache().getResource();
		
		return cache.get(key);
	}
	
	/**
	 * 附属账户间划账
	 * @param tran
	 * @return
	 */
	public Double[] transfer(Transfer tran) {		
		BankAccountInfo payInfo = BankAccountInfo.fromJson( this.getAccountInfo(tran.getPayAccNo()) );
		BankAccountInfo recInfo = BankAccountInfo.fromJson( this.getAccountInfo(tran.getRecvAccNo()) );
		
		double amount = tran.getTranAmt();
		
		if ( payInfo.getAmount() < amount ) {
			return null;
		}
		
		payInfo.setAmount( payInfo.getAmount() - amount );
		recInfo.setAmount( recInfo.getAmount() + amount );

		/* 保存信息 */
		this.saveAccountInfo(payInfo.getSubAccNo(), payInfo.toJson());
		this.saveAccountInfo(recInfo.getSubAccNo(), recInfo.toJson());

		Double[] ret = new Double[2];
		ret[0] = new Double( payInfo.getAmount() );
		ret[1] = new Double( recInfo.getAmount() );
		
		return ret;
	}
	
	/**
	 * 入金
	 * @param tran
	 * @return	余额
	 */
	public Double transferIn(AccountReceive rec) {
		BankAccountInfo recInfo = BankAccountInfo.fromJson( this.getAccountInfo(rec.getSubAccNo()) );
		MainAccountInfo mainInfo = MainAccountInfo.fromJson( this.getMainAccInfo(recInfo.getMainAccNo()) );
		
		double amount = rec.getTranAmt();
		
		recInfo.setAmount( recInfo.getAmount() + amount );
		mainInfo.setAmount( mainInfo.getAmount() + amount );

		/* 保存信息 */
		this.saveMainAccInfo(mainInfo.getMainAccount(), mainInfo.toJson());
		this.saveAccountInfo(recInfo.getSubAccNo(), recInfo.toJson());
		
		return recInfo.getAmount();
	}
	
	/**
	 * 出金
	 * @param tran
	 * @return
	 */
	public Double transferOut(AccountPay pay) {		
		BankAccountInfo payInfo = BankAccountInfo.fromJson( this.getAccountInfo(pay.getAccountNo()) );
		MainAccountInfo mainInfo = MainAccountInfo.fromJson( this.getMainAccInfo(payInfo.getMainAccNo()) );
		
		double amount = pay.getTranAmt();
		
		if ( payInfo.getAmount() < amount ) {
			return null;
		}
		
		payInfo.setAmount( payInfo.getAmount() - amount );
		mainInfo.setAmount( mainInfo.getAmount() - amount );

		/* 保存信息 */
		this.saveMainAccInfo(mainInfo.getMainAccount(), mainInfo.toJson());
		this.saveAccountInfo(payInfo.getSubAccNo(), payInfo.toJson());
		
		return payInfo.getAmount();
	}
}

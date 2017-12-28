package com.semsun.redis;

import java.util.ArrayList;
import java.util.List;

import com.semsun.bank.entity.account.AccountJournal;
import com.semsun.bank.entity.account.AccountJournalParam;
import com.semsun.bank.entity.define.DealType;
import com.semsun.bank.entity.define.RedisPrefix;
import com.semsun.bank.entity.io.AccountPay;
import com.semsun.bank.entity.io.AccountReceive;
import com.semsun.bank.entity.io.Transfer;

public class JournalProfit {

	private static JournalProfit instance = null;
	
	public static JournalProfit getInstance() {
		if( null == instance ) {
			instance = new JournalProfit();
		}
		
		return instance;
	}
	
	/**
	 * 增加流水
	 * @param journal
	 */
	public void addJournal( AccountPay data, double amount ) {
		AccountJournal journal = new AccountJournal();
		
		journal.setSubAccNo( data.getAccountNo() );
		journal.setTRANTYPE( String.valueOf(DealType.OuterTran.getCode()) );
		journal.setTRANDATE( data.getPreDate() );
		journal.setTRANTIME( data.getPreTime() );
		journal.setHOSTFLW( "" );
		journal.setHOSTSEQ( data.getClientID() );
		journal.setOPPACCNO( data.getRecvAccNo() );
		journal.setOPPACCNAME( data.getRecvAccNm() );
		journal.setOPPBRANCHNAME( data.getRecvBankNm() );
		journal.setCDFG( "D" );
		journal.setTRANAMT( data.getTranAmt() );
		journal.setACCBAL( amount );
		journal.setXTSFAM( 0 );
		journal.setRESUME( data.getMemo() );
		journal.setOPPBANKNO( data.getRecvTgfi() );
		
		this.addJournal(journal);
	}
	
	/**
	 * 增加流水
	 * @param journal
	 */
	public void addJournal( AccountReceive data, double amount ) {
		AccountJournal journal = new AccountJournal();
		
		journal.setSubAccNo( data.getSubAccNo() );
		journal.setTRANTYPE( String.valueOf(DealType.PublicIn.getCode()) );
//		journal.setTRANDATE( data.getPreDate() );
//		journal.setTRANTIME( data.getPreTime() );
		journal.setHOSTFLW( "" );
		journal.setHOSTSEQ( data.getClientID() );
		journal.setOPPACCNO( data.getAccountNo() );
//		journal.setOPPACCNAME( data.getRecvAccNm() );
//		journal.setOPPBRANCHNAME( data.getRecvBankNm() );
		journal.setCDFG( "D" );
		journal.setTRANAMT( data.getTranAmt() );
		journal.setACCBAL( amount );
		journal.setXTSFAM( 0 );
		journal.setRESUME( data.getMemo() );
//		journal.setOPPBANKNO( data.getRecvTgfi() );
		
		this.addJournal(journal);
	}
	
	/**
	 * 增加流水
	 * @param journal
	 */
	public void addInJournal( Transfer data, double amount ) {

		AccountJournal journal = new AccountJournal();
		
		journal.setSubAccNo( data.getRecvAccNo() );
		journal.setTRANTYPE( String.valueOf(DealType.InnerTran.getCode()) );
//		journal.setTRANDATE( data.getPreDate() );
//		journal.setTRANTIME( data.getPreTime() );
		journal.setHOSTFLW( "" );
		journal.setHOSTSEQ( data.getClientID() );
		journal.setOPPACCNO( data.getPayAccNo() );
//		journal.setOPPACCNAME( data.getRecvAccNm() );
//		journal.setOPPBRANCHNAME( data.getRecvBankNm() );
		journal.setCDFG( "D" );
		journal.setTRANAMT( data.getTranAmt() );
		journal.setACCBAL( amount );
		journal.setXTSFAM( 0 );
		journal.setRESUME( data.getMemo() );
//		journal.setOPPBANKNO( data.getRecvTgfi() );
		
		this.addJournal(journal);
	}
	
	/**
	 * 增加流水
	 * @param journal
	 */
	public void addOutJournal( Transfer data, double amount ) {

		AccountJournal journal = new AccountJournal();
		
		journal.setSubAccNo( data.getPayAccNo() );
		journal.setTRANTYPE( String.valueOf(DealType.InnerTran.getCode()) );
//		journal.setTRANDATE( data.getPreDate() );
//		journal.setTRANTIME( data.getPreTime() );
		journal.setHOSTFLW( "" );
		journal.setHOSTSEQ( data.getClientID() );
		journal.setOPPACCNO( data.getRecvAccNo() );
		journal.setOPPACCNAME( data.getRecvAccNm() );
//		journal.setOPPBRANCHNAME( data.getRecvBankNm() );
		journal.setCDFG( "D" );
		journal.setTRANAMT( data.getTranAmt() );
		journal.setACCBAL( amount );
		journal.setXTSFAM( 0 );
		journal.setRESUME( data.getMemo() );
//		journal.setOPPBANKNO( data.getRecvTgfi() );
		
		this.addJournal(journal);
	}
	
	/**
	 * 增加流水
	 * @param journal
	 */
	public void addJournal( AccountJournal journal ) {
		String key = String.format("%s_%s", RedisPrefix.JOURNAL_PREFIX , journal.getSubAccNo() );
		RedisList list = new RedisList( key );
		
		list.add( journal.toJson() );
	}
	
	/**
	 * 查询流水列表
	 * @param param
	 * @return
	 */
	public List<AccountJournal> queryJournal( AccountJournalParam param ) {
		String key = String.format("%s_%s", RedisPrefix.JOURNAL_PREFIX , param.getSubAccNo() );
		
		RedisList list = new RedisList( key );
		
		int pasgeSize = param.getPageNumber() > 10 ? 10 : param.getPageNumber();
		
		List<String> jsonList = list.getList(pasgeSize, 1);
		
		List<AccountJournal> retList = new ArrayList<AccountJournal>();
		for( String json : jsonList ) {
			retList.add( AccountJournal.fromJson(json) );
		}
		
		return retList;
	}
}

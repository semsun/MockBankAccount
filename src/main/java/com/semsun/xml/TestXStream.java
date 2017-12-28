package com.semsun.xml;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.stream.StreamResult;

import org.xml.sax.InputSource;

import com.semsun.bank.entity.account.BankAccountInfo;
import com.semsun.bank.entity.account.ContactInfo;
import com.semsun.bank.entity.account.ContactInfoList;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("stream")
public class TestXStream {
	
	private String name;
	
	private int age;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}
	
	public static String formatXml(String xml){
      try{
         Transformer serializer= SAXTransformerFactory.newInstance().newTransformer();
//         serializer.setOutputProperty(OutputKeys.INDENT, "yes");
//         serializer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
         serializer.setOutputProperty("encoding", "GBK");
         Source xmlSource=new SAXSource(new InputSource(new ByteArrayInputStream(xml.getBytes())));
         StreamResult res =  new StreamResult(new ByteArrayOutputStream());
         serializer.transform(xmlSource, res);
         return new String(((ByteArrayOutputStream)res.getOutputStream()).toByteArray());
      }catch(Exception e){         
         return xml;
      }
   }
	
	public static void main(String args[]) {
		TestXStream  test = new TestXStream();
		
		test.setName("TTT");
		test.setAge(12);
		
		XStream xStream = new XStream();
		xStream.autodetectAnnotations(true);
		xStream.setMode(XStream.NO_REFERENCES);
		
		//注册使用了注解的VO
//		xStream.processAnnotations(new Class[]{TestXStream.class});
		
		String xml = xStream.toXML(test);
		
		System.out.println(formatXml(xml));
		
		BankAccountInfo info = new BankAccountInfo();
		info.setAccGenType("111");
		info.setAccType("222");
		info.setAutoAssignInterestFlag("3333");
		
		List<ContactInfo> list = new ArrayList<ContactInfo>();
		ContactInfoList contactList = new ContactInfoList();
		ContactInfo contart = new ContactInfo();
		contart.setContactName("1111");
		contart.setContactPhone("22222");
		contart.setMailAddress("33333");
		
		list.add(contart);contart = new ContactInfo();
		contart.setContactName("1111");
		contart.setContactPhone("22222");
		contart.setMailAddress("33333");
		
		list.add(contart);contart = new ContactInfo();
		contart.setContactName("1111");
		contart.setContactPhone("22222");
		contart.setMailAddress("33333");
		
		list.add(contart);
		contactList.setList(list);
		
		info.setVilcstDataList(contactList);
		
		xml = xStream.toXML(info);
		
		System.out.println(formatXml(xml));

		System.out.println(TestXStream.class.getClassLoader().getResource("xmls/in.xml"));
		
		BankAccountInfo readInfo = (BankAccountInfo) xStream.fromXML(TestXStream.class.getClassLoader().getResource("xmls/in.xml"));
//		String xmlStr="<?xml version=\"1.0\" encoding=\"GBK\"?><stream><action>DLFONDIN</action><userName>111</userName><clientID>2222</clientID><accountNo>3333</accountNo><subAccNo>4444</subAccNo><subAccNm>5555</subAccNm><tranAmt>6666</tranAmt><memo>777</memo></stream>";
//		xStream.processAnnotations(new Class[]{AccountReceive.class});
//		AccountReceive readInfo = (AccountReceive)xStream.fromXML(xmlStr);
		
		System.out.println();
		
//		XStream xjson = new XStream(new JettisonMappedXmlDriver());
//		xjson.setMode(XStream.NO_REFERENCES);
//		xjson.alias("BankAccountInfo", BankAccountInfo.class);
//		xjson.alias("list", ContactInfoList.class);
//		xjson.alias("row", ContactInfo.class);
//		
//		String jsonStr = xjson.toXML(readInfo);
//		System.out.println(jsonStr);
		
//		BankAccountInfo jsonInfo = JSONObject.parseObject(jsonStr, BankAccountInfo.class);
		Map<String, Class> alias = new HashMap<String, Class>();
		alias.put("BankAccountInfo", BankAccountInfo.class);
		alias.put("VilcstDataList", ContactInfoList.class);
		alias.put("row", ContactInfo.class);
		String jsonStr = XmlJsonUtil.toJson(readInfo, alias);
		System.out.println(jsonStr);
		BankAccountInfo jsonInfo = (BankAccountInfo)XmlJsonUtil.fromJson(jsonStr, alias, BankAccountInfo.class);
//		AccountReceive jsonInfo = (AccountReceive)XmlJsonUtil.fromJson(jsonStr, null, AccountReceive.class);
	}

}

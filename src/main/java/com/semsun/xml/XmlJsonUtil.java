package com.semsun.xml;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.stream.StreamResult;

import org.xml.sax.InputSource;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.json.JettisonMappedXmlDriver;
import com.thoughtworks.xstream.io.json.JsonHierarchicalStreamDriver;

public class XmlJsonUtil {
	
	public static String formatXml(String xml, String encode){
      try{
         Transformer serializer= SAXTransformerFactory.newInstance().newTransformer();
//         serializer.setOutputProperty(OutputKeys.INDENT, "yes");
//         serializer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
         serializer.setOutputProperty(OutputKeys.ENCODING, encode);
         Source xmlSource=new SAXSource(new InputSource(new ByteArrayInputStream(xml.getBytes())));
         StreamResult res =  new StreamResult(new ByteArrayOutputStream());
         serializer.transform(xmlSource, res);
         return new String(((ByteArrayOutputStream)res.getOutputStream()).toByteArray(), encode);
      }catch(Exception e){         
         return xml;
      }
   }
	
	private static XStream xmlTool = null;
	private static XStream jsonTool = null;
	
	private static XStream getXmlTool() {
		if( null == xmlTool ) {
			xmlTool = new XStream();
			xmlTool.autodetectAnnotations(true);
			xmlTool.setMode(XStream.NO_REFERENCES);
		}
		
		return xmlTool;
	}
	
	private static XStream getJsonTool() {
		if( null == jsonTool ) {
			jsonTool = new XStream(new JettisonMappedXmlDriver());
			jsonTool.setMode(XStream.NO_REFERENCES);
		}
		
		return jsonTool;
	}
	
	public static Object fromXml(String xml, Class type) {
		XStream tool = new XStream();
		tool.autodetectAnnotations(true);
		tool.setMode(XStream.NO_REFERENCES);
		tool.processAnnotations(type);
		
		return tool.fromXML(xml);
	}
	
	public static String toXml(Object obj) {
		XStream tool = getXmlTool();
		tool.processAnnotations(obj.getClass());
		
		return formatXml( tool.toXML(obj), "GBK" );
	}
	
	public static String toJson(Object obj, Map<String, Class> alias) {
		XStream tool = new XStream(new JsonHierarchicalStreamDriver());

		if( null != alias ) {
			for (Map.Entry<String, Class> entry : alias.entrySet()) {
				tool.alias(entry.getKey(), entry.getValue());
			}
		}
		
		return tool.toXML(obj);
	}
	
	public static Object fromJson(String json, Map<String, Class> alias, Class type) {
		XStream tool = getJsonTool();
		tool.processAnnotations(type);

		if( null != alias ) {
			for (Map.Entry<String, Class> entry : alias.entrySet()) {
				tool.alias(entry.getKey(), entry.getValue());
			}
		}
		
		return tool.fromXML(json);
	}
	
	/** 
     * 将对象直接转换成String类型的 XML输出 
     *  
     * @param obj 
     * @return 
     */  
    public static String convertToXml(Object obj) {  
        // 创建输出流  
        StringWriter sw = new StringWriter();  
        try {  
            // 利用jdk中自带的转换类实现  
            JAXBContext context = JAXBContext.newInstance(obj.getClass());  
  
            Marshaller marshaller = context.createMarshaller();  
            // 格式化xml输出的格式  
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT,  
                    Boolean.TRUE);  
            // 将对象转换成输出流形式的xml  
            marshaller.marshal(obj, sw);  
        } catch (JAXBException e) {  
            e.printStackTrace();  
        }  
        return sw.toString();  
    }  
  
    /** 
     * 将对象根据路径转换成xml文件 
     *  
     * @param obj 
     * @param path 
     * @return 
     */  
    public static void convertToXml(Object obj, String path) {  
        try {  
            // 利用jdk中自带的转换类实现  
            JAXBContext context = JAXBContext.newInstance(obj.getClass());  
  
            Marshaller marshaller = context.createMarshaller();  
            // 格式化xml输出的格式  
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT,  
                    Boolean.TRUE);  
            // 将对象转换成输出流形式的xml  
            // 创建输出流  
            FileWriter fw = null;  
            try {  
                fw = new FileWriter(path);  
            } catch (IOException e) {  
                e.printStackTrace();  
            }  
            marshaller.marshal(obj, fw);  
        } catch (JAXBException e) {  
            e.printStackTrace();  
        }  
    }  
  
    @SuppressWarnings("unchecked")  
    /** 
     * 将String类型的xml转换成对象 
     */  
    public static Object convertXmlStrToObject(Class clazz, String xmlStr) {  
        Object xmlObject = null;  
        try {  
            JAXBContext context = JAXBContext.newInstance(clazz);  
            // 进行将Xml转成对象的核心接口  
            Unmarshaller unmarshaller = context.createUnmarshaller();  
            StringReader sr = new StringReader(xmlStr);  
            xmlObject = unmarshaller.unmarshal(sr);  
        } catch (JAXBException e) {  
            e.printStackTrace();  
        }  
        return xmlObject;  
    }  
  
    @SuppressWarnings("unchecked")  
    /** 
     * 将file类型的xml转换成对象 
     */  
    public static Object convertXmlFileToObject(Class clazz, String xmlPath) {  
        Object xmlObject = null;  
        try {  
            JAXBContext context = JAXBContext.newInstance(clazz);  
            Unmarshaller unmarshaller = context.createUnmarshaller();  
            FileReader fr = null;  
            try {  
                fr = new FileReader(xmlPath);  
            } catch (FileNotFoundException e) {  
                e.printStackTrace();  
            }  
            xmlObject = unmarshaller.unmarshal(fr);  
        } catch (JAXBException e) {  
            e.printStackTrace();  
        }  
        return xmlObject;  
    }
}

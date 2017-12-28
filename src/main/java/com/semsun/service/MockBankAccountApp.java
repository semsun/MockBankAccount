package com.semsun.service;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.embedded.EmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//import com.masget.command.utils.ServerParamsUtil;
//import com.masget.global.GlobalContainer;
//import org.springframework.context.support.FileSystemXmlApplicationContext;

@Configuration
@EnableAutoConfiguration
public class MockBankAccountApp {

	static Log log = LogFactory.getLog(MockBankAccountApp.class);  
	
	public static void main(String[] args) {
		
//		ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
//		applicationContext.start();	
//		GlobalContainer.setApplicationContext(applicationContext);
		
//		SpringApplication.run(MockBankAccountApp.class, args);
		SpringApplication app = new SpringApplication(MockBankAccountApp.class);
		app.setWebEnvironment(true);
		Set<Object> set = new HashSet<Object>();  
        set.add("classpath:applicationContext.xml");  
        app.setSources(set);  
        app.run(args);
        
//		ApplicationContext applicationContext = new FileSystemXmlApplicationContext("applicationContext.xml");
//		GlobalContainer.setApplicationContext((ClassPathXmlApplicationContext)applicationContext);
		
//		ServerParamsUtil.init(0);
		log.info("---------------start session-service-provider sucessful---------------------");
		
//		 try {
//				System.in.read();
//			} catch (IOException e) {
//				e.printStackTrace();
//			} 
		
	}
	
//	EmbeddedServletContainerCustomizer
	@Bean
	public EmbeddedServletContainerFactory servletContainer() {
		TomcatEmbeddedServletContainerFactory factory = new TomcatEmbeddedServletContainerFactory();
		factory.setPort(8686);
		factory.setSessionTimeout(10, TimeUnit.MINUTES);
//		factory.addErrorPages( new ErrorPage(HttpStatus.NOT_FOUND, "/notfound.html") );
		return factory;
	}

}

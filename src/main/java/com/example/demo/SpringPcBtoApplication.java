package com.example.demo;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import lombok.RequiredArgsConstructor;

//@Slf4j
@RequiredArgsConstructor
@SpringBootApplication
public class SpringPcBtoApplication implements ApplicationRunner {

	//private final ApplicationContext appContext;
	
	public static void main(String[] args) {
		SpringApplication.run(SpringPcBtoApplication.class, args);
	}

	@Override
	public void run (ApplicationArguments args)throws Exception{
//		var allBeanNames=appContext.getBeanDefinitionNames();
//		
//		for(var beanName:allBeanNames) {
//			log.info("Bean名: {}",beanName);
//			if(beanName.matches("dataLoader")) {
//				//var repository=appContext.getBean(UserMngRepository.class);
//				log.info("dataLoader: {}",appContext.getBean(UserMngRepository.class));
//			}
//		}
	}
}

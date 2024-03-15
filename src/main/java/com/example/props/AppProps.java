package com.example.props;

import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

//we can access the messages directly from yml file also but then we need to read the yml file again & again
//to make the application little faster, we are making this class

@Data//to get setters and getters
@Configuration//to make this class spring bean
@EnableConfigurationProperties //to read the yml file data
@ConfigurationProperties (prefix = "app")//from app u need to read application.yml file (not the whole file)
public class AppProps {
	
	//Map name should be same to what you have in application.yml file i.e., "messages"
	private Map<String, String> messages = new HashMap<>();

}
//Now we need to do @Autowiring this class into those classes of the project where we need the messages
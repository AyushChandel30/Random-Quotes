package com.example.service;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.binding.Quote;
import com.example.props.AppProps;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class DashboardServiceImpl implements DashboardService {

	@Autowired
	private AppProps props;
	
	private Quote[] quotes = null;
	
	Random r = new Random();

	@Override
	public String getQuote() {

		String text = "";
		if (quotes == null) {
			
			String quoteUrl = props.getMessages().get("quoteUrl");
			
			RestTemplate rt = new RestTemplate();
						
			ResponseEntity<String> forEntity = rt.getForEntity(quoteUrl, String.class);
			String body = forEntity.getBody();
			
			//to convert string value to java array object
			ObjectMapper mapper = new ObjectMapper();
			try {
				quotes = mapper.readValue(body, Quote[].class);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			int i = r.nextInt(quotes.length - 1);
			text = quotes[i].getText() + ", said by - " + quotes[i].getAuthor();
		}
		return text;
	}						
}
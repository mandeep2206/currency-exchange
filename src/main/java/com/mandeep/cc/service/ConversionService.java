package com.mandeep.cc.service;

import java.io.IOException;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mandeep.cc.exceptions.CCBadRequest;

@Service
public class ConversionService {

	public double getConversion(String fromCode, String toCode) throws IOException, CCBadRequest {
		Logger logger = LoggerFactory.getLogger(ConversionService.class);

		String uri = "http://www.floatrates.com/daily/" + fromCode + ".json";

		logger.info(uri);

		ObjectMapper mapper = new ObjectMapper();
		URL url = null;

		double conversionRate = 0.0;

		JsonNode root;


		url = new URL(uri);
		root = mapper.readTree(url);
		JsonNode rates = root.path(toCode);
		if(rates.isMissingNode()) {
			throw new CCBadRequest("invalid currency code");
		}
		conversionRate = rates.path("rate").asDouble();



		return conversionRate;
	}

}

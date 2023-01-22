package com.mandeep.cc.service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mandeep.cc.model.Currency;

@Service
public class CountryService {

	public List<Currency> getAllCountryCurrency() throws MalformedURLException, IOException {

		List<Currency> countries = parseCountryDetails();
		return countries;
	}

	private List<Currency> parseCountryDetails() throws MalformedURLException, IOException {

		String uri = "http://www.floatrates.com/daily/usd.json";
		ObjectMapper mapper = new ObjectMapper();
		URL url = null;
		ArrayList<Currency> currencies = new ArrayList<>();
		currencies.add(new Currency("usd", "U.S Dollar"));

		url = new URL(uri);
		JsonNode root = null;
		root = mapper.readTree(url);
		Iterator<Entry<String, JsonNode>> ss = root.fields();
		while (ss.hasNext()) {
			Entry<String, JsonNode> keyValue = ss.next();
			String code = keyValue.getValue().path("code").asText();
			
			String name = keyValue.getValue().path("name").asText();
			

			currencies.add(new Currency(code, name));
		}

		return currencies;
	}

}

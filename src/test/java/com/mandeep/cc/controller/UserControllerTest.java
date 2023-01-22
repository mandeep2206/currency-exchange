package com.mandeep.cc.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;


@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Test
	public void testCountryCurrencyEndpoint_p1() throws Exception {
		mockMvc.perform(get("/v1/currency-codes")).andExpect(status().isOk());
	}

	@Test
	public void testConvertorEndpoint_p1() throws Exception
	{
		mockMvc.perform(MockMvcRequestBuilders.get("/v1/convert/{from}/{to}/{value}", "usd", "inr", 1000))
				.andExpectAll(MockMvcResultMatchers.status().isOk());
	}
	@Test
	public void testConvertorEndpoint_n1() throws Exception
	{
		mockMvc.perform(MockMvcRequestBuilders.get("/v1/convert/{from}/{to}/{value}", "usd", "inr", -1000))
				.andExpectAll(MockMvcResultMatchers.status().isBadRequest());
	}
	@Test
	public void testConvertorEndpoint_n2() throws Exception
	{
		mockMvc.perform(MockMvcRequestBuilders.get("/v1/convert/{from}/{to}/{value}", "usd", "d", 1000))
				.andExpectAll(MockMvcResultMatchers.status().isInternalServerError());
	}
	
	@Test
	public void testConvertorEndpoint_n3() throws Exception
	{
		mockMvc.perform(MockMvcRequestBuilders.get("/v1/convert/{from}/{to}/{value}", "u", "inr", 1000))
				.andExpectAll(MockMvcResultMatchers.status().isInternalServerError());
	}
}

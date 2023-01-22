package com.mandeep.cc.dto;

public record ConversionDataDTO(String sourceCurrency, String targetCurrency, double sourceValue, double targetValue,
		double conversionValue) {

}

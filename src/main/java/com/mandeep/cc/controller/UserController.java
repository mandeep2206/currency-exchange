package com.mandeep.cc.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.mandeep.cc.dto.ConversionDataDTO;
import com.mandeep.cc.dto.CountryCurrencyDTO;
import com.mandeep.cc.exceptions.CCBadRequest;
import com.mandeep.cc.model.Currency;
import com.mandeep.cc.service.ConversionService;
import com.mandeep.cc.service.CountryService;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;



@RestController
public class UserController {
	Logger logger = LoggerFactory.getLogger(UserController.class);

	@Autowired
	private CountryService countryService;

	@Autowired
	private ConversionService conversionService;


	@Operation(summary = "Convert Currency", description = "Convert value given in one currency to value in the other currency")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Success"),
			@ApiResponse(responseCode = "500", description = "Internal Server Error") ,
			@ApiResponse(responseCode = "400", description = "Bad Request")})
	@GetMapping("/v1/convert/{from}/{to}/{value}")
	@ResponseBody
	public ResponseEntity<ConversionDataDTO> getConversion( @PathVariable(name = "from", required = true) String from,
			@PathVariable(name = "to",required = true) String to, @PathVariable(name = "value", required = true) double value)  {

		if (value < 0) {
			return new ResponseEntity<ConversionDataDTO>(new ConversionDataDTO("", "", 0, 0, 0),
					HttpStatus.BAD_REQUEST);
		}

		double conversionRate = 1.0;
		HttpStatus status = HttpStatus.OK;
		try {
			conversionRate = conversionService.getConversion(from.toLowerCase(), to.toLowerCase());
		} catch (IOException | CCBadRequest e) {

			logger.error(e.getMessage());
			from = "";
			to = "";
			value = 0;
			conversionRate = 0.0;
			status = HttpStatus.INTERNAL_SERVER_ERROR;
		}
		ConversionDataDTO returnData = new ConversionDataDTO(from, to, value, conversionRate * value, conversionRate);

		return new ResponseEntity<ConversionDataDTO>(returnData, status);
	}

	@Operation(summary = "Get all Currency Codes", description = "This API returns currency code along with popular currency names")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Success"),
			@ApiResponse(responseCode = "500", description = "Internal Server Error") })
	@GetMapping("/v1/currency-codes")
	@ResponseBody
	public ResponseEntity<List<CountryCurrencyDTO>> getCountryCodes() {

		List<Currency> listCountryCurrency = null;
		ArrayList<CountryCurrencyDTO> listCountries = new ArrayList<>();
		HttpStatus status = HttpStatus.OK;
		try {
			listCountryCurrency = countryService.getAllCountryCurrency();

			for (Currency curr : listCountryCurrency) {
				listCountries.add(new CountryCurrencyDTO(curr.code(), curr.name()));
			}
		} catch (IOException e) {
			e.printStackTrace();
			status = HttpStatus.INTERNAL_SERVER_ERROR;

		}

		return new ResponseEntity<List<CountryCurrencyDTO>>(listCountries, status);
	}

}

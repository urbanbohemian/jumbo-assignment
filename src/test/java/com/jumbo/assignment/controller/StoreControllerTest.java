package com.jumbo.assignment.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jumbo.assignment.domain.entity.Store;
import com.jumbo.assignment.exception.DuplicateEntityException;
import com.jumbo.assignment.exception.EntityNotFoundException;
import com.jumbo.assignment.model.DistanceCalculationType;
import com.jumbo.assignment.model.dto.StoreDto;
import com.jumbo.assignment.model.mapper.StoreMapper;
import com.jumbo.assignment.service.StoreService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(StoreController.class)
class StoreControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private StoreService storeService;

	@MockBean
	private StoreMapper storeMapper;

	private Store store;
	private double latitude;
	private double longitude;
	private int numberOfStores;
	private DistanceCalculationType distanceCalculationType;

	@Autowired
	private ObjectMapper objectMapper;

	@BeforeEach
	public void setup() {
		latitude = 51.778461;
		longitude = 4.615551;
		numberOfStores = 5;
		distanceCalculationType = DistanceCalculationType.VINCENTY;
		store = createStore("test_address", "test_city",1.1,2.2);
	}

	@Test
	void whenGetClosestStoresSuccess() throws Exception {
		MvcResult result = mockMvc
				.perform(MockMvcRequestBuilders.get("/v1/stores/closest?latitude="+latitude+"&longitude="+longitude+"&number_of_stores="+numberOfStores+"&distance_calculation_type="+distanceCalculationType)
						.accept(MediaType.APPLICATION_JSON))
				.andReturn();

		int status = result.getResponse().getStatus();
		assertEquals(HttpStatus.OK.value(), status);

	}

	@Test
	void whenGetClosestStoresInvalidDistanceTypePassedReturnBadRequest() throws Exception {

		String restUrl = "/v1/stores/closest?latitude="+latitude+"&longitude="+longitude+"&number_of_stores="+numberOfStores+"&distance_calculation_type=INVALID";
		invokeGetClosestStoreAndValidateResult(restUrl);
	}

	@Test
	void whenGetClosestStoresMissingLatitudeParameterReturnBadRequest() throws Exception {

		String restUrl = "/v1/stores/closest?&longitude="+longitude+"&number_of_stores="+numberOfStores+"&distance_calculation_type="+distanceCalculationType;
		invokeGetClosestStoreAndValidateResult(restUrl);
	}

	@Test
	void whenGetClosestStoresMissingLongitudeParameterReturnBadRequest() throws Exception {

		String restUrl = "/v1/stores/closest?latitude="+latitude+"&number_of_stores="+numberOfStores+"&distance_calculation_type="+distanceCalculationType;
		invokeGetClosestStoreAndValidateResult(restUrl);
	}

	public void invokeGetClosestStoreAndValidateResult(String restUrl) throws Exception{

		MvcResult result = mockMvc
				.perform(MockMvcRequestBuilders.get(restUrl)
						.accept(MediaType.APPLICATION_JSON))
				.andReturn();

		int status = result.getResponse().getStatus();
		assertEquals(HttpStatus.BAD_REQUEST.value(), status);
		assertEquals("", result.getResponse().getContentAsString());
	}

	@Test
	void whenGetStoresSuccess() throws Exception {
		when(storeService.getAllStores(anyInt(), anyInt(), anyString())).thenReturn(Collections.singletonList(store));

		MvcResult result = mockMvc
				.perform(MockMvcRequestBuilders.get("/v1/stores/closest?latitude=51.778461&longitude=4.615551&number_of_stores=5&distance_calculation_type=VINCENTY")
						.accept(MediaType.APPLICATION_JSON))
				.andReturn();

		int status = result.getResponse().getStatus();
		assertEquals(HttpStatus.OK.value(), status);

		String content = result.getResponse().getContentAsString();
		List<StoreDto> returnedStoreDtoList = objectMapper.readValue(content, objectMapper.getTypeFactory().constructCollectionType(List.class, StoreDto.class));
		assertNotNull(returnedStoreDtoList);
	}

	@Test
	void whenGetStoreByIdNotExistentReturnNotFound() throws Exception {
		when(storeService.getAllStores(anyInt(), anyInt(), anyString())).thenReturn(Collections.singletonList(store));

		MvcResult result = mockMvc
				.perform(MockMvcRequestBuilders.get("/v1/store/1")
						.accept(MediaType.APPLICATION_JSON))
				.andReturn();

		int status = result.getResponse().getStatus();
		assertEquals(HttpStatus.NOT_FOUND.value(), status);
	}

	@Test
	void whenCreateStoreDuplicateEntityReturnBadRequest() throws Exception {

		Mockito.when(storeService.createStore(Mockito.any())).thenThrow(new DuplicateEntityException("Duplicate entity"));
		MvcResult result = mockMvc.perform(
				MockMvcRequestBuilders.post("/v1/stores")
						.accept(MediaType.APPLICATION_JSON)
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andReturn();
		assertEquals( "", result.getResponse().getContentAsString());
	}


	@Test
	void whenUpdateStoreDuplicateEntityReturnBadRequest() throws Exception {

		Mockito.when(storeService.createStore(Mockito.any())).thenThrow(new DuplicateEntityException("Duplicate entity"));
		MvcResult result = mockMvc.perform(
				MockMvcRequestBuilders.put("/v1/stores/158")
						.accept(MediaType.APPLICATION_JSON)
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andReturn();
		assertEquals("", result.getResponse().getContentAsString());

	}

	@Test
	void whenUpdateStoreEntityNotFoundReturnBadRequest() throws Exception {

		Mockito.when(storeService.createStore(Mockito.any())).thenThrow(new EntityNotFoundException("Entity not found"));
		MvcResult result = mockMvc.perform(
				MockMvcRequestBuilders.put("/v1/stores/503")
						.accept(MediaType.APPLICATION_JSON)
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andReturn();
		assertEquals("", result.getResponse().getContentAsString());

	}

	@Test
	void whenDeleteStoreEntityNotFoundReturnBadRequest() throws Exception {

		Mockito.doThrow(new EntityNotFoundException("Entity Not found to be deleted")).when(storeService).deleteStore(anyLong());
		MvcResult result = mockMvc.perform(
				MockMvcRequestBuilders.delete("/v1/stores/503")
						.accept(MediaType.APPLICATION_JSON)
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound())
				.andReturn();
		assertEquals("", result.getResponse().getContentAsString());
	}

	@Test
	void whenDeleteStoreEntitySuccess() throws Exception {

		MvcResult result = mockMvc.perform(
				MockMvcRequestBuilders.delete("/v1/stores/1")
						.accept(MediaType.APPLICATION_JSON)
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andReturn();
		assertEquals("", result.getResponse().getContentAsString());

	}

	public Store createStore(String address, String city, Double latitude, Double longitude) {
		Store store = new Store();
		store.setAddressName(address);
		store.setCity(city);
		store.setLatitude(latitude);
		store.setLongitude(longitude);
		return store;
	}

}

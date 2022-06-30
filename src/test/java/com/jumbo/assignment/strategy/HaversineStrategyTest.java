package com.jumbo.assignment.strategy;

import com.jumbo.assignment.domain.entity.Store;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class HaversineStrategyTest {

	@InjectMocks
	private HaversineStrategy haversineStrategy;

	@Test
	void calculateShouldReturnCorrectLatitudeAndLongitude() {

		Store store = new Store();
		store.setLongitude(4.615551);
		store.setLatitude(51.778461);
		ReflectionTestUtils.setField(haversineStrategy, "earthRadius", 6371.13);
		double latitude = 52.150632;
		double longitude = 4.661277;
		double distance = haversineStrategy.calculateDistance(store, latitude, longitude);

		assertEquals(41.5028, distance ,0.000001);
	}
}

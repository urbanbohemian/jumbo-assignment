package com.jumbo.assignment.strategy;

import com.jumbo.assignment.domain.entity.Store;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class VincentStrategyTest {

	@InjectMocks
	@Spy
	private VincentyStrategy vincentStrategy;

	@Test
	void calculateShouldReturnCorrectLatitudeAndLongitude() {

		Store store = new Store();
		store.setLongitude(4.615551);
		store.setLatitude(51.778461);
		double latitude = 52.150632;
		double longitude = 4.661277;
		double distance = vincentStrategy.calculateDistance(store, latitude, longitude);

		assertEquals(41.5293,distance, 0.000001);
	}
}

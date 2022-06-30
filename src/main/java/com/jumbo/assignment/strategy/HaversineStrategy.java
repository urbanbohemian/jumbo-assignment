package com.jumbo.assignment.strategy;

import com.jumbo.assignment.domain.entity.Store;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class HaversineStrategy implements DistanceStrategy{

	@Value("${distance.scale}")
	private int distanceScale;

	@Value("${distance.scale}")
	private double earthRadius;

	@Override
	public double calculateDistance(Store store, double startLatitude, double startLongitude) {
		double endLatitude = store.getLatitude();
		double endLongitude = store.getLongitude();

		double dLat  = Math.toRadians((endLatitude - startLatitude));
		double dLong = Math.toRadians((endLongitude - startLongitude));

		startLatitude = Math.toRadians(startLatitude);
		endLatitude   = Math.toRadians(endLatitude);

		double a = haversin(dLat) + Math.cos(startLatitude) * Math.cos(endLatitude) * haversin(dLong);
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

		double s =  earthRadius * c; // <-- d
		BigDecimal distanceBd = BigDecimal.valueOf(s);
		distanceBd = distanceBd.setScale(4,BigDecimal.ROUND_HALF_UP);

		return distanceBd.doubleValue();
	}

	public static double haversin(double val) {
		return Math.pow(Math.sin(val / 2), 2);
	}
}

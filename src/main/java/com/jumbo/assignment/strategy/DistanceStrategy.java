package com.jumbo.assignment.strategy;


import com.jumbo.assignment.domain.entity.Store;

public interface DistanceStrategy {

	double calculateDistance(Store store, double startLatitude, double startLongitude);

}

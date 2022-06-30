package com.jumbo.assignment.model;

public enum DistanceCalculationType {
	HAVERSINE("haversineStrategy"), VINCENTY("vincentyStrategy");

	public final String strategyName;

	private DistanceCalculationType(String strategyName) {
		this.strategyName = strategyName;
	}
}

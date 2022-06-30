package com.jumbo.assignment.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StoreDto {

	private Long id;

	@NotNull
	private String city;

	@NotNull
	private String postalCode;

	@NotNull
	private String street;

	@NotNull
	private String street2;

	private String street3;

	@NotNull
	private String addressName;

	@NotNull
	private String uuid;

	@NotNull
	@DecimalMin("-180") @DecimalMax("80.0")
	private Double longitude;

	@NotNull
	@DecimalMin("-90") @DecimalMax("90.0")
	private Double latitude;

	private Double distance;

	@NotNull
	private Integer complexNumber;

	@NotNull
	private String showWarningMessage;

	@NotNull
	private String locationType;

	private String collectionPoint;

	@NotNull
	private Integer sapStoreId;

	@NotNull
	private String todayOpen;

	@NotNull
	private String todayClose;
}

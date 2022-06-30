package com.jumbo.assignment.domain.entity;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "store",	uniqueConstraints = {@UniqueConstraint(columnNames = {"latitude", "longitude"})})
@AllArgsConstructor
@NoArgsConstructor
public class Store extends AuditableEntity {

	@Id
	@SequenceGenerator(name = "seq_store_generator", allocationSize = 1, sequenceName = "seq_store")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_store_generator")
	private Long id;

	@Column(nullable = false)
	@NotNull(message = "City can not be null!")
	private String city;

	@Column(name = "postal_code", nullable = false)
	@NotNull(message = "PostalCode can not be null!")
	private String postalCode;

	@Column(nullable = false)
	@NotNull(message = "Street can not be null!")
	private String street;

	@Column(nullable = false)
	@NotNull(message = "Street2 can not be null!")
	private String street2;

	private String street3;

	@Column(name="address_name", nullable = false)
	@NotNull(message = "AddressName can not be null!")
	private String addressName;

	@Column(nullable = false)
	@NotNull(message = "UUID can not be null!")
	private String uuid;

	@Column(nullable = false)
	@NotNull(message = "Longitude can not be null!")
	private Double longitude;

	@Column(nullable = false)
	@NotNull(message = "Latitude can not be null!")
	private Double latitude;

	@Column(name = "complex_number", nullable = false)
	@NotNull(message = "ComplexNumber can not be null!")
	private Integer complexNumber;

	@Column(name = "show_warning_message", nullable = false)
	@NotNull(message = "ShowWarningMessage can not be null!")
	private String showWarningMessage;

	@Column(name = "today_open", nullable = false)
	private String todayOpen;

	@Column(name = "location_type", nullable = false)
	private String locationType;

	@Column(name = "collection_point")
	private String collectionPoint;

	@Column(name = "sap_store_id", nullable = false)
	private Integer sapStoreId;

	@Column(name = "today_close", nullable = false)
	private String todayClose;

	@Transient
	private Double distance;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getStreet2() {
		return street2;
	}

	public void setStreet2(String street2) {
		this.street2 = street2;
	}

	public String getStreet3() {
		return street3;
	}

	public void setStreet3(String street3) {
		this.street3 = street3;
	}

	public String getAddressName() {
		return addressName;
	}

	public void setAddressName(String addressName) {
		this.addressName = addressName;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public Integer getComplexNumber() {
		return complexNumber;
	}

	public void setComplexNumber(Integer complexNumber) {
		this.complexNumber = complexNumber;
	}

	public String getShowWarningMessage() {
		return showWarningMessage;
	}

	public void setShowWarningMessage(String showWarningMessage) {
		this.showWarningMessage = showWarningMessage;
	}

	public String getTodayOpen() {
		return todayOpen;
	}

	public void setTodayOpen(String todayOpen) {
		this.todayOpen = todayOpen;
	}

	public String getLocationType() {
		return locationType;
	}

	public void setLocationType(String locationType) {
		this.locationType = locationType;
	}

	public String getCollectionPoint() {
		return collectionPoint;
	}

	public void setCollectionPoint(String collectionPoint) {
		this.collectionPoint = collectionPoint;
	}

	public Integer getSapStoreId() {
		return sapStoreId;
	}

	public void setSapStoreId(Integer sapStoreId) {
		this.sapStoreId = sapStoreId;
	}

	public String getTodayClose() {
		return todayClose;
	}

	public void setTodayClose(String todayClose) {
		this.todayClose = todayClose;
	}


	public Double getDistance() {
		return distance;
	}

	public void setDistance(Double distance) {
		this.distance = distance;
	}


}

package com.jumbo.assignment.model.mapper;

import com.jumbo.assignment.model.dto.StoreDto;
import com.jumbo.assignment.domain.entity.Store;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StoreMapper {

	public StoreDto toStoreDto(Store store) {
		return StoreDto.builder()
				.id(store.getId())
				.addressName(store.getAddressName())
				.city(store.getCity())
				.collectionPoint(store.getCollectionPoint())
				.complexNumber(store.getComplexNumber())
				.distance(store.getDistance())
				.latitude(store.getLatitude())
				.longitude(store.getLongitude())
				.locationType(store.getLocationType())
				.postalCode(store.getPostalCode())
				.sapStoreId(store.getSapStoreId())
				.showWarningMessage(store.getShowWarningMessage())
				.street(store.getStreet())
				.street2(store.getStreet2())
				.street3(store.getStreet3())
				.todayClose(store.getTodayClose())
				.todayOpen(store.getTodayOpen())
				.uuid(store.getUuid())
				.build();
	}

	public Store toStore(StoreDto storeDto) {

		if(storeDto == null) {
			return null;
		}

		Store store = new Store();
		store = provisionDtoFields(store, storeDto);

		return store;
	}

	public Store provisionDtoFields(Store store, StoreDto storeDto) {
		store.setAddressName(storeDto.getAddressName());
		store.setCity(storeDto.getCity());
		store.setCollectionPoint(storeDto.getCollectionPoint());
		store.setComplexNumber(storeDto.getComplexNumber());
		store.setDistance(storeDto.getDistance());
		store.setLatitude(storeDto.getLatitude());
		store.setLongitude(storeDto.getLongitude());
		store.setLocationType(storeDto.getLocationType());
		store.setPostalCode(storeDto.getPostalCode());
		store.setSapStoreId(storeDto.getSapStoreId());
		store.setShowWarningMessage(storeDto.getShowWarningMessage());
		store.setStreet(storeDto.getStreet());
		store.setStreet2(storeDto.getStreet2());
		store.setStreet3(storeDto.getStreet3());
		store.setTodayClose(storeDto.getTodayClose());
		store.setTodayOpen(storeDto.getTodayOpen());
		store.setUuid(storeDto.getUuid());

		return store;

	}

}

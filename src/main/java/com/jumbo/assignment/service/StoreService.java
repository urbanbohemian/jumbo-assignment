package com.jumbo.assignment.service;

import com.jumbo.assignment.exception.EntityNotFoundException;
import com.jumbo.assignment.repository.StoreRepository;
import com.jumbo.assignment.strategy.DistanceStrategy;
import com.jumbo.assignment.strategy.HaversineStrategy;
import com.jumbo.assignment.domain.entity.Store;
import com.jumbo.assignment.exception.DuplicateEntityException;
import com.jumbo.assignment.model.DistanceCalculationType;
import com.jumbo.assignment.model.dto.StoreDto;
import com.jumbo.assignment.model.mapper.StoreMapper;
import com.jumbo.assignment.strategy.VincentyStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StoreService {

    private final StoreRepository storeRepository;
    private final StoreMapper storeMapper;
    private final Map<String, DistanceStrategy> distanceStrategyMap;

    @Transactional
    public List<Store> getClosestStores(double latitude, double longitude, int numberOfStores, DistanceCalculationType distanceCalculationType) {
        return storeRepository.findAll().stream().sorted(Comparator.comparingDouble(o -> distanceStrategyMap.get(distanceCalculationType.strategyName).calculateDistance(o, latitude, longitude)))
                .limit(numberOfStores)
                .toList();
    }

    @Transactional
    public List<Store> getAllStores(Integer page, Integer size, String sort) {
    	Pageable paging = PageRequest.of(page, size, Sort.by(sort));
        return storeRepository.findAll(paging).getContent();
    }

    @Transactional
    public Store getStore(long id) {
        return findById(id);
    }

    @Transactional
    public Store createStore(StoreDto storeDto) {
		Optional<Store> existingStore = storeRepository.findByAddressNameOrLatitudeAndLongitude(storeDto.getAddressName(), storeDto.getLatitude(), storeDto.getLongitude());

		if(existingStore.isPresent()) {
			throw new DuplicateEntityException("Duplicate entity for store-id"+storeDto.getId());
		}

        Store store = storeMapper.toStore(storeDto);
        return  storeRepository.save(store);
    }

    @Transactional
    public Store updateStore(Long id ,StoreDto storeDto) {
        Store store = findById(id);
		Optional<Store> existingStore = storeRepository.findByAddressNameAndIdNotOrLatitudeAndLongitudeAndIdNot(storeDto.getAddressName(), id, storeDto.getLatitude(), storeDto.getLongitude(), id);

		if(existingStore.isPresent()) {
			throw new DuplicateEntityException("Duplicate entity for store-id"+storeDto.getId());
		}

		store = storeMapper.provisionDtoFields(store, storeDto);
        return storeRepository.save(store);
    }

    @Transactional
    public void deleteStore(Long id) {
        Store store = findById(id);
        storeRepository.delete(store);
    }

    public Store findById(Long id) {
        return storeRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Could not find store with id: " + id));
    }

    @Bean
    public Map<String, DistanceStrategy> distanceStrategyMap(){
        Map<String, DistanceStrategy>  distanceMap = new HashMap<>();
        distanceMap.put("haversine", new HaversineStrategy());
        distanceMap.put("vincenty", new VincentyStrategy());
        return distanceMap;
    }

}

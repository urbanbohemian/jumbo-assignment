package com.jumbo.assignment.service;

import com.jumbo.assignment.domain.entity.Store;
import com.jumbo.assignment.exception.DuplicateEntityException;
import com.jumbo.assignment.exception.EntityNotFoundException;
import com.jumbo.assignment.model.DistanceCalculationType;
import com.jumbo.assignment.model.dto.StoreDto;
import com.jumbo.assignment.model.mapper.StoreMapper;
import com.jumbo.assignment.repository.StoreRepository;
import com.jumbo.assignment.strategy.DistanceStrategy;
import com.jumbo.assignment.strategy.HaversineStrategy;
import com.jumbo.assignment.strategy.VincentyStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class StoreServiceTest  {
    @InjectMocks
    private StoreService storeService;

    @Mock
    private StoreRepository storeRepository;

    @Mock
    private StoreMapper storeMapper;

    @Mock
    private Map<String, DistanceStrategy> distanceStrategyMap;

    private Store store;
    private StoreDto storeDto;
    private ArrayList<Store> storeList;

    @BeforeEach
    public void init()  {
        storeList = new ArrayList<>();
        store = createStore("test_address", "test_city",1.1,2.2);
        storeDto = createStoreDto( "test_address", "test_city",1.1,2.2);
        Store store1 = createStore("test_address_1","test_city_2",3.3,4.4);
        Store store2 = createStore("test_address_2","test_city_2",3.3,4.4);
        storeList.add(store1);
        storeList.add(store2);
    }

    @Test
    void getClosestStoresShouldReturnSpecifiedNumberOfStoresWithVincentStrategy() {
          when(storeRepository.findAll()).thenReturn(storeList);
          when(distanceStrategyMap.get(anyString())).thenReturn(new VincentyStrategy());
          List<Store> storeList = storeService.getClosestStores(1.1,2.2,2, DistanceCalculationType.VINCENTY);
          assertNotNull(storeList);
          assertEquals(2, storeList.size());
    }

    @Test
    void getClosestStoresShouldReturnSpecifiedNumberOfStoresWithHaversineStrategy() {
        when(storeRepository.findAll()).thenReturn(storeList);
        when(distanceStrategyMap.get(anyString())).thenReturn(new HaversineStrategy());
        List<Store> storeList = storeService.getClosestStores(1.1,2.2,2, DistanceCalculationType.HAVERSINE);
        assertNotNull(storeList);
        assertEquals(2, storeList.size());
    }

    @Test
    void getClosestStoresShouldReturnEmptyWhenThereIsNoStore() {
        when(storeRepository.findAll()).thenReturn(List.of());
        List<Store> storeList = storeService.getClosestStores(1.1,2.2,2, DistanceCalculationType.HAVERSINE);
        assertNotNull(storeList);
        assertEquals(0, storeList.size());
    }

    @Test
    void getAllStoresShouldReturnBySpecifiedPageParams() {
        Pageable pageable = PageRequest.of(1, 2, Sort.by("id"));
        PageImpl pagedResponse = new PageImpl(storeList);

        when(storeRepository.findAll(pageable)).thenReturn(pagedResponse);

        List<Store> storeList = storeService.getAllStores(1,2,"id");
        assertNotNull(storeList);
        assertEquals(2, storeList.size());
    }

    @Test
    void getAllStoresShouldReturnEmptyWhenThereIsNoStore() {
        when(storeRepository.findAll()).thenReturn(Arrays.asList());
        List<Store> storeList = storeService.getClosestStores(1.1,2.2,2, DistanceCalculationType.HAVERSINE);
        assertNotNull(storeList);
        assertEquals(0, storeList.size());
    }

    @Test
    void getStoreShouldThrowEntityNotFoundExceptionWhenStoreIsNotFound() {
        when(storeRepository.findById(anyLong())).thenReturn(Optional.empty());
        final Throwable throwable = catchThrowable(() -> storeService.findById(1L));
        assertThat(throwable).isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void getStoreShouldReturnStore() {
        when(storeRepository.findById(anyLong())).thenReturn(Optional.of(store));
        Store returnedStore  = storeService.findById(1L);
        assertEquals(returnedStore, store);
    }

    @Test
    void createStoreShouldSuccessfullySaveStore() {
        Store storeToBeCreated = store;
        when(storeRepository.findByAddressNameOrLatitudeAndLongitude(storeDto.getAddressName(), storeDto.getLatitude(), storeDto.getLongitude())).thenReturn(Optional.empty());
        when(storeMapper.toStore(any(StoreDto.class))).thenReturn(storeToBeCreated);
        when(storeRepository.save(any(Store.class))).thenReturn(storeToBeCreated);

        Store store = storeService.createStore(storeDto);

        assertNotNull(store);
        verify(storeRepository, times(1)).save(any(Store.class));
    }

    @Test()
    void createStoreShouldThrowExceptionWhenThereIsStoreWithSameAddressOrLatitudeAndLongitude() {
        Store storeToBeCreated = store;
        when(storeRepository.findByAddressNameOrLatitudeAndLongitude(storeDto.getAddressName(), storeDto.getLatitude(), storeDto.getLongitude())).thenReturn(Optional.of(store));

        final Throwable throwable = catchThrowable(() ->storeService.createStore(storeDto));

        assertThat(throwable).isInstanceOf(DuplicateEntityException.class);
        assertNotNull(store);
        verify(storeRepository, times(0)).save(any(Store.class));
    }

    @Test
    void updateStoreShouldSuccessfullyUpdateStore() {

        Store storeToBeUpdated = store;
        storeDto.setAddressName("updated_address");
        when(storeRepository.findById(anyLong())).thenReturn(Optional.of(store));
        when(storeMapper.provisionDtoFields(any(Store.class), any(StoreDto.class))).thenReturn(storeToBeUpdated);
        when(storeRepository.save(any(Store.class))).thenReturn(storeToBeUpdated);

        Store store = storeService.updateStore(anyLong(), storeDto);

        assertNotNull(store);
        verify(storeRepository, times(1)).save(any(Store.class));
    }

    @Test
    void updateStoreShouldThrowExceptionWhenThereIsNoStoreWithId() {
        when(storeRepository.findById(anyLong())).thenReturn(Optional.empty());
        final Throwable throwable = catchThrowable(() ->storeService.updateStore(anyLong(),storeDto));
        assertThat(throwable).isInstanceOf(EntityNotFoundException.class);
        verify(storeRepository, times(0)).save(any(Store.class));
    }

    @Test
    void updateStoreShouldThrowExceptionWhenThereIsStoreWithSameAddressOrLatitudeAndLongitudeIdNot() {
        when(storeRepository.findById(anyLong())).thenReturn(Optional.of(store));
        when(storeRepository.findByAddressNameAndIdNotOrLatitudeAndLongitudeAndIdNot(anyString(), anyLong(), anyDouble(), anyDouble(), anyLong())).thenReturn(Optional.of(store));
        final Throwable throwable = catchThrowable(() ->storeService.updateStore(anyLong(),storeDto));
        assertThat(throwable).isInstanceOf(DuplicateEntityException.class);
        verify(storeRepository, times(0)).save(any(Store.class));
    }

    @Test()
    void deleteStoreShouldSuccess() {
        Long id = 1L;
        when(storeRepository.findById(id)).thenReturn(Optional.of(store));
        storeService.deleteStore(id);
        verify(storeRepository, times(1)).delete(any(Store.class));
    }

    public Store createStore(String address, String city, Double latitude, Double longitude) {
        Store store = new Store();
        store.setAddressName(address);
        store.setCity(city);
        store.setLatitude(latitude);
        store.setLongitude(longitude);
        return store;
    }

    public StoreDto createStoreDto(String address, String city, Double latitude, Double longitude) {
        StoreDto storeDto = new StoreDto();
        storeDto.setAddressName(address);
        storeDto.setCity(city);
        storeDto.setLatitude(latitude);
        storeDto.setLongitude(longitude);
        return storeDto;
    }
}

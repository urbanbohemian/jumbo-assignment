package com.jumbo.assignment.controller;

import com.jumbo.assignment.domain.entity.Store;
import com.jumbo.assignment.model.DistanceCalculationType;
import com.jumbo.assignment.model.dto.StoreDto;
import com.jumbo.assignment.model.mapper.StoreMapper;
import com.jumbo.assignment.service.StoreService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("v1/stores")
@RequiredArgsConstructor
public class StoreController {

    private final StoreService storeService;
    private final StoreMapper storeMapper;

    @GetMapping("/closest")
    public List<StoreDto> getClosestStores(@RequestParam double latitude,
                                           @RequestParam double longitude,
                                           @RequestParam(value = "number_of_stores", defaultValue = "5") @Min(1) @Max(600) Integer numberOfStores,
                                           @RequestParam(value = "distance_calculation_type", defaultValue = "VINCENTY") DistanceCalculationType distanceCalculationType) {
        List<Store> storeList = storeService.getClosestStores(latitude, longitude, numberOfStores, distanceCalculationType);

        return storeList.stream()
                .map(storeMapper::toStoreDto)
                .toList();
    }

	@GetMapping
	public List<StoreDto> getStores(@RequestParam(defaultValue = "0") Integer page,
									@RequestParam(defaultValue = "10") Integer size,
									@RequestParam(defaultValue = "id") String sort) {
        List<Store> storeList = storeService.getAllStores(page, size, sort);

        return storeList.stream()
                .map(storeMapper::toStoreDto)
                .toList();
    }

    @GetMapping("/{id}")
    public StoreDto getStore(@PathVariable long id) {
        Store store = storeService.getStore(id);
        return storeMapper.toStoreDto(store);
    }

    @PostMapping
    public StoreDto createStore(@RequestBody @Valid StoreDto storeDto) {
        Store store = storeService.createStore(storeDto);
        return storeMapper.toStoreDto(store);
    }

    @PutMapping("/{id}")
    public StoreDto updateStore(@PathVariable("id") Long id, @RequestBody @Valid StoreDto storeDto) {
        Store store = storeService.updateStore(id, storeDto);
        return storeMapper.toStoreDto(store);
    }

    @DeleteMapping("/{id}")
    public void deleteStore(@PathVariable long id) {
        storeService.deleteStore(id);
    }
}

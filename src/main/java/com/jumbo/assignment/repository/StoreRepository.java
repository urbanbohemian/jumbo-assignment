package com.jumbo.assignment.repository;

import com.jumbo.assignment.domain.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StoreRepository extends JpaRepository<Store, Long> {

    Optional<Store> findByAddressNameOrLatitudeAndLongitude(String addressName, Double latitude, Double longitude);

    Optional<Store> findByAddressNameAndIdNotOrLatitudeAndLongitudeAndIdNot(String addressName, Long idForAddress, Double latitude, Double longitude, Long idForLocation);


}

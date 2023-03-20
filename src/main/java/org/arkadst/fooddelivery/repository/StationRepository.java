package org.arkadst.fooddelivery.repository;

import org.arkadst.fooddelivery.model.Station;
import org.arkadst.fooddelivery.model.StationId;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StationRepository extends CrudRepository<Station, StationId> {
}

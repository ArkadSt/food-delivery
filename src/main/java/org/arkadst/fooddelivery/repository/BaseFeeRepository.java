package org.arkadst.fooddelivery.repository;

import org.arkadst.fooddelivery.model.BaseFee;
import org.arkadst.fooddelivery.model.BaseFeeId;
import org.springframework.data.repository.CrudRepository;

public interface BaseFeeRepository extends CrudRepository<BaseFee, BaseFeeId> {
}

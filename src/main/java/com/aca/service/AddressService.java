package com.aca.service;

import com.aca.db.entity.Address;
import com.aca.db.entity.User;

import java.util.Collection;
import java.util.Optional;

public interface AddressService {

    User getById(Long id);

    Optional<Address> findById(Long id);

    Collection<Address> findAll();

    Long create(AddressCreationParameter creationParameter);
}

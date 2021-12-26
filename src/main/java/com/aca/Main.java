package com.aca;

import com.aca.db.connectionpool.ConnectionPool;
import com.aca.db.initializer.DBInitializer;
import com.aca.db.initializer.DefaultDBInitializer;
import com.aca.service.*;

public class Main {

    public static void main(String[] args) {

        DBInitializer initializer = new DefaultDBInitializer();
        ConnectionPool connectionPool = initializer.initialize();

        UserService userService = new DefaultUserService(connectionPool);
        AddressService addressService = new DefaultAddressService(connectionPool);

        Long userId = userService.create(new UserCreationParameter("Saro", "Qaramyan", "amail.ru", 21));

        Long addressId = addressService.create(new AddressCreationParameter(userId, "France", "Paris"));

        System.out.println(addressId);
    }
}

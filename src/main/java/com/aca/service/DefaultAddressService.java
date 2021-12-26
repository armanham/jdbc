package com.aca.service;

import com.aca.db.connectionpool.ConnectionPool;
import com.aca.db.entity.Address;
import com.aca.db.entity.User;
import com.aca.utils.DBUtils;
import com.aca.utils.IOUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

public class DefaultAddressService implements AddressService {

    private static final String FIND_BY_ID_SQL = "select * from addresses where id=?";
    private static final String CREATE_ADDRESS_SQL = "insert into addresses(id, country, city) values(?,?,?)";
    private static final String CREATE_USER_ADDRESS_SQL = "insert into user_addresses(id, user_id, address_id) values(?,?,?)";

    private final ConnectionPool connectionPool;

    private final AtomicLong addressIdGenerator;

    private final AtomicLong userAddressIdGenerator;

    public DefaultAddressService(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
        this.addressIdGenerator = new AtomicLong(getMaxIdAddress());
        this.userAddressIdGenerator = new AtomicLong(getMaxIdUsers());
    }

    private Long getMaxIdAddress() {
        Statement statement = null;
        ResultSet resultSet = null;

        Connection connection = null;
        try {
            connection = connectionPool.get();

            statement = connection.createStatement();
            resultSet = statement.executeQuery("select max(id) from addresses");

            return resultSet.next() ? resultSet.getLong(1) : 0L;
        } catch (SQLException ex) {
            throw new IllegalStateException("Find maxId is failed.", ex);
        } finally {
            if (connection != null) {
                connectionPool.release(connection);
            }
            IOUtils.closeAll(statement, resultSet);
        }
    }

    private Long getMaxIdUsers() {
        Statement statement = null;
        ResultSet resultSet = null;

        Connection connection = null;
        try {
            connection = connectionPool.get();

            statement = connection.createStatement();
            resultSet = statement.executeQuery("select max(id) from users");

            return resultSet.next() ? resultSet.getLong(1) : 0L;
        } catch (SQLException ex) {
            throw new IllegalStateException("Find maxId is failed.", ex);
        } finally {
            if (connection != null) {
                connectionPool.release(connection);
            }
            IOUtils.closeAll(statement, resultSet);
        }
    }


    @Override
    public User getById(Long id) {
        return null;
    }

    public Optional<Address> findById(Long id) {
        Connection connection = null;

        ResultSet resultSet = null;
        PreparedStatement statement = null;

        try {
            connection = connectionPool.get();

            statement = connection.prepareStatement(FIND_BY_ID_SQL);
            statement.setLong(1, id);

            resultSet = statement.executeQuery();

            return resultSet.next()
                    ? Optional.of(new Address(resultSet))
                    : Optional.empty();
        } catch (SQLException ex) {
            throw new IllegalStateException("Find address by id failed.", ex);
        } finally {
            if (connection != null) {
                connectionPool.release(connection);
            }
            IOUtils.closeAll(resultSet, statement);
        }
    }

    @Override
    public Collection<Address> findAll() {
        Connection connection = null;

        ResultSet resultSet = null;
        PreparedStatement statement = null;

        try {
            connection = connectionPool.get();

            statement = connection.prepareStatement("select * from addresses");
            List<Address> addresses = new ArrayList<>();
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                addresses.add(new Address(resultSet));
            }
            return addresses;
        } catch (SQLException ex) {
            throw new IllegalStateException("Find addresses failed");
        } finally {
            if (connection != null) {
                connectionPool.release(connection);
            }
            IOUtils.closeAll(statement, resultSet);
        }
    }

    @Override
    public Long create(AddressCreationParameter parameter) {
        Connection connection = null;

        try {
            connection = connectionPool.get();
            connection.setAutoCommit(false);

            Long addressId = createAddress(parameter, connection);

            connectUserWithAddress(parameter.getUserId(), addressId, connection);

            connection.commit();
            return addressId;
        } catch (Exception ex) {
            DBUtils.rollback(connection);
            throw new IllegalStateException("Create/connect address failed.", ex);
        } finally {
            if (connection != null) {
                connectionPool.release(connection);
            }
        }
    }

    private Long createAddress(AddressCreationParameter parameter, Connection connection) {
        PreparedStatement statement = null;

        Long generatedId = addressIdGenerator.incrementAndGet();

        try {
            statement = connection.prepareStatement(CREATE_ADDRESS_SQL);

            statement.setLong(1, generatedId);
            statement.setString(2, parameter.getCountry());
            statement.setString(3, parameter.getCity());
            statement.execute();
            return generatedId;
        } catch (SQLException ex) {
            throw new IllegalStateException("Address creation failed.", ex);
        } finally {
            IOUtils.closeAll(statement);
        }
    }

    private void connectUserWithAddress(Long userId, Long addressId, Connection connection) {
        PreparedStatement statement = null;

        try {
            statement = connection.prepareStatement(CREATE_USER_ADDRESS_SQL);

            Long userAddressId = userAddressIdGenerator.incrementAndGet();
            statement.setLong(1, userAddressId);
            statement.setLong(2, userId);
            statement.setLong(3, addressId);

            statement.execute();
        } catch (SQLException ex) {
            throw new IllegalStateException("UserAddress creation failed", ex);
        } finally {
            IOUtils.closeAll(statement);
        }
    }
}

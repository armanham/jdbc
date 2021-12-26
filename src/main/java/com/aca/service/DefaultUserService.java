package com.aca.service;

import com.aca.db.connectionpool.ConnectionPool;
import com.aca.db.entity.User;
import com.aca.db.exeption.EmailAlreadyExistsException;
import com.aca.db.exeption.UserNotFoundExeption;
import com.aca.utils.IOUtils;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

public class DefaultUserService implements UserService {

    private static final String FIND_BY_ID_SQL = "select * from users where id=?";

    private static final String CREATE_SQL = "insert into users(id, name, surname, age, email, created_on, active) values(?,?,?,?,?,?,?)";

    private final AtomicLong idGenerator;

    private final ConnectionPool connectionPool;

    public DefaultUserService(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
        this.idGenerator = new AtomicLong(getMaxId());
    }

//    private Long getAddressOfUser(Long id, ConnectionPool connectionPool) {
//        DefaultAddressService defaultAddressService;
//        Optional<Address> addresses = new DefaultAddressService(connectionPool).findById(id);
//        Optional<User> users = new DefaultUserService(connectionPool).findById(id);
//
//        Map<User, Address> userAddress = new HashMap<>();
//        userAddress.
//
//        return null;
//    }

    @Override
    public User getById(Long id) {
        return findById(id).orElseThrow(() -> new UserNotFoundExeption(id));
    }

    @Override
    public Optional<User> findById(final Long id) {
        ResultSet resultSet = null;
        PreparedStatement statement = null;

        Connection connection = null;
        try {
            connection = connectionPool.get();

            statement = connection.prepareStatement(FIND_BY_ID_SQL);
            statement.setLong(1, id);

            resultSet = statement.executeQuery();

            return resultSet.next() ? Optional.of(new User(resultSet)) : Optional.empty();
        } catch (SQLException ex) {
            throw new IllegalStateException("Find user by id failed.", ex);
        } finally {
            if (connection != null) {
                connectionPool.release(connection);
            }
            IOUtils.closeAll(statement, resultSet);
        }
    }

    @Override
    public Collection<User> findAllActiveUsers() {
        ResultSet resultSet = null;
        PreparedStatement statement = null;

        Connection connection = null;
        try {
            connection = connectionPool.get();

            statement = connection.prepareStatement("select * from users where active=true");
            resultSet = statement.executeQuery();

            List<User> users = new ArrayList<>();
            while (resultSet.next()) {
                users.add(new User(resultSet));
            }
            return users;
        } catch (SQLException ex) {
            throw new IllegalStateException("Find active users failed.", ex);
        } finally {
            if (connection != null) {
                connectionPool.release(connection);
            }
            IOUtils.closeAll(statement, resultSet);
        }
    }


    @Override
    public Collection<User> findAll() {
        Statement statement = null;
        ResultSet resultSet = null;

        Connection connection = null;
        try {
            connection = connectionPool.get();

            statement = connection.createStatement();
            resultSet = statement.executeQuery("select * from users");

            List<User> users = new ArrayList<>();
            while (resultSet.next()) {
                users.add(new User(resultSet));
            }
            return users;
        } catch (SQLException ex) {
            throw new IllegalStateException("Find all users failed.", ex);
        } finally {
            if (connection != null) {
                connectionPool.release(connection);
            }
            IOUtils.closeAll(statement, resultSet);
        }
    }

    @Override
    public Long create(UserCreationParameter parameter) {
        PreparedStatement statement = null;

        Connection connection = null;
        try {
            connection = connectionPool.get();

            if (checkIfAlreadyExists(parameter.getEmail(), connection)) {
                throw new EmailAlreadyExistsException(parameter.getEmail());
            }
            statement = connection.prepareStatement(CREATE_SQL);

            long generatedId = idGenerator.incrementAndGet();

            statement.setLong(1, generatedId);
            statement.setString(2, parameter.getName());
            statement.setString(3, parameter.getSurname());
            statement.setInt(4, parameter.getAge());
            statement.setString(5, parameter.getEmail());
            statement.setTimestamp(6, Timestamp.valueOf(LocalDateTime.now()));
            statement.setBoolean(7, Boolean.TRUE);

            statement.execute();

            return generatedId;
        } catch (SQLException ex) {
            throw new IllegalStateException("User creation failed.", ex);
        } finally {
            if (connection != null) {
                connectionPool.release(connection);
            }
            IOUtils.closeAll(statement);
        }
    }

    private boolean checkIfAlreadyExists(String email, Connection connection) {
        ResultSet resultSet = null;
        PreparedStatement statement = null;
        try {
            statement = connection.prepareStatement("select count(*) from users where email=?");
            statement.setString(1, email);

            resultSet = statement.executeQuery();

            return resultSet.next() && resultSet.getLong(1) > 0;
        } catch (SQLException ex) {
            throw new IllegalStateException("Checking emails failed.", ex);
        } finally {
            IOUtils.closeAll(statement, resultSet);
        }
    }

    private Long getMaxId() {
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
}

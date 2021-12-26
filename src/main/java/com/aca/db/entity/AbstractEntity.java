package com.aca.db.entity;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

public abstract class AbstractEntity {

    private final Long id;

    private final LocalDateTime createdOn;

    private LocalDateTime updatedOn;

    public AbstractEntity(ResultSet resultSet) throws SQLException {
        this.id = resultSet.getLong("id");
        this.createdOn = resultSet.getTimestamp("created_on").toLocalDateTime();

        Timestamp updatedOn = resultSet.getTimestamp("updated_on");
        if (updatedOn != null){
            this.updatedOn = updatedOn.toLocalDateTime();
        }
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedOn() {
        return createdOn;
    }

    public LocalDateTime getUpdatedOn() {
        return updatedOn;
    }
}

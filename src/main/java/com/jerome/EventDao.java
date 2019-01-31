package com.jerome;

import com.jerome.exceptions.DatabaseException;
import com.jerome.models.EventAlert;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.UUID;

import static com.jerome.jooq.tables.Events.EVENTS;

class EventDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(EventDao.class);
    private static final String DATABASE_NAME = "eventsDB";

    private HikariDataSource hikariDatasource;

    EventDao() {
        setupHikariConnectionPool();
        createEventsTableIfNotExist();
    }

    void saveEventAlert(EventAlert eventAlert) {
        try (Connection connection = hikariDatasource.getConnection()) {
            doSaveEventAlert(DSL.using(connection), eventAlert);
        } catch (SQLException e) {
            LOGGER.error("There was an error trying to save an EventAlert", e);
            throw new DatabaseException(e);
        }
    }

    private void doSaveEventAlert(DSLContext dslContext, EventAlert eventAlert) {
        dslContext.insertInto(EVENTS)
                .set(EVENTS.ID, UUID.randomUUID())
                .set(EVENTS.EVENT_ID, eventAlert.getId())
                .set(EVENTS.DURATION, eventAlert.getDuration().intValue())
                .set(EVENTS.TYPE, eventAlert.getType().name())
                .set(EVENTS.HOST, eventAlert.getHost())
                .set(EVENTS.ALERT, eventAlert.getAlert())
                .execute();
    }

    private void setupHikariConnectionPool() {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl("jdbc:hsqldb:file:hsqldb/" + DATABASE_NAME);
        hikariConfig.setUsername("SA");
        hikariConfig.setPassword("");
        hikariConfig.setMaximumPoolSize(10);
        hikariConfig.setMinimumIdle(2);
        hikariConfig.addDataSourceProperty("cachePrepStmts", "true");
        hikariConfig.addDataSourceProperty("prepStmtCacheSize", "250");
        hikariConfig.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        hikariDatasource = new HikariDataSource(hikariConfig);
    }

    private void createEventsTableIfNotExist() {
        try (Connection connection = hikariDatasource.getConnection()) {
            doCreateEventsTableIfNotExist(DSL.using(connection));
        } catch (SQLException e) {
            LOGGER.error("There was an error trying to create the Events table", e);
            throw new DatabaseException(e);
        }
    }

    private void doCreateEventsTableIfNotExist(DSLContext dslContext) {
        dslContext.createTableIfNotExists("EVENTS")
                .column("ID", SQLDataType.UUID.nullable(false))
                .column("EVENT_ID", SQLDataType.VARCHAR(255).nullable(false))
                .column("DURATION", SQLDataType.INTEGER.nullable(false))
                .column("TYPE", SQLDataType.VARCHAR(255))
                .column("HOST", SQLDataType.VARCHAR(255))
                .column("ALERT", SQLDataType.BOOLEAN.nullable(false))
                .execute();
    }

}

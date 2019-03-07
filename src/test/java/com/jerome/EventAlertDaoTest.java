package com.jerome;

import com.jerome.jooq.tables.pojos.EventAlert;
import com.jerome.models.EventLog;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.commons.io.FileUtils;
import org.jooq.impl.DSL;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.UUID;

import static com.jerome.jooq.tables.EventAlert.EVENT_ALERT;
import static junit.framework.TestCase.assertTrue;
import static org.jooq.impl.DSL.count;
import static org.powermock.api.mockito.PowerMockito.whenNew;

@RunWith(PowerMockRunner.class)
@PrepareForTest(EventAlertDao.class)
public class EventAlertDaoTest {

    private static final String DATABASE_PATH = "hsqldb/test";
    private static final String DATABASE_NAME = "test";

    private HikariDataSource hikariDatasource;
    private EventAlertDao eventAlertDao;

    public EventAlertDaoTest() {
        // Restore database in order to work with a cleaned database
        restoreInitialDatabaseState();
    }

    @Before
    public void setUp() throws Exception {
        setupHikariDatasourceForTesting();
        whenNew(HikariDataSource.class).withAnyArguments().thenReturn(hikariDatasource);
        eventAlertDao = new EventAlertDao();
    }

    private void restoreInitialDatabaseState() {
        try {
            FileUtils.deleteDirectory(new File(DATABASE_PATH));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setupHikariDatasourceForTesting() {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl("jdbc:hsqldb:file:" + DATABASE_PATH + "/" + DATABASE_NAME);
        hikariConfig.setUsername("testing");
        hikariConfig.setPassword("");
        hikariDatasource = new HikariDataSource(hikariConfig);
    }

    @Test
    public void shouldCreateEventsTableIfNotExistWhenEventDaoIsCreated() throws SQLException {
        int exists = DSL.using(hikariDatasource.getConnection())
                .select(count())
                .from("information_schema.tables")
                .where("table_name = 'EVENT_ALERT'")
                .fetchOne(0, Integer.class);

        assertTrue(exists > 0);
    }

    @Test
    public void shouldSaveEventsIntoTheDatabase() throws SQLException {
        EventAlert eventAlert = new EventAlert(
                UUID.randomUUID(),
                "This_event_is_an_alert",
                5,
                EventLog.Type.APPLICATION_LOG.name(),
                "myhost",
                false
        );
        eventAlertDao.saveEventAlert(eventAlert);
        EventAlert eventAlertRetrieved = DSL.using(hikariDatasource.getConnection())
                .selectFrom(EVENT_ALERT)
                .fetchOneInto(EventAlert.class);

        assertTrue(bothEventAlertsAreEqual(eventAlert, eventAlertRetrieved));
    }

    private boolean bothEventAlertsAreEqual(EventAlert eventAlert, EventAlert eventAlertRetrieved) {
        return eventAlert.getId().equals(eventAlertRetrieved.getId())
                && eventAlert.getEventId().equals(eventAlertRetrieved.getEventId())
                && eventAlert.getDuration().equals(eventAlertRetrieved.getDuration())
                && eventAlert.getType().equals(eventAlertRetrieved.getType())
                && eventAlert.getHost().equals(eventAlertRetrieved.getHost())
                && eventAlert.getAlert().equals(eventAlertRetrieved.getAlert());
    }
}

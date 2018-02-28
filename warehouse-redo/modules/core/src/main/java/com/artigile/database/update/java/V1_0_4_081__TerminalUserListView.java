package com.artigile.database.update.java;

import com.googlecode.flyway.core.migration.java.JavaMigration;
import com.mysql.jdbc.exceptions.jdbc4.MySQLSyntaxErrorException;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * @author Valery Barysok, 2013-07-25
 */
public class V1_0_4_081__TerminalUserListView implements JavaMigration {

    private static final String USER = "userlist";

    private static final String PASSWORD = "{61B3E12B-3586-3A58-A497-7ED7C4C794B9}";

    private static final String CREATE_READONLY_USER = "create user '" + USER + "'@'%' identified by '" + PASSWORD + "'";

    private static final String GRANT_SELECT_PRIVILEGES = "grant select on user_list to '" + USER + "'@'%'";

    private static final String FLUSH_PRIVILEGES = "flush privileges";

    @Override
    public void migrate(JdbcTemplate jdbcTemplate) throws Exception {
        try {
            jdbcTemplate.getDataSource().getConnection(USER, PASSWORD);
            jdbcTemplate.batchUpdate(new String[] {GRANT_SELECT_PRIVILEGES, FLUSH_PRIVILEGES});
        } catch (MySQLSyntaxErrorException e) {
            jdbcTemplate.batchUpdate(new String[] {GRANT_SELECT_PRIVILEGES, FLUSH_PRIVILEGES});
        } catch (Exception e) {
            jdbcTemplate.batchUpdate(new String[] {CREATE_READONLY_USER, GRANT_SELECT_PRIVILEGES, FLUSH_PRIVILEGES});
        }
    }
}

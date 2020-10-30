package database;

public class MySQLDBUtil {
    private static final String INSTANCE = "***.rds.amazonaws.com";
    private static final String PORT_NUM = "3306";
    public static final String DB_NAME = "***";
    private static final String USERNAME = "admin";
    private static final String PASSWORD = "****";
    public static final String URL = "jdbc:mysql://"
            + INSTANCE + ":" + PORT_NUM + "/" + DB_NAME
            + "?user=" + USERNAME + "&password=" + PASSWORD
            + "&autoReconnect=true&serverTimezone=UTC";
    public static final String URLpart = "jdbc:mysql://"
            + INSTANCE + ":" + PORT_NUM + "/" + DB_NAME;

    public static final String urlLocal = "jdbc:mysql://localhost/jobs";
}

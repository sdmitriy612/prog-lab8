package ru.sdmitriy612.database;

import ru.sdmitriy612.utils.EnvironmentVariableNotFoundException;

import java.sql.*;

public class DatabaseManager {

    public static Connection getConnection() throws EnvironmentVariableNotFoundException, SQLException {
//        String db_url = System.getenv("DB_URL");
//        String db_user = System.getenv("DB_USER");
//        String db_pwd = System.getenv("DB_PASSWORD");

//        if (db_url == null) throw new EnvironmentVariableNotFoundException("DB_URL not set");
//        if (db_user == null) throw new EnvironmentVariableNotFoundException("DB_USER not set");
//        if (db_pwd == null) throw new EnvironmentVariableNotFoundException("DB_PASSWORD not set");

        return DriverManager.getConnection("jdbc:postgresql://localhost:5432/studs", "s467353", "01xm0luOMDDs7E2e");
    }

}

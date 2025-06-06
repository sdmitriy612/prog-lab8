package ru.sdmitriy612.database;

import java.sql.ResultSet;
import java.sql.SQLException;

public class JdbcUtils {
    public static String getNullableString(ResultSet rs, String column) throws SQLException {
        String value = rs.getString(column);
        return rs.wasNull() ? null : value;
    }

    public static Long getNullableLong(ResultSet rs, String column) throws SQLException {
        long value = rs.getLong(column);
        return rs.wasNull() ? null : value;
    }

}

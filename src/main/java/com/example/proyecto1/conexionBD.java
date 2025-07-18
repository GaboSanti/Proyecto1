package com.example.proyecto1;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;
import java.util.Properties;
import java.sql.SQLException;


public class conexionBD {
    private static final String DB_URL =
            "jdbc:oracle:thin:@proyectodb_high?TNS_ADMIN=C:\\Users\\Mario\\IdeaProjects\\Proyecto1\\src\\wallet";
    private static final String USER = "ADMIN";
    private static final String PASSWORD = "ProyectoIntegrador1IRD";

    public static Connection getConnection() throws SQLException {
        Properties props = new Properties();
        props.setProperty("user", USER);
        props.setProperty("password", PASSWORD);
        return DriverManager.getConnection(DB_URL, props);
    }
}

//hola
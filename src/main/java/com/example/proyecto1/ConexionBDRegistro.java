package com.example.proyecto1;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class ConexionBDRegistro {

     // Ruta donde tienes el wallet de Oracle (con tnsnames.ora, sqlnet.ora, etc.)
        private static final String UBICACION_WALLET = "C:\\Users\\ACER\\Desktop\\Proyecto1\\src\\wallet";

        // Cadena JDBC usando el alias TNS definido en tnsnames.ora
        private static final String JDBC_URL = "jdbc:oracle:thin:@proyecto1_high";

        // Usuario y contraseña de tu Autonomous Database
        private static final String USERNAME_BD = "ADMIN";
        private static final String PASSWORD_BD = "ProyectoIntegrador1IRD";

        static {
            try {
                // Carga el driver JDBC de Oracle.
                Class.forName("oracle.jdbc.driver.OracleDriver");
                // Configura la propiedad del sistema para que Oracle JDBC use el TNS admin en la carpeta del wallet.
                System.setProperty("oracle.net.tns_admin", UBICACION_WALLET);
            } catch (ClassNotFoundException e) {
                System.err.println("Error: No se pudo conectar con la base de datos");
                e.printStackTrace();
            }
        }

        public static Connection getConnection() throws SQLException {
            return DriverManager.getConnection(JDBC_URL, USERNAME_BD, PASSWORD_BD);
        }

        public static void closeConnection(Connection conn) {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    System.err.println("Error al cerrar la conexión a la base de datos: " + e.getMessage());
                }
            }
        }
}

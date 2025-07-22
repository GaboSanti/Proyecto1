package com.example.proyecto1;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;




public class ConexionBDRegistro {

     // Ruta donde se tiene el wallet  en lugar de utilizar la ruta se utilizo que se guardara en el variable de usuario
    //para que asi el archivo se pudiera utilizar para todos
     private static final String UBICACION_WALLET = System.getenv("ORACLE_WALLET_PATH");

        // Cadena JDBC usando el alias TNS definido en tnsnames.ora
        private static final String JDBC_URL = "jdbc:oracle:thin:@proyecto1_high";

        // Usuario y contraseña de la Base de Datos
        private static final String USERNAME_BD = "ADMIN";
        private static final String PASSWORD_BD = "ProyectoIntegrador1IRD";

        static {//se utiliza para mantener lista la BD durante toda la aplicacion
            try {

                // **Validación importante:** Verificar si la variable de entorno se cargó correctamente.
                if (UBICACION_WALLET == null || UBICACION_WALLET.isEmpty()) {
                    throw new IllegalStateException("Error: La variable de entorno 'ORACLE_WALLET_PATH' no está configurada. Por favor, configúrela con la ruta completa a su carpeta wallet de Oracle.");
                }

                // Carga el driver JDBC de Oracle.
                Class.forName("oracle.jdbc.driver.OracleDriver");
                // Configura la propiedad del sistema para que Oracle JDBC use el TNS admin en la carpeta del wallet.
                System.setProperty("oracle.net.tns_admin", UBICACION_WALLET);

            } catch (ClassNotFoundException e) {
                System.err.println("Error: No se pudo conectar con la base de datos");//Mensaje de error
                e.printStackTrace();
            }
        }

        public static Connection getConnection() throws SQLException {
            return DriverManager.getConnection(JDBC_URL, USERNAME_BD, PASSWORD_BD);
            //se le pasa el jdbc, el usuario y la contraseña
        }

        public static void closeConnection(Connection conn) {//metodo accesible desde cualquier otra clase
            if (conn != null) {//se verivica que la conexion no sea nula
                try {
                    conn.close();//cerrar conexion de la base de datos
                } catch (SQLException e) {
                    System.err.println("Error al cerrar la conexión a la base de datos: " + e.getMessage());
                    //mensaje de error
                }
            }
        }
}

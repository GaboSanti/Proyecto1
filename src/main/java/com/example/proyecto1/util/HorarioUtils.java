package com.example.proyecto1.util;

public class HorarioUtils {
    /**
     * Convierte siglas de día a ID (como los que están en la base de datos).
     * Ejemplo: "LU" → 1
     */
    public static int diaToId(String sigla) {
        return switch (sigla) {
            case "LU" -> 1;
            case "MA" -> 2;
            case "MI" -> 3;
            case "JU" -> 4;
            case "VI" -> 5;
            default -> 0; // 0 si no encuentra coincidencia
        };
    }

    /**
     * Convierte ID de día a sigla.
     * Ejemplo: 1 → "LU"
     */
    public static String idToDiaSigla(int id) {
        return switch (id) {
            case 1 -> "LU";
            case 2 -> "MA";
            case 3 -> "MI";
            case 4 -> "JU";
            case 5 -> "VI";
            default -> "";
        };
    }

    /**
     * Convierte hora (entero) a ID de intervalo.
     * Ejemplo: 7 → 1, 13 → 7
     */
    public static int horaToIntervaloId(int hora) {
        return switch (hora) {
            case 7 -> 1;
            case 8 -> 2;
            case 9 -> 3;
            case 10 -> 4;
            case 11 -> 5;
            case 12 -> 6;
            case 13 -> 7;
            case 14 -> 8;
            case 15 -> 9;
            case 16 -> 10;
            case 17 -> 11;
            case 18 -> 12;
            case 19 -> 13;
            case 20 -> 14;
            case 21 -> 15;
            default -> 0;
        };
    }

    /**
     * Convierte ID de intervalo a hora (inicio del intervalo).
     * Ejemplo: 1 → 7, 7 → 13
     */
    public static int intervaloIdToHora(int idIntervalo) {
        return switch (idIntervalo) {
            case 1 -> 7;
            case 2 -> 8;
            case 3 -> 9;
            case 4 -> 10;
            case 5 -> 11;
            case 6 -> 12;
            case 7 -> 13;
            case 8 -> 14;
            case 9 -> 15;
            case 10 -> 16;
            case 11 -> 17;
            case 12 -> 18;
            case 13 -> 19;
            case 14 -> 20;
            case 15 -> 21;
            default -> 0;
        };
    }
}

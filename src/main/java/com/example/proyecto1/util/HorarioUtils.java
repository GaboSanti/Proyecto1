package com.example.proyecto1.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HorarioUtils {

    public static int diaToId(String sigla) {
        return switch (sigla) {
            case "LU" -> 1;
            case "MA" -> 2;
            case "MI" -> 3;
            case "JU" -> 4;
            case "VI" -> 5;
            case "SA" -> 6;
            default -> 0;
        };
    }


    public static String idToDiaSigla(int id) {
        return switch (id) {
            case 1 -> "LU";
            case 2 -> "MA";
            case 3 -> "MI";
            case 4 -> "JU";
            case 5 -> "VI";
            case 6 -> "SA";
            default -> "";
        };
    }


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


    public static String intervaloIdToRango(int idIntervalo) {
        int inicio = intervaloIdToHora(idIntervalo);
        int fin    = intervaloIdToHora(idIntervalo + 1);

        if (inicio == 0) return "";
        if (fin == 0 || fin <= inicio) {
            // fallback simple en caso de que no exista el siguiente intervalo
            fin = inicio + 1;
        }
        return String.format("%02d:00 - %02d:00", inicio, fin);
    }


    public static String reemplazarNumPorHoras(String input) {
        if (input == null || input.isEmpty()) return input;

        Pattern p = Pattern.compile("\\[([^\\]]*)\\]");
        Matcher m = p.matcher(input);
        StringBuffer sb = new StringBuffer();

        while (m.find()) {
            String dentro = m.group(1); // p.ej. "1, 2, 3"
            String[] tokens = dentro.split(",");
            List<String> mapeados = new ArrayList<>();

            for (String t : tokens) {
                String s = t.trim();
                if (s.isEmpty()) continue;

                // Solo convierte si es ENTERO puro
                if (s.matches("\\d+")) {
                    int n = Integer.parseInt(s);
                    String rango = intervaloIdToRango(n); // usa tu mapeo
                    mapeados.add(rango.isEmpty() ? s : rango);
                } else {
                    // ya es un rango u otra cosa → lo dejamos
                    mapeados.add(s);
                }
            }

            String replacement = "[" + String.join(", ", mapeados) + "]";
            m.appendReplacement(sb, Matcher.quoteReplacement(replacement));
        }
        m.appendTail(sb);
        return sb.toString();
    }


}



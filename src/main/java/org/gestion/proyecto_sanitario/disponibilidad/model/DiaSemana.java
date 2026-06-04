package org.gestion.proyecto_sanitario.disponibilidad.model;

import java.time.DayOfWeek;

public enum DiaSemana {
    LUNES, MARTES, MIERCOLES, JUEVES, VIERNES, SABADO, DOMINGO;

    public DayOfWeek toTemporalDayOfWeek() {
        switch (this) {
            case LUNES: return DayOfWeek.MONDAY;
            case MARTES: return DayOfWeek.TUESDAY;
            case MIERCOLES: return DayOfWeek.WEDNESDAY;
            case JUEVES: return DayOfWeek.THURSDAY;
            case VIERNES: return DayOfWeek.FRIDAY;
            case SABADO: return DayOfWeek.SATURDAY;
            case DOMINGO: return DayOfWeek.SUNDAY;
            default: throw new IllegalArgumentException("DiaSemana no reconocido: " + this);
        }
    }

}

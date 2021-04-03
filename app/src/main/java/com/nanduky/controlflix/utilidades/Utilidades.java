package com.nanduky.controlflix.utilidades;

public class  Utilidades {
    //Crear las constantes de los campos de la tabla de fichaMensual
    public static final String TABLA_FICHAMENSUAL = "fichaMensual";
    public static final String CAMPO_ID = "id";
    public static final String CAMPO_FECHA = "fecha";
    public static final String CAMPO_OTRAS_ACTIVIDADES = "otrasActividades";
    public static final String CAMPO_OTRAS_ACTIVIDADES_RETRIBUIDO = "retribuido";
    public static final String CAMPO_OTRAS_ACTIVIDADES_VACAS = "tipoVacas";
    public static final String CAMPO_OTRAS_ACTIVIDADES_BAJA = "tipoBaja";
    public static final String CAMPO_OTRAS_ACTIVIDADES_OTRAS = "tipoOtras";
    public static final String CAMPO_OTRAS_ACTIVIDADES_FECHA_VACAS_INI = "fechaVacasIni";
    public static final String CAMPO_OTRAS_ACTIVIDADES_FECHA_VACAS_FIN = "fechaVacasFin";
    public static final String CAMPO_OTRAS_ACTIVIDADES_FECHA_BAJA_INI = "fechaBajaIni";
    public static final String CAMPO_OTRAS_ACTIVIDADES_FECHA_BAJA_FIN = "fechaBajaFin";
    public static final String CAMPO_OTRAS_ACTIVIDADES_FECHA_OTRAS_INI = "fechaOtrasIni";
    public static final String CAMPO_OTRAS_ACTIVIDADES_FECHA_OTRAS_FIN = "fechaOtrasFin";
    public static final String CAMPO_DIA = "dia";
    public static final String CAMPO_HORA_INI = "horaIni";
    public static final String CAMPO_HORA_FIN = "horaFin";
    public static final String CAMPO_ADR = "adr";
    public static final String CAMPO_TOTAL_HORAS = "totalHoras";
    public static final String CAMPO_DESAYUNO = "desayuno";
    public static final String CAMPO_COMIDA = "comida";
    public static final String CAMPO_CENA = "cena";
    public static final String CAMPO_DORMIR = "dormir";
    public static final String CAMPO_TOTAL_DIETAS = "totalDietas";
    public static final String CAMPO_EXTRAS = "extras";
    public static final String CAMPO_NUM_DIA = "numDia";
    public static final String CAMPO_NUM_MES = "numMes";
    public static final String CAMPO_NUM_YEAR = "numYear";

    //Variable string con la estructura de la tabla para su creación
    public static final String CREAR_TABLA_FICHAMENSUAL = "CREATE TABLE " + TABLA_FICHAMENSUAL
            + " (" + CAMPO_ID +" INTERGER, "
            + CAMPO_FECHA        + " TXT, " + CAMPO_DIA      + " TXT, "
            + CAMPO_OTRAS_ACTIVIDADES + " TXT, "
            + CAMPO_OTRAS_ACTIVIDADES_RETRIBUIDO + " TXT, "
            + CAMPO_HORA_INI     + " TXT, " + CAMPO_HORA_FIN + " TXT, "
            + CAMPO_TOTAL_HORAS  + " TXT, " + CAMPO_ADR      + " TXT, "
            + CAMPO_DESAYUNO     + " TXT, " + CAMPO_COMIDA   + " TXT, "
            + CAMPO_CENA         + " TXT, " + CAMPO_DORMIR   + " TXT, "
            + CAMPO_TOTAL_DIETAS + " TXT, " + CAMPO_EXTRAS   + " TXT, "
            + CAMPO_NUM_DIA + " TXT, " + CAMPO_NUM_MES + " TXT, " + CAMPO_NUM_YEAR + " TXT)";

    //Variable String con tabla version 3
    public static final String CREAR_TABLA_FICHAMENSUAL_V3_1 = "ALTER TABLE " + TABLA_FICHAMENSUAL
            + " ADD COLUMN "
            + CAMPO_OTRAS_ACTIVIDADES + " TXT";
    public static final String CREAR_TABLA_FICHAMENSUAL_V3_2 = "ALTER TABLE " + TABLA_FICHAMENSUAL
            + " ADD COLUMN "
            + CAMPO_OTRAS_ACTIVIDADES_RETRIBUIDO + " TXT";

}



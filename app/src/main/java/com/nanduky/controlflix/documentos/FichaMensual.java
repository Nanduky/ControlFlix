package com.nanduky.controlflix.documentos;

public class FichaMensual {
    //Un identificador por si lo necesito
    private Integer dbId;
    //La fecha y el nombre del dia
    private String dbFecha;
    private String dbDia;
    //Las horas de trabajo
    private String dbHoraIni;
    private String dbHoraFin;
    private String dbTotalHoras;
    //adr
    private String dbADR;
    //La disetas
    private String dbDesayuno;
    private String dbComida;
    private String dbCena;
    private String dbDormir;
    private String dbTotalDietas;
    //Los extras de sabados y domingo
    private String dbExtras;
    //para guardar la fecha por separardo para las consultas de SQLite
    private String dbNumDia;
    private String dbNumMes;
    private String dbNumYear;


    public FichaMensual(Integer dbId,//indentificador
                        String dbFecha, String dbDia,//fechas
                        String dbHoraIni, String dbHoraFin, String dbTotalHoras,//horas
                        String dbADR,//adr
                        String dbDesayuno, String dbComida, String dbCena, //dietas
                        String dbDormir, String dbTotalDietas,//dietas
                        String dbExtras,//extras
                        String dbNumDia, String dbNumMes, String dbNumYear) {//para las consultas a la db
        //identificador
        this.dbId = dbId;
        //fechas
        this.dbFecha = dbFecha;
        this.dbDia = dbDia;
        //horas
        this.dbHoraIni = dbHoraIni;
        this.dbHoraFin = dbHoraFin;
        this.dbTotalHoras = dbTotalHoras;
        //adr
        this.dbADR = dbADR;
        //dietas
        this.dbDesayuno = dbDesayuno;
        this.dbComida = dbComida;
        this.dbCena = dbCena;
        this.dbDormir = dbDormir;
        this.dbTotalDietas = dbTotalDietas;
        //extras
        this.dbExtras = dbExtras;
        //Para las consultas
        this.dbNumDia = dbNumDia;
        this.dbNumMes = dbNumMes;
        this.dbNumYear = dbNumYear;
    }

    public Integer getDbId() {
        return dbId;
    }

    public void setDbId(Integer dbId) {
        this.dbId = dbId;
    }

    public String getDbFecha() {
        return dbFecha;
    }

    public void setDbFecha(String dbFecha) {
        this.dbFecha = dbFecha;
    }

    public String getDbDia() {
        return dbDia;
    }

    public void setDbDia(String dbDia) {
        this.dbDia = dbDia;
    }

    public String getDbHoraIni() {
        return dbHoraIni;
    }

    public void setDbHoraIni(String dbHoraIni) {
        this.dbHoraIni = dbHoraIni;
    }

    public String getDbHoraFin() {
        return dbHoraFin;
    }

    public void setDbHoraFin(String dbHoraFin) {
        this.dbHoraFin = dbHoraFin;
    }

    public String getDbTotalHoras() {
        return dbTotalHoras;
    }

    public void setDbTotalHoras(String dbTotalHoras) {
        this.dbTotalHoras = dbTotalHoras;
    }

    public String getDbADR() {
        return dbADR;
    }

    public void setDbADR(String dbADR) {
        this.dbADR = dbADR;
    }

    public String getDbDesayuno() {
        return dbDesayuno;
    }

    public void setDbDesayuno(String dbDesayuno) {
        this.dbDesayuno = dbDesayuno;
    }

    public String getDbComida() {
        return dbComida;
    }

    public void setDbComida(String dbComida) {
        this.dbComida = dbComida;
    }

    public String getDbCena() {
        return dbCena;
    }

    public void setDbCena(String dbCena) {
        this.dbCena = dbCena;
    }

    public String getDbDormir() {
        return dbDormir;
    }

    public void setDbDormir(String dbDormir) {
        this.dbDormir = dbDormir;
    }

    public String getDbTotalDietas() {
        return dbTotalDietas;
    }

    public void setDbTotalDietas(String dbTotalDietas) {
        this.dbTotalDietas = dbTotalDietas;
    }

    public String getDbExtras() {
        return dbExtras;
    }

    public void setDbExtras(String dbExtras) {
        this.dbExtras = dbExtras;
    }

    public String getDbNumDia() {
        return dbNumDia;
    }

    public void setDbNumDia(String dbNumDia) {
        this.dbNumDia = dbNumDia;
    }

    public String getDbNumMes() {
        return dbNumMes;
    }

    public void setDbNumMes(String dbNumMes) {
        this.dbNumMes = dbNumMes;
    }

    public String getDbNumYear() {
        return dbNumYear;
    }

    public void setDbNumYear(String dbNumYear) {
        this.dbNumYear = dbNumYear;
    }
}

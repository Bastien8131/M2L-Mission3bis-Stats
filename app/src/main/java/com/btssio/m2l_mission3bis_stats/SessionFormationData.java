package com.btssio.m2l_mission3bis_stats;

public class SessionFormationData {

    private int domainId;
    private int formationId;
    private int sessionId;
    private String domainLabel;
    private String formationNom;
    private String sessionDateDebut;
    private String sessionDateFin;


    public SessionFormationData(int domainId, int formationId, int sessionId, String domainLabel, String formationNom, String sessionDateDebut, String sessionDateFin) {
        this.domainId = domainId;
        this.formationId = formationId;
        this.sessionId = sessionId;
        this.domainLabel = domainLabel;
        this.formationNom = formationNom;
        this.sessionDateDebut = sessionDateDebut;
        this.sessionDateFin = sessionDateFin;
    }

    public int getDomainId() {
        return domainId;
    }

    public int getFormationId() {
        return formationId;
    }

    public int getSessionId() {
        return sessionId;
    }

    public String getDomainLabel() {
        return domainLabel;
    }

    public String getFormationNom() {
        return formationNom;
    }

    public String getSessionDateDebut() {
        return sessionDateDebut;
    }

    public String getSessionDateFin() {
        return sessionDateFin;
    }
}

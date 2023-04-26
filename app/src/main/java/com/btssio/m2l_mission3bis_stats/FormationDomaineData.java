package com.btssio.m2l_mission3bis_stats;

public class FormationDomaineData {
    private int domainId;
    private int formationId;
    private String domainLabel;
    private String formationNom;

    public FormationDomaineData(String domainLabel, String formationNom, int domainId, int formationId) {
        this.domainLabel = domainLabel;
        this.formationNom = formationNom;
        this.domainId = domainId;
        this.formationId = formationId;
    }

    public int getDomainId() {
        return domainId;
    }

    public int getFormationId() {
        return formationId;
    }

    public String getDomainLabel() {
        return domainLabel;
    }

    public String getFormationNom() {
        return formationNom;
    }
}


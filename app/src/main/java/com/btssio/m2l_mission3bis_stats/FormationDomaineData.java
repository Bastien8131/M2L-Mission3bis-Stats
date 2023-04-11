package com.btssio.m2l_mission3bis_stats;

public class FormationDomaineData {
    private String domainLabel;
    private String formationNom;

    public FormationDomaineData(String domainLabel, String formationNom) {
        this.domainLabel = domainLabel;
        this.formationNom = formationNom;
    }

    public String getDomainLabel() {
        return domainLabel;
    }

    public String getFormationNom() {
        return formationNom;
    }
}

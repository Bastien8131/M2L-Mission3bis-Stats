package com.btssio.m2l_mission3bis_stats;

public class DomaineData {
    private String labelDomaine;
    private int idDomaine;


    public DomaineData(String labelDomaine, int idDomaine) {
        this.labelDomaine = labelDomaine;
        this.idDomaine = idDomaine;
    }

    public int getDomainId() {
        return idDomaine;
    }

    public String getDomainLabel() {
        return labelDomaine;
    }

}

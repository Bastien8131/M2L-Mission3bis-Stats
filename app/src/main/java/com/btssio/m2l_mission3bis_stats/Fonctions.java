package com.btssio.m2l_mission3bis_stats;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Fonctions {
    public static final String md5(final String s) {
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest
                    .getInstance("MD5");
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < messageDigest.length; i++) {
                String h = Integer.toHexString(0xFF & messageDigest[i]);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static Double pourcentageDe(int quantité, int total){
        double resultat = (quantité / (double) total) * 100;
        //double nbPour100InscFormation = Math.round((nbInscFormation*100) / nbInscTotal);
        resultat = Math.round(resultat * 100.0) / 100.0;
        return resultat;
    }

    public static Double round(double value){
        double resultat = Math.round(value * 100.0) / 100.0;
        return resultat;
    }

    public static String getNoteTextuel(double note) {
        if (note < 0 || note > 5) {
            throw new IllegalArgumentException("La note doit être comprise entre 0 et 5.");
        }

        if (note >= 4.5) {
            return "Très bien";
        } else if (note >= 3.5) {
            return "Bien";
        } else if (note >= 2.5) {
            return "Passable";
        } else {
            return "Insuffisant";
        }
    }

}

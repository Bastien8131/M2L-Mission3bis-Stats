package com.btssio.m2l_mission3bis_stats;

import static com.btssio.m2l_mission3bis_stats.SQL.connexionSQLBDD;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.AbstractCollection;

public class StatParFormation extends AppCompatActivity {

    Toast toast;
    ListView lstStatFormList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stat_par_formation);
        init();
        new GetListFormation().execute();
    }

    public void init(){
        lstStatFormList = findViewById(R.id.lstStatFormList);
    }




    /*REQUETE SQL*/
    private class GetListFormation extends AsyncTask<Void, Void, Void> {
        protected Void doInBackground(Void... params) {
            try{
                // Connexion à la base de données MySQL
                Statement st = connexionSQLBDD();

                String SQL = "SELECT DOMAINLABEL, FORMATIONNOM FROM `formation` as f\n" +
                        "INNER JOIN `domaine` as d\n" +
                        "ON f.DOMAINID = d.DOMAINID";

                final ResultSet rs = st.executeQuery(SQL);

                while(rs.next()) {
                    // récupérer les valeurs de chaque colonne de la ligne courante
                    String domainLabel = rs.getString("DOMAINLABEL");
                    String formationNom = rs.getString("FORMATIONNOM");

                    // faire quelque chose avec les valeurs récupérées
                    // par exemple, les ajouter à une liste ou les afficher dans la console
                    System.out.println("Domain Label : " + domainLabel + ", Formation Nom : " + formationNom);
                }

            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(Void result) {
            // Mettre à jour l'interface utilisateur si nécessaire
        }
    }
}
package com.btssio.m2l_mission3bis_stats;

import static com.btssio.m2l_mission3bis_stats.SQL.connexionSQLBDD;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

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
    private class GetListFormation extends AsyncTask<Void, Void, List<FormationDomaineData>> {
        protected List<FormationDomaineData> doInBackground(Void... params) {
            List<FormationDomaineData> dataList = new ArrayList<>();
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

                    // Ajouter les valeurs récupérées à la liste d'objets de données
                    FormationDomaineData data = new FormationDomaineData(domainLabel, formationNom);
                    dataList.add(data);
                }

            }catch (Exception e){
                e.printStackTrace();
            }
            return dataList;
        }

        protected void onPostExecute(List<FormationDomaineData> dataList) {
            if (dataList != null && dataList.size() > 0) {
                // Créer un ArrayAdapter en utilisant la liste d'objets de données
                ArrayAdapter<FormationDomaineData> adapter = new ArrayAdapter<>(StatParFormation.this, android.R.layout.simple_list_item_1, dataList);

                // Affecter l'adaptateur à la ListView
                lstStatFormList.setAdapter(adapter);
            }
        }

    }
}
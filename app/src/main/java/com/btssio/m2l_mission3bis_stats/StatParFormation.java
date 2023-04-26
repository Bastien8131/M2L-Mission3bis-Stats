package com.btssio.m2l_mission3bis_stats;

import static com.btssio.m2l_mission3bis_stats.SQL.connexionSQLBDD;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        //onClickStatFormGetStatOfEle
        lstStatFormList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Faites quelque chose lorsqu'un élément est cliqué
                //Toast.makeText(getApplicationContext(), "Vous avez cliqué sur l'élément " + position, Toast.LENGTH_SHORT).show();

                String titleEle = ((Map<String,String>)lstStatFormList.getAdapter().getItem(position)).get("titleKey");
                System.out.println(titleEle);
            }
        });

    }


    /*REQUETE SQL*/
    private class GetListFormation extends AsyncTask<Void, Void, List<FormationDomaineData>> {
        List<FormationDomaineData> dataList = new ArrayList<>();
        protected List<FormationDomaineData> doInBackground(Void... params) {
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
            //https://stackoverflow.com/questions/14098032/add-string-to-string-array
            ArrayList<String> titleArray = new ArrayList<String>();
            ArrayList<String> detailArray = new ArrayList<String>();
            List<Map<String, String>> listArray = new ArrayList<>();

            if (dataList != null && dataList.size() > 0) {

                for(int i=0; i< dataList.size(); i++){
                    titleArray.add(dataList.get(i).getFormationNom());
                    detailArray.add(dataList.get(i).getDomainLabel());
                }


                for(int i=0; i< titleArray.size(); i++)
                {
                    Map<String, String> listItem = new HashMap<>();
                    listItem.put("titleKey", titleArray.get(i));
                    listItem.put("detailKey", detailArray.get(i));
                    listArray.add(listItem);
                }

                SimpleAdapter simpleAdapter = new SimpleAdapter(StatParFormation.this, listArray,
                        android.R.layout.simple_list_item_2,
                        new String[] {"titleKey", "detailKey" },
                        new int[] {android.R.id.text1, android.R.id.text2 });

                lstStatFormList.setAdapter(simpleAdapter);
            }
        }

    }
}
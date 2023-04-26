package com.btssio.m2l_mission3bis_stats;

import static com.btssio.m2l_mission3bis_stats.SQL.connexionSQLBDD;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StatParFormation extends AppCompatActivity {

    Toast toast;
    ListView lstStatFormList;
    TextView txtStatFormValueNbInsc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stat_par_formation);
        init();
        new GetListFormations().execute();
    }

    public void init(){
        txtStatFormValueNbInsc = findViewById(R.id.txtStatFormValueNbInsc);
        lstStatFormList = findViewById(R.id.lstStatFormList);
        lstStatFormList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //String titleEle = ((Map<String,String>)lstStatFormList.getAdapter().getItem(position)).get("titleKey");
                //System.out.println(titleEle);

                //String titleEle2 = lstStatFormList.getAdapter().getItem(position).toString();
                //System.out.println(titleEle2);

                String domainId = ((Map<String,String>)lstStatFormList.getAdapter().getItem(position)).get("domainId");
                String formationId = ((Map<String,String>)lstStatFormList.getAdapter().getItem(position)).get("formationId");
                new GetDataFormation().execute(domainId, formationId);

            }
        });

    }


    /*REQUETE SQL*/
    private class GetListFormations extends AsyncTask<Void, Void, List<FormationDomaineData>> {
        List<FormationDomaineData> dataList = new ArrayList<>();
        protected List<FormationDomaineData> doInBackground(Void... params) {
            try{
                // Connexion à la base de données MySQL
                Statement st = connexionSQLBDD();

                String SQL = "SELECT f.DOMAINID, f.FORMATIONID, d.DOMAINLABEL, f.FORMATIONNOM FROM `formation` as f\n" +
                        "INNER JOIN `domaine` as d\n" +
                        "ON f.DOMAINID = d.DOMAINID";

                final ResultSet rs = st.executeQuery(SQL);

                while(rs.next()) {
                    // récupérer les valeurs de chaque colonne de la ligne courante
                    int domainId = rs.getInt("DOMAINID");
                    int formationId = rs.getInt("FORMATIONID");
                    String domainLabel = rs.getString("DOMAINLABEL");
                    String formationNom = rs.getString("FORMATIONNOM");

                    // Ajouter les valeurs récupérées à la liste d'objets de données
                    FormationDomaineData data = new FormationDomaineData(domainLabel, formationNom, domainId, formationId);
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
            ArrayList<Integer> domainId = new ArrayList<Integer>();
            ArrayList<Integer> formationId = new ArrayList<Integer>();

            List<Map<String, String>> listArray = new ArrayList<>();

            if (dataList != null && dataList.size() > 0) {

                for(int i=0; i< dataList.size(); i++){
                    titleArray.add(dataList.get(i).getFormationNom());
                    detailArray.add(dataList.get(i).getDomainLabel());
                    domainId.add(dataList.get(i).getDomainId());
                    formationId.add(dataList.get(i).getFormationId());
                }


                for(int i=0; i< titleArray.size(); i++)
                {
                    Map<String, String> listItem = new HashMap<>();
                    listItem.put("titleKey", titleArray.get(i));
                    listItem.put("detailKey", detailArray.get(i));
                    listItem.put("domainId", domainId.get(i).toString());
                    listItem.put("formationId", formationId.get(i).toString());
                    listArray.add(listItem);
                }

                SimpleAdapter simpleAdapter = new SimpleAdapter(StatParFormation.this, listArray,
                        android.R.layout.simple_list_item_2,
                        new String[] {"titleKey", "detailKey", "domainId", "formationId"},
                        new int[] {android.R.id.text1, android.R.id.text2, });

                lstStatFormList.setAdapter(simpleAdapter);
            }
        }
    }

    private class GetDataFormation extends AsyncTask<String, Void, Integer> {
        protected Integer doInBackground(String... params) {
            String domainId = params[0];
            String formationId = params[1];
            try{
                // Connexion à la base de données MySQL
                Statement st = connexionSQLBDD();

                String SQL = "SELECT COUNT(*) as NBINSCRIT FROM `participe`" +
                        "WHERE DOMAINID = " + domainId + "\n" +
                        "AND FORMATIONID = " + formationId;

                final ResultSet rs = st.executeQuery(SQL);

                rs.next();

                //System.out.println(rs.getString("NBINSCRIT"));

                return rs.getInt("NBINSCRIT");

            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(Integer result) {
            // Mettre à jour l'interface utilisateur si nécessaire
            txtStatFormValueNbInsc.setText(result.toString());
        }
    }

}
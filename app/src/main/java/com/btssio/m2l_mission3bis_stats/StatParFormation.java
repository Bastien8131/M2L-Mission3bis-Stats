package com.btssio.m2l_mission3bis_stats;

import static com.btssio.m2l_mission3bis_stats.SQL.connexionSQLBDD;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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

    private int nbInscTotal;
    private int nbPlaceTotal;
    private int nbInscFormation;
    private int nbPlaceFormation;
    private double moyNoteFormation;

    Toast toast;
    ListView lstStatFormList;
    TextView txtStatFormValueNbInsc1;
    TextView txtStatFormValue100Insc1;
    TextView txtStatFormValueNbInsc2;
    TextView txtStatFormValue100Insc2;
    TextView txtStatFormValueNbInsc3;
    TextView txtStatFormValue100Insc3;
    TextView txtStatFormValueNote;
    TextView txtStatFormTextNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stat_par_formation);
        init();
        new GetListFormations().execute();
    }

    public void init(){
        lstStatFormList = findViewById(R.id.lstStatFormList);

        txtStatFormValueNbInsc1 = findViewById(R.id.txtStatFormValueNbInsc1);
        txtStatFormValue100Insc1 = findViewById(R.id.txtStatFormValue100Insc1);

        txtStatFormValueNbInsc2 = findViewById(R.id.txtStatFormValueNbInsc2);
        txtStatFormValue100Insc2 = findViewById(R.id.txtStatFormValue100Insc2);

        txtStatFormValueNbInsc3 = findViewById(R.id.txtStatFormValueNbInsc3);
        txtStatFormValue100Insc3 = findViewById(R.id.txtStatFormValue100Insc3);

        txtStatFormValueNote = findViewById(R.id.txtStatFormValueNote);
        txtStatFormTextNote = findViewById(R.id.txtStatFormTextNote);
        lstStatFormList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                /*
                //String titleEle = ((Map<String,String>)lstStatFormList.getAdapter().getItem(position)).get("titleKey");
                //System.out.println(titleEle);

                //String titleEle2 = lstStatFormList.getAdapter().getItem(position).toString();
                //System.out.println(titleEle2);
                */

                String domainId = ((Map<String,String>)lstStatFormList.getAdapter().getItem(position)).get("domainId");
                String formationId = ((Map<String,String>)lstStatFormList.getAdapter().getItem(position)).get("formationId");
                //System.out.println("---"+domainId+" "+formationId);
                new GetNbTotalInscrit().execute();
                new GetNbTotalPlace().execute();
                new GetNbIncritFormation().execute(domainId, formationId);
                new GetNbPlaceFormation().execute(domainId, formationId);
                new GetNoteFormation().execute(domainId, formationId);
            }
        });

    }

    public void SetResultInView (){
        //System.out.println(nbInscFormation+" / "+nbInscTotal);
        txtStatFormValueNbInsc1.setText(String.valueOf(nbInscFormation)+" / "+String.valueOf(nbInscTotal));
        txtStatFormValue100Insc1.setText(Double.toString(Fonctions.pourcentageDe(nbInscFormation, nbInscTotal))+" %");
        txtStatFormValueNbInsc2.setText(String.valueOf(nbInscFormation)+" / "+String.valueOf(nbPlaceFormation));
        txtStatFormValue100Insc2.setText(Double.toString(Fonctions.pourcentageDe(nbInscFormation, nbPlaceFormation))+" %");
        txtStatFormValueNbInsc3.setText(String.valueOf(nbInscFormation)+" / "+String.valueOf(nbPlaceTotal));
        txtStatFormValue100Insc3.setText(Double.toString(Fonctions.pourcentageDe(nbInscFormation, nbPlaceTotal))+" %");
        txtStatFormValueNote.setText(String.valueOf(Fonctions.round(moyNoteFormation))+" / 5");
        txtStatFormTextNote.setText(Fonctions.getNoteTextuel(moyNoteFormation));
    }

    public void onClickStatFormBackMenu(View view) {
        Intent intent = new Intent(StatParFormation.this, Menu.class);
        startActivity(intent);
        finish();
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

    private class GetNbTotalInscrit extends AsyncTask<Void, Void, Integer> {
        protected Integer doInBackground(Void... params) {
            try{
                // Connexion à la base de données MySQL
                Statement st = connexionSQLBDD();

                String SQL = "SELECT COUNT(*) as NBINSCRIT FROM `participe`";

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
            nbInscTotal = result;
        }
    }

    private class GetNbTotalPlace extends AsyncTask<Void, Void, Integer> {
        protected Integer doInBackground(Void... params) {
            try{
                // Connexion à la base de données MySQL
                Statement st = connexionSQLBDD();

                String SQL = "SELECT SUM(NBPLACE) as NBPLACES FROM `session`";

                final ResultSet rs = st.executeQuery(SQL);

                rs.next();

                //System.out.println(rs.getString("NBPLACES"));

                return rs.getInt("NBPLACES");

            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(Integer result) {
            // Mettre à jour l'interface utilisateur si nécessaire
            nbPlaceTotal = result;
        }
    }

    private class GetNbIncritFormation extends AsyncTask<String, Void, Integer> {
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
            nbInscFormation = result;
        }
    }

    private class GetNbPlaceFormation extends AsyncTask<String, Void, Integer> {
        protected Integer doInBackground(String... params) {
            String domainId = params[0];
            String formationId = params[1];
            try{
                // Connexion à la base de données MySQL
                Statement st = connexionSQLBDD();

                String SQL = "SELECT SUM(NBPLACE) as NBPLACES FROM `session`" +
                        "WHERE DOMAINID = " + domainId + "\n" +
                        "AND FORMATIONID = " + formationId;

                final ResultSet rs = st.executeQuery(SQL);

                rs.next();

                //System.out.println(rs.getString("NBINSCRIT"));

                return rs.getInt("NBPLACES");

            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(Integer result) {
            // Mettre à jour l'interface utilisateur si nécessaire
            nbPlaceFormation = result;
            SetResultInView();
        }
    }

    private class GetNoteFormation extends AsyncTask<String, Void, Double> {
        protected Double doInBackground(String... params) {
            String domainId = params[0];
            String formationId = params[1];
            try{
                // Connexion à la base de données MySQL
                Statement st = connexionSQLBDD();

                /*
                * SELECT SUM(a.NOTE) / COUNT(a.NOTE) as MOYNOTE FROM avis a
                    INNER JOIN session s
                    ON a.SESSIONID = s.SESSIONID
                    WHERE s.DOMAINID = 0
                    AND s.FORMATIONID = 0;
                * */

                String SQL = "SELECT SUM(a.NOTE) / COUNT(a.NOTE) as MOYNOTE FROM avis a" + "\n" +
                        "INNER JOIN session s" + "\n" +
                        "ON a.SESSIONID = s.SESSIONID" + "\n" +
                        "WHERE DOMAINID = " + domainId + "\n" +
                        "AND FORMATIONID = " + formationId;

                final ResultSet rs = st.executeQuery(SQL);

                rs.next();

                //System.out.println(rs.getString("NBINSCRIT"));

                return rs.getDouble("MOYNOTE");

            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(Double result) {
            moyNoteFormation = result;
            SetResultInView();
        }
    }

    /*
    
    SELECT
      MONTH(s.SESSIONDATEDEBUT) AS mois,
      COUNT(p.SESSIONID) AS inscriptions
    FROM
      session s
      LEFT JOIN participe p ON s.SESSIONID = p.SESSIONID
    GROUP BY
      mois
    HAVING
      mois BETWEEN 1 AND 12

    UNION ALL

    SELECT
      n mois,
      0 inscriptions
    FROM
      (
        SELECT 1 AS n
        UNION SELECT 2
        UNION SELECT 3
        UNION SELECT 4
        UNION SELECT 5
        UNION SELECT 6
        UNION SELECT 7
        UNION SELECT 8
        UNION SELECT 9
        UNION SELECT 10
        UNION SELECT 11
        UNION SELECT 12
      ) AS months
      LEFT JOIN (
        SELECT MONTH(s.SESSIONDATEDEBUT) AS mois
        FROM session s
        WHERE YEAR(s.SESSIONDATEDEBUT) = YEAR(NOW())
        GROUP BY mois
      ) AS inscriptions ON months.n = inscriptions.mois
    WHERE
      inscriptions.mois IS NULL
    ORDER BY `mois` ASC;

     */

}
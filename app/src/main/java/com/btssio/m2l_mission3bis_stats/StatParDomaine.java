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

public class StatParDomaine extends AppCompatActivity {
    private int nbInscTotal;
    private int nbPlaceTotal;
    private int nbInscDomain;
    private int nbPlaceDomain;
    private double moyNoteDomain;

    Toast toast;
    ListView lstStatDomainList;
    TextView txtStatDomainValueNbInsc1;
    TextView txtStatDomainValue100Insc1;
    TextView txtStatDomainValueNbInsc2;
    TextView txtStatDomainValue100Insc2;
    TextView txtStatDomainValueNbInsc3;
    TextView txtStatDomainValue100Insc3;
    TextView txtStatDomainValueNote;
    TextView txtStatDomainTextNote;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stat_par_domaine);
        init();
        new GetListDomaine().execute();
    }

    public void init(){
        lstStatDomainList = findViewById(R.id.lstStatDomainList);
        txtStatDomainValue100Insc1 = findViewById(R.id.txtStatDomainValue100Insc1);
        txtStatDomainValue100Insc2 = findViewById(R.id.txtStatDomainValue100Insc2);
        txtStatDomainValue100Insc3 = findViewById(R.id.txtStatDomainValue100Insc3);
        txtStatDomainValueNbInsc1 = findViewById(R.id.txtStatDomainValueNbInsc1);
        txtStatDomainValueNbInsc2 = findViewById(R.id.txtStatDomainValueNbInsc2);
        txtStatDomainValueNbInsc3 = findViewById(R.id.txtStatDomainValueNbInsc3);
        txtStatDomainValueNote = findViewById(R.id.txtStatDomainValueNote);
        txtStatDomainTextNote = findViewById(R.id.txtStatDomainTextNote);

        lstStatDomainList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                /*
                //String titleEle = ((Map<String,String>)lstStatFormList.getAdapter().getItem(position)).get("titleKey");
                //System.out.println(titleEle);

                //String titleEle2 = lstStatFormList.getAdapter().getItem(position).toString();
                //System.out.println(titleEle2);
                */

                String domainId = ((Map<String,String>)lstStatDomainList.getAdapter().getItem(position)).get("domainId");
                //System.out.println("---"+domainId+" "+formationId);
                new StatParDomaine.GetNbTotalInscrit().execute();
                new StatParDomaine.GetNbTotalPlace().execute();
                new StatParDomaine.GetNbIncritDomain().execute(domainId);
                new StatParDomaine.GetNbPlaceDomain().execute(domainId);
                new StatParDomaine.GetNoteDomaine().execute(domainId);
            }
        });
    }

    public void SetResultInView (){
        //System.out.println(nbInscFormation+" / "+nbInscTotal);
        txtStatDomainValueNbInsc1.setText(String.valueOf(nbInscDomain)+" / "+String.valueOf(nbInscTotal));
        txtStatDomainValue100Insc1.setText(Double.toString(Fonctions.pourcentageDe(nbInscDomain, nbInscTotal))+" %");
        txtStatDomainValueNbInsc2.setText(String.valueOf(nbInscDomain)+" / "+String.valueOf(nbPlaceDomain));
        txtStatDomainValue100Insc2.setText(Double.toString(Fonctions.pourcentageDe(nbInscDomain, nbPlaceDomain))+" %");
        txtStatDomainValueNbInsc3.setText(String.valueOf(nbInscDomain)+" / "+String.valueOf(nbPlaceTotal));
        txtStatDomainValue100Insc3.setText(Double.toString(Fonctions.pourcentageDe(nbInscDomain, nbPlaceTotal))+" %");
        System.out.println(Fonctions.round(moyNoteDomain));
        txtStatDomainValueNote.setText(String.valueOf(Fonctions.round(moyNoteDomain))+" / 5");
        txtStatDomainTextNote.setText(Fonctions.getNoteTextuel(moyNoteDomain));
    }

    public void onClickStatDomainBackMenu(View view) {
        Intent intent = new Intent(StatParDomaine.this, Menu.class);
        startActivity(intent);
        finish();
    }

    private class GetListDomaine extends AsyncTask<Void, Void, List<DomaineData>> {
        List<DomaineData> dataList = new ArrayList<>();

        protected List<DomaineData> doInBackground(Void... params) {
            try {
                // Connexion à la base de données MySQL
                Statement st = connexionSQLBDD();

                String SQL = "SELECT DOMAINLABEL, DOMAINID FROM `domaine`";

                final ResultSet rs = st.executeQuery(SQL);

                while (rs.next()) {
                    // récupérer les valeurs de chaque colonne de la ligne courante
                    String domainLabel = rs.getString("DOMAINLABEL");
                    int domainId = rs.getInt("DOMAINID");

                    // Ajouter les valeurs récupérées à la liste d'objets de données
                    DomaineData data = new DomaineData(domainLabel,domainId);
                    dataList.add(data);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return dataList;
        }

        protected void onPostExecute(List<DomaineData> dataList) {
            //https://stackoverflow.com/questions/14098032/add-string-to-string-array
            ArrayList<String> detailArray = new ArrayList<String>();
            ArrayList<Integer> domainId = new ArrayList<Integer>();

            List<Map<String, String>> listArray = new ArrayList<>();

            if (dataList != null && dataList.size() > 0) {

                for(int i=0; i< dataList.size(); i++){
                    detailArray.add(dataList.get(i).getDomainLabel());
                    domainId.add(dataList.get(i).getDomainId());
                }


                for(int i=0; i< detailArray.size(); i++)
                {
                    Map<String, String> listItem = new HashMap<>();
                    listItem.put("detailKey", detailArray.get(i));
                    listItem.put("domainId", domainId.get(i).toString());
                    listArray.add(listItem);
                }

                SimpleAdapter simpleAdapter = new SimpleAdapter(StatParDomaine.this, listArray,
                        android.R.layout.simple_list_item_2,
                        new String[] {"titleKey", "detailKey", "domainId", "formationId"},
                        new int[] {android.R.id.text1, android.R.id.text2, });

                lstStatDomainList.setAdapter(simpleAdapter);
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
            try {
                // Connexion à la base de données MySQL
                Statement st = connexionSQLBDD();

                String SQL = "SELECT SUM(NBPLACE) as NBPLACES FROM `session`";

                final ResultSet rs = st.executeQuery(SQL);

                rs.next();

                //System.out.println(rs.getString("NBPLACES"));

                return rs.getInt("NBPLACES");

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(Integer result) {
            // Mettre à jour l'interface utilisateur si nécessaire
            nbPlaceTotal = result;
        }
    }

    private class GetNbIncritDomain extends AsyncTask<String, Void, Integer> {
        protected Integer doInBackground(String... params) {
            String domainId = params[0];
            try{
                // Connexion à la base de données MySQL
                Statement st = connexionSQLBDD();

                String SQL = "SELECT COUNT(*) as NBINSCRIT FROM `participe`" +
                        "WHERE DOMAINID = " + domainId;

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
            nbInscDomain = result;
        }
    }

    private class GetNbPlaceDomain extends AsyncTask<String, Void, Integer> {
        protected Integer doInBackground(String... params) {
            String domainId = params[0];
            try{
                // Connexion à la base de données MySQL
                Statement st = connexionSQLBDD();

                String SQL = "SELECT SUM(NBPLACE) as NBPLACES FROM `session`" +
                        "WHERE DOMAINID = " + domainId ;

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
            nbPlaceDomain = result;
        }
    }

    private class GetNoteDomaine extends AsyncTask<String, Void, Double> {
        protected Double doInBackground(String... params) {
            String domainId = params[0];
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
                        "WHERE DOMAINID = " + domainId;

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
            moyNoteDomain = result;
            SetResultInView();
        }
    }



}
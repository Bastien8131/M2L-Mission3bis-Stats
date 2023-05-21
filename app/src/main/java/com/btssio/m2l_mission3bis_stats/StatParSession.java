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

public class StatParSession extends AppCompatActivity {

    private int nbInscTotal;
    private int nbPlaceTotal;
    private int nbInscSession;
    private int nbPlaceSession;
    private double moyNoteSession;

    Toast toast;
    ListView lstStatSessList;
    TextView txtStatSessValueNbInsc1;
    TextView txtStatSessValue100Insc1;
    TextView txtStatSessValueNbInsc2;
    TextView txtStatSessValue100Insc2;
    TextView txtStatSessValueNbInsc3;
    TextView txtStatSessValue100Insc3;
    TextView txtStatSessValueNote;
    TextView txtStatSessTextNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stat_par_session);
        init();
        new GetListSession().execute();
    }

    public void init(){
        lstStatSessList = findViewById(R.id.lstStatSessList);

        txtStatSessValueNbInsc1 = findViewById(R.id.txtStatSessValueNbInsc1);
        txtStatSessValue100Insc1 = findViewById(R.id.txtStatSessValue100Insc1);

        txtStatSessValueNbInsc2 = findViewById(R.id.txtStatSessValueNbInsc2);
        txtStatSessValue100Insc2 = findViewById(R.id.txtStatSessValue100Insc2);

        txtStatSessValueNbInsc3 = findViewById(R.id.txtStatSessValueNbInsc3);
        txtStatSessValue100Insc3 = findViewById(R.id.txtStatSessValue100Insc3);

        txtStatSessValueNote = findViewById(R.id.txtStatSessValueNote);
        txtStatSessTextNote = findViewById(R.id.txtStatSessTextNote);
        lstStatSessList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                /*
                //String titleEle = ((Map<String,String>)lstStatSessList.getAdapter().getItem(position)).get("titleKey");
                //System.out.println(titleEle);

                //String titleEle2 = lstStatSessList.getAdapter().getItem(position).toString();
                //System.out.println(titleEle2);
                */

                String domainId = ((Map<String,String>)lstStatSessList.getAdapter().getItem(position)).get("domainId");
                String formationId = ((Map<String,String>)lstStatSessList.getAdapter().getItem(position)).get("formationId");
                String sessionId = ((Map<String,String>)lstStatSessList.getAdapter().getItem(position)).get("sessionId");
                //System.out.println("---"+domainId+" "+formationId);
                new StatParSession.GetNbTotalInscrit().execute();
                new StatParSession.GetNbTotalPlace().execute();
                new GetNbIncritSession().execute(domainId, formationId, sessionId);
                new GetNbPlaceSession().execute(domainId, formationId, sessionId);
                new GetNoteSession().execute(domainId, formationId, sessionId);
            }
        });

    }

    public void SetResultInView (){
        //System.out.println(nbInscFormation+" / "+nbInscTotal);
        txtStatSessValueNbInsc1.setText(String.valueOf(nbInscSession)+" / "+String.valueOf(nbInscTotal));
        txtStatSessValue100Insc1.setText(Double.toString(Fonctions.pourcentageDe(nbInscSession, nbInscTotal))+" %");
        txtStatSessValueNbInsc2.setText(String.valueOf(nbInscSession)+" / "+String.valueOf(nbPlaceSession));
        txtStatSessValue100Insc2.setText(Double.toString(Fonctions.pourcentageDe(nbInscSession, nbPlaceSession))+" %");
        txtStatSessValueNbInsc3.setText(String.valueOf(nbInscSession)+" / "+String.valueOf(nbPlaceTotal));
        txtStatSessValue100Insc3.setText(Double.toString(Fonctions.pourcentageDe(nbInscSession, nbPlaceTotal))+" %");
        txtStatSessValueNote.setText(String.valueOf(Fonctions.round(moyNoteSession))+" / 5");
        txtStatSessTextNote.setText(Fonctions.getNoteTextuel(moyNoteSession));
    }

    public void onClickStatSessBackMenu(View view) {
        Intent intent = new Intent(StatParSession.this, Menu.class);
        startActivity(intent);
        finish();
    }


    /*REQUETE SQL*/
    private class GetListSession extends AsyncTask<Void, Void, List<SessionFormationData>> {
        List<SessionFormationData> dataList = new ArrayList<>();
        protected List<SessionFormationData> doInBackground(Void... params) {
            try{
                // Connexion à la base de données MySQL
                Statement st = connexionSQLBDD();

                /*
                SELECT s.SESSIONID, s.FORMATIONID, s.DOMAINID, f.FORMATIONNOM, s.SESSIONDATEDEBUT, s.SESSIONDATEFIN FROM session s
                INNER JOIN formation f
                ON s.FORMATIONID = f.FORMATIONID
                AND s.DOMAINID = f.DOMAINID
                ORDER BY `s`.`SESSIONDATEDEBUT` ASC
                 */

                String SQL = "SELECT s.SESSIONID, s.FORMATIONID, s.DOMAINID, d.DOMAINLABEL, f.FORMATIONNOM, s.SESSIONDATEDEBUT, s.SESSIONDATEFIN FROM session s\n" +
                        "INNER JOIN formation f\n" +
                        "ON s.FORMATIONID = f.FORMATIONID\n" +
                        "AND s.DOMAINID = f.DOMAINID\n" +
                        "INNER JOIN domaine d\n" +
                        "ON s.DOMAINID = d.DOMAINID\n" +
                        "ORDER BY `s`.`SESSIONDATEDEBUT` ASC;";



                final ResultSet rs = st.executeQuery(SQL);

                while(rs.next()) {
                    // récupérer les valeurs de chaque colonne de la ligne courante
                    int domainId = rs.getInt("DOMAINID");
                    int formationId = rs.getInt("FORMATIONID");
                    int sessionId = rs.getInt("SESSIONID");
                    String domainLabel = rs.getString("DOMAINLABEL");
                    String formationNom = rs.getString("FORMATIONNOM");
                    String sessionDateDebut = rs.getString("SESSIONDATEDEBUT");
                    String sessionDateFin = rs.getString("SESSIONDATEFIN");

                    // Ajouter les valeurs récupérées à la liste d'objets de données
                    SessionFormationData data = new SessionFormationData(domainId, formationId, sessionId, domainLabel, formationNom, sessionDateDebut, sessionDateFin);
                    dataList.add(data);
                }

            }catch (Exception e){
                e.printStackTrace();
            }
            return dataList;
        }


        protected void onPostExecute(List<SessionFormationData> dataList) {
            //https://stackoverflow.com/questions/14098032/add-string-to-string-array
            ArrayList<String> titleArray = new ArrayList<String>();
            ArrayList<String> detailArray = new ArrayList<String>();
            ArrayList<Integer> domainId = new ArrayList<Integer>();
            ArrayList<Integer> formationId = new ArrayList<Integer>();
            ArrayList<Integer> sessionId = new ArrayList<Integer>();

            List<Map<String, String>> listArray = new ArrayList<>();

            if (dataList != null && dataList.size() > 0) {

                for(int i=0; i< dataList.size(); i++){
                    titleArray.add(dataList.get(i).getFormationNom());
                    detailArray.add("Du "+dataList.get(i).getSessionDateDebut()+" au "+dataList.get(i).getSessionDateFin());
                    domainId.add(dataList.get(i).getDomainId());
                    formationId.add(dataList.get(i).getFormationId());
                    sessionId.add(dataList.get(i).getSessionId());
                }


                for(int i=0; i< titleArray.size(); i++)
                {
                    Map<String, String> listItem = new HashMap<>();
                    listItem.put("titleKey", titleArray.get(i));
                    listItem.put("detailKey", detailArray.get(i));
                    listItem.put("domainId", domainId.get(i).toString());
                    listItem.put("formationId", formationId.get(i).toString());
                    listItem.put("sessionId", sessionId.get(i).toString());
                    listArray.add(listItem);
                }

                SimpleAdapter simpleAdapter = new SimpleAdapter(StatParSession.this, listArray,
                        android.R.layout.simple_list_item_2,
                        new String[] {"titleKey", "detailKey", "domainId", "formationId", "sessionId"},
                        new int[] {android.R.id.text1, android.R.id.text2, });

                lstStatSessList.setAdapter(simpleAdapter);
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

    private class GetNbIncritSession extends AsyncTask<String, Void, Integer> {
        protected Integer doInBackground(String... params) {
            String domainId = params[0];
            String formationId = params[1];
            String sessionId = params[2];
            try{
                // Connexion à la base de données MySQL
                Statement st = connexionSQLBDD();

                String SQL = "SELECT COUNT(*) as NBINSCRIT FROM `participe`" +
                        "WHERE DOMAINID = " + domainId + "\n" +
                        "AND SESSIONID = " + sessionId + "\n" +
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
            nbInscSession = result;
        }
    }

    private class GetNbPlaceSession extends AsyncTask<String, Void, Integer> {
        protected Integer doInBackground(String... params) {
            String domainId = params[0];
            String formationId = params[1];
            String sessionId = params[2];
            try{
                // Connexion à la base de données MySQL
                Statement st = connexionSQLBDD();

                String SQL = "SELECT SUM(NBPLACE) as NBPLACES FROM `session`" +
                        "WHERE DOMAINID = " + domainId + "\n" +
                        "AND SESSIONID = " + sessionId + "\n" +
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
            nbPlaceSession = result;
            SetResultInView();
        }
    }

    private class GetNoteSession extends AsyncTask<String, Void, Double> {
        protected Double doInBackground(String... params) {
            String domainId = params[0];
            String formationId = params[1];
            String sessionId = params[2];
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
                        "WHERE s.DOMAINID = " + domainId + "\n" +
                        "AND s.SESSIONID = " + sessionId + "\n" +
                        "AND s.FORMATIONID = " + formationId;

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
            moyNoteSession = result;
            SetResultInView();
        }
    }

}
package com.btssio.m2l_mission3bis_stats;

import static com.btssio.m2l_mission3bis_stats.SQL.connexionSQLBDD;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Calendar;


public class StatParPeriodes extends AppCompatActivity {

    private String dateDebut;
    private String dateFin;
    private Integer nbInscs;
    private Integer nbPlaces;
    private Calendar calendar;

    TextView txtStatPeriodeValueNbInsc;
    TextView txtStatPeriodeValueP100Ins;
    CalendarView calStatPeriodeDebut;
    CalendarView calStatPeriodeFin;
    Button btnStatPeriodeBackMenu;
    Button btnStatPeriodeProcess;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stat_par_periodes);
        init();
        calStatPeriodeDebut.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int dayOfMonth) {
                // Récupérer la date sélectionnée
                dateDebut = year + "-" + (month + 1) + "-" + dayOfMonth;

                updateMinDateCalPeriodeFin(year, month, dayOfMonth, calStatPeriodeFin);
            }
        });
        calStatPeriodeFin.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int dayOfMonth) {
                // Récupérer la date sélectionnée
                dateFin = year + "-" + (month + 1) + "-" + dayOfMonth;
            }
        });
    }

    public void init(){
        txtStatPeriodeValueNbInsc = findViewById(R.id.txtStatFormValueNbInsc1);
        txtStatPeriodeValueP100Ins = findViewById(R.id.txtStatFormValue100Insc1);
        calStatPeriodeDebut = findViewById(R.id.calStatPeriodeDebut);
        calStatPeriodeFin = findViewById(R.id.calStatPeriodeFin);
        btnStatPeriodeProcess = findViewById(R.id.btnStatPeriodeProcess);

        calendar = Calendar.getInstance();
        calStatPeriodeDebut.setDate(calendar.getTimeInMillis());
        calStatPeriodeFin.setMinDate(calendar.getTimeInMillis());
    }

    public void updateMinDateCalPeriodeFin(int year, int month, int dayOfMonth, CalendarView calendarView){
        calendarView.setMinDate(0);
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, dayOfMonth);
        long minDate = calendar.getTimeInMillis();
        calendarView.setMinDate(minDate);
    }

    public void onClickStatPeriodeBackMenu(View view) {
        Intent intent = new Intent(StatParPeriodes.this, Menu.class);
        startActivity(intent);
        finish();
    }

    public void onClickStatPeriodeProcess(View view) {
        new GetNbIncrits().execute(dateDebut, dateFin);
        new GetNbPlaces().execute(dateDebut, dateFin);
    }

    public void SetResultInView(){
        txtStatPeriodeValueNbInsc.setText(String.valueOf(nbInscs)+" / "+String.valueOf(nbPlaces));
        txtStatPeriodeValueP100Ins.setText(Double.toString(Fonctions.pourcentageDe(nbInscs, nbPlaces))+" %");
    }

    private class GetNbIncrits extends AsyncTask<String, Void, Integer> {
        protected Integer doInBackground(String... params) {
            String dateDebut = params[0];
            String dateFin = params[1];
            try{
                // Connexion à la base de données MySQL
                Statement st = connexionSQLBDD();

                /*
                String SQL = "SELECT COUNT(*) as NBINSCRIT FROM `participe`" +
                        "WHERE DOMAINID = " + domainId + "\n" +
                        "AND FORMATIONID = " + formationId;

                 */

                String SQL = " SELECT COUNT(*) as NBINSCRIT FROM participe p\n" +
                        "INNER JOIN session s\n" +
                        "ON p.SESSIONID = s.SESSIONID\n" +
                        "WHERE s.SESSIONDATEDEBUT >= '" + dateDebut + "'\n" +
                        "AND s.SESSIONDATEFIN <=  '" + dateFin + "'";

                //System.out.println(SQL);

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
            nbInscs = result;
        }
    }
    private class GetNbPlaces extends AsyncTask<String, Void, Integer> {
        protected Integer doInBackground(String... params) {
            String dateDebut = params[0];
            String dateFin = params[1];
            try{
                // Connexion à la base de données MySQL
                Statement st = connexionSQLBDD();

                /*
                String SQL = "SELECT COUNT(*) as NBINSCRIT FROM `participe`" +
                        "WHERE DOMAINID = " + domainId + "\n" +
                        "AND FORMATIONID = " + formationId;



                String SQL = " SELECT SUM(s.NBPLACE) as NBPLACES FROM participe p\n" +
                        "INNER JOIN session s\n" +
                        "ON p.SESSIONID = s.SESSIONID\n" +
                        "WHERE s.SESSIONDATEDEBUT >= '" + dateDebut + "'\n" +
                        "AND s.SESSIONDATEFIN <=  '" + dateFin + "'";

                 */

                String SQL = "SELECT SUM(NBPLACE) AS NBPLACES FROM (\n" +
                        "    SELECT NBPLACE FROM participe p\n" +
                        "    INNER JOIN session s\n" +
                        "    ON p.SESSIONID = s.SESSIONID\n" +
                        "    WHERE s.SESSIONDATEDEBUT >= '" + dateDebut + "'\n" +
                        "    AND s.SESSIONDATEFIN <=  '" + dateFin + "'\n" +
                        "    GROUP BY s.SESSIONID\n" +
                        ") as SUBREQ;";

                //System.out.println(SQL);

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
            nbPlaces = result;
            SetResultInView();
        }
    }


}
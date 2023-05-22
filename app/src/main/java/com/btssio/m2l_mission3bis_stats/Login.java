package com.btssio.m2l_mission3bis_stats;

import static com.btssio.m2l_mission3bis_stats.SQL.connexionSQLBDD;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import java.sql.ResultSet;
import java.sql.Statement;




public class Login extends AppCompatActivity {


    EditText edtLoginIdentifiant;
    EditText edtLoginMotDePasse;
    Button btnLoginConnect;
    Toast toast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
    }

    public void init(){
        edtLoginIdentifiant = findViewById(R.id.edtLoginIdentifiant);
        edtLoginMotDePasse = findViewById(R.id.edtLoginMotDePasse);
        btnLoginConnect = findViewById(R.id.btnLoginConnect);
        toast = null;
    }


    public void onClickLoginConnect(View view) {
        if(!edtLoginIdentifiant.getText().toString().equals("")){
            new ConnexionApp().execute();
        }else{
            toast = Toast.makeText(getApplicationContext(),"Aucun identifiant n'est saisie",Toast.LENGTH_LONG);
            toast.show();
        }
    }

    private void OpenStatistique() {
        Intent intent = new Intent(Login.this, Menu.class);
        startActivity(intent);
        finish();
    }

    /*REQUETE SQL*/
    private class ConnexionApp extends AsyncTask<Void, Void, Boolean> {
        protected Boolean doInBackground(Void... params) {
            try{
                // Connexion à la base de données MySQL
                Statement st = connexionSQLBDD();

                String SQL = "SELECT COMPTEMOTDEPASSE, COMPTERESPONSABLE FROM compte WHERE COMPTELOGIN = '"+edtLoginIdentifiant.getText()+"'";

                final ResultSet rs = st.executeQuery(SQL);
                rs.next();

                String motDePasse = rs.getString("COMPTEMOTDEPASSE");
                Integer estResponsable = rs.getInt("COMPTERESPONSABLE");

                if( motDePasse.equals(Fonctions.md5(edtLoginMotDePasse.getText().toString())) && estResponsable == 1 ){
                    return true;
                }else{
                    return false;
                }

            }catch (Exception e){
                e.printStackTrace();
                return false;
            }
        }

        protected void onPostExecute(Boolean result) {
            if(result){
                OpenStatistique();
            }else{
                toast = Toast.makeText(getApplicationContext(),"Le mot de passe ou l'identifiant n'est pas bon",Toast.LENGTH_LONG);
                toast.show();
            }
        }
    }


}
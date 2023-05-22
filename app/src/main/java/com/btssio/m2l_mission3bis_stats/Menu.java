package com.btssio.m2l_mission3bis_stats;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Menu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
    }

    public void onClickMenuToStatDom(View view) {
        Intent intent = new Intent(Menu.this, StatParDomaine.class);
        startActivity(intent);
        finish();
    }

    public void onClickMenuToStatForm(View view) {
        Intent intent = new Intent(Menu.this, StatParFormation.class);
        startActivity(intent);
        finish();
    }

    public void onClickMenuToStatSess(View view) {
        Intent intent = new Intent(Menu.this, StatParSession.class);
        startActivity(intent);
        finish();
    }

    public void onClickMenuToStatPeri(View view) {
        Intent intent = new Intent(Menu.this, StatParPeriodes.class);
        startActivity(intent);
        finish();
    }
}
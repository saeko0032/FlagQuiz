package com.example.saeko.flagquizapp;

import android.content.Intent;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {
    public static final String CHOICES = "pref_numberOfChoices";
    public static final String REGIONS = "pref_regionsToInclude";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolabar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolabar);}
    @Override
    protected void onStart() {
        super.onStart();

        MainActivityFragment quizFragment = (MainActivityFragment) getSupportFragmentManager().findFragmentById(R.id.quizFragment);

        quizFragment.updateRegions(PreferenceManager.getDefaultSharedPreferences(this));

        quizFragment.updateGuessRows(PreferenceManager.getDefaultSharedPreferences(this));

        quizFragment.startQuize();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent preferenceIntent = new Intent(this, SettingsActivity.class);
        startActivity(preferenceIntent);
        return super.onOptionsItemSelected(item);
    }

}

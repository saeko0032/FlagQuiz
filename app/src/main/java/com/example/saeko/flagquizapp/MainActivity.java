package com.example.saeko.flagquizapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.preference.PreferenceManager;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.Set;

public class MainActivity extends AppCompatActivity {
    public static final String CHOICES = "pref_numberOfChoices";
    public static final String REGIONS = "pref_regionsToInclude";
    private Toolbar toolbar;
    private boolean phoneDevice = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
        PreferenceManager.getDefaultSharedPreferences(this).registerOnSharedPreferenceChangeListener(preferenceChangeListener);
        int screenSize = getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK;
        if(screenSize == Configuration.SCREENLAYOUT_SIZE_LARGE) {
            phoneDevice = false;
        }
        if (phoneDevice) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

        toolbar = (Toolbar) findViewById(R.id.toolbar);
       // setSupportActionBar(toolbar);
    }
    @Override
    protected void onStart() {
        super.onStart();

        MainActivityFragment quizFragment = (MainActivityFragment) getSupportFragmentManager().findFragmentById(R.id.quizFragment);

        Context context = getApplicationContext();
        quizFragment.updateRegions(PreferenceManager.getDefaultSharedPreferences(context));

        quizFragment.updateGuessRows(PreferenceManager.getDefaultSharedPreferences(context));

        quizFragment.startQuiz();

    }

    private SharedPreferences.OnSharedPreferenceChangeListener preferenceChangeListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
             MainActivityFragment quizFragment = (MainActivityFragment) getSupportFragmentManager().findFragmentById(R.id.quizFragment);

            Context context = getApplicationContext();
            Set<String> regionSet = sharedPreferences.getStringSet(MainActivity.REGIONS, null);
            if (key.equals(CHOICES)) {
                quizFragment.updateGuessRows(PreferenceManager.getDefaultSharedPreferences(context));

            } else if (key.equals(REGIONS)){
                if(regionSet.size() == 0) {
                    Toast.makeText(getApplicationContext(), R.string.default_region_message, Toast.LENGTH_LONG).show();
                    regionSet.add("North_America");
                }
                quizFragment.updateRegions(PreferenceManager.getDefaultSharedPreferences(context));
            }
            quizFragment.startQuiz();
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent preferencesIntent = new Intent(this, SettingsActivity.class);
        startActivity(preferencesIntent);
        return super.onOptionsItemSelected(item);
    }

}

package com.example.saeko.flagquizapp;

import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.IOError;
import java.io.IOException;
import java.io.InputStream;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static android.R.attr.path;

/**
 * Created by saeko on 8/1/2017.
 */

public class MainActivityFragment extends Fragment {
    private int FLAGS_IN_QUIZ = 1;

    private List<String> fileNameList;
    private List<String> quizCountriesList;
    private Set<String> regionSet;

    private LinearLayout quizLinearLayout;
    private TextView questionNumberTextView;
    private ImageView flagImageView;
    private LinearLayout[] guessLinearLayouts;
    private TextView answerTextView;
    private SecureRandom random;
    private String correctAnswer;
    private int guessRows;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.fragment_main, container, false);
        random = new SecureRandom();
        fileNameList = new ArrayList<>();
        quizCountriesList = new ArrayList<>();

        quizLinearLayout = (LinearLayout) view.findViewById(R.id.quizLinearLayout);
        questionNumberTextView = (TextView)view.findViewById(R.id.questionNumberTextView);
        flagImageView = (ImageView)view.findViewById(R.id.flagImageView);

        guessLinearLayouts = new LinearLayout[4];
        guessLinearLayouts[0] = (LinearLayout)view.findViewById(R.id.row1LinearLayout);
        guessLinearLayouts[1] = (LinearLayout)view.findViewById(R.id.row2LinearLayout);
        guessLinearLayouts[2] = (LinearLayout)view.findViewById(R.id.row3LinearLayout);
        guessLinearLayouts[3] = (LinearLayout)view.findViewById(R.id.row4LinearLayout);

        answerTextView = (TextView) view.findViewById(R.id.answerTextView);

        questionNumberTextView.setText(getString(R.string.question, FLAGS_IN_QUIZ));

        return view;
    }

    public void updateGuessRows(SharedPreferences sharedPreferences) {
        String choices = sharedPreferences.getString(MainActivity.CHOICES, null);
        guessRows = Integer.parseInt(choices);

        for(LinearLayout layout: guessLinearLayouts) {
            layout.setVisibility(View.GONE);
        }

        for (int row = 0; row < guessRows; row++) {
            guessLinearLayouts[row].setVisibility(View.VISIBLE);
        }
    }

    public void updateRegions(SharedPreferences sharedPreferences) {
        regionSet = sharedPreferences.getStringSet(MainActivity.REGIONS, null);
    }

    public void startQuize() {
        int i = 0;
        AssetManager assets = getActivity().getAssets();
        try {
            for (String region : regionSet) {
                String[] paths = assets.list(region);
                for (String path : paths) {
                    fileNameList.add(path.replace(".png", ""));
                }
            }
        } catch (IOException exception) {
            Log.e("IN FLAG QUIZE : ", "ERROR LOADING IMAGE FILE NAMES", exception);
        }

        int flagCounter = 1;
        int numberOfFlags = fileNameList.size();
        while (flagCounter <= FLAGS_IN_QUIZ) { // ex) 1 of 10
            int randomIndex = random.nextInt(numberOfFlags);

            String fileName = fileNameList.get(randomIndex);
            if (!quizCountriesList.contains(fileName)) {
                quizCountriesList.add(fileName);
                ++flagCounter;
            }
        }
    }

        private void loadFlags() {
        String nextImage = quizCountriesList.remove(0);
        correctAnswer = nextImage;

        String region = nextImage.substring(0, nextImage.indexOf('-'));
        AssetManager assets = getActivity().getAssets();

        try (InputStream stream = assets.open(region + "/" + nextImage + ".png")){
            Drawable flag = Drawable.createFromStream(stream, nextImage);
            flagImageView.setImageDrawable(flag);
        } catch (IOException exception) {
            Log.e("In the flag quize", "Error loading" + nextImage, exception);
        }

        Collections.shuffle(fileNameList);


        int correct = fileNameList.indexOf(correctAnswer);
        fileNameList.add(fileNameList.remove(correct));

        for (int row = 0; row < guessRows; row++) {
            for (int column = 0; column < guessLinearLayouts[row].getChildCount(); column++) {
                Button newGuessButton = (Button)guessLinearLayouts[row].getChildAt(column);
                newGuessButton.setEnabled(true);

                String fileName = fileNameList.get((row*2) + column);
                newGuessButton.setText(getCountryName(fileName));
            }
        }
        int row = random.nextInt(guessRows);
        int column = random.nextInt(2);
        LinearLayout randomRow = guessLinearLayouts[row];

        String countryName = getCountryName(correctAnswer);
        ((Button) randomRow.getChildAt(column)).setText(countryName);
    }

    private String getCountryName(String name) {
        return name.substring(name.indexOf('-') + 1).replace('-', ' ');
    }
}

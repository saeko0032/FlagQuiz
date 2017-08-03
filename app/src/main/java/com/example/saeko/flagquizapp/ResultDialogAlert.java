package com.example.saeko.flagquizapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import static java.lang.System.exit;

/**
 * Created by saeko on 8/3/2017.
 */

public class ResultDialogAlert extends DialogFragment {
    private int totalGuess = 1;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
       // totalGuess = (int)savedInstanceState.get("guessCount");
        // getActivity tell that activity is not new activity, its using same activity
        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        //totalGuess = getArguments().getInt("count");
        totalGuess = MainActivityFragment.guessCount;

        dialog.setMessage("Your score is " + 1000/totalGuess + " %. \n" + "You took "+ totalGuess + " times for guessing. \n\n Do you want to Restart the Quiz?")
                .setPositiveButton("RESET_QUIZ", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Activity activity = getActivity();
                        Intent intent = new Intent(activity, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // completely cleared the entire stack and made the new activity the only one in the app
                        activity.finish();
                        startActivity(intent);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(getContext(), "Cancel", Toast.LENGTH_SHORT);
                    }
                });
        return dialog.create();
    }
}

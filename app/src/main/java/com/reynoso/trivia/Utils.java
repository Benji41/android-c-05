package com.reynoso.trivia;

import android.view.View;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.reynoso.trivia.controller.AppController;

/**
 * Created by Noé Benjamín Reynoso Aguirre on 9/25/2022.
 */
public class Utils {
    public static void toast(String MESSAGE){
        Toast.makeText(AppController.getInstance(), MESSAGE, Toast.LENGTH_SHORT).show();
    }
    public static void snackBar(View view, String MESSAGE){
        Snackbar.make(view,MESSAGE,Snackbar.LENGTH_SHORT).show();
    }
}

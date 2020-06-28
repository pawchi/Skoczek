package com.example.skoczek;

import android.animation.Animator;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import java.util.Locale;

public class WelcomeAnimation extends AppCompatActivity {
    private ImageView animatedImage;
    String startedLanguage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        loadLocale();
        SharedPreferences prefs = getSharedPreferences("Activity_Settings", Activity.MODE_PRIVATE);
        startedLanguage = prefs.getString("My_Lang",""); //read the language in which the activity was created
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_screen);

        animatedImage = findViewById(R.id.imageView_animated);

        YoYo.with(Techniques.StandUp)
                .duration(1500)
                .withListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animator) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animator) {
                        //nextAnimation();
                    }

                    @Override
                    public void onAnimationCancel(Animator animator) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animator) {

                    }
                })
                .playOn(animatedImage);


        //'WELCOME SCREEN' for 4s
        int WELCOME_SCREEN_TIME_OUT = 1500;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(getApplicationContext(), CreateBoardAnySize.class));
                finish();
            }
        }, WELCOME_SCREEN_TIME_OUT);
    }

    public void loadLocale(){
        SharedPreferences prefs = getSharedPreferences("Activity_Settings", Activity.MODE_PRIVATE);
        String language = prefs.getString("My_Lang", "");
        setLanguageForApp(language);
    }

    private void setLanguageForApp(String languageCode) {
        //Adam's solution
        Locale locale = new Locale(languageCode);
        Locale.setDefault(locale);
        Configuration configuration = new Configuration();
        configuration.setLocale(locale);
        getBaseContext().getResources().updateConfiguration(configuration, getBaseContext().getResources().getDisplayMetrics());
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences prefs = getSharedPreferences("Activity_Settings", Activity.MODE_PRIVATE);
        String language = prefs.getString("My_Lang","");
        if(!language.equals(startedLanguage)){ //check weather language is changed
            recreate();
            startedLanguage = language;
        }
    }

    public void nextAnimation(){
        YoYo.with(Techniques.Flash)
                .duration(1500)
                .playOn(animatedImage);
    }
}


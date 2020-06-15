package com.example.skoczek;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class CreateBoardAnySize extends AppCompatActivity {

    Button createBoard;
    EditText getColumns, getRows;
    public static final String SHARED_PREFS_POOL = "anySizePrefs";
    private String bestRes_44, bestRes_55, bestRes_66, bestRes_77, bestRes_88, bestRes_99;
    private TextView textRes_44, textRes_55, textRes_66, textRes_77, textRes_88, textRes_99;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.board_any_size);

        loadSharedPrefs();
        setAllBestResults();

        getColumns = findViewById(R.id.textSetColumns);
        makeInputLimit(getColumns, 2, 9);
        getRows = findViewById(R.id.textSetRows);
        makeInputLimit(getRows, 2, 9);

        createBoard = findViewById(R.id.buttonCreateBoard);
        createBoard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), PlayGame.class);
                Bundle bundle = new Bundle();
                bundle.putInt("columns", Integer.parseInt(getColumns.getText().toString()));
                bundle.putInt("rows", Integer.parseInt(getRows.getText().toString()));
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });


    }

    private void makeInputLimit(EditText editText, int min, int max) {
        editText.setFilters(new InputFilter[]{new InputFilterMinMax(min, max)});
    }

    public void loadSharedPrefs(){
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS_POOL, MODE_PRIVATE);
        bestRes_44 = sharedPreferences.getString("44","0");
        bestRes_55 = sharedPreferences.getString("55","0");
        bestRes_66 = sharedPreferences.getString("66","0");
        bestRes_77 = sharedPreferences.getString("77","0");
        bestRes_88 = sharedPreferences.getString("88","0");
        bestRes_99 = sharedPreferences.getString("99","0");
    }

    public void setAllBestResults(){
        textRes_44 = findViewById(R.id.textViewBestResult_4x4);
        textRes_44.setText(bestRes_44);
        textRes_55 = findViewById(R.id.textViewBestResult_5x5);
        textRes_55.setText(bestRes_55);
        textRes_66 = findViewById(R.id.textViewBestResult_6x6);
        textRes_66.setText(bestRes_66);
        textRes_77 = findViewById(R.id.textViewBestResult_7x7);
        textRes_77.setText(bestRes_77);
        textRes_88 = findViewById(R.id.textViewBestResult_8x8);
        textRes_88.setText(bestRes_88);
        textRes_99 = findViewById(R.id.textViewBestResult_9x9);
        textRes_99.setText(bestRes_99);
    }
}

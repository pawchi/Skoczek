package com.example.skoczek;

import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.gridlayout.widget.GridLayout;

public class BoardAnySize extends AppCompatActivity {

    GridLayout gridLayoutAnySize;
    Button createBoard;
    EditText getColumns, getRows;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.board_any_size);

        gridLayoutAnySize = findViewById(R.id.gridLayoutAnySize);
        gridLayoutAnySize.removeAllViews();

        getColumns = findViewById(R.id.textSetColumns);
        getRows = findViewById(R.id.textSetRows);

        createBoard = findViewById(R.id.buttonCreateBoard);
        createBoard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createGridLayout(gridLayoutAnySize);
            }
        });



    }

    public int getScreenWidthInPixels(){
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager windowManager = (WindowManager)getApplicationContext().getSystemService(WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels;
    }

    public void createGridLayout(GridLayout myGrid){
        int column = Integer.parseInt(getColumns.getText().toString());
        int row = Integer.parseInt(getRows.getText().toString());
        myGrid.setColumnCount(column);
        myGrid.setRowCount(row);

        for (int x =0; x<row; x++){
            for (int y=0; y<column; y++) {

                TextView textView = new TextView(this);
                android.widget.GridLayout.LayoutParams param = new android.widget.GridLayout.LayoutParams();

                param.width = (getScreenWidthInPixels()/column)-column;
                param.height = (getScreenWidthInPixels()/column)-column;
                param.rightMargin = 3;
                param.leftMargin = 3;
                param.topMargin = 3;
                param.bottomMargin = 3;
                param.setGravity(Gravity.CENTER);
                param.columnSpec = android.widget.GridLayout.spec(y);
                param.rowSpec = android.widget.GridLayout.spec(x);
                textView.setLayoutParams(param);
                textView.setBackgroundColor(Color.BLUE);
                textView.setGravity(Gravity.CENTER);
                //textView.setOnClickListener(this);

                myGrid.addView(textView);
            }
        }
    }
}

package com.example.skoczek;

import android.graphics.Color;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.gridlayout.widget.GridLayout;

public class BoardAnySize extends AppCompatActivity implements View.OnClickListener {

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
        makeInputLimit(getColumns, 2, 9);
        getRows = findViewById(R.id.textSetRows);
        makeInputLimit(getRows, 2, 9);

        createBoard = findViewById(R.id.buttonCreateBoard);
        createBoard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createGridLayout(gridLayoutAnySize);
            }
        });


    }

    private void makeInputLimit(EditText editText, int min, int max) {
        editText.setFilters(new InputFilter[]{new InputFilterMinMax(min, max)});
    }

    public int getScreenWidthInPixels() {
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) getApplicationContext().getSystemService(WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels;
    }

    public int getScreenHeightInPixels() {
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) getApplicationContext().getSystemService(WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(dm);
        return dm.heightPixels;
    }

    public void createGridLayout(GridLayout myGrid) {
        if (getColumns.getText().toString().equals("") || getRows.getText().toString().equals("")) {
            new AlertDialog.Builder(this)
                    .setTitle("Zły wymiar planszy")
                    .setMessage("Musisz podać obydwa wymiary planszy z przedziału od 2 do 9. Wierszy nie może być więcej niż kolumn.")
                    .show();
        } else if (Integer.parseInt(getColumns.getText().toString()) < Integer.parseInt(getRows.getText().toString())) {
            new AlertDialog.Builder(this)
                    .setTitle("Zły wymiar planszy")
                    .setMessage("Wierszy nie może być więcej niż kolumn.")
                    .show();
        } else {

            int column = Integer.parseInt(getColumns.getText().toString());
            int row = Integer.parseInt(getRows.getText().toString());
            myGrid.setColumnCount(column);
            myGrid.setRowCount(row);

            for (int x = 1; x < row + 1; x++) {
                for (int y = 1; y < column + 1; y++) {

                    TextView textView = new TextView(this);
                    android.widget.GridLayout.LayoutParams param = new android.widget.GridLayout.LayoutParams();

                    param.rightMargin = getScreenWidthInPixels() / column / 50;
                    param.leftMargin = getScreenWidthInPixels() / column / 50;
                    param.topMargin = getScreenWidthInPixels() / column / 50;
                    param.bottomMargin = getScreenWidthInPixels() / column / 50;
                    param.width = (getScreenWidthInPixels() / column) - (2 * param.rightMargin);
                    param.height = (getScreenWidthInPixels() / column) - (2 * param.rightMargin);
                    param.setGravity(Gravity.CENTER);
                    param.columnSpec = android.widget.GridLayout.spec(y);
                    param.rowSpec = android.widget.GridLayout.spec(x);

                    textView.setLayoutParams(param);
                    textView.setBackgroundColor(Color.BLUE);
                    textView.setGravity(Gravity.CENTER);
                    textView.setOnClickListener(this);
                    String id = Integer.toString(x) + Integer.toString(y);
                    textView.setId(Integer.parseInt(id));

                    myGrid.addView(textView);
                }
            }
        }
    }

    @Override
    public void onClick(View v) {
        TextView textView = (TextView) findViewById(getResources().getIdentifier(Integer.toString(v.getId()), "id", getPackageName()));
        textView.setText("Id: " + Integer.toString(v.getId()));
        textView.setTextColor(Color.WHITE);

    }
}

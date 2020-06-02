package com.example.skoczek;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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

    public static final int COLOR_MOVE_POSSIBLE = Color.MAGENTA;
    public static final int COLOR_CLICKED = Color.GREEN;
    public static final int COLOR_NOT_CLICKED = Color.BLUE;

    GridLayout gridLayoutAnySize;
    Button createBoard;
    EditText getColumns, getRows;
    int countResult = 0;
    int column = 0;
    int row = 0;


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

            column = Integer.parseInt(getColumns.getText().toString());
            row = Integer.parseInt(getRows.getText().toString());
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
                    textView.setBackgroundColor(COLOR_NOT_CLICKED);
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
        TextView field = (TextView) findViewById(getResources().getIdentifier(Integer.toString(v.getId()), "id", getPackageName()));
        ColorDrawable itemColor = (ColorDrawable) v.getBackground();
        String id = String.valueOf(v.getId());
        int posX = Integer.parseInt(id.substring(0, 1));
        int posY = Integer.parseInt(id.substring(1, 2));

        if (itemColor.getColor() == COLOR_NOT_CLICKED && countResult == 0) {
            showPossibleMoves(posX, posY);
            field.setBackgroundColor(COLOR_CLICKED);
            countResult++;
        }
        if (itemColor.getColor() == COLOR_MOVE_POSSIBLE) {
            field.setBackgroundColor(COLOR_CLICKED);
            cancelOldMovesPossible();
            countResult++;
            showPossibleMoves(posX, posY);
        }


    }

    public void cancelOldMovesPossible() {

        for (int i = 0; i < gridLayoutAnySize.getChildCount(); i++) {
            View child = gridLayoutAnySize.getChildAt(i);
            ColorDrawable childBackground = (ColorDrawable) child.getBackground();
            if (childBackground.getColor() != COLOR_CLICKED) {
                child.setBackgroundColor(COLOR_NOT_CLICKED);
            }
        }
    }

    public void showPossibleMoves(int posX, int posY) {
        if (posX + 1 <= column && posY + 2 <= row) {
            setColorPossibleMove(posX + 1, posY + 2);
        }
        if (posX + 2 <= column && posY + 1 <= row) {
            setColorPossibleMove(posX + 2, posY + 1);
        }
        if (posX - 1 >= 1 && posY + 2 <= row) {
            setColorPossibleMove(posX - 1, posY + 2);
        }
        if (posX - 2 >= 1 && posY + 1 <= row) {
            setColorPossibleMove(posX - 2, posY + 1);
        }
        if (posX + 1 <= column && posY - 2 >= 1) {
            setColorPossibleMove(posX + 1, posY - 2);
        }
        if (posX + 2 <= column && posY - 1 >= 1) {
            setColorPossibleMove(posX + 2, posY - 1);
        }
        if (posX - 1 >= 1 && posY - 2 >= 1) {
            setColorPossibleMove(posX - 1, posY - 2);
        }
        if (posX - 2 >= 1 && posY - 1 >= 1) {
            setColorPossibleMove(posX - 2, posY - 1);
        }
    }

    public void setColorPossibleMove(int x, int y) {
        String idX = Integer.toString(x);
        String idY = Integer.toString(y);
        String idXY = idX + idY;
        TextView field = (TextView) findViewById(getResources().getIdentifier(idXY, "id", getPackageName()));
        ColorDrawable fieldColor = (ColorDrawable) field.getBackground();
        if (fieldColor.getColor()!=COLOR_CLICKED){
            field.setBackgroundColor(COLOR_MOVE_POSSIBLE);
        }

    }

    public boolean isMovePossible(View v) {
        String id = String.valueOf(v.getId());
        int posX = Integer.parseInt(id.substring(0, 1));
        int posY = Integer.parseInt(id.substring(1, 2));

        return true;
    }


}

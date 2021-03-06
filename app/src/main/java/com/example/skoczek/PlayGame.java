package com.example.skoczek;

import android.animation.Animator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.gridlayout.widget.GridLayout;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import java.util.Locale;

public class PlayGame extends AppCompatActivity implements View.OnClickListener {

    public static final int COLOR_MOVE_POSSIBLE = Color.MAGENTA;
    public static final int COLOR_CLICKED = Color.GREEN;
    public static final int COLOR_CLICKED_TEMP = Color.parseColor("#fff400");
    public static final int COLOR_NOT_CLICKED = Color.BLUE;

    public static final String SHARED_PREFS_POOL = "anySizePrefs";
    public static String SHARED_PREFS_CURRENT_KEY;

    GridLayout gridLayoutAnySize;
    TextView result, scoreForBestResult, textForBestResult, currentBoardSize;
    int currentResult = 0;
    int bestResultEver = 0;
    int column;
    int row;
    String currentBoard_KEY;
    ImageView backButton;
    TextView resetBoardTextView;
    int newRecordSignalLimiter = 0;
    LinearLayout linearLayoutBestResult;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode==KeyEvent.KEYCODE_BACK){
            startActivity(new Intent(getApplicationContext(), CreateBoardAnySize.class));
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.play_game);

        LinearLayout layoutToInjectXmlUpBar = findViewById(R.id.layout_up_bar_playgame);
        getLayoutInflater().inflate(R.layout.fragment_up_bar, layoutToInjectXmlUpBar);

        backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), CreateBoardAnySize.class));
                finish();
            }
        });

        linearLayoutBestResult = findViewById(R.id.layoutBestResult);
        result = findViewById(R.id.textBestCurrentResultAnySize);
        scoreForBestResult = findViewById(R.id.scoreForBestResultEverAnySize);
        textForBestResult = findViewById(R.id.textForBestScore);
        currentBoardSize = findViewById(R.id.textCurrantBoardSize);

        resetBoardTextView = findViewById(R.id.textViewResetBoard);
        resetBoardTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveSharedPrefs();
                currentResult=0;
                result.setText("0");
                linearLayoutBestResult.setBackground(getResources().getDrawable(R.drawable.button_border,null));
                scoreForBestResult.setText(Integer.toString(bestResultEver));
                textForBestResult.setText("Najlepszy wynik: ");
                gridLayoutAnySize.removeAllViews();
                createGridLayout(gridLayoutAnySize);
                loadSharedPrefs();
                scoreForBestResult.setText(Integer.toString(bestResultEver));

            }
        });

        gridLayoutAnySize = findViewById(R.id.gridLayoutAnySize);
        gridLayoutAnySize.removeAllViews();

        Bundle bundle = getIntent().getExtras();
        if (bundle!=null){
            column = bundle.getInt("columns");
            row = bundle.getInt("rows");
            currentBoard_KEY = Integer.toString(column)+ " x " + Integer.toString(row);
        }
        currentBoardSize.setText(Integer.toString(column)+ " x " + Integer.toString(row));
        loadSharedPrefs();
        scoreForBestResult.setText(Integer.toString(bestResultEver));
        createGridLayout(gridLayoutAnySize);



    }

    private void newRecordSignal() {

        linearLayoutBestResult.setBackground(getResources().getDrawable(R.drawable.button_border_new_record,null));
        textForBestResult.setText("New Record!!!");
        scoreForBestResult.setText("");

        YoYo.with(Techniques.Flash)
                .duration(1500)
                .withListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animator) {
                    }

                    @Override
                    public void onAnimationEnd(Animator animator) {
                    }

                    @Override
                    public void onAnimationCancel(Animator animator) {
                    }

                    @Override
                    public void onAnimationRepeat(Animator animator) {
                    }
                })
                .playOn(linearLayoutBestResult);
        newRecordSignalLimiter=1;
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

            myGrid.setColumnCount(column);
            myGrid.setRowCount(row);

            for (int x = 1; x < row + 1; x++) {
                for (int y = 1; y < column + 1; y++) {

                    TextView textView = new TextView(this);
                    android.widget.GridLayout.LayoutParams param = new android.widget.GridLayout.LayoutParams();

                    int margin = getScreenWidthInPixels() / column / 50;
                    int width = (getScreenWidthInPixels() / column) - (2 * margin);
                    int height = (getScreenWidthInPixels() / column) - (2 * margin);
                    if (height*row>getScreenHeightInPixels()-(3*width)){
                        height= (getScreenHeightInPixels()-(2*width)-width/3)/row - (2 * margin);
                    }


                    param.rightMargin = margin;
                    param.leftMargin = margin;
                    param.topMargin = margin;
                    param.bottomMargin = margin;
                    param.width = width;
                    param.height = height;
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

    @Override
    public void onClick(View v) {
        TextView field = (TextView) findViewById(getResources().getIdentifier(Integer.toString(v.getId()), "id", getPackageName()));
        ColorDrawable itemColor = (ColorDrawable) v.getBackground();
        String id = String.valueOf(v.getId());
        int posX = Integer.parseInt(id.substring(0, 1));
        int posY = Integer.parseInt(id.substring(1, 2));

        if (itemColor.getColor() == COLOR_NOT_CLICKED && currentResult == 0) {
            showPossibleMoves(posX, posY);
            field.setBackgroundColor(COLOR_CLICKED_TEMP);
            currentResult++;
            field.setText(Integer.toString(currentResult));
            result.setText(Integer.toString(currentResult));
        }
        if (itemColor.getColor() == COLOR_MOVE_POSSIBLE) {
            cancelOldMovesPossible();
            field.setBackgroundColor(COLOR_CLICKED_TEMP);
            currentResult++;
            field.setText(Integer.toString(currentResult));
            showPossibleMoves(posX, posY);
            result.setText(String.format(Locale.getDefault(), "%d", currentResult));
            if (currentResult>bestResultEver&&newRecordSignalLimiter==0){
                newRecordSignal();
            }
        }
    }

    public void cancelOldMovesPossible() {

        for (int i = 0; i < gridLayoutAnySize.getChildCount(); i++) {
            View child = gridLayoutAnySize.getChildAt(i);
            ColorDrawable childBackground = (ColorDrawable) child.getBackground();
            if (childBackground.getColor()==COLOR_CLICKED_TEMP){
                child.setBackgroundColor(COLOR_CLICKED);
            }
            if (childBackground.getColor() != COLOR_CLICKED) {
                child.setBackgroundColor(COLOR_NOT_CLICKED);
            }
        }
    }

    public void showPossibleMoves(int posX, int posY) {
        if (posX + 1 <= row && posY + 2 <= column) {
            setColorPossibleMove(posX + 1, posY + 2);
        }
        if (posX + 2 <= row && posY + 1 <= column) {
            setColorPossibleMove(posX + 2, posY + 1);
        }
        if (posX - 1 >= 1 && posY + 2 <= column) {
            setColorPossibleMove(posX - 1, posY + 2);
        }
        if (posX - 2 >= 1 && posY + 1 <= column) {
            setColorPossibleMove(posX - 2, posY + 1);
        }
        if (posX + 1 <= row && posY - 2 >= 1) {
            setColorPossibleMove(posX + 1, posY - 2);
        }
        if (posX + 2 <= row && posY - 1 >= 1) {
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

    @Override
    protected void onPause() {
        super.onPause();
        saveSharedPrefs();
    }


    public void saveSharedPrefs(){
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS_POOL, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        if (bestResultEver<currentResult){
            editor.putString(currentBoard_KEY, Integer.toString(currentResult));
        }
        editor.apply();
    }

    public void loadSharedPrefs(){
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS_POOL, MODE_PRIVATE);
        bestResultEver = Integer.parseInt(sharedPreferences.getString(currentBoard_KEY,"0"));
    }


}

package com.example.skoczek;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public static final int NOT_CLICKED = 0;
    public static final int CLICKED = 1;
    public static final int MOVE_POSSIBLE = 2;

    public static final int COLOR_MOVE_POSSIBLE = Color.MAGENTA;
    public static final int COLOR_CLICKED = Color.GREEN;
    public static final int COLOR_NOT_CLICKED = Color.BLUE;

    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String TEXT = "test";

    Map<Integer,Skoczek2_Field> board = new HashMap<>();
    public ArrayList<ArrayList<Integer>> arrayMoves = new ArrayList<>();
    public int countResult = 0;
    public TextView showBestResult;
    public TextView bestResultEver;
    public int bestScore = 0;

    GridLayout gridLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.skoczek_main);

        if (savedInstanceState != null){
            bestScore = savedInstanceState.getInt("bestScore");
        }

        DisplayMetrics dm = new DisplayMetrics();
        WindowManager windowManager = (WindowManager)getApplicationContext().getSystemService(WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(dm);
        int widthInDP = Math.round(dm.widthPixels/dm.density);
        Toast.makeText(this, "Screen width pixels = " + dm.widthPixels, Toast.LENGTH_SHORT).show();



        showBestResult = (TextView) findViewById(R.id.textBestCurrentResult);
        bestResultEver = (TextView) findViewById(R.id.textBestResultEver);
        loadSharedPrefs(); //load best ever user score
        setListWithPossibleMoves();

        gridLayout = (GridLayout)findViewById(R.id.tableGrid);
        gridLayout.removeAllViews();
        int total = 25;
        int column = 5;
        int row = total / column;
        gridLayout.setColumnCount(column);
        gridLayout.setRowCount(row + 1);
        for(int i =0, c = 0, r = 0; i < total; i++, c++){
            if(c == column){
                c = 0;
                r++;
            }
            TextView textView = new TextView(this);
            GridLayout.LayoutParams param =new GridLayout.LayoutParams();

            param.width = (dm.widthPixels/5)-10;
            param.height = (dm.widthPixels/5)-10;
            param.rightMargin = 5;
            param.leftMargin = 5;
            param.topMargin = 5;
            param.bottomMargin = 5;
            param.setGravity(Gravity.CENTER);
            param.columnSpec = GridLayout.spec(c);
            param.rowSpec = GridLayout.spec(r);
            textView.setLayoutParams(param);
            textView.setBackgroundColor(COLOR_NOT_CLICKED);
            textView.setGravity(Gravity.CENTER);
            textView.setOnClickListener(this);
            //textView.setText(Integer.toString(i+1));
            textView.setId(i+1);
            gridLayout.addView(textView);
        }

        for (int i=0; i<gridLayout.getChildCount(); i++){
            Skoczek2_Field field = new Skoczek2_Field(NOT_CLICKED,new ArrayList<Integer>(arrayMoves.get(i)));
            board.put(i,field);
        }

        //int childWidth = gridLayout.getChildAt(0).getLayoutParams().width;
        //Toast.makeText(this, "Child width = " + childWidth, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onClick(View v) {
        TextView textView = (TextView)findViewById(getResources().getIdentifier(Integer.toString(v.getId()),"id",getPackageName()));
        ColorDrawable textColor = (ColorDrawable) textView.getBackground();
        ColorDrawable itemColor = (ColorDrawable)v.getBackground();

        int viewCell = v.getId(); //1-25
        int boardField = viewCell-1;

        if (countResult ==0){
            board.get(boardField).setFieldStatus(CLICKED); //BOARD
            v.setBackgroundColor(Color.GREEN); //TEMPORARY
            showAndSetPossibleMoves(v); //TEMPORARY
            removeFieldFromPossibleMoves(viewCell);
            countResult =1;
            textView.setText(Integer.toString(countResult));
            if(Integer.parseInt(showBestResult.getText().toString())>= bestScore){
                showBestResult.setText(Integer.toString(countResult));
            }
        } else {
            if ( board.get(boardField).getFieldStatus()==NOT_CLICKED && itemColor.getColor()==COLOR_MOVE_POSSIBLE){ //
                setWholeBoardColors(v);
                board.get(boardField).setFieldStatus(CLICKED);
                countResult++;
                textView.setText(Integer.toString(countResult));
                v.setBackgroundColor(Color.GREEN); //TEMPORARY
                showAndSetPossibleMoves(v); //TEMPORARY
                removeFieldFromPossibleMoves(viewCell);

                if(Integer.parseInt(showBestResult.getText().toString())>= bestScore){
                    showBestResult.setText(Integer.toString(countResult));
                }
            }
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        if (bestScore == 0) {
            outState.putInt("bestScore", countResult);
        }
        if (bestScore<countResult){
            outState.putInt("bestScore", countResult);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        saveSharedPrefs();
    }

    public void saveSharedPrefs(){
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (bestScore==0){
            editor.putString(TEXT, Integer.toString(countResult));
        }
        if (bestScore<countResult){
            editor.putString(TEXT, Integer.toString(countResult));
        }
        editor.apply();
    }

    public void loadSharedPrefs(){
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        bestResultEver.setText(sharedPreferences.getString(TEXT,"0"));
    }

    private void showAndSetPossibleMoves(View view) {
        int viewCell = view.getId();
        int boardField = viewCell-1;
        for (int i : board.get(boardField).getPossibleMoves()){
            if (board.get(i-1).getFieldStatus()==NOT_CLICKED){
                gridLayout.getChildAt(i-1).setBackgroundColor(COLOR_MOVE_POSSIBLE);
                //board.get(i-1).setFieldStatus(MOVE_POSSIBLE);
            }

        }
    }

    public void removeFieldFromPossibleMoves(int boardField){
        for (int i=0; i<board.size(); i++){
            for (int x=0; x < board.get(i).getPossibleMoves().size(); x++){
                if (board.get(i).getPossibleMoves().get(x)==boardField){
                    board.get(i).getPossibleMoves().remove(x);
                }
            }
        }
    }


    public void setWholeBoardColors(View view){
        for (int i=0; i<board.size(); i++){
            switch (board.get(i).getFieldStatus()){
                case CLICKED: gridLayout.getChildAt(i).setBackgroundColor(COLOR_CLICKED);
                    break;
                case NOT_CLICKED: gridLayout.getChildAt(i).setBackgroundColor(COLOR_NOT_CLICKED);
                    break;
                case MOVE_POSSIBLE: gridLayout.getChildAt(i).setBackgroundColor(COLOR_MOVE_POSSIBLE);
                    break;
            }
        }
    }

    public void setListWithPossibleMoves(){
        arrayMoves.add(new ArrayList<Integer>(Arrays.asList(8,12)));
        arrayMoves.add(new ArrayList<Integer>(Arrays.asList(9,11,13)));
        arrayMoves.add(new ArrayList<Integer>(Arrays.asList(6,10,12,14)));
        arrayMoves.add(new ArrayList<Integer>(Arrays.asList(7,13,15)));
        arrayMoves.add(new ArrayList<Integer>(Arrays.asList(8,14)));

        arrayMoves.add(new ArrayList<Integer>(Arrays.asList(3,13,17)));
        arrayMoves.add(new ArrayList<Integer>(Arrays.asList(4,14,16,18)));
        arrayMoves.add(new ArrayList<Integer>(Arrays.asList(1,5,11,15,17,19)));
        arrayMoves.add(new ArrayList<Integer>(Arrays.asList(2,12,18,20)));
        arrayMoves.add(new ArrayList<Integer>(Arrays.asList(3,13,19)));

        arrayMoves.add(new ArrayList<Integer>(Arrays.asList(2,8,18,22)));
        arrayMoves.add(new ArrayList<Integer>(Arrays.asList(1,3,9,19,21,23)));
        arrayMoves.add(new ArrayList<Integer>(Arrays.asList(2,4,6,10,16,20,22,24)));
        arrayMoves.add(new ArrayList<Integer>(Arrays.asList(3,5,7,17,23,25)));
        arrayMoves.add(new ArrayList<Integer>(Arrays.asList(4,8,18,24)));

        arrayMoves.add(new ArrayList<Integer>(Arrays.asList(7,13,23)));
        arrayMoves.add(new ArrayList<Integer>(Arrays.asList(6,8,14,24)));
        arrayMoves.add(new ArrayList<Integer>(Arrays.asList(7,9,11,15,21,25)));
        arrayMoves.add(new ArrayList<Integer>(Arrays.asList(8,10,12,22)));
        arrayMoves.add(new ArrayList<Integer>(Arrays.asList(9,13,23)));

        arrayMoves.add(new ArrayList<Integer>(Arrays.asList(12,18)));
        arrayMoves.add(new ArrayList<Integer>(Arrays.asList(11,13,19)));
        arrayMoves.add(new ArrayList<Integer>(Arrays.asList(12,14,16,20)));
        arrayMoves.add(new ArrayList<Integer>(Arrays.asList(13,15,17)));
        arrayMoves.add(new ArrayList<Integer>(Arrays.asList(14,18)));
    }
}

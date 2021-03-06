package com.example.skoczek;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;

public class CreateBoardAnySize extends AppCompatActivity {

    GridLayout menuGridLayout;
    int screenWidth;
    int screenHeight;
    public static final String SHARED_PREFS_POOL = "anySizePrefs";
    private ArrayList<BoardInfo> boardInfosList = new ArrayList<>(Arrays.asList(new BoardInfo("5 x 5", "25"),
            new BoardInfo("5 x 6", "30"), new BoardInfo("5 x 7", "35"),
            new BoardInfo("5 x 8", "40"), new BoardInfo("5 x 9", "45"),
            new BoardInfo("6 x 6", "36"), new BoardInfo("6 x 7", "42"),
            new BoardInfo("6 x 8", "48"), new BoardInfo("6 x 9", "54"),
            new BoardInfo("7 x 7", "49"), new BoardInfo("7 x 8", "56"),
            new BoardInfo("7 x 9", "63"), new BoardInfo("8 x 8", "64"),
            new BoardInfo("8 x 9", "72"), new BoardInfo("9 x 9", "81")));
    private ArrayList<String> colors = new ArrayList<>(Arrays.asList("#ff71ce", "#01cdfe", "#05ffa1", "#b967ff", "#fffb96"));

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.board_any_size);

        LinearLayout layoutToInjectXmlUpBar = findViewById(R.id.layout_up_bar);
        getLayoutInflater().inflate(R.layout.fragment_up_bar, layoutToInjectXmlUpBar);

        menuGridLayout = findViewById(R.id.startPageGridLayout);
        screenWidth = getScreenWidthInPixels();
        screenHeight = getScreenHeightInPixels();

        createMenuItems();
    }

    private void createMenuItems() {
        int loopPos = 0;
        int itemMarginWidth = 30;
        int screenMarginWidth = 20;
        int itemWidth = ((screenWidth - screenMarginWidth) / 3) - itemMarginWidth;
        int itemHeight = (screenHeight / 5) - 70;


        for (final BoardInfo boardInfo : boardInfosList) {
            LinearLayout linearLayout = new LinearLayout(this);
            LinearLayout.LayoutParams scoreLinLayParam = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            scoreLinLayParam.gravity = Gravity.CENTER;
            linearLayout.setLayoutParams(scoreLinLayParam);
            linearLayout.setOrientation(LinearLayout.HORIZONTAL);
            TextView bestRes = new TextView(this);
            bestRes.setText("Best : ");
            bestRes.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            TextView score = new TextView(this);
            score.setText(loadSharedPrefs(boardInfo.getSize()));
            score.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            TextView numOfFields = new TextView(this);
            String tempText = "/" + boardInfo.getNumberOfFields();
            numOfFields.setText(tempText);
            numOfFields.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            linearLayout.addView(bestRes);
            linearLayout.addView(score);
            linearLayout.addView(numOfFields);

            LinearLayout mainLinLay = new LinearLayout(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(itemWidth, itemHeight);
            params.gravity = Gravity.CENTER;
            mainLinLay.setGravity(Gravity.CENTER);
            params.setMargins(10, 10, 10, 10);
            mainLinLay.setOrientation(LinearLayout.VERTICAL);
            mainLinLay.setWeightSum(1);

            mainLinLay.setBackgroundResource(R.drawable.corners_bg);
            if (loopPos > 4) {
                loopPos = 0;
            }
            String randomColor = colors.get(loopPos);
            loopPos++;
            mainLinLay.getBackground().setTint(Color.parseColor(randomColor));
            mainLinLay.setLayoutParams(params);

            TextView boardSize = new TextView(this);
            boardSize.setText(boardInfo.getSize());
            boardSize.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            boardSize.setTextSize(35);

            mainLinLay.addView(linearLayout);
            mainLinLay.addView(boardSize);
            mainLinLay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), PlayGame.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt("columns", Integer.parseInt(String.valueOf(boardInfo.getSize().charAt(0))));
                    bundle.putInt("rows", Integer.parseInt(boardInfo.getSize().substring(boardInfo.getSize().length() - 1)));
                    intent.putExtras(bundle);
                    startActivity(intent);
                    finish();
                }
            });
            menuGridLayout.addView(mainLinLay);
        }
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

    public String loadSharedPrefs(String key) {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS_POOL, MODE_PRIVATE);
        return sharedPreferences.getString(key, "0");
    }
}

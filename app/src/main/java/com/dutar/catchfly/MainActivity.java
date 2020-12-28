package com.dutar.catchfly;

import androidx.appcompat.app.AppCompatActivity;

import android.opengl.Visibility;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Random;

public class MainActivity extends AppCompatActivity {
    int score;
    TextView textScore;
    TextView textViewTime;

    ImageView imageView1, imageView2, imageView3, imageView4, imageView5, imageView6, imageView7, imageView8, imageView9, imageView10, imageView11, imageView12;
    ImageView[] AllImages;

    Runnable runnable;
    Handler handler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();

        CountDowner();

        otoHideFly();
    }

    private void init() {
        imageView1 = findViewById(R.id.imageView1);
        imageView2 = findViewById(R.id.imageView2);
        imageView3 = findViewById(R.id.imageView3);
        imageView4 = findViewById(R.id.imageView4);
        imageView5 = findViewById(R.id.imageView5);
        imageView6 = findViewById(R.id.imageView6);
        imageView7 = findViewById(R.id.imageView7);
        imageView8 = findViewById(R.id.imageView8);
        imageView9 = findViewById(R.id.imageView9);
        imageView10 = findViewById(R.id.imageView10);
        imageView11 = findViewById(R.id.imageView11);
        imageView12 = findViewById(R.id.imageView12);

        AllImages = new ImageView[]{imageView1, imageView2, imageView3, imageView4, imageView5, imageView6, imageView7, imageView8, imageView9, imageView10, imageView11, imageView10, imageView12};

        score = 0;
        textViewTime = findViewById(R.id.textViewTime);

        textScore = findViewById(R.id.textScore);
        textScore.setText("Skor: " + score);
    }

    public void CountDowner() {
        new CountDownTimer(10000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                textViewTime.setText("Kalan süre: " + millisUntilFinished / 1000);
            }

            @Override
            public void onFinish() {
                textViewTime.setText("Kalan süre: 0");
            }
        }.start();
    }

    private void otoHideFly() {
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                for (ImageView image : AllImages) {
                    image.setVisibility(View.INVISIBLE);
                }
                Random random = new Random();
                int index = random.nextInt(AllImages.length);
                AllImages[index].setVisibility(View.VISIBLE);
                handler.postDelayed(this, 500);
            }
        };

        handler.post(runnable);
    }

    public void fly_Click(View view) {
        score++;
        textScore.setText("Skor: " + score);
    }


}
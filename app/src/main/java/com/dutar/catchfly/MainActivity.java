package com.dutar.catchfly;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.AdRequest;

import java.util.Random;

public class MainActivity extends AppCompatActivity {
    TextView textViewHighestScore;
    TextView textViewScore;
    TextView textViewTime;
    ImageView imageView1, imageView2, imageView3, imageView4, imageView5, imageView6, imageView7, imageView8, imageView9, imageView10, imageView11, imageView12;
    ImageView[] AllImages;
    Button buttonStart;

    CountDownTimer countDownTimer;

    Runnable runnable;
    Handler handler;
    int counterSpeed = 500;
    int score = 0;

    SharedPreferences sharedPreferences;
    private final String _highestScore = "HighestScore";
    private InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();

        adMobSettings();

        CountDowner();

        RandomHideFly();
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

        textViewTime = findViewById(R.id.textViewTime);
        textViewScore = findViewById(R.id.textScore);
        textViewHighestScore = findViewById(R.id.textViewHighestScore);
        buttonStart = findViewById(R.id.buttonStart);
        sharedPreferences = this.getSharedPreferences("com.dutar.catchfly", MODE_PRIVATE);

        SetDefaultValues();

        HideAllFly();

        CreateAlertDialog("Hoşgeldin :)", "Sinekleri yakalamaya hazırsan \"BAŞLA\" butonuna tıkla ve sinekleri yakala :)", "Tamam", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

    }

    private void SetDefaultValues() {
        textViewScore.setText("Skor: 0");
        textViewHighestScore.setText("Rekor: " + GetSharedPreferences(_highestScore));
    }

    private void adMobSettings() {
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        //InterstitialAd
        mInterstitialAd = new InterstitialAd(MainActivity.this);
        mInterstitialAd.setAdUnitId("xxxxxxxxxxxx");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());

        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                mInterstitialAd.loadAd(new AdRequest.Builder().build());
            }
        });
    }

    //timer
    public void CountDowner() {
        countDownTimer = new CountDownTimer(10000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                textViewTime.setText("Kalan süre: " + millisUntilFinished / 1000);
            }

            @Override
            public void onFinish() {
                textViewTime.setText("Kalan süre: 0");
                handler.removeCallbacks(runnable);

                HideAllFly();
                buttonStart.setVisibility(View.VISIBLE);

                FinishGame();
            }
        };
    }

    //Counter
    private void RandomHideFly() {
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                HideAllFly();

                ShowRandomFly();

                handler.postDelayed(this, counterSpeed);
            }
        };
    }

    private void HideAllFly() {
        for (ImageView image : AllImages)
            image.setVisibility(View.INVISIBLE);
    }

    private void ShowRandomFly() {
        Random random = new Random();
        int index = random.nextInt(AllImages.length);
        AllImages[index].setVisibility(View.VISIBLE);
    }

    public void Fly_Click(View view) {
        score++;
        textViewScore.setText("Skor: " + score);
    }

    public void ButtonStart_Click(View view) {
        score = 0;
        textViewScore.setText("Skor: " + score);
        buttonStart.setVisibility(View.INVISIBLE);

        countDownTimer.start();
        handler.post(runnable);
    }

    public int GetSharedPreferences(String key) {
        return sharedPreferences.getInt(key, 0);
    }

    public void SetSharedPreferences(String key, int value) {
        sharedPreferences.edit().putInt(key, value).apply();
    }

    public void FinishGame() {
        int highestScore = GetSharedPreferences(_highestScore);

        DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                }
            }
        };

        if (score > highestScore) {
            SetSharedPreferences(_highestScore, score);
            textViewHighestScore.setText("Rekor: " + score);

            CreateAlertDialog("Tebrikler yeni rekor kırdın :)", "Yeni rekor :" + score, "Tamam", listener);

        } else if (score == 0) {
            CreateAlertDialog("Hiç sinek yakalayamadın :(", "Skor :" + score, "Tekrar Dene", listener);

        } else {
            CreateAlertDialog("Rekoru kıramadın :( ", "Skor :" + score, "Tekrar Dene", listener);
        }
    }

    private void CreateAlertDialog(String title, String message, String positiveButtonText, DialogInterface.OnClickListener positiveButton) {
        AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
        alert.setTitle(title)
                .setMessage(message)
                .setPositiveButton(positiveButtonText, positiveButton).setCancelable(false);

        alert.show();
    }
}
package com.skawuma.quizup.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.skawuma.quizup.MainActivity;
import com.skawuma.quizup.R;
import com.skawuma.quizup.activity.QuestionActivity;

public class ResultActivity extends AppCompatActivity {

    String setJson;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        ImageView ivMood = findViewById(R.id.ivMood);
        TextView tvScore = findViewById(R.id.tvScore);
        Button btnRetry = findViewById(R.id.btnRetry);
        Button btnHome = findViewById(R.id.btnHome);

        int score = getIntent().getIntExtra("score", 0);
        int totalQuestions = getIntent().getIntExtra("totalQuestions", 0);
        setJson = getIntent().getStringExtra("setData");

        double percentage = (score / (double) totalQuestions) * 100;

        if (percentage > 50) {
            ivMood.setImageResource(R.drawable.happy_face);
        } else {
            ivMood.setImageResource(R.drawable.better_luck);
        }
        tvScore.setText("You scored: " + score + " out of " + totalQuestions);

        btnRetry.setOnClickListener(v -> {
            Intent intent = new Intent(ResultActivity.this, QuestionActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("setData", setJson);
            startActivity(intent);
            finish();
        });

        btnHome.setOnClickListener(v -> {
            Intent intent = new Intent(ResultActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });
    }
}

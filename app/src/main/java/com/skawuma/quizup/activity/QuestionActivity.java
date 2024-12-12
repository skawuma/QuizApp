package com.skawuma.quizup.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;


import com.skawuma.quizup.MainActivity;
import com.skawuma.quizup.R;
import com.skawuma.quizup.model.MCQ;
import com.skawuma.quizup.model.MCQSet;
import com.skawuma.quizup.utility.LocalStorageHelper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class QuestionActivity extends AppCompatActivity {

    private MCQSet selectedSet;
    private TextView tvQuestion;
    private RadioGroup rgOptions;
    private RadioButton rbOption1, rbOption2, rbOption3, rbOption4;
    private Button btnNext;

    private List<MCQ> questions;
    private int currentQuestionIndex = 0;
    private int score = 0;
    private int selectedOptionIndex = -1;
    String setJson;

    LocalStorageHelper storageHelper = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        storageHelper = new LocalStorageHelper(this);

        tvQuestion = findViewById(R.id.tvQuestion);
        rgOptions = findViewById(R.id.rgOptions);
        rbOption1 = findViewById(R.id.rbOption1);
        rbOption2 = findViewById(R.id.rbOption2);
        rbOption3 = findViewById(R.id.rbOption3);
        rbOption4 = findViewById(R.id.rbOption4);
        btnNext = findViewById(R.id.btnNext);

        setJson = getIntent().getStringExtra("setData");

        if (setJson != null) {
            Gson gson = new Gson();
            selectedSet = gson.fromJson(setJson, MCQSet.class);
            getSupportActionBar().setTitle(selectedSet.getSetName());
            loadQuestionsFromFile(selectedSet);
        } else {
            Log.e("QuestionActivity", "No set data received.");
        }

        btnNext.setOnClickListener(v -> {
            if (currentQuestionIndex < questions.size() - 1) {
                checkAnswer();
                currentQuestionIndex++;
                loadQuestion(currentQuestionIndex);
            } else {
                checkAnswer();
                showFinalScore();
            }
        });
    }

    public void loadQuestionsFromFile(MCQSet mcqSet) {
        try {
            int rawResourceId = getResources().getIdentifier(mcqSet.getFileName().split("\\.")[0], "raw", getPackageName());
            InputStream inputStream = getResources().openRawResource(rawResourceId);

            InputStreamReader reader = new InputStreamReader(inputStream);
            StringBuilder stringBuilder = new StringBuilder();
            int character;
            while ((character = reader.read()) != -1) {
                stringBuilder.append((char) character);
            }

            String jsonData = stringBuilder.toString();
            Type mcqListType = new TypeToken<List<MCQ>>() {}.getType();
            Gson gson = new Gson();
            questions = gson.fromJson(jsonData, mcqListType);

            mcqSet.setQuestions(questions);
            Collections.shuffle(questions);
            loadQuestion(currentQuestionIndex);

        } catch (Exception e) {
            Log.e("QuestionActivity", "Error loading questions for " + mcqSet.getFileName(), e);
        }
    }

    private void loadQuestion(int questionIndex) {
        if (questionIndex < questions.size()) {
            MCQ question = questions.get(questionIndex);
            tvQuestion.setText(question.getQuestion());

            List<String> options = question.getOptions();
            Collections.shuffle(options);
            rbOption1.setText(options.get(0));
            rbOption2.setText(options.get(1));
            rbOption3.setText(options.get(2));
            rbOption4.setText(options.get(3));

            rgOptions.clearCheck();
            selectedOptionIndex = -1;
            updateTitle(questionIndex);

            if (questionIndex == questions.size() - 1) {
                btnNext.setText("Finish");
            } else {
                btnNext.setText("Next");
            }
        }
    }

    private void checkAnswer() {
        int selectedOptionId = rgOptions.getCheckedRadioButtonId();
        if (selectedOptionId != -1) {
            RadioButton selectedRadioButton = findViewById(selectedOptionId);
            String selectedOptionText = selectedRadioButton.getText().toString();

            MCQ currentQuestion = questions.get(currentQuestionIndex);
            if (selectedOptionText.equalsIgnoreCase(currentQuestion.getCorrectAnswer())) {
                score++;
            }
        }
    }

    private void showFinalScore() {
        Map<String,String> resultDataMap = new HashMap<>();
        resultDataMap.put("score",String.valueOf(score));
        resultDataMap.put("dateTime", getCurrentTimestamp());
        resultDataMap.put("totalQuestions", String.valueOf(questions.size()));

        Gson gson = new Gson();
        String resultData = gson.toJson(resultDataMap);
        storageHelper.saveData(selectedSet.getSetName(), resultData);

        Intent intent = new Intent(QuestionActivity.this, ResultActivity.class);
        intent.putExtra("score", score);
        intent.putExtra("totalQuestions", questions.size());
        intent.putExtra("setData", setJson);
        startActivity(intent);
        finish();
    }

    public String getCurrentTimestamp() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.getDefault());
        return sdf.format(new Date());
    }


    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setMessage("Are you sure you want to exit the quiz?")
                .setCancelable(false)
                .setPositiveButton("Yes", (dialog, id) -> {
                    super.onBackPressed();
                    Intent intent = new Intent(QuestionActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                })
                .setNegativeButton("No", null)
                .show();
    }

    private void updateTitle(int currentQuestionIndex) {
        getSupportActionBar().setTitle(selectedSet.getSetName() + " : Ques " + (currentQuestionIndex + 1) + " of " + questions.size());

    }
}

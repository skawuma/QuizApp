package com.skawuma.quizup;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.skawuma.quizup.activity.QuestionActivity;
import com.skawuma.quizup.adapter.MCQSetAdapter;
import com.skawuma.quizup.model.MCQSet;
import com.skawuma.quizup.utility.BDUtility;
import com.google.gson.Gson;
import com.skawuma.quizup.R;

import java.util.List;


public class MainActivity extends AppCompatActivity {
    private RecyclerView rvSets;
    private List<MCQSet> mcqSets;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rvSets = findViewById(R.id.rvSets);
        rvSets.setLayoutManager(new LinearLayoutManager(this));

        try {
            mcqSets = BDUtility.readJsonFromRaw(this,R.raw.mcq_sets);

            if (mcqSets != null) {
                for (MCQSet mcqSet : mcqSets) {
                    Log.d("MainActivity", mcqSet.toString());
                }
            }

            MCQSetAdapter adapter = new MCQSetAdapter(mcqSets, set -> {
                Gson gson = new Gson();
                String setJson = gson.toJson(set);

                Intent intent = new Intent(MainActivity.this, QuestionActivity.class);
                intent.putExtra("setData", setJson);
                startActivity(intent);
            }, this);
            rvSets.setAdapter(adapter);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}

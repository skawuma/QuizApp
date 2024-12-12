package com.skawuma.quizup.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.skawuma.quizup.R;
import com.skawuma.quizup.model.MCQSet;
import com.skawuma.quizup.utility.LocalStorageHelper;
import com.google.gson.Gson;

import org.w3c.dom.Text;

import java.util.List;
import java.util.Map;

public class MCQSetAdapter extends RecyclerView.Adapter<MCQSetAdapter.ViewHolder> {
    private List<MCQSet> mcqSets;
    private OnSetClickListener listener;
    LocalStorageHelper storageHelper = null;


    public interface OnSetClickListener {
        void onSetClick(MCQSet set);
    }

    public MCQSetAdapter(List<MCQSet> mcqSets, OnSetClickListener listener, Context context) {
        this.mcqSets = mcqSets;
        this.listener = listener;
        storageHelper =  new LocalStorageHelper(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mcq_set, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MCQSet set = mcqSets.get(position);
        holder.tvSetName.setText(set.getSetName());

        String resultData = storageHelper.getData(set.getSetName());
        if(resultData!=null) {
            Gson gson = new Gson();
            Map<String, String> resultDataMap = gson.fromJson(resultData, Map.class);
            String score = resultDataMap.get("score");
            String dateTime = resultDataMap.get("dateTime");
            String totalQuestions = resultDataMap.get("totalQuestions");

            double percentage = (Integer.parseInt(score) / (double) Integer.parseInt(totalQuestions)) * 100;
            holder.tvLastAttemptScore.setText(score +" out of "+getItemCount());
            if (percentage < 50) {
                holder.tvRetryMessage.setVisibility(View.VISIBLE);
            }

            holder.tvDateTime.setText(dateTime);
        }else{
            holder.tvDateTime.setVisibility(View.GONE);
            holder.tvLastAttemptScore.setText("Not Attempted");
        }
        holder.itemView.setOnClickListener(v -> listener.onSetClick(set));
    }

    @Override
    public int getItemCount() {
        return mcqSets.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvSetName;
        TextView tvDateTime;
        TextView tvLastAttemptScore;
        TextView tvRetryMessage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvSetName = itemView.findViewById(R.id.tvSetName);
            tvDateTime = itemView.findViewById(R.id.tvDateTime);
            tvLastAttemptScore = itemView.findViewById(R.id.tvLastAttemptScore);
            tvRetryMessage = itemView.findViewById(R.id.tvRetryMessage);
        }
    }
}

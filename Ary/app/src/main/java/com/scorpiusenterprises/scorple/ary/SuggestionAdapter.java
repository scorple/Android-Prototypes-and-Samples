package com.scorpiusenterprises.scorple.ary;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Scorple on 10/1/2016.
 */

public class SuggestionAdapter extends RecyclerView.Adapter<SuggestionAdapter.SuggestionViewHolder> {

    ArrayList<SuggestionType> suggestionTypeList;

    SuggestionAdapter() {
        suggestionTypeList = new ArrayList<>();

        SuggestionType suggestionType = new SuggestionType();

        suggestionTypeList.add(suggestionType);
    }

    @Override
    public SuggestionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.suggestion_card_layout, parent, false);

        return new SuggestionViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(SuggestionViewHolder holder, int position) {
        SuggestionType suggestionType = suggestionTypeList.get(position);

        holder.lbl_suggestion.setText(suggestionType.getSuggestionName());
        holder.card_suggestion.setOnClickListener(new SuggestionClickListener(suggestionType));
    }

    @Override
    public int getItemCount() {
        return suggestionTypeList.size();
    }

    class SuggestionViewHolder extends RecyclerView.ViewHolder {
        CardView card_suggestion;
        TextView lbl_suggestion;

        public SuggestionViewHolder(View itemView) {
            super(itemView);

            card_suggestion = (CardView) itemView.findViewById(R.id.card_suggestion);
            lbl_suggestion = (TextView) itemView.findViewById(R.id.lbl_suggestion);
        }
    }

    private class SuggestionClickListener implements View.OnClickListener {

        SuggestionType suggestionType;

        SuggestionClickListener(SuggestionType suggestionType) {
            this.suggestionType = suggestionType;
        }

        @Override
        public void onClick(View v) {

        }
    }
}

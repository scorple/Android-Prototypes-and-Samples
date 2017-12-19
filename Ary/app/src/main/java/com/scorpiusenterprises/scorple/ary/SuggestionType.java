package com.scorpiusenterprises.scorple.ary;

import java.util.ArrayList;

/**
 * Created by Scorple on 10/1/2016.
 */

public class SuggestionType {

    String suggestionName;
    ArrayList<String> suggestionList;

    SuggestionType() {
        suggestionName = "name";
        suggestionList = new ArrayList<>();
    }

    SuggestionType(String name) {
        suggestionName = name;
        suggestionList = new ArrayList<>();
    }

    public String getSuggestionName() {
        return suggestionName;
    }

    public void setSuggestionName(String suggestionName) {
        this.suggestionName = suggestionName;
    }

    public ArrayList<String> getSuggestionList() {
        return suggestionList;
    }

    public void setSuggestionList(ArrayList<String> suggestionList) {
        this.suggestionList = suggestionList;
    }

}

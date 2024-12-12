package com.skawuma.quizup.model;

import java.util.List;

public class MCQSet {
    private String setName;
    private String fileName;
    private List<MCQ> questions;

    public MCQSet(String setName, String fileName) {
        this.setName = setName;
        this.fileName = fileName;
    }

    public String getSetName() {
        return setName;
    }

    public String getFileName() {
        return fileName;
    }

    public List<MCQ> getQuestions() {
        return questions;
    }

    public void setQuestions(List<MCQ> questions) {
        this.questions = questions;
    }

    @Override
    public String toString() {
        return "MCQSet{" +
                "setName='" + setName + '\'' +
                ", fileName='" + fileName + '\'' +
                ", questions=" + questions +
                '}';
    }
}

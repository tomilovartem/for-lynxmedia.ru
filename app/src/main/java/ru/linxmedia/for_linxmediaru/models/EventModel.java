package ru.linxmedia.for_linxmediaru.models;

import java.io.Serializable;

public class EventModel implements Serializable {
     private String team1;
     private String team2;
     private String time;
     private String tournament;
     private String place;
     private ArticleModel[] article;
     private String prediction;

    public EventModel(String team1, String team2, String time, String tournament, String place, ArticleModel[] article, String prediction) {
        this.team1 = team1;
        this.team2 = team2;
        this.time = time;
        this.tournament = tournament;
        this.place = place;
        this.article = article;
        this.prediction = prediction;
    }

    public String getTeam1() {
        return team1;
    }

    public void setTeam1(String team1) {
        this.team1 = team1;
    }

    public String getTeam2() {
        return team2;
    }

    public void setTeam2(String team2) {
        this.team2 = team2;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTournament() {
        return tournament;
    }

    public void setTournament(String tournament) {
        this.tournament = tournament;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public ArticleModel[] getArticle() {
        return article;
    }

    public void setArticle(ArticleModel[] article) {
        this.article = article;
    }

    public String getPrediction() {
        return prediction;
    }

    public void setPrediction(String prediction) {
        this.prediction = prediction;
    }


}

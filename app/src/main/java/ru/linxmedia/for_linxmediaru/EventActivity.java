package ru.linxmedia.for_linxmediaru;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Arrays;
import java.util.List;

import ru.linxmedia.for_linxmediaru.models.ArticleModel;
import ru.linxmedia.for_linxmediaru.models.EventModel;

public class EventActivity extends AppCompatActivity {

    private EventModel event;
    private TextView team1;
    private TextView team2;
    private TextView time;
    private TextView tournament;
    private TextView place;
    private TextView prediction;
    private LinearLayout scrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        Intent intent = getIntent();
        if(intent.hasExtra(MainActivity.EXTRA_EVENT)){
            event = (EventModel) intent.getSerializableExtra(MainActivity.EXTRA_EVENT);
        }
        if(event!=null){
            String title = (event.getTeam1()!=null &&  event.getTeam2()!=null)? event.getTeam1() + " vs " + event.getTeam2(): " ";
            setTitle(title);
            showEvent(event);
        }

    }

    private void showEvent(EventModel event) {

        team1 = (TextView) findViewById(R.id.team1);
        team1.setVisibility(event.getTeam1() == null ? View.GONE : View.VISIBLE);
        team1.setText(event.getTeam1() == null ? "" : event.getTeam1());

        team2 = (TextView) findViewById(R.id.team2);
        team2.setVisibility(event.getTeam2() == null ? View.GONE : View.VISIBLE);
        team2.setText(event.getTeam2() == null ? "" : event.getTeam2());

        time = (TextView) findViewById(R.id.time);
        time.setVisibility(event.getTime() == null ? View.GONE : View.VISIBLE);
        time.setText(event.getTime() == null ? "" : event.getTime());

        tournament = (TextView) findViewById(R.id.tournament);
        tournament.setVisibility(event.getTournament() == null ? View.GONE : View.VISIBLE);
        tournament.setText(event.getTournament() == null ? "" : event.getTournament());

        place = (TextView) findViewById(R.id.place);
        place.setVisibility(event.getPlace() == null ? View.GONE : View.VISIBLE);
        place.setText(event.getPlace() == null ? "" : event.getPlace());

        prediction = (TextView) findViewById(R.id.prediction);
        prediction.setVisibility(event.getPrediction() == null ? View.GONE : View.VISIBLE);
        prediction.setText(event.getPrediction() == null ? "" : event.getPrediction());

        scrollView = (LinearLayout) findViewById(R.id.scrollView);
        if(event.getArticle()!=null && event.getArticle().length!=0){
            List<ArticleModel> articles = Arrays.asList(event.getArticle());
            for(ArticleModel article: articles){
                LayoutInflater layoutInflater = LayoutInflater.from(getApplicationContext());
                View view = layoutInflater.inflate(R.layout.article_item, null);

                TextView article_header = (TextView) view.findViewById(R.id.article_header);
                article_header.setVisibility(article.getHeader() == null ? View.GONE : View.VISIBLE);
                article_header.setText(article.getHeader() == null ? "" : article.getHeader());

                TextView article_text = (TextView) view.findViewById(R.id.article_text);
                article_text.setVisibility(article.getText() == null ? View.GONE : View.VISIBLE);
                article_text.setText(article.getText() == null ? "" : article.getText());

                scrollView.addView(view);
            }
        }

    }

}

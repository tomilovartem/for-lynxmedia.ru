package ru.linxmedia.for_linxmediaru;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.TabHost;
import android.widget.TextView;

import java.util.Arrays;
import java.util.List;

import ru.linxmedia.for_linxmediaru.helpers.HttpHelper;
import ru.linxmedia.for_linxmediaru.helpers.IHttpResponseListener;
import ru.linxmedia.for_linxmediaru.helpers.ToastHelper;
import ru.linxmedia.for_linxmediaru.models.EventModel;
import ru.linxmedia.for_linxmediaru.models.EventsItemModel;
import ru.linxmedia.for_linxmediaru.models.EventsModel;

public class MainActivity extends AppCompatActivity {


    public static final String EXTRA_EVENT = "event";
    private RecyclerView rvEventsList;
    private TabHost tabHost;
    private FrameLayout tabContent;
    private RecyclerView recyclerView;
    private ProgressDialog progressDialog;
    private HorizontalScrollView scrollView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);


        progressDialog = new ProgressDialog(MainActivity.this);

        initTabs();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        progressDialog.dismiss();
    }

    private void initTabs() {

        tabHost = (TabHost) findViewById(R.id.tab_host);
        tabContent = (FrameLayout) findViewById(android.R.id.tabcontent);

        tabHost.setup();

        scrollView = (HorizontalScrollView) findViewById(R.id.scrollView);






        final String[] categories = {"football" , "hockey" , "tennis" , "basketball" , "volleyball" , "cybersport"};

        for(String category: categories){
            Integer tabId = View.generateViewId();
            TabHost.TabSpec tabSpec = tabHost.newTabSpec(category);
            View tabIndicator =  View.inflate(getApplicationContext(), R.layout.tabs_bg, null);
            TextView tv = (TextView) tabIndicator.findViewById(R.id.tab_text);
            tv.setText(getResources().getIdentifier(category, "string", getApplicationContext().getPackageName()));
            tabSpec.setIndicator(tabIndicator);
            tabSpec.setContent(R.id.events_list);
            tabHost.addTab(tabSpec);

        }


        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                Log.d("TAB", tabId);
                getEvents(tabId);


                
                Handler handler = new Handler();
                Runnable tabSelector = new Runnable() {
                    public void run() {
                        View tabView = tabHost.getCurrentTabView();
                        final int scrollPos = tabView.getLeft() - (scrollView.getWidth() - tabView.getWidth()) / 2;
                        scrollView.smoothScrollTo(scrollPos, 0);
                    }

                };
                handler.postDelayed(tabSelector, 10);
            }
        });

        tabHost.setCurrentTab(0);
        recyclerView = (RecyclerView) tabContent.findViewById(R.id.events_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        getEvents(categories[0]);

    }


    private void showWaitDialog() {
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMessage(getString(R.string.loadingStr));
        progressDialog.show();
    }

    private void hideWaitDialog() {
        if (progressDialog != null) progressDialog.hide();
    }



    IHttpResponseListener eventsResponseListener = new IHttpResponseListener<EventsModel>() {

        @Override
        public void onSuccess(EventsModel events, String message, String response) {
            Log.d("onSuccess", message+" "+response);
            hideWaitDialog();
            if(events!=null && events.getEvenst()!= null && events.getEvenst().length > 0)
            {

                recyclerView.setVisibility(View.INVISIBLE);
                recyclerView.setAdapter(new EventsAdapter(events.getEvenst()));
                recyclerView.setVisibility(View.VISIBLE);
            }
            else{
                ToastHelper.showMessageLong(getApplicationContext(), getString(R.string.dataError));
            }
        }



        @Override
        public void onFail(String message) {
            Log.d("onFail", message);
            hideWaitDialog();
            ToastHelper.showMessageLong(getApplicationContext(), getString(R.string.errorDataLoading)+message);

        }

        @Override
        public void onException(String message) {
            Log.d("onException", message);
            hideWaitDialog();
            ToastHelper.showMessageLong(getApplicationContext(), getString(R.string.appException)+message);

        }
    };

    private void getEvents(String category){
        if(isNetworkAvailable()){
            showWaitDialog();
            HttpHelper.Get(URLs.EVENTS,EventsModel.class, eventsResponseListener, category);
        }
        else{
            ToastHelper.showMessageLong(getApplicationContext(), getString(R.string.networkNotAvailable));
        }
    }

    IHttpResponseListener eventResponseListener = new IHttpResponseListener<EventModel>() {

        @Override
        public void onSuccess(EventModel event, String message, String response) {
            Log.d("onSuccess", message+" "+response);
            hideWaitDialog();
            if(event!=null){
                showEvent(event);
            }
            else{
                ToastHelper.showMessageLong(getApplicationContext(), getString(R.string.dataError));
            }
        }



        @Override
        public void onFail(String message) {
            Log.d("onFail", message);
            hideWaitDialog();
            ToastHelper.showMessageLong(getApplicationContext(), getString(R.string.errorDataLoading)+message);
        }

        @Override
        public void onException(String message) {
            Log.d("onException", message);
            hideWaitDialog();
            ToastHelper.showMessageLong(getApplicationContext(), getString(R.string.appException)+message);


        }
    };

    private void getEvent(String article){
        showWaitDialog();
        if(isNetworkAvailable()){
            HttpHelper.Get(URLs.EVENT, EventModel.class, eventResponseListener, article);
        }
            else{
            ToastHelper.showMessageLong(getApplicationContext(), getString(R.string.networkNotAvailable));
        }
    }


    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }



    private void showEvent(EventModel event) {
        Intent intent = new Intent(this, EventActivity.class);
        intent.putExtra(EXTRA_EVENT, event);
        startActivity(intent);
    }



    private class EventHolder extends RecyclerView.ViewHolder implements View.OnClickListener{


        private final TextView title;
        private final TextView place;
        private final TextView time;
        private final TextView coefficient;
        private final TextView preview;
        private EventsItemModel eventsItem;

        private EventHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
            place = (TextView) itemView.findViewById(R.id.place);
            time = (TextView) itemView.findViewById(R.id.time);
            coefficient = (TextView) itemView.findViewById(R.id.coefficient);
            preview = (TextView) itemView.findViewById(R.id.preview);
            itemView.setOnClickListener(this);
        }

        private void bind(EventsItemModel event){
            this.eventsItem = event;
            title.setVisibility(event.getTitle()==null ? View.GONE:View.VISIBLE);
            title.setText(event.getTitle()==null? "":event.getTitle());

            place.setVisibility(event.getPlace()==null ? View.GONE:View.VISIBLE);
            place.setText(event.getPlace()==null? "":event.getPlace());

            coefficient.setVisibility(event.getCoefficient()==null ? View.GONE:View.VISIBLE);
            coefficient.setText(event.getCoefficient()==null? "":event.getCoefficient());

            time.setVisibility(event.getTime()==null ? View.GONE:View.VISIBLE);
            time.setText(event.getTime()==null? "":event.getTime());

            preview.setVisibility(event.getPreview()==null ? View.GONE:View.VISIBLE);
            preview.setText(event.getPreview()==null? "":event.getPreview());
        }

        @Override
        public void onClick(View view) {
            getEvent(eventsItem.getArticle());
        }
    }

    private class EventsAdapter extends RecyclerView.Adapter<EventHolder>{

        private List<EventsItemModel> events;


        private EventsAdapter(EventsItemModel[] events){
            this.events = Arrays.asList(events);
        }


        @Override
        public EventHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getApplicationContext());
            View view = layoutInflater.inflate(R.layout.events_item, parent, false);
            return new EventHolder(view);
        }

        @Override
        public void onBindViewHolder(EventHolder holder, int position) {
            EventsItemModel event = events.get(position);
            holder.bind(event);
        }

        @Override
        public int getItemCount() {
            return events.size();
        }



    }


}

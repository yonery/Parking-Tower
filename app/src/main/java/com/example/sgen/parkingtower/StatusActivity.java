package com.example.sgen.parkingtower;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TabHost;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by hr on 2015-12-26.
 */
public class StatusActivity extends AppCompatActivity{

    TabHost tabHost;
    WebView webview1, webview2;
    String roomNumber, carNumber, Progress_num;
    Button status_carbtn;

    URL url;
    HttpURLConnection conn;
    String line;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);
        // ActionBar의 배경색 변경
        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.navigation_bar_04));
        // 홈 아이콘 표시
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.guide_actionbar_back);
        //액션바 그림자 지우기
        getSupportActionBar().setElevation(0);

        Intent intent = getIntent();
        roomNumber = intent.getStringExtra("RoomNumber");
        carNumber = intent.getStringExtra("CarNumber");

        try {
            url = new URL("http://makesomenoiz.dothome.co.kr/getStatus.php?CarNumber="+carNumber);
            conn = (HttpURLConnection) url.openConnection();
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            Progress_num = br.readLine();
            conn.disconnect();

        }catch (Exception ex) {
            ex.printStackTrace();
        }



        webview1 = (WebView) findViewById(R.id.webview1);
        webview1.getSettings().setJavaScriptEnabled(true);
        webview1.loadUrl("http://makesomenoiz.dothome.co.kr/app_box_graph_carIO.html");

        webview2 = (WebView) findViewById(R.id.webview2);
        webview2.getSettings().setJavaScriptEnabled(true);
        webview2.loadUrl("http://makesomenoiz.dothome.co.kr/app_total_graph.html");

        status_carbtn = (Button) findViewById(R.id.status_direct);
        status_carbtn.setOnClickListener(new WebView.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Progress_num.equals("0")){
                    Intent direct_intent = new Intent(StatusActivity.this, DirectActivity.class);
                    direct_intent.putExtra("RoomNumber", roomNumber);
                    direct_intent.putExtra("CarNumber", carNumber);
                    startActivity(direct_intent);
                    finish();
                }
                else {
                    Intent progress_intent = new Intent(StatusActivity.this, ProgressActivity.class);
                    progress_intent.putExtra("RoomNumber", roomNumber);
                    progress_intent.putExtra("CarNumber", carNumber);
                    startActivity(progress_intent);
                    finish();
                }
            }
        });

        tabHost = (TabHost) findViewById(R.id.tabHost);
        tabHost.setup();

        TabHost.TabSpec spec;

        ImageView tabwidget01 = new ImageView(this);
        tabwidget01.setImageResource(R.drawable.tab1);

        ImageView tabwidget02 = new ImageView(this);
        tabwidget02.setImageResource(R.drawable.tab2);

        spec = tabHost.newTabSpec("tab01").setIndicator(tabwidget01)
                .setContent(R.id.tab1);
        tabHost.addTab(spec);

        spec = tabHost.newTabSpec("tab02").setIndicator(tabwidget02)
                .setContent(R.id.tab2);
        tabHost.addTab(spec);

        tabHost.setCurrentTab(0);


        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                for (int i = 0; i < tabHost.getTabWidget().getChildCount(); i++) {
                    tabHost.getTabWidget().getChildAt(i)
                            .setBackgroundColor(Color.parseColor("#273238"));
                    tabHost.getTabWidget().getChildAt(tabHost.getCurrentTab())
                            .setBackgroundColor(Color.parseColor("#273238"));
                }
            }
        });

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int scrrenHeight = metrics.heightPixels;
        tabHost.getTabWidget().getChildAt(0).getLayoutParams().height = (scrrenHeight*15)/200;
        tabHost.getTabWidget().getChildAt(1).getLayoutParams().height = (scrrenHeight*15)/200;



    }

    @Override
    public void onBackPressed(){
        Intent back_intent = new Intent(StatusActivity.this, ParkingActivity.class);
        back_intent.putExtra("RoomNumber", roomNumber);
        back_intent.putExtra("CarNumber", carNumber);
        startActivity(back_intent);
        finish();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home){
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

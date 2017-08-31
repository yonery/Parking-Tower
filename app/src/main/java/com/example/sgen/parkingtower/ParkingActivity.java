package com.example.sgen.parkingtower;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by hr on 2015-12-26.
 */
public class ParkingActivity extends AppCompatActivity{

    String Progress_num, roomNumber, carNumber, reserv_cars, reserv_time;
    Button directparkingbtn, parkingstatusbtn, nfctaggingbtn;
    LinearLayout dashboard;
    WebView parkwebview;
    TextView txt1, txt2;
    GitView2 gitView2;
    GitView3 gitView3;
    GitView4 gitView4;
    URL url;
    HttpURLConnection conn;
    String line;
    int tmp,reserve_timeMin,reserve_timeSec;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parking);

        // ActionBar의 배경색 변경
        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.navigation_bar_02));
        // 홈 아이콘 표시
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.guide_actionbar_back);

        //액션바 그림자 지우기
        getSupportActionBar().setElevation(0);


        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .permitDiskReads()
                .permitDiskWrites()
                .permitNetwork().build());


        Intent progress_intent = getIntent();
        roomNumber = progress_intent.getStringExtra("RoomNumber");
        //roomNumber에 해당하는 carNumber

        try {
            url = new URL("http://makesomenoiz.dothome.co.kr/getCarNumber.php?RoomNumber="+roomNumber);
            System.out.println("RoomNumber="+roomNumber);
            conn = (HttpURLConnection) url.openConnection();
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            line = br.readLine();
            carNumber=line;
            System.out.println("carNumber="+carNumber);

        }catch (Exception ex) {
            ex.printStackTrace();
        }
        try {
            url = new URL("http://makesomenoiz.dothome.co.kr/getWaitNum.php");
            conn = (HttpURLConnection) url.openConnection();
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            reserv_cars = br.readLine();

        }catch (Exception ex) {
            ex.printStackTrace();
        }
        try {
            url = new URL("http://makesomenoiz.dothome.co.kr/getWaitTime.php");
            conn = (HttpURLConnection) url.openConnection();
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            reserv_time = br.readLine();
            tmp=Integer.parseInt(reserv_time);
            reserve_timeMin=tmp/60;
            reserve_timeSec=tmp%60;

        }catch (Exception ex) {
            ex.printStackTrace();
        }




        Progress_num = progress_intent.getStringExtra("Progress_num");

        directparkingbtn = (Button) findViewById(R.id.directparkingbtn);
        parkingstatusbtn = (Button) findViewById(R.id.parkingstatusbtn);
        nfctaggingbtn = (Button) findViewById(R.id.NFCtaggingbtn);
        dashboard = (LinearLayout) findViewById(R.id.dashboard);


        txt1 = (TextView) findViewById(R.id.parktxt1);
        txt2 = (TextView) findViewById(R.id.parktxt2);
        txt1.setText("현재 "+reserv_cars+"대가 출차 대기중입니다.");
        txt2.setText("예상 출차 "+ reserve_timeMin +"분정도 소요");

        gitView2 = (GitView2) findViewById(R.id.gif_view2);
        gitView3 = (GitView3) findViewById(R.id.gif_view3);
        gitView4 = (GitView4) findViewById(R.id.gif_view4);



        parkwebview = (WebView) findViewById(R.id.parkwebview);
        parkwebview.getSettings().setJavaScriptEnabled(true);
        parkwebview.loadUrl("http://makesomenoiz.dothome.co.kr/app_bar_graph.html");


        try {
            url = new URL("http://makesomenoiz.dothome.co.kr/getStatus.php?CarNumber="+carNumber);
            conn = (HttpURLConnection) url.openConnection();
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            Progress_num = br.readLine();
            conn.disconnect();

        }catch (Exception ex) {
            ex.printStackTrace();
        }

        //출차 신청하고 대기중 상태인 주황
        if(Progress_num.equals("1")){
            dashboard.setBackgroundDrawable(getResources().getDrawable(R.drawable.guide_003_dashboard_02));
            parkwebview.setVisibility(View.GONE);
            txt1.setVisibility(View.VISIBLE);
            txt2.setVisibility(View.VISIBLE);

            gitView2.setVisibility(View.VISIBLE);
            gitView3.setVisibility(View.GONE);
            gitView4.setVisibility(View.GONE);
        }
        //출차 시작한 경우 초록
        else if(Progress_num.equals("2")){
            dashboard.setBackgroundDrawable(getResources().getDrawable(R.drawable.guide_003_dashboard_03));
            parkwebview.setVisibility(View.GONE);
            txt1.setVisibility(View.VISIBLE);
            txt2.setVisibility(View.VISIBLE);

            gitView2.setVisibility(View.GONE);
            gitView3.setVisibility(View.VISIBLE);
            gitView4.setVisibility(View.GONE);

        }
        //출차완료하면 빨강
        else if(Progress_num.equals("3")){
            dashboard.setBackgroundDrawable(getResources().getDrawable(R.drawable.guide_003_dashboard_04));
            parkwebview.setVisibility(View.GONE);
            txt1.setVisibility(View.GONE);
            txt2.setVisibility(View.GONE);

            gitView2.setVisibility(View.GONE);
            gitView3.setVisibility(View.GONE);
            gitView4.setVisibility(View.VISIBLE);
        }

        //그 외의 대기상태 ("0"이거나 null)
        else{
            dashboard.setBackgroundDrawable(getResources().getDrawable(R.drawable.guide_003_dashboard_01_n));
            parkwebview.setVisibility(View.VISIBLE);
            txt1.setVisibility(View.VISIBLE);
            txt2.setVisibility(View.VISIBLE);
            gitView2.setVisibility(View.GONE);
            gitView3.setVisibility(View.GONE);
            gitView4.setVisibility(View.GONE);
        }

        dashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(Progress_num.equals("1")||Progress_num.equals("2")||Progress_num.equals("3")) {
                    Intent Progress_intent = new Intent(ParkingActivity.this, ProgressActivity.class);
                    Progress_intent.putExtra("RoomNumber", roomNumber);
                    Progress_intent.putExtra("CarNumber", carNumber);
                    startActivity(Progress_intent);
                    finish();
                }
                else{
                    //0이거나 null일 경우 클릭해도 이벤트 발생 X.
                }
            }
        });
        directparkingbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Progress_num.equals("0")){
                    Intent direct_intent = new Intent(ParkingActivity.this, DirectActivity.class);
                    direct_intent.putExtra("RoomNumber", roomNumber);
                    direct_intent.putExtra("CarNumber", carNumber);
                    startActivity(direct_intent);
                    finish();
                }
                else{
                    Intent direct_progress_intent = new Intent(ParkingActivity.this, ProgressActivity.class);
                    direct_progress_intent.putExtra("RoomNumber", roomNumber);
                    direct_progress_intent.putExtra("CarNumber", carNumber);
                    startActivity(direct_progress_intent);
                    finish();
                }
            }
        });
        parkingstatusbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent status_intent = new Intent(ParkingActivity.this, StatusActivity.class);
                status_intent.putExtra("RoomNumber", roomNumber);
                status_intent.putExtra("CarNumber", carNumber);
                startActivity(status_intent);
                finish();
            }
        });
        nfctaggingbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent nfc_intent = new Intent(ParkingActivity.this, NfcActivity.class);
                nfc_intent.putExtra("RoomNumber", roomNumber);
                nfc_intent.putExtra("CarNumber", carNumber);
                startActivity(nfc_intent);
                finish();
            }
        });

    }

    @Override
    public void onBackPressed(){
        Intent back_intent = new Intent(ParkingActivity.this, MenuActivity.class);
        back_intent.putExtra("RoomNumber", roomNumber);
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

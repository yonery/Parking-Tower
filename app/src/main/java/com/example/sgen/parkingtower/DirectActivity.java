package com.example.sgen.parkingtower;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by hr on 2015-12-26.
 */
public class DirectActivity extends AppCompatActivity{


    Button direct_requestbtn, yes, no;
    View dialogView;
    String carNumber, roomNumber, reserv_cars, reserv_time;
    GitView1 gitView1;
    TextView txt1, txt2;
    URL url;
    HttpURLConnection conn;
    int tmp,reserve_timeMin,reserve_timeSec;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_direct);

        // ActionBar의 배경색 변경
        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.navigation_bar_02));
        // 홈 아이콘 표시
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.guide_actionbar_back);
        //액션바 그림자 지우기
        getSupportActionBar().setElevation(0);


        Intent intent = getIntent();
        carNumber = intent.getStringExtra("CarNumber");
        roomNumber = intent.getStringExtra("RoomNumber");
        txt1 = (TextView)findViewById(R.id.parktxt1);
        txt2 = (TextView)findViewById(R.id.parktxt2);
        gitView1 = (GitView1) findViewById(R.id.gif_view_direct);

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
        txt1.setText("현재 "+reserv_cars+"대가 출차 대기중입니다.");
        txt2.setText("출차시 "+reserve_timeMin+"분정도 소요");


        direct_requestbtn = (Button) findViewById(R.id.requestbtn);
        direct_requestbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogView = (View) View.inflate(DirectActivity.this, R.layout.activity_direct_dialog, null);
                final Dialog dlg = new Dialog(DirectActivity.this);
                yes = (Button) dialogView.findViewById(R.id.yes);
                no = (Button) dialogView.findViewById(R.id.no);
                dlg.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dlg.setContentView(dialogView);


                yes.setOnClickListener(new View.OnClickListener() {
                                           @Override
                                           public void onClick(View v) {
                                               String line=null;
                                               try {
                                                   URL url = new URL("http://makesomenoiz.dothome.co.kr/getIsBeingIn.php?CarNumber=" + carNumber);

                                                   HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                                                   BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
                                                   line = br.readLine();

                                               }catch (IOException e) {
                                                   e.printStackTrace();
                                               }
                                               if(line.equals("1")){
                                                   try {
                                                       url = new URL("http://makesomenoiz.dothome.co.kr/setCheckOut.php?CarNumber="+carNumber);
                                                       conn = (HttpURLConnection) url.openConnection();
                                                       BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
                                                       line = br.readLine();
                                                       Log.e("error",line);
                                                       System.out.println("error"+carNumber.length());
                                                   } catch (MalformedURLException e) {
                                                       e.printStackTrace();
                                                       Log.e("error1", carNumber);
                                                   } catch (IOException e) {
                                                       e.printStackTrace();
                                                       Log.e("error2", carNumber);
                                                   }

                                                   Intent Progress_intent = new Intent(DirectActivity.this, ProgressActivity.class);
                                                   dlg.dismiss();
                                                   Progress_intent.putExtra("Progress_num", "1");
                                                   Progress_intent.putExtra("CarNumber", carNumber);
                                                   Progress_intent.putExtra("RoomNumber", roomNumber);
                                                   System.out.println("direct-RoomNumber="+roomNumber);
                                                   startActivity(Progress_intent);
                                                   finish();
                                               }
                                               else{
                                                   Toast.makeText(DirectActivity.this, "입차된 차량이 없습니다.", Toast.LENGTH_SHORT).show();
                                                   onBackPressed();
                                               }
                                           }
                                       }

                );

                    no.setOnClickListener(new View.OnClickListener()

                                          {
                                              @Override
                                              public void onClick(View v) {
                                                  dlg.dismiss();
                                                  onBackPressed();
                                              }
                                          }
                    );
                    dlg.show();
                }
            });
    }

    @Override
    public void onBackPressed(){
        Intent back_intent = new Intent(DirectActivity.this, ParkingActivity.class);
        back_intent.putExtra("RoomNumber", roomNumber);
        back_intent.putExtra("Progress_num", "0");
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

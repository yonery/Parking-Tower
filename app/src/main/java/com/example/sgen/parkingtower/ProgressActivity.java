package com.example.sgen.parkingtower;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by hr on 2015-12-26.
 */
public class ProgressActivity extends AppCompatActivity {
    LinearLayout progress_layout;
    View finish_dialogView, cancel_dialogView;
    Button check, backprogressbtn;
    String Progress_num, Progress_num2, carNumber, roomNumber;
    String txt1, txt2, reserv_cars, reserv_time, reserv_cars2;
    GitView2 gitview2;
    GitView3 gitview3;
    GitView4 gitview4;
    GitView5 gitview5;
    TextView progress_txt1, progress_txt2;
    Handler mHandler, cHandler;
    int value = 0;
    int tmp,reserve_timeMin,reserve_timeSec;
    URL url;
    HttpURLConnection conn;


    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (Progress_num.equals("1")) {
                progress_layout.setBackgroundDrawable(getResources().getDrawable(R.drawable.guide_005_back));
                backprogressbtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.guide_005_button));
                gitview2.setVisibility(View.VISIBLE);
                gitview3.setVisibility(View.GONE);
                gitview4.setVisibility(View.GONE);
                progress_txt1.setText("현재 " + reserv_cars + "대가 출차 대기중입니다.");
                progress_txt2.setText("출차시 " + reserve_timeMin + "분정도 소요");
            }
            //출차 시작하거나 NFC 태깅할 경우
            else if (Progress_num.equals("2")) {
                progress_layout.setBackgroundDrawable(getResources().getDrawable(R.drawable.guide_006_back));
                backprogressbtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.guide_006_button));
                gitview2.setVisibility(View.GONE);
                gitview3.setVisibility(View.VISIBLE);
                gitview4.setVisibility(View.GONE);
                progress_txt1.setText("현재 "+reserv_cars+"대가 출차 대기중입니다.");
                progress_txt2.setText("출차시 "+reserve_timeMin+"분정도 소요");
            }
            //출차완료하면 빨강
            else if (Progress_num.equals("3")) {
                progress_layout.setBackgroundDrawable(getResources().getDrawable(R.drawable.guide_007_back));
                backprogressbtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.guide_007_button));
                gitview2.setVisibility(View.GONE);
                gitview3.setVisibility(View.GONE);
                gitview4.setVisibility(View.VISIBLE);
                txt1 = "";
                progress_txt1.setText(txt1);
                progress_txt2.setTextSize(20);
                mHandler = new Handler(){
                    public void handleMessage(Message msg){
                        progress_txt1.setText("");
                        progress_txt2.setText((120 - value)+"초가 남았습니다.");
                        if(value < 6)
                        {
                            mHandler.sendEmptyMessageDelayed(0, 1000);
                        }
                        value++;
                        if(value==6){
                            finish_dialogView = (View) View.inflate(ProgressActivity.this, R.layout.activity_progress_dialog, null);
                            final Dialog dlg = new Dialog(ProgressActivity.this);
                            dlg.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                            check = (Button) finish_dialogView.findViewById(R.id.finish_check);
                            dlg.requestWindowFeature(Window.FEATURE_NO_TITLE);
                            dlg.setContentView(finish_dialogView);
                            dlg.setCancelable(false);
                            dlg.setCanceledOnTouchOutside(false);
                            check.setOnClickListener(new View.OnClickListener() {
                                                         @Override
                                                         public void onClick(View v) {
                                                             try {
                                                                 URL url = new URL("http://makesomenoiz.dothome.co.kr/resetStatus.php?CarNumber=" + carNumber);

                                                                 HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                                                                 BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
                                                                 String line = br.readLine();

                                                             }catch (IOException e) {
                                                                 e.printStackTrace();
                                                             }
                                                             Progress_num = "0";
                                                             onBackPressed();
                                                             dlg.dismiss();
                                                         }
                                                     }
                            );
                            dlg.show();
                        }
                    }
                };
                mHandler.sendEmptyMessage(0);

            } else {
                progress_layout.setBackgroundDrawable(getResources().getDrawable(R.drawable.guide_004_page));
                backprogressbtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.guide_004_button));
            }
        }
    };
    Handler gHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {

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

            progress_txt1.setText("현재 "+reserv_cars+"대가 출차 대기중입니다.");
            progress_txt2.setText("출차시 "+reserve_timeMin+"분정도 소요");


        }
    };
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_direct_progress);

        // ActionBar의 배경색 변경
        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.navigation_bar_05));
        // 홈 아이콘 표시
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.guide_actionbar_back);

        //액션바 그림자 지우기
        getSupportActionBar().setElevation(0);


        Intent intent = getIntent();
        carNumber = intent.getStringExtra("CarNumber");
        roomNumber = intent.getStringExtra("RoomNumber");


        try {
            url = new URL("http://makesomenoiz.dothome.co.kr/getWaitNum.php");
            conn = (HttpURLConnection) url.openConnection();
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            reserv_cars = br.readLine();
            reserv_cars2=reserv_cars;
            Log.e("lastLog",reserv_cars);

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
        progress_txt1 = (TextView) findViewById(R.id.progresstxt1);
        progress_txt2 = (TextView) findViewById(R.id.progresstxt2);
//        progress_txt1.setText("현재 "+reserv_cars+"대가 출차 대기중입니다.");
//        progress_txt2.setText("출차시 "+reserve_timeMin+"분정도 소요");


        backprogressbtn = (Button) findViewById(R.id.backprogressbtn);
        progress_layout = (LinearLayout) findViewById(R.id.progress_layout);

        gitview2 = (GitView2) findViewById(R.id.progress_gif_view2);
        gitview3 = (GitView3) findViewById(R.id.progress_gif_view3);
        gitview4 = (GitView4) findViewById(R.id.progress_gif_view4);





        try {
            URL url = new URL("http://makesomenoiz.dothome.co.kr/getStatus.php?CarNumber=" + carNumber);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            System.out.println("progree-RoomNumber="+roomNumber);
            System.out.println("pregress-CarNumber="+carNumber);
            Progress_num = br.readLine();
            System.out.println("progres-ProgessNumber="+Progress_num);
        }catch (IOException e) {
            e.printStackTrace();
        }

        if (Progress_num.equals("1")) {
            progress_layout.setBackgroundDrawable(getResources().getDrawable(R.drawable.guide_005_back));
            backprogressbtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.guide_005_button));
            gitview2.setVisibility(View.VISIBLE);
            gitview3.setVisibility(View.GONE);
            gitview4.setVisibility(View.GONE);
            progress_txt1.setText("현재 " + reserv_cars + "대가 출차 대기중입니다.");
            progress_txt2.setText("출차시 " + reserve_timeMin + "분정도 소요");
        }
        //출차 시작하거나 NFC 태깅할 경우
        else if (Progress_num.equals("2")) {
            progress_layout.setBackgroundDrawable(getResources().getDrawable(R.drawable.guide_006_back));
            backprogressbtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.guide_006_button));
            gitview2.setVisibility(View.GONE);
            gitview3.setVisibility(View.VISIBLE);
            gitview4.setVisibility(View.GONE);
            progress_txt1.setText("현재 "+reserv_cars+"대가 출차 대기중입니다.");
            progress_txt2.setText("출차시 "+reserve_timeMin+"분정도 소요");

        }
        //출차완료하면 빨강
        else if (Progress_num.equals("3")) {
            progress_layout.setBackgroundDrawable(getResources().getDrawable(R.drawable.guide_007_back));
            backprogressbtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.guide_007_button));
            gitview2.setVisibility(View.GONE);
            gitview3.setVisibility(View.GONE);
            gitview4.setVisibility(View.VISIBLE);
            txt1 = "";
            progress_txt1.setText(txt1);
            progress_txt2.setTextSize(20);
            mHandler = new Handler(){
                public void handleMessage(Message msg){
                    progress_txt1.setText("");
                    progress_txt2.setText((120 - value)+"초가 남았습니다.");
                    if(value < 6)
                    {
                        mHandler.sendEmptyMessageDelayed(0, 1000);
                    }
                    value++;
                    if(value==6){
                        finish_dialogView = (View) View.inflate(ProgressActivity.this, R.layout.activity_progress_dialog, null);
                        final Dialog dlg = new Dialog(ProgressActivity.this);
                        dlg.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        check = (Button) finish_dialogView.findViewById(R.id.finish_check);
                        dlg.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dlg.setContentView(finish_dialogView);
                        dlg.setCancelable(false);
                        dlg.setCanceledOnTouchOutside(false);
                        check.setOnClickListener(new View.OnClickListener() {
                                                     @Override
                                                     public void onClick(View v) {
                                                         try {
                                                             URL url = new URL("http://makesomenoiz.dothome.co.kr/resetStatus.php?CarNumber=" + carNumber);

                                                             HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                                                             BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
                                                             String line = br.readLine();

                                                         }catch (IOException e) {
                                                             e.printStackTrace();
                                                         }
                                                         Progress_num = "0";
                                                         onBackPressed();
                                                         dlg.dismiss();
                                                     }
                                                 }
                        );
                        dlg.show();
                    }
                }
            };
            mHandler.sendEmptyMessage(0);



        } else {
                //예외
        }

        Progress_num2=Progress_num;

        backprogressbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //출차취소와 완료시 구현
                if (Progress_num.equals("3")) {
                        onBackPressed();
                } else {
                    try {
                        URL url = new URL("http://makesomenoiz.dothome.co.kr/setCancleOut.php?CarNumber=" + carNumber);
                        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
                        System.out.println("progress-cancel-carNum=" + carNumber);
                        String line = br.readLine();
                        Log.e("ProgressCarNum1=", line);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                    Progress_num = "0";

                    //출차취소가 완료되는 화면
                    cancel_dialogView = (View) View.inflate(ProgressActivity.this, R.layout.activity_cancel_dialog, null);
                    final Dialog cancel_dlg = new Dialog(ProgressActivity.this);
                    cancel_dlg.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    gitview5 = (GitView5) cancel_dialogView.findViewById(R.id.progress_gif_view5);
                    cancel_dlg.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    cancel_dlg.setContentView(cancel_dialogView);
                    cancel_dlg.setCanceledOnTouchOutside(false);
                    cancel_dlg.setCancelable(false);

                    try {
                        URL url = new URL("http://makesomenoiz.dothome.co.kr/resetStatus.php?CarNumber=" + carNumber);

                        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
                        String line = br.readLine();
                    }catch (IOException e) {
                        e.printStackTrace();
                    }
                    cancel_dlg.show();

                    value=0;
                    cHandler = new Handler() {
                        public void handleMessage(Message msg) {

                            if (value < 10) {
                                cHandler.sendEmptyMessageDelayed(0, 1000);
                            }
                            value++;
                            if (value>10){
                                cancel_dlg.dismiss();
                                Progress_num="0";
                                onBackPressed();
                            }
                        }
                    };
                    cHandler.sendEmptyMessage(0);
                }
            }
        });
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(true) {
                    try {
                        URL url = new URL("http://makesomenoiz.dothome.co.kr/getStatus.php?CarNumber=" + carNumber);
                        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
                        Progress_num = br.readLine();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (Progress_num != null && Progress_num2 != null) {
                        if (!(Progress_num.equals(Progress_num2))) {
                            Progress_num2 = Progress_num;
                            handler.sendEmptyMessage(0);
                        }
                    }
                    try {
                        URL url = new URL("http://makesomenoiz.dothome.co.kr/getWaitNum.php");
                        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
                        reserv_cars = br.readLine();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (reserv_cars != null && reserv_cars2 != null) {
                        if (!(reserv_cars.equals(reserv_cars2))) {
                            reserv_cars2 = reserv_cars;
                            gHandler.sendEmptyMessage(0);
                        }
                    }
                }


            }
        }).start();

    }
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev){
        Rect dialogBounds = new Rect();
        getWindow().getDecorView().getHitRect(dialogBounds);
        if(!dialogBounds.contains((int)ev.getX(), (int)ev.getY())){
            return false;
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public void onBackPressed() {
        Intent back_press = new Intent(ProgressActivity.this, ParkingActivity.class);
        back_press.putExtra("Progress_num", Progress_num);
        back_press.putExtra("RoomNumber", roomNumber);
        back_press.putExtra("CarNumber", carNumber);
        startActivity(back_press);
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
package com.example.sgen.parkingtower;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by hr on 2015-12-26.
 */
public class NfcActivity extends AppCompatActivity {

    String roomNumber, carNumber;
    URL url;
    HttpURLConnection conn;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nfc);
        // ActionBar의 배경색 변경
        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.navigation_bar_03));
        // 홈 아이콘 표시
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.guide_actionbar_back);
        //액션바 그림자 지우기
        getSupportActionBar().setElevation(0);

        Button testnfc = (Button) findViewById(R.id.testnfc);

        Intent intent = getIntent();
        roomNumber = intent.getStringExtra("RoomNumber");
        carNumber = intent.getStringExtra("CarNumber");

        testnfc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String line=null;
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

                Intent Progress_intent = new Intent(NfcActivity.this, ProgressActivity.class);
                Progress_intent.putExtra("Progress_num", "1");
                Progress_intent.putExtra("CarNumber", carNumber);
                Progress_intent.putExtra("RoomNumber", roomNumber);
                System.out.println("direct-RoomNumber="+roomNumber);
                startActivity(Progress_intent);
                finish();

            }
        });
    }

    @Override
    public void onBackPressed(){
        Intent back_intent = new Intent(NfcActivity.this, ParkingActivity.class);
        back_intent.putExtra("RoomNumber", roomNumber);
        back_intent.putExtra("CarNumber", carNumber);
        System.out.println("nfc-RoomNumber="+roomNumber);

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

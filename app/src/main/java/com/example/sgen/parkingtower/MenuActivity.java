package com.example.sgen.parkingtower;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

/**
 * Created by hr on 2015-12-26.
 */
public class MenuActivity extends AppCompatActivity {

    Button parking;
    String roomNumber;

    protected  void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        // ActionBar의 배경색 변경
        getSupportActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.navigation_bar_01));

        //액션바 그림자 지우기
        getSupportActionBar().setElevation(0);

        parking= (Button) findViewById(R.id.parking);

        //MainActivity의 ID와 같은 RoomNumber 받아오기
        Intent intent = getIntent();
        roomNumber = intent.getStringExtra("RoomNumber");

        parking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent parkIntent = new Intent(MenuActivity.this, ParkingActivity.class);
                parkIntent.putExtra("Progress_num", "0");
                parkIntent.putExtra("RoomNumber", roomNumber);
                System.out.println("RoomNumber="+roomNumber);
                startActivity(parkIntent);
                finish();
            }
        });
    }


    public void onBackPressed() {
        AlertDialog.Builder d = new AlertDialog.Builder(this);
        d.setMessage("정말 종료하시겠습니까?");
        d.setPositiveButton("예", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                // process전체 종료
                finish();
            }
        });
        d.setNegativeButton("아니요", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        d.show();
    }
}

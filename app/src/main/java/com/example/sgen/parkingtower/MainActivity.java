package com.example.sgen.parkingtower;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    EditText room, password, regi_room, regi_pass, regi_car;
    View dialogView;
    Button login, register, findID;
    String carNumber, roomNumber, passwordNumber;
    phpInsert phpRegister;
    phpLogin phpLoginCheck;
    String line;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startActivity(new Intent(MainActivity.this, SplashActivity.class));
        setContentView(R.layout.activity_main);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


        room = (EditText) findViewById(R.id.room);
        password = (EditText) findViewById(R.id.password);
        login = (Button) findViewById(R.id.login);
        register = (Button) findViewById(R.id.register);
        findID = (Button) findViewById(R.id.findID);

        phpRegister = new phpInsert();
        phpLoginCheck = new phpLogin();

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (room.length() <= 0)
                    Toast.makeText(MainActivity.this, "Enter again", Toast.LENGTH_LONG).show();
                else {
                    //로그인 php 구현

                    roomNumber = room.getText().toString();
                    passwordNumber = password.getText().toString();
                    phpLoginCheck=new phpLogin();
                    phpLoginCheck.execute("http://makesomenoiz.dothome.co.kr/phpLogin.php?RoomNumber=" + roomNumber + "&Password=" + passwordNumber);


                }
            }
        });


        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogView = (View) View.inflate(MainActivity.this, R.layout.activity_register, null);
                AlertDialog.Builder dlg = new AlertDialog.Builder(MainActivity.this);
                dlg.setTitle("Resister");
                dlg.setView(dialogView);
                dlg.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        regi_car = (EditText) dialogView.findViewById(R.id.register_car);
                        regi_room = (EditText) dialogView.findViewById(R.id.register_room);
                        regi_pass = (EditText) dialogView.findViewById(R.id.register_pass);
                        carNumber = regi_car.getText().toString();
                        roomNumber = regi_room.getText().toString();
                        passwordNumber = regi_pass.getText().toString();
                        phpRegister.execute("http://makesomenoiz.dothome.co.kr/phpInsert.php?RoomNumber="+roomNumber+"&Password="+passwordNumber+"&CarNumber="+carNumber);
                        //                    insertTest.execute("http://makesomenoiz.dothome.co.kr/phpInsert.php?RoomNumber=" + roomNumber + "&CarNumber=" + passwordNumber + "&UserName=" + user);
                    }
                });
                dlg.setNegativeButton("취소", null);
                dlg.show();

            }
        });



    }
    private class phpInsert extends AsyncTask<String, Integer, String> {


        @Override
        protected String doInBackground(String... urls) {
            Log.e("tag", urls[0]);
            StringBuilder jsonHtml = new StringBuilder();

            try {
                // 연결 url 설정
                URL url = new URL(urls[0]);
                // 커넥션 객체 생성
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                // 연결되었으면.
                if (conn != null) {
                    conn.setConnectTimeout(10000);
                    conn.setUseCaches(false);
                    // 연결되었음 코드가 리턴되면.
                    if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
                       String line = br.readLine();
                        if(line!=null){

                        }
                        br.close();
                    }
                    conn.disconnect();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            return jsonHtml.toString();

        }

        protected void onPostExecute(String str) {
            Toast.makeText(MainActivity.this, "Insert 안됨", Toast.LENGTH_SHORT).show();
        }
    }
    private class phpLogin extends AsyncTask<String, Integer, String> {


        @Override
        protected String doInBackground(String... urls) {
            Log.e("tag", urls[0]);
            line=null;
            try {
                // 연결 url 설정

                URL url = new URL(urls[0]);
                // 커넥션 객체 생성
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                // 연결되었으면.
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
                line= br.readLine();

            } catch (Exception ex) {
                ex.printStackTrace();
            }

            System.out.println("main-line="+line);
            return line;

        }

        @Override
        protected void onPostExecute(String s) {
            if(line!=null) {
                if(line.equals("success")) {
                    System.out.println("아마도 성공");
                    Intent intent = new Intent(MainActivity.this, MenuActivity.class);
                    intent.putExtra("RoomNumber", roomNumber);
                    startActivity(intent);
                    finish();
                }
                else{
                    Toast.makeText(MainActivity.this,"입력한 정보가 올바르지 않습니다.",Toast.LENGTH_SHORT).show();
                    System.out.println("main-line else 성공!");
                    phpLoginCheck.cancel(true);
                }
            }

        }
    }
}
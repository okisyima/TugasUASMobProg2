package com.example.tugasuasmobprog2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    Button btnLogout;
    TextView txtId;
    TextView txtNickname;
    String id;
    String nickname;
    SharedPreferences sharedPreferences;

    public static final String TAG_ID = "id";
    public static final String TAG_NICKNAME = "nickname";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtId = (TextView) findViewById(R.id.txt_id);
        txtNickname = (TextView) findViewById(R.id.txt_nickname);
        btnLogout = (Button) findViewById(R.id.btn_logout);

        sharedPreferences = getSharedPreferences(Login.my_shared_pref, Context.MODE_PRIVATE);

        id = getIntent().getStringExtra(TAG_ID);
        nickname = getIntent().getStringExtra(TAG_NICKNAME);

        txtId.setText("ID : " + id);
        txtNickname.setText("NICKNAME : " + nickname);

        btnLogout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean(Login.session_stat, false);
                editor.putString(TAG_ID, null);
                editor.putString(TAG_NICKNAME, null);
                editor.commit();

                Intent intent = new Intent(MainActivity.this, Login.class);
                finish();
                startActivity(intent);
            }
        });
    }
}

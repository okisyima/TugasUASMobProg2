package com.example.tugasuasmobprog2;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.tugasuasmobprog2.paa.Controller;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity {

    ProgressDialog progressDialog;
    Button btnRegister;
    Button btnLogin;
    EditText txtNickname;
    EditText txtPassword;
    Intent intent;

    int success;
    ConnectivityManager connectivityManager;

    private String url = Server.URL + "login.php";

    private static final String TAG = Login.class.getSimpleName();

    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";

    private static final String TAG_NICKNAME = "nickname";
    private static final String TAG_ID = "id";

    String tag_json_obj = "json_obj_req";

    SharedPreferences sharedPreferences;
    Boolean session = false;
    String id, nickname;
    public static final String my_shared_pref = "my_shared_preferences";
    public static final String session_stat = "session_status";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        {
            if (connectivityManager.getActiveNetworkInfo() != null
            && connectivityManager.getActiveNetworkInfo().isAvailable()
            && connectivityManager.getActiveNetworkInfo().isConnected()) {

            } else {
                Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_LONG).show();
            }
        }

        btnLogin    = (Button) findViewById(R.id.btn_login);
        btnRegister = (Button) findViewById(R.id.btn_register);
        txtNickname = (EditText) findViewById(R.id.txt_nickname);
        txtPassword = (EditText) findViewById(R.id.txt_password);

        sharedPreferences = getSharedPreferences(my_shared_pref, Context.MODE_PRIVATE);
        session = sharedPreferences.getBoolean(session_stat, false);
        id = sharedPreferences.getString(TAG_ID, null);
        nickname = sharedPreferences.getString(TAG_NICKNAME, null);

        if (session) {

            Intent intent = new Intent(Login.this, MainActivity.class);
            intent.putExtra(TAG_ID, id);
            intent.putExtra(TAG_NICKNAME, nickname);
            finish();
            startActivity(intent);
        }

        btnLogin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String nickname = txtNickname.getText().toString();
                String password = txtPassword.getText().toString();

                if (nickname.trim().length() > 0 && password.trim().length() > 0) {
                    if (connectivityManager.getActiveNetworkInfo() != null
                    && connectivityManager.getActiveNetworkInfo().isAvailable()
                    && connectivityManager.getActiveNetworkInfo().isConnected()) {
                        checkLogin(nickname, password);
                    } else {
                        Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Kolom tidak boleh kosong", Toast.LENGTH_LONG).show();
                }
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                intent = new Intent(Login.this, Register.class);
                finish();
                startActivity(intent);
            }
        });
    }

    private void checkLogin(final String nickname, final String password) {

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Logging in..");
        showDialog();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e(TAG, "Register Response : " + response.toString());
                hideDialog();

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    success = jsonObject.getInt(TAG_SUCCESS);

                    if (success == 1) {
                        String nickname = jsonObject.getString(TAG_NICKNAME);
                        String id = jsonObject.getString(TAG_ID);

                        Log.e("Successfully Login!", jsonObject.toString());

                        Toast.makeText(getApplicationContext(), jsonObject.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();

                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putBoolean(session_stat, true);
                        editor.putString(TAG_ID, id);
                        editor.putString(TAG_NICKNAME, nickname);
                        editor.commit();

                        Intent intent = new Intent(Login.this, MainActivity.class);
                        intent.putExtra(TAG_ID, id);
                        intent.putExtra(TAG_NICKNAME, nickname);
                        finish();
                        startActivity(intent);

                    } else {
                        Toast.makeText(getApplicationContext(), jsonObject.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Login Error : " + error.getMessage());
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();

                hideDialog();
            }

    }) {
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<String, String>();
                params.put("nickname", nickname);
                params.put("password", password);

                return params;
            }

        };

        Controller.getInstance().addToReqQueue(stringRequest, tag_json_obj);
    }

    public void showDialog() {
        if (!progressDialog.isShowing())
            progressDialog.show();
    }

    public void hideDialog() {
        if (progressDialog.isShowing())
            progressDialog.dismiss();
    }

}

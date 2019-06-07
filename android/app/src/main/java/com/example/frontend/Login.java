package com.example.frontend;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import android.widget.Toast;
import okhttp3.RequestBody;
import okhttp3.MediaType;
import android.content.Context;
import android.database.Cursor;

import com.example.frontend.APIManager.DataHolder;
import com.example.frontend.APIManager.LandingPageResponse;
import com.example.frontend.APIManager.LoginResponse;
import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Login extends AppCompatActivity implements View.OnClickListener{
    private EditText username;
    private EditText password;
    private Button login;

    @Override
    public void onBackPressed() {


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);



        username = findViewById(R.id.uname);
        password = findViewById(R.id.input_password);
        login = (Button) findViewById(R.id.blogin);
        login.setOnClickListener(this);



    }

    @Override
    public void onClick(View v) {
        System.out.println("Click");
        loginApi ();
    }

    private void loginApi () {
        String usernameValue = username.getText().toString().trim();
        String passwordValue = password.getText().toString().trim();

        if (usernameValue.isEmpty()||passwordValue.isEmpty()) {
            login.setEnabled(true);
            Toast.makeText(Login.this, "Please Provide All Fields", Toast.LENGTH_LONG).show();

            return;
        }

        Call <LoginResponse> call = RetrofitClient.getInstance()
                .getApi()
                .login(usernameValue, passwordValue);

        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {

                try{
                    LoginResponse lr = response.body();
                    System.out.println(response.body());

                    DataHolder dh = new DataHolder();
                    dh.setTOKEN("Token "+ lr.getToken());
                    dh.setID(lr.getId());
                    System.out.println("Login Token  and ID" +lr.getToken()+" "+lr.getId());
                    landingPageApi(dh);
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Toast.makeText(Login.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void landingPageApi (final DataHolder dh) {
        Call <LandingPageResponse> call = RetrofitClient.getInstance()
                .getApi()
                .getLandingPage(dh.getID(), dh.getTOKEN());


        call.enqueue(new Callback<LandingPageResponse>() {
            @Override
            public void onResponse(Call<LandingPageResponse> call, Response<LandingPageResponse> response) {
                try {
                    LandingPageResponse lpr = response.body();
                    System.out.println("Success "+ response.code());
                    System.out.println("Landing Page " + lpr);
                    dh.setLPR(lpr);

                    ;
                    if (lpr.getIs_student()) {
                        Intent i = new Intent(Login.this, Student_Page.class);
                        Gson gson = new Gson();
                        String myJson = gson.toJson(dh);
                        i.putExtra("myjson", myJson);
                        startActivity(i);
                    } else {
                        Intent i = new Intent(Login.this, Teacher_page.class);
                        Gson gson = new Gson();
                        String myJson = gson.toJson(dh);
                        i.putExtra("myjson", myJson);
                        startActivity(i);
                    }
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<LandingPageResponse> call, Throwable t) {
                Toast.makeText(Login.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });


    }
}


package com.example.frontend;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.frontend.APIManager.CreateClassResponse;
import com.example.frontend.APIManager.DataHolder;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CreateClassRoom extends AppCompatActivity implements View.OnClickListener {
    private EditText clss;
    private EditText courseNo;
    private Button bcreateclass;

    private static DataHolder dh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_classroom);

        Gson gson = new Gson();
        dh = gson.fromJson(getIntent().getStringExtra("myjson"), DataHolder.class);

        clss = findViewById(R.id.clss);
        courseNo = findViewById(R.id.courseNo);
        bcreateclass = findViewById(R.id.bcreateclass);
        bcreateclass.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        String classNameValue=clss.getText().toString().trim();
        String courseNoValue = courseNo.getText().toString().trim();
        if (classNameValue.isEmpty() || courseNoValue.isEmpty()) {
            Toast.makeText(this, "Empty Field", Toast.LENGTH_LONG).show();
            return;
        }

        createClass(classNameValue, courseNoValue);

    }

    private void createClass(String classNameValue, String courseNoValue){

        Call<CreateClassResponse> call = RetrofitClient.getInstance()
                .getApi()
                .createClass(dh.getTOKEN(), classNameValue, courseNoValue);

        call.enqueue(new Callback<CreateClassResponse>() {
            @Override
            public void onResponse(Call<CreateClassResponse> call, Response<CreateClassResponse> response) {

                try{
                    CreateClassResponse lr = response.body();
                    System.out.println("Success Create Class room"+ response.isSuccessful());

                    Intent i = new Intent(CreateClassRoom.this, Teacher_page.class);
                    Gson gson = new Gson();
                    String myJson = gson.toJson(dh);
                    i.putExtra("myjson", myJson);
                    startActivity(i);

                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<CreateClassResponse> call, Throwable t) {
                Toast.makeText(CreateClassRoom.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }
}

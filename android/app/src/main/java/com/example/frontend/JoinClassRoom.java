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

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class JoinClassRoom extends AppCompatActivity implements View.OnClickListener {
    private EditText clss;
    private EditText courseNo;
    private Button bcreateclass;

    private static DataHolder dh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_classroom);

        Gson gson = new Gson();
        dh = gson.fromJson(getIntent().getStringExtra("myjson"), DataHolder.class);

        courseNo = findViewById(R.id.courseId);
        bcreateclass = findViewById(R.id.bJoinclass);
        bcreateclass.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        String courseNoValue = courseNo.getText().toString().trim();
        if (courseNoValue.isEmpty()) {
            Toast.makeText(this, "Empty Field", Toast.LENGTH_LONG).show();
            return;
        }

        createClass(courseNoValue);

    }

    private void createClass(String courseNoValue){

        Call<ResponseBody> call = RetrofitClient.getInstance()
                .getApi()
                .joinClass(dh.getTOKEN(), courseNoValue);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                try{
                    Intent i = new Intent(JoinClassRoom.this, Student_Page.class);
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
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(JoinClassRoom.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }
}

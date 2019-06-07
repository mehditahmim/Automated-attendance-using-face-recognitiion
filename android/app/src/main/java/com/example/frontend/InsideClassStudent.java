package com.example.frontend;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.frontend.APIManager.ClassAttendanceResponse;
import com.example.frontend.APIManager.ClassResponse;
import com.example.frontend.APIManager.DataHolder;
import com.example.frontend.APIManager.DayAttendance;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InsideClassStudent extends AppCompatActivity {
    private static DataHolder dh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insideclass_student);
        Intent intent=getIntent();
        Gson gson = new Gson();
        dh = gson.fromJson(getIntent().getStringExtra("myjson"), DataHolder.class);
        populateListView();
    }


    private void populateListView() {

        Call<List<DayAttendance>> call = RetrofitClient.getInstance()
                .getApi()
                .insideAtte(dh.getClass_id(), dh.getTOKEN());

        call.enqueue(new Callback<List<DayAttendance>>() {
            @Override
            public void onResponse(Call<List<DayAttendance>> call, Response<List<DayAttendance>> response) {

                try{
                    List<DayAttendance> ls = response.body();
                    ArrayList<String> classList = new ArrayList<>();


                    for (DayAttendance d : ls) {
                        String s= d.getStatus()+" on "+d.getDay();
                        classList.add(s);
                    }


                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(InsideClassStudent.this,R.layout.activity_listview,classList){
                        @Override
                        public View getView(int position, View convertView, ViewGroup parent){
                            // Get the current item from ListView
                            View view = super.getView(position,convertView,parent);
                            if(position %2 == 1)
                            {
                                // Set a background color for ListView regular row/item
                                view.setBackgroundColor(Color.parseColor("#ffffff"));
                            }
                            else
                            {
                                // Set the background color for alternate row/item
                                view.setBackgroundColor(Color.parseColor("#fef2e8"));
                            }
                            return view;
                        }
                    };
                    ListView list =(ListView) findViewById(R.id.listb);
                    list.setAdapter(adapter);

                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<List<DayAttendance>> call, Throwable t) {
                Toast.makeText(InsideClassStudent.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }


}

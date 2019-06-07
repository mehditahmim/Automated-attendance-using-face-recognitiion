package com.example.frontend;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InsideClassTeacher extends AppCompatActivity {

    private Button takeAddendance;
    private static DataHolder dh;
    private HashMap<String,String> mapClasAttendanceResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insideclass_teacher);
        mapClasAttendanceResponse = new HashMap<>();

        Intent intent=getIntent();
        Gson gson = new Gson();
        dh = gson.fromJson(getIntent().getStringExtra("myjson"), DataHolder.class);
        populateListView();
        registerClicks();

        takeAddendance = findViewById(R.id.button9);
        takeAddendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toTakeAttendance();
            }
        });

    }

    private  void toTakeAttendance() {
        try {

            Intent i = new Intent(InsideClassTeacher.this, TakeAttendance.class);
            Gson gson = new Gson();
            String myJson = gson.toJson(dh);
            i.putExtra("myjson", myJson);
            startActivity(i);

        }catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void populateListView() {
        Call<ClassResponse> call = RetrofitClient.getInstance()
                .getApi()
                .insideClass(dh.getClass_id(), dh.getTOKEN());

        call.enqueue(new Callback<ClassResponse>() {
            @Override
            public void onResponse(Call<ClassResponse> call, Response<ClassResponse> response) {

                try{
                    ClassResponse cr = response.body();
                    ArrayList<String> classList = new ArrayList<>();
                    dh.setClassResponse(cr);

                    for (ClassAttendanceResponse clr: cr .getClass_attendance()) {

                        String s= clr.getNum_present()+ " out of "+cr.getClass_student().size()+" students were present " +"on "+ clr.getDate_created();
                        mapClasAttendanceResponse.put(s, clr.getId());
                        System.out.println(s);
                        classList.add(s);
                    }


                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(InsideClassTeacher.this,R.layout.activity_listview,classList){
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
            public void onFailure(Call<ClassResponse> call, Throwable t) {
                Toast.makeText(InsideClassTeacher.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }
    private void registerClicks() {
        final ListView list = (ListView) findViewById(R.id.listb);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String key = (String)list.getItemAtPosition(position);
                String at_id = mapClasAttendanceResponse.get(key);
                System.out.println("moving to class attendance "+ at_id);
                dh.setAt_id(at_id);
                Intent i = new Intent(InsideClassTeacher.this, InsideAttendanceTeacher.class);
                Gson gson = new Gson();
                String myJson = gson.toJson(dh);
                i.putExtra("myjson", myJson);
                startActivity(i);

            }
        });
    }

}

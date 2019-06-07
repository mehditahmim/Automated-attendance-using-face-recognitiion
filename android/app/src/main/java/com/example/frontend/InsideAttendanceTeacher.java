package com.example.frontend;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.frontend.APIManager.ClassResponse;
import com.example.frontend.APIManager.DataHolder;
import com.example.frontend.APIManager.ExAttendanceResponse;
import com.example.frontend.Utility.ShowImageFromURL;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InsideAttendanceTeacher extends AppCompatActivity {
    private static DataHolder dh;
    private ArrayList<ImageView> lst_imv;
    private Button bUploadButton, bTakeAttendance;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewa_attendance);
        Intent intent=getIntent();
        Gson gson = new Gson();
        dh = gson.fromJson(getIntent().getStringExtra("myjson"), DataHolder.class);

        lst_imv=new ArrayList<ImageView>();

        lst_imv.add((ImageView) findViewById(R.id.timView1));
        lst_imv.add((ImageView)findViewById(R.id.timView2));
        lst_imv.add((ImageView)findViewById(R.id.timView3));
        lst_imv.add((ImageView)findViewById(R.id.timView4));
        lst_imv.add((ImageView)findViewById(R.id.timView5));
        lst_imv.add((ImageView)findViewById(R.id.timView6));
        lst_imv.add((ImageView)findViewById(R.id.timView7));

        populateListView();



    }


    private void populateListView() {
        Call<ExAttendanceResponse> call = RetrofitClient.getInstance()
                .getApi()
                .insideAt(dh.getAt_id(), dh.getTOKEN());
        System.out.println(dh.getAt_id()+" inside attendance teacher ");

        call.enqueue(new Callback<ExAttendanceResponse>() {
            @Override
            public void onResponse(Call<ExAttendanceResponse> call, Response<ExAttendanceResponse> response) {

                try{

                    System.out.println("Success?"+ response.isSuccessful());
                    ExAttendanceResponse er = response.body();


                    for (int i =0; i< er.getAttendance_image().size(); i++) {
                        new ShowImageFromURL(lst_imv.get(i))
                                .execute(RetrofitClient.Base_url2+ er.getAttendance_image().get(i).getProcessed_img());
                    }

                    System.out.println("size of images"+er.getAttendance_image().size());

                    ClassResponse cr = dh.getClassResponse();

                    ArrayList<String> out = new ArrayList<>();
                    HashMap<String, Boolean> mp = new HashMap<>();

                    for (int i =0; i< er.getAttendance_student().size(); i++) {
                        mp.put( er.getAttendance_student().get(i).getStudent(), true);
                        System.out.println("Haha"+   er.getAttendance_student().get(i).getStudent());
                    }

                    for (int i =0; i< cr.getClass_student().size(); i++) {
                        System.out.println("Haha"+ cr.getClass_student().get(i).getStudent());
                        if (! mp.containsKey(cr.getClass_student().get(i).getStudent())) {

                                out.add("Student-ID-"+ cr.getClass_student().get(i).getStudent()+" was not present.");
                        }
                    }

                    System.out.println(out.size() +" at size");
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(InsideAttendanceTeacher.this,R.layout.activity_listview, out){
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
                    ListView list =(ListView) findViewById(R.id.atlist);
                    list.setAdapter(adapter);

                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<ExAttendanceResponse> call, Throwable t) {
                Toast.makeText(InsideAttendanceTeacher.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }
}
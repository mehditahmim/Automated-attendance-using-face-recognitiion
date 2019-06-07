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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.frontend.APIManager.ClassResponse;
import com.example.frontend.APIManager.DataHolder;
import com.example.frontend.APIManager.LandingPageResponse;
import com.example.frontend.APIManager.SClassResponse;
import com.example.frontend.Utility.ShowImageFromURL;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Student_Page extends AppCompatActivity {
    private Button bAddClass, blogout;
    private TextView personName;

    private HashMap<String,String> mapClasResponse;
    private static DataHolder dh;

    @Override
    public void onBackPressed() {


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_page);

        Intent intent=getIntent();
        Gson gson = new Gson();
        dh = gson.fromJson(getIntent().getStringExtra("myjson"), DataHolder.class);

        bAddClass=  (Button)findViewById(R.id.bjoinclass);
        personName = findViewById(R.id.person_name);
        blogout= findViewById(R.id.blogout);
        mapClasResponse = new HashMap<>();

        try {
            landingPageApi();
            populateListView();
            registerClicks();

        } catch (Exception e) {
            e.printStackTrace();
        }

        bAddClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createClassroom();
            }
        });
        blogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });

    }

    private void logout() {
        Call<ResponseBody> call = RetrofitClient.getInstance()
                .getApi()
                .logout(dh.getTOKEN());

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                Intent intent = new Intent(Student_Page.this, Login.class);
                startActivity(intent);
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(Student_Page.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });



    }
   private void createClassroom() {
        Intent intent = new Intent(Student_Page.this, JoinClassRoom.class);
        Gson gson = new Gson();
        String myJson = gson.toJson(dh);
        intent.putExtra("myjson", myJson);
        startActivity(intent);
    }
   private void populateListView() {
       Call<List<SClassResponse>> call = RetrofitClient.getInstance()
               .getApi()
               .slistClass(dh.getTOKEN());

       call.enqueue(new Callback<List<SClassResponse>>() {
           @Override
           public void onResponse(Call<List<SClassResponse>> call, Response<List<SClassResponse>> response) {

               try{
                   List<SClassResponse> lr = response.body();
                   ArrayList<String> classList = new ArrayList<>();

                   for (SClassResponse r: lr) {
                       String s= r.getCourse_no()+ "("+r.getId()+")";
                       mapClasResponse.put(s,r.getId());
                       System.out.println(s);
                       classList.add(s);
                   }


                   ArrayAdapter<String> adapter = new ArrayAdapter<String>(Student_Page.this,R.layout.activity_listview,classList){
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
                   ListView list =(ListView) findViewById(R.id.list_teachers_subj);
                   list.setAdapter(adapter);
                   
               }
               catch (Exception e){
                   e.printStackTrace();
               }
           }
           @Override
           public void onFailure(Call<List<SClassResponse>> call, Throwable t) {
               Toast.makeText(Student_Page.this, t.getMessage(), Toast.LENGTH_LONG).show();
           }
           });
       
   }
    private void registerClicks() {
        final ListView list = (ListView) findViewById(R.id.list_teachers_subj);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String key = (String)list.getItemAtPosition(position);
                String class_id = mapClasResponse.get(key);
                System.out.println("moving to class "+ class_id);
                dh.setClass_id(class_id);

                Intent i = new Intent(Student_Page.this, InsideClassStudent.class);
                Gson gson = new Gson();
                String myJson = gson.toJson(dh);
                i.putExtra("myjson", myJson);
                startActivity(i);

            }
        });
    }


    private void landingPageApi () {
        Call <LandingPageResponse> call = RetrofitClient.getInstance()
                .getApi()
                .getLandingPage(dh.getID(), dh.getTOKEN());


        call.enqueue(new Callback<LandingPageResponse>() {
            @Override
            public void onResponse(Call<LandingPageResponse> call, Response<LandingPageResponse> response) {
                try {
                    LandingPageResponse lpr = response.body();
                    System.out.println("Success "+ response.code());
                    personName.setText(lpr.getFirst_name() +" "+ lpr.getLast_name());
                    new ShowImageFromURL((ImageView) findViewById(R.id.pp2))
                            .execute(lpr.getPicture());


                }catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<LandingPageResponse> call, Throwable t) {
                Toast.makeText(Student_Page.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });


    }
}

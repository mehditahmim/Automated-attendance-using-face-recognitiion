package com.example.frontend;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.frontend.APIManager.AttendanceResponse;
import com.example.frontend.APIManager.DataHolder;
import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TakeAttendance extends AppCompatActivity {
    private Button bUploadButton, bTakeAttendance;
    private static DataHolder dh;
    private ArrayList<Uri> lst_uris;
    private ArrayList<ImageView> lst_imv;
    private ArrayList<ImageView> const_lst_imv=new ArrayList<ImageView>();
    ProgressBar progressBar;

    private String getPath(Context context, Uri uri ) {
        String result = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().query(uri, proj, null, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                int column_index = cursor.getColumnIndexOrThrow(proj[0]);
                result = cursor.getString(column_index);
            }
            cursor.close();
        }
        if (result == null) {
            result = "Not found";
        }
        return result;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_attendance);

        Intent intent=getIntent();
        Gson gson = new Gson();
        dh = gson.fromJson(getIntent().getStringExtra("myjson"), DataHolder.class);

        bUploadButton = findViewById(R.id.button9);
        bTakeAttendance = findViewById(R.id.calcbutton);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);

        const_lst_imv=new ArrayList<ImageView>();
        lst_uris = new ArrayList<>();
        lst_imv = new ArrayList<>();

        const_lst_imv.add((ImageView) findViewById(R.id.imView1));
        const_lst_imv.add((ImageView)findViewById(R.id.imView2));
        const_lst_imv.add((ImageView)findViewById(R.id.imView3));
        const_lst_imv.add((ImageView)findViewById(R.id.imView4));
        const_lst_imv.add((ImageView)findViewById(R.id.imView5));
        const_lst_imv.add((ImageView)findViewById(R.id.imView6));
        const_lst_imv.add((ImageView)findViewById(R.id.imView7));



        bUploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadPhoto();
            }
        });
        bTakeAttendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calcAttendance();
            }
        });
    }



    private void uploadPhoto() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"android.intent.action.SEND_MULTIPLE"), 2);

    }

    private void calcAttendance() {
        if (lst_uris.size() < 1) {
            Toast.makeText(this, "Upload Image", Toast.LENGTH_LONG).show();
            return;
        }

        bUploadButton.setEnabled(false);
        progressBar.setVisibility(View.VISIBLE);
        takeAttendance();

        for (Uri uri : lst_uris) {
            String picturePath = getPath(this.getApplicationContext( ), uri );
            File file = new File(picturePath);
            uploadAttendanceImange(file);
        }

        calcAttendance2();
        Intent i = new Intent(TakeAttendance.this, InsideClassTeacher.class);
        startActivity(i);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {


        if (requestCode != 2 || resultCode != RESULT_OK  || data == null) {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show();
            return;
        }

        int count = data.getClipData()==null ?  (data.getData() == null ? 0 : 1) :  data.getClipData().getItemCount();
        if (count >7) {
            count=7;
        }

        if(data.getClipData()!=null) {

            for(int i = 0; i < count; i++) {
                lst_uris.add(data.getClipData().getItemAt(i).getUri());
                lst_imv.add(const_lst_imv.get(i));
                lst_imv.get(i).setImageURI(lst_uris.get(i));
            }


        } else if (data.getData() != null) {
            Uri uri = data.getData();

            lst_uris.add(uri);
            lst_imv.add(const_lst_imv.get(0));
            lst_imv.get(0).setImageURI(lst_uris.get(0));

        }

        super.onActivityResult(requestCode, resultCode, data);

    }


    private void takeAttendance () {

        Call<AttendanceResponse> call = RetrofitClient.getInstance()
                .getApi()
                .createAttendance(dh.getTOKEN(), dh.getClass_id());

        call.enqueue(new Callback<AttendanceResponse>() {
            @Override
            public void onResponse(Call<AttendanceResponse> call, Response<AttendanceResponse> response) {
                try {
                    AttendanceResponse atr = response.body();
                    System.out.println("Success1 "+ response.code() +" "+ response.isSuccessful()+" "+ atr.getId());
                    dh.setAt_id(atr.getId());



                }catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<AttendanceResponse> call, Throwable t) {

                Toast.makeText(TakeAttendance.this, t.getMessage(), Toast.LENGTH_LONG).show();

            }
        });
    }

    public void uploadAttendanceImange (File file) {


        RequestBody requestFile =
                RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part body1 =
                MultipartBody.Part.createFormData("img", file.getName(), requestFile);
        MultipartBody.Part body2 =
                MultipartBody.Part.createFormData("processed_img", file.getName(), requestFile);
        RequestBody attendance_r =
                RequestBody.create(MediaType.parse("multipart/form-data"), dh.getAt_id());

        Call <ResponseBody> call = RetrofitClient.getInstance()
                .getApi()
                .atImage(body1,
                        body2,
                        attendance_r);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try{
                    String s = response.body().string();
//                    Toast.makeText(context, s, Toast.LENGTH_LONG).show();
                }
                catch (IOException e){
                    e.printStackTrace();
                }


            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(TakeAttendance.this, t.getMessage()+"!!", Toast.LENGTH_LONG).show();
            }
        });



    }

    public void calcAttendance2() {


        Call<ResponseBody> call = RetrofitClient.getInstance()
                .getApi()
                .calcAttendance(dh.getTOKEN(), dh.getAt_id());

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    System.out.println("hello");

                }catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(TakeAttendance.this, t.getMessage(), Toast.LENGTH_LONG).show();

            }
        });


    }

}

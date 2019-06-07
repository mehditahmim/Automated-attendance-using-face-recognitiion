package com.example.frontend;

import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import okhttp3.RequestBody;
import okhttp3.MediaType;
import android.content.Context;
import android.database.Cursor;

import java.io.File;
import java.io.IOException;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;



public class Signup extends AppCompatActivity implements View.OnClickListener{
    private static  final int RESULT_LOAD_IMAGE=1;
    private EditText password;
    private EditText first_name;
    private EditText last_name;
    private EditText username;
    private Button signup;
    private Button bProfilePhoto;
    private ImageView targetImage;
    private CheckBox cb;
    private File file;
    private  String is_student="false";

    private String getPath(Context context, Uri uri ) {
        String result = null;
        String[] proj = { MediaStore.Images.Media.DATA };
        Cursor cursor = context.getContentResolver( ).query( uri, proj, null, null, null );
        if(cursor != null){
            if ( cursor.moveToFirst( ) ) {
                int column_index = cursor.getColumnIndexOrThrow( proj[0] );
                result = cursor.getString( column_index );
            }
            cursor.close( );
        }
        if(result == null) {
            result = "Not found";
        }
        return result;
    }



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        signup = findViewById(R.id.signup2);
        password = findViewById(R.id.passcode);
        username = findViewById(R.id.user_name);
        first_name = findViewById(R.id.first_name);
        last_name = findViewById(R.id.last_name);

        bProfilePhoto = findViewById(R.id.bProfilePhoto);
        targetImage = findViewById(R.id.profilePhoto);
        cb = findViewById(R.id.checkBox);

        signup.setOnClickListener(this);
        bProfilePhoto.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.bProfilePhoto:
                Intent galaryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galaryIntent, RESULT_LOAD_IMAGE);
                break;

            case R.id.signup2:

                String pass = password.getText().toString().trim();
                String usn = username.getText().toString().trim();
                String fn = first_name.getText().toString().trim();
                String last =last_name.getText().toString().trim();

                System.out.println(pass+" "+ usn+" "+fn+" "+last);


                if (!pass.isEmpty()&&!usn.isEmpty()&&!fn.isEmpty()&&!last.isEmpty() && targetImage.getDrawable() != null) {
                    connectSignupApi();

                } else
                    Toast.makeText(Signup.this, "Please Provide All Fields", Toast.LENGTH_LONG).show();
                break;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && data != null) {
            Uri image = data.getData();
            targetImage.setImageURI(image);
            String picturePath = getPath(this.getApplicationContext( ), image );
            file = new File(picturePath);
        }
    }

    private void connectSignupApi () {
        String usn = username.getText().toString().trim();
        String pass = password.getText().toString().trim();
        String fn = first_name.getText().toString().trim();
        String last = last_name.getText().toString().trim();
        Boolean is_st = cb.isChecked();

//        Bitmap bitmap = ((BitmapDrawable)targetImage.getDrawable()).getBitmap();
        System.out.println(file.getPath()+" !!! "+file.getName());

        RequestBody requestFile =
               RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part body =
                MultipartBody.Part.createFormData("picture", file.getName(), requestFile);

        RequestBody user_name_r =
                RequestBody.create(MediaType.parse("multipart/form-data"), usn);
        RequestBody password_r =
                RequestBody.create(MediaType.parse("multipart/form-data"), pass);
        RequestBody first_name_r =
                RequestBody.create(MediaType.parse("multipart/form-data"), fn);
        RequestBody last_name_r =
                RequestBody.create(MediaType.parse("multipart/form-data"), last);
        RequestBody is_student_r =
                RequestBody.create(MediaType.parse("multipart/form-data"), is_st.toString());

        Call <ResponseBody> call = RetrofitClient.getInstance()
                .getApi()
                .createUsers(user_name_r,
                        first_name_r,
                        last_name_r,
                        password_r,
                        is_student_r,
                        body);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try{
                    String s = response.body().string();
                    Toast.makeText(Signup.this, s, Toast.LENGTH_LONG).show();
                    System.out.println(s);
                    Intent i = new Intent(Signup.this, Login.class);
                    startActivity(i);

                }
                catch (IOException e){
                    e.printStackTrace();
                }


            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(Signup.this, t.getMessage()+"!!", Toast.LENGTH_LONG).show();
            }
        });



    }
}

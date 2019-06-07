package com.example.frontend;

import okhttp3.MultipartBody;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Part;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Path;

import com.example.frontend.APIManager.AttendanceResponse;
import com.example.frontend.APIManager.ClassResponse;
import com.example.frontend.APIManager.CreateClassResponse;
import com.example.frontend.APIManager.DayAttendance;
import com.example.frontend.APIManager.ExAttendanceResponse;
import com.example.frontend.APIManager.LandingPageResponse;
import com.example.frontend.APIManager.LoginResponse;
import com.example.frontend.APIManager.SClassResponse;

import java.util.List;

public interface AllApi {

    @Multipart
    @POST("createusers/")
    Call<ResponseBody>  createUsers(@Part("username") RequestBody user_name,
                                    @Part("first_name") RequestBody first_Name,
                                    @Part("last_name") RequestBody last_Name,
                                    @Part("password") RequestBody password,
                                    @Part("is_student") RequestBody is_student,
                                    @Part MultipartBody.Part file
            );

    @FormUrlEncoded
    @POST("get-token/")
    Call<LoginResponse>  login(@Field("username") String user_name,
                              @Field("password") String password

    );

    @Headers({ "Content-Type: application/json;charset=UTF-8"})
    @GET("landingpage/{id}/")
    Call<LandingPageResponse>  getLandingPage(@Path("id") String id,
                                              @Header("Authorization") String token
    );

    @FormUrlEncoded
    @POST("classroom/")
    Call<CreateClassResponse>  createClass(
                                            @Header("Authorization") String token,
                                            @Field("name") String name,
                                            @Field("course_no") String courseno


    );

    @FormUrlEncoded
    @POST("joinclass/")
    Call<ResponseBody> joinClass(
            @Header("Authorization") String token,
            @Field("classroom") String courseno
    );


    @GET("classlist/")
    Call<List<ClassResponse>>  listClass(
            @Header("Authorization") String token
    );


    @GET("sclasslist/")
    Call<List<SClassResponse>>  slistClass(
            @Header("Authorization") String token
    );

    @GET("classroom/{id}")
    Call<ClassResponse>  insideClass(
            @Path("id") String id,
            @Header("Authorization") String token
    );

    @GET("all-at/{id}")
    Call<List<DayAttendance>>  insideAtte(
            @Path("id") String id,
            @Header("Authorization") String token
    );

    @GET("att/{id}")
    Call<ExAttendanceResponse>  insideAt(
            @Path("id") String id,
            @Header("Authorization") String token
    );

    @FormUrlEncoded
    @POST("attendance/")
    Call<AttendanceResponse>  createAttendance(
            @Header("Authorization") String token,
            @Field("class_room") String class_room
    );

    @Multipart
    @POST("at-image/")
    Call<ResponseBody>  atImage(@Part MultipartBody.Part file1,
                                    @Part MultipartBody.Part file2,
                                    @Part("attendance") RequestBody attendance
    );



    @GET("calc-at/{at_id}")
    Call<ResponseBody>  calcAttendance(
            @Header("Authorization") String token,
             @Path("at_id") String id
    );

    @GET("log-out/")
    Call<ResponseBody>  logout(
            @Header("Authorization") String token

    );

}

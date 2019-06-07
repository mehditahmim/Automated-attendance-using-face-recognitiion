package com.example.frontend;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
public class RetrofitClient
{

//    private static final String Base_url = "http://192.168.0.11:8000/";
    private static final String Base_url = "http://10.0.2.2:8000/";
    public static final String Base_url2 = "http://10.0.2.2:8000";
    private static RetrofitClient mInstance;
    private Retrofit retrofit;

    private RetrofitClient(){
        retrofit = new Retrofit.Builder().baseUrl(Base_url).addConverterFactory
                (GsonConverterFactory.create())
                .build();

    }
    public static synchronized RetrofitClient getInstance()
    {
      if (mInstance == null)
      {
          mInstance = new RetrofitClient();
      }
      return mInstance;

    }
    public AllApi getApi ()
    {
        return retrofit.create(AllApi.class);
    }

}

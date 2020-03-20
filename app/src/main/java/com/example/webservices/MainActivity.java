package com.example.webservices;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    Button btnGlide,btnHttpurl,btnJsonHttp,btnretro;
    public static TextView data;
    String url="https://zdnet3.cbsistatic.com/hub/i/2019/03/16/e118b0c5-cf3c-4bdb-be71-103228677b25/android-logo.png";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnGlide=findViewById(R.id.btnGlide);
        btnHttpurl=findViewById(R.id.btnHttp);
        btnJsonHttp=findViewById(R.id.btnJsonHttp);
        btnretro=findViewById(R.id.btnretrofit);
        btnretro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Retrofit retrofit=new Retrofit.Builder()
                        .baseUrl("https://jsonplaceholder.typicode.com/")
                        .addConverterFactory(GsonConverterFactory.create()).build();
                JsonPlaceHolder jsonPlaceHolder=retrofit.create(JsonPlaceHolder.class);
                Call<List<Post>> call=jsonPlaceHolder.getPost();
                call.enqueue(new Callback<List<Post>>() {
                    private Call<List<Post>> call;
                    private Response<List<Post>> response;

                    @Override
                    public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                        this.call = call;
                        this.response = response;
                        if(!response.isSuccessful()){
                            data.setText("code:"+response.code());
                            return;
                        }
                        List<Post> posts=response.body();
                        for(Post post:posts){
                            String content="";
                            content+="id"+post.getId()+"\n";
                            content+="user id"+post.getUserId()+"\n";
                            content+="title"+post.getTitle()+"\n\n";
                            content+="text:"+post.getText()+"\n\n";

                            data.append(content);

                        }

                    }

                    @Override
                    public void onFailure(Call<List<Post>> call, Throwable t) {
                        data.setText(t.getMessage());

                    }
                });
            }
        });

        data=findViewById(R.id.data);
        btnJsonHttp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FetchData fetchData=new FetchData();
                fetchData.execute();

            }
        });
        btnGlide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Glide.with(MainActivity.this).load(url).into(new CustomTarget<Drawable>() {
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        LinearLayout root=(LinearLayout)findViewById(R.id.root);
                        root.setBackground(resource);
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {
                    }
                });
            }
        });
        btnHttpurl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DownloadTask().execute(url);
            }
        });



    }
    public Bitmap downloadUrl(String strUrl) throws IOException {
        Bitmap bitmap=null;
        InputStream iStream = null;
        try{
            URL url = new URL(strUrl);
            /** Creating an http connection to communcate with url */
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            /** Connecting to url */
            urlConnection.connect();

            /** Reading data from url */
            iStream = urlConnection.getInputStream();

            /** Creating a bitmap from the stream returned from the url */
            bitmap = BitmapFactory.decodeStream(iStream);

        }catch(Exception e){
            Log.d("Exception download url", e.toString());
        }finally{
            iStream.close();
        }
        return bitmap;
    }

    public void retrofitactivity(View view) {

    }

    private class DownloadTask extends AsyncTask<String,Integer,Bitmap> {

        @Override
        protected Bitmap doInBackground(String... url) {
            Bitmap bm=null;
            try{
                bm = downloadUrl(url[0]);
            }catch(Exception e){
                Log.d("Background Task",e.toString());
            }
            return bm;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            BitmapDrawable ob = new BitmapDrawable(getResources(), result);
            LinearLayout root=(LinearLayout)findViewById(R.id.root);
            root.setBackground(ob);

        }
    }
}

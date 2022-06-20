package ru.mpt.p50_4_19.api;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    TextView txt;
    Button btn;
    ImageView image;


    String data[] = new String[6];
    TextView url;
    TextView created_at;
    TextView updated_at;
    TextView jokeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txt = findViewById(R.id.txtJoke);
        btn = findViewById(R.id.btnClick);
        image = findViewById(R.id.image);
        url = findViewById(R.id.url);
        created_at = findViewById(R.id.created_at);
        updated_at = findViewById(R.id.updated_at);
        jokeId = findViewById(R.id.jokeId);


        btn.setOnClickListener(view -> new JokeLoader().execute());
    }

    private  class  JokeLoader extends AsyncTask<Void, Void, Void>
    {

        @Override
        protected Void doInBackground(Void... voids)
        {
            String jsonString = getJson("https://api.chucknorris.io/jokes/random");

            try {

                JSONObject jo = new JSONObject(jsonString);
                int i= 0;
                data[i++] = jo.getString("id");
                data[i++] = jo.getString("created_at");
                data[i++] = jo.getString("updated_at");
                data[i++] = jo.getString("url");
                data[i++] = jo.getString("icon_url");
                data[i++] = jo.getString("value");

            }catch (Exception e){
                e.printStackTrace();
            }

            return null;
        }

        private String getJson(String link)
        {
            String data = "";
            try {
                URL url = new URL(link);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK){
                    BufferedReader r = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(),
                    "utf-8"));
                    data = r.readLine();
                    urlConnection.disconnect();
                }
            }catch (IOException e){
                e.printStackTrace();
            }
            return data;
        }

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            for( int i =0; i < data.length; i++ )
                data[i] = "";

            image.setImageResource(R.drawable.ic_launcher_foreground);

            txt.setText("Loading...");
        }

        @Override
        protected void onPostExecute(Void aVoid)
        {
            super.onPostExecute(aVoid);
            jokeId.setText( data[0] );
            created_at.setText( data[1] );
            updated_at.setText( data[2] );
            url.setText(data[3]);
            if(data[4] != null && !data[4].isEmpty() )
            {
                new DownloadImageTask((ImageView) findViewById(R.id.image)).execute(data);
            }
            txt.setText(data[5]);
        }
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView image;

        public DownloadImageTask(ImageView image) {
            this.image = image;
        }

        protected Bitmap doInBackground(String[] urls) {
            String urldisplay = urls[4];
            Bitmap bitmap = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                bitmap = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return bitmap;
        }

        protected void onPostExecute(Bitmap result) {
            image.setImageBitmap(result);
            //Data.inst.setData(data[5],result);
        }
    }
}
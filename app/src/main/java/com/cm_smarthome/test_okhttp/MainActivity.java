package com.cm_smarthome.test_okhttp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;


public class MainActivity extends ActionBarActivity {

    OkHttpClient okHttpClient = new OkHttpClient();

    private ImageView imageView;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = (ImageView) findViewById(R.id.imageView);
        textView = (TextView) findViewById(R.id.textView);

        AsyncTaskGetData taskGetData = new AsyncTaskGetData();
        taskGetData.execute("1");

        AsyncTaskGetImage taskGetImage = new AsyncTaskGetImage();
        taskGetImage.execute();
    }

    public class AsyncTaskGetData extends AsyncTask<String, Void, Void> {
        private String x;

        @Override
        protected void onPostExecute(Void aVoid) {
            textView.setText(x);
        }

        @Override
        protected Void doInBackground(String... params) {
            RequestBody body = new FormEncodingBuilder()
                    .add("sID", params[0])
                    .build();

            Request request = new Request.Builder()
                    .url("http://www.cm-smarthome.com/android/testOKHttp.php")
                    .post(body)
                    .build();
            Response response;
            try {
                response = okHttpClient.newCall(request).execute();
                String result = response.body().string();

                JSONObject object;
                try {
                    object = new JSONObject(result);
                    String name = object.getString("Name");
                    String lastName = object.getString("LastName");
                    String tel = object.getString("Tel");
                    String email = object.getString("Email");

                    x = "Name : " + name + "\n" + "LastName : " + lastName
                            + "\n" + "Tel : " + tel + "\n" + "Email : " + email;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    public class AsyncTaskGetImage extends AsyncTask<String, Void, Void> {
        private  Bitmap bitmap;

        @Override
        protected void onPostExecute(Void aVoid) {
            imageView.setImageBitmap(bitmap);
        }

        @Override
        protected Void doInBackground(String... params) {

            Request request = new Request.Builder()
                    .url("http://www.cm-smarthome.com/android/a1.png")
                    .build();
            Response response;
            try {
                response = okHttpClient.newCall(request).execute();
                InputStream result = response.body().byteStream();
                bitmap = BitmapFactory.decodeStream(result);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
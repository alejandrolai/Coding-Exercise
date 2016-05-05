package com.alejandrolai.bofa.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.alejandrolai.bofa.R;
import com.alejandrolai.bofa.model.Place;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ResultActivity extends AppCompatActivity {

    private Place mPlace;

    @BindView(R.id.placeID) TextView placeId;
    @BindView(R.id.placeName) TextView placeName;
    @BindView(R.id.placeAddress) TextView placeAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        getPlace(intent.getStringExtra("PlaceId"));
    }

    private void getPlace(String placeId) {
        String url = "https://" +
                "maps.googleapis.com/maps/api/place/details/json?" +
                "placeid=" + placeId +
                "&key=" + getResources().getString(R.string.api_key);

        Log.d("Result",url);

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();

        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(ResultActivity.this,"Place not found",Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    final String jsonData = response.body().string();
                    if (response.isSuccessful()){
                        mPlace = setPlace(jsonData);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                updateUI();
                            }
                        });
                    } else {
                        Toast.makeText(ResultActivity.this,"Could not get place details",Toast.LENGTH_SHORT).show();
                        Log.e("Result",response.toString());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void updateUI() {
        if (mPlace != null) {
            placeId.setText("ID: " + mPlace.getId());
            placeName.setText("Name: " + mPlace.getName());
            placeAddress.setText("Address: " + mPlace.getAddress());
        }
    }

    private Place setPlace(String jsonData) throws JSONException {
        Log.d("Result",jsonData);
        JSONObject jsonPlace = new JSONObject(jsonData);
        Place place = new Place();
        JSONObject jsonResult = jsonPlace.getJSONObject("result");
        place.setName(jsonResult.getString("name"));
        place.setId(jsonResult.getString("place_id"));
        place.setAddress(jsonResult.getString("formatted_address"));

        return place;
    }
}

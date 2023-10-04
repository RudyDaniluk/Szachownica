package com.example.pogoda;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    private EditText cityEditText;
    private TextView weatherTextView;
    private ImageView weatherIconImageView;

    private static final String API_KEY = "4fd89fd989b504bf4cb7e3d8beace553";
    private static final String BASE_URL = "https://api.openweathermap.org/data/2.5/weather";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cityEditText = findViewById(R.id.cityEditText);
        weatherTextView = findViewById(R.id.weatherTextView);
        weatherIconImageView = findViewById(R.id.weatherIconImageView);

        Button searchButton = findViewById(R.id.searchButton);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cityName = cityEditText.getText().toString();
                new FetchWeatherTask().execute(cityName);
            }
        });
    }

    private class FetchWeatherTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String cityName = params[0];
            String urlString = BASE_URL + "?q=" + cityName + "&appid=" + API_KEY + "&lang=pl";

            try {
                URL url = new URL(urlString);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder result = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }
                return result.toString();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String json) {
            super.onPostExecute(json);
            if (json != null) {
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    JSONObject main = jsonObject.getJSONObject("main");
                    JSONObject wind = jsonObject.getJSONObject("wind");
                    JSONObject rain = jsonObject.optJSONObject("rain");
                    JSONObject snow = jsonObject.optJSONObject("snow");
                    JSONObject clouds = jsonObject.getJSONObject("clouds");
                    String cityName = jsonObject.getString("name");

                    double visibility = jsonObject.optDouble("visibility");
                    double windSpeed = wind.getDouble("speed");
                    String windDirection = getWindDirection(wind.optInt("deg"));
                    String rainInfo = rain != null ? rain.toString() : "Brak informacji o deszczu";
                    String snowInfo = snow != null ? snow.toString() : "Brak informacji o śniegu";
                    int cloudiness = clouds.getInt("all");

                    String weatherInfo = "Pogoda w " + cityName + ":\n" +
                            "Wilgotność: " + main.getDouble("humidity") + "%\n" +
                            "Widoczność: " + visibility + " m\n" +
                            "Prędkość wiatru: " + windSpeed + " m/s, Kierunek: " + windDirection + "\n" +
                            "Deszcz: " + rainInfo + "\n" +
                            "Śnieg: " + snowInfo + "\n" +
                            "Zachmurzenie: " + cloudiness + "%";
                    weatherTextView.setText(weatherInfo);
                    String weatherIcon = jsonObject.getJSONArray("weather").getJSONObject(0).getString("icon");


                    String imageUrl = "https://openweathermap.org/img/w/" + weatherIcon + ".png";
                    Picasso.get().load(imageUrl).into(weatherIconImageView);



                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                weatherTextView.setText("Brak danych o pogodzie dla podanego miasta.");
            }
        }
        private String getWindDirection(int degrees) {
            if (degrees >= 338 || degrees <= 22)
                return "N";
            else if (degrees > 22 && degrees <= 67)
                return "NE";
            else if (degrees > 67 && degrees <= 112)
                return "E";
            else if (degrees > 112 && degrees <= 157)
                return "SE";
            else if (degrees > 157 && degrees <= 202)
                return "S";
            else if (degrees > 202 && degrees <= 247)
                return "SW";
            else if (degrees > 247 && degrees <= 292)
                return "W";
            else
                return "NW";
        }
    }
}

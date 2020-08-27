package com.example.covid_19;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.view.View;

import org.eazegraph.lib.models.PieModel;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
public class FetchData extends AsyncTask<Void,Void,Void> {

   public StringBuilder output=new StringBuilder();
    @Override
    protected Void doInBackground(Void... voids) {
        InputStream inputStream=null;
        try {

            URL url =new URL("https://disease.sh/v3/covid-19/all/");
            HttpURLConnection connection= (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            inputStream=connection.getInputStream();
                BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(inputStream));
                String line=bufferedReader.readLine();
                while(line!=null)
                {
                    output.append(line);
                    line=bufferedReader.readLine();
                }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        try {

            JSONObject jsonObject=new JSONObject(String.valueOf(output));
            int cases=jsonObject.getInt("cases");
            int recovered=jsonObject.getInt("recovered");
            int deaths=jsonObject.getInt("deaths");
            int today_cases=jsonObject.getInt("todayCases");
            int critical=jsonObject.getInt("critical");
            MainActivity.critical.setText(String.valueOf(critical));
            MainActivity.total_case.setText(String.valueOf(cases));
            MainActivity.recovered.setText(String.valueOf(recovered));
            MainActivity.deaths.setText(String.valueOf(deaths));
            MainActivity.today_cases.setText(String.valueOf(today_cases));
            MainActivity.mPieChart.addPieSlice(new PieModel("", cases, Color.parseColor("#FE6DA8")));
            MainActivity.mPieChart.addPieSlice(new PieModel("", recovered, Color.parseColor("#00EE04")));
            MainActivity.mPieChart.addPieSlice(new PieModel("", deaths, Color.parseColor("#DF0707")));
            MainActivity.mPieChart.addPieSlice(new PieModel("", critical, Color.parseColor("#FF0073EE")));
            MainActivity.mPieChart.startAnimation();

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}

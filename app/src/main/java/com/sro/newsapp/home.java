package com.sro.newsapp;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;

import static android.widget.Toast.LENGTH_LONG;

public class home extends Fragment {
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    public RecyclerView.Adapter adapter;
    public String[] data;
    public String[] img;
    public String[] links;
    public String[] des;
    public String newsUrl;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        data = new String[10];
        img = new String[10];
        links = new String[10];
        des = new String[10];

        Arrays.fill(data, "");
        Arrays.fill(img, "");
        Arrays.fill(links, "");
        Arrays.fill(des, "");

        getActivity().setTitle("Home");
        View view = inflater.inflate(R.layout.home,container,false);
        newsUrl = "https://gnews.io/api/v4/top-headlines?token=094cf41139d1539304d6" +
                "a4d291f1aab4&lang=en&country=in&topic=politics";

        recyclerView =  view.findViewById(R.id.rView);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        fetchData fetchData = new fetchData();
        fetchData.execute();
        return view;
    }
    public class fetchData extends AsyncTask<Void, Void, Void> {
        String line = "";
        String result = "";


        @Override
        protected Void doInBackground(Void... voids) {
            URL url;
            HttpURLConnection connection;
            try {
                url = new URL(newsUrl);
                connection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = connection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                while (line != null) {
                    line = reader.readLine();
                    result += line;
                }
                Log.d("tg",result);
                JSONObject object = new JSONObject(result);
                JSONArray array =  object.getJSONArray("articles");

                for (int i = 0; i < array.length(); i++) {
                    JSONObject jsonObject = array.getJSONObject(i);
                    data[i] = jsonObject.getString("title");
                    img[i] = jsonObject.getString("image");
                    links[i] = jsonObject.getString("url");
                    des[i] = jsonObject.getString("description");

                }
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            adapter = new rv(data, getContext(),img,links,des);
            recyclerView.setAdapter(adapter);

        }
    }

}


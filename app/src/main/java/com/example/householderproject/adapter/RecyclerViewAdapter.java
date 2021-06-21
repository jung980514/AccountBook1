package com.example.householderproject.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.householderproject.R;
import com.example.householderproject.intentview.MapViewActivity;
import com.example.householderproject.model.HouseHoldModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.CustomViewHolder> {

    private ArrayList<HouseHoldModel> list;
    private int layout;
    private String location;
    private Context context;
    private MapTask mapTask;


    public RecyclerViewAdapter(ArrayList<HouseHoldModel> list, int layout) {

        this.list = list;
        this.layout = layout;

    }

    @NonNull
    @Override
    public RecyclerViewAdapter.CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        context = parent.getContext();

        View view = LayoutInflater.from(parent.getContext()).inflate(layout, parent, false);
        CustomViewHolder viewHolder = new CustomViewHolder(view);

        return viewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, final int position) {

        holder.textViewDetail.setText(list.get(position).getDetail());
        holder.textViewCategory.setText(list.get(position).getCategory());
        holder.textViewCredit.setText(list.get(position).getCredit());
        holder.textViewLocation.setText(list.get(position).getLocation());

        holder.itemView.setTag(position);

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {

                location = list.get(position).getLocation();

                mapTask = new MapTask();
                mapTask.execute();

                return true;
            }
        });

    }

    /******************
     * 맵 API 를 받아오는 AsyncTask 클래스
     */
    public class MapTask extends AsyncTask<Void, Void, String> {
        String resultString = null;
        @Override
        protected String doInBackground(Void... voids) {

            String domain = "https://dapi.kakao.com/v2/local/search/keyword.json";
            Uri.Builder builder = Uri.parse(domain).buildUpon();

            builder = addCurrentLocationQueryAt(builder);
            builder = addSearchKeywordQueryAt(builder);

            try{

                URL requestURL = new URL(builder.build().toString());

                HttpsURLConnection connection = (HttpsURLConnection) requestURL.openConnection();
                connection.setRequestProperty("Authorization", "KakaoAK 14a53985a0736bc1c48a5f952eb5c6d8");

                if(connection.getResponseCode() == HttpsURLConnection.HTTP_OK) {

                    InputStreamReader reader = new InputStreamReader(connection.getInputStream(), "UTF-8");
                    BufferedReader bufferedReader = new BufferedReader(reader);
                    StringBuffer buffer = new StringBuffer();

                    String str = null;
                    resultString = null;

                    while((str = bufferedReader.readLine()) != null) {
                        buffer.append(str);
                    }

                    resultString = buffer.toString();
                    return resultString;

                } else {

                }

            }catch (IOException e) {

            }

            return resultString;

        }

        @Override
        protected void onPostExecute(String resultString) {

            super.onPostExecute(resultString);

            handleSearchResult(context, resultString);

        }

    }

    private void handleSearchResult(Context context, String resultString) {

        try {

            JSONArray jsonArray = new JSONObject(resultString).getJSONArray("documents");

            String locationX = null;
            String locationY = null;

            for(int i = 0; i < jsonArray.length(); i++) {

                JSONObject jsonObject = jsonArray.getJSONObject(i);

                locationX = jsonObject.optString("x");
                locationY = jsonObject.getString("y");

            }

            Intent intent = new Intent(context, MapViewActivity.class);
            intent.putExtra("locationX", locationX);
            intent.putExtra("locationY", locationY);
            intent.putExtra("location", location);

            context.startActivity(intent);

        }catch (JSONException e) {

        }

    }

    private Uri.Builder addCurrentLocationQueryAt(Uri.Builder builder) {

        builder.appendQueryParameter("x", "37.5642135")
                .appendQueryParameter("y", "127.0016985");

        return builder;

    }

    private Uri.Builder addSearchKeywordQueryAt(Uri.Builder builder) {

        builder.appendQueryParameter("query", location);

        return builder;

    }


    @Override
    public int getItemCount() {
        return (list != null) ? list.size() : 0;
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {

        public TextView textViewDetail, textViewCategory, textViewCredit, textViewLocation;

        public CustomViewHolder(@NonNull View itemView) {

            super(itemView);

            this.textViewDetail = itemView.findViewById(R.id.textViewDetail);
            this.textViewCategory = itemView.findViewById(R.id.textViewCategory);
            this.textViewCredit = itemView.findViewById(R.id.textViewCredit);
            this.textViewLocation = itemView.findViewById(R.id.textViewLocation);

        }
    }

}

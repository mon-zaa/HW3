package com.example.hw3;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import java.util.Arrays;
import java.util.List;

public class CatListRecyclerFragment extends Fragment implements View.OnClickListener {
    private RecyclerView recyclerView;
    private EditText searchText;

    public CatListRecyclerFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_rv, container, false);
        recyclerView = view.findViewById(R.id.list_rv_id);
        LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(layoutManager);


        searchText = view.findViewById(R.id.search_text);
        Button searchButton = view.findViewById(R.id.search_button);
        searchButton.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        String query = searchText.getText().toString();

        final CatListAdapter catListAdapter = new CatListAdapter();
        final RequestQueue requestQueue = Volley.newRequestQueue(getActivity());

        String url = "https://api.thecatapi.com/v1/breeds/search?q="+ query;
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Gson gson = new Gson();
                Cat[] cats = gson.fromJson(response, Cat[].class);
                List<Cat> catList = Arrays.asList(cats);
                CatDatabase.saveCatsToDatabase(catList);

                catListAdapter.setData(catList);
                recyclerView.setAdapter(catListAdapter);

                requestQueue.stop();

            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("u dumb egged");
                requestQueue.stop();
            }
        };

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, responseListener, errorListener);
        requestQueue.add(stringRequest);

    }
}

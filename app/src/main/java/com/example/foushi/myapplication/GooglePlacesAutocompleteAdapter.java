package com.example.foushi.myapplication;

import android.content.Context;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;


public class GooglePlacesAutocompleteAdapter extends ArrayAdapter implements Filterable {
    private ArrayList resultList1;
    private final Volley helper = Volley.getInstance();
    private JSONObject rep;

    private ArrayList<String> autocomplete (String input) {
        ArrayList<String> resultList = null;
        StringBuilder sb;
        String url = "";
        try {
            sb = new StringBuilder(getContext().getResources().getString(R.string.GoogleAutoPlaceLoc));
            sb.append("?input=").append(URLEncoder.encode(input, "utf8"));
            sb.append("&types=(cities)");
            sb.append("&key=" + getContext().getResources().getString(R.string.KeyGoogleAPI));
            url = sb.toString();
        } catch (Exception e) {
            Log.e(CityActivity.LOG_TAG, "Error processing Places API URL", e);
        }
        CustomJsonRequest request = new CustomJsonRequest
                (Request.Method.GET,url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            rep =response;
                        } catch (Exception e) {
                            Log.e(CityActivity.LOG_TAG,"Error");
                        }

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                      }
                });
        request.setPriority(Request.Priority.HIGH);
        helper.add(request);
        if (rep!= null)
        {
            try {
                JSONArray predsJsonArray = rep.getJSONArray("predictions");
                resultList = new ArrayList<>(predsJsonArray.length());
                CityActivity.placeid = new ArrayList<>(predsJsonArray.length());
                for (int i = 0; i < predsJsonArray.length(); i++) {
                    resultList.add(predsJsonArray.getJSONObject(i).getString("description"));
                    CityActivity.placeid.add(predsJsonArray.getJSONObject(i).getString("place_id"));
                }
            } catch (JSONException e) {
                Log.e(CityActivity.LOG_TAG, "Cannot process JSON results", e);
            }
        }
        return resultList;
    }


    public GooglePlacesAutocompleteAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }
    @Override
    public int getCount() {
        return resultList1.size();
    }

    @Override
    public String getItem(int index) {
        return resultList1.get(index).toString();

    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                if (constraint != null) {
                    resultList1 = autocomplete(constraint.toString());
                    if (resultList1 != null) {
                        filterResults.values = resultList1;
                        filterResults.count = resultList1.size();
                    }
                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, Filter.FilterResults results) {
                if (results != null && results.count > 0) {
                    notifyDataSetChanged();
                } else {
                    notifyDataSetInvalidated();
                }
            }
        };
    }

}

package com.example.foushi.myapplication;


import android.content.Context;
import android.support.design.widget.TabLayout;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar;

import org.json.JSONObject;
import org.w3c.dom.Text;

import java.net.URLEncoder;

import de.greenrobot.event.EventBus;

public class WeatherLoader {
    /*
    Context context;
    private final Volley helper = Volley.getInstance();
    CircleProgressBar bar;
    CustomSpinner spinner;
    TextView textCity;
    ImageView img1;
    TextView Ratio;
    TextView textTemp;
    TextView textDesc;
    public Temps[] predTemps = {null,null,null,null};
    int index;
    //int index;
    //String ville;
    //public Temps[] predTemps = {null,null,null,null};
    Tab1 act;
    private EventBus bus = EventBus.getDefault();
    public WeatherLoader(Context context, CircleProgressBar bar, CustomSpinner spinner, TextView textCity, ImageView img1, TextView Ratio
                         ,TextView textTemp, TextView textDesc, int spinnerindex, String ville, Temps[] predTemps)
    {
        this.context = context;
        this.bar = bar;
        this.spinner = spinner;
        this.textCity=textCity;
        this.img1=img1;
        this.Ratio=Ratio;
        this.textTemp=textTemp;
        this.textDesc=textDesc;
        index=0;

       // this.index=index;
       // this.ville=ville;
       // this.predTemps=predTemps;
    }*/


    public static int getIcon (String s) {
        switch (s) {
            case "01d" : return R.drawable.sun;
            case "01n" : return R.drawable.sun;
            case "02d" : return R.drawable.sunnuage;
            case "02n" : return R.drawable.sunnuage;
            case "03d" : return R.drawable.cloud;
            case "03n" : return R.drawable.cloud;
            case "04d" : return R.drawable.clouds2;
            case "04n" : return R.drawable.clouds2;
            case "09d" : return R.drawable.rain;
            case "09n" : return R.drawable.rain;
            case "10d" : return R.drawable.raincloud;
            case "10n" : return R.drawable.raincloud;
            case "11d" : return R.drawable.storm;
            case "11n" : return R.drawable.storm;
            case "13d" : return R.drawable.snow;
            case "13n" : return R.drawable.snow;
            case "50d" : return R.drawable.fog;
            case "50n" : return R.drawable.fog;
            default: return R.drawable.sun;
        }
    }
/*
    public void GetCoord() {
        act= new Tab1().getInstance();
        StringBuilder sb;
        String url = "";
        try {
            sb = new StringBuilder(context.getResources().getString(R.string.GoogleAutoplacePlaceId));
            sb.append(URLEncoder.encode(new PreferenceClass(context).getPlace(), "utf8"));
            sb.append("&key=");
            sb.append(context.getResources().getString(R.string.KeyGoogleAPI));
            url = sb.toString();
        }
        catch (Exception e) {
            Log.e("API", "Error processing Places API URL", e);
        }
        CustomJsonRequest request = new CustomJsonRequest
                (Request.Method.GET,url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String latitude= response.getJSONObject("result").getJSONObject("geometry").getJSONObject("location").getString("lat");
                            String longitude= response.getJSONObject("result").getJSONObject("geometry").getJSONObject("location").getString("lng");
                            new PreferenceClass(context).setLat(latitude);
                            new PreferenceClass(context).setLong(longitude);
                            loadWeatherData();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, context.getResources().getString(R.string.ErreurChargement), Toast.LENGTH_SHORT).show();
                    }
                });
        request.setPriority(Request.Priority.HIGH);
        helper.add(request);
    }

    public Temps[] loadWeatherData() {
        bar.setVisibility(CircleProgressBar.VISIBLE);
        final PreferenceClass preferences = new PreferenceClass(context);
        String lat = preferences.getLat();
        String lng = preferences.getLong();
        StringBuilder sb;
        String url=null;
        try {
            sb = new StringBuilder(context.getResources().getString(R.string.OpenWeatherEndPoint));
            sb.append(URLEncoder.encode(lat, "utf8"));
            sb.append("&lon=");
            sb.append(URLEncoder.encode(lng, "utf8"));
            sb.append("&appid=");
            sb.append(context.getResources().getString(R.string.KEYOpenWeather));
            url = sb.toString();
        }
        catch (Exception e) {
            Log.e("API", "Error processing Places API URL", e);
        }
        CustomJsonRequest request = new CustomJsonRequest
                (Request.Method.GET,url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            //act.index= spinner.getSelectedItemPosition();
                            index= new Tab1().getInstance().index;
                            String minTemp,maxTemp,moyTemp;
                            String weathercode ;
                            boolean pluie=false;
                            for(int i=0;i<=3;i++)
                            {
                                if (i>index)
                                {
                                    predTemps[i]= null;
                                }
                                else
                                {
                                    predTemps[i]= new Temps();
                                }

                            }
                            for (int i=0; i<=index;i++)

                            {
                                String desc = "";
                                String code = response.getJSONArray("list").getJSONObject(i).getJSONArray("weather").getJSONObject(0).getString("icon");
                                predTemps[i].code= code;
                                switch (code)
                                {
                                    case "01d" : desc= context.getResources().getString(R.string.Soleil); break;
                                    case "01n" : desc= context.getResources().getString(R.string.Soleil); break;
                                    case "02d" : desc= context.getResources().getString(R.string.SoleilNuage); break;
                                    case "02n" : desc= context.getResources().getString(R.string.SoleilNuage); break;
                                    case "03d" : desc= context.getResources().getString(R.string.Nuageux); break;
                                    case "03n" : desc= context.getResources().getString(R.string.Nuageux); break;
                                    case "04d" : desc= context.getResources().getString(R.string.Nuageux); break;
                                    case "04n" : desc= context.getResources().getString(R.string.Nuageux); break;
                                    case "09d" : desc= context.getResources().getString(R.string.AttentionPluie);pluie=true; break;
                                    case "09n" : desc= context.getResources().getString(R.string.AttentionPluie);pluie=true; break;
                                    case "10d" : desc= context.getResources().getString(R.string.PluieSoleil);pluie=true; break;
                                    case "10n" : desc= context.getResources().getString(R.string.PluieSoleil);pluie=true; break;
                                    case "11d" : desc= context.getResources().getString(R.string.AttentonOrage);pluie=true; break;
                                    case "11n" : desc= context.getResources().getString(R.string.AttentonOrage);pluie=true; break;
                                    case "13d" : desc= context.getResources().getString(R.string.AttentionNeige);pluie=true; break;
                                    case "13n" : desc= context.getResources().getString(R.string.AttentionNeige);pluie=true; break;
                                    case "50d" : desc= context.getResources().getString(R.string.AttentionBrouillard); break;
                                    case "50n" : desc= context.getResources().getString(R.string.AttentionBrouillard); break;
                                }
                                predTemps[i].description= desc;
                            }

                            double max = 0;
                            double min = 400;
                            double moy= 0;
                            for (int i=0; i<=index;i++)
                            {
                                maxTemp = response.getJSONArray("list").getJSONObject(i).getJSONObject("main").getString("temp_max");
                                minTemp = response.getJSONArray("list").getJSONObject(i).getJSONObject("main").getString("temp_min");
                                moyTemp = response.getJSONArray("list").getJSONObject(i).getJSONObject("main").getString("temp");
                                double temp = Double.parseDouble(maxTemp);
                                double temp2 = Double.parseDouble(minTemp);
                                double temp3 = Double.parseDouble(moyTemp);
                                if (temp>max)
                                {
                                    max=temp;
                                }
                                if (temp2<min)
                                {
                                    min=temp2;
                                }
                                temp3= temp3 - 273.15;
                                moy+=temp3;
                            }
                            moy=moy/(index+1);
                            min=min-273.15;
                            max=max-273.15;
                            String temp = "Min : "+String.format("%.1f",min)+" °C Max : "+String.format("%.1f",max)+" °C";
                            weathercode = response.getJSONArray("list").getJSONObject(0).getJSONArray("weather").getJSONObject(0).getString("icon");
                            img1.setImageResource(WeatherLoader.getIcon(weathercode));
                            textDesc.setText(predTemps[0].description + " (0-3h)");
                            String ratio5 = "1/" + (index + 1);
                            Ratio.setText(ratio5);
                            textTemp.setText(temp);
                            bar.setVisibility(CircleProgressBar.INVISIBLE);
                            preferences.setDesc(predTemps[0].description);
                            preferences.setTemp(temp);
                            preferences.setCode(predTemps[0].code);
                            bus.post(new EventClothes(moy, pluie));
                            act.ville= preferences.getVille();
                            String text = context.getResources().getString(R.string.Prevision)+" "+act.ville+" "+context.getResources().getString(R.string.Pourles)+" "+(spinner.getSelectedItemPosition()+1)*3+" "+context.getResources().getString(R.string.Prochaine);
                            textCity.setText(text);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, context.getResources().getString(R.string.ErreurChargement), Toast.LENGTH_SHORT).show();
                        bar.setVisibility(CircleProgressBar.INVISIBLE);
                    }
                });

        request.setPriority(Request.Priority.HIGH);
        helper.add(request);
        return predTemps;
    }*/
}

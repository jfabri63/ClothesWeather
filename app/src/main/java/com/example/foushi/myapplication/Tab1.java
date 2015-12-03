package com.example.foushi.myapplication;

import android.support.v4.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar;
import org.json.JSONObject;
import java.net.URLEncoder;
import de.greenrobot.event.EventBus;

public class Tab1 extends Fragment  {

    private SharedPreferences pref ;
    private TextView mTxtDegrees, mTxtDescription,mTxtcity,Ratio ;
    private ImageView img1;
    private final Volley helper = Volley.getInstance();
    private String ville;
    private final static String RECENT_API_ENDPOINT = "http://api.openweathermap.org/data/2.5/forecast?lat=";
    private final static String APIKEY = "&appid=6f38b1099567fec43b77a24f9448dcf3";
    private CircleProgressBar bar;
    private Spinner spinner;
    private int lastIndex ;
    private Temps[] predTemps = {null,null,null,null};
    private int index;
    private EventBus bus = EventBus.getDefault();
    private ImageButton Img3,Img4;
    private FloatingActionButton fab;


    public Tab1() {
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_main, container, false);
    }


    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        View v = getView();
        if (v != null)
        {
            mTxtDegrees = (TextView) v.findViewById(R.id.Text1);
            mTxtDescription = (TextView) v.findViewById(R.id.Text2);
            bar= (CircleProgressBar)v.findViewById(R.id.ProgressBar);
            spinner = (Spinner) v.findViewById(R.id.choix);
            mTxtcity = (TextView)v.findViewById(R.id.HelloWorld);
            img1 = (ImageView)v.findViewById(R.id.Img1);
            Ratio = (TextView) v.findViewById(R.id.ratio);
            Img3 = (ImageButton) v.findViewById(R.id.Img3);
            Img4 = (ImageButton) v.findViewById(R.id.Img4);
            fab = (FloatingActionButton) v.findViewById(R.id.fab);
        }

        index=1;
        bar.setColorSchemeResources(android.R.color.holo_blue_light);
        bar.setVisibility(CircleProgressBar.INVISIBLE);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.temps, R.layout.spinner);
        adapter.setDropDownViewResource(R.layout.spinner);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                loadWeatherData();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CharSequence colors[] = new CharSequence[]{getResources().getString(R.string.SelectCity)};
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle(getResources().getString(R.string.QueFaire));
                builder.setItems(colors, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            Intent i = new Intent(getActivity(), ChoseYourCity.class);
                            startActivity(i);
                        }
                    }
                });
                builder.show();
            }
        });
        pref = getActivity().getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        ville = pref.getString("ville", getResources().getString(R.string.AucuneVille));
        String temp = pref.getString("temp","");
        int img = pref.getInt("img", R.drawable.sun);
        img1.setImageResource(img);
        String forecast = pref.getString("temps","");
        String text = getResources().getString(R.string.Prevision)+" "+ville+" "+getResources().getString(R.string.Pourles)+" "+(spinner.getSelectedItemPosition()+1)*3+" "+getResources().getString(R.string.Prochaine);
        mTxtcity.setText(text);
        lastIndex=0;
        int index1 = lastIndex*3;
        int index2 = (lastIndex+1)*3;
        mTxtDegrees.setText(forecast+" ("+index1+"-"+index2+"h)");
        mTxtDescription.setText(temp);

        Img3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (predTemps[0]!=null)
                {
                    if (lastIndex<3 && predTemps[lastIndex+1]!=null)
                    {
                        img1.setImageResource(getIcon(predTemps[lastIndex + 1].code));
                        int index1 = (lastIndex+1)*3;
                        int index2 = (lastIndex+2)*3;
                        mTxtDegrees.setText(predTemps[lastIndex + 1].description+" ("+index1+"-"+index2+"h)");
                        String ratio = (lastIndex+2)+"/"+(index+1);
                        Ratio.setText(ratio);
                        lastIndex++;
                    }
                    else
                    {
                        img1.setImageResource(getIcon(predTemps[0].code));
                        int index1 = 0;
                        int index2 = 3;
                        mTxtDegrees.setText(predTemps[0].description+" ("+index1+"-"+index2+"h)");
                        String ratio2 = "1/"+(index+1);
                        Ratio.setText(ratio2);
                        lastIndex=0;
                    }
                }

            }
        });
        Img4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (predTemps[0]!=null)
                {
                    if (lastIndex>0 && predTemps[lastIndex-1]!=null)
                    {
                        img1.setImageResource(getIcon(predTemps[lastIndex - 1].code));
                        int index1 = (lastIndex-1)*3;
                        int index2 = (lastIndex)*3;
                        mTxtDegrees.setText(predTemps[lastIndex - 1].description+" ("+index1+"-"+index2+"h)");
                        String ratio3=(lastIndex)+"/"+(index+1);
                        Ratio.setText(ratio3);
                        lastIndex--;
                    }
                    else
                    {
                        img1.setImageResource(getIcon(predTemps[index].code));
                        int index1 = (index)*3;
                        int index2 = (index+1)*3;
                        mTxtDegrees.setText(predTemps[index].description+" ("+index1+"-"+index2+"h)");
                        String ratio4 = (index+1)+"/"+(index+1);
                        Ratio.setText(ratio4);
                        lastIndex=index;
                    }
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return id == R.id.action_settings || super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();
        pref = getActivity().getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        ville = pref.getString("ville", null);
        String text5 = getResources().getString(R.string.Prevision)+" "+ville+" "+getResources().getString(R.string.Pourles)+" "+(spinner.getSelectedItemPosition()+1)*3+" "+getResources().getString(R.string.Prochaine);
        mTxtcity.setText(text5);
        loadWeatherData();
    }

    private void loadWeatherData() {
        bar.setVisibility(CircleProgressBar.VISIBLE);
        String lat = pref.getString("lat", "0");
        String lng = pref.getString("lng","0");
        StringBuilder sb;
        String url=null;
        try {
            sb = new StringBuilder(RECENT_API_ENDPOINT);
            sb.append(URLEncoder.encode(lat, "utf8"));
            sb.append("&lon=");
            sb.append(URLEncoder.encode(lng, "utf8"));
            sb.append(APIKEY);
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
                            index= spinner.getSelectedItemPosition();
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
                                    case "01d" : desc= getResources().getString(R.string.Soleil); break;
                                    case "01n" : desc= getResources().getString(R.string.Soleil); break;
                                    case "02d" : desc= getResources().getString(R.string.SoleilNuage); break;
                                    case "02n" : desc= getResources().getString(R.string.SoleilNuage); break;
                                    case "03d" : desc= getResources().getString(R.string.Nuageux); break;
                                    case "03n" : desc= getResources().getString(R.string.Nuageux); break;
                                    case "04d" : desc= getResources().getString(R.string.Nuageux); break;
                                    case "04n" : desc= getResources().getString(R.string.Nuageux); break;
                                    case "09d" : desc= getResources().getString(R.string.AttentionPluie);pluie=true; break;
                                    case "09n" : desc= getResources().getString(R.string.AttentionPluie);pluie=true; break;
                                    case "10d" : desc= getResources().getString(R.string.PluieSoleil);pluie=true; break;
                                    case "10n" : desc= getResources().getString(R.string.PluieSoleil);pluie=true; break;
                                    case "11d" : desc= getResources().getString(R.string.AttentonOrage);pluie=true; break;
                                    case "11n" : desc= getResources().getString(R.string.AttentonOrage);pluie=true; break;
                                    case "13d" : desc= getResources().getString(R.string.AttentionNeige);pluie=true; break;
                                    case "13n" : desc= getResources().getString(R.string.AttentionNeige);pluie=true; break;
                                    case "50d" : desc= getResources().getString(R.string.AttentionBrouillard); break;
                                    case "50n" : desc= getResources().getString(R.string.AttentionBrouillard); break;
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
                            img1.setImageResource(getIcon(weathercode));
                            mTxtDegrees.setText(predTemps[0].description+" (0-3h)");
                            String ratio5 = "1/" + (index + 1);
                            Ratio.setText(ratio5);
                            mTxtDescription.setText(temp);
                            bar.setVisibility(CircleProgressBar.INVISIBLE);
                            SharedPreferences.Editor editor =pref.edit();
                            editor.putString("temps", predTemps[0].description);
                            editor.putString("temp", temp);
                            editor.putInt("img", getIcon(weathercode));
                            editor.apply();
                            bus.post(new Evenement(moy, pluie));
                            String text = getResources().getString(R.string.Prevision)+" "+ville+" "+getResources().getString(R.string.Pourles)+" "+(spinner.getSelectedItemPosition()+1)*3+" "+getResources().getString(R.string.Prochaine);
                            mTxtcity.setText(text);

                        } catch (Exception e) {
                            txtError(e);
                        }

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        txtError(error);
                        Toast.makeText(getActivity(), getResources().getString(R.string.ErreurChargement), Toast.LENGTH_SHORT).show();
                        bar.setVisibility(CircleProgressBar.INVISIBLE);
                    }
                });

        request.setPriority(Request.Priority.HIGH);
        helper.add(request);
    }

    private void txtError(Exception e) {
        e.printStackTrace();
    }

    private int getIcon (String s) {
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

}

package com.example.foushi.myapplication;


import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceClass {
    private SharedPreferences.Editor editor;
    private Context context;
    private SharedPreferences pref;

    public PreferenceClass (Context context){
        pref = context.getSharedPreferences("MyPref",Context.MODE_PRIVATE);
        editor = pref.edit();
    }

    public String getVille () {
        return pref.getString("ville", "Error");
    }
    public void setVille(String s)
    {
        editor.putString("ville",s);
        editor.commit();
    }
    public String getTemp (){
        return pref.getString("temp","");
    }

    public String getDesc(){
        return pref.getString("temps","");
    }

    public String getLat () {return pref.getString("lat", "0");}

    public void setLat () {

    }
    public String getLong(){
        return pref.getString("lng","0");
    }

    public void setDesc (String s) {
        editor.putString("temps",s);
        editor.apply();
    }

    public void setTemp (String s){
        editor.putString("temp",s);
        editor.apply();
    }

    public void setLat (String s){
        editor.putString("lat",s);
        editor.apply();
    }
    public void setLong(String s){
        editor.putString("lng",s);
        editor.apply();
    }
    public void setPlace(String s){
        editor.putString("place",s);
        editor.apply();
    }
    public String getPlace (){
        return pref.getString("place","0");
    }
    public String getCode (){
        return pref.getString("code","01d");
    }
    public void setCode (String s)
    {
        editor.putString("code",s);
        editor.apply();
    }
}

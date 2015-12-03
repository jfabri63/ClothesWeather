package com.example.foushi.myapplication;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import de.greenrobot.event.EventBus;

public class Tab2 extends Fragment {

    public Tab2(){
    }
    private ImageView img1,img2,img3,img4;
    private TextView txt1,txt2,txt3,txt4;
    private EventBus bus = EventBus.getDefault();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.vetement_layout, container, false);
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        View v = getView();
        if (v!=null)
        {
            img1 = (ImageView) v.findViewById(R.id.imagevetementhaut);
            img2 = (ImageView) v.findViewById(R.id.imagevetementhaut2);
            img3 = (ImageView) v.findViewById(R.id.imagevetementbas);
            img4 = (ImageView) v.findViewById(R.id.warning);
            txt1 = (TextView) v.findViewById(R.id.hautvetement);
            txt2 = (TextView) v.findViewById(R.id.hautvetement2);
            txt3 = (TextView) v.findViewById(R.id.bas);
            txt4 = (TextView) v.findViewById(R.id.pluieWarning);
        }

        bus.register(this);
    }

    public void onEvent(Evenement event){
        VetementTemp(event.getMoy(),event.isPluie());
    }

    @Override
    public void onStart(){
        super.onStart();
    }
    @Override
    public void onResume() {
        super.onResume();
    }

    public void VetementTemp (double temp, boolean pluie)
    {
        if (temp <= -10)
        {
            txt1.setText(getResources().getString(R.string.ManteauVeste)+" "+getResources().getString(R.string.ParkaPolaire));
            txt2.setText(getResources().getString(R.string.Haut)+" "+getResources().getString(R.string.Pull));
            txt3.setText(getResources().getString(R.string.Bas)+" "+getResources().getString(R.string.PantalonChaud));
                    img1.setImageResource(R.drawable.polar);
            img2.setImageResource(R.drawable.sweat);
            img3.setImageResource(R.drawable.polarpant);
        }
        if (temp <= 0 && temp > -10)
        {
            txt1.setText(getResources().getString(R.string.Haut)+" "+getResources().getString(R.string.Parka));
            txt2.setText(getResources().getString(R.string.DessousHaut) +" "+ getResources().getString(R.string.Pull));
            txt3.setText(getResources().getString(R.string.Bas)+" "+getResources().getString(R.string.PantalonChaud));
                    img1.setImageResource(R.drawable.parka);
            img2.setImageResource(R.drawable.pullover);
            img3.setImageResource(R.drawable.polarpant);
        }
        if (temp <= 10 && temp > 0)
        {
            txt1.setText(getResources().getString(R.string.Haut)+" "+getResources().getString(R.string.Manteau));
            txt2.setText(getResources().getString(R.string.DessousHaut) +" "+ getResources().getString(R.string.Pull));
            txt3.setText(getResources().getString(R.string.Bas)+" "+getResources().getString(R.string.Jeans));
                    img1.setImageResource(R.drawable.coat);
            img2.setImageResource(R.drawable.pullover);
            img3.setImageResource(R.drawable.jeans);
        }
        if (temp <= 20 && temp > 10)
        {
            txt1.setText(getResources().getString(R.string.Haut)+" "+getResources().getString(R.string.SweatShirt));
            txt2.setText(getResources().getString(R.string.DessousHaut)+" "+getResources().getString(R.string.Tshirt));
            txt3.setText(getResources().getString(R.string.Bas)+" "+getResources().getString(R.string.Jeans));
            img1.setImageResource(R.drawable.sweat);
            img2.setImageResource(R.drawable.tshirt);
            img3.setImageResource(R.drawable.jeans);
        }
        if (temp <= 30 && temp > 20)
        {
            txt1.setText(getResources().getString(R.string.Haut)+" "+getResources().getString(R.string.Chemise));
            txt2.setText(getResources().getString(R.string.HautAlternative)+" "+getResources().getString(R.string.Chemise));
            txt3.setText(getResources().getString(R.string.Bas)+" "+getResources().getString(R.string.PantalonLeger));
            img1.setImageResource(R.drawable.chemise);
            img2.setImageResource(R.drawable.tshirt);
            img3.setImageResource(R.drawable.chino);
        }
        if (temp > 30)
        {
            txt1.setText(getResources().getString(R.string.Haut)+" "+getResources().getString(R.string.Chemise));
            txt2.setText(getResources().getString(R.string.HautAlternative)+" "+getResources().getString(R.string.Tshirt));
            txt3.setText(getResources().getString(R.string.Bas) +" "+ getResources().getString(R.string.Short));
            img1.setImageResource(R.drawable.sweat);
            img2.setImageResource(R.drawable.tshirt);
            img3.setImageResource(R.drawable.shortpant);
        }
        if (pluie)
        {
            img4.setImageResource(R.drawable.warning);
            txt4.setText(getResources().getString(R.string.RisquePluie));
        }
        else
        {
            img4.setImageResource(android.R.color.transparent);
            txt4.setText("");
        }
    }

}
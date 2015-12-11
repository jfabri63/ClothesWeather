package models;


import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.foushi.myapplication.R;

public class SelectClothes {
    TextView txt1, txt2, txt3, txt4;
    ImageView image1, image2, image3, warning;
    Context context;

    public SelectClothes(TextView t1, TextView t2, TextView t3, TextView t4, ImageView img1, ImageView img2, ImageView img3, ImageView img4, Context context) {
        txt1 = t1;
        txt2 = t2;
        txt3 = t3;
        txt4 = t4;
        image1 = img1;
        image2 = img2;
        image3 = img3;
        warning = img4;
        this.context = context;
    }

    public void VetementTemp(double temp, boolean pluie) {
        if (temp <= -10) {
            txt1.setText(context.getResources().getString(R.string.ManteauVeste) + " " + context.getResources().getString(R.string.ParkaPolaire));
            txt2.setText(context.getResources().getString(R.string.Haut) + " " + context.getResources().getString(R.string.Pull));
            txt3.setText(context.getResources().getString(R.string.Bas) + " " + context.getResources().getString(R.string.PantalonChaud));
            image1.setImageResource(R.drawable.polar);
            image2.setImageResource(R.drawable.sweat);
            image3.setImageResource(R.drawable.polarpant);
        }
        if (temp <= 0 && temp > -10) {
            txt1.setText(context.getResources().getString(R.string.Haut) + " " + context.getResources().getString(R.string.Parka));
            txt2.setText(context.getResources().getString(R.string.DessousHaut) + " " + context.getResources().getString(R.string.Pull));
            txt3.setText(context.getResources().getString(R.string.Bas) + " " + context.getResources().getString(R.string.PantalonChaud));
            image1.setImageResource(R.drawable.parka);
            image2.setImageResource(R.drawable.pullover);
            image3.setImageResource(R.drawable.polarpant);
        }
        if (temp <= 10 && temp > 0) {
            txt1.setText(context.getResources().getString(R.string.Haut) + " " + context.getResources().getString(R.string.Manteau));
            txt2.setText(context.getResources().getString(R.string.DessousHaut) + " " + context.getResources().getString(R.string.Pull));
            txt3.setText(context.getResources().getString(R.string.Bas) + " " + context.getResources().getString(R.string.Jeans));
            image1.setImageResource(R.drawable.coat);
            image2.setImageResource(R.drawable.pullover);
            image3.setImageResource(R.drawable.jeans);
        }
        if (temp <= 20 && temp > 10) {
            txt1.setText(context.getResources().getString(R.string.Haut) + " " + context.getResources().getString(R.string.SweatShirt));
            txt2.setText(context.getResources().getString(R.string.DessousHaut) + " " + context.getResources().getString(R.string.Tshirt));
            txt3.setText(context.getResources().getString(R.string.Bas) + " " + context.getResources().getString(R.string.Jeans));
            image1.setImageResource(R.drawable.sweat);
            image2.setImageResource(R.drawable.tshirt);
            image3.setImageResource(R.drawable.jeans);
        }
        if (temp <= 30 && temp > 20) {
            txt1.setText(context.getResources().getString(R.string.Haut) + " " + context.getResources().getString(R.string.Chemise));
            txt2.setText(context.getResources().getString(R.string.HautAlternative) + " " + context.getResources().getString(R.string.Chemise));
            txt3.setText(context.getResources().getString(R.string.Bas) + " " + context.getResources().getString(R.string.PantalonLeger));
            image1.setImageResource(R.drawable.chemise);
            image2.setImageResource(R.drawable.tshirt);
            image3.setImageResource(R.drawable.chino);
        }
        if (temp > 30) {
            txt1.setText(context.getResources().getString(R.string.Haut) + " " + context.getResources().getString(R.string.Chemise));
            txt2.setText(context.getResources().getString(R.string.HautAlternative) + " " + context.getResources().getString(R.string.Tshirt));
            txt3.setText(context.getResources().getString(R.string.Bas) + " " + context.getResources().getString(R.string.Short));
            image1.setImageResource(R.drawable.sweat);
            image2.setImageResource(R.drawable.tshirt);
            image3.setImageResource(R.drawable.shortpant);
        }
        if (pluie) {
            warning.setImageResource(R.drawable.warning);
            txt4.setText(context.getResources().getString(R.string.RisquePluie));
        } else {
            warning.setImageResource(android.R.color.transparent);
            txt4.setText("");
        }

    }
}


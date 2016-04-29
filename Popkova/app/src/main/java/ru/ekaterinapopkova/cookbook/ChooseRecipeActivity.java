package ru.ekaterinapopkova.cookbook;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class ChooseRecipeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_recipe);

        String type;
        TextView tv0 = (TextView) findViewById(R.id.tv0);
        TextView tv1 = (TextView) findViewById(R.id.tv1);
        ImageView image= (ImageView) findViewById(R.id.image);
        type = getIntent().getStringExtra("type");

        switch (type) {
            case "Snacks":
                image.setImageResource(R.drawable.hsn);
                tv0.setText(R.string.three_sn);
                tv1.setText(R.string.tom2);
                break;
            case "Salads":
                image.setImageResource(R.drawable.hsal);
                tv0.setText(R.string.one_sal);
                tv1.setText(R.string.cez2);
                break;
            case "Soups":
                image.setImageResource(R.drawable.hsoup);
                tv0.setText(R.string.three_soup);
                tv1.setText(R.string.rass2);
                break;
            case "SecDishes":
                image.setImageResource(R.drawable.hsec);
                tv0.setText(R.string.three_dish);
                tv1.setText(R.string.kur2);
                break;
            case "Desserts":
                image.setImageResource(R.drawable.hdes);
                tv0.setText(R.string.two_des);
                tv1.setText(R.string.p2);
                break;
            default:
                image.setImageResource(R.drawable.hdr);
                tv0.setText(R.string.one_dr);
                tv1.setText(R.string.kok2);
                break;
        }
    }
}

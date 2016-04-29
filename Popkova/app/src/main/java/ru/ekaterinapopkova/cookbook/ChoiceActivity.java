package ru.ekaterinapopkova.cookbook;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class ChoiceActivity extends AppCompatActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choice);
    }

    public void onClick(View view) {
        String type;
        switch (view.getId()) {
            case R.id.salads:
                type = "Salads";
                break;
            case R.id.snacks:
                type = "Snacks";
                break;
            case R.id.soups:
                type = "Soups";
                break;
            case R.id.secdishes:
                type = "SecDishes";
                break;
            case R.id.desserts:
                type = "Desserts";
                break;
            default:
                type = "Drinks";
            }
        Intent intent = new Intent(ChoiceActivity.this, ChooseRecipeActivity.class);
        intent.putExtra("type", type);
        startActivity(intent);
        }
    }

package iuh.doan.coffeeshop.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.List;

import iuh.doan.coffeeshop.R;
import iuh.doan.coffeeshop.model.Drink;

public class DrinkAdapter extends ArrayAdapter<Drink> {

    Activity context;
    int resource;
    List<Drink> objects;

    public DrinkAdapter(Activity context, int resource, List<Drink> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.objects = objects;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = this.context.getLayoutInflater();
        View item = inflater.inflate(this.resource, null);

        TextView textViewMaOnListViewDrinks = item.findViewById(R.id.textViewMaOnListViewDrinks);
        TextView textViewTenOnListViewDrinks = item.findViewById(R.id.textViewTenOnListViewDrinks);
        TextView textViewGiaOnListViewDrinks = item.findViewById(R.id.textViewGiaOnListViewDrinks);

        final Drink drink = this.objects.get(position);

        textViewMaOnListViewDrinks.setText("Mã: " + drink.getMa());
        textViewTenOnListViewDrinks.setText(drink.getTen());
        textViewGiaOnListViewDrinks.setText(drink.getGia()+"đ");

        return item;
    }
}

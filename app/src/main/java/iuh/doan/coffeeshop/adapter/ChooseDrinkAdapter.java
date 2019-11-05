package iuh.doan.coffeeshop.adapter;

import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import iuh.doan.coffeeshop.R;
import iuh.doan.coffeeshop.model.Drink;
import iuh.doan.coffeeshop.model.OrderDetails;

public class ChooseDrinkAdapter extends ArrayAdapter<OrderDetails> {

    Activity context;
    int resource;
    ArrayList<OrderDetails> objects;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference tableDrink;

    public ChooseDrinkAdapter(Activity context, int resource, ArrayList<OrderDetails> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.objects = objects;
        firebaseDatabase = FirebaseDatabase.getInstance();
        tableDrink = firebaseDatabase.getReference("drink");
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = this.context.getLayoutInflater();
        final View item = inflater.inflate(this.resource, null);

        final TextView textViewDrinkName = item.findViewById(R.id.textViewDrinkName);
        final TextView textViewDrinkPrice = item.findViewById(R.id.textViewDrinkPrice);
        final TextView textViewDrinkID = item.findViewById(R.id.textViewDrinkID);
        final TextView textViewDrinkCount = item.findViewById(R.id.textViewDrinkCount);
        ImageButton imageButtonSubDrink = item.findViewById(R.id.imageButtonSubDrink);
        ImageButton imageButtonAddDrink = item.findViewById(R.id.imageButtonAddDrink);

        final OrderDetails orderDetails = this.objects.get(position);
        if (orderDetails.getNum() > 0) {
            item.setBackgroundColor(Color.rgb(255,245,157));
        }
        tableDrink.child(orderDetails.getDrinkId()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Drink drink = dataSnapshot.getValue(Drink.class);
                textViewDrinkID.setText("Mã: " + orderDetails.getDrinkId());
                textViewDrinkName.setText(drink.getTen());
                textViewDrinkPrice.setText("Giá: " + drink.getGia()+"đ");
                textViewDrinkCount.setText(String.valueOf(orderDetails.getNum()));
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        imageButtonAddDrink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count = Integer.parseInt(textViewDrinkCount.getText().toString());
                count++;
                textViewDrinkCount.setText(String.valueOf(count));
                item.setBackgroundColor(Color.rgb(255,245,157));
            }
        });
        imageButtonSubDrink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count = Integer.parseInt(textViewDrinkCount.getText().toString());
                if (count > 0){
                    count--;
                    textViewDrinkCount.setText(String.valueOf(count));
                    if (count == 0) {
                        item.setBackgroundColor(Color.rgb(250,250,250));
                    }
                }
            }
        });
        return item;
    }
}

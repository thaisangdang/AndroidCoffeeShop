package iuh.doan.coffeeshop.adapter;

import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import iuh.doan.coffeeshop.R;
import iuh.doan.coffeeshop.model.Order;

public class OrderAdapter extends ArrayAdapter<Order> {

    Activity context;
    int resource;
    List<Order> objects;

    public OrderAdapter(Activity context, int resource, List<Order> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.objects = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = this.context.getLayoutInflater();
        View item = inflater.inflate(this.resource, null);

        TextView textMaOnListViewOrders = item.findViewById(R.id.textMaOnListViewOrders);
        TextView textSoBanOnListViewOrders = item.findViewById(R.id.textSoBanOnListViewOrders);
        TextView textTotalCostOnListViewOrders = item.findViewById(R.id.textTotalCostOnListViewOrders);
        TextView textCreatedTimeOnListViewOrders = item.findViewById(R.id.textCreatedTimeOnListViewOrders);
        TextView textStatusOnListViewOrders = item.findViewById(R.id.textStatusOnListViewOrders);

        final Order order = this.objects.get(position);

        textMaOnListViewOrders.setText("Order " + order.getMa());
        textSoBanOnListViewOrders.setText("Bàn " + order.getMaBan());
        textTotalCostOnListViewOrders.setText(order.getTotalCost()+"đ");

        SimpleDateFormat sf = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss a");
        Date date = new Date(Long.parseLong(order.getCreatedTime()));
        textCreatedTimeOnListViewOrders.setText(sf.format(date));

        textStatusOnListViewOrders.setText(order.getStatus());
        if (order.getStatus().equals("unpaid")) {
            textStatusOnListViewOrders.setTextColor(Color.rgb(46,204,113));
        } else {
            textStatusOnListViewOrders.setTextColor(Color.rgb(231,76,60));
        }

        return item;
    }
}

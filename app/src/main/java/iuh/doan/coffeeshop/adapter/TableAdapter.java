package iuh.doan.coffeeshop.adapter;

import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import iuh.doan.coffeeshop.R;
import iuh.doan.coffeeshop.model.Table;

public class TableAdapter extends ArrayAdapter<Table> {

    Activity context;
    int resource;
    List<Table> objects;

    public TableAdapter(Activity context, int resource, List<Table> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.objects = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = this.context.getLayoutInflater();
        View item = inflater.inflate(this.resource, null);

        TextView textIdOnListViewTables = item.findViewById(R.id.textSoBanOnListViewTables);
        TextView textStatusOnListViewTables = item.findViewById(R.id.textStatusOnListViewTables);
        TextView textMoTaOnListViewTables = item.findViewById(R.id.textMoTaOnListViewTables);

        final Table table = this.objects.get(position);

        textIdOnListViewTables.setText(String.valueOf(table.getSoBan()));
        textStatusOnListViewTables.setText(table.getStatus());
        textMoTaOnListViewTables.setText(table.getMoTa());

        if (table.getStatus().equals("available")) {
            textStatusOnListViewTables.setTextColor(Color.rgb(46,204,113));
        } else {
            textStatusOnListViewTables.setTextColor(Color.rgb(231,76,60));
        }

        return item;
    }
}

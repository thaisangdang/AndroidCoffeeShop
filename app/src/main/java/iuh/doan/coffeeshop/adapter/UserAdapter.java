package iuh.doan.coffeeshop.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import iuh.doan.coffeeshop.HomeActivity;
import iuh.doan.coffeeshop.R;
import iuh.doan.coffeeshop.model.User;

public class UserAdapter extends ArrayAdapter<User> {

    Activity context;
    int resource;
    List<User> objects;

    public UserAdapter(Activity context, int resource, List<User> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.objects = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = this.context.getLayoutInflater();
        View item = inflater.inflate(this.resource, null);

        TextView textViewUsername = item.findViewById(R.id.textViewUsernameOnListViewUsers);

        final User user = this.objects.get(position);

        textViewUsername.setText(user.getName());

        return item;
    }
}

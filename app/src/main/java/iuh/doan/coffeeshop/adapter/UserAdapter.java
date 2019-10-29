package iuh.doan.coffeeshop.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import iuh.doan.coffeeshop.R;
import iuh.doan.coffeeshop.model.User;

public class UserAdapter extends ArrayAdapter<User> {

    Activity context;
    int resource;
//    List<User> users;

    public UserAdapter(Activity context, int resource) {
        super(context, resource);
        this.context = context;
        this.resource = resource;
//        this.users = users;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = this.context.getLayoutInflater();
        View item = inflater.inflate(this.resource, null);

        TextView textViewUsername = item.findViewById(R.id.textViewUsernameOnListViewUsers);
        Button buttonEdit = item.findViewById(R.id.buttonEditOnListViewUsers);
        Button buttonDelete = item.findViewById(R.id.buttonDeleteOnListViewUsers);

//        final User user = this.users.get(position);

        final User user = getItem(position);
        textViewUsername.setText(user.getName());
        buttonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit(user);
            }
        });
        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                delete(user);
            }
        });

        return item;
    }

    private void delete(User user) {

    }

    private void edit(User user) {

    }
}

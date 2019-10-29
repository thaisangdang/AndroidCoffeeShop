package iuh.doan.coffeeshop.ui.tables;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import iuh.doan.coffeeshop.R;

public class TablesFragment extends Fragment {

    private TablesViewModel tablesViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        tablesViewModel =
                ViewModelProviders.of(this).get(TablesViewModel.class);
        View root = inflater.inflate(R.layout.fragment_tables, container, false);
        final TextView textView = root.findViewById(R.id.text_tables);
        tablesViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}
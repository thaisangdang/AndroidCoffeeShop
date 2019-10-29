package iuh.doan.coffeeshop.ui.drinks;

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

public class DrinksFragment extends Fragment {

    private DrinksViewModel drinksViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        drinksViewModel =
                ViewModelProviders.of(this).get(DrinksViewModel.class);
        View root = inflater.inflate(R.layout.fragment_drinks, container, false);
        final TextView textView = root.findViewById(R.id.text_drinks);
        drinksViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}
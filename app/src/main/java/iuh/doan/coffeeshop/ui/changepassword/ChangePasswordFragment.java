package iuh.doan.coffeeshop.ui.changepassword;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import iuh.doan.coffeeshop.R;

public class ChangePasswordFragment extends Fragment {

    private ChangePasswordViewModel changePasswordViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        changePasswordViewModel =
                ViewModelProviders.of(this).get(ChangePasswordViewModel.class);
        View root = inflater.inflate(R.layout.fragment_accounts, container, false);
        final TextView textView = root.findViewById(R.id.text_accounts);
        changePasswordViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}
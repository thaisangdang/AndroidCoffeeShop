package iuh.doan.coffeeshop;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

import info.hoang8f.widget.FButton;
import iuh.doan.coffeeshop.helper.AuthenHelper;
import iuh.doan.coffeeshop.model.User;

public class SignInActivity extends AppCompatActivity {

    MaterialEditText edtPhone, edtPassword;
    FButton btnSignIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        edtPhone = this.findViewById(R.id.edtPhoneOnSignInActivity);
        edtPassword = this.findViewById(R.id.edtPasswordOnSignInActivity);
        btnSignIn = this.findViewById(R.id.btnSignInOnSignInActivity);

        // Initial Firebasedatabase
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        final DatabaseReference table_user = firebaseDatabase.getReference("user");

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final ProgressDialog progressDialog = new ProgressDialog(SignInActivity.this);
                progressDialog.setMessage("Please waiting");
                progressDialog.show();

                table_user.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // check if user not exist in database
                        if (dataSnapshot.child(edtPhone.getText().toString()).exists()) {
                            // get user information
                            progressDialog.dismiss();
                            User user = dataSnapshot.child(edtPhone.getText().toString()).getValue(User.class);
                            user.setId(dataSnapshot.getKey());
                            if (user.getPassword().equals(edtPassword.getText().toString())) {
                                Toast.makeText(SignInActivity.this, "Sign in successfully!", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(SignInActivity.this, HomeActivity.class);
                                AuthenHelper.currentUser = user;
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(SignInActivity.this, "Sign in failed!", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(SignInActivity.this, "User not exist in Database", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });

    }
}

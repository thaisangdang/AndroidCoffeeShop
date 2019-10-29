package iuh.doan.coffeeshop;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.FirebaseDatabase;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;

import iuh.doan.coffeeshop.helper.AuthenHelper;
import iuh.doan.coffeeshop.ui.home.HomeFragment;
import iuh.doan.coffeeshop.ui.order.OrderFragment;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private AppBarConfiguration mAppBarConfiguration;

    FirebaseDatabase firebaseDatabase;

    TextView txtUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);

        toolbar.setTitle("Menu");

        setSupportActionBar(toolbar);

        // initial database
        firebaseDatabase = FirebaseDatabase.getInstance();

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home,
                R.id.nav_order, R.id.nav_orderHistory, R.id.nav_statistic,
                R.id.nav_drinks, R.id.nav_accounts, R.id.nav_tables,
                R.id.nav_changePassword, R.id.nav_logout)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController controller, @NonNull
                    NavDestination destination, @Nullable Bundle arguments) {
                if (destination.getId() == R.id.nav_home) {
                    Toast.makeText(HomeActivity.this, "nav_home", Toast.LENGTH_LONG).show();
                }
                if (destination.getId() == R.id.nav_order) {
                    Toast.makeText(HomeActivity.this, "nav_order", Toast.LENGTH_LONG).show();
                }
                if (destination.getId() == R.id.nav_orderHistory) {
                    Toast.makeText(HomeActivity.this, "nav_orderHistory", Toast.LENGTH_LONG).show();
                }
                if (destination.getId() == R.id.nav_statistic) {
                    Toast.makeText(HomeActivity.this, "nav_statistic", Toast.LENGTH_LONG).show();
                }
                if (destination.getId() == R.id.nav_accounts) {
                    Toast.makeText(HomeActivity.this, "nav_accounts", Toast.LENGTH_LONG).show();
                }
                if (destination.getId() == R.id.nav_tables) {
                    Toast.makeText(HomeActivity.this, "nav_tables", Toast.LENGTH_LONG).show();
                }
                if (destination.getId() == R.id.nav_drinks) {
                    Toast.makeText(HomeActivity.this, "nav_drinks", Toast.LENGTH_LONG).show();
                }
                if (destination.getId() == R.id.nav_changePassword) {
                    Toast.makeText(HomeActivity.this, "nav_changePassword", Toast.LENGTH_LONG).show();
                }
                if (destination.getId() == R.id.nav_logout) {
                    Toast.makeText(HomeActivity.this, "nav_logout", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(HomeActivity.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
            }
        });

        // Set name for user
        View header = navigationView.getHeaderView(0);
        txtUsername = header.findViewById(R.id.txtUsername);
        txtUsername.setText(AuthenHelper.currentUser.getName());

    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        return false;
    }
}

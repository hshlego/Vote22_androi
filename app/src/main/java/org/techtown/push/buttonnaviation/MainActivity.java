package org.techtown.push.buttonnaviation;

import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.messaging.FirebaseMessaging;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.splashscreen.SplashScreen;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import org.techtown.push.buttonnaviation.databinding.ActivityMainBinding;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SplashScreen splashScreen = SplashScreen.installSplashScreen(this);

        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //register id for firebase
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if(!task.isSuccessful()) {
                            return;
                        }
                        String newToken = task.getResult();
                        registerToken(newToken);
                    }
                });

        //
        BottomNavigationView navView = binding.navView;
        navView.setItemIconTintList(null);

        //action bar setup
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_notifications, R.id.navigation_dashboard)
                .build();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupWithNavController(binding.navView, navController);
    }

    private void registerToken(String newToken) {
        TokenRetrofit tokenRetrofit = new TokenRetrofit();
        Map<String, String> map = new HashMap<String, String>();
        map.put("regid", newToken);
        tokenRetrofit.insert.insertOne(map).enqueue(new Callback<Registered>() {
            @Override
            public void onResponse(Call<Registered> call, Response<Registered> response) {
                if(!response.isSuccessful()) return;
            }
            @Override
            public void onFailure(Call<Registered> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
}
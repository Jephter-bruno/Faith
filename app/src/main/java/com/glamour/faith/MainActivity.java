package com.glamour.faith;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.glamour.faith.adapters.ViewPagerAdapter;
import com.glamour.faith.drop.NotificationFragment;
import com.glamour.faith.drop.OurArtistActivity;
import com.glamour.faith.drop.ProfileActivity;
import com.glamour.faith.drop.UpcomingEventsActivity;
import com.glamour.faith.ui.ChristianMoviesActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import org.jetbrains.annotations.NotNull;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private ViewPagerAdapter adapter;
    DatabaseReference userRef;
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        fab = findViewById(R.id.fab);
        mAuth=FirebaseAuth.getInstance();
        userRef = FirebaseDatabase.getInstance().getReference().child("Members");
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,ChatgptSplashActivity.class));
            }
        });
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_post, R.id.navigation_adverts,R.id.navigation_testimony)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

/*
        if (id == R.id.action_message) {
            Fragment mess = new MessagesFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.nav_host_fragment,mess);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();

            return true;
        }
*/
        if (id == R.id.action_notification) {
            Fragment mess = new NotificationFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.nav_host_fragment,mess);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
            return true;
        }

        if (id == R.id.action_profile) {
            startActivity(new Intent(this, ProfileActivity.class));
            return true;
        }
/*
        if (id == R.id.action_donate) {
            Fragment mess = new DonateFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.nav_host_fragment,mess);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
            return true;
        }
*/
        if (id == R.id.action_events) {
            startActivity(new Intent(this, UpcomingEventsActivity.class));

            return true;
        }
        if (id == R.id.action_movies) {
            Toast.makeText(this, "Coming soon", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, ChristianMoviesActivity.class));
            return true;
        }
/*
        if (id == R.id.action_biblical_movies) {
            Toast.makeText(this, "Coming soon", Toast.LENGTH_SHORT).show();
            */
/*startActivity(new Intent(this, BiblicalMoviesActivity.class));*//*

            return true;
        }
*/
        if (id == R.id.action_artist) {
           startActivity(new Intent(this, OurArtistActivity.class));

            return true;
        }



/*
        if (id == R.id.action_bible) {
            Fragment mess = new BibleFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.nav_host_fragment,mess);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
            return true;
        }
*/
       /* if (id == R.id.action_listen) {
            Fragment mess = new GospelFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.nav_host_fragment,mess);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
            return true;
        }*/
        if (id == R.id.action_logout) {
          mAuth.signOut();
          startActivity(new Intent(this, LoginActivity.class));
          finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentuserID = mAuth.getCurrentUser();

        if(currentuserID == null){
            startActivity(new Intent(MainActivity.this,LoginActivity.class));
        }
        else
        {
            CheckUserExistence();
        }
    }

    private void CheckUserExistence() {
      final String curent_user_id = mAuth.getCurrentUser().getUid();
      userRef.addValueEventListener(new ValueEventListener() {
          @Override
          public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
              if(!snapshot.hasChild(curent_user_id)){
                  Intent setupActivity = new Intent(MainActivity.this, SetUpActivity.class);
                  setupActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                  startActivity(setupActivity);
                  finish();
              }
          }

          @Override
          public void onCancelled(@NonNull @NotNull DatabaseError error) {

          }
      });
    }
}
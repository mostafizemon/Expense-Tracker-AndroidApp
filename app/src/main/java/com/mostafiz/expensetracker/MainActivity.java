package com.mostafiz.expensetracker;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.navigation.NavigationBarView;
import com.mostafiz.expensetracker.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding =ActivityMainBinding.inflate(getLayoutInflater());
        View view=binding.getRoot();
        setContentView(view);


        binding.bottomnavigationview.setBackground(null);
        replaceFragment(new HomeFragment());
        binding.bottomnavigationview.setSelectedItemId(R.id.home);
/////////////////////////////////////////////////////
////------------- Bottomnavigation View -----------------------//
/////////////////////////////////////////////////////
        binding.bottomnavigationview.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId()==R.id.home){
                    replaceFragment(new HomeFragment());
                }
                else if (item.getItemId()==R.id.reminder){
                    replaceFragment(new RecentFragment());
                }
                else if (item.getItemId()==R.id.statistics){
                    replaceFragment(new StatisticsFragment());
                }
                else if (item.getItemId()==R.id.receipt){
                    replaceFragment(new ReceiptFragment());
                }
                return true;
            }
        });

        binding.addbottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replaceFragment(new AddFragment());
                binding.bottomnavigationview.setSelectedItemId(R.id.fab);
            }
        });

    }

    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.mainactivityframelayout, fragment);
        fragmentTransaction.commit();
    }


    @Override
    public void onBackPressed() {
        if (binding.bottomnavigationview.getSelectedItemId() != R.id.home) {
            binding.bottomnavigationview.setSelectedItemId(R.id.home);
        } else {
            super.onBackPressed();
        }
    }


}
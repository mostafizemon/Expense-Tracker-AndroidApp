package com.mostafiz.expensetracker;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mostafiz.expensetracker.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {

    FragmentHomeBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding=FragmentHomeBinding.inflate(getLayoutInflater(),container,false);
        View view=binding.getRoot();






        return view;
    }
//--------------------------------------------------------------------------------------





//--------------------------------------------------------------------------------------
}
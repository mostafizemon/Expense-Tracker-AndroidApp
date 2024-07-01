package com.mostafiz.expensetracker;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.mostafiz.expensetracker.databinding.FragmentHomeBinding;

import java.util.ArrayList;
import java.util.HashMap;

public class HomeFragment extends Fragment {

    FragmentHomeBinding binding;
    DatabaseHelper databaseHelper;
    ArrayList<HashMap<String,String>> arrayList;
    HashMap<String,String> hashMap;
    boolean isExpenseSelected = true;
    public static String selected;
    public static String showcategory;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding=FragmentHomeBinding.inflate(getLayoutInflater(),container,false);
        View view=binding.getRoot();

        databaseHelper=new DatabaseHelper(getContext());
        updaterealtime();

        binding.homesegmentedcontrol.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.home_expense) {
                    isExpenseSelected = true;
                } else if (checkedId == R.id.home_income) {
                    isExpenseSelected = false;
                }
                updateGridview();
            }
        });

        updateGridview();
        return view;
    }
//--------------------------------------------------------------------------------------
public void updaterealtime(){
        binding.hometotalexpenseamount.setText("BDT "+databaseHelper.calculatetotalexpense());
        binding.hometotalincomeamount.setText("BDT "+databaseHelper.calculatetotalincome());
}

    @Override
    public void onResume() {
        super.onResume();
        updaterealtime();
    }

    //---------------------------------------------------------------

    public void updateGridview() {
        arrayList = new ArrayList<>(); // Initialize a new ArrayList
        Cursor cursor;

        if (isExpenseSelected) {
            cursor = databaseHelper.getExpenseSumAmountByCategory();
        } else {
            cursor = databaseHelper.getIncomeSumAmountByCategory();
        }

        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                double amount = cursor.getDouble(1);
                String category = cursor.getString(0);

                HashMap<String, String> hashMap = new HashMap<>(); // Create a new HashMap for each entry
                hashMap.put("amount", String.valueOf(amount));
                hashMap.put("category", category);
                arrayList.add(hashMap);
            }
            cursor.close();

            binding.homegridview.setAdapter(new MyAdapter());
            binding.homeErrormsg.setVisibility(View.GONE); // Hide the error message if data is found
        } else {
            binding.homeErrormsg.setVisibility(View.VISIBLE);
            binding.homeErrormsg.setText("No Data Found");
        }
    }
//    public void updateGridview(){
//        if (isExpenseSelected){
//            Cursor cursor= databaseHelper.getExpenseSumAmountByCategory();
//            if (cursor!=null && cursor.getCount()>0){
//                arrayList=new ArrayList<>();
//                while (cursor.moveToNext()){
//                    double amount=cursor.getDouble(1);
//                    String category=cursor.getString(0);
//
//                    hashMap=new HashMap<>();
//                    hashMap.put("amount", String.valueOf(amount));
//                    hashMap.put("category",category);
//                    arrayList.add(hashMap);
//
//                }
//                binding.homegridview.setAdapter(new MyAdapter());
//            }
//            else {
//                binding.homeErrormsg.setVisibility(View.VISIBLE);
//                binding.homeErrormsg.setText("No Data Found");
//            }
//
//        }
//        else {
//            Cursor cursor= databaseHelper.getIncomeSumAmountByCategory();
//            if (cursor!=null && cursor.getCount()>0){
//                arrayList=new ArrayList<>();
//                while (cursor.moveToNext()){
//                    double amount=cursor.getDouble(1);
//                    String category=cursor.getString(0);
//
//                    hashMap=new HashMap<>();
//                    hashMap.put("amount", String.valueOf(amount));
//                    hashMap.put("category",category);
//                    arrayList.add(hashMap);
//
//                }
//                binding.homegridview.setAdapter(new MyAdapter());
//            }
//            else {
//                binding.homeErrormsg.setVisibility(View.VISIBLE);
//                binding.homeErrormsg.setText("No Data Found");
//
//            }
//        }
//    }
    //--------------------------------------------------------------------------------


    public class MyAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return arrayList.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            LayoutInflater inflater=getLayoutInflater();
            View myView=inflater.inflate(R.layout.gridview_home,viewGroup,false);

            TextView tvcategory=myView.findViewById(R.id.gridview_category);
            TextView tvamount=myView.findViewById(R.id.gridview_amount);

            hashMap=arrayList.get(i);
            String amount=hashMap.get("amount");
            String category=hashMap.get("category");

            tvcategory.setText(category);
            tvamount.setText("BDT "+amount);

            if (isExpenseSelected==true){

                myView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(getContext(),ShowActivity.class));
                        selected="expense";
                        showcategory=category;
                    }
                });
            }
            else {
                myView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(getContext(),ShowActivity.class));
                        selected="income";
                        showcategory=category;
                    }
                });

            }


            return myView;
        }
    }
//--------------------------------------------------------------------------------------
}
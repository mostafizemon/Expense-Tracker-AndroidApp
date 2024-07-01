package com.mostafiz.expensetracker;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.mostafiz.expensetracker.databinding.FragmentReceiptBinding;
import com.mostafiz.expensetracker.databinding.FragmentRecentBinding;

import java.util.ArrayList;
import java.util.HashMap;


public class RecentFragment extends Fragment {

    FragmentRecentBinding binding;
    private DatabaseHelper databaseHelper;
    private ArrayList<HashMap<String, String>> arrayList;
    private HashMap<String, String> hashMap;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding=FragmentRecentBinding.inflate(getLayoutInflater(),container,false);
        View view=binding.getRoot();

        databaseHelper = new DatabaseHelper(getContext());
        fetchRecentTransactions();





        return view;
    }


    private void fetchRecentTransactions() {
        Cursor cursor = databaseHelper.getRecentTransactions();
        if (cursor != null && cursor.getCount() > 0) {
            arrayList = new ArrayList<>();
            while (cursor.moveToNext()) {
                @SuppressLint("Range") String type = cursor.getString(cursor.getColumnIndex("type"));
                @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex("id"));
                @SuppressLint("Range") String date = cursor.getString(cursor.getColumnIndex("date"));
                @SuppressLint("Range") String category = cursor.getString(cursor.getColumnIndex("category"));
                @SuppressLint("Range") String description = cursor.getString(cursor.getColumnIndex("description"));
                @SuppressLint("Range") double amount = cursor.getDouble(cursor.getColumnIndex("amount"));

                hashMap = new HashMap<>();
                hashMap.put("type", type);
                hashMap.put("id", String.valueOf(id));
                hashMap.put("date", date);
                hashMap.put("category", category);
                hashMap.put("description", description);
                hashMap.put("amount", String.valueOf(amount));
                arrayList.add(hashMap);
            }
            cursor.close();
            if (arrayList != null && !arrayList.isEmpty()) {
                binding.recentListView.setAdapter(new MyAdapter());
            }
            else {
                binding.recenterrormsg.setVisibility(View.VISIBLE);
            }
        }

        else {
            binding.recenterrormsg.setVisibility(View.VISIBLE);

        }
    }


    public class MyAdapter extends BaseAdapter {

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

        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater=getLayoutInflater();
            View myview=inflater.inflate(R.layout.list_item_recent,parent,false);

            TextView tvCategory = myview.findViewById(R.id.recent_category);
            TextView tvAmount = myview.findViewById(R.id.recent_amount);
            TextView tvDescription = myview.findViewById(R.id.recent_description);
            TextView tvDate = myview.findViewById(R.id.recent_date);
            ImageButton editButton = myview.findViewById(R.id.edit);
            ImageButton deleteButton = myview.findViewById(R.id.delete);

            hashMap=arrayList.get(position);

            String type = hashMap.get("type");
            String date = hashMap.get("date");
            String category = hashMap.get("category");
            String description = hashMap.get("description");
            String amount = hashMap.get("amount");
            String id=hashMap.get("id");

            tvCategory.setText(category);
            tvAmount.setText(amount);
            tvDescription.setText(description);
            tvDate.setText(date);

            editButton.setOnClickListener(v -> {
                // Handle edit button click
            });

            deleteButton.setOnClickListener(v -> {
                databaseHelper.delete(type,id);
                // Handle delete button click
                fetchRecentTransactions();
            });

            return myview;
        }
    }















}
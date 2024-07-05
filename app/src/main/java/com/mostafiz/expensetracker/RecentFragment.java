package com.mostafiz.expensetracker;

import static android.app.Activity.RESULT_OK;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.provider.Telephony;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.ads.nativetemplates.NativeTemplateStyle;
import com.google.android.ads.nativetemplates.TemplateView;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.nativead.NativeAd;
import com.mostafiz.expensetracker.databinding.FragmentReceiptBinding;
import com.mostafiz.expensetracker.databinding.FragmentRecentBinding;

import java.util.ArrayList;
import java.util.HashMap;


public class RecentFragment extends Fragment {

    FragmentRecentBinding binding;
    private DatabaseHelper databaseHelper;
    private ArrayList<HashMap<String, String>> arrayList;
    private HashMap<String, String> hashMap;
    private static final int EDIT_REQUEST_CODE = 1;
    TemplateView templateView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding=FragmentRecentBinding.inflate(getLayoutInflater(),container,false);
        View view=binding.getRoot();
        databaseHelper = new DatabaseHelper(getContext());
        fetchRecentTransactions();
        templateView=view.findViewById(R.id.my_template);



        MobileAds.initialize(getContext());





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

        private static final int TYPE_ITEM = 0;
        private static final int TYPE_AD = 1;

        @Override
        public int getCount() {
            int itemCount = arrayList.size();
            int adCount = itemCount / 3;
            return itemCount + adCount;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public int getItemViewType(int position) {
            return (position + 1) % 4 == 0 ? TYPE_AD : TYPE_ITEM;
        }

        @Override
        public int getViewTypeCount() {
            return 2; // Two types: regular items and ad items
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            int viewType = getItemViewType(position);

            if (viewType == TYPE_AD) {
                if (convertView == null) {
                    LayoutInflater inflater = getLayoutInflater();
                    convertView = inflater.inflate(R.layout.native_ad, parent, false);
                }
                // Check for network availability before loading the ad
                if (NetworkUtils.isNetworkAvailable(getContext())) {
                    TemplateView templateView = convertView.findViewById(R.id.my_template);
                    AdLoader adLoader = new AdLoader.Builder(getContext(), "ca-app-pub-3940256099942544/2247696110")
                            .forNativeAd(nativeAd -> templateView.setNativeAd(nativeAd))
                            .build();
                    adLoader.loadAd(new AdRequest.Builder().build());
                    templateView.setVisibility(View.VISIBLE);
                } else {
                    // Hide the ad view if there is no internet connection
                    convertView.setVisibility(View.GONE);
                }
            } else {
                if (convertView == null) {
                    LayoutInflater inflater = getLayoutInflater();
                    convertView = inflater.inflate(R.layout.list_item_recent, parent, false);
                }

                TextView tvCategory = convertView.findViewById(R.id.recent_category);
                TextView tvAmount = convertView.findViewById(R.id.recent_amount);
                TextView tvDescription = convertView.findViewById(R.id.recent_description);
                TextView tvDate = convertView.findViewById(R.id.recent_date);
                ImageButton editButton = convertView.findViewById(R.id.edit);
                ImageButton deleteButton = convertView.findViewById(R.id.delete);

                int actualPosition = position - (position / 4); // Adjust position for ads
                hashMap = arrayList.get(actualPosition);

                String type = hashMap.get("type");
                String date = hashMap.get("date");
                String category = hashMap.get("category");
                String description = hashMap.get("description");
                String amount = hashMap.get("amount");
                String id = hashMap.get("id");

                tvCategory.setText(category);
                tvAmount.setText(amount);
                tvDescription.setText(description);
                tvDate.setText(date);

                editButton.setOnClickListener(v -> {
                    Intent intent = new Intent(getContext(), EditActivity.class);
                    intent.putExtra("id", id);
                    intent.putExtra("type", type);
                    intent.putExtra("amount", amount);
                    intent.putExtra("description", description);
                    intent.putExtra("category", category);
                    startActivityForResult(intent, EDIT_REQUEST_CODE);
                });

                deleteButton.setOnClickListener(v -> {
                    databaseHelper.delete(type, id);
                    fetchRecentTransactions();
                });
            }
            return convertView;
        }
    }





    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == EDIT_REQUEST_CODE && resultCode == RESULT_OK) {
            // Data has been updated, refresh the list
            fetchRecentTransactions();
            ((BaseAdapter) binding.recentListView.getAdapter()).notifyDataSetChanged();
        }
    }





}
package com.mostafiz.expensetracker;

import static java.security.AccessController.getContext;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.ads.nativetemplates.NativeTemplateStyle;
import com.google.android.ads.nativetemplates.TemplateView;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.nativead.NativeAd;
import com.mostafiz.expensetracker.databinding.ActivityShowBinding;

import java.util.ArrayList;
import java.util.HashMap;

public class ShowActivity extends AppCompatActivity {
    ActivityShowBinding binding;
    String showcategory = HomeFragment.showcategory;
    String table = HomeFragment.selected;
    DatabaseHelper databaseHelper;
    ArrayList<HashMap<String, String>> arrayList;
    HashMap<String, String> hashMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityShowBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        if (table.equals("expense")) {
            binding.showcategory.setText(showcategory + " Expense");
        } else {
            binding.showcategory.setText(showcategory);
        }

        databaseHelper = new DatabaseHelper(ShowActivity.this);

        Cursor cursor = databaseHelper.showcategorywisealldata(table, showcategory);
        if (cursor != null && cursor.getCount() > 0) {
            arrayList = new ArrayList<>();
            while (cursor.moveToNext()) {
                int id = cursor.getInt(0);
                double amount = cursor.getDouble(1);
                String description = cursor.getString(2);
                String date = cursor.getString(3);

                hashMap = new HashMap<>();
                hashMap.put("id", String.valueOf(id));
                hashMap.put("amount", String.valueOf(amount));
                hashMap.put("description", description);
                hashMap.put("date", date);
                arrayList.add(hashMap);
            }
            binding.showlistview.setAdapter(new MyAdapter());
        } else {
            binding.errormsgshow.setVisibility(View.VISIBLE);
            binding.errormsgshow.setText("No Data Found");
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
                if (NetworkUtils.isNetworkAvailable(ShowActivity.this)) {
                    TemplateView templateView = convertView.findViewById(R.id.my_template);
                    AdLoader adLoader = new AdLoader.Builder(ShowActivity.this, "ca-app-pub-3940256099942544/2247696110")
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
                    convertView = inflater.inflate(R.layout.showlistviewdesign, parent, false);
                }

                TextView listamount = convertView.findViewById(R.id.listview_amount);
                TextView listdescription = convertView.findViewById(R.id.listview_description);
                TextView listdate = convertView.findViewById(R.id.listviewdate);

                int actualPosition = position - (position / 4); // Adjust position for ads
                hashMap = arrayList.get(actualPosition);

                String id = hashMap.get("id");
                String amount = hashMap.get("amount");
                String date = hashMap.get("date");
                String description = hashMap.get("description");

                listamount.setText(amount);
                listdate.setText(date);
                listdescription.setText(description);
            }
            return convertView;
        }
    }
}

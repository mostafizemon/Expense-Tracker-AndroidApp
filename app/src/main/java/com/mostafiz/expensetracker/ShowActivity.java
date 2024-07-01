package com.mostafiz.expensetracker;

import static android.text.TextUtils.split;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.mostafiz.expensetracker.databinding.ActivityShowBinding;

import java.util.ArrayList;
import java.util.HashMap;

public class ShowActivity extends AppCompatActivity {
    ActivityShowBinding binding;
    String showcategory=HomeFragment.showcategory;
    String table=HomeFragment.selected;
    DatabaseHelper databaseHelper;
    ArrayList<HashMap<String,String>> arrayList;
    HashMap<String,String> hashMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        binding=ActivityShowBinding.inflate(getLayoutInflater());
        View view=binding.getRoot();
        setContentView(view);
        binding.showcategory.setText(showcategory);

        databaseHelper=new DatabaseHelper(ShowActivity.this);

        Cursor cursor=databaseHelper.showcategorywisealldata(table,showcategory);
        if (cursor!=null && cursor.getCount()>0){
            arrayList=new ArrayList<>();
            while (cursor.moveToNext()){
                int id=cursor.getInt(0);
                double amount=cursor.getDouble(1);
                String description=cursor.getString(2);
                String date=cursor.getString(3);

                hashMap=new HashMap<>();
                hashMap.put("id", String.valueOf(id));
                hashMap.put("amount", String.valueOf(amount));
                hashMap.put("description",description);
                hashMap.put("date",date);
                arrayList.add(hashMap);

            }
            binding.showlistview.setAdapter(new MyAdapter());
        }
        else {
            binding.errormsgshow.setVisibility(View.VISIBLE);
            binding.errormsgshow.setText("No Data Found");
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

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            LayoutInflater inflater=getLayoutInflater();
            View myView=inflater.inflate(R.layout.showlistviewdesign,viewGroup,false);

            TextView listamount=myView.findViewById(R.id.listview_amount);
            TextView listdescription=myView.findViewById(R.id.listview_description);
            TextView listdate=myView.findViewById(R.id.listviewdate);

            hashMap=arrayList.get(i);
            String id=hashMap.get("id");
            String amount=hashMap.get("amount");
            String date=hashMap.get("date");
            String description=hashMap.get("description");

            listamount.setText(amount);
            listdate.setText(date);
            listdescription.setText(description);









            return myView;
        }
    }


}
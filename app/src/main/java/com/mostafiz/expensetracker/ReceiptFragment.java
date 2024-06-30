package com.mostafiz.expensetracker;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.mostafiz.expensetracker.databinding.FragmentReceiptBinding;

import java.util.ArrayList;

public class ReceiptFragment extends Fragment {

    FragmentReceiptBinding binding;
    DatabaseHelper dbHelper;
    ArrayList<Receipt> receiptList;
    ReceiptAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentReceiptBinding.inflate(getLayoutInflater(), container, false);
        View view = binding.getRoot();

        dbHelper = new DatabaseHelper(getContext());
        receiptList = new ArrayList<>();
        Cursor cursor = dbHelper.getDailyExpensesAndIncomes();
        if (cursor != null) {
            while (cursor.moveToNext()) {
                @SuppressLint("Range") String date = cursor.getString(cursor.getColumnIndex("date"));
                @SuppressLint("Range") double expense = cursor.getDouble(cursor.getColumnIndex("expense"));
                @SuppressLint("Range") double income = cursor.getDouble(cursor.getColumnIndex("income"));

                receiptList.add(new Receipt(date, income, expense));
            }
            cursor.close();
        }

        adapter = new ReceiptAdapter(getContext(), receiptList);
        binding.receiptlistview.setAdapter(adapter);

        return view;
    }

    private class Receipt {
        String date;
        double income;
        double expense;

        Receipt(String date, double income, double expense) {
            this.date = date;
            this.income = income;
            this.expense = expense;
        }

        @Override
        public String toString() {
            return date + "    " + String.format("%.2f", income) + "    " + String.format("%.2f", expense);
        }
    }

    private class ReceiptAdapter extends ArrayAdapter<Receipt> {
        Context context;
        ArrayList<Receipt> receipts;

        ReceiptAdapter(Context context, ArrayList<Receipt> receipts) {
            super(context, 0, receipts);
            this.context = context;
            this.receipts = receipts;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.list_item_receipt, parent, false);
            }

            Receipt receipt = getItem(position);

            TextView tvDate = convertView.findViewById(R.id.tvDate);
            TextView tvIncome = convertView.findViewById(R.id.tvIncome);
            TextView tvExpense = convertView.findViewById(R.id.tvExpense);

            if (receipt != null) {
                tvDate.setText(receipt.date);
                tvIncome.setText(String.format("%.2f", receipt.income));
                tvExpense.setText(String.format("%.2f", receipt.expense));
            }

            return convertView;
        }
    }
}

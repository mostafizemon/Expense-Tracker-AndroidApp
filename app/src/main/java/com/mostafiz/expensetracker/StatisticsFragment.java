package com.mostafiz.expensetracker;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.mostafiz.expensetracker.databinding.FragmentStatisticsBinding;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class StatisticsFragment extends Fragment {

    FragmentStatisticsBinding binding;
    DatabaseHelper databaseHelper;
    SQLiteDatabase database;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding=FragmentStatisticsBinding.inflate(getLayoutInflater(), container, false);
        View view=binding.getRoot();


        databaseHelper = new DatabaseHelper(getContext());
        database = databaseHelper.getReadableDatabase();

        setupBarChart();



        return view;
    }

    private void setupBarChart() {
        ArrayList<BarEntry> entries = getBarEntries();

        BarDataSet dataSet = new BarDataSet(entries, "Monthly Data");
        dataSet.setColors(new int[]{Color.RED, Color.GREEN}); // Red for expenses, Green for incomes
        dataSet.setStackLabels(new String[]{"Expenses", "Incomes"});

        BarData barData = new BarData(dataSet);
        binding.barchart.setData(barData);
        binding.barchart.getDescription().setEnabled(false);
        binding.barchart.setFitBars(true);
        // Customize X axis (months)
        List<String> months = getMonths(); // Get months for X axis
        XAxis xAxis = binding.barchart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(months));
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1);
        xAxis.setLabelCount(months.size());

        binding.barchart.invalidate(); // Refresh chart
    }

    private ArrayList<BarEntry> getBarEntries() {
        ArrayList<BarEntry> entries = new ArrayList<>();
        Map<String, float[]> monthlyData = new HashMap<>();

        Cursor expenseCursor = databaseHelper.getexpensebymonth();
        while (expenseCursor.moveToNext()) {
            @SuppressLint("Range") String monthYear = expenseCursor.getString(expenseCursor.getColumnIndex("monthyear"));
            @SuppressLint("Range") float totalAmount = expenseCursor.getFloat(expenseCursor.getColumnIndex("total"));
            if (!monthlyData.containsKey(monthYear)) {
                monthlyData.put(monthYear, new float[]{0f, 0f});
            }
            monthlyData.get(monthYear)[0] = totalAmount; // Expenses
        }
        expenseCursor.close();

        Cursor incomeCursor = databaseHelper.getincomebymonth();
        while (incomeCursor.moveToNext()) {
            @SuppressLint("Range") String monthYear = incomeCursor.getString(incomeCursor.getColumnIndex("monthyear"));
            @SuppressLint("Range") float totalAmount = incomeCursor.getFloat(incomeCursor.getColumnIndex("total"));
            if (!monthlyData.containsKey(monthYear)) {
                monthlyData.put(monthYear, new float[]{0f, 0f});
            }
            monthlyData.get(monthYear)[1] = totalAmount; // Incomes
        }
        incomeCursor.close();

        int i = 0;
        for (Map.Entry<String, float[]> entry : monthlyData.entrySet()) {
            entries.add(new BarEntry(i++, entry.getValue()));
        }

        return entries;
    }

    private List<String> getMonths() {
        List<String> months = new ArrayList<>();
        Cursor cursor = databaseHelper.getReadableDatabase().rawQuery("SELECT DISTINCT monthyear FROM expense UNION SELECT DISTINCT monthyear FROM income ORDER BY monthyear ASC", null);
        while (cursor.moveToNext()) {
            @SuppressLint("Range") String monthYear = cursor.getString(cursor.getColumnIndex("monthyear"));
            months.add(formatMonthYear(monthYear)); // Format the month-year before adding it to the list
        }
        cursor.close();
        return months;
    }

    private String formatMonthYear(String monthYear) {
        // Example format: "2024-01" -> "Jan 2024"
        String[] parts = monthYear.split("-");
        int year = Integer.parseInt(parts[0]);
        int month = Integer.parseInt(parts[1]);
        String monthName = new DateFormatSymbols().getMonths()[month - 1];
        return monthName + " " + year;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Close the database to avoid memory leaks
        if (database != null && database.isOpen()) {
            database.close();
        }
    }
}

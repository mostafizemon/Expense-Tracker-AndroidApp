package com.mostafiz.expensetracker;

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
        ArrayList<BarEntry> expenseEntries = getExpenseEntries();
        ArrayList<BarEntry> incomeEntries = getIncomeEntries();
        ArrayList<String> months = getMonths();

        BarDataSet expenseDataSet = new BarDataSet(expenseEntries, "Expenses");
        expenseDataSet.setColor(Color.RED);
        BarDataSet incomeDataSet = new BarDataSet(incomeEntries, "Incomes");
        incomeDataSet.setColor(Color.GREEN);

        BarData barData = new BarData(expenseDataSet, incomeDataSet);
        float groupSpace = 0.2f;
        float barSpace = 0.05f;
        float barWidth = 0.4f;
        barData.setBarWidth(barWidth); // set the width of each bar
        binding.barchart.setData(barData);

        binding.barchart.groupBars(0, groupSpace, barSpace);
        binding.barchart.getDescription().setEnabled(false);
        binding.barchart.setFitBars(true);

        XAxis xAxis = binding.barchart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(months));
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1);
        xAxis.setLabelCount(months.size());
        xAxis.setCenterAxisLabels(true);

        YAxis leftAxis = binding.barchart.getAxisLeft();
        leftAxis.setAxisMinimum(0f); // Start Y-axis at zero
        leftAxis.setGranularity(1f); // Force labels to 1-unit intervals
        leftAxis.setLabelCount(6, true); // Set number of labels
        //binding.barchart.getAxisRight().setEnabled(false); // Disable right Y-axis

        // Adding some padding to ensure small differences are visible
        barData.setBarWidth(0.45f);
        binding.barchart.getXAxis().setAxisMinimum(0);
        binding.barchart.getXAxis().setAxisMaximum(months.size());

        binding.barchart.invalidate();
    }

    private ArrayList<BarEntry> getExpenseEntries() {
        ArrayList<BarEntry> entries = new ArrayList<>();
        String[] columns = {"monthyear", "SUM(amount) as total"};
        Cursor cursor = databaseHelper.getExpenseSumAmountByMonthYear();
        int i = 0;
        while (cursor.moveToNext()) {
            float totalAmount = cursor.getFloat(1);
            entries.add(new BarEntry(i++, totalAmount));
        }
        cursor.close();
        return entries;
    }

    private ArrayList<BarEntry> getIncomeEntries() {
        ArrayList<BarEntry> entries = new ArrayList<>();
        String[] columns = {"monthyear", "SUM(amount) as total"};
        Cursor cursor = databaseHelper.getIncomeSumAmountByMonthYear();
        int i = 0;
        while (cursor.moveToNext()) {
            float totalAmount = cursor.getFloat(1);
            entries.add(new BarEntry(i++, totalAmount));
        }
        cursor.close();
        return entries;
    }



    private ArrayList<String> getMonths() {
        ArrayList<String> months = new ArrayList<>();
        String[] columns = {"monthyear"};
        Cursor cursor = database.query(true, "expense", columns, null, null, null, null, "monthyear", null);
        while (cursor.moveToNext()) {
            String monthYear = cursor.getString(0);
            // Format monthYear as needed, e.g., "Jan 2024"
            months.add(formatMonthYear(monthYear));
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

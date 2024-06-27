package com.mostafiz.expensetracker;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.provider.CalendarContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.mostafiz.expensetracker.databinding.FragmentAddBinding;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AddFragment extends Fragment {
    FragmentAddBinding binding;
    int day,month,year;
    String date;
    String[] expensecategory;
    String[] incomecategory;
    boolean isExpenseSelected = true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        binding=FragmentAddBinding.inflate(getLayoutInflater(),container,false);
        View view=binding.getRoot();




        expensecategory = getResources().getStringArray(R.array.expense_category);
        incomecategory = getResources().getStringArray(R.array.income_category);

        ArrayAdapter<String> expenseAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, expensecategory);
        expenseAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        ArrayAdapter<String> incomeAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, incomecategory);
        incomeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Set initial adapter (expense by default)
        binding.spinner.setAdapter(expenseAdapter);

        // Handle radio button selection
        binding.segmentedControl.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.expense_button) {
                    isExpenseSelected = true;
                    binding.spinner.setAdapter(expenseAdapter);
                } else if (checkedId == R.id.income_button) {
                    isExpenseSelected = false;
                    binding.spinner.setAdapter(incomeAdapter);
                }
            }
        });



        Calendar calendar=Calendar.getInstance();
        binding.adddate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                day=calendar.get(Calendar.DAY_OF_MONTH);
                month=calendar.get(Calendar.MONTH);
                year=calendar.get(Calendar.YEAR);
                DatePickerDialog datePickerDialog=new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int dayofmonth) {
//                        binding.adddate.setText(SimpleDateFormat.getDateInstance().format(calendar.getTime()));
                        month = month+1;
                        date = dayofmonth+"/"+month+"/"+year;
                        binding.adddate.setText(date);
                    }
                },year,month,day);
                datePickerDialog.show();

            }
        });




        binding.submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int selectedPosition = binding.spinner.getSelectedItemPosition();
                if (selectedPosition == 0) {
                    // Handle the case where "Please select expense category" is selected
                    Toast.makeText(getContext(), "Please select a valid expense category", Toast.LENGTH_SHORT).show();
                } else {
                    // Get the selected item
                    String selectedItem = (String) binding.spinner.getSelectedItem();
                    Toast.makeText(getContext(), selectedItem, Toast.LENGTH_SHORT).show();
                }




            }
        });













        return view;
    }

    private void setExpenseAdapter() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, expensecategory);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinner.setAdapter(adapter);
    }

    // Method to set income category adapter
    private void setIncomeAdapter() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, incomecategory);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinner.setAdapter(adapter);
    }
}
package com.mostafiz.expensetracker;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.provider.CalendarContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.mostafiz.expensetracker.databinding.FragmentAddBinding;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AddFragment extends Fragment{
    FragmentAddBinding binding;
    int day,month,year;
    String date;
    String[] expensecategory;
    String[] incomecategory;
    boolean isExpenseSelected = true;
    DatabaseHelper databaseHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        binding=FragmentAddBinding.inflate(getLayoutInflater(),container,false);
        View view=binding.getRoot();


        databaseHelper=new DatabaseHelper(getContext());
        expensecategory = getResources().getStringArray(R.array.expense_category);
        incomecategory = getResources().getStringArray(R.array.income_category);

        //-----------------------------------------------------------------
        ArrayAdapter<String> expenseAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, expensecategory);
        expenseAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        ArrayAdapter<String> incomeAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, incomecategory);
        incomeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //------------------------------------------------------------------

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



        binding.submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String description=binding.adddescription.getText().toString();
                String samount=binding.addtotal.getText().toString().trim();


                if (samount.isEmpty()) {
                    binding.addtotal.setError("Please enter an amount");
                    return; // Exit the method if amount is empty
                }
                Double amount=Double.parseDouble(samount);

                if (isExpenseSelected){
                    int selectedPosition = binding.spinner.getSelectedItemPosition();
                    if (selectedPosition == 0) {
                        // Handle the case where "Please select expense category" is selected
                        Toast.makeText(getContext(), "Please select a valid category", Toast.LENGTH_SHORT).show();
                    } else {

                            String selectedItem = (String) binding.spinner.getSelectedItem();
                            String currentDateTime = getCurrentDateTime();
                            String currentmonthyear=getcurrentmonthyear();
                            String currentyear=getcurrentyear();
                            String currentdate=getcurrentdate();
                            databaseHelper.addexpense(currentDateTime,selectedItem,description,amount,currentdate,currentmonthyear,currentyear);
                            Toast.makeText(getContext(),"Expense added Successfull",Toast.LENGTH_SHORT).show();
                            binding.addtotal.setText("");
                            binding.spinner.setSelection(0);
                            binding.adddescription.setText("");


                    }
                        // Get the selected item
                }
                else {
                    int selectedPosition = binding.spinner.getSelectedItemPosition();
                    if (selectedPosition == 0) {
                        // Handle the case where "Please select expense category" is selected
                        Toast.makeText(getContext(), "Please select a valid category", Toast.LENGTH_SHORT).show();
                    } else {

                            String selectedItem = (String) binding.spinner.getSelectedItem();
                            String currentDateTime = getCurrentDateTime();
                            String currentmonthyear=getcurrentmonthyear();
                            String currentyear=getcurrentyear();
                            String currentdate=getcurrentdate();
                            databaseHelper.addincome(currentDateTime,selectedItem,description,amount,currentdate,currentmonthyear,currentyear);
                            Toast.makeText(getContext(),"Income added Successfull",Toast.LENGTH_SHORT).show();
                            binding.addtotal.setText("");
                            binding.spinner.setSelection(0);
                            binding.adddescription.setText("");
                    }
                }
            }

        });



        return view;
    }




    private String getCurrentDateTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return sdf.format(new java.util.Date());
    }
    private String getcurrentdate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return sdf.format(new java.util.Date());
    }
    private String getcurrentmonthyear() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM", Locale.getDefault());
        return sdf.format(new java.util.Date());
    }
    private String getcurrentyear() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy", Locale.getDefault());
        return sdf.format(new java.util.Date());
    }




}
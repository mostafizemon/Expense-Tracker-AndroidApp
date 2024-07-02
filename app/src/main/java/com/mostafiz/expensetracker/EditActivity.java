package com.mostafiz.expensetracker;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.mostafiz.expensetracker.databinding.ActivityEditBinding;

public class EditActivity extends AppCompatActivity {
    ActivityEditBinding binding;
    DatabaseHelper databaseHelper;
    private static final int EDIT_REQUEST_CODE = 1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityEditBinding.inflate(getLayoutInflater());
        View view=binding.getRoot();
        setContentView(view);
        databaseHelper = new DatabaseHelper(this);

        Intent intent = getIntent();
        String id = intent.getStringExtra("id");
        String type = intent.getStringExtra("type");
        String amount = intent.getStringExtra("amount");
        String description = intent.getStringExtra("description");
        String category = intent.getStringExtra("category");

        binding.edittotal.setText(amount);
        binding.editdescription.setText(description);
        setupSpinner(binding.editspinner,type, category);

        binding.editcancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onBackPressed();

            }
        });


        binding.updateButton.setOnClickListener(v -> {
            // Update the data in the database
            DatabaseHelper databaseHelper = new DatabaseHelper(this);
            boolean isUpdated = databaseHelper.updateTransaction(id, type, binding.edittotal.getText().toString(), binding.editdescription.getText().toString(), binding.editspinner.getSelectedItem().toString());

            if (isUpdated) {
                Intent resultIntent = new Intent();
                setResult(RESULT_OK, resultIntent);
                finish();
            } else {
                // Handle update failure
                // Optionally, show a message to the user
            }
        });




    }



    private void setupSpinner(Spinner spinner, String type, String category) {
        ArrayAdapter<CharSequence> adapter;
        if ("expense".equals(type)) {
            adapter = ArrayAdapter.createFromResource(this,
                    R.array.expense_category, android.R.layout.simple_spinner_item);
        } else {
            adapter = ArrayAdapter.createFromResource(this,
                    R.array.income_category, android.R.layout.simple_spinner_item);
        }
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        setSpinnerSelection(spinner, category);
    }

    private void setSpinnerSelection(Spinner spinner, String category) {
        ArrayAdapter<CharSequence> adapter = (ArrayAdapter<CharSequence>) spinner.getAdapter();
        if (adapter != null) {
            int position = adapter.getPosition(category);
            if (position >= 0) {
                spinner.setSelection(position);
            } else {
                // Log.e(TAG, "Category not found in adapter: " + category);
            }
        } else {
            // Log.e(TAG, "Spinner adapter is null");
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
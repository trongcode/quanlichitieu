package com.btec.fpt.campus_expense_manager.fragments;

import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.btec.fpt.campus_expense_manager.R;
import com.btec.fpt.campus_expense_manager.database.DatabaseHelper;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AddExpenseFragment extends Fragment {

    private DatabaseHelper dbHelper;
    private EditText amountEditText, descriptionEditText, dateEditText;
    private Button addButton;
    private RadioGroup typeRadioGroup;
    private RadioButton rbExpense, rbIncome;
    private final Calendar calendar = Calendar.getInstance();

    public AddExpenseFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_expense, container, false);

        // Initialize UI elements
        amountEditText = view.findViewById(R.id.amountEditText);
        descriptionEditText = view.findViewById(R.id.descriptionEditText);
        dateEditText = view.findViewById(R.id.dateEditText);
        typeRadioGroup = view.findViewById(R.id.typeRadioGroup);
        rbExpense = view.findViewById(R.id.rbExpense);
        rbIncome = view.findViewById(R.id.rbIncome);
        addButton = view.findViewById(R.id.btnAddExpense);

        // Initialize database helper
        dbHelper = new DatabaseHelper(getContext());

        // Set up date picker
        dateEditText.setOnClickListener(v -> showDatePickerDialog());

        // Set up button click listener
        addButton.setOnClickListener(v -> addTransaction());

        return view;
    }

    private void showDatePickerDialog() {
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), (view, year1, month1, dayOfMonth) -> {
            calendar.set(year1, month1, dayOfMonth);
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy (EEE)", Locale.getDefault());
            dateEditText.setText(dateFormat.format(calendar.getTime()));
        }, year, month, day);
        datePickerDialog.show();
    }

    private void addTransaction() {
        String amount = amountEditText.getText().toString().trim();
        String description = descriptionEditText.getText().toString().trim();
        String date = dateEditText.getText().toString().trim();

        // Determine transaction type (Expense = 1, Income = 0)
        int selectedTypeId = typeRadioGroup.getCheckedRadioButtonId();
        int type ;
        // Validate input
        if (!isValidAmount(amount)) {
            showToastCustom("Error: Invalid amount! Please enter a positive number.");
            return;
        }

        if (description.isEmpty()) {
            showToastCustom("Error: Description cannot be empty!");
            return;
        }

        if (date.isEmpty()) {
            showToastCustom("Error: Please select a date!");
            return;
        }

        // Save the transaction to the database
        double amountValue = Double.parseDouble(amount);
        String email = getCurrentUserEmail();
        dbHelper.insertTransaction(amountValue, description, date, type = (selectedTypeId == R.id.rbExpense) ? 0 : 1 // Make sure
 , email);

        // Show success message
        String successMessage = (type == 0) ? "Expense added successfully!" : "Income added successfully!";
        showToastCustom(successMessage);

        // Navigate back to the home fragment
        loadFragment(new HomeFragment());
    }




    private boolean isValidAmount(String amount) {
        try {
            double value = Double.parseDouble(amount);
            return value > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private void showToastCustom(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    private String getCurrentUserEmail() {
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("UserPrefs", getContext().MODE_PRIVATE);
        return sharedPreferences.getString("email", null);
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}

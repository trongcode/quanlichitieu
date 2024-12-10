package com.btec.fpt.campus_expense_manager.fragments;

import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.btec.fpt.campus_expense_manager.R;
import com.btec.fpt.campus_expense_manager.database.DatabaseHelper;
import com.btec.fpt.campus_expense_manager.models.SharedViewModel;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class AddExpenseFragment extends Fragment {


    private DatabaseHelper dbHelper;
    private EditText amountEditText, descriptionEditText, dateEditText;
    private SharedViewModel sharedViewModel;
    private Button button_date, addTransaction;
    private final Calendar calendar = Calendar.getInstance();
    private View view;
    private ImageButton btn_home_fragment, btn_chart, btn_home_add, btn_categories, btn_info;
    private Spinner categorySpinner;
    private RadioGroup typeRadioGroup;
    private RadioButton rbExpense, rbIncome;

    public AddExpenseFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        view = inflater.inflate(R.layout.fragment_add_expense, container, false);
        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);


        amountEditText = view.findViewById(R.id.amountEditText);
        descriptionEditText = view.findViewById(R.id.descriptionEditText);
        dateEditText = view.findViewById(R.id.dateEditText);
        addTransaction = view.findViewById(R.id.btnAddTransaction);
        btn_home_fragment = view.findViewById(R.id.btn_home_fragment);
        btn_chart = view.findViewById(R.id.btn_chart);
        btn_categories = view.findViewById(R.id.btn_categories);
        btn_home_add = view.findViewById(R.id.btn_home_add);
        btn_info = view.findViewById(R.id.btn_info);
        typeRadioGroup = view.findViewById(R.id.typeRadioGroup);
        rbExpense = view.findViewById(R.id.rbExpense);
        rbIncome = view.findViewById(R.id.rbIncome);
        categorySpinner = view.findViewById(R.id.categorySpinner);

        dbHelper = new DatabaseHelper(getContext());
        loadCategories();


        dateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog();
            }
        });
        addTransaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String amount = amountEditText.getText().toString().trim();
                String description = descriptionEditText.getText().toString().trim();
                String date = dateEditText.getText().toString().trim();
                String category = categorySpinner.getSelectedItem() != null ? categorySpinner.getSelectedItem().toString() : "";

                // Determine transaction type (Expense = 1, Income = 0)
                int selectedTypeId = typeRadioGroup.getCheckedRadioButtonId();
                int type = (selectedTypeId == R.id.rbExpense) ? 0 : 1; // 0 = Income, 1 = Expense

                // Validate input
                if (amount.isEmpty() || !isValidAmount(amount)) {
                    showToastCustom("Error: Invalid amount! Please enter a positive number.");
                    return; // Stop execution if amount is invalid
                }

                if (description.isEmpty()) {
                    showToastCustom("Error: Description cannot be empty!");
                    return;
                }

                if (date.isEmpty()) {
                    showToastCustom("Error: Please select a date!");
                    return;
                }

                // Parse the amount as a valid number
                double amountValue = 0;
                try {
                    amountValue = Double.parseDouble(amount);
                    if (amountValue <= 0) {
                        showToastCustom("Error: Amount must be greater than 0.");
                        return; // Stop execution if the amount is non-positive
                    }
                } catch (NumberFormatException e) {
                    showToastCustom("Error: Invalid amount format! Please enter a valid number.");
                    return;
                }

                String email = getCurrentUserEmail();

                // Assuming you have a method to get the category ID based on category name
                int categoryId = dbHelper.getCategoryIdByName(category, email); // Ensure this method exists in your DB helper

                // Insert the transaction into the database
                boolean isInserted = dbHelper.insertTransaction(amountValue, description, date, type, email, category);

                // Show success message
                if (isInserted) {
                    String successMessage = (type == 0) ? "Income added successfully!" : "Expense added successfully!";
                    showToastCustom(successMessage);
                    new android.os.Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            HomeFragment homeFragment = new HomeFragment();
                            getActivity().getSupportFragmentManager()
                                    .beginTransaction()
                                    .replace(R.id.fragment_container, homeFragment)
                                    .addToBackStack(null)
                                    .commit();
                        }
                    }, 1000);
                } else {
                    showToastCustom("Error: Failed to save transaction.");
                }
            }
        });
        setupButtonListeners();
        return view;
    }

    private boolean isValidAmount(String amount) {
        try {
            double value = Double.parseDouble(amount);
            return value > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    private void setupButtonListeners() {
        btn_home_add.setOnClickListener(view -> loadFragment(new AddExpenseFragment()));
        btn_categories.setOnClickListener(view -> loadFragment(new CategoryFragment()));
        btn_info.setOnClickListener(view -> loadFragment(new InformationFragment()));
        btn_home_fragment.setOnClickListener(view -> loadFragment(new HomeFragment()));
        btn_chart.setOnClickListener(view -> loadFragment(new ChartFragment()));
    }
    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private String getCurrentUserEmail() {
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("UserPrefs", getContext().MODE_PRIVATE);
        return sharedPreferences.getString("email", null);
    }

    private void loadCategories() {
        // Get the current user's email from shared preferences
        String email = getCurrentUserEmail();
        // Fetch categories from the database for the given email
        List<String> categories = dbHelper.getAllCategoryNamesByEmail(email);
        // Handle the case where no categories are returned
        if (categories.isEmpty()) {
            categories.add("No categories available"); // Default message
        }
        // Set up the adapter for the spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, categories);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(adapter);
    }


    private void showDatePickerDialog() {
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(year, month, dayOfMonth);
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy (EEE)", Locale.getDefault());
                dateEditText.setText(dateFormat.format(calendar.getTime()));
            }
        }, year, month, day);
        datePickerDialog.show();
    }

    void showToastCustom(String message) {

        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.custom_toast, view.findViewById(R.id.custom_toast_layout));
// Set the icon
        ImageView icon = layout.findViewById(R.id.toast_icon);
        icon.setImageResource(R.drawable.icon_x);  // Set your desired icon

// Set the text
        TextView text = layout.findViewById(R.id.toast_message);
        text.setText(message);

// Create and show the toast
        Toast toast = new Toast(getContext());
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();

    }
}
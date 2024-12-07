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
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.btec.fpt.campus_expense_manager.R;
import com.btec.fpt.campus_expense_manager.database.DatabaseHelper;
import com.btec.fpt.campus_expense_manager.entities.Category;
import com.btec.fpt.campus_expense_manager.models.SharedViewModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class AddExpenseFragment extends Fragment {

    private DatabaseHelper dbHelper;
    private EditText amountEditText, descriptionEditText, dateEditText;
    /*private Spinner categorySpinner;*/
    private SharedViewModel sharedViewModel;
    private Button addButton,addIncomeButton;
    private final Calendar calendar = Calendar.getInstance();
    private View view;
    private ImageButton btn_home_fragment, btn_chart, btn_categories, btn_info;
    private RadioButton rbExpense, rbIncome;
    private RadioGroup typeRadioGroup;
    public AddExpenseFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_add_expense, container, false);
        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        dbHelper = new DatabaseHelper(getContext());

        // Initialize UI elements
        amountEditText = view.findViewById(R.id.amountEditText);
        descriptionEditText = view.findViewById(R.id.descriptionEditText);
        dateEditText = view.findViewById(R.id.dateEditText);
        /*categorySpinner = view.findViewById(R.id.categorySpinner);*/
        addButton = view.findViewById(R.id.btnAddExpense);
        typeRadioGroup = view.findViewById(R.id.typeRadioGroup);
        rbExpense = view.findViewById(R.id.rbExpense);
        rbIncome = view.findViewById(R.id.rbIncome);

        btn_home_fragment = view.findViewById(R.id.btn_home_fragment);
        btn_chart = view.findViewById(R.id.btn_chart);
        btn_categories = view.findViewById(R.id.btn_categories);
        btn_info = view.findViewById(R.id.btn_info);

        // Set up date picker
        dateEditText.setOnClickListener(v -> showDatePickerDialog());

        // Load categories into spinner
        /*loadCategories();*/

        // Add button click listener
        addButton.setOnClickListener(v -> addExpense());

        // Setup navigation buttons
        setupNavigation();

        return view;
    }

   /* private void loadCategories() {
        // Fetch the email from SharedPreferences
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("UserPrefs", getContext().MODE_PRIVATE);
        String loggedInEmail = sharedPreferences.getString("email", null);

        // Insert default categories if needed
        dbHelper.insertDefaultCategories(loggedInEmail);

        // Fetch categories from the database
        List<Category> categories = dbHelper.getAllCategoryByEmail(loggedInEmail);

        // Extract category names into a List<String>
        List<String> categoryNames = new ArrayList<>();
        for (Category category : categories) {
            categoryNames.add(category.getName()); // Assuming `getName()` returns the category name
        }

        // Set up the spinner with category names
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, categoryNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(adapter);
    }*/

    private void addExpense() {
        String amount = amountEditText.getText().toString().trim();
        String description = descriptionEditText.getText().toString().trim();
        String date = dateEditText.getText().toString().trim();

        // Lấy loại giao dịch từ RadioGroup
        int selectedTypeId = typeRadioGroup.getCheckedRadioButtonId();
        int type = (selectedTypeId == R.id.rbExpense) ? 0 : 1; // 1: Income, 0: Expense

        // Kiểm tra đầu vào hợp lệ
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

        // Lưu giao dịch vào cơ sở dữ liệu
        double amountValue = Double.parseDouble(amount);
        String email = getCurrentUserEmail();
        dbHelper.addTransaction(amountValue, description, date, type, email);

        // Hiển thị thông báo thành công
        String successMessage = (type == 1) ? "Expense added successfully!" : "Income added successfully!";
        showToastCustom(successMessage);

        // Quay lại trang chính
        loadFragment(new HomeFragment());
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

    private boolean isValidAmount(String amount) {
        try {
            double value = Double.parseDouble(amount);
            return value > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private void setupNavigation() {
        btn_home_fragment.setOnClickListener(v -> loadFragment(new HomeFragment()));
        btn_chart.setOnClickListener(v -> loadFragment(new ChartFragment()));
        btn_categories.setOnClickListener(v -> loadFragment(new CategoryFragment()));
        btn_info.setOnClickListener(v -> loadFragment(new InformationFragment()));
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void showToastCustom(String message) {
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.custom_toast, view.findViewById(R.id.custom_toast_layout));

        // Set the icon
        ImageView icon = layout.findViewById(R.id.toast_icon);
        icon.setImageResource(R.drawable.icon_x);

        // Set the text
        TextView text = layout.findViewById(R.id.toast_message);
        text.setText(message);

        // Create and show the toast
        Toast toast = new Toast(getContext());
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();
    }

    private String getCurrentUserEmail() {
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("UserPrefs", getContext().MODE_PRIVATE);
        return sharedPreferences.getString("email", null);
    }
}

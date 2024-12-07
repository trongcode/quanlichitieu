package com.btec.fpt.campus_expense_manager.fragments;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
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
import java.util.Locale;

public class AddExpenseFragment extends Fragment {


    private DatabaseHelper dbHelper;
    private EditText amountEditText, descriptionEditText, dateEditText;
    private SharedViewModel sharedViewModel;
    private Button button_date, addButton, btnIncome;
    private final Calendar calendar = Calendar.getInstance();
    private View view;
    private ImageButton btn_home_fragment, btn_chart, btn_home_add, btn_categories, btn_info;


    public AddExpenseFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        view = inflater.inflate(R.layout.fragment_add_expense, container, false);
        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        dbHelper = new DatabaseHelper(getContext());

        amountEditText = view.findViewById(R.id.amountEditText);
        descriptionEditText = view.findViewById(R.id.descriptionEditText);
        dateEditText = view.findViewById(R.id.dateEditText);
        addButton = view.findViewById(R.id.btnAddExpense);
        btn_home_fragment = view.findViewById(R.id.btn_home_fragment);
        btn_chart = view.findViewById(R.id.btn_chart);
        btn_categories = view.findViewById(R.id.btn_categories);
        btn_home_add = view.findViewById(R.id.btn_home_add);
        btn_info = view.findViewById(R.id.btn_info);
        btnIncome = view.findViewById(R.id.btnIncome);




        dateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog();
            }
        });

        btn_home_fragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadFragment(new HomeFragment());
            }

            private void loadFragment(HomeFragment homeFragment) {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, homeFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
        btn_chart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadFragment(new ChartFragment());
            }

            private void loadFragment(ChartFragment chartFragment) {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, chartFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
        btn_categories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadFragment(new CategoryFragment());
            }

            private void loadFragment(CategoryFragment categoryFragment) {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, categoryFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
        btn_home_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadFragment(new AddExpenseFragment());
            }

            private void loadFragment(AddExpenseFragment addExpenseFragment) {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, addExpenseFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
        btn_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadFragment(new InformationFragment());
            }

            private void loadFragment(InformationFragment informationFragment) {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, informationFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
        btnIncome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String description = descriptionEditText.getText().toString();
                String amount = amountEditText.getText().toString();
                String date = dateEditText.getText().toString();

                if (!description.isEmpty() && !amount.isEmpty() && !date.isEmpty()) {
                    // Đóng gói dữ liệu vào Bundle
                    Bundle bundle = new Bundle();
                    bundle.putString("description", description);
                    bundle.putString("amount", amount);
                    bundle.putString("date", date);

                    IncomeFragment fragment = new IncomeFragment();
                    fragment.setArguments(bundle);

                    FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                    transaction.replace(R.id.fragment_container, fragment); // container chứa các Fragment
                    transaction.addToBackStack(null); // Thêm vào back stack
                    transaction.commit();
                } else {
                    showToastCustom("Please fill in all fields");
                }
            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String amount = amountEditText.getText().toString().trim();
                String description = descriptionEditText.getText().toString().trim();
                String date = dateEditText.getText().toString().trim();
                Boolean isAmountValid = !amount.isEmpty();

                // Kiểm tra điều kiện
                if (isAmountValid && !description.isEmpty() && !date.isEmpty()) {
                    String expense = "Amount: " + amount + " -- " + "Description: " + description + " -- "+"Date: " + date;


                    sharedViewModel.addExpense(expense);
                    showToastCustom("Expense add succesfully!");

                    FragmentManager fragmentManager = getParentFragmentManager();
                    FragmentTransaction transaction = fragmentManager.beginTransaction();
                    transaction.replace(R.id.fragment_container, new ExpenseFragment());
                    transaction.commit();
                } else {
                    showToastCustom("Error: Please enter information!");
                }
            }
        });
        return view;
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
    void showToastCustom(String message){

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
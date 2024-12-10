package com.btec.fpt.campus_expense_manager.fragments;


import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.btec.fpt.campus_expense_manager.DataStatic;
import com.btec.fpt.campus_expense_manager.R;
import com.btec.fpt.campus_expense_manager.database.DatabaseHelper;
import com.btec.fpt.campus_expense_manager.entities.Expense;
import com.btec.fpt.campus_expense_manager.entities.Transaction;

import java.util.ArrayList;
import java.util.List;

public class ExpenseFragment extends Fragment {
    private ImageButton btn_back;
    private View view;
    private ListView incomeListView;
    private Spinner categorySpinnerE;
    private DatabaseHelper dbHelper;
    private ArrayAdapter<String> adapter;
    private List<Transaction> transactionList;
    private ImageButton btnBack, btn_home_fragment, btn_chart, btn_home_add, btn_categories, btn_info;

    public ExpenseFragment() {
        // Required empty public constructor
    }

    @SuppressLint("WrongViewCast")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_expense, container, false);

        incomeListView = view.findViewById(R.id.expenseListView);
        categorySpinnerE = view.findViewById(R.id.categorySpinnerE);
        btn_home_fragment = view.findViewById(R.id.btn_home_fragment);
        btn_chart = view.findViewById(R.id.btn_chart);
        btn_categories = view.findViewById(R.id.btn_categories);
        btn_home_add = view.findViewById(R.id.btn_home_add);
        btn_info = view.findViewById(R.id.btn_info);
        btnBack = view.findViewById(R.id.btnBack);
        dbHelper = new DatabaseHelper(getContext());
        adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, new ArrayList<>());
        incomeListView.setAdapter(adapter);

        loadTransactionsByEmail(getCurrentUserEmail());
        loadCategories(getCurrentUserEmail());

        categorySpinnerE.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedCategory = (String) parent.getItemAtPosition(position);
                filterTransactionsByCategory(selectedCategory);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Optionally handle the case when no category is selected
                loadTransactionsByEmail(getCurrentUserEmail()); // Load all transactions if nothing is selected
            }
        });

        btn_back = view.findViewById(R.id.btnBack);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requireActivity().onBackPressed();
            }
        });

        setupButtonListeners();
        return view;
    }

    private String getCurrentUserEmail() {
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("UserPrefs", getContext().MODE_PRIVATE);
        return sharedPreferences.getString("email", null);
    }

    private void loadTransactionsByEmail(String email) {
        // Fetch all income transactions by email
        transactionList = dbHelper.getAllTransactionsByType(email, 0); // Assuming 1 is for income
        adapter.clear();
        for (Transaction transaction : transactionList) {
            adapter.add(transaction.toString()); // Assuming you have a toString method in Transaction for display
        }
        adapter.notifyDataSetChanged();
        // Animate each item in the ListView after loading
        for (int i = 0; i < transactionList.size(); i++) {
            final int position = i; // Final variable for use in the lambda
            incomeListView.postDelayed(() -> animateItemAtPosition(position), i * 100L); // Staggered animation
        }
    }

    private void loadCategories(String email) {
        // Fetch all categories from the database
        List<String> categories = dbHelper.getAllCategoryNamesByEmail(email);
        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, categories);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinnerE.setAdapter(categoryAdapter);
    }

    private void filterTransactionsByCategory(String category) {
        if (category == null || category.isEmpty()) {
            // If no category is selected, load all transactions
            loadTransactionsByEmail(getCurrentUserEmail());
            return;
        }
        // Filter transactions based on the selected category
        List<Transaction> filteredList = new ArrayList<>();
        for (Transaction transaction : transactionList) {
            if (transaction.getCategory().equals(category)) { // Assuming Transaction has a getCategory method
                filteredList.add(transaction);
            }
        }
        // Update the adapter with filtered transactions
        adapter.clear();
        for (Transaction transaction : filteredList) {
            adapter.add(transaction.toString());
        }
        adapter.notifyDataSetChanged();
    }

    private void animateItemAtPosition(int position) {
        // Get the view for the item at the specified position
        View itemView = incomeListView.getChildAt(position);
        if (itemView != null) {
            // Create an animation for the added item
            ObjectAnimator animator = ObjectAnimator.ofFloat(itemView, "translationY", 100f, 0f);
            animator.setDuration(300); // Duration of the animation
            animator.start();
        }
    }
    private void setupButtonListeners() {
        btn_home_add.setOnClickListener(view -> loadFragment(new AddExpenseFragment()));
        btn_categories.setOnClickListener(view -> loadFragment(new CategoryFragment()));
        btn_info.setOnClickListener(view -> loadFragment(new InformationFragment()));
        btn_home_fragment.setOnClickListener(view -> loadFragment(new HomeFragment()));
        btn_chart.setOnClickListener(view -> loadFragment(new ChartFragment()));
        btn_chart.setOnClickListener(view -> loadFragment(new ChartFragment()));
    }
    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

}

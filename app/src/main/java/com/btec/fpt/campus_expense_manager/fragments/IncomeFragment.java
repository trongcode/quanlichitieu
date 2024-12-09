package com.btec.fpt.campus_expense_manager.fragments;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.AdapterView; // Import for AdapterView
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.btec.fpt.campus_expense_manager.R;
import com.btec.fpt.campus_expense_manager.database.DatabaseHelper;
import com.btec.fpt.campus_expense_manager.entities.Transaction;

import java.util.ArrayList;
import java.util.List;

public class IncomeFragment extends Fragment {
    private View view;
    private ListView incomeListView;
    private Spinner categorySpinnerI; // Spinner for category selection
    private DatabaseHelper dbHelper;
    private ArrayAdapter<String> adapter;
    private List<Transaction> transactionList; // Store all transactions

    public IncomeFragment() {
        // Required empty public constructor
    }

    @SuppressLint("WrongViewCast")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_income, container, false);

        // Initialize views and database helper
        incomeListView = view.findViewById(R.id.incomeListView);
        categorySpinnerI = view.findViewById(R.id.categorySpinnerI); // Initialize the category spinner
        dbHelper = new DatabaseHelper(getContext());

        // Initialize the adapter with an empty list
        adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, new ArrayList<>());
        incomeListView.setAdapter(adapter);

        // Load all income transactions initially
        loadTransactionsByEmail(getCurrentUserEmail());

        // Load categories into the spinner
        loadCategories(getCurrentUserEmail());

        // Set a listener for category selection to filter transactions
        categorySpinnerI.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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

        return view;
    }

    private String getCurrentUserEmail() {
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("UserPrefs", getContext().MODE_PRIVATE);
        return sharedPreferences.getString("email", null);
    }

    private void loadTransactionsByEmail(String email) {
        // Fetch all income transactions by email
        transactionList = dbHelper.getAllTransactionsByType(email, 1); // Assuming 1 is for income
        adapter.clear();
        for (Transaction transaction : transactionList) {
            adapter.add(transaction.toString()); // Assuming you have a toString method in Transaction for display
        }
        adapter.notifyDataSetChanged();

        // Animate each item in the ListView after loading
        for (int i = 0; i < transactionList.size(); i++) {
            final int position = i; // Final variable for use in the lambda
            incomeListView.postDelayed(() -> animateItemAtPosition(position), i * 100); // Staggered animation
        }
    }

    private void loadCategories(String email) {
        // Fetch all categories from the database
        List<String> categories = dbHelper.getAllCategoryNamesByEmail(email);
        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, categories);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinnerI.setAdapter(categoryAdapter);
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
}

package com.btec.fpt.campus_expense_manager.fragments;

import static android.content.Context.MODE_PRIVATE;

import android.animation.ObjectAnimator;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.btec.fpt.campus_expense_manager.R;
import com.btec.fpt.campus_expense_manager.database.DatabaseHelper;
import com.btec.fpt.campus_expense_manager.models.BalanceInfor;
import com.btec.fpt.campus_expense_manager.models.SharedViewModel;
import com.btec.fpt.campus_expense_manager.entities.Transaction;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    private DatabaseHelper dbHelper;
    private SharedViewModel sharedViewModel;
    private ArrayAdapter<String> adapter;
    private TextView tvFullName, tvBalance, tvNames, tvHello;
    private ListView expensesListView;
    private Button btnIncome, btnExpense;
    private ImageButton btn_home_fragment, btn_chart, btn_home_add, btn_categories, btn_info, btn_change;

    private View view;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);

        // Initialize components
        initializeComponents();

        // Load user balance and transactions
        String emails = getCurrentUserEmail();
        if (emails != null) {
            loadUserBalance(emails);
            loadTransactionsByEmail(emails);
        } else {
            showToastCustom("No email found for user.");
        }

        // Set up button listeners
        setupButtonListeners();
        btn_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadFragment(new ChangePassWordFragment());
            }
        });

        return view;
    }

    private void initializeComponents() {
        dbHelper = new DatabaseHelper(getContext());
        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        expensesListView = view.findViewById(R.id.expensesListView);
        tvFullName = view.findViewById(R.id.tvFullname);
        tvBalance = view.findViewById(R.id.tvBalance);
        tvHello = view.findViewById(R.id.tv_name);
        btnIncome = view.findViewById(R.id.btnIncome);
        btnExpense = view.findViewById(R.id.btnExpense);
        btn_home_fragment = view.findViewById(R.id.btn_home_fragment);
        btn_chart = view.findViewById(R.id.btn_chart);
        btn_categories = view.findViewById(R.id.btn_categories);
        btn_home_add = view.findViewById(R.id.btn_home_add);
        btn_info = view.findViewById(R.id.btn_info);
        btn_change = view.findViewById(R.id.btn_change_pass);

        // Initialize the adapter with an empty list
        adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, new ArrayList<>());
        expensesListView.setAdapter(adapter);
    }

    private String getCurrentUserEmail() {
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String emails = sharedPreferences.getString("email", null);
        Log.d("SharedPreferences", "Current user email: " + emails);  // Debugging line
        return emails;
    }


    private void loadUserBalance(String email) {
        BalanceInfor balanceInfor = dbHelper.getBalanceFromEmail(email);
        if (balanceInfor != null) {
            tvFullName.setText(String.format("%s %s", balanceInfor.getLastName(), balanceInfor.getFirstName()));
            DecimalFormat formatter = new DecimalFormat("#,###");
            String formattedBalance = formatter.format(balanceInfor.getBalance());
            tvBalance.setText(String.format("%s VNĐ", formattedBalance));
            tvHello.setText(String.format("Hello %s", balanceInfor.getFirstName()));
        }
    }

    private void loadTransactionsByEmail(String email) {
        // Fetch all transactions by email
        List<Transaction> transactions = dbHelper.getAllTransactionsByEmail(email);
        // Clear the adapter and add the fetched transactions
        adapter.clear();
        for (Transaction transaction : transactions) {
            adapter.add(transaction.toString()); // Assuming you have a toString method in Transaction for display
        }
        adapter.notifyDataSetChanged();
        // Animate each item in the ListView after loading
        for (int i = 0; i < transactions.size(); i++) {
            final int position = i; // Final variable for use in the lambda
            expensesListView.postDelayed(() -> animateItemAtPosition(position), i * 100); // Staggered animation
        }
    }

    private void animateItemAtPosition(int position) {
        // Get the view for the item at the specified position
        View itemView = expensesListView.getChildAt(position);
        if (itemView != null) {
            // Create an animation for the added item
            ObjectAnimator animator = ObjectAnimator.ofFloat(itemView, "translationY", 100f, 0f);
            animator.setDuration(300); // Duration of the animation
            animator.start();
        }
    }

    private void setupButtonListeners() {
        btnExpense.setOnClickListener(view -> loadFragment(new ExpenseFragment()));
        btn_home_add.setOnClickListener(view -> loadFragment(new AddExpenseFragment()));
        btn_categories.setOnClickListener(view -> loadFragment(new CategoryFragment()));
        btnIncome.setOnClickListener(view -> loadFragment(new IncomeFragment()));
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

    private void showToastCustom(String message) {
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.custom_toast, view.findViewById(R.id.custom_toast_layout));

        ImageView icon = layout.findViewById(R.id.toast_icon);
        icon.setImageResource(R.drawable.user);  // Set your desired icon

        TextView text = layout.findViewById(R.id.toast_message);
        text.setText(message);

        Toast toast = new Toast(getContext());
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();
    }
}
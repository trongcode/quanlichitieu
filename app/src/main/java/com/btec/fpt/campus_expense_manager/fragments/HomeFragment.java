package com.btec.fpt.campus_expense_manager.fragments;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Bundle;
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

import java.util.ArrayList;

public class HomeFragment extends Fragment {
    private DatabaseHelper dbHelper;
    private SharedViewModel sharedViewModel;
    private ArrayAdapter<String> adapter;
    private TextView tvFullName, tvBalance, tvNames, tvHello;
    private ListView expensesListView;
    private Button btnIncome, btnExpense;
    private ImageButton btn_home_fragment, btn_chart, btn_home_add, btn_categories, btn_info, btnMenu;

    private View view;

    public HomeFragment() {

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);

        dbHelper = new DatabaseHelper(getContext());



        tvFullName = view.findViewById(R.id.tvFullname);
        tvBalance = view.findViewById(R.id.tvBalance);
        tvNames = view.findViewById(R.id.tv_names);
        tvHello = view.findViewById(R.id.tv_name);
        btnIncome = view.findViewById(R.id.btnIncome);
        btnMenu = view.findViewById(R.id.btnMenu);
        btn_home_fragment = view.findViewById(R.id.btn_home_fragment);
        btn_chart = view.findViewById(R.id.btn_chart);
        btn_categories = view.findViewById(R.id.btn_categories);
        btn_home_add = view.findViewById(R.id.btn_home_add);
        btn_info = view.findViewById(R.id.btn_info);
        btnExpense = view.findViewById(R.id.btnExpense);


        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String email = sharedPreferences.getString("email", null);
        String password = sharedPreferences.getString("password", null);
        BalanceInfor balanceInfor = dbHelper.getBalanceFromEmail(email);
        tvFullName.setText(String.format("%s %s", balanceInfor.getLastName(), balanceInfor.getFirstName()));
        tvBalance.setText(String.format("%s", balanceInfor.getBalance()));
        tvNames.setText(String.format("%s %s", balanceInfor.getLastName(), balanceInfor.getFirstName()));
        tvHello.setText(String.format("Hello %s", balanceInfor.getFirstName()));


        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        adapter = new ArrayAdapter<>(requireContext(), android.R.layout.list_content, new ArrayList<>());
        expensesListView = view.findViewById(R.id.expensesListView);
        expensesListView.setAdapter(adapter);
        sharedViewModel.getExpenses().observe(getViewLifecycleOwner(), expenses -> {
            adapter.clear();
            adapter.addAll(expenses);
            adapter.notifyDataSetChanged();
        });

        btnExpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadFragment(new ExpenseFragment());
            }
        });
        btn_home_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadFragment(new AddExpenseFragment());
            }
        });
        btn_categories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadFragment(new CategoryFragment());
            }
        });
        btnIncome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadFragment(new IncomeFragment());
            }
        });
        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showToastCustom("You pressed the menu button!!!");
            }
        });
        btn_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadFragment(new InformationFragment());
            }
        });
        btn_home_fragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadFragment(new HomeFragment());
            }
        });
        btn_chart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadFragment(new ChartFragment());
            }
        });
        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadFragment(new ChangePassWordFragment());
            }

            private void loadFragment(ChangePassWordFragment menuFragment) {
                FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, menuFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
        return view;
    }

    private void loadFragment(InformationFragment informationFragment) {
        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, informationFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    void showToastCustom(String message) {

        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.custom_toast, view.findViewById(R.id.custom_toast_layout));

        ImageView icon = layout.findViewById(R.id.toast_icon);
        icon.setImageResource(R.drawable.icon_x);  // Set your desired icon

        TextView text = layout.findViewById(R.id.toast_message);
        text.setText(message);

        Toast toast = new Toast(getContext());
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();
    }


    private void loadFragment(ChartFragment chartFragment) {
        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, chartFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void loadFragment(CategoryFragment categoryFragment) {
        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, categoryFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void loadFragment(HomeFragment homeFragment) {
        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, homeFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void loadFragment(IncomeFragment incomeFragment) {
        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, incomeFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void loadFragment(AddExpenseFragment addExpenseFragment) {
        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, addExpenseFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void loadFragment(ExpenseFragment expenseFragment) {
        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, expenseFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}

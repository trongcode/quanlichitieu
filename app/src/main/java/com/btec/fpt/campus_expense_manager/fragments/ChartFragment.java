package com.btec.fpt.campus_expense_manager.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.btec.fpt.campus_expense_manager.R;

public class ChartFragment extends Fragment {
    private View view;
    private ImageButton btn_home_fragment, btn_chart, btn_home_add, btn_categories, btnBack, btn_info;
    public ChartFragment(){

    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_chart, container, false);

        btn_home_fragment = view.findViewById(R.id.btn_home_fragment);
        btn_chart = view.findViewById(R.id.btn_chart);
        btn_categories = view.findViewById(R.id.btn_categories);
        btn_home_add = view.findViewById(R.id.btn_home_add);
        btn_info = view.findViewById(R.id.btn_info);
        btnBack = view.findViewById(R.id.btnBack);


        setupButtonListeners();
        return view;
    }

    private void setupButtonListeners() {
        btn_home_add.setOnClickListener(view -> loadFragment(new AddExpenseFragment()));
        btn_categories.setOnClickListener(view -> loadFragment(new CategoryFragment()));
        btn_info.setOnClickListener(view -> loadFragment(new InformationFragment()));
        btn_home_fragment.setOnClickListener(view -> loadFragment(new HomeFragment()));
        btn_chart.setOnClickListener(view -> loadFragment(new ChartFragment()));
        btn_chart.setOnClickListener(view -> loadFragment(new ChartFragment()));
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requireActivity().onBackPressed();
            }
        });
    }
    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}

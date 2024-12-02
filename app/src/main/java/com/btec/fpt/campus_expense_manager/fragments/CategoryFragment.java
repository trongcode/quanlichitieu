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

public class CategoryFragment extends Fragment {
    private View view;
    private ImageButton btn_home_fragment, btn_chart, btn_home_add, btn_categories, btn_info;
    public CategoryFragment() {

    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_information, container, false);
        btn_home_fragment = view.findViewById(R.id.btn_home_fragment);
        btn_chart = view.findViewById(R.id.btn_chart);
        btn_categories = view.findViewById(R.id.btn_categories);
        btn_home_add = view.findViewById(R.id.btn_home_add);
        btn_info = view.findViewById(R.id.btn_info);

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

        return null;
    }
}

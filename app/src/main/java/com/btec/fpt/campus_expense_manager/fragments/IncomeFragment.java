package com.btec.fpt.campus_expense_manager.fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.btec.fpt.campus_expense_manager.R;
import com.btec.fpt.campus_expense_manager.adapters.IncomeAdapter;
import com.btec.fpt.campus_expense_manager.database.DatabaseHelper;

public class IncomeFragment extends Fragment {

    private TextView descriptionTextView, amountTextView, dateTextView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate layout cho Fragment này
        View view = inflater.inflate(R.layout.fragment_income, container, false);

        // Kết nối với các thành phần giao diện
        descriptionTextView = view.findViewById(R.id.descriptionTextView);
        amountTextView = view.findViewById(R.id.amountTextView);
        dateTextView = view.findViewById(R.id.dateTextView);

        // Lấy dữ liệu từ Bundle
        Bundle bundle = getArguments();
        if (bundle != null) {
            String description = bundle.getString("description");
            String amount = bundle.getString("amount");
            String date = bundle.getString("date");

            // Hiển thị dữ liệu lên TextView
            descriptionTextView.setText(description);
            amountTextView.setText(amount);
            dateTextView.setText(date);
        }

        return view;
    }
}

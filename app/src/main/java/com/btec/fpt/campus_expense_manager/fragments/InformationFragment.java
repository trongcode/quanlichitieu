package com.btec.fpt.campus_expense_manager.fragments;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.btec.fpt.campus_expense_manager.R;
import com.btec.fpt.campus_expense_manager.database.DatabaseHelper;
import com.btec.fpt.campus_expense_manager.models.BalanceInfor;

public class InformationFragment extends Fragment {
    private View view;
    private TextView tvFullname, edt_fullname, edt_sex, edt_email;
    private DatabaseHelper dbHelper;

    public InformationFragment() {

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_information, container, false);

        dbHelper = new DatabaseHelper(getContext());

        edt_fullname = view.findViewById(R.id.edt_fullname);
        edt_email = view.findViewById(R.id.edt_email);

        tvFullname = view.findViewById(R.id.tvFullname);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String email = sharedPreferences.getString("email", null);
        BalanceInfor balanceInfor = dbHelper.getBalanceFromEmail(email);
        tvFullname.setText(String.format("%s %s", balanceInfor.getLastName(), balanceInfor.getFirstName()));
        edt_fullname.setText(String.format("%s %s", balanceInfor.getLastName(), balanceInfor.getFirstName()));
        edt_email.setText(String.format("%s %s", balanceInfor.getFirstName() + "@gmail.com"));

        return view;
    }
}

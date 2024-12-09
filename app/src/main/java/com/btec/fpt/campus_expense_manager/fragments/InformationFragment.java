package com.btec.fpt.campus_expense_manager.fragments;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.btec.fpt.campus_expense_manager.R;
import com.btec.fpt.campus_expense_manager.database.DatabaseHelper;
import com.btec.fpt.campus_expense_manager.models.BalanceInfor;

public class InformationFragment extends Fragment {
    private View view;
    private TextView tvFullname, edt_fullname, edt_lastname, edt_firstname, edt_email;
    private ImageButton btn_home_fragment, btn_chart, btn_home_add, btn_categories, btn_info;
    private DatabaseHelper dbHelper;

    public InformationFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_information, container, false);

        dbHelper = new DatabaseHelper(getContext());

        // Initialize views
        edt_fullname = view.findViewById(R.id.edt_fullname);
        edt_email = view.findViewById(R.id.edt_email);
        tvFullname = view.findViewById(R.id.tvFullname);
        edt_firstname = view.findViewById(R.id.edt_firstname);
        edt_lastname = view.findViewById(R.id.edt_lastname);
        Button btn_logout = view.findViewById(R.id.btnLogout);
        btn_home_fragment = view.findViewById(R.id.btn_home_fragment);
        btn_chart = view.findViewById(R.id.btn_chart);
        btn_categories = view.findViewById(R.id.btn_categories);
        btn_home_add = view.findViewById(R.id.btn_home_add);
        btn_info = view.findViewById(R.id.btn_info);

        // Load user information from SharedPreferences
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String email = sharedPreferences.getString("email", null);
        BalanceInfor balanceInfor = dbHelper.getBalanceFromEmail(email);

        // Display user information
        if (balanceInfor != null) {
            tvFullname.setText(String.format("%s %s", balanceInfor.getLastName(), balanceInfor.getFirstName()));
            edt_fullname.setText(String.format("%s %s", balanceInfor.getLastName(), balanceInfor.getFirstName()));
            edt_email.setText(balanceInfor.getEmail());
            edt_firstname.setText(balanceInfor.getFirstName());
            edt_lastname.setText(balanceInfor.getLastName());
        }

        // Set up button click listeners
        btn_home_fragment.setOnClickListener(view -> loadFragment(new HomeFragment()));
        btn_chart.setOnClickListener(view -> loadFragment(new ChartFragment()));
        btn_categories.setOnClickListener(view -> loadFragment(new CategoryFragment()));
        btn_home_add.setOnClickListener(view -> loadFragment(new AddExpenseFragment()));
        btn_info.setOnClickListener(view -> loadFragment(new InformationFragment()));

        btn_logout.setOnClickListener(view -> {
            // Clear user information from SharedPreferences
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear();
            editor.apply();
            // Show logout success message
            showToastCustom("Logged out successfully");
            // Navigate to LoginFragment
            loadFragment(new LoginFragment());
        });

        return view;
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

        ImageView icon = layout.findViewById(R.id.toast_icon);
        icon.setImageResource(R.drawable.icon_x); // Adjust as needed

        TextView text = layout.findViewById(R.id.toast_message);
        text.setText(message);

        Toast toast = new Toast(getContext());
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout); // Set custom layout for the toast
        toast.show();
    }
}

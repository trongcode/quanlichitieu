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
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.btec.fpt.campus_expense_manager.R;
import com.btec.fpt.campus_expense_manager.database.DatabaseHelper;
import com.btec.fpt.campus_expense_manager.models.BalanceInfor;
import com.btec.fpt.campus_expense_manager.models.UserInfor;

public class InformationFragment extends Fragment {
    private View view;
    private TextView tvFullname, edt_fullname, edt_lastname, edt_firstname, edt_email;
    private ImageButton btn_home_fragment, btn_chart, btn_home_add, btn_categories, btn_info;
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
        edt_firstname = view.findViewById(R.id.edt_firstname);
        edt_lastname = view.findViewById(R.id.edt_lastname);
        Button btn_logout = view.findViewById(R.id.btnLogout);
        btn_home_fragment = view.findViewById(R.id.btn_home_fragment);
        btn_chart = view.findViewById(R.id.btn_chart);
        btn_categories = view.findViewById(R.id.btn_categories);
        btn_home_add = view.findViewById(R.id.btn_home_add);
        btn_info = view.findViewById(R.id.btn_info);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String email = sharedPreferences.getString("email", null);
        BalanceInfor balanceInfor = dbHelper.getBalanceFromEmail(email);
        tvFullname.setText(String.format("%s %s", balanceInfor.getLastName(), balanceInfor.getFirstName()));
        edt_fullname.setText(String.format("%s %s", balanceInfor.getLastName(), balanceInfor.getFirstName()));
        edt_email.setText(balanceInfor.getEmail());
        edt_firstname.setText(balanceInfor.getFirstName());
        edt_lastname.setText(balanceInfor.getLastName());
        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("UserPrefs", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();

                boolean rememberMeChecked = sharedPreferences.getBoolean("remember_me", false);
                if (rememberMeChecked) {
                    // Nếu "Remember Me" được chọn, xóa dữ liệu (email, password)
                    editor.putBoolean("remember_me", true);
                    editor.apply();
                } else {
                    editor.putString("email", "");
                    editor.putString("password", "");
                    editor.putBoolean("remember_me", false);
                    editor.clear();// Cập nhật trạng thái của checkbox
                    editor.apply();
                }

                showToastCustom("Logged out successfully!");
                new android.os.Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // Chuyển sang LoginFragment sau khi toast đã hiển thị
                        LoginFragment loginFragment = new LoginFragment();
                        getActivity().getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.fragment_container, loginFragment)
                                .addToBackStack(null)
                                .commit();
                    }
                }, 2000);
            }
        });
        setupButtonListeners();
        return view;
    }
    private void setupButtonListeners() {
        btn_home_add.setOnClickListener(view -> loadFragment(new AddExpenseFragment()));
        btn_categories.setOnClickListener(view -> loadFragment(new CategoryFragment()));
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
    void showToastCustom(String message) {

        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.custom_toast, view.findViewById(R.id.custom_toast_layout));

        ImageView icon = layout.findViewById(R.id.toast_icon);
        icon.setImageResource(R.drawable.icon_x);

        TextView text = layout.findViewById(R.id.toast_message);
        text.setText(message);

        Toast toast = new Toast(getContext());
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();

    }
}

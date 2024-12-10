package com.btec.fpt.campus_expense_manager.fragments;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
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

public class ChangePassWordFragment extends Fragment {
    private EditText etCurrentPassword, etNewPassword, etConfirmNewPassword;
    private TextView tvEmail; // TextView để hiển thị email người dùng
    private Button btnChangePassword;
    private DatabaseHelper dbHelper;
    private View view;
    private String userEmail;
    private ImageButton btn_home_fragment, btn_chart, btn_home_add, btn_categories, btn_info;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_change_password, container, false);
        dbHelper = new DatabaseHelper(getContext());

        tvEmail = view.findViewById(R.id.tvEmail); // TextView để hiển thị email người dùng
        etCurrentPassword = view.findViewById(R.id.etCurrentPassword);
        etNewPassword = view.findViewById(R.id.etNewPassword);
        btnChangePassword = view.findViewById(R.id.btnChangePassword);
        etConfirmNewPassword = view.findViewById(R.id.etConfirmNewPassword);
        btn_home_fragment = view.findViewById(R.id.btn_home_fragment);
        btn_chart = view.findViewById(R.id.btn_chart);
        btn_categories = view.findViewById(R.id.btn_categories);
        btn_home_add = view.findViewById(R.id.btn_home_add);
        btn_info = view.findViewById(R.id.btn_info);
        // Lấy email người dùng từ SharedPreferences
        userEmail = getCurrentUserEmail();
        if (userEmail != null) {
            tvEmail.setText(userEmail); // Hiển thị email người dùng
        } else {
            showToastCustom("Không thể tải thông tin người dùng");
        }

        btnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String currentPassword = etCurrentPassword.getText().toString().trim();
                String newPassword = etNewPassword.getText().toString().trim();
                String confirmNewPassword = etConfirmNewPassword.getText().toString().trim();

                // Kiểm tra các trường nhập liệu
                if (currentPassword.isEmpty() || newPassword.isEmpty() || confirmNewPassword.isEmpty()) {
                    showToastCustom("Please fill in all information");
                    return;
                }

                // Kiểm tra mật khẩu mới và xác nhận mật khẩu mới có khớp nhau không
                if (!newPassword.equals(confirmNewPassword)) {
                    showToastCustom("New password and confirm password do not match");
                    return;
                }

                // Kiểm tra mật khẩu cũ
                boolean isValid = dbHelper.signIn(userEmail, currentPassword);
                if (!isValid) {
                    showToastCustom("Old password is incorrect");
                    return;
                }

                // Gọi hàm đổi mật khẩu
                boolean isChanged = dbHelper.changePassword(userEmail, currentPassword, newPassword);

                if (isChanged) {
                    showToastCustom("Password change successfully");
                    requireActivity().getSupportFragmentManager().popBackStack();
                } else {
                    showToastCustom("Password change failed! Please check your information again.");
                }
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
    // Lấy email của người dùng hiện tại từ SharedPreferences
    private String getCurrentUserEmail() {
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("UserPrefs", MODE_PRIVATE);
        return sharedPreferences.getString("email", null);
    }

    // Hiển thị thông báo tùy chỉnh
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
}

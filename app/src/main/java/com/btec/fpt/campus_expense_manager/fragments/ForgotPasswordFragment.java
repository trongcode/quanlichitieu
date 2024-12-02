package com.btec.fpt.campus_expense_manager.fragments;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.btec.fpt.campus_expense_manager.R;
import com.btec.fpt.campus_expense_manager.database.DatabaseHelper;

public class ForgotPasswordFragment extends Fragment {

    public ForgotPasswordFragment() {
        // Required empty public constructor

    }

    private EditText newFirstNameEditText, newLastNameEditText, newEmailEditText, newPasswordEditText;
    private Button saveButton;
    private DatabaseHelper dbHelper;
    private View view;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_forgot_password, container, false);
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("UserPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // Khởi tạo các view
        newFirstNameEditText = view.findViewById(R.id.newFirstname);
        newLastNameEditText = view.findViewById(R.id.newLastName);
        newEmailEditText = view.findViewById(R.id.new_email);
        newPasswordEditText = view.findViewById(R.id.new_password);
        saveButton = view.findViewById(R.id.save_button_forgot);

        // Khởi tạo DatabaseHelper
        dbHelper = new DatabaseHelper(getContext());


        saveButton.setOnClickListener(v -> {
            String newFirstName = newFirstNameEditText.getText().toString().trim();
            String newLastName = newLastNameEditText.getText().toString().trim();
            String newEmail = newEmailEditText.getText().toString().trim();
            String newPassword = newPasswordEditText.getText().toString().trim();

            // Kiểm tra các trường không trống
            if (!newFirstName.isEmpty() && !newLastName.isEmpty() && !newEmail.isEmpty() && !newPassword.isEmpty()) {

                boolean check = dbHelper.signUp(newFirstName, newLastName, newEmail, newPassword);
                if (check) {
                    editor.putString("firstName", newFirstName);
                    editor.putString("lastName", newLastName);
                    editor.putString("email", newEmail);
                    editor.putString("password", newPassword);
                    showToastCustom("Register Successful");

                    Fragment fragmentHome = new LoginFragment();
                    FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.fragment_container, new LoginFragment());
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                } else {
                    showToastCustom("Username already exists!!!!");
                }
            } else {
                Toast.makeText(getContext(), "Please enter all information!!!", Toast.LENGTH_SHORT).show();
            }
        });
        return view;

    }
    void showToastCustom(String message){

        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.custom_toast, view.findViewById(R.id.custom_toast_layout));
// Set the icon
        ImageView icon = layout.findViewById(R.id.toast_icon);
        icon.setImageResource(R.drawable.icon_x);  // Set your desired icon

// Set the text
        TextView text = layout.findViewById(R.id.toast_message);
        text.setText(message);

// Create and show the toast
        Toast toast = new Toast(getContext());
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();
    }
}

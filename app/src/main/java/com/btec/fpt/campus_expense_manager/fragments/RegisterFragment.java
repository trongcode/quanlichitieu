package com.btec.fpt.campus_expense_manager.fragments;


import static android.content.Context.MODE_PRIVATE;

import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Patterns;
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

public class RegisterFragment extends Fragment {
    DatabaseHelper databaseHelper;
    public RegisterFragment() {
        // Required empty public constructor
    }

    View view = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_register, container, false);
        databaseHelper = new DatabaseHelper(requireContext());
        EditText edtFirstName = view.findViewById(R.id.firstName);
        EditText edtLastName = view.findViewById(R.id.lastName);
        EditText edtEmail = view.findViewById(R.id.email);
        EditText edtPassword = view.findViewById(R.id.password);
        EditText edtConfirmPassword = view.findViewById(R.id.confirmPassword);
        Button buttonSave = view.findViewById(R.id.register_button);

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String firstName = edtFirstName.getText().toString();
                String lastName = edtLastName.getText().toString();
                String email = edtEmail.getText().toString();
                String password = edtPassword.getText().toString();
                String confirmPassword = edtConfirmPassword.getText().toString();

                boolean check = databaseHelper.signUp(firstName, lastName,email,password);
                if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty()) {
                    showToastCustom("Please fill in all fields");
                    return;
                }

                if (!password.equals(confirmPassword)) {
                    showToastCustom("Passwords do not match");
                    return;
                }
                if(check){
                    showToastCustom("Register successfully");
                    FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                    FragmentTransaction transaction = fragmentManager.beginTransaction();
                    transaction.replace(R.id.fragment_container, new LoginFragment()); // R.id.fragment_container là layout chứa fragment
                    transaction.addToBackStack(null); // Thêm vào BackStack để có thể quay lại
                    transaction.commit();
                }else {
                    showToastCustom("Cannot register !! Try again");
                }
            }
        });
        Button loginButton = view.findViewById(R.id.login_button);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.fragment_container, new LoginFragment()); // R.id.fragment_container là layout chứa fragment
                transaction.addToBackStack(null); // Thêm vào BackStack để có thể quay lại
                transaction.commit();
            }
        });
        // Set up button to go to ForgotPasswordFragment
        Button forgotPasswordButton = view.findViewById(R.id.forgot_password_button);
        forgotPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.fragment_container, new ForgotPasswordFragment());
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
        return view;
    }

    void showToastCustom(String message) {

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

    private void loadFragment(ForgotPasswordFragment forgotPasswordFragment) {
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, forgotPasswordFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }


    private boolean isValidEmail(String userInput) {
        // Use Android's built-in Patterns utility to validate the email format
        return !userInput.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(userInput).matches();
    }
}

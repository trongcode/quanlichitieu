package com.btec.fpt.campus_expense_manager.fragments;


import static android.content.Context.MODE_PRIVATE;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.btec.fpt.campus_expense_manager.MainActivity;
import com.btec.fpt.campus_expense_manager.R;
import com.btec.fpt.campus_expense_manager.database.DatabaseHelper;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class LoginFragment extends Fragment {

    public LoginFragment() {
        // Required empty public constructor
    }

    // Initialize SharedPreferences
    DatabaseHelper databaseHelper =null;

    View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         view = inflater.inflate(R.layout.fragment_login, container, false);
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("UserPrefs", MODE_PRIVATE);
        databaseHelper = new DatabaseHelper(getContext());

        Button loginButton = view.findViewById(R.id.login_button);
        Button registerButton = view.findViewById(R.id.register_button);
        Button forgotPasswordButton = view.findViewById(R.id.forgot_password_button);
        SharedPreferences.Editor editor = sharedPreferences.edit();


        EditText edtEmail = view.findViewById(R.id.email);
        EditText edtPassword = view.findViewById(R.id.password);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = edtEmail.getText().toString();
                String pwd = edtPassword.getText().toString();

                if(!email.isEmpty() && !pwd.isEmpty()) {
                    boolean check = databaseHelper.signIn(email, pwd);
                    if (check) {

                        editor.putString("email", email);
                        editor.putString("password", pwd);
                        editor.apply();
                        showToastCustom("Login successful");
                        Fragment fragmentHome = new HomeFragment();
                        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.fragment_container, new HomeFragment());
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();

                    } else {

                        showToastCustom("Email or password incorrect!");
                    }

                    }else{
                        showToastCustom("Email or password is invalid !!!");
                    }
            }
        });


        // Set up button to go to RegisterFragment
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.fragment_container, new RegisterFragment());
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        // Set up button to go to ForgotPasswordFragment
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


    private void showCustomToastMessage(String message) {
        Toast toast = new Toast(getContext());
        toast.setDuration(Toast.LENGTH_LONG);
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate
                (R.layout.custom_toast_layout,
                        view.findViewById(R.id.custom_toast_layout));
        ImageView icon = layout.findViewById(R.id.icon);
        icon.setImageResource(R.drawable.insta_icon);  // Set your desired icon
// Set the text
        TextView text = layout.findViewById(R.id.tv_content);
        text.setText(message);
        toast.setView(layout);
        toast.show();
    }

    void showMes(String message){

        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();

    }



    private  void showMessage(String message){
        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
    }

    private void showAlertDialog() {
        // Create an AlertDialog.Builder instance
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        // Set the title of the dialog
        builder.setTitle("Alert Dialog Title");

        // Set the message to be displayed
        builder.setMessage("This is a message to alert the user.");

        // Set a positive button with an onClick listener
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Action to take when the user clicks OK
                Toast.makeText(getContext(), "You clicked OK", Toast.LENGTH_SHORT).show();
            }
        });

        // Set a negative button with an onClick listener
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Action to take when the user clicks Cancel
                dialog.dismiss();  // Dismiss the dialog
            }
        });

        // Create and show the AlertDialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }




    private void showAlertDialogExample(){

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        // Set the title of the dialog
        builder.setTitle("Alert Dialog Title");

        // Set the message to be displayed
        builder.setMessage("This is a message to alert the user.");

        // Set a positive button with an onClick listener
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Action to take when the user clicks OK
                Toast.makeText(getContext(), "You clicked OK", Toast.LENGTH_SHORT).show();
            }
        });

        // Set a negative button with an onClick listener
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Action to take when the user clicks Cancel
                dialog.dismiss();  // Dismiss the dialog
            }
        });
        // Create and show the AlertDialog
        AlertDialog dialog = builder.create();
        dialog.show();

    }
}


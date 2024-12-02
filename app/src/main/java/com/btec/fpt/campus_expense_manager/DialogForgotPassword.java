//package com.btec.fpt.campus_expense_manager;
//
//import android.os.Bundle;
//import android.view.View;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.Toast;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.btec.fpt.campus_expense_manager.database.DatabaseHelper;
//import com.btec.fpt.campus_expense_manager.fragments.LoginFragment;
//
//public class DialogForgotPassword extends AppCompatActivity {
//    private EditText dialogInputEmail;
//    private Button dialogButtonOk;
//    private DatabaseHelper databaseHelper;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.dialog_option);
//        dialogInputEmail = findViewById(R.id.dialog_input);
//        dialogButtonOk = findViewById(R.id.dialog_button_ok);
//        databaseHelper = new DatabaseHelper(this);
//        dialogButtonOk.setOnClickListener(v -> {
//            String email = dialogInputEmail.getText().toString().trim();
//
//            // Kiểm tra email có hợp lệ và tồn tại trong cơ sở dữ liệu không
//            if (isValidEmail(email)) {
//                if (databaseHelper.emailExists(email)) {
//                    // Email tồn tại, thực hiện hành động tiếp theo (chuyển màn hình, v.v.)
//                    Toast.makeText(DialogForgotPassword.this, "Email tồn tại!", Toast.LENGTH_SHORT).show();
//                } else {
//                    // Nếu email không tồn tại
//                    Toast.makeText(DialogForgotPassword.this, "Email không tồn tại", Toast.LENGTH_SHORT).show();
//                }
//            } else {
//                Toast.makeText(DialogForgotPassword.this, "Email không hợp lệ", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//
//    // Hàm kiểm tra email hợp lệ
//    private boolean isValidEmail(String email) {
//        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
//    }
//}

package com.btec.fpt.campus_expense_manager;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.btec.fpt.campus_expense_manager.adapters.ItemAdapter;
import com.btec.fpt.campus_expense_manager.models.Item;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);
        getWindow().setStatusBarColor(ContextCompat.getColor(HomeActivity.this, R.color.F6918D));
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;

        });

        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);


        String email = sharedPreferences.getString("email", null);
        String password = sharedPreferences.getString("password", null); // Retrieve the hashed/encrypted version

        List<Item> itemList = new ArrayList<>();

        // Add data to the list (image resource + text)
        itemList.add(new Item(R.drawable.item1, "Item 1"));
        itemList.add(new Item(R.drawable.item2, "Item 2"));
        itemList.add(new Item(R.drawable.item3, "Item 3"));

        // Add data to the list (image resource + text)
        itemList.add(new Item(R.drawable.item4, "Item 4"));
        itemList.add(new Item(R.drawable.item5, "Item 5"));
        itemList.add(new Item(R.drawable.item6, "Item 6"));


        ItemAdapter itemAdapter = new ItemAdapter(this, itemList);

    }
}
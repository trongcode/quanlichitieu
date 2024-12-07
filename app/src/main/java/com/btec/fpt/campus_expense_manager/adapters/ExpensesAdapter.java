package com.btec.fpt.campus_expense_manager.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.btec.fpt.campus_expense_manager.R;

public class ExpensesAdapter extends CursorAdapter {
    public ExpensesAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0); // 0 để không cần cập nhật tự động
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_item_expense, parent , false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        @SuppressLint("Range") String description = cursor.getString(cursor.getColumnIndex("description"));
        @SuppressLint("Range") String amount = cursor.getString(cursor.getColumnIndex("amount"));
        @SuppressLint("Range") String date = cursor.getString(cursor.getColumnIndex("date"));

        description = (description == null) ? "No description" : description;
        amount = (amount == null) ? "0" : amount;
        date = (date == null) ? "No date" : date;

        // Ánh xạ dữ liệu vào các view trong item của ListView
        TextView descriptionTextView = view.findViewById(R.id.descriptionTextView);
        TextView amountTextView = view.findViewById(R.id.amountTextView);
        TextView dateTextView = view.findViewById(R.id.dateTextView);
        descriptionTextView.setText(description);
        amountTextView.setText(amount);
        dateTextView.setText(date);
    }
}

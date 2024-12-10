package com.btec.fpt.campus_expense_manager.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.btec.fpt.campus_expense_manager.R;
import com.btec.fpt.campus_expense_manager.entities.Expense;
import com.btec.fpt.campus_expense_manager.entities.Transaction;

import java.util.List;

public class ExpenseAdapter extends ArrayAdapter<Expense> {
    public ExpenseAdapter(Context context, List<Expense> expenses) {
        super(context, 0, expenses);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_transaction, parent, false);
        }

        Expense expense = getItem(position);

        TextView descriptionTextView = convertView.findViewById(R.id.descriptionTextView);
        TextView amountTextView = convertView.findViewById(R.id.amountTextView);
        TextView dateTextView = convertView.findViewById(R.id.dateTextView);
        TextView typeTextView = convertView.findViewById(R.id.typeTextView);

        descriptionTextView.setText(expense.getDescription());
        amountTextView.setText(String.format("%s VNĐ", expense.getAmount()));
        dateTextView.setText(expense.getDate());
        typeTextView.setText(expense.getType() == 0 ? "Expense" : "Income");

        return convertView;
    }
}

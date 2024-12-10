package com.btec.fpt.campus_expense_manager.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.btec.fpt.campus_expense_manager.R;
import com.btec.fpt.campus_expense_manager.entities.Transaction;
import com.btec.fpt.campus_expense_manager.models.TransactionInfor;

import java.text.DecimalFormat;
import java.util.List;

public class TransactionAdapter extends ArrayAdapter<Transaction> {
    public TransactionAdapter(Context context, List<Transaction> transactions) {
        super(context, 0, transactions);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_transaction, parent, false);
        }

        Transaction transaction = getItem(position);

        TextView descriptionTextView = convertView.findViewById(R.id.descriptionTextView);
        TextView amountTextView = convertView.findViewById(R.id.amountTextView);
        TextView dateTextView = convertView.findViewById(R.id.dateTextView);
        TextView typeTextView = convertView.findViewById(R.id.typeTextView);

        descriptionTextView.setText(transaction.getDescription());
        amountTextView.setText(String.format("$%.2f", transaction.getAmount()));
        dateTextView.setText(transaction.getDate());

        if (transaction.getType() == 0) {
            typeTextView.setText("Expense");
            typeTextView.setTextColor(getContext().getResources().getColor(R.color.blue));
        } else {
            typeTextView.setText("Income");
            typeTextView.setTextColor(getContext().getResources().getColor(R.color.F6918D));
        }

        return convertView;
    }
}


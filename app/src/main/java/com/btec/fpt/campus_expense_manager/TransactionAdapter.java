package com.btec.fpt.campus_expense_manager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.btec.fpt.campus_expense_manager.models.TransactionInfor;

import java.util.List;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder> {

    private Context context;
    private List<TransactionInfor> transactionList;

    // Constructor
    public TransactionAdapter(Context context, List<TransactionInfor> transactionList) {
        this.context = context;
        this.transactionList = transactionList;
    }

    @NonNull
    @Override
    public TransactionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate layout for each item
        View view = LayoutInflater.from(context).inflate(R.layout.fragment_categories, parent, false);
        return new TransactionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionViewHolder holder, int position) {
        // Get the current transaction
        TransactionInfor transaction = transactionList.get(position);

        // Bind data to the views
        holder.textViewAmount.setText(String.format("Amount: $%.2f", transaction.getAmount()));
        holder.textViewDescription.setText(String.format("Description: %s", transaction.getDescription()));
        holder.textViewDate.setText(String.format("Date: %s", transaction.getDate()));
    }

    @Override
    public int getItemCount() {
        return transactionList.size();
    }

    // ViewHolder class
    public static class TransactionViewHolder extends RecyclerView.ViewHolder {

        TextView textViewAmount;
        TextView textViewDescription;
        TextView textViewDate;

        public TransactionViewHolder(@NonNull View itemView) {
            super(itemView);

//            textViewAmount = itemView.findViewById(R.id.textViewAmount);
//            textViewDescription = itemView.findViewById(R.id.textViewDescription);
//            textViewDate = itemView.findViewById(R.id.textViewDate);
        }
    }
}

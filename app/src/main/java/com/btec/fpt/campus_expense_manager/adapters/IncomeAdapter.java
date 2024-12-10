package com.btec.fpt.campus_expense_manager.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.btec.fpt.campus_expense_manager.R;
import com.btec.fpt.campus_expense_manager.entities.Transaction;

import java.util.List;

public class IncomeAdapter extends ArrayAdapter<Transaction> {
    private Context context;
    private List<Transaction> transactionList;

    public IncomeAdapter(Context context, List<Transaction> transactionList) {
        super(context, 0, transactionList);
        this.context = context;
        this.transactionList = transactionList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Kiểm tra nếu convertView chưa được tạo
        if (convertView == null) {
            // Inflate layout item (fragment_display_expense.xml)
            convertView = LayoutInflater.from(context).inflate(R.layout.fragment_display_expense, parent, false);
        }

        // Lấy đối tượng Transaction từ danh sách
        Transaction transaction = getItem(position);

        // Tìm các TextView trong layout item
        TextView descriptionText = convertView.findViewById(R.id.descriptionTextView);
        TextView amountText = convertView.findViewById(R.id.amountTextView);
        TextView dateText = convertView.findViewById(R.id.dateTextView);

        // Gán giá trị vào các TextView
        if (transaction != null) {
            descriptionText.setText(transaction.getDescription());  // Mô tả giao dịch
            amountText.setText(String.valueOf(transaction.getAmount())); // Số tiền
            dateText.setText(transaction.getDate()); // Ngày giao dịch
        }

        // Trả về view đã được gắn dữ liệu
        return convertView;
    }
}

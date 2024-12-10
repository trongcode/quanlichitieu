package com.btec.fpt.campus_expense_manager.models;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

public class SharedViewModel extends ViewModel {
    private final MutableLiveData<List<String>> expenses = new MutableLiveData<>(new ArrayList<>());
    public LiveData<List<String>> getExpenses() {
        return expenses;
    }

    public void addExpense(String expense) {
        List<String> currentExpenses = expenses.getValue();
        if (currentExpenses != null) {
            currentExpenses.add(expense);
            expenses.setValue(currentExpenses);
        }
    }
}

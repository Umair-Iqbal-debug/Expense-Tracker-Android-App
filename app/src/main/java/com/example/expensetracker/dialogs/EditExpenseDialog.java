package com.example.expensetracker.dialogs;


import android.app.Dialog;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.example.expensetracker.MainActivity;
import com.example.expensetracker.R;
import com.example.expensetracker.model.Expense;

public class EditExpenseDialog extends DialogFragment {

    private Expense mExpense;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View dialogView = inflater.inflate(R.layout.dialog_edit_expense,null);

        builder.setMessage("Edit Expense").setView(dialogView);

        final EditText dateEditText = dialogView.findViewById(R.id.dateEditText);
        final EditText addressEditText = dialogView.findViewById(R.id.addressEditText);
        final EditText costEditText = dialogView.findViewById(R.id.costEditText);
        final EditText descriptionEditText = dialogView.findViewById(R.id.descriptionEditText);
        final RadioGroup category = dialogView.findViewById(R.id.categoryRadioGroup);
        RadioButton otherRadioBtn = dialogView.findViewById(R.id.otherRadioBtn);
        RadioButton autoRadioBtn = dialogView.findViewById(R.id.autoRadioBtn);
        RadioButton hardwareRadioBtn = dialogView.findViewById(R.id.hardwareRadioBtn);

        Button cancelBtn = dialogView.findViewById(R.id.cancelBtn);
        Button updateBtn = dialogView.findViewById(R.id.updateBtn);

        dateEditText.setText(mExpense.getDate());
        addressEditText.setText(mExpense.getAddress());
        costEditText.setText(mExpense.getTotalCost() + "");
        descriptionEditText.setText(mExpense.getDescription());

        if(mExpense.getCategory().equals(Expense.Category.OTHER)) otherRadioBtn.setChecked(true);
        if(mExpense.getCategory().equals(Expense.Category.AUTO)) autoRadioBtn.setChecked(true);
        if(mExpense.getCategory().equals(Expense.Category.HARDWARE)) hardwareRadioBtn.setChecked(true);


        cancelBtn.setOnClickListener(v -> dismiss());

        updateBtn.setOnClickListener(v ->{

            mExpense
                    .setOtherCost(0)
                    .setHardwareCost(0)
                    .setAutoCost(0);

            mExpense.setAddress(addressEditText.getText().toString())
                    .setDate(dateEditText.getText().toString())
                    .setTotalCost(Double.parseDouble(costEditText.getText().toString()))
                    .setDescription(descriptionEditText.getText().toString());

            switch(category.getCheckedRadioButtonId()){

                case R.id.autoRadioBtn:
                    mExpense.setCategory(Expense.Category.AUTO);
                    mExpense.setAutoCost(mExpense.getTotalCost());
                    break;

                case R.id.hardwareRadioBtn:
                    mExpense.setCategory(Expense.Category.HARDWARE);
                    mExpense.setHardwareCost(mExpense.getTotalCost());
                    break;

                case R.id.otherRadioBtn:
                    mExpense.setCategory(Expense.Category.OTHER);
                    mExpense.setOtherCost(mExpense.getTotalCost());
                    break;
            }

           // Log.d("EDIT","INSIDE EDIT EXPENSE" + mExpense);

            MainActivity callingActivity = (MainActivity) getActivity();
            callingActivity.getAdapter().notifyDataSetChanged();
            callingActivity.saveExpenseList();
            dismiss();

        });
        return builder.create();

    }

    public void sendExpense(Expense expense){
        mExpense = expense;
    }
}

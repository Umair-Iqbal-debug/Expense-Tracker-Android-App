package com.example.expensetracker.dialogs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.example.expensetracker.MainActivity;
import com.example.expensetracker.R;
import com.example.expensetracker.model.Expense;

public class AddExpenseDialog extends DialogFragment  {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)  {

        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();

        View dialogView = inflater.inflate(R.layout.dialog_new_expense, null);

        final EditText dateEditText = dialogView.findViewById(R.id.dateEditText);
        final EditText addressEditText = dialogView.findViewById(R.id.addressEditText);
        final EditText costEditText = dialogView.findViewById(R.id.costEditText);
        final EditText descriptionEditText = dialogView.findViewById(R.id.descriptionEditText);
        final RadioGroup category = dialogView.findViewById(R.id.categoryRadioGroup);



        Button cancelBtn = dialogView.findViewById(R.id.cancelBtn);
        Button addBtn = dialogView.findViewById(R.id.addBtn);

        cancelBtn.setOnClickListener(v -> dismiss());

        addBtn.setOnClickListener(v ->{

            if(!isAllFieldsFilled(category,dateEditText,addressEditText,costEditText,descriptionEditText)){
                // show alert dialog box
                AlertDialog alertDialog = new AlertDialog.Builder(requireActivity()).create();
                alertDialog.setTitle("Alert");
                alertDialog.setMessage("All Fields must be filled");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        (dialog, which) -> dialog.dismiss());
                alertDialog.show();


                return;
            }
            Expense expense = new Expense();
            setExpenseCategory(category, expense);
            expense.setAddress(addressEditText.getText().toString())
                    .setDate(dateEditText.getText().toString())
                    .setTotalCost(Double.parseDouble(costEditText.getText().toString()))
                    .setDescription(descriptionEditText.getText().toString());

            MainActivity callingActivity = (MainActivity) getActivity();
            callingActivity.addExpense(expense);
            dismiss();

        });

        builder.setMessage("Add an expense").setView(dialogView);

        return builder.create();
    }

    private void setExpenseCategory(RadioGroup category, Expense expense) {
        switch(category.getCheckedRadioButtonId()){

            case R.id.autoRadioBtn:
                expense.setCategory(Expense.Category.AUTO);
                break;

            case R.id.hardwareRadioBtn:
                expense.setCategory(Expense.Category.HARDWARE);
                break;

            case R.id.otherRadioBtn:
                expense.setCategory(Expense.Category.OTHER);
                break;
        }
    }

    private boolean isAllFieldsFilled(RadioGroup r, EditText... fields){

        if(r.getCheckedRadioButtonId() == -1) return false;

        for(EditText field: fields)
            if(field.getText() == null) return false;

        return true;
    }

}

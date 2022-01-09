package com.example.expensetracker.dialogs;

import android.app.Dialog;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.example.expensetracker.MainActivity;

public class DeleteAllDialog extends DialogFragment {


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class because this dialog
        // has a simple UI
        AlertDialog.Builder builder =
                new AlertDialog.Builder(requireActivity());
        MainActivity mainActivity = (MainActivity) getActivity();

        builder.setMessage("ARE YOU SURE YOU WANT TO DELETE ALL ?")
                .setPositiveButton("OK", ((dialog, which) -> {
                    mainActivity.deleteAll();
                    dismiss();
                }))
                .setNegativeButton("Cancel", (dialog, which) -> {
                    dismiss();
                });

        return builder.create();
    }


}
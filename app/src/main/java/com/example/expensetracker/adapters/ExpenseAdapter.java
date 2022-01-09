package com.example.expensetracker.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.expensetracker.MainActivity;
import com.example.expensetracker.R;
import com.example.expensetracker.dialogs.EditExpenseDialog;
import com.example.expensetracker.model.Expense;

import java.util.List;

public class ExpenseAdapter extends RecyclerView.Adapter<ExpenseAdapter.ViewHolder> {


    private List<Expense> mExpenseList;
    private MainActivity mMainActivity;

    public ExpenseAdapter(MainActivity mainActivity, List<Expense> mExpenseList) {
        this.mExpenseList = mExpenseList;
        this.mMainActivity = mainActivity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View listItem = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item, parent, false);
        return new ViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setDetails(mExpenseList.get(position));
    }

    @Override
    public int getItemCount() {
        return mExpenseList.size();
    }

    // view holder class
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView mCategoryTextView, mAddressTextView, mDescriptionTextView, mCostTextView, mDateTextView;
        ImageView mDeleteExpenseBtn, mEditExpenseBtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mCategoryTextView = itemView.findViewById(R.id.categoryTextView);
            mAddressTextView = itemView.findViewById(R.id.addressTextView);
            mDescriptionTextView = itemView.findViewById(R.id.descriptionTextView);
            mCostTextView = itemView.findViewById(R.id.costTextView);
            mDateTextView = itemView.findViewById(R.id.dateTextView);
            mDeleteExpenseBtn = itemView.findViewById(R.id.deleteImageView);
            mEditExpenseBtn = itemView.findViewById(R.id.editExpenseImageView);

            mDeleteExpenseBtn.setOnClickListener(this);

            mEditExpenseBtn.setOnClickListener(this);

        }

        public void setDetails(Expense expense) {
            // might limit length of address shown
            mAddressTextView.setText(expense.getAddress());
            mCategoryTextView.setText(expense.getCategoryString());
            mCostTextView.setText(expense.getTotalCostString());
            // could limit description
            mDescriptionTextView.setText(expense.getDescription());
            mDateTextView.setText(expense.getDate());

        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {

                case R.id.deleteImageView:
                    int position = getAdapterPosition();
                    mExpenseList.remove(getAdapterPosition());
                    notifyItemRemoved(position);
                    break;

                case R.id.editExpenseImageView:
                    mMainActivity.showEditExpenseDialog(mExpenseList.get(getAdapterPosition()));
                    notifyItemChanged(getAdapterPosition());
                    break;
            }
        }
    }
}

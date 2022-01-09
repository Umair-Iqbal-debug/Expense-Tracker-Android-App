package com.example.expensetracker;

import static androidx.core.content.FileProvider.getUriForFile;
import static java.security.AccessController.getContext;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import com.example.expensetracker.adapters.ExpenseAdapter;
import com.example.expensetracker.dialogs.AddExpenseDialog;
import com.example.expensetracker.dialogs.DeleteAllDialog;
import com.example.expensetracker.dialogs.EditExpenseDialog;
import com.example.expensetracker.model.Expense;
import com.example.expensetracker.pdftable.Util;
import com.example.expensetracker.serializer.JSONSerializer;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Environment;
import android.util.Log;
import android.view.View;

import androidx.core.content.FileProvider;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.expensetracker.databinding.ActivityMainBinding;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Table;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private List<Expense> mExpenseList;
    private RecyclerView recyclerView;
    private ExpenseAdapter mAdapter;
    private JSONSerializer mSerializer;
    private SharedPreferences prefs;
    SharedPreferences.Editor editor;
    private String name;
    public static final String app_name = "EXPENSE";
    public static final String KEY_NAME = "NAME";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        prefs = getSharedPreferences(app_name,MODE_PRIVATE);

        editor=prefs.edit();

        name = prefs.getString(KEY_NAME,"new name");


        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddExpenseDialog dialog = new AddExpenseDialog();
                dialog.show(getSupportFragmentManager(),"123");

            }
        });

        mSerializer = new JSONSerializer("ExpenseList.json",
                getApplicationContext());
        try {
            mExpenseList = mSerializer.load();
        } catch (Exception e) {
            mExpenseList = new ArrayList<>();
            Log.e("Error loading notes: ", "", e);
        }

        initRecyclerView();
    }

    public void initRecyclerView(){

        recyclerView =
                findViewById(R.id.recyclerView);
        mAdapter = new ExpenseAdapter(this, mExpenseList);
        RecyclerView.LayoutManager mLayoutManager =
                new LinearLayoutManager(getApplicationContext());

        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        recyclerView.setAdapter(mAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {

            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }

        else if(id == R.id.generatePDF){
            try {
                name = prefs.getString(KEY_NAME,"no name found");
                Util.createPdf(mExpenseList,name);
                Toast.makeText(getApplicationContext(),"PDF created",Toast.LENGTH_SHORT).show();

                composeEmail(new String[]{"mlambert@ddelevator.com,kenny@ddelevator.com"},"Employee Expense Report",
                        FileProvider.getUriForFile(Objects.requireNonNull(getApplicationContext()),
                                BuildConfig.APPLICATION_ID + ".provider", Util.file));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            return true;
        }

        else if(id == R.id.deleteAll){
            // maybe show another dialog here
            DeleteAllDialog dialog = new DeleteAllDialog();
            dialog.show(getSupportFragmentManager(),"123");

        }

        return super.onOptionsItemSelected(item);
    }

    public void addExpense(Expense expense){
        mExpenseList.add(expense);
        mAdapter.notifyItemInserted(mExpenseList.size()-1);
        saveExpenseList();
    }

    @Override
    public void onPause(){
        super.onPause();
        saveExpenseList();
    }

    public void saveExpenseList(){
        try{
            mSerializer.save(mExpenseList);
        }catch(Exception e){
            Log.e("Error Saving expenses","", e);
        }
    }


    public void showEditExpenseDialog(Expense expense) {
        EditExpenseDialog dialog = new EditExpenseDialog();
        dialog.sendExpense(expense);
        dialog.show(getSupportFragmentManager(),"123");
        mAdapter.notifyDataSetChanged();
        saveExpenseList();
    }

    public ExpenseAdapter getAdapter(){
        return mAdapter;
    }

    @Override
    public void onResume() {

        super.onResume();

        prefs = getSharedPreferences("Expense",MODE_PRIVATE);
        name = prefs.getString("name","new user");
    }

    public void deleteAll() {
        mExpenseList.clear();
        mAdapter.notifyDataSetChanged();
        saveExpenseList();
    }

    public void composeEmail(String[] addresses, String subject, Uri attachment) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("*/*");
        intent.putExtra(Intent.EXTRA_EMAIL, addresses);
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_STREAM, attachment);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }
}
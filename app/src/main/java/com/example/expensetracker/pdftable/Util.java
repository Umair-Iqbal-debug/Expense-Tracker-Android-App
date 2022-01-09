package com.example.expensetracker.pdftable;

import android.os.Build;
import android.os.Environment;
import android.util.Log;

import androidx.annotation.RequiresApi;



import com.example.expensetracker.model.Expense;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.layout.property.TextAlignment;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class Util {

    private static final String DOWNLOADS_DIRECTORY = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString();
    private static final String DEFAULT_PATH = "Employee_expense_report.pdf";
    public static final File file = new File(DOWNLOADS_DIRECTORY,DEFAULT_PATH);


    @RequiresApi(api = Build.VERSION_CODES.O)
    public static void createPdf(List<Expense> expenseList,String name) throws FileNotFoundException {

        PdfWriter writer = new PdfWriter(file);
        PdfDocument pdfDocument = new PdfDocument(writer);
        Document document = new Document(pdfDocument);

        Paragraph header = new Paragraph("Employee Expense Report");
        header.setTextAlignment(TextAlignment.CENTER);
        header.setFontSize(30f);

        header.setMarginBottom(20f);

        document.add(header);

        Paragraph employeeName = new Paragraph("Employee Name: " + name);
        employeeName.setMarginBottom(10f);
        employeeName.setHorizontalAlignment(HorizontalAlignment.CENTER);
        document.add(employeeName);

        Paragraph date = new Paragraph("Date: " + LocalDate.now().format(DateTimeFormatter.ofPattern("MM/dd/yyyy")));
        date.setMarginBottom(10f);
        date.setHorizontalAlignment(HorizontalAlignment.CENTER);
        document.add(date);

        float[] columnWidth = {50f,200f,50f,50f,50f,100f};

        Table table = new Table(columnWidth);

        table.addCell("Date");
        table.addCell("Location (Job Address) ");
        table.addCell("Auto");
        table.addCell("Hardware");
        table.addCell("Other");
        table.addCell("Description Of Items Purchased");

        double totalCost = 0, totalAutoCost = 0, totalHardwareCost = 0,totalOtherCost = 0;

        for(Expense expense : expenseList){

            Log.d("EXPENSE", expense.toString());
            table.addCell(expense.getDate());
            table.addCell(expense.getAddress());
            table.addCell(expense.getAutoCost()+"");
            table.addCell(expense.getHardwareCost()+"");
            table.addCell(expense.getOtherCost()+ "");
            table.addCell(expense.getDescription());

            double cost = expense.getTotalCost();
            totalCost += cost;

            switch (expense.getCategory()){
                case AUTO:
                    totalAutoCost+=cost;
                    break;
                case HARDWARE:
                    totalHardwareCost+= cost;
                    break;

                case OTHER:
                    totalOtherCost += cost;
                    break;

            }
        }

        table.addCell(" ");
        table.addCell(" ");
        table.addCell("$"+totalAutoCost);
        table.addCell("$"+totalHardwareCost);
        table.addCell("$"+ totalOtherCost);
        table.addCell("Grand Total $" + totalCost);



        table.setHorizontalAlignment(HorizontalAlignment.CENTER);
        document.add(table);
        document.close();
    }
}

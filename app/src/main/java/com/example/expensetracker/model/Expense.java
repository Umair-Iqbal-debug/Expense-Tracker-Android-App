package com.example.expensetracker.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

public class Expense {

    public enum Category{
        AUTO,HARDWARE,OTHER;
    }

    private String address, description, date;
    private double autoCost,hardwareCost,otherCost,totalCost;
    private Category category;

    // JSON KEYS
    private final String JSON_ADDRESS = "address";
    private final String JSON_DESC = "description";
    private final String JSON_DATE = "date";
    private final String JSON_CATEGORY = "category";
    private final String JSON_TOTAL_COST = "totalCost";

    public Expense() {
    }

    public Expense(JSONObject jo) throws JSONException {
        address = jo.getString(JSON_ADDRESS);
        description = jo.getString(JSON_DESC);
        date = jo.getString(JSON_DATE);
        totalCost = jo.getDouble(JSON_TOTAL_COST);
        category = Category.valueOf(jo.getString(JSON_CATEGORY));

        setCost(totalCost, category);
    }

    public Expense(String address, String description, String date,
                   double totalCost, Category category) {
        this.address = address;
        this.description = description;
        this.date = date;
        this.totalCost = totalCost;
        this.category = category;

        setCost(totalCost, category);
    }

    private void setCost(double totalCost, Category category) {
        if(category.equals(Category.OTHER)) otherCost = totalCost;
        if(category.equals(Category.HARDWARE)) hardwareCost = totalCost;
        if(category.equals(Category.AUTO)) autoCost = totalCost;
    }

    public JSONObject convertToJson() throws JSONException {
        JSONObject jo = new JSONObject();
        jo.put(JSON_ADDRESS,address);
        jo.put(JSON_DATE,date);
        jo.put(JSON_TOTAL_COST,totalCost);
        jo.put(JSON_DESC,description);
        jo.put(JSON_CATEGORY,category.toString());

        return jo;
    }

    public String getAddress() {
        return address;
    }

    public Expense setAddress(String address) {
        this.address = address;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public Expense setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getDate() {
        return date;
    }

    public Expense setDate(String date) {
        this.date = date;

        return this;
    }

    public double getAutoCost() {
        return autoCost;
    }

    public Expense setAutoCost(double autoCost) {
        this.autoCost = autoCost;
        return this;
    }

    public double getHardwareCost() {
        return hardwareCost;
    }

    public Expense setHardwareCost(double hardwareCost) {
        this.hardwareCost = hardwareCost;
        return this;
    }

    public double getOtherCost() {
        return otherCost;
    }

    public Expense setOtherCost(double otherCost) {
        this.otherCost = otherCost;
        return this;
    }

    public double getTotalCost() {
        return totalCost;
    }
    public String getTotalCostString() {
        return "$" + totalCost;
    }

    public Expense setTotalCost(double totalCost) {
        this.totalCost = totalCost;
        // when we set total cost change
       if(category!=null)inferCostFromCategory();
        return this;
    }

    private void inferCostFromCategory() {
        // everytime category changes we have to update it's particular cost
        resetAllCosts();

        switch (category){
            case AUTO:
                autoCost = totalCost;
                break;
            case HARDWARE:
                hardwareCost = totalCost;
                break;
            case OTHER:
                otherCost = totalCost;
                break;
        }
    }

    private void resetAllCosts(){
        autoCost = 0;
        otherCost = 0;
        hardwareCost = 0;
    }

    @Override
    public String toString() {
        return "Expense{" +
                "address='" + address + '\'' +
                ", description='" + description + '\'' +
                ", date='" + date + '\'' +
                ", autoCost=" + autoCost +
                ", hardwareCost=" + hardwareCost +
                ", otherCost=" + otherCost +
                ", totalCost=" + totalCost +
                ", category=" + category +
                '}';
    }

    public Category getCategory() {
        return category;
    }

    public String getCategoryString(){
        StringBuilder sb = new StringBuilder();
        String categoryString = category.toString();
        sb.append(categoryString.charAt(0))
                .append(categoryString.substring(1).toLowerCase());

        return sb.toString();
    }

    public Expense setCategory(Category category) {
        this.category = category;
        inferCostFromCategory();
        return this;
    }


}

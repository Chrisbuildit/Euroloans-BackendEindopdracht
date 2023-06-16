package com.euroloans.eindopdracht.model;

public enum Users {
    ;
    public String Borrower;
    public String Investor;
    public String Employee;

    Users(String borrower, String investor, String employee) {
        Borrower = borrower;
        Investor = investor;
        Employee = employee;
    }
}

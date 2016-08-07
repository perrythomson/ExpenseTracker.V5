package com.tracker.servlets;


import com.tracker.data.Expense;
import com.tracker.data.util.ExpenseUtil;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;

public class ExpenseControllerServlet extends HttpServlet {
    private int[] expenseYears = new int[17];
    private int[] expenseMonths = {1,2,3,4,5,6,7,8,9,10,11,12};
    private int[] expenseDays = {1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31};

    public void init(ServletConfig config) throws ServletException {
        int year = 2000;
        for(int i=0;i<17;i++) {
            expenseYears[i] = year;
            year++;
        }
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {


        String uri = request.getRequestURI();
        System.out.println("Requested URI: "+uri);

        String jspName = uri.substring(uri.lastIndexOf('/')+1);
        System.out.println("JSP Name: "+jspName);

        if(jspName.equalsIgnoreCase("viewAllExpenses")) {
            ArrayList<Expense> expenses = ExpenseUtil.getAllExpenses();
            request.setAttribute("expenses",expenses);
            request.setAttribute("expenseCategories",Expense.ExpenseCategory.values());
            request.setAttribute("expenseYears",expenseYears);
            request.setAttribute("expenseMonths",expenseMonths);
            request.setAttribute("expenseDays",expenseDays);
        } else if (jspName.equalsIgnoreCase("viewExpensesInCategory")) {
            ArrayList<Expense> expenses = ExpenseUtil.getExpensesInCategory(Expense.ExpenseCategory.valueOf(request.getParameter("expenseCategory")));
            request.setAttribute("expenses",expenses);
            request.setAttribute("expenseCategories",Expense.ExpenseCategory.values());
            request.setAttribute("expenseYears",expenseYears);
            request.setAttribute("expenseMonths",expenseMonths);
            request.setAttribute("expenseDays",expenseDays);
            jspName = "viewAllExpenses";
        } else if (jspName.equalsIgnoreCase("viewExpensesInDateRange")) {
            int yearStart = Integer.parseInt(request.getParameter("expenseYearStart"));
            int monthStart = Integer.parseInt(request.getParameter("expenseMonthStart"));
            int dayStart = Integer.parseInt(request.getParameter("expenseDayStart"));
            int yearEnd = Integer.parseInt(request.getParameter("expenseYearEnd"));
            int monthEnd = Integer.parseInt(request.getParameter("expenseMonthEnd"));
            int dayEnd = Integer.parseInt(request.getParameter("expenseDayEnd"));
            ArrayList<Expense> expenses = ExpenseUtil.getExpensesInDateRange(LocalDate.of(yearStart, monthStart, dayStart),LocalDate.of(yearEnd, monthEnd, dayEnd));
            request.setAttribute("expenses",expenses);
            request.setAttribute("expenseCategories",Expense.ExpenseCategory.values());
            request.setAttribute("expenseYears",expenseYears);
            request.setAttribute("expenseMonths",expenseMonths);
            request.setAttribute("expenseDays",expenseDays);
            jspName = "viewAllExpenses";
        } else if (jspName.equalsIgnoreCase("viewExpense")) {
            String expenseId = request.getParameter("id");
            Expense expense = new Expense(Integer.parseInt(expenseId));
            request.setAttribute("expense",expense);
        } else if (jspName.equalsIgnoreCase("addExpense")) {
            request.setAttribute("expenseCategories",Expense.ExpenseCategory.values());
            request.setAttribute("expenseYears",expenseYears);
            request.setAttribute("expenseMonths",expenseMonths);
            request.setAttribute("expenseDays",expenseDays);
        } else if (jspName.equalsIgnoreCase("saveNewExpense")) {
            Expense expense = new Expense();
            expense.setExpenseName(request.getParameter("expenseName"));
            expense.setExpenseAmount(Integer.parseInt(request.getParameter("expenseAmount")));
            int year = Integer.parseInt(request.getParameter("expenseYear"));
            int month = Integer.parseInt(request.getParameter("expenseMonth"));
            int day = Integer.parseInt(request.getParameter("expenseDay"));
            expense.setExpenseDate(LocalDate.of(year, month, day));
            expense.setExpenseCategory(Expense.ExpenseCategory.valueOf(request.getParameter("expenseCategory")));
            expense.saveNew();
            request.setAttribute("expenseCategories",Expense.ExpenseCategory.values());
            request.setAttribute("expenseYears",expenseYears);
            request.setAttribute("expenseMonths",expenseMonths);
            request.setAttribute("expenseDays",expenseDays);
            ArrayList<Expense> expenses = ExpenseUtil.getAllExpenses();
            request.setAttribute("expenses",expenses);
            jspName = "viewAllExpenses";
        }

        RequestDispatcher view = request.getRequestDispatcher("/expenseViews/"+jspName+".jsp");
        view.forward(request, response);
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        doPost(request,response);
    }
}

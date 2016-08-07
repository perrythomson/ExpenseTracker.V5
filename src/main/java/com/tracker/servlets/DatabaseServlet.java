package com.tracker.servlets;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.sql.*;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.mchange.v2.c3p0.DataSources;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

public class DatabaseServlet extends HttpServlet {

    private static ComboPooledDataSource connectionPool = null;
    private static final String JDBC_DRIVER = "org.hsqldb.jdbcDriver";
    private static final String DB_URL = "jdbc:hsqldb:expense_tracker_db";
    private static final String USER = "sa";
    private static final String PASS = "password";
    //run before starting compared to destroy

    public void init(ServletConfig config) throws ServletException {                //init unique to servlets-it is not a constructor
        try                                                                         //used only in servlet container-scans and identifies resources ready to go for constructing
        {                                                                           //pool of connections waiting to go...open connection...database is ready look in Maven C3P0
            final ComboPooledDataSource cpds = new ComboPooledDataSource();
            cpds.setDriverClass(JDBC_DRIVER);
            cpds.setJdbcUrl(DB_URL);
            cpds.setUser(USER);
            cpds.setPassword(PASS);
            connectionPool = cpds;
            System.out.println("NOTE: DATABASE CONNECTION POOL STARTED");
            expenseInitialLoad();//loads test data logs
//            expenseCatInitalLoad();
//            dropExpenseTable();
//            dropTestTable();
        }
        catch (PropertyVetoException pve)
        {
            pve.printStackTrace();
        }
    }

    private void dropExpenseTable() {
        try {
//            Class.forName("org.hsqldb.jdbcDriver");
//            Connection conn = DriverManager.getConnection("jdbc:hsqldb:expense_tracker_db","sa","");
//            Statement stmt = conn.createStatement();
//            stmt.executeUpdate("DROP TABLE expense;");  //todo find out why its different
//
//            stmt.close();
//            conn.close();
            update("DROP TABLE expense");
            System.out.println("NOTE: Table dropped.");
        } catch(Exception e) {
            System.out.println("ERROR: Table didn't drop.");
        }
    }

    public static Connection getConnection() { //call this getConnection to get a connection from the poo
        try
        {
            return connectionPool.getConnection();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            return null;
        }
    }

    private void expenseInitialLoad() {
        try {
            Connection connection = DatabaseServlet.getConnection();
            if(connection != null) {
                Statement stmt = connection.createStatement();
                String query = "SELECT * FROM expense";
                ResultSet rs = stmt.executeQuery(query);
                rs.next();
            } else {
                System.out.println("ERROR: connection is NULL");
            }
        }
        catch(SQLException sqle){
            try {
                System.out.println("NOTE: TABLE EXPENSE DOES NOT EXIST, CREATING DATABASE");
                update("CREATE TABLE expense (id INTEGER IDENTITY PRIMARY KEY, expenseName VARCHAR(30), " +
                        "expenseAmount INTEGER, expenseDate DATE, expenseCategory VARCHAR(30))"); //changed varchar to date for enum
                update("INSERT INTO expense (expenseName, expenseAmount, expenseDate, expenseCategory) VALUES('Alpha1', 100, '1/1/2020', A )");
                update("INSERT INTO expense (expenseName, expenseAmount, expenseDate, expenseCategory) VALUES('Bravo2', 200, '1/2/2020', B )");
                update("INSERT INTO expense (expenseName, expenseAmount, expenseDate, expenseCategory) VALUES('Charlie3', 300, '1/3/2020', C )");
                update("INSERT INTO expense (expenseName, expenseAmount, expenseDate, expenseCategory) VALUES('Echo4', 400, '1/4/2020', D )");
                System.out.println("NOTE: DATABASE FINISHED CREATING");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }



//    private void expCatInitalLoad() {
//        try {
//            Connection connection = com.tracker.servlets.DatabaseServlet.getConnection();
//            if(connection != null) {
//                Statement stmt = connection.createStatement();
//                String query = "SELECT * FROM expCat";
//                ResultSet rs = stmt.executeQuery(query);
//                rs.next();
//            } else {
//                System.out.println("ERROR: connection is NULL");
//            }
//        }
//        catch(SQLException sqle){
//            try {
//                System.out.println("NOTE: DATABASE DOES NOT EXIST, CREATING DATABASE");
//                update("CREATE TABLE expCat (id INTEGER IDENTITY PRIMARY KEY, categoryType VARCHAR(2)");
//                update("INSERT INTO expCat (categoryName) VALUES('A')");
//                update("INSERT INTO expCat (categoryName) VALUES('B')");
//                update("INSERT INTO expCat (categoryName) VALUES('C')");
//                update("INSERT INTO expCat (categoryName) VALUES('D')");
//                System.out.println("NOTE: DATABASE FINISHED CREATING");
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
//    private void readAllJoinedRecords(boolean printMe) throws Exception{
//        Class.forName("org.hsqldb.jdbcDriver");
//        Connection conn = DriverManager.getConnection("jdbc:hsqldb:expense_tracker_db","sa","");
//        Statement stmt = conn.createStatement();
//        ResultSet rs = stmt.executeQuery("SELECT exp.employee, exp.amount, exp.expDate, expCat.categoryName" +
//                "FROM exp LEFT JOIN expCat" +
//                "ON exp.expId = expCat.id;");
//        while(rs.next()) {
//            String rowResults = rs.getString("ex.employee")+" -- "+rs.getString("exp.amount")
//                    +" -- "+rs.getString("exp.expDate"+" -- "+rs.getString("expCat.categoryName"));
//            if(printMe) {
//                System.out.println(rowResults);
//            }
//        }
//
//        rs.close();
//        stmt.close();
//        conn.close();
//    }

    private synchronized void update(String sqlExpression) throws SQLException {
        Connection connection = DatabaseServlet.getConnection();
        if(connection != null) {
            System.out.println("========= sqlExpression: "+sqlExpression);
            Statement stmt = connection.createStatement();
            int i = stmt.executeUpdate(sqlExpression);
            if (i == -1) {
                System.out.println("ERROR: database error in update "+sqlExpression);
            }
        }  else {
            System.out.println("ERROR: connection is NULL");
        }
    }

    public void destroy()
    {
        try
        {
            DataSources.destroy(connectionPool);
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    protected void doPost(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {}

    protected void doGet(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {}
}



//package com.tracker.data;
//
//import com.tracker.servlets.DatabaseServlet;
//
//import java.sql.*;
//
///**
// * Created by perrythomson on 8/5/16.
// */
//public class ExpCat {
//    private Connection connection;
//    private int id;
//
//    private String categoryName;
//    private String A;
//    private String B;
//    private String C;
//    private String D;
//
//
//    private boolean exists = false;
//
//    public ExpCat() {
//
//    }
//
//    public ExpCat(int id) {
//        load(id);
//    }
//
//    // This is the READ in CRUD
//    public void load(int id) {
//        try
//        {
//            connection = DatabaseServlet.getConnection();
//
//            Statement stmt = connection.createStatement();
//            String query = "SELECT * FROM expCat WHERE id = "+id;
//            ResultSet rs = stmt.executeQuery(query);
//            rs.next();
//            this.id = id;
//            this.setCategoryName(rs.getString("categoryName"));
//            exists = true;
//        }
//        catch(SQLException sqle){
//            sqle.printStackTrace();
//        }
//    }
//
//    // This is the CREATE in CRUD
//    public void saveNew() {
//        if(!exists){
//            try
//            {
//                connection = DatabaseServlet.getConnection();
//
//                String query = "INSERT INTO expCat (categoryName) VALUES (?, ?, ?, ?);";
//                PreparedStatement preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
//                preparedStatement.setString(1,this.getA());
//                preparedStatement.setString(2,this.getB());
//                preparedStatement.setString(3,this.getC());
//                preparedStatement.setString(4,this.getD());
//                ResultSet rs = preparedStatement.getGeneratedKeys();
//                if (rs != null && rs.next()) {
//                    this.id = rs.getInt(1);
//                    exists = true;
//                }
//            }
//            catch(SQLException sqle){
//                sqle.printStackTrace();
//            }
//        } else {
//            System.out.println("ERROR: Object already exists in database. Don't use saveNew(), use update().");
//        }
//    }
//
//    // This is the UPDATE in CRUD
//    private void update() {
//        if(exists){
//            try
//            {
//                connection = DatabaseServlet.getConnection();
//
//                String query = "UPDATE expCat SET categoryName = ? WHERE id = ?";
//                PreparedStatement preparedStatement = connection.prepareStatement(query);
//
//                preparedStatement.setString(1,this.getCategoryName());
//                preparedStatement.setInt(2,this.getId());
//                preparedStatement.executeUpdate();
//            }
//            catch(SQLException sqle){
//                sqle.printStackTrace();
//            }
//        } else {
//            System.out.println("ERROR: Object does not exist in database yet. Don't use update(), use saveNew().");
//        }
//    }
//
//    // This is the DELETE in CRUD
//    private void delete(){
//        if(exists){
//            try
//            {
//                connection = DatabaseServlet.getConnection();
//
//                String query = "DELETE FROM expCat WHERE id = ?";
//                PreparedStatement preparedStatement = connection.prepareStatement(query);
//                preparedStatement.setInt(1,this.getId());
//                preparedStatement.executeUpdate();
//                exists = false;
//            }
//            catch(SQLException sqle){
//                sqle.printStackTrace();
//            }
//        } else {
//            System.out.println("ERROR: Object does not exist in database yet. You must load() object before you can delete()");
//        }
//    }
//
//    public int getId() {return id;}
//    public void setId(int id) {this.id = id;}
//
//    public String getCategoryName() {return categoryName;}
//    public void setCategoryName(String categoryName) {this.categoryName = categoryName;}
//
//    public String getA() {return A;}
//    public void setA(String a) {A = a;}
//
//    public String getB() {return B;}
//    public void setB(String b) {B = b;}
//
//    public String getC() {return C;}
//    public void setC(String c) {C = c;}
//
//    public String getD() {return D;}
//    public void setD(String d) {D = d;}
//}
//
//

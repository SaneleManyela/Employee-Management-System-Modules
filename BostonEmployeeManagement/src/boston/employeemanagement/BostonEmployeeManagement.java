/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package boston.employeemanagement;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import javax.swing.*;
/**
 *
 * @author Sanele
 */
public class BostonEmployeeManagement extends JFrame {
    public BostonEmployeeManagement() {
        this.setTitle("Boston Employee Management System"); //Sets the title of the frame
        this.setExtendedState(MAXIMIZED_BOTH); //Display the form in full screen
        this.setDefaultCloseOperation(EXIT_ON_CLOSE); //On an event of closing this form, the whole application will exit
        mMainWindow(); //A method call to create graphical user interface of this form
    }
    
    //A method that creates the main window by positioning the menu on a JPanel container
    private void mMainWindow() {
        JPanel jpPanel = new JPanel(new BorderLayout()); //Creates a JPanel object and set its layout to BorderLayout
        jpPanel.setOpaque(true); //prepares the JPanel to be set background colour
        jpPanel.setBackground(new Color(30, 30, 30)); //The JPanel object is set the colour
        jpPanel.add(mMenu(), BorderLayout.NORTH); //The JPanel object adds a JMenuBar in the north border of the window
        this.add(jpPanel); //The JPanel object is added to this form
    }
    
    /**
     * A method that defines a JMenuBar, the menu bar's menus,
     * and the menus' menu items.
     */
    private JMenuBar mMenu() {
        JMenuBar mbMenuBar = new JMenuBar(); //Instantiation of a JMenuBar object
        JMenu mnuFile = new JMenu("File"); //Instantiation of a JMenu object File
        JMenuItem mnuItemExit = new JMenuItem("Exit"); //Instantiation of a JMenuItem object Exit
        mnuItemExit.addActionListener(new ActionListener() { //an action listener is added to the menu item mnuItemExit so that when clicked, the application stops
           @Override
           public void actionPerformed(ActionEvent e) {
               System.exit(0); //Stops the application
           }
        });
    
        JMenu mnuAdminPortal = new JMenu("Admin Portal"); //Instantiation of a JMenu object Admin Portal
        JMenuItem mnuItemAdminPortal = new JMenuItem("Admin Portal"); //Instantiation of a JMenuItem object Admin Portal
        mnuItemAdminPortal.addActionListener(new ActionListener() { //an action listener is added to the menu item mnuItemAdminPortal so that when clicked, the order form is displayed
            @Override
            public void actionPerformed(ActionEvent e) {
                frmAdminPortal frmPortal = new frmAdminPortal();
                frmPortal.setVisible(true);
            }
        });
        
        JMenu mnuSalary = new JMenu("Salary"); //Instantiation of a JMenu object Salary
        JMenuItem mnuItemSalary = new JMenuItem("Calculate employee salary"); //Instantiation of a JMenuItem object Salary
        mnuItemSalary.addActionListener(new ActionListener() { //an action listener is added to the menu item mnuItemSalary so that when clicked, the waiter interface is displayed
            @Override
            public void actionPerformed(ActionEvent e) {
                frmEmployeeSalary frmSalary = new frmEmployeeSalary();
            }
        });
        
        mnuFile.add(mnuItemExit); //Adds menu item exit to the JMenu File
        mbMenuBar.add(mnuFile); //Adds JMenu File to the menu bar
        mnuAdminPortal.add(mnuItemAdminPortal);
        mbMenuBar.add(mnuAdminPortal);
        mnuSalary.add(mnuItemSalary);
        mbMenuBar.add(mnuSalary);
        return mbMenuBar; //returns the JMenuBar
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        BostonEmployeeManagement employeeManagement = new BostonEmployeeManagement();
        employeeManagement.setVisible(true); //this displays the form
    }
    
    //This methods connect to the database and return
    //a connection for further use in the system.
    public Connection mConnectToDatabaseBostonEmployees() {
        String strDBConnectionString = "jdbc:mysql://localhost:3306/employeedatabase";
    	String strDBUser = "root";
        String strDBPassword = "password";
        java.sql.Connection conMySQLConnectionString = null;
        try {
            conMySQLConnectionString = DriverManager.getConnection(strDBConnectionString, strDBUser, strDBPassword);
        } catch(SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
        return conMySQLConnectionString;
    }
    
    //A method that checks a status of an account, if it exists and
    //return a boolean value.
    public boolean mCheckRecordStatus(String strQuery) {
        boolean boolStatus = false;
        try(Statement stStatement = mConnectToDatabaseBostonEmployees().prepareStatement(strQuery)){
            stStatement.execute(strQuery);
            try (ResultSet rs = stStatement.getResultSet()) {
                boolStatus = rs.next();
                stStatement.close();
                rs.close();
            }
        } catch(SQLException e){
            JOptionPane.showMessageDialog(null, "The application has encountered a technical error.\n"+ e.getMessage());
        }
        return boolStatus;
    }
    
    //A method that insert data and create records in the database
    public void mCreateRecord(String strQuery, String strButtonOfAction) {
        try
        {
            try (Statement stStatement = mConnectToDatabaseBostonEmployees().prepareStatement(strQuery)) {
                stStatement.executeUpdate(strQuery);
                stStatement.close();
                switch(strButtonOfAction){
                    case "Add":
                        JOptionPane.showMessageDialog(null, "Account created.");
                        break;
                    case "Save":
                        JOptionPane.showMessageDialog(null, "Salary information saved.");
                        break;
                }
            }
	} catch(SQLException e){
            JOptionPane.showMessageDialog(null, " The record could not be created\n"+e.getMessage());
        }
    }
    
    //A method that gets a numeric field from the database
    //and return it as integer value.
    public int mGetNumericField(String strQuery) {
        try {
            try (Statement stStatement = mConnectToDatabaseBostonEmployees().prepareStatement(strQuery)) {
                stStatement.execute(strQuery);
                try (ResultSet rs = stStatement.getResultSet()) {
                    while(rs.next()){
                        return rs.getInt(1);
                    }
                    stStatement.close();
                    rs.close();
                }
            }
	} catch(SQLException | NullPointerException e){
            JOptionPane.showMessageDialog(null, "Technical error has been encounterd\n"+e.getMessage());
        }
       return 0;
    }
    
    public String mGetTextField(String strQuery) {
        try {
            try (Statement stStatement = mConnectToDatabaseBostonEmployees().prepareStatement(strQuery)) {
                stStatement.execute(strQuery);
                try (ResultSet rs = stStatement.getResultSet()) {
                    while(rs.next()){
                        return rs.getString(1);
                    }
                    stStatement.close();
                    rs.close();
                }
            }
	} catch(SQLException | NullPointerException e){
            JOptionPane.showMessageDialog(null, "Technical error has been encounterd\n"+e.getMessage());
        }
       return null;
    }
    
    static String[] arrRecordDetails = null; //A declaration of an array of type string.
    
    //A method that fetches data from the database and 
    //populate a string array and return that array
    public String[] mFetchEmployeeRecords(String strQuery) {
        try {
            try (Statement stStatement = mConnectToDatabaseBostonEmployees().prepareStatement(strQuery)) {
                stStatement.execute(strQuery);
                try (ResultSet rs = stStatement.getResultSet()) {
                    ResultSetMetaData rsmt = rs.getMetaData();
                    arrRecordDetails = new String[rsmt.getColumnCount()+1];
                    while(rs.next()) {
                        for(int i = 1; i < arrRecordDetails.length; i++){
                            arrRecordDetails[i] = String.valueOf(rs.getString(i));                    
                        }
                    }
                    stStatement.close();
                    rs.close();
                }
                return arrRecordDetails;
            }
	} catch(SQLException e){
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
        return null;
    }
    
    //A method that updates data in the database
    public void mUpdateRecordDetails(String strQuery) {
        try {
            try (Statement stStatement = mConnectToDatabaseBostonEmployees().prepareStatement(strQuery)) {
                stStatement.executeUpdate(strQuery);
                stStatement.close();
                JOptionPane.showMessageDialog(null, "Record updated successfully.");
            }
        } catch(HeadlessException | SQLException e){
            JOptionPane.showMessageDialog(null, "Technical error, updated transaction could not be finished\n."+e.getMessage());
        }
    }
    
    //A method that deletes data in the database
    public void mDeleteRecordDetails(String strQuery) {
        try {
            try (Statement stStatement = mConnectToDatabaseBostonEmployees().prepareStatement(strQuery)) {
                stStatement.execute(strQuery);
                stStatement.close();
                JOptionPane.showMessageDialog(null, "Record was deleted.");
            }
        } catch(HeadlessException | SQLException e){
            JOptionPane.showMessageDialog(null, "Technical error, deleted transaction could not be finished.\n"+e.getMessage());
        }
    }
    
    //A method to delete all database table records
    public void mDeleteAllRecordDetails(String strQuery) {
        try { 
            try (Statement stStatement = mConnectToDatabaseBostonEmployees().prepareStatement(strQuery)) {
                stStatement.addBatch(strQuery);
                stStatement.executeBatch();
                stStatement.close();
                JOptionPane.showMessageDialog(null, "Record was deleted.");
            }
        } catch(HeadlessException | SQLException e){
            JOptionPane.showMessageDialog(null, "Technical error, deleted transaction could not be finished.\n"+e.getMessage());
        }
    }
}

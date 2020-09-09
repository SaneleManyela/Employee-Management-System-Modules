/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package boston.employeemanagement;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import javax.swing.border.*;
import java.util.Date;
import java.time.*;
import java.util.ArrayList;
import javax.swing.table.DefaultTableModel;
/**
 *
 * @author Sanele
 */
public class frmAdminPortal extends javax.swing.JFrame {

    /**
     * Creates new form frmAdminPortal
     */
    public frmAdminPortal() {
        initComponents();
        this.setTitle("Admin Portal"); //This sets the title of the form
        dskPane.setBackground(new Color(30, 30, 30)); //This sets the background colour of the JDesktopPane container
        this.setLocationRelativeTo(null); //Displays the form at the center of the window
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);  //When closed, this form is destroyed/exited, not the entire apllication
        mTable("SELECT Firstname, Lastname, Department, DateOfBirth, PhoneNumber FROM tblEmployee", tblEmployee, "tblEmployee"); //Displays data on table
        txtDOB.setText(new SimpleDateFormat("yyyy/MM/dd").format(new Date())); //Sets the Date of birth as current date
        txtFirstname.requestFocusInWindow(); //Places the cursor on the JTextField txtFirstname
    }

    //Declaration of string variables to be used to hold data passed by an admin
    String strEmployeeID, strFirstname, strLastname, strJobTitle, strDepartment, strPhoneNo,
           strDateOfBirth, strUsername, strPassword;
    private DefaultTableModel dmModel;
    
    //A method to get from the GUI values passed by an admin
    private void mGetValuesFromGUI() {
        this.strFirstname = txtFirstname.getText();
        this.strLastname = txtLastname.getText();
        this.strJobTitle = txtJobTitle.getText();
        this.strDepartment = txtDepartment.getText();
        this.strPhoneNo = txtPhoneNo.getText();
        this.strDateOfBirth = txtDOB.getText();
        this.strUsername = txtUsername.getText();
        this.strPassword = txtPassword.getText();
    }
      
    //A method that returns a string variable, a query to insert and
    //create a new employee record
    private String mAddEmployeeDetailsQuery() {
        return "INSERT INTO tblEmployee(Firstname, Lastname, JobTitle, Department,"
                + " DateOfBirth, PhoneNumber, Username, Password)"+
                "VALUES('"+ strFirstname + "','" + strLastname + "','" 
                + strJobTitle +"','"+strDepartment+"','"+strDateOfBirth+"','"
                +strPhoneNo+"','"+strUsername+"','"+strPassword+"')";
    }
   
    //A method that returns a string variable, a query to update an
    //employee record in the database
    private String mUpdateRecord() {
        mGetValuesFromGUI();
        return "UPDATE tblEmployee SET Firstname ='"+strFirstname+"', Lastname='"+strLastname+
                "', JobTitle ='"+strJobTitle+"', Department ='"+strDepartment+"', DateOfBirth ='"
                +strDateOfBirth+"', PhoneNumber ='"+strPhoneNo+"', Username='"+strUsername+"', Password='"
                +strPassword+"' WHERE ID ='"+strEmployeeID+"'";
    }
    
    //A method that returns a string variable, a query to delete all the records in
    //the employee table
    private String mDeleteAll() {
        return "DELETE FROM tblEmployee";
    }
    
    //A method that returns an array list object of the employee details
    private void mRetriveColumnNames(String strQuery) {
        try (Statement stSQLQuery = new BostonEmployeeManagement().mConnectToDatabaseBostonEmployees().prepareStatement(strQuery)) {
                ResultSet rs = stSQLQuery.executeQuery(strQuery);
                ResultSetMetaData rsmt = rs.getMetaData();
                
                String[] str = new String[rsmt.getColumnCount() + 1];
                
                for(int i = 1; i < rsmt.getColumnCount() + 1; i++) {
                    str[i] = rsmt.getColumnName(i);
                }
                dmModel.setColumnIdentifiers(new clsRemoveEmptyIndexes().mRemoveEmptyIndexes(str).toArray(
                        new String[new clsRemoveEmptyIndexes().mRemoveEmptyIndexes(str).size()]));
        } catch(SQLException e) {
            JOptionPane.showMessageDialog(frmAdminPortal.this, "Message: "+e.getMessage());
        }
    }
            
    public void mTable(String strQuery, JTable tbl, String strTable) {
        String[] arrEmployeeIDs = new clsRemoveEmptyIndexes().mRemoveEmptyIndexes(
                new BostonEmployeeManagement().mFetchEmployeeRecords("SELECT ID FROM tblEmployee")).toArray(
                        new String[new clsRemoveEmptyIndexes().mRemoveEmptyIndexes(
                                new BostonEmployeeManagement().mFetchEmployeeRecords("SELECT ID FROM tblEmployee")).size()]); 
        
        dmModel =  (DefaultTableModel)tbl.getModel();
        
        mRetriveColumnNames(strQuery);
        
        String[] arrEmployeeDetails;
        for(int i = 0; i < arrEmployeeIDs.length; i++){
            if(strTable.equals("tblEmployee")){
                arrEmployeeDetails = new BostonEmployeeManagement().mFetchEmployeeRecords(strQuery+" WHERE ID='"+arrEmployeeIDs[i]+"'");
            } else {
                arrEmployeeDetails = new BostonEmployeeManagement().mFetchEmployeeRecords(strQuery);
            }
            dmModel.insertRow(i, new clsRemoveEmptyIndexes().mRemoveEmptyIndexes(arrEmployeeDetails).toArray(
                        new String[new clsRemoveEmptyIndexes().mRemoveEmptyIndexes(arrEmployeeDetails).size()]));
        }
        
        tbl.setOpaque(true);
        tbl.setBackground(new Color(255, 255, 255));
    }
    
    class clsRemoveEmptyIndexes {
        public java.util.List<String> mRemoveEmptyIndexes(String[] array) {
            java.util.List<String> values = new ArrayList<>();
            for(int data = 0; data < array.length; data++) {
                if(array[data] != null) { 
                    values.add(array[data]);
                }
            }   
            return values;
        }
}
    /**
     * An inner class to display a JDialog with a combo box populated with
     * employee first and user name. Depending on where an object of this class is
     * instantiated, it sets values to GUI text fields or perform a transaction to 
     * delete a record from a database table
     */
    public class clsTransactionDialog extends JDialog {
        public clsTransactionDialog(String str) {
            super(null, str, Dialog.ModalityType.APPLICATION_MODAL); //Sets title of this JDialog box then set it to rquire all the focus
            this.setSize(400, 200); //sets size of the dialog
            this.setLocationRelativeTo(null); //displays the dialog at the very center
            this.setDefaultCloseOperation(DISPOSE_ON_CLOSE); //Causes the dialog to be exited but not the entire app
            mCreateDialog(str); //Calls a method to create the dialog GUI
        }
        
        JComboBox cboEmployeeNames = new JComboBox(); //A combo box object to hold employee data
        String[] arrValues; //A string array to hold employee details from the database
        
        //A method that create the GUI of the dialog by specifying how each component
        //should be positioned
        private void mCreateDialog(String str) {
            JPanel jpPanel = new JPanel(new BorderLayout(0, 20));  //Creates a JPanel container object and sets its layout
            jpPanel.setBorder(new EmptyBorder(20, 20, 20, 20)); //Sets a border 
            jpPanel.setOpaque(true); //Prepares the JPanel to be applied colour
            jpPanel.setBackground(new Color(255, 255, 255)); //Sets background colour to the JPanel
            
            JLabel lblLabel; //A JLabel variable to be used to hold the dialog label title
            if(str.equals("Update")) { //Checks if this class was instantiated during an Update transaction
                lblLabel = new JLabel("Select Employee to Update"); //Creates an instance of JLabel and set its text
                jpPanel.add(lblLabel, BorderLayout.NORTH);
            } else if(str.equals("Delete")) { //Checks if this class was instantiated during a Delete transaction
                lblLabel = new JLabel("Select Employee to Delete");
                jpPanel.add(lblLabel, BorderLayout.NORTH);
            } else if(str.equals("Salary")) { //Checks if this class was instantiated before calculating an employee salary
                lblLabel = new JLabel("Select to Calculate Salary for");
                jpPanel.add(lblLabel, BorderLayout.NORTH);
            }
                        
            JPanel jpCenterPart = new JPanel(new BorderLayout()); //A JPanel to contain the center part of the dialog GUI
            jpCenterPart.add(cboEmployeeNames, BorderLayout.CENTER);
            mLoadToComboBox("SELECT Firstname, Username FROM tblEmployee");
            jpPanel.add(jpCenterPart);
            
            JPanel jpLowerPart = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0)); //A JPanel to contain the lower part of the GUI
            jpLowerPart.setOpaque(true);
            jpLowerPart.setBackground(new Color(255, 255, 255));
            JButton btn = null; //Declaration of a variable of type JButton
            if(str.equals("Update")) { //Checks if this class was instantiated during an Update transaction
                btn = new JButton("Ok"); //Instantiate a button object and set its text to Ok
                btn.addActionListener(new ActionListener() { //Sets an action listener so that when clicked this button does an event 
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        mSetGUIValues();
                        mCloseDialog();
                    }
                });
                btn.setPreferredSize(new Dimension(90, 25));
            } else if(str.equals("Delete")) { //Checks if this class was instantiated during a Delete transaction
                btn = new JButton("Delete");
                btn.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) { //Sets an action listener so that in an event this button is clicked, details of an employee will be deleted
                        new BostonEmployeeManagement().mDeleteRecordDetails(
                                "DELETE FROM tblEmployee WHERE ID='"+
                                        new BostonEmployeeManagement().mGetNumericField(
                                                "SELECT ID FROM tblEmployee WHERE Firstname='"+
                                 cboEmployeeNames.getSelectedItem().toString().substring(
                                         0, cboEmployeeNames.getSelectedItem().toString().indexOf(" ")).trim()+"' AND "
                                         + "Username ='"+cboEmployeeNames.getSelectedItem().toString().substring(
                                                 cboEmployeeNames.getSelectedItem().toString().indexOf(" "),
                                                 cboEmployeeNames.getSelectedItem().toString().length()).trim()+"'")+"'");
                        mCloseDialog();
                    }
                });
            } else if(str.equals("Salary")) {
                btn = new JButton("Ok");
                btn.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        frmEmployeeSalary.strSelectedEmployee = cboEmployeeNames.getSelectedItem().toString();
                        mCloseDialog();
                    }
                });
                btn.setPreferredSize(new Dimension(90, 25));
            }
            
            jpLowerPart.add(btn);
            jpPanel.add(jpLowerPart, BorderLayout.SOUTH); //Specifies where the lower part should be positioned
            this.add(jpPanel);
        }
        
        //A method that fetches data/values from the
        //database and set values to the GUI input fields.
        private void mSetGUIValues() {
            String[] arrValues = 
                    new BostonEmployeeManagement().mFetchEmployeeRecords("SELECT * FROM tblEmployee WHERE Username ='"+
                            cboEmployeeNames.getSelectedItem().toString().substring(cboEmployeeNames.getSelectedItem().toString().indexOf(" "),
                                    cboEmployeeNames.getSelectedItem().toString().trim().length()).trim()+"' AND Firstname ='"+
                            cboEmployeeNames.getSelectedItem().toString().substring(0, cboEmployeeNames.getSelectedItem().toString().indexOf(" ")).trim()+"'");
            strEmployeeID = arrValues[1];
            txtFirstname.setText(arrValues[2]);
            txtLastname.setText(arrValues[3]);
            txtJobTitle.setText(arrValues[4]);
            txtDepartment.setText(arrValues[5]);
            txtDOB.setText(arrValues[6]);
            txtPhoneNo.setText(arrValues[7]);
            txtUsername.setText(arrValues[8]);
            txtPassword.setText(arrValues[9]);
            btnUpdate.setText("Save");
        }   
        
        //A method to destroy a current object of this class
        private void mCloseDialog() {
            this.hide();
        }
                      
        //A methos to fetch details from the database and populate thie dialog's conbo box
        private void mLoadToComboBox(String strQuery) {
            try {
                try (Statement stStatement = 
                        new BostonEmployeeManagement().mConnectToDatabaseBostonEmployees().prepareStatement(strQuery)) {
                    stStatement.execute(strQuery);
                    try (ResultSet rs = stStatement.getResultSet()) {
                        while(rs.next()) {
                            cboEmployeeNames.addItem(rs.getString(1) + " " + rs.getString(2));
                        }
                        stStatement.close();
                        rs.close();
                    }
                }
            } catch(SQLException e) {
                JOptionPane.showMessageDialog(null,"A technical error has been encountered\n"+e.getMessage());
            }
        }
    }
    
    private boolean mIsPhoneNumberNumeric(String str) {
        for (char c : str.toCharArray()) {
            if (!Character.isDigit(c)){
                return false;
            }
        }
        return true;
    } 
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        dskPane = new javax.swing.JDesktopPane();
        jpEmployeeInformation = new javax.swing.JPanel();
        txtFirstname = new javax.swing.JTextField();
        lblEmployeeInfo = new javax.swing.JLabel();
        lblFirstname = new javax.swing.JLabel();
        lblLastName = new javax.swing.JLabel();
        txtLastname = new javax.swing.JTextField();
        lblJobTitle = new javax.swing.JLabel();
        txtJobTitle = new javax.swing.JTextField();
        lblDepartment = new javax.swing.JLabel();
        txtDepartment = new javax.swing.JTextField();
        lblDateOfBirth = new javax.swing.JLabel();
        txtDOB = new javax.swing.JTextField();
        lblPhoneNumber = new javax.swing.JLabel();
        txtPhoneNo = new javax.swing.JTextField();
        lblUsername = new javax.swing.JLabel();
        txtUsername = new javax.swing.JTextField();
        lblPassword = new javax.swing.JLabel();
        txtPassword = new javax.swing.JTextField();
        jpEmployeeDetailsView = new javax.swing.JPanel();
        jsScrollPane = new javax.swing.JScrollPane();
        tblEmployee = new javax.swing.JTable();
        lblCommands = new javax.swing.JLabel();
        lblEmployeeMgmtSys = new javax.swing.JLabel();
        btnAdd = new javax.swing.JButton();
        btnUpdate = new javax.swing.JButton();
        btnDelete = new javax.swing.JButton();
        btnDeleteAll = new javax.swing.JButton();
        btnRefresh = new javax.swing.JButton();
        btnClose = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        dskPane.setBackground(new java.awt.Color(30, 30, 30));

        jpEmployeeInformation.setBackground(new java.awt.Color(255, 255, 255));

        lblEmployeeInfo.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblEmployeeInfo.setText("Employee Information");

        lblFirstname.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        lblFirstname.setText("First name");

        lblLastName.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        lblLastName.setText("Last name");

        lblJobTitle.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        lblJobTitle.setText("Job Title");

        lblDepartment.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        lblDepartment.setText("Department");

        lblDateOfBirth.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        lblDateOfBirth.setText("Date Of Birth");

        lblPhoneNumber.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        lblPhoneNumber.setText("Phone Number");

        lblUsername.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        lblUsername.setText("Username");

        lblPassword.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        lblPassword.setText("Password");

        javax.swing.GroupLayout jpEmployeeInformationLayout = new javax.swing.GroupLayout(jpEmployeeInformation);
        jpEmployeeInformation.setLayout(jpEmployeeInformationLayout);
        jpEmployeeInformationLayout.setHorizontalGroup(
            jpEmployeeInformationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpEmployeeInformationLayout.createSequentialGroup()
                .addGap(32, 32, 32)
                .addGroup(jpEmployeeInformationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jpEmployeeInformationLayout.createSequentialGroup()
                        .addComponent(lblEmployeeInfo)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jpEmployeeInformationLayout.createSequentialGroup()
                        .addGroup(jpEmployeeInformationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jpEmployeeInformationLayout.createSequentialGroup()
                                .addComponent(lblFirstname)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 91, Short.MAX_VALUE)
                                .addComponent(txtFirstname, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jpEmployeeInformationLayout.createSequentialGroup()
                                .addGroup(jpEmployeeInformationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lblLastName)
                                    .addComponent(lblJobTitle)
                                    .addComponent(lblDepartment)
                                    .addComponent(lblDateOfBirth)
                                    .addComponent(lblPhoneNumber)
                                    .addComponent(lblUsername)
                                    .addComponent(lblPassword))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(jpEmployeeInformationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(txtLastname)
                                    .addComponent(txtJobTitle)
                                    .addComponent(txtDepartment)
                                    .addComponent(txtDOB)
                                    .addComponent(txtPhoneNo)
                                    .addComponent(txtUsername)
                                    .addComponent(txtPassword, javax.swing.GroupLayout.DEFAULT_SIZE, 115, Short.MAX_VALUE))))
                        .addGap(34, 34, 34))))
        );
        jpEmployeeInformationLayout.setVerticalGroup(
            jpEmployeeInformationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpEmployeeInformationLayout.createSequentialGroup()
                .addGap(38, 38, 38)
                .addComponent(lblEmployeeInfo)
                .addGap(26, 26, 26)
                .addGroup(jpEmployeeInformationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblFirstname)
                    .addComponent(txtFirstname, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jpEmployeeInformationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblLastName)
                    .addComponent(txtLastname, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jpEmployeeInformationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblJobTitle)
                    .addComponent(txtJobTitle, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jpEmployeeInformationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblDepartment)
                    .addComponent(txtDepartment, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jpEmployeeInformationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblDateOfBirth)
                    .addComponent(txtDOB, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jpEmployeeInformationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblPhoneNumber)
                    .addComponent(txtPhoneNo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jpEmployeeInformationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblUsername)
                    .addComponent(txtUsername, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jpEmployeeInformationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblPassword)
                    .addComponent(txtPassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(61, Short.MAX_VALUE))
        );

        jpEmployeeDetailsView.setBackground(new java.awt.Color(255, 255, 255));

        tblEmployee.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Firstname", "Lastname", "Department", "Date Of Birth", "PhoneNumber"
            }
        ));
        jsScrollPane.setViewportView(tblEmployee);
        if (tblEmployee.getColumnModel().getColumnCount() > 0) {
            tblEmployee.getColumnModel().getColumn(0).setHeaderValue("Firstname");
            tblEmployee.getColumnModel().getColumn(1).setHeaderValue("Lastname");
            tblEmployee.getColumnModel().getColumn(2).setHeaderValue("Department");
            tblEmployee.getColumnModel().getColumn(3).setHeaderValue("Date Of Birth");
            tblEmployee.getColumnModel().getColumn(4).setHeaderValue("PhoneNumber");
        }

        lblCommands.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblCommands.setText("Commands");

        lblEmployeeMgmtSys.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblEmployeeMgmtSys.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblEmployeeMgmtSys.setText("Employee Management System: Admin Portal");

        btnAdd.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnAdd.setText("Add");
        btnAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddActionPerformed(evt);
            }
        });

        btnUpdate.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnUpdate.setText("Update");
        btnUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdateActionPerformed(evt);
            }
        });

        btnDelete.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnDelete.setText("Delete");
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });

        btnDeleteAll.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnDeleteAll.setText("Delete All");
        btnDeleteAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteAllActionPerformed(evt);
            }
        });

        btnRefresh.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnRefresh.setText("Refresh");
        btnRefresh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRefreshActionPerformed(evt);
            }
        });

        btnClose.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnClose.setText("Close");
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jpEmployeeDetailsViewLayout = new javax.swing.GroupLayout(jpEmployeeDetailsView);
        jpEmployeeDetailsView.setLayout(jpEmployeeDetailsViewLayout);
        jpEmployeeDetailsViewLayout.setHorizontalGroup(
            jpEmployeeDetailsViewLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpEmployeeDetailsViewLayout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addGroup(jpEmployeeDetailsViewLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jpEmployeeDetailsViewLayout.createSequentialGroup()
                        .addComponent(btnAdd)
                        .addGap(18, 18, 18)
                        .addComponent(btnUpdate)
                        .addGap(18, 18, 18)
                        .addComponent(btnDelete)
                        .addGap(18, 18, 18)
                        .addComponent(btnDeleteAll)
                        .addGap(18, 18, 18)
                        .addComponent(btnRefresh))
                    .addComponent(lblCommands)
                    .addComponent(jsScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblEmployeeMgmtSys))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnClose)
                .addGap(0, 28, Short.MAX_VALUE))
        );
        jpEmployeeDetailsViewLayout.setVerticalGroup(
            jpEmployeeDetailsViewLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpEmployeeDetailsViewLayout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addComponent(lblEmployeeMgmtSys)
                .addGap(33, 33, 33)
                .addComponent(jsScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 205, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(lblCommands)
                .addGap(18, 18, 18)
                .addGroup(jpEmployeeDetailsViewLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnAdd)
                    .addComponent(btnUpdate)
                    .addComponent(btnDelete)
                    .addComponent(btnDeleteAll)
                    .addComponent(btnRefresh)
                    .addComponent(btnClose))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        dskPane.setLayer(jpEmployeeInformation, javax.swing.JLayeredPane.DEFAULT_LAYER);
        dskPane.setLayer(jpEmployeeDetailsView, javax.swing.JLayeredPane.DEFAULT_LAYER);

        javax.swing.GroupLayout dskPaneLayout = new javax.swing.GroupLayout(dskPane);
        dskPane.setLayout(dskPaneLayout);
        dskPaneLayout.setHorizontalGroup(
            dskPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(dskPaneLayout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(jpEmployeeInformation, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jpEmployeeDetailsView, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(25, 25, 25))
        );
        dskPaneLayout.setVerticalGroup(
            dskPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(dskPaneLayout.createSequentialGroup()
                .addGap(46, 46, 46)
                .addGroup(dskPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jpEmployeeInformation, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jpEmployeeDetailsView, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(46, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(dskPane)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(dskPane)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddActionPerformed
        if(txtFirstname.getText().equals("") || txtLastname.getText().equals("") ||
                txtJobTitle.getText().equals("") || txtDepartment.getText().equals("") ||
                txtDOB.getText().equals("") || txtPhoneNo.getText().equals("") || 
                txtUsername.getText().equals("") || txtPassword.getText().equals(""))
        {
            JOptionPane.showMessageDialog(frmAdminPortal.this, "All text field input is required!");
        } else if(Period.between((LocalDate.parse((txtDOB.getText().replace("/", "-")))),
                                LocalDate.parse(new SimpleDateFormat("yyyy-MM-dd").format(new Date()))).getYears() < 18){
            JOptionPane.showMessageDialog(frmAdminPortal.this, "An employee must be 18 yrs or older!");
        } else if(txtPassword.getText().length() < 7) {
            JOptionPane.showMessageDialog(frmAdminPortal.this, "Password characters must exceed seven characters.");
        } else if(new BostonEmployeeManagement().mCheckRecordStatus("SELECT * FROM tblEmployee WHERE Firstname ='"+txtFirstname.getText()+"' AND Lastname='"+txtLastname.getText()+
                "' AND DateOfBirth ='"+txtDOB.getText()+"'")) {
            JOptionPane.showMessageDialog(frmAdminPortal.this, "This record exists");
        } else if(txtPhoneNo.getText().length() < 10 || txtPhoneNo.getText().length() > 10) {
            JOptionPane.showMessageDialog(null, "A Phone Number Must Be Ten Numbers");
        }else if(!mIsPhoneNumberNumeric(txtPhoneNo.getText())) {
                    JOptionPane.showMessageDialog(frmAdminPortal.this, "Please provide valid number");
        } else {
            mGetValuesFromGUI();
            new BostonEmployeeManagement().mCreateRecord(mAddEmployeeDetailsQuery(), btnAdd.getText());
        }
    }//GEN-LAST:event_btnAddActionPerformed

    private void btnUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateActionPerformed
        if(btnUpdate.getText().equals("Update")) {
            clsTransactionDialog clsDialog = new clsTransactionDialog("Update");
            clsDialog.setVisible(true);
            btnAdd.setEnabled(false);
            btnDelete.setEnabled(false);
        } else if(btnUpdate.getText().equals("Save")) {
            if(txtFirstname.getText().equals("") || txtLastname.getText().equals("") ||
                txtJobTitle.getText().equals("") || txtDepartment.getText().equals("") ||
                txtDOB.getText().equals("") || txtPhoneNo.getText().equals("") || 
                txtUsername.getText().equals("") || txtPassword.getText().equals(""))
            {
                JOptionPane.showMessageDialog(frmAdminPortal.this, "All text field input is required!");
            } else if(Period.between((LocalDate.parse((txtDOB.getText().replace("/", "-")))),
                                LocalDate.parse(new SimpleDateFormat("yyyy-MM-dd").format(new Date()))).getYears() < 18){
                JOptionPane.showMessageDialog(frmAdminPortal.this, "An employee must be 18 yrs or older!");
            } else if(txtPassword.getText().length() < 7) {
                JOptionPane.showMessageDialog(frmAdminPortal.this, "Password characters must exceed seven characters.");
            } else {
                new BostonEmployeeManagement().mUpdateRecordDetails(mUpdateRecord());
                btnUpdate.setText("Update");
                btnAdd.setEnabled(true);
                btnDelete.setEnabled(true);
            }
        }
    }//GEN-LAST:event_btnUpdateActionPerformed

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        clsTransactionDialog clsDialog = new clsTransactionDialog("Delete");
        clsDialog.setVisible(true);
    }//GEN-LAST:event_btnDeleteActionPerformed

    private void btnRefreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRefreshActionPerformed
        this.dispose();
        frmAdminPortal frmPortal = new frmAdminPortal();
        frmPortal.show();
    }//GEN-LAST:event_btnRefreshActionPerformed

    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        this.hide();
    }//GEN-LAST:event_btnCloseActionPerformed

    private void btnDeleteAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteAllActionPerformed
        new BostonEmployeeManagement().mDeleteAllRecordDetails(mDeleteAll());
    }//GEN-LAST:event_btnDeleteAllActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(frmAdminPortal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(frmAdminPortal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(frmAdminPortal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(frmAdminPortal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new frmAdminPortal().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAdd;
    private javax.swing.JButton btnClose;
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnDeleteAll;
    private javax.swing.JButton btnRefresh;
    private javax.swing.JButton btnUpdate;
    private javax.swing.JDesktopPane dskPane;
    private javax.swing.JPanel jpEmployeeDetailsView;
    private javax.swing.JPanel jpEmployeeInformation;
    private javax.swing.JScrollPane jsScrollPane;
    private javax.swing.JLabel lblCommands;
    private javax.swing.JLabel lblDateOfBirth;
    private javax.swing.JLabel lblDepartment;
    private javax.swing.JLabel lblEmployeeInfo;
    private javax.swing.JLabel lblEmployeeMgmtSys;
    private javax.swing.JLabel lblFirstname;
    private javax.swing.JLabel lblJobTitle;
    private javax.swing.JLabel lblLastName;
    private javax.swing.JLabel lblPassword;
    private javax.swing.JLabel lblPhoneNumber;
    private javax.swing.JLabel lblUsername;
    private javax.swing.JTable tblEmployee;
    private javax.swing.JTextField txtDOB;
    private javax.swing.JTextField txtDepartment;
    private javax.swing.JTextField txtFirstname;
    private javax.swing.JTextField txtJobTitle;
    private javax.swing.JTextField txtLastname;
    private javax.swing.JTextField txtPassword;
    private javax.swing.JTextField txtPhoneNo;
    private javax.swing.JTextField txtUsername;
    // End of variables declaration//GEN-END:variables
}

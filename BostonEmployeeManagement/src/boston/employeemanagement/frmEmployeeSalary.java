/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package boston.employeemanagement;

import java.awt.*;
import java.math.RoundingMode;
import java.text.*;
import java.util.Date;
import javax.swing.*;

/**
 *
 * @author Sanele
 */
public class frmEmployeeSalary extends javax.swing.JFrame {

    /**
     * Creates new form frmEmployeeSalary
     */
    public frmEmployeeSalary() {
        initComponents();
        this.setTitle("Calculate Employee Salary"); //Sets the title of this form
        this.setLocationRelativeTo(null); //Displays the form at center screen
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        dskPane.setBackground(new Color(30, 30, 30)); //Sets background colour using rgb colour values 
        frmAdminPortal frmPortal = new frmAdminPortal(); //An instance of frmAdminPortal is created
        frmPortal.new clsTransactionDialog("Salary").setVisible(true); //An inner class object of class frmAdminPortal is instantiated
        mSetEmployeeInformationValues(strSelectedEmployee); //A call to a method that sets employee values to the GUI
        frmPortal.mTable("SELECT Firstname, Lastname, "+new BostonEmployeeManagement().mGetNumericField(
                "SELECT BasicSalary FROM tblSalary WHERE EmployeeID ='"+txtEmployeeID.getText()+"'")+", "
                +new BostonEmployeeManagement().mGetNumericField("SELECT SalaryAmmount FROM tblSalary WHERE EmployeeID ='"+txtEmployeeID.getText()+"'")
                +" FROM tblEmployee WHERE ID='"+txtEmployeeID.getText()+"'", tblSalary, "tblSalary");
        btnSave.setEnabled(false);
        mSetDefaultValues();
        this.setVisible(true);
    }

    static String strSelectedEmployee; //A static variable to hold the value of the selected option in a combo box
    
    //A method to set default values to numeric fields used to calculate an employee salary
    private void mSetDefaultValues(){
        //A JTextField array to hold references to JTextField objects
        //used in calculating an employee salary
        JTextField[] arrSalaryCalculatingTextFields = new JTextField[]{
            txtOvertime, txtTotalOvertime, txtMedical, txtOther, txtBonus
        };
        for(int i = 0; i < arrSalaryCalculatingTextFields.length; i++) {
            arrSalaryCalculatingTextFields[i].setText("0");
        }
    }
    
    //A method that retrieves data from the database and set the appropriate 
    //data to the GUI text fields. The method takes a string argument, in this case
    //a value of a selected option in a combo box
    private void mSetEmployeeInformationValues(String strSelectedEmployee) {
        try{
            int intEmployeeID = new BostonEmployeeManagement().mGetNumericField("SELECT ID FROM tblEmployee WHERE Firstname ='"+
                    strSelectedEmployee.substring(0, strSelectedEmployee.indexOf(" ")).trim()
                                    +"' AND Username='"+strSelectedEmployee.substring(strSelectedEmployee.indexOf(" "), strSelectedEmployee.length()).trim()+"'");
            System.out.println(intEmployeeID);
            String[] arrEmployeeInfo = new BostonEmployeeManagement().mFetchEmployeeRecords("SELECT Firstname, Lastname, DateOfBirth,"
                    + " Department FROM tblEmployee WHERE ID='"+ intEmployeeID+"'");
            
            String[] arrEmployeeSalaryInfo = new BostonEmployeeManagement().mFetchEmployeeRecords(
                    "SELECT BasicSalary, RateOfPerHour FROM tblSalary WHERE EmployeeID ='"+intEmployeeID+"'");
            
            txtEmployeeID.setText(String.valueOf(intEmployeeID));
            txtFirstname.setText(arrEmployeeInfo[1]);
            txtSurname.setText(arrEmployeeInfo[2]);
            txtDOB.setText(arrEmployeeInfo[3]);
            txtDepartment.setText(arrEmployeeInfo[4]);
            txtBasicSalary.setText(arrEmployeeSalaryInfo[1]);
            txtRatePerHour.setText(arrEmployeeSalaryInfo[2]);
        } catch(Exception e){
            JOptionPane.showMessageDialog(frmEmployeeSalary.this, "You did not select an employee to calculate salary for!");
        }
    }
    
    //A method that accepts an argument of type double and return a variable
    //of type double which is the same double value passed but rounded up to two decimal places
    private double mFormat(double var)
    {
        DecimalFormat dformat = new DecimalFormat("#.##");
        dformat.setRoundingMode(RoundingMode.UP);
        var = Double.parseDouble(dformat.format(var));
        return var;
    }
    
    //A method to calculate an employee salary by doing deductions
    //and additions with a number of variables to an employee basic 
    private double mCalculateSalary() {
        double dblSalary = Double.parseDouble(txtBasicSalary.getText());
        try{
            dblSalary += mFormat(dblSalary * (Double.parseDouble(txtBonus.getText()) / 100));
            dblSalary += mFormat((Double.parseDouble(txtTotalOvertime.getText()) * Double.parseDouble(txtRatePerHour.getText())));
            dblSalary -= mFormat(Double.parseDouble(txtMedical.getText()));
            dblSalary -= mFormat((Double.parseDouble(txtOther.getText())));
        } catch(Exception e) {
            JOptionPane.showMessageDialog(frmEmployeeSalary.this, e.getMessage());
        }
        return dblSalary;
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
        lblEmployeeInfo = new javax.swing.JLabel();
        lblFirstname = new javax.swing.JLabel();
        txtFirstname = new javax.swing.JTextField();
        lblSurname = new javax.swing.JLabel();
        txtSurname = new javax.swing.JTextField();
        lblDateOfBirth = new javax.swing.JLabel();
        txtDOB = new javax.swing.JTextField();
        lblBasicSalary = new javax.swing.JLabel();
        txtBasicSalary = new javax.swing.JTextField();
        lblDepartment = new javax.swing.JLabel();
        txtDepartment = new javax.swing.JTextField();
        lblEmployeeID = new javax.swing.JLabel();
        txtEmployeeID = new javax.swing.JTextField();
        jpEnterSalaryAmounts = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        lblOverime = new javax.swing.JLabel();
        txtOvertime = new javax.swing.JTextField();
        lblMedical = new javax.swing.JLabel();
        txtMedical = new javax.swing.JTextField();
        lblBonus = new javax.swing.JLabel();
        txtBonus = new javax.swing.JTextField();
        lblOther = new javax.swing.JLabel();
        txtOther = new javax.swing.JTextField();
        lblTotalOvertime = new javax.swing.JLabel();
        txtTotalOvertime = new javax.swing.JTextField();
        lblRatePerHour = new javax.swing.JLabel();
        txtRatePerHour = new javax.swing.JTextField();
        jpTableAndButtons = new javax.swing.JPanel();
        jsScrollPane = new javax.swing.JScrollPane();
        tblSalary = new javax.swing.JTable();
        btnCalculate = new javax.swing.JButton();
        btnSave = new javax.swing.JButton();
        btnClear = new javax.swing.JButton();
        lblOutput = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        dskPane.setForeground(new java.awt.Color(30, 30, 30));

        jpEmployeeInformation.setBackground(new java.awt.Color(255, 255, 255));

        lblEmployeeInfo.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblEmployeeInfo.setText("Employee Information");

        lblFirstname.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        lblFirstname.setText("Firstname");

        lblSurname.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        lblSurname.setText("Surname");

        lblDateOfBirth.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        lblDateOfBirth.setText("Date Of Birth");

        lblBasicSalary.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        lblBasicSalary.setText("Basic Salary");

        lblDepartment.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        lblDepartment.setText("Department");

        lblEmployeeID.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        lblEmployeeID.setText("EmployeeID");

        txtEmployeeID.setToolTipText("");

        javax.swing.GroupLayout jpEmployeeInformationLayout = new javax.swing.GroupLayout(jpEmployeeInformation);
        jpEmployeeInformation.setLayout(jpEmployeeInformationLayout);
        jpEmployeeInformationLayout.setHorizontalGroup(
            jpEmployeeInformationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpEmployeeInformationLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(jpEmployeeInformationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblEmployeeInfo)
                    .addGroup(jpEmployeeInformationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jpEmployeeInformationLayout.createSequentialGroup()
                            .addComponent(lblSurname)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txtSurname, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jpEmployeeInformationLayout.createSequentialGroup()
                            .addComponent(lblDateOfBirth)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txtDOB, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jpEmployeeInformationLayout.createSequentialGroup()
                            .addGroup(jpEmployeeInformationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jpEmployeeInformationLayout.createSequentialGroup()
                                    .addComponent(lblFirstname)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jpEmployeeInformationLayout.createSequentialGroup()
                                    .addComponent(lblEmployeeID)
                                    .addGap(118, 118, 118)))
                            .addGroup(jpEmployeeInformationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(txtFirstname)
                                .addComponent(txtEmployeeID, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE)))
                        .addGroup(jpEmployeeInformationLayout.createSequentialGroup()
                            .addGroup(jpEmployeeInformationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(lblBasicSalary)
                                .addComponent(lblDepartment))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jpEmployeeInformationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(txtDepartment, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE)
                                .addComponent(txtBasicSalary)))))
                .addContainerGap(76, Short.MAX_VALUE))
        );
        jpEmployeeInformationLayout.setVerticalGroup(
            jpEmployeeInformationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpEmployeeInformationLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblEmployeeInfo)
                .addGap(27, 27, 27)
                .addGroup(jpEmployeeInformationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblEmployeeID)
                    .addComponent(txtEmployeeID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jpEmployeeInformationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblFirstname)
                    .addComponent(txtFirstname, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jpEmployeeInformationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblSurname)
                    .addComponent(txtSurname, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jpEmployeeInformationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblDateOfBirth)
                    .addComponent(txtDOB, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jpEmployeeInformationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblBasicSalary, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtBasicSalary, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jpEmployeeInformationLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblDepartment)
                    .addComponent(txtDepartment, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(19, Short.MAX_VALUE))
        );

        jpEnterSalaryAmounts.setBackground(new java.awt.Color(255, 255, 255));

        jLabel9.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel9.setText("Please Enter The Amounts");

        lblOverime.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        lblOverime.setText("Overtime");

        lblMedical.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        lblMedical.setText("Medical");

        lblBonus.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        lblBonus.setText("Bonus");

        lblOther.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        lblOther.setText("Other");
        lblOther.setToolTipText("");

        lblTotalOvertime.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        lblTotalOvertime.setText("Total Overtime");

        lblRatePerHour.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        lblRatePerHour.setText("Rate Per Hour");

        javax.swing.GroupLayout jpEnterSalaryAmountsLayout = new javax.swing.GroupLayout(jpEnterSalaryAmounts);
        jpEnterSalaryAmounts.setLayout(jpEnterSalaryAmountsLayout);
        jpEnterSalaryAmountsLayout.setHorizontalGroup(
            jpEnterSalaryAmountsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpEnterSalaryAmountsLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jpEnterSalaryAmountsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel9)
                    .addGroup(jpEnterSalaryAmountsLayout.createSequentialGroup()
                        .addGroup(jpEnterSalaryAmountsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblOverime)
                            .addComponent(lblMedical)
                            .addComponent(lblBonus)
                            .addComponent(lblOther))
                        .addGap(64, 64, 64)
                        .addGroup(jpEnterSalaryAmountsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtOvertime)
                            .addComponent(txtMedical)
                            .addComponent(txtBonus)
                            .addComponent(txtOther, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addGroup(jpEnterSalaryAmountsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblTotalOvertime)
                            .addComponent(lblRatePerHour))
                        .addGap(18, 18, 18)
                        .addGroup(jpEnterSalaryAmountsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtRatePerHour, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE)
                            .addComponent(txtTotalOvertime))))
                .addContainerGap(37, Short.MAX_VALUE))
        );
        jpEnterSalaryAmountsLayout.setVerticalGroup(
            jpEnterSalaryAmountsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpEnterSalaryAmountsLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel9)
                .addGap(18, 18, 18)
                .addGroup(jpEnterSalaryAmountsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblOverime)
                    .addComponent(txtOvertime, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblTotalOvertime)
                    .addComponent(txtTotalOvertime, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jpEnterSalaryAmountsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblMedical)
                    .addGroup(jpEnterSalaryAmountsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtMedical, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lblRatePerHour)
                        .addComponent(txtRatePerHour, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(jpEnterSalaryAmountsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblBonus)
                    .addComponent(txtBonus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jpEnterSalaryAmountsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblOther)
                    .addComponent(txtOther, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jpTableAndButtons.setBackground(new java.awt.Color(255, 255, 255));

        tblSalary.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Firstname", "Lastname", "Basic Salary", "Bonus Salary"
            }
        ));
        jsScrollPane.setViewportView(tblSalary);

        btnCalculate.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnCalculate.setText("Calculate");
        btnCalculate.setToolTipText("");
        btnCalculate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCalculateActionPerformed(evt);
            }
        });

        btnSave.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnSave.setText("Save");
        btnSave.setToolTipText("");
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });

        btnClear.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        btnClear.setText("Clear");
        btnClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClearActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jpTableAndButtonsLayout = new javax.swing.GroupLayout(jpTableAndButtons);
        jpTableAndButtons.setLayout(jpTableAndButtonsLayout);
        jpTableAndButtonsLayout.setHorizontalGroup(
            jpTableAndButtonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpTableAndButtonsLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jsScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 492, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(jpTableAndButtonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jpTableAndButtonsLayout.createSequentialGroup()
                        .addGap(199, 199, 199)
                        .addGroup(jpTableAndButtonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(btnClear, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnSave, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnCalculate, javax.swing.GroupLayout.DEFAULT_SIZE, 150, Short.MAX_VALUE)))
                    .addGroup(jpTableAndButtonsLayout.createSequentialGroup()
                        .addGap(50, 50, 50)
                        .addComponent(lblOutput, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jpTableAndButtonsLayout.setVerticalGroup(
            jpTableAndButtonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpTableAndButtonsLayout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(jpTableAndButtonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jsScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 199, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jpTableAndButtonsLayout.createSequentialGroup()
                        .addComponent(lblOutput, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(58, 58, 58)
                        .addComponent(btnCalculate)
                        .addGap(18, 18, 18)
                        .addComponent(btnSave)
                        .addGap(18, 18, 18)
                        .addComponent(btnClear)))
                .addContainerGap(56, Short.MAX_VALUE))
        );

        dskPane.setLayer(jpEmployeeInformation, javax.swing.JLayeredPane.DEFAULT_LAYER);
        dskPane.setLayer(jpEnterSalaryAmounts, javax.swing.JLayeredPane.DEFAULT_LAYER);
        dskPane.setLayer(jpTableAndButtons, javax.swing.JLayeredPane.DEFAULT_LAYER);

        javax.swing.GroupLayout dskPaneLayout = new javax.swing.GroupLayout(dskPane);
        dskPane.setLayout(dskPaneLayout);
        dskPaneLayout.setHorizontalGroup(
            dskPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(dskPaneLayout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addGroup(dskPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jpTableAndButtons, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(dskPaneLayout.createSequentialGroup()
                        .addComponent(jpEmployeeInformation, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jpEnterSalaryAmounts, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(21, Short.MAX_VALUE))
        );
        dskPaneLayout.setVerticalGroup(
            dskPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(dskPaneLayout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addGroup(dskPaneLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jpEmployeeInformation, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jpEnterSalaryAmounts, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addComponent(jpTableAndButtons, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(dskPane)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(dskPane, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnCalculateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCalculateActionPerformed
        if(txtBasicSalary.getText().equals("")){
            JOptionPane.showMessageDialog(frmEmployeeSalary.this, "Provide Basic Salary");
            txtBasicSalary.requestFocusInWindow();
        } else if(txtBonus.getText().equals("")){
            JOptionPane.showMessageDialog(frmEmployeeSalary.this, "Provide Bonus Percentage");
        } else if(txtOvertime.getText().equals("")){
            JOptionPane.showMessageDialog(frmEmployeeSalary.this, "Provide value of overtime hours");
            txtOvertime.requestFocusInWindow();
        } else if(txtTotalOvertime.getText().equals("")) {
            JOptionPane.showMessageDialog(frmEmployeeSalary.this, "Provide value of total overtime hours");
            txtTotalOvertime.requestFocusInWindow();
        } else if(!txtTotalOvertime.getText().equals(txtOvertime.getText())) {
            JOptionPane.showMessageDialog(frmEmployeeSalary.this, "Overtime hours must be equal to total overtime hours");
        } else if(txtMedical.getText().equals("")){
            JOptionPane.showMessageDialog(frmEmployeeSalary.this, "Provide amount of medical deductions");
        } else if(txtOther.getText().equals("")) {
            JOptionPane.showMessageDialog(frmEmployeeSalary.this, "Provide amount for other deductions");
            txtOther.requestFocusInWindow();
        } else {
            lblOutput.setText("Total Amount R"+mCalculateSalary());
            btnSave.setEnabled(true);
        }
    }//GEN-LAST:event_btnCalculateActionPerformed

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        String[] arrSalaryDetails = new BostonEmployeeManagement().mFetchEmployeeRecords("SELECT EmployeeID, BasicSalary, PaymentMethod,"
                + "RateOfPerHour, DateOfPay, SalaryAmmount, BankAccountNumber, DateOfEntry FROM tblSalary WHERE EmployeeID='"+txtEmployeeID.getText()+"'");
        
        String strQuery = "INSERT INTO tblSalary(EmployeeID, BasicSalary, PaymentMethod,"
                + "RateOfPerHour, DateOfPay, SalaryAmmount, BankAccountNumber, DateOfEntry)"
                + "VALUES('"+arrSalaryDetails[1]+"','"+arrSalaryDetails[2]+"','"+arrSalaryDetails[3]+"','"+
                arrSalaryDetails[4]+"','"+ new SimpleDateFormat("yyyy/MM/dd").format(new Date()) +"','"+mCalculateSalary()+"','"+
                arrSalaryDetails[7]+"','"+arrSalaryDetails[8]+"')";
        
        new BostonEmployeeManagement().mCreateRecord(strQuery.trim(), btnSave.getText());
        btnSave.setEnabled(false);
    }//GEN-LAST:event_btnSaveActionPerformed

    private void btnClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearActionPerformed
        this.dispose();
        new frmEmployeeSalary().setVisible(true);
    }//GEN-LAST:event_btnClearActionPerformed

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
            java.util.logging.Logger.getLogger(frmEmployeeSalary.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(frmEmployeeSalary.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(frmEmployeeSalary.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(frmEmployeeSalary.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new frmEmployeeSalary().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCalculate;
    private javax.swing.JButton btnClear;
    private javax.swing.JButton btnSave;
    private javax.swing.JDesktopPane dskPane;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jpEmployeeInformation;
    private javax.swing.JPanel jpEnterSalaryAmounts;
    private javax.swing.JPanel jpTableAndButtons;
    private javax.swing.JScrollPane jsScrollPane;
    private javax.swing.JLabel lblBasicSalary;
    private javax.swing.JLabel lblBonus;
    private javax.swing.JLabel lblDateOfBirth;
    private javax.swing.JLabel lblDepartment;
    private javax.swing.JLabel lblEmployeeID;
    private javax.swing.JLabel lblEmployeeInfo;
    private javax.swing.JLabel lblFirstname;
    private javax.swing.JLabel lblMedical;
    private javax.swing.JLabel lblOther;
    private javax.swing.JLabel lblOutput;
    private javax.swing.JLabel lblOverime;
    private javax.swing.JLabel lblRatePerHour;
    private javax.swing.JLabel lblSurname;
    private javax.swing.JLabel lblTotalOvertime;
    private javax.swing.JTable tblSalary;
    private javax.swing.JTextField txtBasicSalary;
    private javax.swing.JTextField txtBonus;
    private javax.swing.JTextField txtDOB;
    private javax.swing.JTextField txtDepartment;
    private javax.swing.JTextField txtEmployeeID;
    private javax.swing.JTextField txtFirstname;
    private javax.swing.JTextField txtMedical;
    private javax.swing.JTextField txtOther;
    private javax.swing.JTextField txtOvertime;
    private javax.swing.JTextField txtRatePerHour;
    private javax.swing.JTextField txtSurname;
    private javax.swing.JTextField txtTotalOvertime;
    // End of variables declaration//GEN-END:variables
}

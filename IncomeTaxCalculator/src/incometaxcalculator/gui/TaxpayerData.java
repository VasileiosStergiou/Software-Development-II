package incometaxcalculator.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.ListSelectionModel;
import javax.swing.MenuElement;
import javax.swing.MenuSelectionManager;
import javax.swing.border.EmptyBorder;

import incometaxcalculator.data.management.Receipt;
import incometaxcalculator.data.management.TaxpayerManager;
import incometaxcalculator.exceptions.ReceiptAlreadyExistsException;
import incometaxcalculator.exceptions.WrongFileFormatException;
import incometaxcalculator.exceptions.WrongReceiptDateException;
import incometaxcalculator.exceptions.WrongReceiptKindException;

public class TaxpayerData extends JFrame {

  private static final short ENTERTAINMENT = 0;
  private static final short BASIC = 1;
  private static final short TRAVEL = 2;
  private static final short HEALTH = 3;
  private static final short OTHER = 4;
  private JPanel contentPane;
  private String outputFileFormat;
  private String outputLogFileFormat;
  
  JSpinner receiptID;
  JTextField date;
  JComboBox receipt_categories_combobox;
  JTextField amount;
  JSpinner number;
  
  public boolean checkTextFields(String company, String country,String city,String street) {
    if (company.equals("")||
        country.equals("")||
        city.equals("")||
        street.equals("")) {
      return false;
    }
    return true;
  }
  
  public JComboBox addComboboxIntoPanel(JPanel contentPane,int locationOnXAxis,int locationOnYAxis, 
                                   int menuLocationY,String labelText) {
    String [] infoFileFormats = {"txt","xml"};
    
    JLabel label = new JLabel(labelText);
    Dimension size = label.getPreferredSize();
    label.setBounds(locationOnXAxis, locationOnYAxis, size.width, size.height);
    contentPane.add(label);
    
    JComboBox infoFileFormatsMenu = new JComboBox(infoFileFormats);
    
    infoFileFormatsMenu.setSize(100, 30);
    infoFileFormatsMenu.setLocation(333, menuLocationY);
    contentPane.add(infoFileFormatsMenu);
    return infoFileFormatsMenu;
  }
  
  public void addTextFieldsIntoPanel(JPanel receiptImporterPanel,JTextField [] textFields,String [] labels) {
    for (int i =0; i< textFields.length;i++) {
      receiptImporterPanel.add(new JLabel(labels[i]));
      receiptImporterPanel.add(textFields[i]);
    }
    receiptImporterPanel.add(new JLabel("Receipt ID:"));
    receiptImporterPanel.add(receiptID);
    receiptImporterPanel.add(new JLabel("Kind:"));
    receiptImporterPanel.add(receipt_categories_combobox);
    receiptImporterPanel.add(new JLabel("Amount:"));
    receiptImporterPanel.add(amount);
    receiptImporterPanel.add(new JLabel("Number:"));
    receiptImporterPanel.add(number);
  }
  
  public TaxpayerData(int taxRegistrationNumber, TaxpayerManager taxpayerManager) {
    setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    setBounds(200, 100, 450, 420);
    contentPane = new JPanel();
    contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
    setContentPane(contentPane);
    contentPane.setLayout(null);

    DefaultListModel<Integer> receiptsModel = new DefaultListModel<Integer>();
    
    String [] infoFileFormats = {"txt","xml"};
    String [] logFileFormats = {"txt","xml"};

    JComboBox infoFileFormatsMenu = addComboboxIntoPanel(contentPane,333,30,50,"Output File Format");
    JComboBox logFileFormatsMenu = addComboboxIntoPanel(contentPane,333,90,110,"Log File Format");
    
    JList<Integer> receiptsList = new JList<Integer>(receiptsModel);
    receiptsList.setBackground(new Color(153, 204, 204));
    receiptsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    receiptsList.setSelectedIndex(0);
    receiptsList.setVisibleRowCount(3);

    JScrollPane receiptsListScrollPane = new JScrollPane(receiptsList);
    receiptsListScrollPane.setSize(150, 200);
    receiptsListScrollPane.setLocation(100, 170);
    contentPane.add(receiptsListScrollPane);

    HashMap<Integer, Receipt> receipts = taxpayerManager.getReceiptHashMap(taxRegistrationNumber);
    Iterator<HashMap.Entry<Integer, Receipt>> iterator = receipts.entrySet().iterator();

    while (iterator.hasNext()) {
      HashMap.Entry<Integer, Receipt> entry = iterator.next();
      Receipt receipt = entry.getValue();
      receiptsModel.addElement(receipt.getId());
    }
    
    JButton btnAddReceipt = new JButton("Add Receipt");
    btnAddReceipt.setToolTipText("Click to add a new receipt on the costumer");
    btnAddReceipt.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        JPanel receiptImporterPanel = new JPanel(new GridLayout(9, 2));
        receiptID = new JSpinner();
        date = new JTextField(16);
        date.setToolTipText("<html>" + "Date Format:" + "<br>" + "day/month/year" + "</html>"); 
        // ---------- NEW----------------
        String [] receipt_categories = {"Entertainment","Basic","Travel","Health","Other"};
        receipt_categories_combobox=new JComboBox(receipt_categories);
        amount = new JTextField(16);
        amount.setToolTipText("<html>" + "The amount must be a floating point number" + "<br>" + "E.g 1.0" + "</html>"); 
        JTextField company = new JTextField(16);
        JTextField country = new JTextField(16);
        JTextField city = new JTextField(16);
        JTextField street = new JTextField(16);
        number = new JSpinner();
        int receiptIDValue, numberValue;
        float amountValue = 0;
        boolean validAmount = false;
        String dateValue, kindValue, companyValue, countryValue;
        String cityValue, streetValue;
        
        String [] labels = {"Date:","Amount:","Company:","Country:","Street:"};
        JTextField [] textFields = {date,amount,company,country,street};
        
        receiptImporterPanel.add(new JLabel("Receipt ID:"));
        receiptImporterPanel.add(receiptID);
        receiptImporterPanel.add(new JLabel("Date:"));
        receiptImporterPanel.add(date);
        receiptImporterPanel.add(new JLabel("Kind:"));
        receiptImporterPanel.add(receipt_categories_combobox);
        receiptImporterPanel.add(new JLabel("Amount:"));
        receiptImporterPanel.add(amount);
        receiptImporterPanel.add(new JLabel("Company:"));
        receiptImporterPanel.add(company);
        receiptImporterPanel.add(new JLabel("Country:"));
        receiptImporterPanel.add(country);
        receiptImporterPanel.add(new JLabel("City:"));
        receiptImporterPanel.add(city);
        receiptImporterPanel.add(new JLabel("Street:"));
        receiptImporterPanel.add(street);
        receiptImporterPanel.add(new JLabel("Number:"));
        receiptImporterPanel.add(number);  
        
        int op = JOptionPane.showConfirmDialog(null, receiptImporterPanel, "",
            JOptionPane.OK_CANCEL_OPTION);
        if (op == 0) {
          receiptIDValue = (Integer) receiptID.getValue();
          dateValue = date.getText();
          kindValue = receipt_categories_combobox.getSelectedItem().toString();
          
          try {
            amountValue = Float.parseFloat(amount.getText());
            if (amountValue >= 0) {
              validAmount = true;
            }
            else {
              throw new Exception("Negative number entered");
            }
          } catch(Exception e1){
            JOptionPane.showMessageDialog(null,"Amount is not a number or a negative amount was entered");
          }

          companyValue = company.getText();
          countryValue = country.getText();
          cityValue = city.getText();
          streetValue = street.getText();
          numberValue = (Integer) number.getValue();
          
          boolean nonEmptyFieldValues = checkTextFields(companyValue,countryValue,cityValue,streetValue);
          
          if (validAmount && nonEmptyFieldValues) {
            outputFileFormat = infoFileFormatsMenu.getSelectedItem().toString();
            try {
              if(taxpayerManager.addReceipt(receiptIDValue, dateValue, amountValue, kindValue,
                   companyValue, countryValue, cityValue, streetValue, numberValue,
                   taxRegistrationNumber,outputFileFormat)) {
                 receiptsModel.addElement(receiptIDValue);
                }else {
                    JOptionPane.showMessageDialog(null,"Receipt ID " +receiptIDValue+" already exists in taxpayer "+ taxRegistrationNumber);
              }
            } catch (IOException e1) {
              JOptionPane.showMessageDialog(null,
                 "Problem with opening file ." + receiptIDValue + "_INFO.txt");
            } catch (WrongReceiptKindException e1) {
              JOptionPane.showMessageDialog(null, "Please check receipts kind and try again.");
            } catch (WrongReceiptDateException e1) {
              JOptionPane.showMessageDialog(null,
                "Please make sure your date " + "is DD/MM/YYYY and try again.");
            } catch (ReceiptAlreadyExistsException e1) {
              JOptionPane.showMessageDialog(null, "Receipt ID already exists.");
           }       
         }
         else {
            JOptionPane.showMessageDialog(null, "Amount is not number and/or empty text fields");
         }
       }
     }
    });
    btnAddReceipt.setBounds(0, 0, 102, 23);
    contentPane.add(btnAddReceipt);

    JButton btnDeleteReceipt = new JButton("Delete Receipt");
    btnDeleteReceipt.setToolTipText("Select a receipt ID from the list to delete it");
    btnDeleteReceipt.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
         int receiptIDValue = 0;
         try {
             receiptIDValue = receiptsList.getSelectedValue();
          try {
            outputFileFormat = infoFileFormatsMenu.getSelectedItem().toString();
            taxpayerManager.removeReceipt(receiptIDValue,taxRegistrationNumber,outputFileFormat);
            receiptsModel.removeElement(receiptIDValue);             
           } catch (IOException e1) {
             JOptionPane.showMessageDialog(null,
                  "Problem with opening file ." + receiptIDValue + "_INFO.txt");
           } catch (WrongReceiptKindException e1) {
             JOptionPane.showMessageDialog(null, "Please check receipt's kind and try again.");
           }
        }
        catch (NullPointerException npe){
        JOptionPane.showMessageDialog(null, "You must select an ID from the list to delete first.");
       }
      }
    });
    btnDeleteReceipt.setBounds(100, 0, 120, 23);
    contentPane.add(btnDeleteReceipt);

    JButton btnViewReport = new JButton("View Report");
    btnViewReport.setToolTipText("Click to view the stats from taxes and receipts");
    btnViewReport.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        ChartDisplay.createBarChart(taxpayerManager.getTaxpayerBasicTax(taxRegistrationNumber),
            taxpayerManager.getTaxpayerVariationTaxOnReceipts(taxRegistrationNumber),
            taxpayerManager.getTaxpayerTotalTax(taxRegistrationNumber));
        ChartDisplay.createPieChart(
            taxpayerManager.getTaxpayerAmountOfReceiptKind(taxRegistrationNumber, ENTERTAINMENT),
            taxpayerManager.getTaxpayerAmountOfReceiptKind(taxRegistrationNumber, BASIC),
            taxpayerManager.getTaxpayerAmountOfReceiptKind(taxRegistrationNumber, TRAVEL),
            taxpayerManager.getTaxpayerAmountOfReceiptKind(taxRegistrationNumber, HEALTH),
            taxpayerManager.getTaxpayerAmountOfReceiptKind(taxRegistrationNumber, OTHER));
      }
    });
    btnViewReport.setBounds(214, 0, 109, 23);
    contentPane.add(btnViewReport);

    JButton btnSaveData = new JButton("Save Log Data");
    btnSaveData.setToolTipText("Click to save taxes and receipts stats into a txt or xml file");
    btnSaveData.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        outputLogFileFormat = logFileFormatsMenu.getSelectedItem().toString();
        String taxRegistrationNumberFile = String.valueOf(taxRegistrationNumber);
        try {
           try {
              taxpayerManager.saveLogFile(taxRegistrationNumber, outputLogFileFormat);
            } catch (IOException e1) {
              JOptionPane.showMessageDialog(null,
                 "Problem with opening file ." + taxRegistrationNumber + "_LOG."+outputLogFileFormat);
            } catch (WrongFileFormatException e1) {
               JOptionPane.showMessageDialog(null, "Wrong file format");
            }          
        } catch (NumberFormatException e1) {
          JOptionPane.showMessageDialog(null,
           "The tax registration number must have only digits.");
        }
     }
    });
    btnSaveData.setBounds(322, 0, 102, 23);
    contentPane.add(btnSaveData);

    JTextPane txtpnName = new JTextPane();
    txtpnName.setEditable(false);
    txtpnName.setText("Name :");
    txtpnName.setBounds(10, 34, 92, 20);
    contentPane.add(txtpnName);

    JTextPane txtpnTrn = new JTextPane();
    txtpnTrn.setEditable(false);
    txtpnTrn.setText("TRN :");
    txtpnTrn.setBounds(10, 65, 92, 20);
    contentPane.add(txtpnTrn);

    JTextPane txtpnStatus = new JTextPane();
    txtpnStatus.setEditable(false);
    txtpnStatus.setText("Status :");
    txtpnStatus.setBounds(10, 96, 92, 20);
    contentPane.add(txtpnStatus);

    JTextPane txtpnIncome = new JTextPane();
    txtpnIncome.setEditable(false);
    txtpnIncome.setText("Income :");
    txtpnIncome.setBounds(10, 127, 92, 20);
    contentPane.add(txtpnIncome);

    JTextArea taxpayerName = new JTextArea();
    taxpayerName.setFont(new Font("Tahoma", Font.PLAIN, 11));
    taxpayerName.setEditable(false);
    taxpayerName.setBounds(110, 34, 213, 20);
    taxpayerName.setText(taxpayerManager.getTaxpayerName(taxRegistrationNumber));
    contentPane.add(taxpayerName);

    JTextArea taxpayerTRN = new JTextArea();
    taxpayerTRN.setFont(new Font("Tahoma", Font.PLAIN, 11));
    taxpayerTRN.setEditable(false);
    taxpayerTRN.setBounds(110, 65, 213, 20);
    taxpayerTRN.setText(taxRegistrationNumber + "");
    contentPane.add(taxpayerTRN);

    JTextArea taxpayerStatus = new JTextArea();
    taxpayerStatus.setFont(new Font("Tahoma", Font.PLAIN, 11));
    taxpayerStatus.setEditable(false);
    taxpayerStatus.setBounds(110, 96, 213, 20);
    taxpayerStatus.setText(taxpayerManager.getTaxpayerStatus(taxRegistrationNumber));
    contentPane.add(taxpayerStatus);

    JTextArea taxpayerIncome = new JTextArea();
    taxpayerIncome.setFont(new Font("Tahoma", Font.PLAIN, 11));
    taxpayerIncome.setEditable(false);
    taxpayerIncome.setBounds(112, 127, 213, 20);
    taxpayerIncome.setText(taxpayerManager.getTaxpayerIncome(taxRegistrationNumber));
    contentPane.add(taxpayerIncome);

    JTextPane txtpnReceipts = new JTextPane();
    txtpnReceipts.setEditable(false);
    txtpnReceipts.setText("Receipts :");
    txtpnReceipts.setBounds(10, 170, 80, 20);
    contentPane.add(txtpnReceipts);
  }
}
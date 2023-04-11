package incometaxcalculator.data.management;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import incometaxcalculator.data.management.TaxpayerCreatorFactory;

import incometaxcalculator.data.io.FileReader;
import incometaxcalculator.data.io.FileWriter;
import incometaxcalculator.data.io.FileWriterFactory;
import incometaxcalculator.data.io.LoadTaxpayerFactory;
import incometaxcalculator.data.io.LogFileFactory;
import incometaxcalculator.data.io.TXTFileReader;
import incometaxcalculator.data.io.TXTInfoWriter;
import incometaxcalculator.data.io.TXTLogWriter;
import incometaxcalculator.data.io.XMLFileReader;
import incometaxcalculator.data.io.XMLInfoWriter;
import incometaxcalculator.data.io.XMLLogWriter;
import incometaxcalculator.exceptions.ReceiptAlreadyExistsException;
import incometaxcalculator.exceptions.WrongFileEndingException;
import incometaxcalculator.exceptions.WrongFileFormatException;
import incometaxcalculator.exceptions.WrongReceiptDateException;
import incometaxcalculator.exceptions.WrongReceiptKindException;
import incometaxcalculator.exceptions.WrongTaxpayerStatusException;

public class TaxpayerManager {

  public static HashMap<Integer, Taxpayer> taxpayerHashMap = new HashMap<Integer, Taxpayer>(0);
  public static HashMap<Integer, Integer> receiptOwnerTRN = new HashMap<Integer, Integer>(0);

  public void createTaxpayer(String fullname, int taxRegistrationNumber, String status,
      float income) throws WrongTaxpayerStatusException {
    
    TaxpayerCreatorFactory factory = new TaxpayerCreatorFactory(fullname,taxRegistrationNumber,status,income);
    Taxpayer taxpayer = factory.getAppropriateTaxpayer();
    if (taxpayer == null) {
      throw new WrongTaxpayerStatusException();
    }
    else {
      taxpayerHashMap.put(taxRegistrationNumber,taxpayer);
    }
  }
  
  public boolean createReceipt(int receiptId, String issueDate, float amount, String kind,
      String companyName, String country, String city, String street, int number,
      int taxRegistrationNumber) throws WrongReceiptKindException, WrongReceiptDateException {

    Receipt receipt = new Receipt(receiptId, issueDate, amount, kind,
        new Company(companyName, country, city, street, number));
    
    if(taxpayerHashMap.get(taxRegistrationNumber).addReceipt(receipt)) {
      return true;
    };
    return false;
  }

  public void removeTaxpayer(int taxRegistrationNumber) {
    Taxpayer taxpayer = taxpayerHashMap.get(taxRegistrationNumber);
    taxpayerHashMap.remove(taxRegistrationNumber);
    HashMap<Integer, Receipt> receiptsHashMap = taxpayer.getReceiptHashMap();
    Iterator<HashMap.Entry<Integer, Receipt>> iterator = receiptsHashMap.entrySet().iterator();
    while (iterator.hasNext()) {
      HashMap.Entry<Integer, Receipt> entry = iterator.next();
      Receipt receipt = entry.getValue();
      receiptOwnerTRN.remove(receipt.getId());
    }
  }

  public boolean addReceipt(int receiptId, String issueDate, float amount, String kind,
      String companyName, String country, String city, String street, int number,
      int taxRegistrationNumber,String outputFileFormat) throws IOException, WrongReceiptKindException,
      WrongReceiptDateException, ReceiptAlreadyExistsException {
    
    if(createReceipt(receiptId, issueDate, amount, kind, companyName, country, city, street, number,
        taxRegistrationNumber)) {
      updateFiles(taxRegistrationNumber,outputFileFormat);
      return true;
    };
    return false;
  }

  public void removeReceipt(int receiptId,int taxRegistrationNumber,String outputFileFormat) throws IOException, WrongReceiptKindException {
    taxpayerHashMap.get(taxRegistrationNumber).removeReceipt(receiptId);
    updateFiles(taxRegistrationNumber,outputFileFormat);
  }

  private void updateFiles(int taxRegistrationNumber, String outputFileFormat) throws IOException {
    FileWriterFactory file_writer_factory = new FileWriterFactory();
    FileWriter fileWriter = file_writer_factory.getFileWriter(outputFileFormat);
    if (fileWriter!= null) {
      fileWriter.generateFile(taxRegistrationNumber);
    }
  }

  public void saveLogFile(int taxRegistrationNumber, String fileFormat)
      throws IOException, WrongFileFormatException {
    LogFileFactory factory = new LogFileFactory();
    FileWriter file_writer = factory.getFileWriter(fileFormat);
    if (file_writer == null) {
      throw new WrongFileFormatException();
    }
    else {
      file_writer.generateFile(taxRegistrationNumber);
    }
  }

  public boolean receiptExists(int taxRegistrationNumber, int receiptId) {
    if(taxpayerHashMap.get(taxRegistrationNumber).getReceiptHashMap().containsKey(receiptId)) {
      return true;
    }
    return false;
  }
  
  public void loadTaxpayer(String fileName)
      throws NumberFormatException, IOException, WrongFileFormatException, WrongFileEndingException,
      WrongTaxpayerStatusException, WrongReceiptKindException, WrongReceiptDateException {

    String ending[] = fileName.split("\\.");
    LoadTaxpayerFactory factory = new LoadTaxpayerFactory();
    FileReader file_reader = factory.getAppropriateFileReader(ending[1]);
    
    if (file_reader!=null) {
      file_reader.readFile(fileName);
    }
    else {
      throw new WrongFileEndingException();
    }
  }
  
  public String getFileStructure(String fileName) {

    String [] path_structure = fileName.split("\\\\");
    String fileFormat = path_structure[path_structure.length-1];
    
    String [] file_info = fileFormat.split("_");
    
    String taxRegistrationNumber = file_info[0];
    
    return taxRegistrationNumber;
  }

  public static String getTaxpayerName(int taxRegistrationNumber) {
    return taxpayerHashMap.get(taxRegistrationNumber).getFullname();
  }

  public static String getTaxpayerStatus(int taxRegistrationNumber) {
    if (taxpayerHashMap.get(taxRegistrationNumber) instanceof MarriedFilingJointlyTaxpayer) {
      return "Married Filing Jointly";
    } else if (taxpayerHashMap
        .get(taxRegistrationNumber) instanceof MarriedFilingSeparatelyTaxpayer) {
      return "Married Filing Separately";
    } else if (taxpayerHashMap.get(taxRegistrationNumber) instanceof SingleTaxpayer) {
      return "Single";
    } else {
      return "Head of Household";
    }
  }

  public static String getTaxpayerIncome(int taxRegistrationNumber) {
    return "" + taxpayerHashMap.get(taxRegistrationNumber).getIncome();
  }

  public static double getTaxpayerVariationTaxOnReceipts(int taxRegistrationNumber) {
    return taxpayerHashMap.get(taxRegistrationNumber).getVariationTaxOnReceipts();
  }

  public static int getTaxpayerTotalReceiptsGathered(int taxRegistrationNumber) {
    return taxpayerHashMap.get(taxRegistrationNumber).getTotalReceiptsGathered();
  }

  public static float getTaxpayerAmountOfReceiptKind(int taxRegistrationNumber, short kind) {
    return taxpayerHashMap.get(taxRegistrationNumber).getAmountOfReceiptKind(kind);
  }

  public static double getTaxpayerTotalTax(int taxRegistrationNumber) {
    return taxpayerHashMap.get(taxRegistrationNumber).getTotalTax();
  }

  public static double getTaxpayerBasicTax(int taxRegistrationNumber) {
    return taxpayerHashMap.get(taxRegistrationNumber).getBasicTax();
  }

  public static HashMap<Integer, Receipt> getReceiptHashMap(int taxRegistrationNumber) {
    return taxpayerHashMap.get(taxRegistrationNumber).getReceiptHashMap();
  }
}
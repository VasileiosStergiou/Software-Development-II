package incometaxcalculator.tests;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;

import org.junit.After;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import incometaxcalculator.data.management.Address;
import incometaxcalculator.data.management.Company;
import incometaxcalculator.data.management.Receipt;
import incometaxcalculator.data.management.TaxpayerManager;
import incometaxcalculator.exceptions.WrongFileEndingException;
import incometaxcalculator.exceptions.WrongFileFormatException;
import incometaxcalculator.exceptions.WrongReceiptDateException;
import incometaxcalculator.exceptions.WrongReceiptKindException;
import incometaxcalculator.exceptions.WrongTaxpayerStatusException;

class AddReceiptTest {
  
  public String getFileStructure(String fileName) {

    String [] path_structure = fileName.split("\\\\");
    String fileFormat = path_structure[path_structure.length-1];
    
    String [] file_info = fileFormat.split("_");
    
    String taxRegistrationNumber = file_info[0];
    
    return taxRegistrationNumber;
  }
  
  public void checkAddReceipt(TaxpayerManager test_tpm, String test_filename,Receipt test_receipt, int receiptID) {
    int test_taxpyerID = Integer.parseInt(getFileStructure(test_filename));
    int numberOfReceipts = 0;
    
    assertNotNull(test_receipt);
    assertNotNull(test_tpm.taxpayerHashMap);
    
    try {
      test_tpm.loadTaxpayer(test_filename);
    } catch (NumberFormatException | IOException | WrongFileFormatException
        | WrongFileEndingException | WrongTaxpayerStatusException | WrongReceiptKindException
        | WrongReceiptDateException e) {
      e.printStackTrace();
    }
    
    numberOfReceipts = test_tpm.taxpayerHashMap.get(test_taxpyerID).getTotalReceiptsGathered();
    try {
      test_tpm.taxpayerHashMap.get(test_taxpyerID).addReceipt(test_receipt);
    } catch (WrongReceiptKindException e) {
      e.printStackTrace();
    }   
    System.out.println(test_tpm.taxpayerHashMap.get(test_taxpyerID).getTotalReceiptsGathered());
    assertEquals(test_tpm.taxpayerHashMap.get(test_taxpyerID).getTotalReceiptsGathered(),numberOfReceipts+1);
    
  }
  
  @Test
  void testAddReceipt() {
    
    String test_filename_0;
    String test_filename_1;
    String test_filename_2;
    TaxpayerManager test_tpm;
    
    test_filename_0 = "C:\\Users\\Vasilis Stergiou\\Desktop\\IncomeTaxCalculator\\123456789_INFO.txt";
    test_filename_1 = "C:\\Users\\Vasilis Stergiou\\Desktop\\130456093_INFO.txt";
    test_filename_2 = "C:\\Users\\Vasilis Stergiou\\Desktop\\IncomeTaxCalculator\\130456094_INFO.xml";
    
    test_tpm = new TaxpayerManager();
    assertNotNull(test_tpm);
    
    Company test_company = new Company("Company","Greece","Ioannina","Street",1);
    Receipt test_receipt_0 = null;
    Receipt test_receipt_1 = null;
    Receipt test_receipt_2 = null;
    
    int receiptOwnerTRNsize;
    int numberOfReceipts_0 = 0;
    int numberOfReceipts_1 = 0;
    int numberOfReceipts_2 = 0;
    
    int test_taxpyerID_0 = Integer.parseInt(getFileStructure(test_filename_0));
    int test_taxpyerID_1 = Integer.parseInt(getFileStructure(test_filename_1)); 
    int test_taxpyerID_2 = Integer.parseInt(getFileStructure(test_filename_2));
    
    int receiptID_0 = 7;
    int receiptID_1 = 10;
    int receiptID_2 = 13;
    
    try {
      test_receipt_0 = new Receipt(receiptID_0,"12/4/1999",(float)400.0,"Other",test_company);
      test_receipt_1 = new Receipt(receiptID_1,"5/12/2022",(float)147.0,"Health",test_company);
      test_receipt_2 = new Receipt(receiptID_2,"25/12/2022",(float)3452.321,"Entertainment",test_company);
    } catch (WrongReceiptDateException e) {
      e.printStackTrace();
    }
    
    checkAddReceipt(test_tpm,test_filename_0, test_receipt_0, receiptID_0);
    checkAddReceipt(test_tpm,test_filename_1, test_receipt_1, receiptID_1);
    checkAddReceipt(test_tpm,test_filename_2, test_receipt_2, receiptID_2);
    
  }
}

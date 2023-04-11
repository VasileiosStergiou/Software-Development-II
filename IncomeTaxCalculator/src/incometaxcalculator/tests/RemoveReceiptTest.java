package incometaxcalculator.tests;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;

import org.junit.After;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import incometaxcalculator.data.management.Company;
import incometaxcalculator.data.management.Receipt;
import incometaxcalculator.data.management.TaxpayerManager;
import incometaxcalculator.exceptions.WrongFileEndingException;
import incometaxcalculator.exceptions.WrongFileFormatException;
import incometaxcalculator.exceptions.WrongReceiptDateException;
import incometaxcalculator.exceptions.WrongReceiptKindException;
import incometaxcalculator.exceptions.WrongTaxpayerStatusException;

class RemoveReceiptTest {
  
  public String getFileStructure(String fileName) {

    String [] path_structure = fileName.split("\\\\");
    String fileFormat = path_structure[path_structure.length-1];
    
    String [] file_info = fileFormat.split("_");
    
    String taxRegistrationNumber = file_info[0];
    
    return taxRegistrationNumber;
  }
  
  @Test
  void testRemoveReceipt() {
    
    String test_filename_0;
    String test_filename_1;
    String test_filename_2;
    TaxpayerManager test_tpm;
    
    test_filename_0 = "C:\\Users\\Vasilis Stergiou\\Desktop\\IncomeTaxCalculator\\123456789_INFO.txt";
    test_filename_1 = "C:\\Users\\Vasilis Stergiou\\Desktop\\130456093_INFO.txt";
    test_filename_2 = "C:\\Users\\Vasilis Stergiou\\Desktop\\IncomeTaxCalculator\\130456094_INFO.xml";
    test_tpm = new TaxpayerManager();
    
    Company test_company = new Company("Company","Greece","Ioannina","Street",1);
    Receipt test_receipt = null;
    
    int receiptOwnerTRNsize;
    int numberOfReceipts = 0;
    int receiptID =4;
    
    int test_taxpyerID_0 = Integer.parseInt(getFileStructure(test_filename_0));
    int test_taxpyerID_1 = Integer.parseInt(getFileStructure(test_filename_1)); 
    int test_taxpyerID_2 = Integer.parseInt(getFileStructure(test_filename_2));
    
    assertNotNull(test_tpm);
    try {
      test_tpm.loadTaxpayer(test_filename_0);
      test_tpm.loadTaxpayer(test_filename_1);
      test_tpm.loadTaxpayer(test_filename_2);
    } catch (NumberFormatException | IOException | WrongFileFormatException
        | WrongFileEndingException | WrongTaxpayerStatusException | WrongReceiptKindException
        | WrongReceiptDateException e) {
      e.printStackTrace();
    }
    assertNotNull(test_tpm.taxpayerHashMap);
    
    //receiptOwnerTRNsize = test_tpm.taxpayerHashMap.get(test_taxpyerID_0).receiptHashMap.size();
    numberOfReceipts = test_tpm.taxpayerHashMap.get(test_taxpyerID_0).getTotalReceiptsGathered();
    
    try {
      test_tpm.taxpayerHashMap.get(test_taxpyerID_0).removeReceipt(receiptID);
    } catch (WrongReceiptKindException e) {
      e.printStackTrace();
    }
    
    assertFalse(test_tpm.taxpayerHashMap.get(test_taxpyerID_0).receiptHashMap.containsKey(receiptID));    
    assertEquals(test_tpm.taxpayerHashMap.get(test_taxpyerID_0).getTotalReceiptsGathered(),numberOfReceipts-1);
  }
}

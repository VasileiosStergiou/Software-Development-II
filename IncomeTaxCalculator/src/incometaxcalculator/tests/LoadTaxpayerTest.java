package incometaxcalculator.tests;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JFrame;

import org.junit.After;
import org.junit.Before;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import incometaxcalculator.data.management.TaxpayerManager;
import incometaxcalculator.exceptions.WrongFileEndingException;
import incometaxcalculator.exceptions.WrongFileFormatException;
import incometaxcalculator.exceptions.WrongReceiptDateException;
import incometaxcalculator.exceptions.WrongReceiptKindException;
import incometaxcalculator.exceptions.WrongTaxpayerStatusException;

class LoadTaxpayerTest {
  
  public String getFileStructure(String fileName) {

    String [] path_structure = fileName.split("\\\\");
    String fileFormat = path_structure[path_structure.length-1];
    
    String [] file_info = fileFormat.split("_");
    
    String taxRegistrationNumber = file_info[0];
    
    return taxRegistrationNumber;
  }
  
  @Test
  void testLoadTaxpyer() {
    // Uncomment one of the test filename and comment the others
    // Depending on which file browsing you want to test
    
    String test_filename_0;
    String test_filename_1;
    String test_filename_2;
    TaxpayerManager test_tpm = new TaxpayerManager();
    
    assertNotNull(test_tpm);
    
    test_filename_0 = "C:\\Users\\Vasilis Stergiou\\Desktop\\IncomeTaxCalculator\\123456789_INFO.txt";
    test_filename_1 = "C:\\Users\\Vasilis Stergiou\\Desktop\\130456093_INFO.txt";
    test_filename_2 = "C:\\Users\\Vasilis Stergiou\\Desktop\\IncomeTaxCalculator\\130456094_INFO.xml";
    
    int test_taxpyerID_0 = Integer.parseInt(getFileStructure(test_filename_0));
    int test_taxpyerID_1 = Integer.parseInt(getFileStructure(test_filename_1)); 
    int test_taxpyerID_2 = Integer.parseInt(getFileStructure(test_filename_2)); 
         
    int taxpayerHashMapSize = test_tpm.taxpayerHashMap.size();
    
    assertNotNull(test_tpm.taxpayerHashMap);
    assertFalse(test_tpm.taxpayerHashMap.containsKey(test_taxpyerID_0));
    assertFalse(test_tpm.taxpayerHashMap.containsKey(test_taxpyerID_1));
    assertFalse(test_tpm.taxpayerHashMap.containsKey(test_taxpyerID_2));
    
    try {
      test_tpm.loadTaxpayer(test_filename_0);
      test_tpm.loadTaxpayer(test_filename_1);
      test_tpm.loadTaxpayer(test_filename_2);
    } catch (NumberFormatException | IOException | WrongFileFormatException
        | WrongFileEndingException | WrongTaxpayerStatusException | WrongReceiptKindException
        | WrongReceiptDateException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    
    assertTrue(test_tpm.taxpayerHashMap.containsKey(test_taxpyerID_0));
    assertTrue(test_tpm.taxpayerHashMap.containsKey(test_taxpyerID_1));
    assertTrue(test_tpm.taxpayerHashMap.containsKey(test_taxpyerID_2));  
    assertEquals(test_tpm.taxpayerHashMap.size(),taxpayerHashMapSize+3);
  }
}

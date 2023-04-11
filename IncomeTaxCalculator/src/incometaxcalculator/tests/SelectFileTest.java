package incometaxcalculator.tests;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.util.Map;

import org.junit.After;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import incometaxcalculator.data.management.Taxpayer;
import incometaxcalculator.data.management.TaxpayerManager;
import incometaxcalculator.exceptions.WrongFileEndingException;
import incometaxcalculator.exceptions.WrongFileFormatException;
import incometaxcalculator.exceptions.WrongReceiptDateException;
import incometaxcalculator.exceptions.WrongReceiptKindException;
import incometaxcalculator.exceptions.WrongTaxpayerStatusException;
import incometaxcalculator.gui.TaxpayerData;

class SelectFileTest {
  
  public String getFileStructure(String fileName) {

    String [] path_structure = fileName.split("\\\\");
    String fileFormat = path_structure[path_structure.length-1];
    
    String [] file_info = fileFormat.split("_");
    
    String taxRegistrationNumber = file_info[0];
    
    return taxRegistrationNumber;
  }
  
  public boolean validNumericFields(float income, int trn, double basicTax,
                                    double variationTax,double totalTax,int totalReceipts) {
    if(income <0 || basicTax <0 || totalReceipts <0 || variationTax <0 || totalTax < 0) {
       return false;
     }
    return true;
  }
  
  public boolean validTextFields(String name,String taxpayerCategory) {
    if (name == null ||taxpayerCategory == null) {
      return false;
    }
    return true;
  }
  
  public boolean fieldsEmpty(Taxpayer taxpayer) {
    if (taxpayer == null) {
      return false;
    }
    String name = taxpayer.getFullname();
    float income = taxpayer.getIncome();
    int trn = taxpayer.getTaxRegistrationNumber();
    double basicTax = taxpayer.getBasicTax();
    int totalReceipts = taxpayer.getTotalReceiptsGathered();
    double variationTax = taxpayer.getVariationTaxOnReceipts();
    double totalTax = taxpayer.getTotalTax();
    String taxpayerCategory = taxpayer.getClass().toString().split("\\.")[3];
    
    if(!validNumericFields(income,trn,basicTax,variationTax,totalTax,totalReceipts)||
        !validTextFields(name,taxpayerCategory)){
      return true;
    }
    return false;
  }
  
  @Test
  void testSelectTaxpayer() {
    
    String test_filename_0;
    String test_filename_1;
    String test_filename_2;
    TaxpayerManager test_tpm;
    
    test_filename_0 = "C:\\Users\\Vasilis Stergiou\\Desktop\\IncomeTaxCalculator\\123456789_INFO.txt";
    test_filename_1 = "C:\\Users\\Vasilis Stergiou\\Desktop\\130456093_INFO.txt";
    test_filename_2 = "C:\\Users\\Vasilis Stergiou\\Desktop\\IncomeTaxCalculator\\130456094_INFO.xml";
    
    test_tpm = new TaxpayerManager();
    assertNotNull(test_tpm);
    
    int test_taxpyerID_0 = Integer.parseInt(getFileStructure(test_filename_0));
    int test_taxpyerID_1 = Integer.parseInt(getFileStructure(test_filename_1)); 
    int test_taxpyerID_2 = Integer.parseInt(getFileStructure(test_filename_2));
    
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
    
    assertNotNull(test_tpm.taxpayerHashMap);
    
    boolean fieldsNotNull = true;
    for (Map.Entry<Integer, Taxpayer> taxpayers : test_tpm.taxpayerHashMap.entrySet()) {
      int tax_category_ID = taxpayers.getKey();
      Taxpayer taxpayer = taxpayers.getValue();
      if (fieldsEmpty(taxpayer)) {
        fieldsNotNull = false;
        break;
      }
    }
    assertTrue(fieldsNotNull);
  }
}

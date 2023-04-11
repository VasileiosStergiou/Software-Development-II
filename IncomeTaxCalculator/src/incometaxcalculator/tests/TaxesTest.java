package incometaxcalculator.tests;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import javax.swing.JFileChooser;
import javax.swing.JFrame;

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
import incometaxcalculator.tests.TaxCalculationTesting;

class TaxesTest {
  
  public String getFileStructure(String fileName) {

    String [] path_structure = fileName.split("\\\\");
    String fileFormat = path_structure[path_structure.length-1];
    
    String [] file_info = fileFormat.split("_");
    
    String taxRegistrationNumber = file_info[0];
    
    return taxRegistrationNumber;
  }
  
  public void checkTaxpayerTax(Taxpayer taxpayer) {
    double income = taxpayer.income;
    double variationTax = taxpayer.getVariationTaxOnReceipts();
    double basicTax = taxpayer.calculateBasicTax();
    double totalTax =taxpayer.getTotalTax();
    float totalAmountOfReceipts = taxpayer.getTotalAmountOfReceipts();
    
    String [] taxpayerCategoryStructure = taxpayer.getClass().toString().split("\\.");
    int size = taxpayerCategoryStructure.length-1;
    String taxpayerCategory = taxpayerCategoryStructure[size];
    
    TaxCalculationTesting test_tax = new TaxCalculationTesting();
    double testBasicTax = test_tax.getTestBasicTax(income,taxpayerCategory);
    double testVariationTax = test_tax.getVariationTaxOnReceipts(income, basicTax, totalAmountOfReceipts);
    
    assertNotEquals(basicTax,-1);
    assertNotEquals(testBasicTax,-1);
    
    assertEquals(basicTax,testBasicTax);
    assertEquals(variationTax,testVariationTax);
    assertEquals(basicTax+variationTax,testBasicTax +testVariationTax);
  }
  
  String test_filename_0;
  String test_filename_1;
  String test_filename_2;
  TaxpayerManager test_tpm;
  
  @Test
  void testCorrectTaxes() {
    
    String test_filename_0;
    String test_filename_1;
    String test_filename_2;
    TaxpayerManager test_tpm;
    
    test_filename_0 = "C:\\Users\\Vasilis Stergiou\\Desktop\\IncomeTaxCalculator\\123456789_INFO.txt";
    test_filename_1 = "C:\\Users\\Vasilis Stergiou\\Desktop\\130456093_INFO.txt";
    test_filename_2 = "C:\\Users\\Vasilis Stergiou\\Desktop\\IncomeTaxCalculator\\130456094_INFO.xml";
    //test_filename_1 = "C:\\Users\\Vasilis Stergiou\\Desktop\\test taxpayers\\130456093_INFO.txt";
    //test_filename_2 = "C:\\Users\\Vasilis Stergiou\\Desktop\\130456094_INFO.xml";
    test_tpm = new TaxpayerManager();
    int taxpayerHashMapSize = 0;
    
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
      e.printStackTrace();
    }
    
    Taxpayer tp_0 = test_tpm.taxpayerHashMap.get(test_taxpyerID_0);
    Taxpayer tp_1 = test_tpm.taxpayerHashMap.get(test_taxpyerID_1);
    Taxpayer tp_2 = test_tpm.taxpayerHashMap.get(test_taxpyerID_2);
    
    assertNotNull(tp_0);
    assertNotNull(tp_1);
    assertNotNull(tp_2);
    
    checkTaxpayerTax(tp_0);
    checkTaxpayerTax(tp_1);
    checkTaxpayerTax(tp_2);
  }
}

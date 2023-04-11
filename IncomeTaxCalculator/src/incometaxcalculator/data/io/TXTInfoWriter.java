package incometaxcalculator.data.io;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;

import javax.swing.JOptionPane;

import incometaxcalculator.data.management.Receipt;
import incometaxcalculator.data.management.Taxpayer;
import incometaxcalculator.data.management.TaxpayerManager;
import incometaxcalculator.data.io.InfoWriter;

public class TXTInfoWriter extends FileWriter {
  
  public boolean fileCreated = false;;
  
  public boolean fileCreated() {
    return fileCreated;
  }
  
  public void generateFile(int taxRegistrationNumber) throws IOException {
    
    PrintWriter outputStream;
    File outputFile = createNewSavingFile();
    String outputFileName;
    if (outputFile != null) {
      String outputFilePath = outputFile.getAbsolutePath();
      outputFileName = outputFilePath+"//"+taxRegistrationNumber + "_INFO.txt";
      
    }
    else {
      outputFileName = taxRegistrationNumber + "_INFO.txt";
      JOptionPane.showMessageDialog(null,"No folder was chosen. "+
                                          taxRegistrationNumber + "_INFO.txt file will be saved in the application folder");
    }
    outputStream = new PrintWriter(new java.io.FileWriter(outputFileName));
                 
    InfoWriter info_writer = new InfoWriter(outputStream,"txt",taxRegistrationNumber);
  
    info_writer.generateFile(taxRegistrationNumber);
    info_writer.generateTaxpayerReceipts();
  }
}
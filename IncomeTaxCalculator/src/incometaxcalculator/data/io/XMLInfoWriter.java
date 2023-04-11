package incometaxcalculator.data.io;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;

import javax.swing.JOptionPane;

import incometaxcalculator.data.management.Receipt;
import incometaxcalculator.data.management.TaxpayerManager;

public class XMLInfoWriter extends FileWriter  {
  
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
      outputFileName = outputFilePath+"//"+taxRegistrationNumber + "_INFO.xml";
      
    }
    else {
      outputFileName = taxRegistrationNumber + "_INFO.xml";
      JOptionPane.showMessageDialog(null,"No folder was chosen. "+
                                          taxRegistrationNumber + "_INFO.xml file will be saved in the application folder");
    }
    outputStream = new PrintWriter(new java.io.FileWriter(outputFileName));
      
    InfoWriter info_writer = new InfoWriter(outputStream,"xml",taxRegistrationNumber);
  
    info_writer.generateFile(taxRegistrationNumber);
    info_writer.generateTaxpayerReceipts();
  }
}
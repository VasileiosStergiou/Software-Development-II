package incometaxcalculator.data.io;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import javax.swing.JOptionPane;

import incometaxcalculator.data.management.Receipt;
import incometaxcalculator.data.management.Taxpayer;
import incometaxcalculator.data.management.TaxpayerManager;

public class TXTLogWriter extends FileWriter {

  private static final short ENTERTAINMENT = 0;
  private static final short BASIC = 1;
  private static final short TRAVEL = 2;
  private static final short HEALTH = 3;
  private static final short OTHER = 4;
  
  public boolean fileCreated = false;;
  
  public boolean fileCreated() {
    return fileCreated;
  }

  public void generateFile(int taxRegistrationNumber) throws IOException {    
    PrintWriter outputStream ;
    
    File outputFile = createNewSavingFile();
    String outputFileName;
    if (outputFile != null) {
      String outputFilePath = outputFile.getAbsolutePath();
      outputFileName = outputFilePath+"//"+taxRegistrationNumber + "_LOG.txt";
    }
    else {
      outputFileName = taxRegistrationNumber + "_LOG.txt";
      JOptionPane.showMessageDialog(null,"No folder was chosen. "+
                                          taxRegistrationNumber + "_LOG.txt file will be saved in the application folder");
    }
    outputStream = new PrintWriter(new java.io.FileWriter(outputFileName));
            
    LogWriter txt_log_writer = new LogWriter(outputStream, "txt", taxRegistrationNumber);
      
    txt_log_writer.writeLogDetails();
      
    outputStream.close();
    txt_log_writer.info_writer.outputStream.close();
  }
}

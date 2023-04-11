package incometaxcalculator.data.io;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javax.swing.JFileChooser;

import incometaxcalculator.data.management.Receipt;
import incometaxcalculator.data.management.Taxpayer;
import incometaxcalculator.data.management.TaxpayerManager;

public abstract class FileWriter {

  public abstract void generateFile(int taxRegistrationNumber) throws IOException;
  
  public abstract boolean fileCreated();

  public Taxpayer getTaxpayer(int taxRegistrationNumber) {
    TaxpayerManager manager = new TaxpayerManager();
    return manager.taxpayerHashMap.get(taxRegistrationNumber);
  }
  
  public static File createNewSavingFile() {
    JFileChooser fileChooser = new JFileChooser();
    fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
    int option = fileChooser.showOpenDialog(fileChooser);
    
    File file = null;
    
    if(option == JFileChooser.APPROVE_OPTION){
       file = fileChooser.getSelectedFile();
    }
    return file;
  }
}
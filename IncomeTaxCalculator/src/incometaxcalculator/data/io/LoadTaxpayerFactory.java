package incometaxcalculator.data.io;

import incometaxcalculator.exceptions.WrongFileEndingException;

public class LoadTaxpayerFactory {
  public FileReader getAppropriateFileReader(String ending) {
    FileReader file_reader = null;
    
    switch(ending) {
      case "txt":
        file_reader = new TXTFileReader();
        break;
      case "xml":
        file_reader = new XMLFileReader();
        break;
      default:
        file_reader = null;
        break;  
    }
    return file_reader;
  }
}

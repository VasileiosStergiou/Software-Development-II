package incometaxcalculator.data.io;

import java.io.File;

public class FileWriterFactory{
    FileWriter file_writer = null;
    
    public FileWriter getFileWriter(String outputFileFormat) {    
      switch(outputFileFormat) {
        case "txt":
          file_writer = new TXTInfoWriter();
          break;
        case "xml":
          file_writer = new XMLInfoWriter();
          break;
        default:
          file_writer = null;
          break;  
      }
      return file_writer;
  }
}

package incometaxcalculator.data.io;

import java.io.File;

public class LogFileFactory {
  public FileWriter file_writer = null;
  
  public FileWriter getFileWriter(String fileFormat) {
      switch(fileFormat) {
        case "txt":
          file_writer = new TXTLogWriter();
          break;
        case "xml":
          file_writer = new XMLLogWriter();
          break;
        default:
          file_writer = null;
          break;  
      }
      return file_writer;
   }
}

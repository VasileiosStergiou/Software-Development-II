package incometaxcalculator.data.io;

import java.io.BufferedReader;
import java.io.IOException;

import incometaxcalculator.exceptions.WrongFileFormatException;

public class TXTFileReader extends FileReader {
  protected int checkForReceipt(BufferedReader inputStream)
      throws NumberFormatException, IOException {
    String line;
    while (!isEmpty(line = inputStream.readLine())) {
      String values[] = line.split(" ", 3);
      int receiptID = getReceiptID(values);
      if (receiptID != -1) {
        return receiptID;
      }
    }
    return -1;
  }
  
  protected String getValueOfField(String fieldsLine) throws WrongFileFormatException {
    if (isEmpty(fieldsLine)) {
      throw new WrongFileFormatException();
    }
    try {
      String values[] = fieldsLine.split(" ", 2);
      String trimmedValue = trimValue(values);
      return trimmedValue;
    } catch (NullPointerException e) {
      throw new WrongFileFormatException();
    }
  }
  
  public String trimValue(String [] values) {
    values[1] = values[1].trim();
    return values[1];
  }  
}
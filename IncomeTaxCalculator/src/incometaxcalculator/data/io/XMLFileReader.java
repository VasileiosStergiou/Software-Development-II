package incometaxcalculator.data.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Arrays;

import incometaxcalculator.exceptions.WrongFileFormatException;

public class XMLFileReader extends FileReader {
  
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
      String valueWithTail[] = fieldsLine.split(" ", 2);
      String valueReversed = getReversedValue(valueWithTail);
      return valueReversed;
    } catch (NullPointerException e) {
      throw new WrongFileFormatException();
    }
  }
  
  public String getReversedValue(String [] valueWithTail) {
    String valueReversed[] = new StringBuilder(valueWithTail[1]).reverse().toString().trim().split(" ", 2);
    return new StringBuilder(valueReversed[1]).reverse().toString(); 
  }
}

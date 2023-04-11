package incometaxcalculator.data.io;

import java.io.BufferedReader;
import java.io.IOException;

import incometaxcalculator.data.management.TaxpayerManager;
import incometaxcalculator.exceptions.WrongReceiptDateException;
import incometaxcalculator.exceptions.WrongFileFormatException;
import incometaxcalculator.exceptions.WrongReceiptKindException;
import incometaxcalculator.exceptions.WrongTaxpayerStatusException;

public abstract class FileReader {

  protected abstract int checkForReceipt(BufferedReader inputStream)
      throws NumberFormatException, IOException;

  protected abstract String getValueOfField(String fieldsLine) throws WrongFileFormatException;
  TaxpayerManager manager = new TaxpayerManager();
  
  /**
   * 
   * @throws NumberFormatException 
   * @throws IOException 
   * @throws WrongTaxpayerStatusException
   * @throws WrongFileFormatException
   * @throws WrongReceiptKindException
   * @throws WrongReceiptDateException
   */
  public void readFile(String fileName)
      throws NumberFormatException, IOException, WrongTaxpayerStatusException,
      WrongFileFormatException, WrongReceiptKindException, WrongReceiptDateException {

    BufferedReader inputStream = new BufferedReader(new java.io.FileReader(fileName));
    String fullname = getValueOfField(inputStream.readLine());
    int taxRegistrationNumber = Integer.parseInt(getValueOfField(inputStream.readLine()));
    String status = getValueOfField(inputStream.readLine());
    float income = Float.parseFloat(getValueOfField(inputStream.readLine()));
    manager.createTaxpayer(fullname, taxRegistrationNumber, status, income);
    while (readReceipt(inputStream,taxRegistrationNumber));
  }

  protected boolean readReceipt(BufferedReader inputStream, int taxRegistrationNumber)
      throws WrongFileFormatException, IOException, WrongReceiptKindException,
      WrongReceiptDateException {
    
    String receiptExistance;
    
    int receiptId;
    if ((receiptId = checkForReceipt(inputStream)) < 0) {
      return false;
    }
    String issueDate = getValueOfField(inputStream.readLine());
    String kind = getValueOfField(inputStream.readLine());
    float amount = Float.parseFloat(getValueOfField(inputStream.readLine()));
    String companyName = getValueOfField(inputStream.readLine());
    String country = getValueOfField(inputStream.readLine());
    String city = getValueOfField(inputStream.readLine());
    String street = getValueOfField(inputStream.readLine());
    int number = Integer.parseInt(getValueOfField(inputStream.readLine()));
    manager.createReceipt(receiptId, issueDate, amount, kind, companyName, country, city, street, number,
        taxRegistrationNumber);
    return true;
  }
  
  public int getReceiptID(String[] values) {
    int receiptId = -1;
    if (values[0].equals("<ReceiptID>")) {
      receiptId = Integer.parseInt(values[1].trim());
      return receiptId;
    } else if (values[0].equals("Receipt")) {
      if (values[1].equals("ID:")) {
        receiptId = Integer.parseInt(values[2].trim());
        return receiptId;
      }
    }
    return receiptId;
  }
  
  protected boolean isEmpty(String line) {
    if (line == null) {
      return true;
    } else {
      return false;
    }
  }

}
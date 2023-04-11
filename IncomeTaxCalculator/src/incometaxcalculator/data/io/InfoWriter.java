package incometaxcalculator.data.io;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import incometaxcalculator.data.management.Receipt;
import incometaxcalculator.data.management.TaxpayerManager;

public class InfoWriter{
  
  PrintWriter outputStream;
  String file_type;
  int taxRegistrationNumber;
  
  public String fileFormat;
  public String [] fileFormats = {"txt","xml"};
  public String [] tagsStart = {": ","<> "};
  public String [] tagsEnd = {""," </>"};
  public int fileFormatIndex;
  public String tagStart;
  public String tagEnd;
  public String formatedTagStart;
  public String formatedTagEnd;
  
  public int getFileFormat() {
    List<String> fileFormats = Arrays.asList("txt", "xml");
    ListIterator<String> it = fileFormats.listIterator();
    while (it.hasNext()) {
      if (file_type == it.next()) {
        fileFormatIndex = it.previousIndex();
        break;
      }
    }
    if (fileFormatIndex == fileFormats.size()) {
      fileFormatIndex = fileFormats.size()-1;
    }
    return fileFormatIndex;
  }
    
  public String addLabel(String str, String ch, int position) {
    StringBuilder sb = new StringBuilder(str);
    sb.insert(position, ch);
    return sb.toString();
  }
  
  public void setOutputLabels(String label) {
    int labelStartingPosition;
    int labelEndingPosition;
    if (fileFormat != "xml") {
      labelStartingPosition = 0;
      formatedTagEnd = tagEnd;
    }
    else {
      labelStartingPosition = 1;
      labelEndingPosition =3;
      formatedTagEnd = addLabel(tagEnd,label,labelEndingPosition);
    }
    formatedTagStart = addLabel(tagStart,label,labelStartingPosition);
  }
  
  public void setOutputCompositeLabels(String label) {
    if (fileFormat != "xml") {
      formatedTagStart = addLabel(tagStart,label,0);
      formatedTagEnd = tagEnd;
    }
    else{
      String [] compositeLabel = label.split(" ");
      String outputLabel = compositeLabel[0] + compositeLabel[1];
      formatedTagStart = addLabel(tagStart,outputLabel,1);
      formatedTagEnd = addLabel(tagEnd,outputLabel,3);     
    }
  }
  
  public void setOutputReceiptsLabel() {
    if (fileFormat != "xml") {
      formatedTagStart = addLabel(tagStart,"Receipts",0);
    }
    else{
      formatedTagStart = addLabel(tagStart,"Receipts",1);
    }
  }
  
  public InfoWriter(PrintWriter outputStream, final String file_type,int taxRegistrationNumber) {
    this.outputStream = outputStream;
    this.file_type = file_type;
    this.taxRegistrationNumber = taxRegistrationNumber;
    this.fileFormatIndex = getFileFormat();
    this.fileFormat = fileFormats[fileFormatIndex];
    this.tagStart = tagsStart[fileFormatIndex];
    this.tagEnd = tagsEnd[fileFormatIndex];
  }
  
  public void writeName() {
    setOutputLabels("Name");
    String taxpayerName = TaxpayerManager.getTaxpayerName(taxRegistrationNumber);
    outputStream.println(formatedTagStart + taxpayerName + formatedTagEnd);
  }
  
  public void writeAFM() {
    setOutputLabels("AFM");
    outputStream.println(formatedTagStart + taxRegistrationNumber + formatedTagEnd);
  }
  
  public void writeStatus() {
    setOutputLabels("Status");
    String status = TaxpayerManager.getTaxpayerStatus(taxRegistrationNumber);
    outputStream.println(formatedTagStart + status + formatedTagEnd);
  }
  
  public void writeIncome() {
    setOutputLabels("Income");
    String income = TaxpayerManager.getTaxpayerIncome(taxRegistrationNumber);
    outputStream.println(formatedTagStart + income + formatedTagEnd);
  }
  
  public void writeReceipts() {
    setOutputReceiptsLabel();
    outputStream.println();
  }
  
  public void writeReceiptID(Receipt receipt) {
    setOutputCompositeLabels("Receipt ID");
    int receiptId = receipt.getId();
    outputStream.println(formatedTagStart + receiptId + formatedTagEnd);
  }
  
  public void writeDate(Receipt receipt) {
    setOutputLabels("Date");
    String issueDate = receipt.getIssueDate();
    outputStream.println(formatedTagStart + issueDate + formatedTagEnd);
  }
  
  public void writeKind(Receipt receipt) {
    setOutputLabels("Kind");
    String receiptKind = receipt.getKind();
    outputStream.println(formatedTagStart + receiptKind + formatedTagEnd);
  }
  
  public void writeAmount(Receipt receipt) {
    setOutputLabels("Amount");
    float amount =  receipt.getAmount();
    outputStream.println(formatedTagStart + amount + formatedTagEnd);
  }
  
  public void writeCompany(Receipt receipt) {
    setOutputLabels("Company");
    String companyName = receipt.getCompany().getName();
    outputStream.println(formatedTagStart + companyName + formatedTagEnd);
  }
  
  public void writeCountry(Receipt receipt) {
    setOutputLabels("Country");
    String country = receipt.getCompany().getCountry();
    outputStream.println(formatedTagStart + country + formatedTagEnd);
  }
  
  public void writeCity(Receipt receipt) {
    setOutputLabels("City");
    String city = receipt.getCompany().getCity();
    outputStream.println(formatedTagStart + city + formatedTagEnd);
  }
  
  public void writeStreet(Receipt receipt) {
    setOutputLabels("Street");
    String street = receipt.getCompany().getStreet();
    outputStream.println(formatedTagStart + street + formatedTagEnd);
  }
  
  public void writeNumber(Receipt receipt) {
    setOutputLabels("Number");
    int number = receipt.getCompany().getNumber();
    outputStream.println(formatedTagStart+ number + formatedTagEnd);
    outputStream.println();
  }

  public void generateFile(int taxRegistrationNumber) throws IOException {
    writeName();
    writeAFM();
    writeStatus();
    writeIncome();
    writeReceipts();
  }
  
  public void generateTaxpayerReceipts() {
    HashMap<Integer, Receipt> receiptsHashMap = TaxpayerManager.getReceiptHashMap(taxRegistrationNumber);
    Iterator<HashMap.Entry<Integer, Receipt>> iterator = receiptsHashMap.entrySet().iterator();
    while (iterator.hasNext()) {
      HashMap.Entry<Integer, Receipt> entry = iterator.next();
      Receipt receipt = entry.getValue();
      writeReceiptID(receipt);
      writeDate(receipt);
      writeKind(receipt);
      writeAmount(receipt);
      writeCompany(receipt);
      writeCountry(receipt);
      writeCity(receipt);
      writeStreet(receipt);
      writeNumber(receipt);
    }
    outputStream.close();
  }
}

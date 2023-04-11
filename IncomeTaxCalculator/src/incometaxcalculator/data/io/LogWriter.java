package incometaxcalculator.data.io;

import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import incometaxcalculator.data.io.InfoWriter;
import incometaxcalculator.data.management.TaxpayerManager;

public class LogWriter {
  PrintWriter outputStream;
  InfoWriter info_writer;
  String file_type;
  int taxRegistrationNumber;
  
  final int TXT =0;
  final int XML =1;
  
  private static final short ENTERTAINMENT = 0;
  private static final short BASIC = 1;
  private static final short TRAVEL = 2;
  private static final short HEALTH = 3;
  private static final short OTHER = 4;
  
  public String fileFormat;
  public String [] fileFormats = {"txt","xml"};
  public String [] tagsStart = {": ","<> "};
  public String [] tagsEnd = {""," </>"};
  public int fileFormatIndex;
  public String tagStart;
  public String tagEnd;
  public String formatedTagStart;
  public String formatedTagEnd;
  
  public int getFileFormat(String file_type) {
    List<String> fileFormats = Arrays.asList("txt", "xml");
    ListIterator<String> it = fileFormats.listIterator();
    int fileFormatIndex = -1;
    while (it.hasNext()) {
      if (file_type == it.next()) {
        fileFormatIndex = it.nextIndex();
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
  
  public void setOutputLogLabels(String fileFormat, String label) {
    int labelStartingPosition;
    int labelEndingPosition;
    String editedLabel;
    if (fileFormat != "xml") {
      labelStartingPosition = 0;
      formatedTagEnd = tagEnd;
      editedLabel = label;
    }
    else{
      labelStartingPosition = 1;
      labelEndingPosition =3;
      String [] compositeLabel = label.split(" ");
      editedLabel = compositeLabel[0] + compositeLabel[1];
      formatedTagEnd = addLabel(tagEnd,editedLabel,labelEndingPosition);     
    }
    formatedTagStart = addLabel(tagStart,editedLabel,labelStartingPosition);
  }
  
  public void setOutputLabels(String fileFormat, String label) {
    if (fileFormat != "xml") {
      formatedTagStart = addLabel(tagStart,label,0);
      formatedTagEnd = tagEnd;
    }
    else {
      formatedTagStart = addLabel(tagStart,label,1);
      formatedTagEnd = addLabel(tagEnd,label,3);
    }
  }
  
  public String getTaxVariationLabel() {
    String label;
    double variationTax = TaxpayerManager.getTaxpayerVariationTaxOnReceipts(taxRegistrationNumber);
    if (variationTax > 0) {
      label = "Tax Increase";
    } else {
      label = "Tax Decrease";
    }
    return label;
  }
  
  public String getTotalReceiptsLabel() {
    String label;
    if (fileFormat != "xml") {
      label = "TotalReceiptsGathered";
      formatedTagEnd = tagEnd;
    }
    else{
      label = "Receipts";
    }
    return label;
  }
  
  public void writeTaxVariation() {
    String label = getTaxVariationLabel();
    setOutputLogLabels(fileFormat,label);
    double variationTaxOnReceipts = TaxpayerManager.getTaxpayerVariationTaxOnReceipts(taxRegistrationNumber);
    outputStream.println(formatedTagStart + variationTaxOnReceipts + formatedTagEnd);
  }
  
  public LogWriter(PrintWriter outputStream, final String file_type,int taxRegistrationNumber) {
    this.outputStream = outputStream;
    this.file_type = file_type;
    this.taxRegistrationNumber = taxRegistrationNumber;
    this.fileFormatIndex = getFileFormat(file_type);
    this.fileFormat = fileFormats[fileFormatIndex];
    this.tagStart = tagsStart[fileFormatIndex];
    this.tagEnd = tagsEnd[fileFormatIndex];
    initialiseInfoWriter();
  }
  
  public void initialiseInfoWriter() {
    this.info_writer = new InfoWriter(outputStream, fileFormat, taxRegistrationNumber);
  }
  
  public void writeBasicTax() {
    setOutputLogLabels(fileFormat,"Basic Tax");
    double basicTax = TaxpayerManager.getTaxpayerBasicTax(taxRegistrationNumber);
    outputStream.println(formatedTagStart + basicTax + formatedTagEnd);
  }
  
  public void writeTotalTax() {
    setOutputLogLabels(fileFormat,"Total Tax");
    double totalTax = TaxpayerManager.getTaxpayerTotalTax(taxRegistrationNumber);
    outputStream.println(formatedTagStart + totalTax + formatedTagEnd);
  }
  
  public void writeTotalReceiptsGathered() {
    String label = getTotalReceiptsLabel();
    int totalReceiptsGathered = TaxpayerManager.getTaxpayerTotalReceiptsGathered(taxRegistrationNumber);
    setOutputLabels(fileFormat,label);
    outputStream.println(formatedTagStart + totalReceiptsGathered + formatedTagEnd);
  }
  
  public void writeReceiptKindTax() {
    
    HashMap<Integer,String> kinds_of_tax = new HashMap<Integer,String>();
    
    kinds_of_tax.put((int) ENTERTAINMENT, "Entertainment");
    kinds_of_tax.put((int) BASIC, "Basic");
    kinds_of_tax.put((int) TRAVEL, "Travel");
    kinds_of_tax.put((int) HEALTH, "Health");
    kinds_of_tax.put((int) OTHER, "Other");
    
    String kind_label = "";
    
    for (Map.Entry<Integer,String> kind_of_tax : kinds_of_tax.entrySet()) {
      int tax_category_ID = kind_of_tax.getKey();
      String tax_category = kind_of_tax.getValue();
      float totalAmountPerKind = TaxpayerManager.getTaxpayerAmountOfReceiptKind(taxRegistrationNumber, (short) tax_category_ID);
      setOutputLabels(fileFormat,tax_category);
      outputStream.println(formatedTagStart +totalAmountPerKind +formatedTagEnd);
    }
  }
  
  public void writeLogDetails() {
    info_writer.writeName();
    info_writer.writeAFM();
    info_writer.writeIncome();
    
    writeBasicTax();
    writeTaxVariation();
    writeTotalTax();
    writeTotalReceiptsGathered();
    writeReceiptKindTax();
  }
  
}

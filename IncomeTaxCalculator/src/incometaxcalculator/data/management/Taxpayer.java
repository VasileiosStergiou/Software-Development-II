package incometaxcalculator.data.management;

import java.util.HashMap;
import java.util.ArrayList;
import incometaxcalculator.exceptions.WrongReceiptKindException;

public abstract class Taxpayer {

  protected final String fullname;
  protected final int taxRegistrationNumber;
  public final float income;
  public int index;
  
  private float amountPerReceiptsKind[] = new float[5];
  private int totalReceiptsGathered = 0;
  public HashMap<Integer, Receipt> receiptHashMap = new HashMap<Integer, Receipt>(0);
  
  public ArrayList<double[]> taxPerCategory = new ArrayList<double[]>();
  
  private String [] receipt_categories = {"Entertainment",
                                          "Basic",
                                          "Travel",
                                          "Health",
                                          "Other"};

  public abstract double calculateBasicTax();
  
  public int [] HeadOfHouseholdIncome = {30390,90000,122110,203390};
  public int [] MarriedFilingJointlyIncome = {36080,90000,143350,254240};
  public int [] MarriedFilingSeparatelyIncome = {18040,71680,90000,127120};
  public int [] SingleIncome = {24680,81080,90000,152540};
  
  private double [] HeadOfHouseholdTax;
  private double [] MarriedFilingJointlyTax;
  private double [] MarriedFilingSeparatelyTax;
  private double [] SingleTax;
  
  final int HEAD_OF_HOUSEHOLD = 0;
  final int MARRIED_FILLING_JOINTLY = 1;
  final int MARRIED_FILLING_SEPARATELY = 2;
  final int SINGLE = 3;

  protected Taxpayer(String fullname, int taxRegistrationNumber, float income) {
    this.fullname = fullname;
    this.taxRegistrationNumber = taxRegistrationNumber;
    this.income = income;
    setTaxesPerCategory();
  }

  public boolean addReceipt(Receipt receipt) throws WrongReceiptKindException {    
    
   //------------------ NEW CODE --------------------
    
    if (receiptHashMap.containsKey(receipt.getId())) {
      return false;
    }
    
    int index = getReceiptCategory(receipt);
    
    if (index >=0 && index <receipt_categories.length) {
      amountPerReceiptsKind[index] += receipt.getAmount();
      receiptHashMap.put(receipt.getId(), receipt);
      totalReceiptsGathered++;
      return true;
    } else {
      throw new WrongReceiptKindException();
    }
  }

  public void removeReceipt(int receiptId) throws WrongReceiptKindException {
    Receipt receipt = receiptHashMap.get(receiptId);
    
    //------------------ NEW CODE --------------------
    
    int index = getReceiptCategory(receipt);
    
    if (index >=0 && index <receipt_categories.length) {
      amountPerReceiptsKind[index] -= receipt.getAmount();
      totalReceiptsGathered--;
      receiptHashMap.remove(receiptId);
    } else {
      throw new WrongReceiptKindException();
    }
  }

  public String getFullname() {
    return fullname;
  }
  
  
  //--------------- NEW CODE -------------------
  public void setTaxesPerCategory() {
    HeadOfHouseholdTax = new double[]{0.0535 * income,
                                      1625.87 + 0.0705 * (income - 30390),
                                      5828.38 + 0.0705 * (income - 90000),
                                      8092.13 + 0.0785 * (income - 122110)};
    
    MarriedFilingJointlyTax = new double[]{0.0535 * income,
                                           1930.28 + 0.0705 * (income - 36080),
                                           5731.64 + 0.0705 * (income - 90000),
                                           9492.82 + 0.0785 * (income - 143350)};
    
    MarriedFilingSeparatelyTax = new double[]{0.0535 * income,
                                              965.14 + 0.0705 * (income - 18040),
                                              4746.76 + 0.0785 * (income - 71680),
                                              6184.88 + 0.0785 * (income - 90000)};
    SingleTax = new double[]{0.0535 * income,
        1320.38 + 0.0705 * (income - 24680),
        5296.58 + 0.0785 * (income - 81080),
        5996.80 + 0.0785 * (income - 90000)};
    
    taxPerCategory.add(HeadOfHouseholdTax);
    taxPerCategory.add(MarriedFilingJointlyTax);
    taxPerCategory.add(MarriedFilingSeparatelyTax);
    taxPerCategory.add(SingleTax);
  }
  
  //--------------- NEW CODE -------------------

  public int getTaxRegistrationNumber() {
    return taxRegistrationNumber;
  }

  public float getIncome() {
    return income;
  }

  public HashMap<Integer, Receipt> getReceiptHashMap() {
    return receiptHashMap;
  }

  public double getVariationTaxOnReceipts() {
    float totalAmountOfReceipts = getTotalAmountOfReceipts();
    
    double [] income_percentages = {(double) (0.2 * income),
                                    (double) (0.4 * income),
                                    (double) (0.6 * income)};
    
    double calculated_tax = calculateBasicTax();
    
    double [] basic_tax = {calculated_tax * 0.08,
                           calculated_tax * 0.04,
                           calculated_tax * 0.15};

    index = -1;
    for (int i=0; i< income_percentages.length;i++) {
      if (totalAmountOfReceipts < income_percentages[i]) {
        index = i;
        break;
      }
    }
    double final_tax = getAppropriateBasicTax(index,calculated_tax,basic_tax);
    return final_tax;
  }
  
  public double getAppropriateBasicTax(int index,double calculated_tax,double [] basic_tax) {
    if (index ==0 || index == 1) {
      return basic_tax[index];
    }
    else if (index ==2) {
      return -basic_tax[index];
    }
    else {
      return -calculated_tax * 0.3;
    }  
  }
  
  public int getReceiptCategory(Receipt receipt) {
    int index = -1;  
    for (int i = 0; i < receipt_categories.length; i++) {
      if (receipt.getKind().equals(receipt_categories[i])){
        index = i;
        return index;
      }
    }
    return index;
  }
  
  public int getTaxpayerIncomeIndex(float income, int [] incomeCategory) {
    int index =-1;
    
    for (int i=0 ; i< incomeCategory.length;i++) {
      if (income < incomeCategory[i]){
        index = i;
        return index;
      }
    }
    return index;
  }
  
  public float getTotalAmountOfReceipts() {
    int sum = 0;
    for (int i = 0; i < 5; i++) {
      sum += amountPerReceiptsKind[i];
    }
    return sum;
  }

  public int getTotalReceiptsGathered() {
    return totalReceiptsGathered;
  }

  public float getAmountOfReceiptKind(short kind) {
    return amountPerReceiptsKind[kind];
  }

  public double getTotalTax() {
    return calculateBasicTax() + getVariationTaxOnReceipts();
  }

  public double getBasicTax() {
    return calculateBasicTax();
  }

}
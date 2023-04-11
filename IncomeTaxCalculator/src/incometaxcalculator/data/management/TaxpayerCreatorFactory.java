package incometaxcalculator.data.management;

import incometaxcalculator.data.io.TXTInfoWriter;
import incometaxcalculator.data.io.XMLInfoWriter;

public class TaxpayerCreatorFactory{
  
  String fullname;
  String status;
  int taxRegistrationNumber;
  float income;
  
  
  public TaxpayerCreatorFactory (String fullname, int taxRegistrationNumber,String status,float income) {
     this.fullname = fullname;
     this.taxRegistrationNumber = taxRegistrationNumber;
     this.status = status;
     this.income = income;
  }
    
  public Taxpayer getAppropriateTaxpayer() {
    Taxpayer taxpayer = null;
    
    switch(status) {
      case "Married Filing Jointly":
        taxpayer =  new MarriedFilingJointlyTaxpayer(fullname, taxRegistrationNumber, income);
        break;
      case "Married Filing Separately":
        taxpayer = new MarriedFilingSeparatelyTaxpayer(fullname, taxRegistrationNumber, income);
        break;
      case "Single":
        taxpayer = new SingleTaxpayer(fullname, taxRegistrationNumber, income);
        break;
      case "Head of Household":
        taxpayer = new HeadOfHouseholdTaxpayer(fullname, taxRegistrationNumber, income);
        break;
      default:
        taxpayer = null;
        break;  
     }
     return taxpayer;
  }
}

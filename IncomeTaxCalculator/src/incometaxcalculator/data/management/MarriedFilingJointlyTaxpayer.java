package incometaxcalculator.data.management;

import java.util.Arrays;

public class MarriedFilingJointlyTaxpayer extends Taxpayer {

  public MarriedFilingJointlyTaxpayer(String fullname, int taxRegistrationNumber, float income) {
    super(fullname, taxRegistrationNumber, income);
  }

  public double calculateBasicTax() {
    int index = getTaxpayerIncomeIndex(income,MarriedFilingJointlyIncome);
    
    if (index >= 0 && index<MarriedFilingJointlyIncome.length) {
      return taxPerCategory.get(MARRIED_FILLING_JOINTLY)[index];
    }
    else {
     return 18197.69 + 0.0985 * (income - 254240);
    }
  }
}
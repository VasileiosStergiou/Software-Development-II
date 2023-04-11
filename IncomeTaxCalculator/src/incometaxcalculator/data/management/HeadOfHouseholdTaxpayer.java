package incometaxcalculator.data.management;

public class HeadOfHouseholdTaxpayer extends Taxpayer {

  public HeadOfHouseholdTaxpayer(String fullname, int taxRegistrationNumber, float income) {
    super(fullname, taxRegistrationNumber, income);
  }

  public double calculateBasicTax() {
    int index = getTaxpayerIncomeIndex(income,HeadOfHouseholdIncome);
    
    if (index >= 0 && index<HeadOfHouseholdIncome.length) {
      return taxPerCategory.get(HEAD_OF_HOUSEHOLD)[index];
    }
    else {
     return 14472.61 + 0.0985 * (income - 203390);
    }
  }
}

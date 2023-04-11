package incometaxcalculator.data.management;

public class MarriedFilingSeparatelyTaxpayer extends Taxpayer {

  public MarriedFilingSeparatelyTaxpayer(String fullname, int taxRegistrationNumber, float income) {
    super(fullname, taxRegistrationNumber, income);
  }

  public double calculateBasicTax() {
    int index = getTaxpayerIncomeIndex(income,MarriedFilingSeparatelyIncome);
    if (index >= 0 && index<MarriedFilingSeparatelyIncome.length) {
      return taxPerCategory.get(MARRIED_FILLING_SEPARATELY)[index];
    }
    else {
     return 9098.80 + 0.0985 * (income - 127120);
    }
  }
}
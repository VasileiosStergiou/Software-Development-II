package incometaxcalculator.data.management;

public class SingleTaxpayer extends Taxpayer {

  public SingleTaxpayer(String fullname, int taxRegistrationNumber, float income) {
    super(fullname, taxRegistrationNumber, income);
  }

  public double calculateBasicTax() {
    int index = getTaxpayerIncomeIndex(income,SingleIncome);
    
    if (index >= 0 && index<SingleIncome.length) {
      return taxPerCategory.get(SINGLE)[index];
    }
    else {
      return 10906.19 + 0.0985 * (income - 152540);
    }
  }
}
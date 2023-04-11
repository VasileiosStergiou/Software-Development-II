package incometaxcalculator.tests;

public class TaxCalculationTesting {
  public double checkHeadOfHouseholdTax(double income) {
    if (income < 30390) {
      return 0.0535 * income;
    } else if (income < 90000) {
      return 1625.87 + 0.0705 * (income - 30390);
    } else if (income < 122110) {
      return 5828.38 + 0.0705 * (income - 90000);
    } else if (income < 203390) {
      return 8092.13 + 0.0785 * (income - 122110);
    } else {
      return 14472.61 + 0.0985 * (income - 203390);
    }
  }
  
  public double checkMarriedFilingJointlyTax(double income) {
    if (income < 36080) {
      return 0.0535 * income;
    } else if (income < 90000) {
      return 1930.28 + 0.0705 * (income - 36080);
    } else if (income < 143350) {
      return 5731.64 + 0.0705 * (income - 90000);
    } else if (income < 254240) {
      return 9492.82 + 0.0785 * (income - 143350);
    } else {
      return 18197.69 + 0.0985 * (income - 254240);
    }
  }
  
  public double checkMarriedFilingSeparatelyTax(double income) {
    if (income < 18040) {
      return 0.0535 * income;
    } else if (income < 71680) {
      return 965.14 + 0.0705 * (income - 18040);
    } else if (income < 90000) {
      return 4746.76 + 0.0785 * (income - 71680);
    } else if (income < 127120) {
      return 6184.88 + 0.0785 * (income - 90000);
    } else {
      return 9098.80 + 0.0985 * (income - 127120);
    }
  }
  
  public double checkSingleTax(double income) {
    if (income < 24680) {
      return 0.0535 * income;
    } else if (income < 81080) {
      return 1320.38 + 0.0705 * (income - 24680);
    } else if (income < 90000) {
      return 5296.58 + 0.0785 * (income - 81080);
    } else if (income < 152540) {
      return 5996.80 + 0.0785 * (income - 90000);
    } else {
      return 10906.19 + 0.0985 * (income - 152540);
    }
  }
  
  public double getVariationTaxOnReceipts(double income,double basicTax,float totalAmountOfReceipts) {
    if (totalAmountOfReceipts < 0.2 * income) {
      return basicTax * 0.08;
    } else if (totalAmountOfReceipts < 0.4 * income) {
      return basicTax * 0.04;
    } else if (totalAmountOfReceipts < 0.6 * income) {
      return -basicTax * 0.15;
    } else {
      return -basicTax * 0.3;
    }
  }
  
  public double getTestBasicTax(double income, String taxpayerCategory) {
    
    double testBasicTax;
    
    switch(taxpayerCategory) {
      case "SingleTaxpayer":
        testBasicTax = checkSingleTax(income);
        return testBasicTax;
      case "MarriedFilingJointlyTaxpayer":
        testBasicTax = checkMarriedFilingJointlyTax(income);
        return testBasicTax;
      case "MarriedFilingSeparatelyTaxpayer":
        testBasicTax = checkMarriedFilingSeparatelyTax(income);
        return testBasicTax;
      case "HeadOfHouseholdTaxpayer":
        testBasicTax = checkHeadOfHouseholdTax(income);
        return testBasicTax;
      default:
        testBasicTax =-1;
        return testBasicTax;
    }
  }
  
}

package com.cesiztel.dutax.helpers;

import java.util.HashMap;

public class TaxCalculatorHelper {
    private static final String TAXES_RATE_NORMAL_INDEX = "normal";
    private static final String TAXES_WITHOUT_SOCIAL_INDEX = "withoutSocial";
    private static final String TAXES_OVER_64_INDEX = "over64";

    public static int[] getTaxAmountPeriods(int year) {
        HashMap taxAmountPeriods = new HashMap<Integer, int[]>();
        taxAmountPeriods.put(2015, new int[] {
                19822, // 0 - 19,822
                13767, // 33,589 - 19,822
                23996, // 57,585 - 33,589
        });
        taxAmountPeriods.put(2016, new int[] {
                19922, // 0 - 19,922
                13793, // 33,715 - 19,922
                32697, // 66,421 - 33,715
        });
        taxAmountPeriods.put(2017, new int[] {
                19981, // 0 - 19,982
                13807, // 33,789 - 19,982
                33282, // 67,071 - 33,789
        });

        return (int[]) taxAmountPeriods.get(year);
    }

    // get taxes rates
    public static double[] getTaxRates(int year, boolean socialSecurity) {
        HashMap taxesRates = new HashMap<Integer, HashMap>();

        HashMap taxes2015 = new HashMap();
        taxes2015.put(TAXES_RATE_NORMAL_INDEX, new double[]{ .365, .42, .42, .52 });
        taxes2015.put(TAXES_WITHOUT_SOCIAL_INDEX, new double[]{ .0835, .1385, .42, .52 });
        taxes2015.put(TAXES_OVER_64_INDEX, new double[]{ 0.1860, 0.2410, .42, .52 });

        HashMap taxes2016 = new HashMap();
        taxes2016.put(TAXES_RATE_NORMAL_INDEX, new double[]{ .3655, .404, .404, .52 });
        taxes2016.put(TAXES_WITHOUT_SOCIAL_INDEX, new double[]{ .0835, .1385, .404, .52 });
        taxes2016.put(TAXES_OVER_64_INDEX, new double[]{ 0.1860, 0.2250, .404, .52 });

        HashMap taxes2017 = new HashMap();
        taxes2017.put(TAXES_RATE_NORMAL_INDEX, new double[]{ .3655, .408, .408, .52 });
        taxes2017.put(TAXES_WITHOUT_SOCIAL_INDEX, new double[]{ .0835, .1385, .408, .52 });
        taxes2017.put(TAXES_OVER_64_INDEX, new double[]{ .1865, .2290, .408, .52 });

        taxesRates.put(2015, taxes2015);
        taxesRates.put(2016, taxes2016);
        taxesRates.put(2017, taxes2017);

        HashMap taxesRatesYear = (HashMap) taxesRates.get(year);
        double[] currentTaxRates = (double[]) taxesRatesYear.get(TAXES_RATE_NORMAL_INDEX);

        if (!socialSecurity) {
            currentTaxRates = (double[]) taxesRatesYear.get(TAXES_WITHOUT_SOCIAL_INDEX);
        }

        return currentTaxRates;
    }

    //tax amount
    public static double getTaxAmount(double incomeTaxable, boolean socialSecurity, int year) {

        int[] taxAmountPeriods = TaxCalculatorHelper.getTaxAmountPeriods(year);

        double[] taxRates = TaxCalculatorHelper.getTaxRates(year, socialSecurity);

        double taxAmount = 0;
        for (int i = 0; i < taxRates.length - 1; i++) {
            if(incomeTaxable - taxAmountPeriods[i] < 0) {
                taxAmount += Math.floor(incomeTaxable * taxRates[i]);

                break;
            } else {
                taxAmount += Math.floor(taxAmountPeriods[i] * taxRates[i]);
                incomeTaxable -= taxAmountPeriods[i];
            }
        }

        return taxAmount;
    }

    // labor discount
    public static double getArbeidskorting(double incomeTaxable){
        if(incomeTaxable < 9147){
            return incomeTaxable * 1.793 / 100;
        }
        if(incomeTaxable < 19758){
            return 164 + (incomeTaxable - 9147) * 27.698 / 100;
        }
        if(incomeTaxable < 34015){
            return 3103;
        }
        if(incomeTaxable < 111590){
            return 3103 - (incomeTaxable - 34015) * 4 / 100;
        }

        return 0;
    }

    public static double applyHolidayAllowance(double annualIncome) {
        return annualIncome / 1.08; // -1.08%
    }

    public static double apply30PercentRuling(double incomeTaxable) {
        return incomeTaxable * 0.7;
    }

    // General discount
    public static double getAlgemeneHeffingskorting(double incomeTaxable) {
        if(incomeTaxable < 19922) { return 2242; }

        if(incomeTaxable < 66417){
            return 2242 - (incomeTaxable - 19922) * 4.822 / 100;
        }

        return 0;
    }
}

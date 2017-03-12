package com.cesiztel.dutax.models;


import com.cesiztel.dutax.helpers.TaxCalculatorHelper;

public class Income {
    private double mAnnual;
    private double mTaxable;
    private double mGeneralCredit;
    private double mLabourCredit;
    private double mGrossMoth;
    private double mNetYear;
    private double mNetMonth;
    private double mIncomeTax;
    private boolean mSocialSecurity = false;
    private boolean m30PercentRuling = false;
    private boolean mHolidaysAllowance = false;

    public Income(double annual) {
        mAnnual = annual;
        mTaxable = annual;
    }

    public double getAnnualTaxable() {
        return mTaxable;
    }

    public void setWithSocialSecurity() {
        mSocialSecurity = true;
    }

    public void set30PercentRuling() {
        m30PercentRuling = true;
    }

    public void setHolidaysAllowance() {
        mHolidaysAllowance = true;
    }

    public double getGeneralCredit() {
        return mGeneralCredit;
    }

    public double getLabourCredit() {
        return mLabourCredit;
    }

    public double getmGrossMoth() {
        return mGrossMoth;
    }

    public double getNetYear() {
        return mNetYear;
    }

    public double getNetMonth() {
        return mNetMonth;
    }

    public double getIncomeTax() {
        return mIncomeTax;
    }

    public void applyHolidaysAllowance() {
        mAnnual = TaxCalculatorHelper.applyHolidayAllowance(mAnnual);
        mTaxable = mAnnual;
    }

    public void apply30percentRuling() {
        mTaxable = TaxCalculatorHelper.apply30PercentRuling(mTaxable);
    }

    public void recalculateOverall() {
        if(mHolidaysAllowance) { applyHolidaysAllowance(); }

        if(m30PercentRuling) { apply30percentRuling(); }

        mGeneralCredit = TaxCalculatorHelper.getAlgemeneHeffingskorting(mTaxable);
        mLabourCredit = TaxCalculatorHelper.getArbeidskorting(mTaxable);
        mGrossMoth = mTaxable / 12;
        mNetYear = mAnnual - TaxCalculatorHelper.getTaxAmount(mTaxable, mSocialSecurity, 2017);
        mNetYear += mGeneralCredit + mLabourCredit;
        mNetMonth = mNetYear / 12;
        mIncomeTax = TaxCalculatorHelper.getTaxAmount(mTaxable, mSocialSecurity, 2017);
    }
}

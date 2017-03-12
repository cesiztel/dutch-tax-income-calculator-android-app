package com.cesiztel.dutax;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.Spanned;
import android.text.TextWatcher;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import com.cesiztel.dutax.models.Income;

import java.text.DecimalFormat;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity
    implements CheckBox.OnCheckedChangeListener {
    @BindView(R.id.annual_gross_income_text_view)
    EditText mAnnualIncomeGrossTextView;
    @BindView(R.id.holiday_allowance_check_box)
    CheckBox mHolidayAllowanceCheckBox;
    @BindView(R.id.social_security_check_box)
    CheckBox mSocialSecurityCheckBox;
    @BindView(R.id.thirty_percent_ruling_check_box)
    CheckBox m30PercentRulingCheckBox;
    @BindView(R.id.taxable_income_text_view)
    TextView mTaxableIncomeTextView;
    @BindView(R.id.income_tax_text_view)
    TextView mIncomeTaxTextView;
    @BindView(R.id.general_tax_text_view)
    TextView mGeneralTaxTextView;
    @BindView(R.id.labour_tax_text_view)
    TextView mLabourTaxTextView;
    @BindView(R.id.year_net_income_text_view)
    TextView mYearNetIncomeTextView;
    @BindView(R.id.monthly_net_income_text_view)
    TextView mMonthlyNetIncomeTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        // init action bar
        initActionBar();


        // Input text watcher to update the information
        mAnnualIncomeGrossTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                updateTaxableData();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        // Checkboxes option group
        mHolidayAllowanceCheckBox.setOnCheckedChangeListener(this);
        mSocialSecurityCheckBox.setChecked(true); // default activated
        mSocialSecurityCheckBox.setOnCheckedChangeListener(this);
        m30PercentRulingCheckBox.setOnCheckedChangeListener(this);

        // update the data
        updateTaxableData();
    }

    /**
     * Init action bar
     *
     */
    private void initActionBar() {
        if(getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.tax_calculation_year);
            getSupportActionBar().setElevation(0);
        }
    }

    /**
     * When some of the options for the calculation change
     * we should to recalculate all the vaoues
     *
     * @param buttonView The component clicked
     * @param isChecked  The current value checked or not
     */
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        updateTaxableData();
    }

    /**
     * Function to get the income gross from
     * the input as value to update the ui with
     * the calculation
     *
     * @return Value represents the income salary gross
     */
    private double getIncomeGross() {
        if(mAnnualIncomeGrossTextView == null) { return 0; }

        return (mAnnualIncomeGrossTextView
            .getText().toString().length() == 0) ? 0 : Double.valueOf(mAnnualIncomeGrossTextView
            .getText().toString());

    }

    /**
     * Open dialog information that show extra
     * information regarding the 30% ruling
     */
    @OnClick(R.id.thirty_percent_ruling_help_image_view)
    public void open30PercentRulingDialog() {
        Spanned result;
        String html = getResources().getString(R.string.about_ruling_alert_message);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            result = Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY);
        } else {
            result = Html.fromHtml(html);
        }

        new AlertDialog.Builder(this)
                .setTitle(R.string.about_ruling_alert_title)
                .setMessage(result)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create()
                .show();
    }

    /**
     * Update the information of the taxes based on the interface options and
     * call to update the UI with the new information
     *
     */
    private void updateTaxableData() {
        Income income = recalculateTaxes();

        if(income == null) { return; }

        updateUI(income);
    }

    private Income recalculateTaxes() {
        Income income = new Income(getIncomeGross());

        if(mHolidayAllowanceCheckBox.isChecked()) {
            income.setHolidaysAllowance();
        }

        if(m30PercentRulingCheckBox.isChecked()) {
            income.set30PercentRuling();
        }

        if(mSocialSecurityCheckBox.isChecked()) {
            income.setWithSocialSecurity();
        }

        income.recalculateOverall();

        return income;
    }

    /**
     * Update the UI with the calculations done
     * depends on income salary
     *
     */
    private void updateUI(Income income) {
        DecimalFormat df = new DecimalFormat("#.##");


        mTaxableIncomeTextView.setText(df.format(income.getAnnualTaxable()));
        mGeneralTaxTextView.setText(df.format(income.getGeneralCredit()));
        mLabourTaxTextView.setText(df.format(income.getLabourCredit()));
        mYearNetIncomeTextView.setText(df.format(income.getNetYear()));
        mIncomeTaxTextView.setText(df.format(income.getIncomeTax()));
        mMonthlyNetIncomeTextView.setText(df.format(income.getNetMonth()));
    }
}

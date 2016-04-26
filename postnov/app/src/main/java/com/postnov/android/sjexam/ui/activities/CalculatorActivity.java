package com.postnov.android.sjexam.ui.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.postnov.android.sjexam.CalculatorFormulaBuilder;
import com.postnov.android.sjexam.CalculatorFormulaEvaluator;
import com.postnov.android.sjexam.R;

import static com.postnov.android.sjexam.CalculatorFormulaEvaluator.EvaluateListener;

public class CalculatorActivity extends AppCompatActivity implements EvaluateListener
{

    public static final int INVALID_RES_ID = -1;
    private static final String CURRENT_STATE = "state";
    private static final String CURRENT_FORMULA = "formula";

    private TextView mResultView;
    private EditText mFormulaView;
    private Button mButtonDelete;

    private CalculatorFormulaEvaluator mEvaluator;

    private enum CalculatorStates
    {
        RESULT, ERROR, INPUT
    }

    private CalculatorStates mState;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calc);

        mEvaluator = new CalculatorFormulaEvaluator();
        initViews();

        if (savedInstanceState != null)
        {
            mFormulaView.setText(savedInstanceState.getString(CURRENT_FORMULA));
            int index = savedInstanceState.getInt(CURRENT_STATE);
            setState(CalculatorStates.values()[index]);
            onResult();
        }
        else
        {
            setState(CalculatorStates.INPUT);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        outState.putInt(CURRENT_STATE, mState.ordinal());
        outState.putString(CURRENT_FORMULA, mFormulaView.getText().toString());
    }

    private void initViews()
    {
        mButtonDelete = (Button) findViewById(R.id.delete);
        mResultView = (TextView) findViewById(R.id.result_text);
        mFormulaView = (EditText) findViewById(R.id.formula);

    }

    public void onButtonClick(View button)
    {
        switch (button.getId())
        {
            case R.id.divide:
                mFormulaView.append("/");
                break;

            case R.id.mult:
                mFormulaView.append("*");
                break;

            case R.id.delete:
                onTwoFaceButtonClick();
                break;

            case R.id.result_btn:
                onResult();
                break;

            case R.id.one:
            case R.id.two:
            case R.id.three:
            case R.id.four:
            case R.id.five:
            case R.id.six:
            case R.id.seven:
            case R.id.eight:
            case R.id.nine:
            case R.id.zero:
            case R.id.sum:
            case R.id.subtract:
            case R.id.comma:
                mFormulaView.append(((Button) button).getText());
                break;
        }
    }

    @Override
    public void onEvaluate(String formula, String result, int errorResId)
    {
        if (errorResId != INVALID_RES_ID)
        {
            onError(errorResId);
            return;
        }
        if (result.equals("Infinity") || result.equals("-Infinity"))
        {
            onError(R.string.error_infinity);
            return;
        }
        setState(CalculatorStates.RESULT);
        mResultView.setText(result);
    }

    private void onResult()
    {
        if (mFormulaView.length() > 0)
        {
            CharSequence result = new CalculatorFormulaBuilder(mFormulaView.getText());
            mEvaluator.evaluate(result, this);
        }
    }

    private void onClearFormula()
    {
        mFormulaView.getEditableText().clear();
        setState(CalculatorStates.INPUT);
    }

    private void onClearResult()
    {
        mResultView.setText("");
        setState(CalculatorStates.INPUT);
    }

    private void onDelete()
    {
        if (mFormulaView.length() > 0)
        {
            int index = mFormulaView.length() - 1;
            mFormulaView.getEditableText().delete(index, mFormulaView.length());
        }
    }

    private void onTwoFaceButtonClick()
    {
        switch (mState)
        {
            case RESULT:
                onClearFormula();
                onClearResult();
                break;

            case INPUT:
                onDelete();
                break;

            case ERROR:
                onDelete();
                onClearResult();
                break;
        }
    }

    private void onError(int errorResId)
    {
        setState(CalculatorStates.ERROR);
        mResultView.setText(errorResId);
    }

    private void setState(CalculatorStates state)
    {
        if (mState != state) mState = state;

        switch (mState)
        {
            case RESULT:
                mButtonDelete.setText(R.string.button_clr_text);
                break;

            case INPUT:
                mButtonDelete.setText(R.string.button_del_text);
                break;

            case ERROR:
                mButtonDelete.setText(R.string.button_del_text);
                break;
        }
    }
}

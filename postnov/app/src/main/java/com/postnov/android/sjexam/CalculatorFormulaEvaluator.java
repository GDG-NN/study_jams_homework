package com.postnov.android.sjexam;

import com.postnov.android.sjexam.ui.activities.CalculatorActivity;

import org.javia.arity.Symbols;
import org.javia.arity.SyntaxException;
import org.javia.arity.Util;

/**
 * Created by postnov on 15.04.2016.
 */
public class CalculatorFormulaEvaluator
{
    private static final int MAX_DIGITS = 12;
    private static final int ROUNDING_DIGITS = 2;

    private final Symbols mSymbols;

    public CalculatorFormulaEvaluator()
    {
        mSymbols = new Symbols();
    }

    public void evaluate(CharSequence formula, EvaluateListener listener)
    {
        evaluate(formula.toString(), listener);
    }

    public void evaluate(String formula, EvaluateListener listener)
    {
        //удаляем операторы в конце строки
        while (formula.length() > 0 && "+-/*".indexOf(formula.charAt(formula.length() - 1)) != -1)
        {
            formula = formula.substring(0, formula.length() - 1);
        }

        try
        {
            double result = mSymbols.eval(formula);
            if (Double.isNaN(result))
            {
                listener.onEvaluate(formula, null, R.string.error_nan);
            }
            else
            {
                final String resultString = Util.doubleToString(result, MAX_DIGITS, ROUNDING_DIGITS);
                listener.onEvaluate(formula, resultString, CalculatorActivity.INVALID_RES_ID);
            }
        }
        catch (SyntaxException e)
        {
            listener.onEvaluate(formula, null, R.string.error_syntax);
        }
    }

    public interface EvaluateListener
    {
        void onEvaluate(String formula, String result, int errorResId);
    }
}

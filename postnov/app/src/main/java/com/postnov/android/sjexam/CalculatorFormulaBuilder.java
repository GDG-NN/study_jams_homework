package com.postnov.android.sjexam;

import android.text.SpannableStringBuilder;
import android.text.TextUtils;

/**
 * Created by postnov on 15.04.2016.
 */
public class CalculatorFormulaBuilder extends SpannableStringBuilder
{
    public CalculatorFormulaBuilder(CharSequence text)
    {
        super(text);
    }

    @Override
    public SpannableStringBuilder replace(int start, int end, CharSequence tb, int tbstart, int tbend)
    {
        if (start != length() || end != length())
        {
            return super.replace(start, end, tb, tbstart, tbend);
        }

        String appendFormula = tb.subSequence(tbstart, tbend).toString();
        if (appendFormula.length() == 1)
        {
            final String formula = toString();
            switch (appendFormula.charAt(0))
            {
                case '.':
                    final int index = formula.lastIndexOf('.');
                    if (index != -1 && TextUtils.isDigitsOnly(formula.substring(index + 1, start)))
                    {
                        appendFormula = "";
                    }
                    break;
                case '+':
                case '*':
                case '/':
                    if (start == 0)
                    {
                        appendFormula = "";
                        break;
                    }
                    while (start > 0 && "+-*/".indexOf(formula.charAt(start - 1)) != -1)
                    {
                        --start;
                    }
                case '-':
                    if (start > 0 && "+-".indexOf(formula.charAt(start - 1)) != -1)
                    {
                        --start;
                    }
                    break;
                default:
                    break;
            }
        }
        if (appendFormula.length() > 0)
        {
            start = 0;
        }

        return super.replace(start, end, appendFormula, 0, appendFormula.length());
    }
}

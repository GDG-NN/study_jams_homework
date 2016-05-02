package zverevvv.android.moneykeeper.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import java.util.Date;
import java.util.UUID;

import zverevvv.android.moneykeeper.Payment;
import zverevvv.android.moneykeeper.database.PaymentDbSchema.PaymentTable;

/**
 * Created by Vasily on 02.05.2016.
 */
public class PaymentCursorWrapper extends CursorWrapper {

    public PaymentCursorWrapper(Cursor cursor){
        super(cursor);
    }

    public Payment getPayment(){
        String uuidString = getString(getColumnIndex(PaymentTable.Cols.UUID));
        String name = getString(getColumnIndex(PaymentTable.Cols.NAME));
        long date = getLong(getColumnIndex(PaymentTable.Cols.DATE));
        float sum = getFloat(getColumnIndex(PaymentTable.Cols.SUM));

        Payment payment = new Payment(UUID.fromString(uuidString));
        payment.setName(name);
        payment.setDate(new Date(date));
        payment.setSum(sum);

        return payment;
    }
}

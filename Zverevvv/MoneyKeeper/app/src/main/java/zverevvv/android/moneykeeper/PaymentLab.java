package zverevvv.android.moneykeeper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import zverevvv.android.moneykeeper.database.PaymentBaseHelper;
import zverevvv.android.moneykeeper.database.PaymentCursorWrapper;
import zverevvv.android.moneykeeper.database.PaymentDbSchema;
import zverevvv.android.moneykeeper.database.PaymentDbSchema.PaymentTable;

/**
 * Created by Vasily on 01.05.2016.
 */
public class PaymentLab {
    private static PaymentLab paymentLab;

    private Context context;
    private SQLiteDatabase database;

    public static PaymentLab get(Context context){
        if (paymentLab == null){
            paymentLab = new PaymentLab(context);
        }
        return paymentLab;
    }

    private PaymentLab(Context context){
        this.context = context.getApplicationContext();
        database = new PaymentBaseHelper(context).getWritableDatabase();

    }

    public List<Payment> getPayments(){
        List<Payment> payments = new ArrayList<>();

        PaymentCursorWrapper cursor = queryPayments(null, null);

        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()){
                payments.add(cursor.getPayment());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }
        return payments;
    }

    public Payment getPayment(UUID id){
        PaymentCursorWrapper cursor = queryPayments(PaymentTable.Cols.UUID + " = ?", new String[]{id.toString()});
        try {
            if (cursor.getCount() == 0){
                return null;
            }

            cursor.moveToFirst();
            return cursor.getPayment();
        } finally {
            cursor.close();
        }
    }

    public void addPayment(Payment payment){
        ContentValues values = getContentValues(payment);
        database.insert(PaymentTable.NAME, null, values);
    }

    private static ContentValues getContentValues(Payment payment){
        ContentValues values = new ContentValues();
        values.put(PaymentTable.Cols.UUID, payment.getId().toString());
        values.put(PaymentTable.Cols.NAME, payment.getName());
        values.put(PaymentTable.Cols.DATE, payment.getDate().getTime());
        values.put(PaymentTable.Cols.SUM, payment.getSum());

        return values;
    }

    public void updatePayent(Payment payment){
        String uuidString = payment.getId().toString();
        ContentValues values = getContentValues(payment);

        database.update(PaymentTable.NAME, values, PaymentTable.Cols.UUID + " = ?",new String[]{uuidString});
    }

    private PaymentCursorWrapper queryPayments(String whereClause, String[] whereArgs){
        Cursor cursor = database.query(PaymentTable.NAME, null, whereClause, whereArgs, null, null, null);
        return new PaymentCursorWrapper(cursor);
    }
}

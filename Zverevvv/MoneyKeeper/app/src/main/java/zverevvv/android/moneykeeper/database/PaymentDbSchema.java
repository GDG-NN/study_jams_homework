package zverevvv.android.moneykeeper.database;

/**
 * Created by Vasily on 02.05.2016.
 */
public class PaymentDbSchema {
    public static final class PaymentTable{
        public static final String NAME  = "payments";
        public static final class Cols {
            public static final String UUID = "uuid";
            public static final String NAME = "name";
            public static final String DATE = "date";
            public static final String SUM = "sum";
        }
    }


}

package zverevvv.android.moneykeeper;

import android.support.v4.app.Fragment;

/**
 * Created by Vasily on 01.05.2016.
 */
public class PaymentListActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        return new PaymentListFragment();
    }
}

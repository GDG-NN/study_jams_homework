package zverevvv.android.moneykeeper;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringDef;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.ListFragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import java.util.List;
import java.util.UUID;

/**
 * Created by Vasily on 02.05.2016.
 */
public class PaymentPagerActivity extends AppCompatActivity {
    private static final String EXTRA_PAYMENT_ID = "verevvv.android.moneykeeper.payment_id";

    private ViewPager viewPager;
    private List<Payment> payments;

    public static Intent newIntent(Context packageContext, UUID paymentId){
        Intent intent = new Intent(packageContext, PaymentPagerActivity.class);
        intent.putExtra(EXTRA_PAYMENT_ID, paymentId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_pager);

        UUID paymentId = (UUID) getIntent().getSerializableExtra(EXTRA_PAYMENT_ID);

        viewPager = (ViewPager) findViewById(R.id.activity_payment_pager_view_pager);

        payments = PaymentLab.get(this).getPayments();

        FragmentManager fragmentManager = getSupportFragmentManager();
        viewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {
            @Override
            public Fragment getItem(int position) {
                Payment payment = payments.get(position);
                return PaymentFragment.newInstance(payment.getId());
            }

            @Override
            public int getCount() {
                return payments.size();
            }
        });

        for (int i = 0; i < payments.size(); i++) {
            if (payments.get(i).getId().equals(paymentId)){
                viewPager.setCurrentItem(i);
                break;
            }
        }
    }
}

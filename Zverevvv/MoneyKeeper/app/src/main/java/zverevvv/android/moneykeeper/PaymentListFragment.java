package zverevvv.android.moneykeeper;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * Created by Vasily on 02.05.2016.
 */
public class PaymentListFragment extends Fragment {
    private RecyclerView paymentRecyclerView;
    private PaymentAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_item_new_payment:
                Payment payment = new Payment();
                PaymentLab.get(getActivity()).addPayment(payment);

                Intent intent = PaymentPagerActivity.newIntent(getActivity(), payment.getId());
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_payment_list, container, false);

        paymentRecyclerView = (RecyclerView) view.findViewById(R.id.payment_recycler_view);
        paymentRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        updateUI();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_payment_list, menu);
    }

    private void updateUI(){
        PaymentLab paymentLab = PaymentLab.get(getActivity());
        List<Payment> payments = paymentLab.getPayments();

        if (adapter == null) {
            adapter = new PaymentAdapter(payments);
            paymentRecyclerView.setAdapter(adapter);
        } else {
            adapter.setPayments(payments);
            adapter.notifyDataSetChanged();
        }
    }

    private class PaymentHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public TextView sumTextView;
        public TextView nameTextView;
        public TextView dateTextView;

        private Payment payment;

        public PaymentHolder(View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);

            sumTextView = (TextView) itemView.findViewById(R.id.list_item_payment_sum);
            nameTextView = (TextView) itemView.findViewById(R.id.list_item_payment_name);
            dateTextView = (TextView) itemView.findViewById(R.id.list_item_payment_date);
        }

        @Override
        public void onClick(View v) {
            Intent intent = PaymentPagerActivity.newIntent(getActivity(), payment.getId());
            startActivity(intent);
        }

        public void bindPayment(Payment payment){
            //Do i need this?
            this.payment = payment;
            nameTextView.setText(payment.getName());
            sumTextView.setText(String.valueOf(payment.getSum()));
            dateTextView.setText(payment.getDate().toString());
        }
    }

    private class PaymentAdapter extends RecyclerView.Adapter<PaymentHolder>{

        private List<Payment> payments;

        public PaymentAdapter(List<Payment> payments) {
            this.payments = payments;
        }

        @Override
        public PaymentHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.list_item_payment, parent, false);
            return new PaymentHolder(view);
        }

        @Override
        public void onBindViewHolder(PaymentHolder holder, int position) {
            Payment payment = payments.get(position);
            holder.bindPayment(payment);
        }

        @Override
        public int getItemCount() {
            return payments.size();
        }

        public void setPayments(List<Payment> payments){
            this.payments= payments;
        }
    }
}

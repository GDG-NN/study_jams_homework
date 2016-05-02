package zverevvv.android.moneykeeper;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import java.util.Date;
import java.util.UUID;

/**
 * Created by Vasily on 01.05.2016.
 */
public class PaymentFragment extends Fragment {

    public static final String ARG_PAYMENT_ID = "payment_id";
    public static final String DIALOG_DATE = "Dialog_date";
    public static final int REQUEST_DATE = 0;
    private Payment payment;
    private EditText nameField;
    private Button dateButton;
    private EditText sumField;

    public static PaymentFragment newInstance(UUID paymentId){
        Bundle args = new Bundle();
        args.putSerializable(ARG_PAYMENT_ID, paymentId);

        PaymentFragment fragment = new PaymentFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UUID paymentId = (UUID) getArguments().getSerializable(ARG_PAYMENT_ID);
        payment = PaymentLab.get(getActivity()).getPayment(paymentId);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_payment, container, false);

        nameField = (EditText) v.findViewById(R.id.payment_name);
        nameField.setText(payment.getName());
        nameField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                payment.setName(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        dateButton = (Button) v.findViewById(R.id.payment_date);
        dateButton.setText(payment.getDate().toString());
        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getFragmentManager();
                DatePickerFragment dialog = DatePickerFragment.newIntance(payment.getDate());
                dialog.setTargetFragment(PaymentFragment.this, REQUEST_DATE);
                dialog.show(manager, DIALOG_DATE);
            }
        });

        sumField = (EditText) v.findViewById(R.id.payment_sum);
        sumField.setText(String.valueOf(payment.getSum()));
        sumField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    payment.setSum(Float.valueOf(s.toString()));
                } catch (Exception e){
                    //later
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode != Activity.RESULT_OK){
            return;
        }

        if (requestCode == REQUEST_DATE){
            Date date = (Date) data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            payment.setDate(date);
            dateButton.setText(payment.getDate().toString());
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        PaymentLab.get(getActivity()).updatePayent(payment);
    }
}

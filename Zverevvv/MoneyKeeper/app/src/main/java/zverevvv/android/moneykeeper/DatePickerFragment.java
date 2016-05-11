package zverevvv.android.moneykeeper;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by Vasily on 02.05.2016.
 */
public class DatePickerFragment extends DialogFragment {

    private static final String ARD_DATE = "date";
    public static final String EXTRA_DATE = "zverevvv.android.moneykeeper.date";

    private DatePicker datePicker;

    public static DatePickerFragment newIntance(Date date){
        Bundle args = new Bundle();
        args.putSerializable(ARD_DATE, date);

        DatePickerFragment datePickerFragment = new DatePickerFragment();
        datePickerFragment.setArguments(args);
        return datePickerFragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Date date = (Date) getArguments().getSerializable(ARD_DATE);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);

        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_date, null);

        datePicker = (DatePicker) view.findViewById(R.id.dialog_date_date_picker);
        datePicker.init(year, month, day, null);

        return new AlertDialog.Builder(getActivity()).setView(view).setTitle(R.string.data_picker_title).setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int year = datePicker.getYear();
                int month = datePicker.getMonth();
                int dat = datePicker.getDayOfMonth();

                Date date = new GregorianCalendar(year, month, day).getTime();
                sendResult(Activity.RESULT_OK, date);
            }
        }).create();
    }

    private void sendResult(int resultCode, Date date){
        if (getTargetFragment() == null)
            return;

        Intent intent = new Intent();
        intent.putExtra(EXTRA_DATE, date);

        getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, intent);
    }
}

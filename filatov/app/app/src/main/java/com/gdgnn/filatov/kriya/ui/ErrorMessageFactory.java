package com.gdgnn.filatov.kriya.ui;

import android.content.Context;

import com.gdgnn.filatov.kriya.R;
import com.gdgnn.filatov.kriya.exception.MessageNotFoundException;
import com.gdgnn.filatov.kriya.exception.NetworkException;

public class ErrorMessageFactory {

    private ErrorMessageFactory() {}

    public static String create(Context context, Throwable exception) {
        String message = context.getString(R.string.error__message_default);

        if (exception instanceof NetworkException)
            message = context.getString(R.string.error__message_no_connection);
        else if (exception instanceof MessageNotFoundException)
            message = context.getString(R.string.error__message_not_found);

        return message;
    }
}

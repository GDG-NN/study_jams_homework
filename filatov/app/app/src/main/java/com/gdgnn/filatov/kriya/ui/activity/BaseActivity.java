package com.gdgnn.filatov.kriya.ui.activity;

import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gdgnn.filatov.kriya.ui.ErrorMessageFactory;

public class BaseActivity extends AppCompatActivity {

    private RelativeLayout progressBar;
    private TextView errorText;
    private Button errorButtonRetry;

    protected void setupLoadingView(RelativeLayout progressBar) {
        this.progressBar = progressBar;
    }

    protected void setupErrorView(TextView errorText, Button errorButtonRetry) {
        this.errorText = errorText;
        this.errorButtonRetry = errorButtonRetry;
    }

    public void showLoading() {
        if (this.progressBar != null)
            this.progressBar.setVisibility(View.VISIBLE);
    }

    public void hideLoading() {
        if (this.progressBar != null)
            progressBar.setVisibility(View.GONE);
    }

    public void showError(Throwable e, boolean showButtonRetry) {
        if (this.errorText != null) {
            errorText.setText(ErrorMessageFactory.create(this, e));
            errorText.setVisibility(View.VISIBLE);
        }

        if (this.errorButtonRetry != null && showButtonRetry)
            this.errorButtonRetry.setVisibility(View.VISIBLE);
    }

    public void hideError() {
        if (this.errorText != null)
            errorText.setVisibility(View.GONE);

        if (this.errorButtonRetry != null)
            errorButtonRetry.setVisibility(View.GONE);
    }
}

package com.gdgnn.filatov.kriya.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gdgnn.filatov.kriya.R;
import com.gdgnn.filatov.kriya.data.Repository;
import com.gdgnn.filatov.kriya.data.RepositoryListener;
import com.gdgnn.filatov.kriya.model.MessageModel;
import com.gdgnn.filatov.kriya.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MessageDetailActivity extends com.gdgnn.filatov.kriya.ui.activity.BaseActivity {

    private static final String INTENT_EXTRA_PARAM_MESSAGE_ID = "com.gdgnn.filatov.kriya.INTENT_PARAM_ARTIST_ID";
    private static final String INSTANCE_STATE_PARAM_MESSAGE_ID = "com.gdgnn.filatov.kriya.STATE_PARAM_ARTIST_ID";

    private int messageId;

    @Bind(R.id.message_detail__title)
    TextView title;
    @Bind(R.id.message_detail__place)
    TextView place;
    @Bind(R.id.message_detail__text)
    TextView text;
    @Bind(R.id.progress_bar)
    RelativeLayout progressBar;
    @Bind(R.id.error__text)
    TextView errorText;
    @Bind(R.id.error__button_retry)
    Button errorButtonRetry;

    public static Intent getCallingIntent(Context context, int messageId) {
        Intent callingIntent = new Intent(context, MessageDetailActivity.class);
        callingIntent.putExtra(INTENT_EXTRA_PARAM_MESSAGE_ID, messageId);
        return callingIntent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_detail);
        ButterKnife.bind(this);

        this.setupLoadingView(this.progressBar);
        this.setupErrorView(this.errorText, this.errorButtonRetry);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
            actionBar.setDisplayHomeAsUpEnabled(true);

        if (savedInstanceState == null)
            this.messageId = getIntent().getIntExtra(INTENT_EXTRA_PARAM_MESSAGE_ID, 0);
        else
            this.messageId = savedInstanceState.getInt(INSTANCE_STATE_PARAM_MESSAGE_ID);

        this.showLoading();
        this.getMessage();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(INSTANCE_STATE_PARAM_MESSAGE_ID, this.messageId);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void getMessage() {
        Repository.getInstance().getMessage(this.messageId, new RepositoryListener.GetMessage() {
            @Override
            public void onSuccess(MessageModel message) {
                MessageDetailActivity.this.hideLoading();
                MessageDetailActivity.this.renderMessage(message);
            }

            @Override
            public void onError(Throwable e) {
                MessageDetailActivity.this.hideLoading();
                MessageDetailActivity.this.showError(e, true);
            }
        });
    }

    private void renderMessage(MessageModel message) {
        setTitle(message.getDate());

        String title = message.getTitle();
        if (!TextUtils.isEmpty(title))
            this.title.setText(message.getTitle());
        else
            this.title.setVisibility(View.GONE);

        List<String> placeChunks = new ArrayList<>();
        if (!TextUtils.isEmpty(message.getPlace()))
            placeChunks.add(message.getPlace());
        if (!TextUtils.isEmpty(message.getCountry()))
            placeChunks.add(message.getCountry());

        if (placeChunks.size() > 0)
            this.place.setText(StringUtils.join(", ", placeChunks));
        else
            this.place.setVisibility(View.GONE);

        this.text.setText(Html.fromHtml(message.getText()));
    }

    @OnClick(R.id.error__button_retry)
    void onButtonRetryClick() {
        MessageDetailActivity.this.hideError();
        MessageDetailActivity.this.showLoading();
        MessageDetailActivity.this.getMessage();
    }
}

package com.gdgnn.filatov.kriya.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gdgnn.filatov.kriya.data.Repository;
import com.gdgnn.filatov.kriya.data.RepositoryListener;
import com.gdgnn.filatov.kriya.ui.adapter.MessageAdapter;
import com.gdgnn.filatov.kriya.R;
import com.gdgnn.filatov.kriya.model.MessageModel;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MessageListActivity extends BaseActivity implements MessageAdapter.OnItemClickListener {

    @Bind(R.id.progress_bar)
    RelativeLayout progressBar;
    @Bind(R.id.error__text)
    TextView errorText;
    @Bind(R.id.error__button_retry)
    Button errorButtonRetry;

    private RecyclerView mRecycler;
    private MessageAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_list);
        ButterKnife.bind(this);

        this.setupLoadingView(this.progressBar);
        this.setupErrorView(this.errorText, this.errorButtonRetry);

        setTitle(getString(R.string.activity_title__message_list));

        this.showLoading();
        this.setupRecyclerView();

        this.getMessages();
    }

    private void setupRecyclerView() {
        this.mRecycler = (RecyclerView) findViewById(R.id.message_list__recycler);
        this.mAdapter = new MessageAdapter(this);

        this.mRecycler.setLayoutManager(new LinearLayoutManager(this));
        this.mRecycler.setAdapter(mAdapter);
        this.mRecycler.addItemDecoration(
                new HorizontalDividerItemDecoration.Builder(this)
                        .color(getResources().getColor(R.color.message_list__item_divider))
                        .sizeResId(R.dimen.message_list__item_divider_height)
                        .build()
        );
    }

    private void getMessages() {
        Repository.getInstance().getMessages(new RepositoryListener.GetMessages() {
            @Override
            public void onSuccess(List<MessageModel> messages) {
                MessageListActivity.this.hideLoading();

                if (MessageListActivity.this.mAdapter != null) {
                    MessageListActivity.this.mAdapter.setMessages(messages);
                    MessageListActivity.this.mAdapter.setOnItemClickListener(MessageListActivity.this);
                }
            }

            @Override
            public void onError(Throwable e) {
                MessageListActivity.this.hideLoading();
                MessageListActivity.this.showError(e, true);
            }
        });
    }

    @Override
    public void onMessageItemClicked(MessageModel messageModel) {
        startActivity(MessageDetailActivity.getCallingIntent(this, messageModel.getId()));
    }

    @OnClick(R.id.error__button_retry)
    void onButtonRetryClick() {
        MessageListActivity.this.hideError();
        MessageListActivity.this.showLoading();
        MessageListActivity.this.getMessages();
    }
}

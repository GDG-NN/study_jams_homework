package com.gdgnn.filatov.kriya.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gdgnn.filatov.kriya.R;
import com.gdgnn.filatov.kriya.model.MessageModel;
import com.gdgnn.filatov.kriya.utils.AnimationUtils;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {

    public interface OnItemClickListener {
        void onMessageItemClicked(MessageModel messageModel);
    }
    private OnItemClickListener onItemClickListener;

    private Context context;
    private final LayoutInflater layoutInflater;
    private List<MessageModel> messages;

    public MessageAdapter(Context context) {
        this.context = context;
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.messages = Collections.emptyList();
    }

    @Override
    public int getItemCount() {
        return (this.messages != null) ? this.messages.size() : 0;
    }

    @Override
    public MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = this.layoutInflater.inflate(R.layout.list_item_message, parent, false);
        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MessageViewHolder holder, final int position) {
        final MessageModel message = this.messages.get(position);

        holder.date.setText(message.getDate());

        String title = message.getTitle();
        holder.title.setText((!TextUtils.isEmpty(title)) ? title : this.context.getString(R.string.messages_list__item_title_default));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MessageAdapter.this.onItemClickListener != null)
                    MessageAdapter.this.onItemClickListener.onMessageItemClicked(message);
            }
        });

        holder.itemView.startAnimation(AnimationUtils.createAlpha(0, 1, 1000));
    }

    @Override
    public void onViewDetachedFromWindow(MessageViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        holder.itemView.clearAnimation();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void setMessages(Collection<MessageModel> messages) {
        this.validateMessages(messages);
        this.messages = (List<MessageModel>) messages;
        this.notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    private void validateMessages(Collection<MessageModel> messages) {
        if (messages == null)
            throw new IllegalArgumentException("The list cannot be null");
    }

    static class MessageViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.item_message__date)
        TextView date;

        @Bind(R.id.item_message__title)
        TextView title;

        public MessageViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}

package com.gdgnn.filatov.kriya.data;

import com.gdgnn.filatov.kriya.exception.MessageNotFoundException;
import com.gdgnn.filatov.kriya.exception.NetworkException;
import com.gdgnn.filatov.kriya.model.MessageModel;
import com.gdgnn.filatov.kriya.net.Api;
import com.gdgnn.filatov.kriya.net.ApiEndpointInterface;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Repository {

    private Repository() {}

    private List<MessageModel> messages = null;

    public void setMessages(List<MessageModel> messages) {
        this.messages = messages;
    }

    public void getMessages(final RepositoryListener.GetMessages listener) {
        if (this.messages != null) {
            listener.onSuccess(this.messages);
        } else {
            ApiEndpointInterface apiService = Api.getInstance().create(ApiEndpointInterface.class);
            Call<List<MessageModel>> call = apiService.getMessages();
            call.enqueue(new Callback<List<MessageModel>>() {
                @Override
                public void onResponse(Call<List<MessageModel>> call, Response<List<MessageModel>> response) {
                    List<MessageModel> messages = response.body();

                    listener.onSuccess(messages);
                    Repository.this.setMessages(messages);
                }

                @Override
                public void onFailure(Call<List<MessageModel>> call, Throwable t) {
                    listener.onError(new NetworkException());
                }
            });
        }
    }

    public void getMessage(final int messageId, final RepositoryListener.GetMessage listener) {
        if (this.messages != null) {
            this.findMessageInList(this.messages, messageId, listener);
        } else {
            this.getMessages(new RepositoryListener.GetMessages() {
                @Override
                public void onSuccess(List<MessageModel> messages) {
                    Repository.this.findMessageInList(messages, messageId, listener);
                }

                @Override
                public void onError(Throwable e) {
                    listener.onError(e);
                }
            });
        }
    }

    private void findMessageInList(List<MessageModel> messages, int messageId, RepositoryListener.GetMessage listener) {
        boolean hasMessage = false;

        for (MessageModel message: messages)
            if (message.getId() == messageId) {
                hasMessage = true;
                listener.onSuccess(message);
                break;
            }

        if (!hasMessage)
            listener.onError(new MessageNotFoundException());
    }

    private static Repository instance = null;

    public static Repository getInstance() {
        if (instance == null)
            instance = new Repository();

        return instance;
    }
}

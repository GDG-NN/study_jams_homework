package com.gdgnn.filatov.kriya.data;

import com.gdgnn.filatov.kriya.model.MessageModel;

import java.util.List;

public class RepositoryListener {

    public interface GetMessages{
        void onSuccess(List<MessageModel> messages);
        void onError(Throwable e);
    }

    public interface GetMessage {
        void onSuccess(MessageModel message);
        void onError(Throwable e);
    }
}

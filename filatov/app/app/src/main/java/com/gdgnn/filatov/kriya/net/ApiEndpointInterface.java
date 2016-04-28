package com.gdgnn.filatov.kriya.net;

import com.gdgnn.filatov.kriya.model.MessageModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;

public interface ApiEndpointInterface {

    String HEADER_CONTENT_TYPE = "Content-Type: application/json; charset=utf-8";

    @Headers({HEADER_CONTENT_TYPE})
    @GET("/")
    Call<List<MessageModel>> getMessages();
}

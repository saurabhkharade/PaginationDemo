package com.saurabhkharade.paginationdemo.Retrofit;

import com.saurabhkharade.paginationdemo.Model.Post;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface PostsApi {
    @GET("posts")
    Call<List<Post>> getPosts(@Query("_start") int start, @Query("_limit") int limit);
}

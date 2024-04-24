package com.saurabhkharade.paginationdemo.MainApp;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.saurabhkharade.paginationdemo.Model.Post;
import com.saurabhkharade.paginationdemo.PostAdapter;
import com.saurabhkharade.paginationdemo.R;
import com.saurabhkharade.paginationdemo.Retrofit.PostRetrofitClient;
import com.saurabhkharade.paginationdemo.Retrofit.PostsApi;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private PostAdapter postAdapter;
    private List<Post> posts = new ArrayList<>();
    private static final int PAGE_SIZE = 15;
    private ProgressBar loadingPB;
    private static final String BASE_URL = "https://jsonplaceholder.typicode.com/";
    private PostsApi postsApi;
    private NestedScrollView nestedSV;
    int page = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.idRVUsers);
        loadingPB = findViewById(R.id.idPBLoading);
        nestedSV = findViewById(R.id.idNestedSV);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        postAdapter = new PostAdapter(posts,MainActivity.this);
        recyclerView.setAdapter(postAdapter);

        // Setup scroll listener
        postsApi = PostRetrofitClient.getClient(BASE_URL).create(PostsApi.class);

        getDataFromAPI(page);
        nestedSV.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY == v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight()) {
                        page++;
                    getDataFromAPI(page);
                }
            }
        });

    }


    private void getDataFromAPI(int page) {
        loadingPB.setVisibility(View.VISIBLE);
        int start = page * PAGE_SIZE;
        Call<List<Post>> call = postsApi.getPosts(start, PAGE_SIZE);
        call.enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {

                if (response.isSuccessful()) {
                    loadingPB.setVisibility(View.VISIBLE);

                    List<Post> newPosts = response.body();
                    posts.addAll(newPosts);
                    postAdapter.notifyDataSetChanged();
                    Log.e("MainActivity", "Fetched " + newPosts.size() + " posts");

                    if (newPosts.size() < PAGE_SIZE) {
                        loadingPB.setVisibility(View.GONE);
                        Toast.makeText(MainActivity.this, "All data loaded", Toast.LENGTH_SHORT).show();
                    } else {
                        loadingPB.setVisibility(View.VISIBLE);
                    }

                    if (posts.size() % 15 == 0) {
                        loadingPB.setVisibility(View.GONE);
                    }else{
                        loadingPB.setVisibility(View.VISIBLE);

                    }

                } else {
                    Toast.makeText(MainActivity.this, "Error fetching posts", Toast.LENGTH_SHORT).show();
                    Log.e("MainActivity", "Error fetching posts: " + response.message());
                    loadingPB.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("MainActivity", "Error: " + t.getMessage());
                loadingPB.setVisibility(View.GONE); // Hide progress bar on failure
            }
        });
    }

}

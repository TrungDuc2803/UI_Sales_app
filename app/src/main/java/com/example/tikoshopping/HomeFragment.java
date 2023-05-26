package com.example.tikoshopping;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.tikoshopping.API.APIPostSales;
import com.example.tikoshopping.API.APITypeGoods;
import com.example.tikoshopping.Service.ResultPostSales;
import com.example.tikoshopping.Service.ResultRecommended;
import com.example.tikoshopping.Service.ResultTypeGoods;
import com.example.tikoshopping.adapters.PostSalesAdapter;
import com.example.tikoshopping.adapters.PostsByTypeAdapter;
import com.example.tikoshopping.adapters.RecommenAdapter;
import com.example.tikoshopping.adapters.TypeGoodsAdapter;
import com.example.tikoshopping.adapters.ViewPagerAdapter;
import com.example.tikoshopping.models.PostSales;
import com.example.tikoshopping.models.RecommendedModel;
import com.example.tikoshopping.models.TypeGoods;
import com.example.tikoshopping.models.ViewAllModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    //popular Items
    private RecyclerView recyclerViewPosts;
    private ArrayList<PostSales> posts ;
    private PostSalesAdapter postSalesAdapter ;

    // home category
    private RecyclerView recyclerViewType;
    private TypeGoodsAdapter typeAdapter ;
    private ArrayList<TypeGoods> typeGoods;

    // recommended

    private RecyclerView recommendRecyclerView;
    private ArrayList<PostSales> recommendPosts = new ArrayList<PostSales>();
    private RecommenAdapter recommenAdapter;

    /// Search view

    EditText search_box;
    private ArrayList<PostSales> resultSearch = new ArrayList<PostSales>();
    private RecyclerView recyclerViewSearch;
    private PostSalesAdapter searchAdapter;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_home_fragment,container,false);

        recyclerViewType = view.findViewById(R.id.exp_rec);
        recyclerViewPosts = view.findViewById(R.id.pop_rec);
        recommendRecyclerView = view.findViewById(R.id.reccommended_rec);






        //search view
        recyclerViewSearch = view.findViewById(R.id.search_rec);
        search_box = view.findViewById(R.id.search_box);
        recyclerViewSearch.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewSearch.setAdapter(searchAdapter);
        recyclerViewSearch.setHasFixedSize(true);

        search_box.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(editable.toString().trim() == ""){
                    resultSearch.clear();
                    searchAdapter.notifyDataSetChanged();
                    return;
                }
                searchProduct(editable.toString());
            }
        });

        CallAPITypeGood();
        CallAPIPostSales();
        CallAPIRecommended();


        return view;
    }


    public void CallAPITypeGood(){

        SharedPreferences sharedPreferences = getContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("token", null);

        APITypeGoods.apiService.getAllType("Bearer " +token).enqueue(new Callback<ResultTypeGoods>() {
            @Override
            public void onResponse(Call<ResultTypeGoods> call, Response<ResultTypeGoods> response) {
                    ResultTypeGoods result  = response.body();
                    if(result != null && result.getResult() ){
                        typeGoods = result.getData();
                        typeAdapter = new TypeGoodsAdapter(typeGoods,getContext());
                        recyclerViewType.setAdapter(typeAdapter);
                        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
                        recyclerViewType.setLayoutManager(layoutManager);
                    }

            }

            @Override
            public void onFailure(Call<ResultTypeGoods> call, Throwable t) {
                Log.e("API: ", t.getMessage());
            }
        });
    }


    public void CallAPIPostSales(){

        SharedPreferences sharedPreferences = getContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("token", null);

        APIPostSales.apiService.getPostSalesRandom("Bearer "+token,10).enqueue(new Callback<ResultPostSales>() {
            @Override
            public void onResponse(Call<ResultPostSales> call, Response<ResultPostSales> response) {
                ResultPostSales result = response.body();
                if(result != null && result.getResult() ){
                    Log.e("API postsales" ,response.body().toString());
                    posts = result.getData();
                    postSalesAdapter = new PostSalesAdapter(posts,getContext());
                    recyclerViewPosts.setAdapter(postSalesAdapter);
                    LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
                    recyclerViewPosts.setLayoutManager(layoutManager);

                }
                else{
                    Log.e("API postsales" ,"khong co du lieu");
                }
            }

            @Override
            public void onFailure(Call<ResultPostSales> call, Throwable t) {
                Log.e("API postsales", t.getMessage());
            }
        });
    }



    private void searchProduct(String inputSearch) {

        SharedPreferences sharedPreferences = getContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("token", null);

        APIPostSales.apiService.findIncludeName("Bearer " + token, inputSearch).enqueue(new Callback<ResultPostSales>() {
            @Override
            public void onResponse(Call<ResultPostSales> call, Response<ResultPostSales> response) {
                ResultPostSales res = response.body();

                if(res != null && res.getResult()){

                    resultSearch.clear();

                    resultSearch = res.getData();

                    searchAdapter = new PostSalesAdapter(resultSearch, getContext());

                    recyclerViewSearch.setAdapter(searchAdapter);
                    LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
                    recyclerViewSearch.setLayoutManager(layoutManager);


                }
                else {

                    Log.e("Search", "Call API that bai");
                }
            }

            @Override
            public void onFailure(Call<ResultPostSales> call, Throwable t) {
                Log.e("Search", t.getMessage());
            }
        });
    }


    public void CallAPIRecommended(){

        SharedPreferences sharedPreferences = getContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("token", null);

        APIPostSales.apiService.getPostSalesRandom("Bearer "+token,10).enqueue(new Callback<ResultPostSales>() {
            @Override
            public void onResponse(Call<ResultPostSales> call, Response<ResultPostSales> response) {
                ResultPostSales result = response.body();
                if(result != null && result.getResult() && result.getData().size() > 0 ){
                    Log.e("post", result.getData().get(0).getDescription());
                    recommendPosts = result.getData();
                    recommenAdapter = new RecommenAdapter(recommendPosts,getContext());
                    recommendRecyclerView.setAdapter(recommenAdapter);
                    LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
                    recommendRecyclerView.setLayoutManager(layoutManager);

                }
                else{
                    Log.e("API postsales" ,"khong co du lieu");
                }
            }

            @Override
            public void onFailure(Call<ResultPostSales> call, Throwable t) {
                Log.e("API postsales", t.getMessage());
            }
        });

    }
}
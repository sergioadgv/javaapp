package com.example.draganddrop.dialog;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.draganddrop.R;
import com.example.draganddrop.adapter.AllPuzzleAdapter;
import com.example.draganddrop.model.puzzle_model.PuzzleModel;
import com.example.draganddrop.network.Api;
import com.example.draganddrop.network.NetworkClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AllPuzzlesDialog extends Dialog {
    Context context;
    ProgressDialog progressDialog;
    RecyclerView puzzleRecycler;

    public AllPuzzlesDialog(@NonNull Context context) {
        super(context);
        this.context = context;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_all_puzzle);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
        puzzleRecycler = findViewById(R.id.all_puzzle_recycler);
        puzzleRecycler.setLayoutManager(new LinearLayoutManager(context));
        progressDialog = new ProgressDialog(context);
        getAllPuzzles();


    }

    void getAllPuzzles(){
        progressDialog.show();
        Api api = NetworkClient.getRetrofitClient().create(Api.class);
        Call<PuzzleModel> call = api.getAllPuzzle("1");
        call.enqueue(new Callback<PuzzleModel>() {
            @Override
            public void onResponse(Call<PuzzleModel> call, Response<PuzzleModel> response) {
                progressDialog.cancel();
                AllPuzzleAdapter allPuzzleAdapter = new AllPuzzleAdapter(context,response.body());

                puzzleRecycler.setAdapter(allPuzzleAdapter);


            }

            @Override
            public void onFailure(Call<PuzzleModel> call, Throwable t) {
                progressDialog.cancel();

            }
        });
    }

}

package com.example.draganddrop.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.example.draganddrop.R;
import com.squareup.picasso.Picasso;

public class HintDialog extends Dialog {
    String imageUrl ;
    ImageView imageView;
    ImageView close_btn;
    public HintDialog(@NonNull Context context,String imageUrl) {
        super(context);
        this.imageUrl = imageUrl;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hint_dialog);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        imageView = findViewById(R.id.hint_image);
        close_btn = findViewById(R.id.close);
        Picasso.get().load(imageUrl).fit().placeholder(R.drawable.progress_animation).into(imageView);

        close_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

    }
}

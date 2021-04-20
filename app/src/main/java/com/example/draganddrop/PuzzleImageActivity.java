package com.example.draganddrop;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.ClipData;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.draganddrop.db.SharePrefManager;
import com.example.draganddrop.dialog.HintDialog;
import com.example.draganddrop.model.puzzle_image_model.PuzzleImageModel;
import com.example.draganddrop.model.puzzle_image_model.PuzzleImageModelItem;
import com.example.draganddrop.network.Api;
import com.example.draganddrop.network.NetworkClient;
import com.example.draganddrop.services.CountUpTimer;
import com.squareup.picasso.Picasso;

import java.util.Collections;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class PuzzleImageActivity extends AppCompatActivity {

    private View[] views;
    ImageView rlStar,rl1,rl2,rl3,rl4,rl5,rl6,rl7,rl8,rl9,rl10,rl11,rl12,rl13,rl14,rl15,rl16,rl17,rl18,rl19,rl20,rl21,rl22,rl23,rl24;
    CardView hindBtn,closeBtn;
    PuzzleImageModel puzzleImageModel = new PuzzleImageModel();
    String hintUrl="0";
    long  openTime;
    long closeTime;
    TextView timerTxt;
    int score = 0;
    SharePrefManager sharePrefManager;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_puzzle);
        openTime = System.currentTimeMillis()/1000;
        sharePrefManager = new SharePrefManager(this);


        rl1 = findViewById(R.id.rl_1);
        rl2 = findViewById(R.id.rl_2);
        rl3 = findViewById(R.id.rl_3);
        rl4 = findViewById(R.id.rl_4);
        rl5 = findViewById(R.id.rl_5);
        rl6 = findViewById(R.id.rl_6);
        rl7 = findViewById(R.id.rl_7);
        rl8 = findViewById(R.id.rl_8);
        rl9 = findViewById(R.id.rl_9);
        rl10 = findViewById(R.id.rl_10);
        rl11 = findViewById(R.id.rl_11);
        rl12 = findViewById(R.id.rl_12);
        rl13 = findViewById(R.id.rl_13);
        rl14 = findViewById(R.id.rl_14);
        rl15 = findViewById(R.id.rl_15);
        rl16 = findViewById(R.id.rl_16);
        rl17 = findViewById(R.id.rl_17);
        rl18 = findViewById(R.id.rl_18);
        rl19 = findViewById(R.id.rl_19);
        rl20 = findViewById(R.id.rl_20);
        rl21 = findViewById(R.id.rl_21);
        rl22 = findViewById(R.id.rl_22);
        rl23 = findViewById(R.id.rl_23);
        rl24 = findViewById(R.id.rl_24);
        hindBtn = findViewById(R.id.hint_btn);
        closeBtn = findViewById(R.id.close_btn);
        timerTxt = findViewById(R.id.timer_txt);




        CountUpTimer timer = new CountUpTimer(10000*100000) {
            public void onTick(int second) {
                timerTxt.setText(String.valueOf(second)+" Sec");
            }
        };

        timer.start();

        rlStar = findViewById(R.id.r_start);

        views = new View[10];
        initView(rl1,"1");
        initView(rl2,"2");
        initView(rl3,"3");
        initView(rl4,"4");
        initView(rl5,"5");
        initView(rl6,"6");
        initView(rl7,"7");
        initView(rl8,"8");
        initView(rl9,"9");
        initView(rl10,"10");
        initView(rl11,"11");
        initView(rl12,"12");
        initView(rl13,"13");
        initView(rl14,"14");
        initView(rl15,"15");
        initView(rl16,"16");
        initView(rl17,"17");
        initView(rl18,"18");
        initView(rl19,"19");
        initView(rl20,"20");
        initView(rl21,"21");
        initView(rl22,"22");
        initView(rl23,"23");
        initView(rl24,"24");


        rlStar.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                ClipData data = ClipData.newPlainText("value", "0");
                rlStar.startDrag(data, new View.DragShadowBuilder(v), null, 0);
                return true;
            }
        });

        getPuzzleImage(getIntent().getStringExtra("puzzle_id"));
        score = Integer.parseInt(getIntent().getStringExtra("score"));

        hindBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!hintUrl.equals("0")){
                    HintDialog hintDialog = new HintDialog(PuzzleImageActivity.this,hintUrl);
                    hintDialog.show();
                }
            }
        });
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //updateScore(sharePrefManager.getUserId(),getIntent().getStringExtra("puzzle_id"),String.valueOf(closeTime-openTime));


    }

    private void initView(final ImageView view, final String value) {
        view.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View v, DragEvent event) {
                switch(event.getAction()) {
                    case DragEvent.ACTION_DRAG_STARTED:
                        return true;

                    case DragEvent.ACTION_DRAG_ENTERED:
                        v.setBackgroundColor(Color.LTGRAY);
                        return true;

                    case DragEvent.ACTION_DRAG_LOCATION:
                        return true;

                    case DragEvent.ACTION_DRAG_EXITED:
                        v.setBackgroundColor(Color.TRANSPARENT);
                        // v.setBackgroundResource(R.drawable.star);


                        return true;

                    case DragEvent.ACTION_DROP:
                        v.setBackgroundColor(Color.TRANSPARENT);
                        int dragVal = Integer.parseInt(event.getClipData().getItemAt(0).getText().toString());
                        //int viewVal = Integer.parseInt(((TextView) v).getText().toString());
                        //((TextView) v).setText("" + (dragVal + viewVal));
                        Log.e("id_s",String.valueOf(v.getId()));
                        v.setPadding(0,0,0,0);


                        if(dragVal ==1  ){
                            rl1.setPadding(0,0,0,0);

                            int getDraggedPos = changeImage(v);
                            PuzzleImageModelItem droppedImageItem= puzzleImageModel.get(0);
                            PuzzleImageModelItem draggedImageItem = puzzleImageModel.get(getDraggedPos);
                            Log.e("test_drag",droppedImageItem.getImage_order()+","+String.valueOf(getDraggedPos+1));
                            loadImage((ImageView) v,puzzleImageModel.get(0).getImage());
                            puzzleImageModel.set(0, draggedImageItem);
                            puzzleImageModel.set(getDraggedPos, droppedImageItem);
                            loadImage(rl1, draggedImageItem.getImage());


                            if(droppedImageItem.getImage_order().equals(String.valueOf(getDraggedPos+1))){
                                v.setPadding(18,18,18,18);


                        }
                            if(draggedImageItem.getImage_order().equals(String.valueOf(1))){
                                //showDialogWrongPlace();

                                rl1.setPadding(8,8,8,8);
//                                rl1.setColorFilter(1);


                            }




                        }else if(dragVal ==2){
                            rl2.setPadding(0,0,0,0);

                            int getDraggedPos = changeImage(v);
                            PuzzleImageModelItem droppedImageItem= puzzleImageModel.get(1);
                            PuzzleImageModelItem draggedImageItem = puzzleImageModel.get(getDraggedPos);
                            loadImage((ImageView) v,puzzleImageModel.get(1).getImage());
                            puzzleImageModel.set(1, draggedImageItem);
                            puzzleImageModel.set(getDraggedPos, droppedImageItem);
                            loadImage(rl2, draggedImageItem.getImage());

                            if(droppedImageItem.getImage_order().equals(String.valueOf(getDraggedPos+1))){
                                v.setPadding(8,8,8,8);

                            }
                            if(draggedImageItem.getImage_order().equals(String.valueOf(2))){
                                //showDialogWrongPlace();

                                rl2.setPadding(8,8,8,8);


                            }

                        }else if(dragVal ==3){
                            rl3.setPadding(0,0,0,0);

                            int getDraggedPos = changeImage(v);
                            PuzzleImageModelItem droppedImageItem= puzzleImageModel.get(2);
                            PuzzleImageModelItem draggedImageItem = puzzleImageModel.get(getDraggedPos);
                            loadImage((ImageView) v,puzzleImageModel.get(2).getImage());
                            puzzleImageModel.set(2, draggedImageItem);
                            puzzleImageModel.set(getDraggedPos, droppedImageItem);
                            loadImage(rl3, draggedImageItem.getImage());


                            if(droppedImageItem.getImage_order().equals(String.valueOf(getDraggedPos+1))){
                                v.setPadding(8,8,8,8);

                            }
                            if(draggedImageItem.getImage_order().equals(String.valueOf(3))){
                                //showDialogWrongPlace();

                                rl3.setPadding(8, 8, 8, 8);

                            }
                        }else if(dragVal ==4){
                            rl4.setPadding(0,0,0,0);

                            int getDraggedPos = changeImage(v);
                            PuzzleImageModelItem droppedImageItem= puzzleImageModel.get(3);
                            PuzzleImageModelItem draggedImageItem = puzzleImageModel.get(getDraggedPos);
                            loadImage((ImageView) v,puzzleImageModel.get(3).getImage());
                            puzzleImageModel.set(3, draggedImageItem);
                            puzzleImageModel.set(getDraggedPos, droppedImageItem);
                            loadImage(rl4, draggedImageItem.getImage());


                            if(droppedImageItem.getImage_order().equals(String.valueOf(getDraggedPos+1))){
                                v.setPadding(8,8,8,8);

                            }
                            if(draggedImageItem.getImage_order().equals(String.valueOf(4))){
                                //showDialogWrongPlace();

                                    rl4.setPadding(8, 8, 8, 8);

                            }
                        }else if(dragVal == 5){
                            rl5.setPadding(0,0,0,0);
                            int getDraggedPos = changeImage(v);
                            PuzzleImageModelItem droppedImageItem= puzzleImageModel.get(4);
                            PuzzleImageModelItem draggedImageItem = puzzleImageModel.get(getDraggedPos);
                            loadImage((ImageView) v,puzzleImageModel.get(4).getImage());
                            puzzleImageModel.set(4, draggedImageItem);
                            puzzleImageModel.set(getDraggedPos, droppedImageItem);
                            loadImage(rl5, draggedImageItem.getImage());

                            if(droppedImageItem.getImage_order().equals(String.valueOf(getDraggedPos+1))){
                                v.setPadding(8,8,8,8);

                            }
                            if(draggedImageItem.getImage_order().equals(String.valueOf(5))){
                                //showDialogWrongPlace();

                                    rl5.setPadding(8, 8, 8, 8);

                            }
                        }else if(dragVal == 6){
                            rl6.setPadding(0,0,0,0);
                            int getDraggedPos = changeImage(v);
                            PuzzleImageModelItem droppedImageItem= puzzleImageModel.get(5);
                            PuzzleImageModelItem draggedImageItem = puzzleImageModel.get(getDraggedPos);
                            loadImage((ImageView) v,puzzleImageModel.get(5).getImage());
                            puzzleImageModel.set(5, draggedImageItem);
                            puzzleImageModel.set(getDraggedPos, droppedImageItem);
                            loadImage(rl6, draggedImageItem.getImage());

                            if(droppedImageItem.getImage_order().equals(String.valueOf(getDraggedPos+1))){
                                v.setPadding(8,8,8,8);

                            }
                            if(draggedImageItem.getImage_order().equals(String.valueOf(6))){
                                //showDialogWrongPlace();

                                rl6.setPadding(8, 8, 8, 8);

                            }
                        }else if(dragVal == 7){
                            rl7.setPadding(0,0,0,0);
                            int getDraggedPos = changeImage(v);
                            PuzzleImageModelItem droppedImageItem= puzzleImageModel.get(6);
                            PuzzleImageModelItem draggedImageItem = puzzleImageModel.get(getDraggedPos);
                            loadImage((ImageView) v,puzzleImageModel.get(6).getImage());
                            puzzleImageModel.set(6, draggedImageItem);
                            puzzleImageModel.set(getDraggedPos, droppedImageItem);
                            loadImage(rl7, draggedImageItem.getImage());

                            if(droppedImageItem.getImage_order().equals(String.valueOf(getDraggedPos+1))){
                                v.setPadding(8,8,8,8);

                            }
                            if(draggedImageItem.getImage_order().equals(String.valueOf(7))){
                                //showDialogWrongPlace();

                                rl7.setPadding(8, 8, 8, 8);

                            }


                        }else if(dragVal ==8){
                            rl8.setPadding(0,0,0,0);
                            int getDraggedPos = changeImage(v);
                            PuzzleImageModelItem droppedImageItem= puzzleImageModel.get(7);
                            PuzzleImageModelItem draggedImageItem = puzzleImageModel.get(getDraggedPos);
                            loadImage((ImageView) v,puzzleImageModel.get(7).getImage());
                            puzzleImageModel.set(7, draggedImageItem);
                            puzzleImageModel.set(getDraggedPos, droppedImageItem);
                            loadImage(rl8, draggedImageItem.getImage());
                            if(droppedImageItem.getImage_order().equals(String.valueOf(getDraggedPos+1))){
                                v.setPadding(8,8,8,8);

                            }
                            if(draggedImageItem.getImage_order().equals(String.valueOf(8))){
                                //showDialogWrongPlace();

                                rl8.setPadding(8, 8, 8, 8);

                            }
                        }else if(dragVal == 9){
                            rl9.setPadding(0,0,0,0);
                            int getDraggedPos = changeImage(v);
                            PuzzleImageModelItem droppedImageItem= puzzleImageModel.get(8);
                            PuzzleImageModelItem draggedImageItem = puzzleImageModel.get(getDraggedPos);
                            loadImage((ImageView) v,puzzleImageModel.get(8).getImage());
                            puzzleImageModel.set(8, draggedImageItem);
                            puzzleImageModel.set(getDraggedPos, droppedImageItem);
                            loadImage(rl9, draggedImageItem.getImage());
                            if(droppedImageItem.getImage_order().equals(String.valueOf(getDraggedPos+1))){
                                v.setPadding(8,8,8,8);

                            }
                            if(draggedImageItem.getImage_order().equals(String.valueOf(9))){
                                //showDialogWrongPlace();

                                rl9.setPadding(8, 8, 8, 8);

                            }
                        }else if(dragVal ==10  ){
                            rl10.setPadding(0,0,0,0);
                            int getDraggedPos = changeImage(v);
                            PuzzleImageModelItem droppedImageItem= puzzleImageModel.get(9);
                            PuzzleImageModelItem draggedImageItem = puzzleImageModel.get(getDraggedPos);
                            loadImage((ImageView) v,puzzleImageModel.get(9).getImage());
                            puzzleImageModel.set(9, draggedImageItem);
                            puzzleImageModel.set(getDraggedPos, droppedImageItem);
                            loadImage(rl10, draggedImageItem.getImage());

                            if(droppedImageItem.getImage_order().equals(String.valueOf(getDraggedPos+1))){
                                v.setPadding(8,8,8,8);

                            }
                            if(draggedImageItem.getImage_order().equals(String.valueOf(10))){
                                //showDialogWrongPlace();

                                rl10.setPadding(8, 8, 8, 8);

                            }
                        }else if(dragVal ==11  ){
                            rl11.setPadding(0,0,0,0);
                            int getDraggedPos = changeImage(v);
                            PuzzleImageModelItem droppedImageItem= puzzleImageModel.get(10);
                            PuzzleImageModelItem draggedImageItem = puzzleImageModel.get(getDraggedPos);
                            loadImage((ImageView) v,puzzleImageModel.get(10).getImage());
                            puzzleImageModel.set(10, draggedImageItem);
                            puzzleImageModel.set(getDraggedPos, droppedImageItem);
                            loadImage(rl11, draggedImageItem.getImage());

                            if(droppedImageItem.getImage_order().equals(String.valueOf(getDraggedPos+1))){
                                v.setPadding(8,8,8,8);

                            }
                            if(draggedImageItem.getImage_order().equals(String.valueOf(11))){
                                //showDialogWrongPlace();

                                rl11.setPadding(8, 8, 8, 8);

                            }
                        }else if(dragVal ==12  ){
                            rl12.setPadding(0,0,0,0);
                            int getDraggedPos = changeImage(v);
                            PuzzleImageModelItem droppedImageItem= puzzleImageModel.get(11);
                            PuzzleImageModelItem draggedImageItem = puzzleImageModel.get(getDraggedPos);
                            loadImage((ImageView) v,puzzleImageModel.get(11).getImage());
                            puzzleImageModel.set(11, draggedImageItem);
                            puzzleImageModel.set(getDraggedPos, droppedImageItem);
                            loadImage(rl12, draggedImageItem.getImage());


                            if(droppedImageItem.getImage_order().equals(String.valueOf(getDraggedPos+1))){
                                v.setPadding(8,8,8,8);

                            }  if(draggedImageItem.getImage_order().equals(String.valueOf(12))){
                                //showDialogWrongPlace();
                                if(getDraggedPos+1!=12) {
                                    rl12.setPadding(8, 8, 8, 8);
                                }
                            }
                        }else if(dragVal ==13  ){
                            rl13.setPadding(0,0,0,0);
                            int getDraggedPos = changeImage(v);
                            PuzzleImageModelItem droppedImageItem= puzzleImageModel.get(12);
                            PuzzleImageModelItem draggedImageItem = puzzleImageModel.get(getDraggedPos);
                            loadImage((ImageView) v,puzzleImageModel.get(12).getImage());
                            puzzleImageModel.set(12, draggedImageItem);
                            puzzleImageModel.set(getDraggedPos, droppedImageItem);
                            loadImage(rl13, draggedImageItem.getImage());

                            if(droppedImageItem.getImage_order().equals(String.valueOf(getDraggedPos+1))){
                                v.setPadding(8,8,8,8);

                            }
                            if(draggedImageItem.getImage_order().equals(String.valueOf(13))){
                                //showDialogWrongPlace();

                                rl13.setPadding(8, 8, 8, 8);

                            }
                        }else if(dragVal ==14  ){
                            rl14.setPadding(0,0,0,0);
                            int getDraggedPos = changeImage(v);
                            PuzzleImageModelItem droppedImageItem= puzzleImageModel.get(13);
                            PuzzleImageModelItem draggedImageItem = puzzleImageModel.get(getDraggedPos);
                            loadImage((ImageView) v,puzzleImageModel.get(13).getImage());
                            puzzleImageModel.set(13, draggedImageItem);
                            puzzleImageModel.set(getDraggedPos, droppedImageItem);
                            loadImage(rl14, draggedImageItem.getImage());

                            if(droppedImageItem.getImage_order().equals(String.valueOf(getDraggedPos+1))){
                                v.setPadding(8,8,8,8);

                            }
                            if(draggedImageItem.getImage_order().equals(String.valueOf(14))){
                                //showDialogWrongPlace();

                                rl14.setPadding(8, 8, 8, 8);

                            }
                        }else if(dragVal ==  15){
                            rl15.setPadding(0,0,0,0);
                            int getDraggedPos = changeImage(v);
                            PuzzleImageModelItem droppedImageItem= puzzleImageModel.get(14);
                            PuzzleImageModelItem draggedImageItem = puzzleImageModel.get(getDraggedPos);
                            loadImage((ImageView) v,puzzleImageModel.get(14).getImage());
                            puzzleImageModel.set(14, draggedImageItem);
                            puzzleImageModel.set(getDraggedPos, droppedImageItem);
                            loadImage(rl15, draggedImageItem.getImage());

                            if(droppedImageItem.getImage_order().equals(String.valueOf(getDraggedPos+1))){
                                v.setPadding(8,8,8,8);

                            }
                            if(draggedImageItem.getImage_order().equals(String.valueOf(15))){
                                //showDialogWrongPlace();if(getDraggedPos+1!=15) {
                                    rl15.setPadding(8, 8, 8, 8);

                            }
                        }else if(dragVal ==16){
                            rl16.setPadding(0,0,0,0);
                            int getDraggedPos = changeImage(v);
                            PuzzleImageModelItem droppedImageItem= puzzleImageModel.get(15);
                            PuzzleImageModelItem draggedImageItem = puzzleImageModel.get(getDraggedPos);
                            loadImage((ImageView) v,puzzleImageModel.get(15).getImage());
                            puzzleImageModel.set(15, draggedImageItem);
                            puzzleImageModel.set(getDraggedPos, droppedImageItem);
                            loadImage(rl16, draggedImageItem.getImage());

                            if(droppedImageItem.getImage_order().equals(String.valueOf(getDraggedPos+1))){
                                v.setPadding(8,8,8,8);

                            }
                            if(draggedImageItem.getImage_order().equals(String.valueOf(16))){
                                //showDialogWrongPlace();

                                    rl16.setPadding(8, 8, 8, 8);

                            }
                        }else if(dragVal == 17){
                            rl17.setPadding(0,0,0,0);
                            int getDraggedPos = changeImage(v);
                            PuzzleImageModelItem droppedImageItem= puzzleImageModel.get(16);
                            PuzzleImageModelItem draggedImageItem = puzzleImageModel.get(getDraggedPos);

                            loadImage((ImageView) v,puzzleImageModel.get(16).getImage());
                            puzzleImageModel.set(16, draggedImageItem);
                            puzzleImageModel.set(getDraggedPos, droppedImageItem);
                            loadImage(rl17, draggedImageItem.getImage());

                            if(droppedImageItem.getImage_order().equals(String.valueOf(getDraggedPos+1))){
                                v.setPadding(8,8,8,8);

                            }
                            if(draggedImageItem.getImage_order().equals(String.valueOf(17))){
                                //showDialogWrongPlace();

                                    rl17.setPadding(8, 8, 8, 8);

                            }
                        }else if(dragVal == 18){
                            rl18.setPadding(0,0,0,0);
                            int getDraggedPos = changeImage(v);
                            PuzzleImageModelItem droppedImageItem= puzzleImageModel.get(17);
                            PuzzleImageModelItem draggedImageItem = puzzleImageModel.get(getDraggedPos);
                            loadImage((ImageView) v,puzzleImageModel.get(17).getImage());
                            puzzleImageModel.set(17, draggedImageItem);
                            puzzleImageModel.set(getDraggedPos, droppedImageItem);
                            loadImage(rl18, draggedImageItem.getImage());

                            if(droppedImageItem.getImage_order().equals(String.valueOf(getDraggedPos+1))){
                                v.setPadding(8,8,8,8);

                            }  if(draggedImageItem.getImage_order().equals(String.valueOf(18))){
                                //showDialogWrongPlace();

                                    rl18.setPadding(8, 8, 8, 8);

                            }
                        }
                        else if(dragVal ==19){
                            rl19.setPadding(0,0,0,0);
                            int getDraggedPos = changeImage(v);
                            PuzzleImageModelItem droppedImageItem= puzzleImageModel.get(18);
                            PuzzleImageModelItem draggedImageItem = puzzleImageModel.get(getDraggedPos);
                            loadImage((ImageView) v,puzzleImageModel.get(18).getImage());
                            puzzleImageModel.set(18, draggedImageItem);
                            puzzleImageModel.set(getDraggedPos, droppedImageItem);
                            loadImage(rl19, draggedImageItem.getImage());

                            if(droppedImageItem.getImage_order().equals(String.valueOf(getDraggedPos+1))){
                                v.setPadding(8,8,8,8);

                            } if(draggedImageItem.getImage_order().equals(String.valueOf(19))){
                                //showDialogWrongPlace();

                                    rl19.setPadding(8, 8, 8, 8);

                            }
                        }else if(dragVal ==20){
                            rl20.setPadding(0,0,0,0);
                            int getDraggedPos = changeImage(v);
                            PuzzleImageModelItem droppedImageItem= puzzleImageModel.get(19);
                            PuzzleImageModelItem draggedImageItem = puzzleImageModel.get(getDraggedPos);
                            loadImage((ImageView) v,puzzleImageModel.get(19).getImage());
                            puzzleImageModel.set(19, draggedImageItem);
                            puzzleImageModel.set(getDraggedPos, droppedImageItem);
                            loadImage(rl20, draggedImageItem.getImage());

                            if(droppedImageItem.getImage_order().equals(String.valueOf(getDraggedPos+1))){
                                v.setPadding(8,8,8,8);

                            } if(draggedImageItem.getImage_order().equals(String.valueOf(20))){
                                //showDialogWrongPlace();

                                rl20.setPadding(8, 8, 8, 8);

                            }
                        }else if(dragVal ==21){
                            rl21.setPadding(0,0,0,0);
                            int getDraggedPos = changeImage(v);
                            PuzzleImageModelItem droppedImageItem= puzzleImageModel.get(20);
                            PuzzleImageModelItem draggedImageItem = puzzleImageModel.get(getDraggedPos);
                            loadImage((ImageView) v,puzzleImageModel.get(20).getImage());
                            puzzleImageModel.set(20, draggedImageItem);
                            puzzleImageModel.set(getDraggedPos, droppedImageItem);
                            loadImage(rl21, draggedImageItem.getImage());

                            if(droppedImageItem.getImage_order().equals(String.valueOf(getDraggedPos+1))){
                                v.setPadding(8,8,8,8);

                            }  if(draggedImageItem.getImage_order().equals(String.valueOf(21))){
                                //showDialogWrongPlace();

                                rl21.setPadding(8, 8, 8, 8);

                            }
                        }else if(dragVal ==22){
                            rl22.setPadding(0,0,0,0);
                            int getDraggedPos = changeImage(v);
                            PuzzleImageModelItem droppedImageItem= puzzleImageModel.get(21);
                            PuzzleImageModelItem draggedImageItem = puzzleImageModel.get(getDraggedPos);
                            loadImage((ImageView) v,puzzleImageModel.get(21).getImage());
                            puzzleImageModel.set(21, draggedImageItem);
                            puzzleImageModel.set(getDraggedPos, droppedImageItem);
                            loadImage(rl22, draggedImageItem.getImage());

                            if(droppedImageItem.getImage_order().equals(String.valueOf(getDraggedPos+1))){
                                v.setPadding(8,8,8,8);

                            }
                            if(draggedImageItem.getImage_order().equals(String.valueOf(22))){
                                //showDialogWrongPlace();

                                    rl22.setPadding(8, 8, 8, 8);

                            }
                        }else if(dragVal ==23){
                            rl23.setPadding(0,0,0,0);
                            int getDraggedPos = changeImage(v);
                            PuzzleImageModelItem droppedImageItem= puzzleImageModel.get(22);
                            PuzzleImageModelItem draggedImageItem = puzzleImageModel.get(getDraggedPos);
                            loadImage((ImageView) v,puzzleImageModel.get(22).getImage());
                            puzzleImageModel.set(22, draggedImageItem);
                            puzzleImageModel.set(getDraggedPos, droppedImageItem);
                            loadImage(rl23, draggedImageItem.getImage());
                            if(droppedImageItem.getImage_order().equals(String.valueOf(getDraggedPos+1))){
                                v.setPadding(8,8,8,8);

                            }
                            if(draggedImageItem.getImage_order().equals(String.valueOf(23))){
                                //showDialogWrongPlace();

                                    rl23.setPadding(8, 8, 8, 8);

                            }
                        }else if(dragVal ==24){
                            rl24.setPadding(0,0,0,0);
                            int getDraggedPos = changeImage(v);
                            PuzzleImageModelItem droppedImageItem= puzzleImageModel.get(23);
                            PuzzleImageModelItem draggedImageItem = puzzleImageModel.get(getDraggedPos);
                            loadImage((ImageView) v,puzzleImageModel.get(23).getImage());
                            puzzleImageModel.set(23, draggedImageItem);
                            puzzleImageModel.set(getDraggedPos, droppedImageItem);
                            loadImage(rl24, draggedImageItem.getImage());

                            if(droppedImageItem.getImage_order().equals(String.valueOf(getDraggedPos+1))){
                                v.setPadding(8,8,8,8);

                            }
                            if(draggedImageItem.getImage_order().equals(String.valueOf(24))){
                                //showDialogWrongPlace();
                                rl24.setPadding(8, 8, 8, 8);
                            }
                        }



                        ckPuzzle();




                        return true;

                    case DragEvent.ACTION_DRAG_ENDED:
                        return true;

                    default:
                        break;
                }
                return false;
            }
        });

        view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                ClipData data = ClipData.newPlainText("value", value);
                rlStar.startDrag(data, new View.DragShadowBuilder(v), null, 0);
                return true;
            }
        });


    }


    void  getPuzzleImage(String puzzle_id){
        Api api = NetworkClient.getRetrofitClient().create(Api.class);
        SharePrefManager sharePrefManager = new SharePrefManager(this);
        Call<PuzzleImageModel> call = api.getPuzzleImage(puzzle_id,sharePrefManager.getLanCode());
        call.enqueue(new Callback<PuzzleImageModel>() {
            @Override
            public void onResponse(Call<PuzzleImageModel> call, Response<PuzzleImageModel> response) {

                puzzleImageModel = response.body();



                Log.e("size",String.valueOf(puzzleImageModel.size()));
                if(puzzleImageModel.size()==25){

                    hintUrl = puzzleImageModel.get(24).getImage();
                    HintDialog hintDialog = new HintDialog(PuzzleImageActivity.this,hintUrl);
                    hintDialog.show();
                    puzzleImageModel.remove(24);
                    Collections.shuffle(puzzleImageModel);
                    for(int i=0;i<puzzleImageModel.size();i++){
                        PuzzleImageModelItem puzzleImageModelItem = puzzleImageModel.get(i);

                        if(i==0){
                            loadImage(rl1,puzzleImageModelItem.getImage());
                        }else if(i==1){
                            loadImage(rl2,puzzleImageModelItem.getImage());
                        }else if(i==2){
                            loadImage(rl3,puzzleImageModelItem.getImage());
                        }else if(i==3){
                            loadImage(rl4,puzzleImageModelItem.getImage());
                        }else if(i==4){
                            loadImage(rl5,puzzleImageModelItem.getImage());
                        }else if(i==5){
                            loadImage(rl6,puzzleImageModelItem.getImage());
                        }else if(i==6){
                            loadImage(rl7,puzzleImageModelItem.getImage());
                        }else if(i==7){
                            loadImage(rl8,puzzleImageModelItem.getImage());
                        }else if(i==8){
                            loadImage(rl9,puzzleImageModelItem.getImage());
                        }else if(i==9){
                            loadImage(rl10,puzzleImageModelItem.getImage());
                        }else if(i==10){
                            loadImage(rl11,puzzleImageModelItem.getImage());
                        }else if(i==11){
                            loadImage(rl12,puzzleImageModelItem.getImage());
                        }else if(i==12){
                            loadImage(rl13,puzzleImageModelItem.getImage());
                        }else if(i==13){
                            loadImage(rl14,puzzleImageModelItem.getImage());
                        }else if(i==14){
                            loadImage(rl15,puzzleImageModelItem.getImage());
                        }else if(i==15){
                            loadImage(rl16,puzzleImageModelItem.getImage());
                        }else if(i==16){
                            loadImage(rl17,puzzleImageModelItem.getImage());
                        }else if(i==17){
                            loadImage(rl18,puzzleImageModelItem.getImage());
                        }else if(i==18){
                            loadImage(rl19,puzzleImageModelItem.getImage());
                        }else if(i==19){
                            loadImage(rl20,puzzleImageModelItem.getImage());
                        }else if(i==20){
                            loadImage(rl21,puzzleImageModelItem.getImage());
                        }else if(i==21){
                            loadImage(rl22,puzzleImageModelItem.getImage());
                        }else if(i==22){
                            loadImage(rl23,puzzleImageModelItem.getImage());
                        }else if(i==23){
                            loadImage(rl24,puzzleImageModelItem.getImage());
                        }


                    }
                }





            }

            @Override
            public void onFailure(Call<PuzzleImageModel> call, Throwable t) {

            }
        });
    }

    void loadImage(final ImageView layout, String url){
        Picasso.get()
                .load(url)
                .fit()
                .placeholder(R.drawable.progress_animation)
                .into(layout);

    }


    int changeImage(View v){

        int dragged_pos = 0;
        if(v.getId() ==R.id.rl_1 ){
            dragged_pos = 0;

        }else if(v.getId()==R.id.rl_2){
            dragged_pos = 1;

        }else if(v.getId()==R.id.rl_3){
            dragged_pos = 2;

        }else if(v.getId()==R.id.rl_4){
            dragged_pos = 3;
        }else if(v.getId()==R.id.rl_5){
            dragged_pos = 4;
        }else if(v.getId()==R.id.rl_6){
            dragged_pos = 5;
        }else if(v.getId()==R.id.rl_7){
            dragged_pos = 6;
        }else if(v.getId()==R.id.rl_8){
            dragged_pos = 7;
        }else if(v.getId()==R.id.rl_9){
            dragged_pos = 8;
        }else if(v.getId()==R.id.rl_10){
            dragged_pos = 9;
        }else if(v.getId()==R.id.rl_11){
            dragged_pos = 10;
        }else if(v.getId()==R.id.rl_12){
            dragged_pos = 11;
        }else if(v.getId()==R.id.rl_13){
            dragged_pos = 12;
        }else if(v.getId()==R.id.rl_14){
            dragged_pos = 13;
        }else if(v.getId()==R.id.rl_15){
            dragged_pos = 14;
        }else if(v.getId()==R.id.rl_16){
            dragged_pos = 15;
        }else if(v.getId()==R.id.rl_17){
            dragged_pos = 16;
        }else if(v.getId()==R.id.rl_18){
            dragged_pos = 17;
        }
        else if(v.getId()==R.id.rl_19){
            dragged_pos = 18;
        }else if(v.getId()==R.id.rl_20){
            dragged_pos = 19;
        }else if(v.getId()==R.id.rl_21){
            dragged_pos = 20;
        }else if(v.getId()==R.id.rl_22){
            dragged_pos = 21;
        }else if(v.getId()==R.id.rl_23){
            dragged_pos = 22;
        }else if(v.getId()==R.id.rl_24){
            dragged_pos = 23;
        }

        return  dragged_pos;

    }


    void  ckPuzzle(){

        boolean is_succes = false;
        for(int i=0;i<puzzleImageModel.size();i++){

            Log.e("serial",String.valueOf(puzzleImageModel.get(i).getImage_order()));
            if(puzzleImageModel.get(i).getImage_order().equals(String.valueOf(i+1))){
                is_succes = true;
            }else {
                is_succes = false;
                break;
            }

        }
        if(is_succes){
            showDialog();
        }
    }

    void  showDialog(){
        String message = "";
        String title = "";

        closeTime = System.currentTimeMillis()/1000;
        int oldScore = score;
        int newScore = (int) (closeTime-openTime);

        if(oldScore<newScore){
            title = "Congratulation";
            message = "You successfully solve this puzzle in "+String.valueOf(closeTime-openTime)+" seconds";
        }else {
            title = "New record";
            message = "New score is "+String.valueOf(newScore)+".You successfully solve this puzzle in "+String.valueOf(closeTime-openTime)+" seconds";
            updateScore(sharePrefManager.getUserId(),getIntent().getStringExtra("puzzle_id"),String.valueOf(closeTime-openTime));
        }

        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                // Specifying a listener allows you to take an action before dismissing the dialog.
                // The dialog is automatically dismissed when a dialog button is clicked.
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Continue with delete operation

                        finish();
                    }
                })

                .setIcon(android.R.drawable.ic_dialog_info)
                .show();

    }



    void  showDialogWrongPlace(){
        new AlertDialog.Builder(this)
                .setTitle("Wrong Place")
                .setMessage("This is not the right place for  the puzzle.")

                // Specifying a listener allows you to take an action before dismissing the dialog.
                // The dialog is automatically dismissed when a dialog button is clicked.
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Continue with delete operation
                        // finish();
                    }
                })

                .setIcon(android.R.drawable.ic_dialog_info)
                .show();

    }
    void updateScore(String userId,String puzzleId,String score){
        Api api = NetworkClient.getRetrofitClient().create(Api.class);
        Call<String> call = api.updateScore(userId,score,puzzleId);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }

}
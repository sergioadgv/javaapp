package com.example.draganddrop;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.draganddrop.model.page_model.PageModel;
import com.example.draganddrop.model.page_model.PageModelItem;
import com.example.draganddrop.model.page_model.SImage;
import com.example.draganddrop.network.Api;
import com.example.draganddrop.network.NetworkClient;
import com.example.draganddrop.services.AddUserData;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PageActivity extends AppCompatActivity {


    PageModel pageModelItems = new PageModel();
    ImageView image_1,s_1,s_2,s_3,image_2,image_3,image_4,reset,drag_pos_1,drag_pos_2,drag_pos_3,drag_pos_4;

    Bitmap image1_bit,image2_bit,s1_bit,s2_bit,s3_bit,image3_bit,image4_bit;

    int pageNo =0;
    boolean is_entered = false;
    Button nextPage;
    int dragged_sticker =0;
    TextView pageText;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page);
        image_1 = findViewById(R.id.image1);
        image_2 = findViewById(R.id.image_2);
        s_1 = findViewById(R.id.rl_1);
        s_2 = findViewById(R.id.rl_2);
        s_3 = findViewById(R.id.rl_3);
        reset = findViewById(R.id.reset);
        nextPage = findViewById(R.id.next_page);
        image_3 = findViewById(R.id.image_3);
        image_4 = findViewById(R.id.image_4);
        pageText = findViewById(R.id.page_txt);
        drag_pos_1 = findViewById(R.id.drag_pos_1);
        drag_pos_2 = findViewById(R.id.drag_pos_2);
        drag_pos_3 = findViewById(R.id.drag_pos_3);
        drag_pos_4 = findViewById(R.id.drag_pos_4);

        s_2.setVisibility(View.GONE);
        s_3.setVisibility(View.GONE);


        s_1.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                dragged_sticker = 1;
                ClipData data = ClipData.newPlainText("value", "1");
                s_1.startDrag(data, new View.DragShadowBuilder(v), null, 0);
                return  true;
            }
        });

        s_2.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                dragged_sticker = 2;
                ClipData data = ClipData.newPlainText("value", "2");
                s_2.startDrag(data, new View.DragShadowBuilder(v), null, 0);
                return  true;
            }
        });
        s_3.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                dragged_sticker = 3;
                ClipData data = ClipData.newPlainText("value", "3");
                s_3.startDrag(data, new View.DragShadowBuilder(v), null, 0);
                return  true;
            }
        });




        initView(drag_pos_1,"1");
        initView(drag_pos_2,"2");
        initView(drag_pos_3,"3");
        initView(drag_pos_4,"4");

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pageModelItems.size()!=0){
                    PageModelItem pageModelItem = pageModelItems.get(pageNo);
                    Picasso.get().load(pageModelItem.getPage_image()).fit().placeholder(R.drawable.progress_animation).fit().into(image_1);
                    Picasso.get().load(pageModelItem.getS_images().get(0).getS_image2()).placeholder(R.drawable.progress_animation).fit().into(s_1);
                    image_2.setImageDrawable(null);
                    image_3.setImageDrawable(null);
                    image_4.setImageDrawable(null);
                    if(pageModelItems.get(pageNo).getS_images().size()==1){
                        Picasso.get().load(pageModelItem.getS_images().get(0).getS_image2()).placeholder(R.drawable.progress_animation).fit().into(s_1);
                    }else if(pageModelItems.get(pageNo).getS_images().size()==2){
                        Picasso.get().load(pageModelItem.getS_images().get(0).getS_image2()).placeholder(R.drawable.progress_animation).fit().into(s_1);
                        Picasso.get().load(pageModelItem.getS_images().get(1).getS_image2()).placeholder(R.drawable.progress_animation).fit().into(s_2);
                    }else if(pageModelItems.get(pageNo).getS_images().size()==3){
                        Picasso.get().load(pageModelItem.getS_images().get(0).getS_image2()).placeholder(R.drawable.progress_animation).fit().into(s_1);
                        Picasso.get().load(pageModelItem.getS_images().get(1).getS_image2()).placeholder(R.drawable.progress_animation).fit().into(s_2);
                        Picasso.get().load(pageModelItem.getS_images().get(2).getS_image2()).placeholder(R.drawable.progress_animation).fit().into(s_3);
                    }



                }
            }
        });
        nextPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                pageNo++;
                if(pageNo<pageModelItems.size()){
                    loadPage(pageNo);
                }else {
                    showDialog();
                }

            }
        });

        getPages(getIntent().getStringExtra("bid"));
        AddUserData addUserData =new  AddUserData(this,"Booklet",getIntent().getStringExtra("b_name"));
        addUserData.adduserData();
    }


    void getPages( String bookletId){
        Api api = NetworkClient.getRetrofitClient().create(Api.class);
        Call<PageModel> call = api.getPages(bookletId);
        call.enqueue(new Callback<PageModel>() {
            @Override
            public void onResponse(Call<PageModel> call, Response<PageModel> response) {
                pageModelItems.addAll(response.body());
                if(pageModelItems.size()!=0){
                   loadPage(pageNo);

                }

            }

            @Override
            public void onFailure(Call<PageModel> call, Throwable t) {

            }
        });


    }

    private void initView(final ImageView view, final String value) {
        view.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View v, DragEvent event) {
                switch(event.getAction()) {
                    case DragEvent.ACTION_DRAG_STARTED:

                        Log.e("drag","started");
                        if(dragged_sticker==1){
                            s_1.setImageDrawable(null);

                        }else if(dragged_sticker ==2){
                            s_2.setImageDrawable(null);

                        }else if(dragged_sticker == 3){
                            s_3.setImageDrawable(null);

                        }

                        return true;

                    case DragEvent.ACTION_DRAG_ENTERED:
                        // v.setBackgroundColor(Color.LTGRAY);
                        Log.e("drag","Entered");
                        is_entered = true;
                        return true;

                    case DragEvent.ACTION_DRAG_LOCATION:
                        Log.e("drag","location");
                        return true;

                    case DragEvent.ACTION_DRAG_EXITED:
                         v.setBackgroundColor(Color.TRANSPARENT);
                        Log.e("drag","Exited");
                        is_entered = false;
                        // v.setBackgroundResource(R.drawable.star);

                        return true;

                    case DragEvent.ACTION_DROP:

                        v.setBackgroundColor(Color.TRANSPARENT);
                        int dragVal = Integer.parseInt(event.getClipData().getItemAt(0).getText().toString());
                        PageModelItem pageModelItem = pageModelItems.get(pageNo);
                        if(dragVal==2){
                            String imageOrder1 =  pageModelItem.getS_images().get(1).getImage_order();
                            String imageOrder2 = pageModelItem.getS_images().get(1).getImage_order2();

                            String ckImgValue = String.valueOf(checkImage((ImageView) v));

                            if(imageOrder1.equals(ckImgValue) || imageOrder2.equals(ckImgValue)){
                                Log.e("drag",String.valueOf(checkImage((ImageView) v)));
                                Picasso.get().load(pageModelItem.getS_images().get(1).getS_image1()).placeholder(R.drawable.progress_animation).fit().into(image_3);
                                s_2.setImageDrawable(null);
                            }else {
                                showDialogWrongPlace();
                                Picasso.get().load(pageModelItem.getS_images().get(1).getS_image2()).placeholder(R.drawable.progress_animation).fit().into(s_2);

                            }

                        }else if(dragVal ==1){
                            String imageOrder1 =  pageModelItem.getS_images().get(0).getImage_order();
                            String imageOrder2 = pageModelItem.getS_images().get(0).getImage_order2();

                            String ckImgValue = String.valueOf(checkImage((ImageView) v));

                            if(imageOrder1.equals(ckImgValue) || imageOrder2.equals(ckImgValue)){
                                Picasso.get().load(pageModelItem.getS_images().get(0).getS_image1()).placeholder(R.drawable.progress_animation).fit().into(image_2);
                                s_1.setImageDrawable(null);
                            }else {
                                showDialogWrongPlace();
                                Picasso.get().load(pageModelItem.getS_images().get(0).getS_image2()).placeholder(R.drawable.progress_animation).fit().into(s_1);
                            }


                        }else if(dragVal == 3){
                            String imageOrder1 =  pageModelItem.getS_images().get(2).getImage_order();
                            String imageOrder2 = pageModelItem.getS_images().get(2).getImage_order2();

                            String ckImgValue = String.valueOf(checkImage((ImageView) v));
                            if(imageOrder1.equals(ckImgValue) || imageOrder2.equals(ckImgValue)){
                                Log.e("drag",String.valueOf(checkImage((ImageView) v)));
                                Picasso.get().load(pageModelItem.getS_images().get(2).getS_image1()).placeholder(R.drawable.progress_animation).fit().into(image_4);
                                s_3.setImageDrawable(null);
                            }else {
                                showDialogWrongPlace();
                                Picasso.get().load(pageModelItem.getS_images().get(2).getS_image2()).placeholder(R.drawable.progress_animation).fit().into(s_3);
                            }

                        }


                        return true;

                    case DragEvent.ACTION_DRAG_ENDED:
                        Log.e("drag","Ended");
                        if(is_entered == false){
                            //s_1.setImageBitmap(s1_bit);
                            pageModelItem = pageModelItems.get(pageNo);
                            if(dragged_sticker==1){
                                Picasso.get().load(pageModelItem.getS_images().get(0).getS_image2()).placeholder(R.drawable.progress_animation).fit().into(s_1);
                            }else if(dragged_sticker ==2){
                                Picasso.get().load(pageModelItem.getS_images().get(1).getS_image2()).placeholder(R.drawable.progress_animation).fit().into(s_2);

                            }else if(dragged_sticker ==3){
                                Picasso.get().load(pageModelItem.getS_images().get(2).getS_image2()).placeholder(R.drawable.progress_animation).fit().into(s_3);

                            }
                        }
                        return true;

                    default:
                        break;
                }
                return false;
            }
        });



    }


    void  loadnextpage(int pageNo){
        pageText.setText("Page no :2");
        s_1.setImageDrawable(null);
        s_2.setImageDrawable(null);
        s_3.setImageDrawable(null);
        image_1.setImageDrawable(null);
        image_2.setImageDrawable(null);
        image_3.setImageDrawable(null);
        image_4.setImageDrawable(null);
        s_2.setVisibility(View.VISIBLE);
        s_3.setVisibility(View.VISIBLE);

        this.pageNo = pageNo;
        PageModelItem pageModelItem = pageModelItems.get(pageNo);
        Picasso.get().load(pageModelItem.getPage_image()).into(image_1);
        Picasso.get().load(pageModelItem.getS_images().get(0).getS_image2()).placeholder(R.drawable.progress_animation).fit().into(s_1);
        Picasso.get().load(pageModelItem.getS_images().get(1).getS_image2()).placeholder(R.drawable.progress_animation).fit().into(s_2);
        Picasso.get().load(pageModelItem.getS_images().get(2).getS_image2()).placeholder(R.drawable.progress_animation).fit().into(s_3);


    }

    void loadPage(int pageNo){
        PageModelItem pageModelItem = pageModelItems.get(pageNo);
        pageText.setText(pageModelItem.getPage_name());
        s_1.setImageDrawable(null);
        s_2.setImageDrawable(null);
        s_3.setImageDrawable(null);
        image_1.setImageDrawable(null);
        image_2.setImageDrawable(null);
        image_3.setImageDrawable(null);
        image_4.setImageDrawable(null);
        s_1.setVisibility(View.GONE);
        s_2.setVisibility(View.GONE);
        s_3.setVisibility(View.GONE);
        List<SImage> sImage = pageModelItem.getS_images();
        if(sImage.size()==1){
            s_1.setVisibility(View.VISIBLE);
            Picasso.get().load(pageModelItem.getPage_image()).into(image_1);
            Picasso.get().load(sImage.get(0).getS_image2()).placeholder(R.drawable.progress_animation).fit().into(s_1);
        }else if(pageModelItem.getS_images().size()==2){
            s_1.setVisibility(View.VISIBLE);
            s_2.setVisibility(View.VISIBLE);
            Picasso.get().load(pageModelItem.getPage_image()).into(image_1);
            Picasso.get().load(sImage.get(0).getS_image2()).placeholder(R.drawable.progress_animation).fit().into(s_1);
            Picasso.get().load(sImage.get(1).getS_image2()).placeholder(R.drawable.progress_animation).fit().into(s_2);
        }else if(sImage.size()==3){
            Log.e("imageurl",sImage.get(2).getS_image2());
           s_1.setVisibility(View.VISIBLE);
            s_2.setVisibility(View.VISIBLE);
            s_3.setVisibility(View.VISIBLE);
            Picasso.get().load(pageModelItem.getPage_image()).into(image_1);
            Picasso.get().load(sImage.get(0).getS_image2()).placeholder(R.drawable.progress_animation).fit().into(s_1);
            Picasso.get().load(sImage.get(1).getS_image2()).placeholder(R.drawable.progress_animation).fit().into(s_2);
            Picasso.get().load(sImage.get(2).getS_image2()).placeholder(R.drawable.progress_animation).fit().into(s_3);
        }



    }


    int checkImage(ImageView v){
        int dragged_pos = 0;
        if(v.getId()==R.id.drag_pos_1){
            dragged_pos = 1;
        }else if(v.getId()==R.id.drag_pos_2){
            dragged_pos = 2;

        }else if(v.getId()==R.id.drag_pos_3){
            dragged_pos = 3;

        }else if(v.getId()==R.id.drag_pos_4){
            dragged_pos = 4;
        }

        return dragged_pos;
    }

    void  showDialog(){
        new AlertDialog.Builder(this)
                .setTitle("Congratulation")
                .setMessage("Book is end.")

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

    public void onclickBack(View view){
        finish();
    }



}
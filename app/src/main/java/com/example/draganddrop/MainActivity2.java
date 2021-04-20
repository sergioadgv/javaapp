package com.example.draganddrop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.View;
import android.widget.RelativeLayout;

public class MainActivity2 extends AppCompatActivity {

  RelativeLayout rl1,rl2,ml1,ml2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draw_and_drop_module2);
        rl1 = findViewById(R.id.rl_1);
        rl2 = findViewById(R.id.rl_2);

        ml1 = findViewById(R.id.m_1);
        ml2 = findViewById(R.id.m_2);

        rl1.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                ClipData data = ClipData.newPlainText("value", "1");
                rl1.startDrag(data, new View.DragShadowBuilder(v), null, 0);
                return true;
            }
        });

        rl2.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                ClipData data = ClipData.newPlainText("value", "2");
                rl2.startDrag(data, new View.DragShadowBuilder(v), null, 0);
                return true;
            }
        });

        initView(ml1,"1");
        initView(ml2,"2");
    }

    private void initView(final RelativeLayout view, final String value) {
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
                        if(dragVal==2){
                            v.setBackgroundResource(R.drawable.m_2);
                        }else if(dragVal ==1){
                            v.setBackgroundResource(R.drawable.m_1);
                        }


                        return true;

                    case DragEvent.ACTION_DRAG_ENDED:
                        return true;

                    default:
                        break;
                }
                return false;
            }
        });



    }
}
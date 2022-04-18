package com.dxw.game2048.widget;

import android.content.Context;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import androidx.core.content.ContextCompat;
import com.dxw.game2048.R;

public class Card extends FrameLayout {

    private int num;
    private TextView label;
    private View background;

    public Card(Context context) {
        super(context);

        LayoutParams lp = null;

        background = new View(getContext());
        lp = new LayoutParams(-1, -1);
        lp.setMargins(10, 10, 0, 0);
        background.setBackgroundColor(0x33ffffff);
        addView(background, lp);

        label = new TextView(getContext()); //新建一个TextView
        label.setTextSize(40);// 设置字体大小
        label.setTypeface(null, Typeface.BOLD);
        label.setGravity(Gravity.CENTER);// 设置字体居中
        label.setBackgroundColor(0xffFFF8DC);// 背景色
        label.setTextColor(ContextCompat.getColor(getContext(), R.color.aquamarine));// 设置字体颜色

        //布局
        new LayoutParams(-1, -1);
        lp.setMargins(20, 20, 0, 0);
        addView(label, lp);

        setNum(0);
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
        if (num <= 0) {
            label.setText("");
        } else {
            label.setText(String.valueOf(num));
        }
        //不同的数字不同的颜色
        switch (num) {
            case 0:
                label.setBackgroundColor(0x00000000);
                break;
            case 2:
                label.setBackgroundColor(0xffeee4da);
                break;
            case 4:
                label.setBackgroundColor(0xffede0c8);
                break;
            case 8:
                label.setBackgroundColor(0xfff2b179);
                break;
            case 16:
                label.setBackgroundColor(0xfff59563);
                break;
            case 32:
                label.setBackgroundColor(0xfff67c5f);
                break;
            case 64:
                label.setBackgroundColor(0xfff65e3b);
                break;
            case 128:
                label.setBackgroundColor(0xffedcf72);
                break;
            case 256:
                label.setBackgroundColor(0xffedcc61);
                break;
            case 512:
                label.setBackgroundColor(0xffedc850);
                break;
            case 1024:
                label.setBackgroundColor(0xffedc53f);
                break;
            case 2048:
                label.setBackgroundColor(0xffedc22e);
                break;
            default:
                label.setBackgroundColor(0xff3c3a32);
                break;
        }
    }

    //判断是否相同，是否可以折叠
    public boolean equals(Card c) {
        return getNum() == c.getNum();
    }

    /*protected Card clone(){
        Card c=new Card(getContext());
        c.setNum(getNum());
        return c;
    }*/

    public TextView getLabel() {
        return label;
    }

}

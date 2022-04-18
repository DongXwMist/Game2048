package com.dxw.game2048.widget.animation;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.dxw.game2048.util.Constant;
import com.dxw.game2048.widget.Card;

public class CardAnimation extends FrameLayout {

    public CardAnimation(@NonNull Context context) {
        super(context);
        initLayer();
    }

    public CardAnimation(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CardAnimation(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initLayer();
    }

    public CardAnimation(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initLayer();
    }

    private void initLayer() {
    }

    /**
     * 创建移动动画
     * */
    public void createMoveAnimate(final Card from, final Card to, int fromX, int toX, int fromY, int toY) {

        //获取一张临时卡片
        final Card c = getCard(from.getNum());
        //设置布局，设置左侧外边距  右侧外边距
        LayoutParams lp = new LayoutParams(Constant.CARD_WIDTH, Constant.CARD_WIDTH);
        lp.leftMargin = fromX * Constant.CARD_WIDTH;
        lp.topMargin = fromY * Constant.CARD_WIDTH;
        //应用设置
        c.setLayoutParams(lp);

        //如果卡片是0  将卡片隐藏
        if (to.getNum() <= 0) {
            to.getLabel().setVisibility(View.INVISIBLE);
        }
        //从from卡片位置移动到to卡片
        //创建一个动画实体
        TranslateAnimation ta = new TranslateAnimation(0, Constant.CARD_WIDTH * (toX - fromX), 0, Constant.CARD_WIDTH * (toY - fromY));
        //设置动画播放速度
        ta.setDuration(100);//动画持续的时间
        //设置动画监听器
        ta.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            //动画结束，将临时卡片回收
            @Override
            public void onAnimationEnd(Animation animation) {
                to.getLabel().setVisibility(View.VISIBLE);
                recycleCard(c);
            }
        });
        c.startAnimation(ta);

    }

    private List<Card> cards = new ArrayList<Card>();

    //创建卡片
    //获取要拷贝的动画的信息 以用来拷贝
    private Card getCard(int num) {
        Card c;

        if (cards.size() > 0) {
            c = cards.remove(0);
        } else {
            c = new Card(getContext());
            addView(c);
        }
        c.setVisibility(View.VISIBLE);
        c.setNum(num);

        return c;
    }

    //回收卡片
    private void recycleCard(Card c) {
        //隐藏卡片
        c.setVisibility(View.INVISIBLE);
        //设置卡片动画为null
        c.setAnimation(null);
        cards.add(c);
    }

    //目标卡片
    //新出现卡片的扩散动画
    public void createScaleTo1(Card target) {
        //缩放
        ScaleAnimation animation = new ScaleAnimation(0.1f, 1, 0.1f, 1, Animation.RELATIVE_TO_SELF,
                0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        //设置动画演示速度
        animation.setDuration(100);
        //卡片动画执行之后回收动画
        target.setAnimation(null);
        //然后开始执行动画
        target.getLabel().startAnimation(animation);
    }

}

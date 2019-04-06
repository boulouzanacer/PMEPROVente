package com.boulouza.uk2018.pme_provente.utils;

import android.animation.ValueAnimator;
import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;

import com.boulouza.uk2018.pme_provente.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * Created by cuneytcarikci on 02/11/2016.
 * This view is layout of list item.
 * when list scrolling margin of items adjust here via eventbus
 */

public class MyCardView2 extends RelativeLayout {

    CardView cardView;

    ValueAnimator increaseAnimation;
    ValueAnimator decreaseAnimation;

    public static final int DURATION = 150;


    public MyCardView2(Context context, int resource) {
        super(context);
        initialize(context,resource);
        EventBus.getDefault().register(this);
    }

    private void initialize(Context context, int resource) {
        View root = inflate(context,resource, this);
        cardView = (CardView) root.findViewById(R.id.item_root);
    }

    @Subscribe
    public void onMessage(ScrollEvent event) {
        int margin = event.getMargin();

        if (margin == 0) {

            if (increaseAnimation != null && increaseAnimation.isRunning())
                increaseAnimation.cancel();

            RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) getLayoutParams();
            int marginBottom = layoutParams.bottomMargin;

            decreaseAnimation = ValueAnimator.ofInt(marginBottom, 0);
            decreaseAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) getLayoutParams();
                    layoutParams.bottomMargin = (int) animation.getAnimatedValue();
                    setLayoutParams(layoutParams);
                }
            });
            decreaseAnimation.setDuration(DURATION);
            decreaseAnimation.start();

        } else {

            if (decreaseAnimation != null && decreaseAnimation.isRunning())
                decreaseAnimation.cancel();

            RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) getLayoutParams();
            int marginBottom = layoutParams.bottomMargin;

            increaseAnimation = ValueAnimator.ofInt(marginBottom, getResources().getDimensionPixelSize(R.dimen.cardview_max_margin_bottom));
            increaseAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) getLayoutParams();
                    layoutParams.bottomMargin = (int) animation.getAnimatedValue();
                    setLayoutParams(layoutParams);
                }
            });
            increaseAnimation.setDuration(DURATION);
            increaseAnimation.start();

        }

    }

}

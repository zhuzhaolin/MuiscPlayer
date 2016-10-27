package com.zhu.muiscplayer.ui.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.zhu.muiscplayer.R;


/**
 * 细数流年，岁月如梭日夜减，三千繁华，仅在弹指一瞬间。
 * 巫山云散，柔情似水相思染，万丈红尘，不过伊人两眉宽。
 * Created by zhuzhaolin
 * User: 2016/10/18
 * Time: 22:27
 * Desc:com.zhu.muiscplayerlib.ui.widget
 */

public class RecyclerViewFastScroller extends LinearLayout {

    private static final int BUBBLE_ANIMATION_DURATION = 100;

    private TextView bubbleView;
    private View fastScroll;
    private RecyclerView mRecyclerView;
    private ObjectAnimator currentAnimator = null;

    private boolean isDragging = false;

    private final RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            if (!isDragging){
                updateBubbleAndHandlePosition();
            }
        }
    };

    public interface BubbleTextGetter{
        String getTextToShowInBubble(int position);
    }

    public RecyclerViewFastScroller(Context context) {
        this(context, null);
    }

    public RecyclerViewFastScroller(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RecyclerViewFastScroller(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    protected void init(){
        setOrientation(HORIZONTAL);
        setClipChildren(false);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        bubbleView = (TextView) findViewById(R.id.bubble);
        bubbleView.setVisibility(View.GONE);
        fastScroll = findViewById(R.id.handle);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        updateBubbleAndHandlePosition();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        final int action = event.getAction();
        switch (action){
            case MotionEvent.ACTION_DOWN:
                if (event.getX() < fastScroll.getX() - ViewCompat.getPaddingStart(fastScroll)){
                    return false;
                }
                if (currentAnimator != null) {
                    currentAnimator.cancel();
                }
                if (bubbleView != null && bubbleView.getVisibility() == GONE){
                   showBubble();
                }
                fastScroll.setSelected(true);
                return true;
            case MotionEvent.ACTION_MOVE:
                isDragging = true;
                setBubbleAndHandlePosition(event.getY());
                setRecyclerViewPosition(event.getY());
                return true;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                isDragging = false;
                fastScroll.setSelected(false);
                hideBubble();
                return true;

        }

        return super.onTouchEvent(event);
    }

    public void setRecyclerView(@NonNull RecyclerView recyclerView){
        if (this.mRecyclerView != recyclerView){
            if (this.mRecyclerView != null){
                this.mRecyclerView.removeOnScrollListener(onScrollListener);
            }
            this.mRecyclerView = recyclerView;
            mRecyclerView.addOnScrollListener(onScrollListener);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mRecyclerView != null) {
            mRecyclerView.removeOnScrollListener(onScrollListener);
            mRecyclerView = null;
        }
    }

    private void setRecyclerViewPosition(float y){
        if (mRecyclerView != null){
            final int itemCount = mRecyclerView.getAdapter().getItemCount();
            float proportion;
            proportion = y / (float) (getHeight() - getPaddingTop());

            final int verticalScrollOffset = mRecyclerView.computeVerticalScrollOffset();
            final int verticalScrollRange = mRecyclerView.computeVerticalScrollRange()
                    - mRecyclerView.getHeight() + mRecyclerView.getPaddingTop() +
                    mRecyclerView.getPaddingBottom();

            float offset = verticalScrollRange * proportion - verticalScrollOffset + 0.5f;
            mRecyclerView.scrollBy(0 , (int) offset);

            final int targetPos = getValueInRange(0, itemCount - 1, (int) (proportion * (float) itemCount));
            final String bubbleText = ((BubbleTextGetter) mRecyclerView.getAdapter()).getTextToShowInBubble(targetPos);
            if (bubbleView != null){
                bubbleView.setText(bubbleText);
            }
        }
    }



    private void updateBubbleAndHandlePosition(){
        if (mRecyclerView == null || bubbleView == null || fastScroll.isSelected()){
            return;
        }

        final int verticalScrollOffset = mRecyclerView.computeVerticalScrollOffset();
        final int verticalScrollRange = mRecyclerView.computeVerticalScrollRange();
        float proportion = (float) verticalScrollOffset/ ((float) verticalScrollRange - getHeight());
        setBubbleAndHandlePosition(getHeight() * proportion);
    }

    private void setBubbleAndHandlePosition(float y){
        final int handleHeight = fastScroll.getHeight();
        fastScroll.setY( getValueInRange(
                getPaddingTop() ,
                getHeight() - handleHeight - getPaddingBottom() ,
                (int) (y - handleHeight / 2)));

        if (bubbleView != null){
            int bubbleHeight = bubbleView.getHeight();
            bubbleView.setY(getValueInRange(
                    getPaddingTop() ,
                    getHeight() - bubbleHeight - handleHeight / 2 - getPaddingBottom(),
                    (int)(y - bubbleHeight)));
        }
    }

    private int getValueInRange(int min , int max , int value){
        int minimum = Math.max(min , value);
        return Math.min(minimum , max);
    }

    private void showBubble(){
        if (bubbleView == null){
            return;
        }
        if (currentAnimator != null){
            currentAnimator.cancel();
        }
        currentAnimator =
                ObjectAnimator.ofFloat(bubbleView , "alpha" , 0f , 1f)
                        .setDuration(BUBBLE_ANIMATION_DURATION);
        currentAnimator.start();
    }

    private void hideBubble(){
        if (bubbleView == null)
            return;;
        if (currentAnimator != null){
            currentAnimator.cancel();
        }
        currentAnimator = ObjectAnimator
                .ofFloat(bubbleView , "alpha" , 1f , 0f)
                .setDuration(BUBBLE_ANIMATION_DURATION);
        currentAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                bubbleView.setVisibility(GONE);
                currentAnimator = null;
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                super.onAnimationCancel(animation);
                bubbleView.setVisibility(GONE);
                currentAnimator = null;
            }
        });
        currentAnimator.start();
    }
}

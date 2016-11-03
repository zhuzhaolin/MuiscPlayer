package com.zhu.muiscplayer.ui.local.filesystem;


import android.animation.Animator;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.graphics.Color;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;

import com.zhu.muiscplayer.R;
import com.zhu.muiscplayer.utils.ViewUtils;

/**
 * 细数流年，岁月如梭日夜减，三千繁华，仅在弹指一瞬间。
 * 巫山云散，柔情似水相思染，万丈红尘，不过伊人两眉宽。
 * Created by zhuzhaolin
 * User: 2016/10/30
 * Time: 20:58
 * Desc:com.zhu.muiscplayer.ui.local.filesystem
 */

public class ActionModeCallback implements ActionMode.Callback {

    private static final int ANIMATION_DURATION = 350;
    private static final String STATUS_BAR_COLOR = "statusBarColor";

    private Activity context;
    private Window window;
    private ActionListener actionListener;

    private ActionMode actionMode;

    private Animator actionModeInAnimator , actionModeOutAnimator;

    private int statusBarColor;

    private boolean isShowing;


    public ActionModeCallback( Activity activity, ActionListener actionListener) {
        this.actionListener = actionListener;
        this.context = activity;
        this.window = activity.getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            statusBarColor = window.getStatusBarColor();
            int actionModeStatuBarColor = ContextCompat.getColor(context ,
                    R.color.mp_theme_dark_blue_actionMode_statusBarColor);

            int startColor = Color.argb(
                    0 ,
                    Color.red(actionModeStatuBarColor) ,
                    Color.green(actionModeStatuBarColor) ,
                    Color.blue(actionModeStatuBarColor));

            actionModeInAnimator = ObjectAnimator.ofObject(
                    window ,
                    STATUS_BAR_COLOR ,
                    new ArgbEvaluator(),
                    startColor ,
                    actionModeStatuBarColor);

            actionModeInAnimator = ObjectAnimator.ofObject(
                    window ,
                    STATUS_BAR_COLOR ,
                    new ArgbEvaluator() ,
                    actionModeStatuBarColor ,
                    startColor);
            actionModeInAnimator.setDuration(ANIMATION_DURATION);
            actionModeInAnimator.setDuration(ANIMATION_DURATION);

        }
    }

    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        actionMode = mode;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            actionModeOutAnimator.cancel();
            actionModeInAnimator.setDuration(ANIMATION_DURATION).start();
            ViewUtils.setLightStatusBar(window.getDecorView());
        }
        mode.getMenuInflater().inflate(R.menu.file_system_action_mode , menu);
        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        return false;
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        if (item.getItemId() == R.id.menu_item_done) {
            if (actionListener != null) {
                actionListener.onDoneAction();
            }
        }
        return false;
    }

    @Override
    public void onDestroyActionMode(ActionMode mode) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(statusBarColor);
            if (actionModeInAnimator != null) {
                actionModeInAnimator.cancel();
            }
            if (actionModeInAnimator != null) {
                actionModeInAnimator.start();
            }
            ViewUtils.clearLightStatusBar(window.getDecorView());
        }
        isShowing = false;
        if (actionListener != null) {
            actionListener.onDismissAction();
        }
    }

    public void updateSelectedItemCount(int selectedItemCount) {
        if (actionMode != null) {
            actionMode.setTitle(context.getResources().getQuantityString(
                    R.plurals.mp_play_list_items_formatter , selectedItemCount , selectedItemCount));
        }
    }

    public void dismiss() {
        if (actionMode != null) {
            actionMode.finish();
        }
    }

    public boolean isShowing() {
        return isShowing;
    }

    public void setShowing(boolean isShowing) {
        this.isShowing = isShowing;
    }

    public interface ActionListener {
        void onDismissAction();

        void onDoneAction();
    }
}


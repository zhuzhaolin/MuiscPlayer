package com.zhu.muiscplayer.ui.playlist;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.zhu.muiscplayer.R;
import com.zhu.muiscplayer.data.model.PlayList;
import com.zhu.muiscplayer.player.IPlayback;
import com.zhu.muiscplayer.ui.base.BaseDialogFragment;

/**
 * 细数流年，岁月如梭日夜减，三千繁华，仅在弹指一瞬间。
 * 巫山云散，柔情似水相思染，万丈红尘，不过伊人两眉宽。
 * Created by zhuzhaolin
 * User: 2016/10/30
 * Time: 15:48
 * Desc:com.zhu.muiscplayer.ui.playlist
 */

public class EditPlayListDialogFragment extends BaseDialogFragment
        implements Dialog.OnShowListener {

    private static final String ARGUMENT_PLAY_LIST = "playList";

    private EditText mEditTextName;

    private PlayList mPlayList;

    private Callback mCallback;

    public static EditPlayListDialogFragment getNewInstance() {
        return newInstance(null);
    }

    public static EditPlayListDialogFragment newInstance(@Nullable PlayList playList) {
        EditPlayListDialogFragment fragment = new EditPlayListDialogFragment();
        if (playList != null) {
            Bundle arguments = new Bundle();
            arguments.putParcelable(ARGUMENT_PLAY_LIST , playList );
            fragment.setArguments(arguments);
        }
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        if (arguments != null) {
            mPlayList = arguments.getParcelable(ARGUMENT_PLAY_LIST);
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Dialog dialog = new AlertDialog.Builder(getActivity())
                .setTitle(getTitle())
                .setView(R.layout.dialog_create_or_edit_play_list)
                .setNegativeButton(R.string.mp_cancel ,null )
                .setPositiveButton(R.string.mp_Confirm, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        onConfirm();
                    }
                }).create();
        dialog.setOnShowListener(this);
        dialog.setOnShowListener(this);
        return dialog;
    }

    @Override
    public void onShow(DialogInterface dialog) {
          resizeDialogSize();
        if (mEditTextName == null) {
            mEditTextName = (EditText) getDialog().findViewById(R.id.edit_text);
            mEditTextName.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if (actionId == EditorInfo.IME_ACTION_DONE) {
                        if (mEditTextName.length() > 0) {
                            onConfirm();
                        }
                        return true;
                    }
                    return false;
                }
            });
        }
        if (isEditMode()) {
            mEditTextName.setText(mPlayList.getName());
        }
        mEditTextName.requestFocus();
        mEditTextName.setSelection(mEditTextName.length());
    }

    public EditPlayListDialogFragment setCallback(Callback callback) {
        mCallback = callback;
        return this;
    }

    private void onConfirm() {
        if (mCallback == null) return;

        PlayList playList = mPlayList;
        if (playList == null) {
            playList = new PlayList();
        }
        playList.setName(mEditTextName.getText().toString());
        if (isEditMode()) {
            mCallback.onEdited(playList);
        } else {
            mCallback.onCreated(playList);
        }
    }

    private boolean isEditMode() {
        return mPlayList != null;
    }

    private String getTitle() {
        return getContext().getString(isEditMode() ?
        R.string.mp_play_list_edit : R.string.mp_play_list_create);
    }

    public static EditPlayListDialogFragment createPlayList() {
        return getNewInstance();
    }

    public static EditPlayListDialogFragment editPlayList(PlayList playList) {
        return newInstance(playList);
    }

    public interface Callback {

        void onCreated(PlayList playList);

        void onEdited(PlayList playList);
    }
}

package com.zhu.muiscplayer.ui.local;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import com.zhu.muiscplayer.R;
import com.zhu.muiscplayer.ui.local.all.AllLocalMusicFragment;
import com.zhu.muiscplayer.ui.local.folder.FolderFragmet;
import com.zhu.muiscplayer.ui.base.BaseFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;


/**
 * 细数流年，岁月如梭日夜减，三千繁华，仅在弹指一瞬间。
 * 巫山云散，柔情似水相思染，万丈红尘，不过伊人两眉宽。
 * Created by zhuzhaolin
 * User: 2016/10/17
 * Time: 20:35
 * Desc:com.zhu.muiscplayer.ui.local
 */

public class LocalFilesFragment extends BaseFragment {

    static final int DEFAULT_SEGMENT_INDEX = 0;

    @BindViews({R.id.radio_button_all , R.id.radio_button_folder})
    List<RadioButton> segmentedControls;

    List<Fragment> mFragments = new ArrayList<>(2);

    final int[] FRAGMENT_CONTAINER_IDS = {
            R.id.layout_fragment_container_all , R.id.layout_fragment_container_folder};


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFragments.add(new AllLocalMusicFragment());
        mFragments.add(new FolderFragmet());
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_local_files, container, false);
        ButterKnife.bind(this , view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        for (int i = 0; i < mFragments.size() ; i++) {
            Fragment fragment = mFragments.get(i);
            fragmentTransaction.add(FRAGMENT_CONTAINER_IDS[i] , fragment , fragment.getTag());
            fragmentTransaction.hide(fragment);
        }
        fragmentTransaction.commit();
        segmentedControls.get(DEFAULT_SEGMENT_INDEX).setChecked(true);
    }

    @OnCheckedChanged({R.id.radio_button_all, R.id.radio_button_folder})
    public void onSegmentedChecked(RadioButton radioButton , boolean isChecked) {
        int index = segmentedControls.indexOf(radioButton);
        Fragment fragment = mFragments.get(index);
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        if (isChecked) {
            fragmentTransaction.show(fragment);
        } else {
            fragmentTransaction.hide(fragment);
        }
        fragmentTransaction.commit();
    }
}

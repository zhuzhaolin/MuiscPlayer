package com.zhu.muiscplayer.ui.playlist;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhu.muiscplayer.R;
import com.zhu.muiscplayer.data.model.PlayList;
import com.zhu.muiscplayer.ui.base.adapter.IAdapterView;
import com.zhu.muiscplayer.utils.ViewUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 细数流年，岁月如梭日夜减，三千繁华，仅在弹指一瞬间。
 * 巫山云散，柔情似水相思染，万丈红尘，不过伊人两眉宽。
 * Created by zhuzhaolin
 * User: 2016/10/30
 * Time: 14:37
 * Desc:com.zhu.muiscplayer.ui.playlist
 */

public class PlayListItemView extends RelativeLayout implements IAdapterView<PlayList> {

    @BindView(R.id.image_view_album)
    AppCompatImageView mImageViewAlbum;
    @BindView(R.id.text_view_name)
    TextView mTextViewName;
    @BindView(R.id.text_view_info)
    TextView mTextViewInfo;
    @BindView(R.id.layout_action)
    View buttonAction;

    public PlayListItemView(Context context) {
        super(context);
        View.inflate(context, R.layout.item_play_list, this);
        ButterKnife.bind(this);
    }

    @Override
    public void bind(PlayList item, int position) {
        if (item.isFavorite()) {
            mImageViewAlbum.setImageResource(R.drawable.ic_favorite_yes);
        } else {
            mImageViewAlbum.setImageDrawable(ViewUtils.generateAlbumDrawable(getContext() , item.getName()));
        }

        mTextViewName.setText(item.getName());
        mTextViewInfo.setText(getResources().getQuantityString(
          R.plurals.mp_play_list_items_formatter , item.getItemCount() , item.getItemCount()));
    }
}

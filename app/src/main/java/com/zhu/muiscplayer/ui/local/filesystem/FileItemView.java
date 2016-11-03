package com.zhu.muiscplayer.ui.local.filesystem;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhu.muiscplayer.R;
import com.zhu.muiscplayer.ui.base.adapter.IAdapterView;
import com.zhu.muiscplayer.utils.FileUtils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 细数流年，岁月如梭日夜减，三千繁华，仅在弹指一瞬间。
 * 巫山云散，柔情似水相思染，万丈红尘，不过伊人两眉宽。
 * Created by zhuzhaolin
 * User: 2016/10/30
 * Time: 16:55
 * Desc:com.zhu.muiscplayer.ui.local.filesystem
 */

public class FileItemView extends RelativeLayout implements IAdapterView<FileWrapper> {

    private static final String DATE_FORMAT = "yyyy-MM-dd";
    private static SimpleDateFormat DATE_FORMATTER;


    static {
        DATE_FORMATTER = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault());
    }

    @BindView(R.id.image_view_file)
    AppCompatImageView mImageViewFile;
    @BindView(R.id.text_view_name)
    TextView mTextViewName;
    @BindView(R.id.text_view_info)
    TextView mTextViewInfo;
    @BindView(R.id.text_view_date)
    TextView mTextViewDate;

    public FileItemView(Context context) {
        super(context);
        View.inflate(context, R.layout.item_local_file, this);
        ButterKnife.bind(this);
    }

    @Override
    public void bind(FileWrapper fileWrapper, int position) {
        final File file =  fileWrapper.file;
        final boolean isItemSelected = fileWrapper.selected;
        if (file.isDirectory()) {
            setSelected(isItemSelected);
            mImageViewFile.setImageResource(isItemSelected ?
                    R.drawable.ic_folder_selected : R.drawable.ic_folder);

            File[] files = file.listFiles(SystemFileFilter.DEFAULT_INSTANCE);
            int itemCount = files == null ? 0 : files.length;
            mTextViewInfo.setText(getContext().getResources().getQuantityString(
                    R.plurals.mp_play_list_items_formatter,
                    itemCount,
                    itemCount));
        } else {
            setSelected(false);
            if (FileUtils.isMusic(file)) {
                mImageViewFile.setImageResource(R.drawable.ic_file_music);
            } else if (FileUtils.isLyric(file)) {
                mImageViewFile.setImageResource(R.drawable.ic_file_lyric);
            } else {
                mImageViewFile.setImageResource(R.drawable.ic_file);
            }
            mTextViewName.setText(file.getName());
            Date lastModified = new Date(file.lastModified());
            mTextViewDate.setText(DATE_FORMATTER.format(lastModified));
        }
    }
}

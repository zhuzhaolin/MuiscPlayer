package com.zhu.muiscplayer.utils;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.zhu.muiscplayer.R;
import com.zhu.muiscplayer.ui.widget.CharacterDrawable;

/**
 * 细数流年，岁月如梭日夜减，三千繁华，仅在弹指一瞬间。
 * 巫山云散，柔情似水相思染，万丈红尘，不过伊人两眉宽。
 * Created by zhuzhaolin
 * User: 2016/10/30
 * Time: 14:57
 * Desc:com.zhu.muiscplayer.utils
 */

public class ViewUtils {

    public static void setLightStatusBar(@NonNull View view) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int flags = view.getSystemUiVisibility();
            flags |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            view.setSystemUiVisibility(flags);
        }
    }

    public static void clearLightStatusBar(@NonNull View view) {

        int flags = view.getSystemUiVisibility();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            flags &= ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            view.setSystemUiVisibility(flags);
        }
    }

    public static CharacterDrawable generateAlbumDrawable(Context context , String albumName) {
        if (context == null || albumName == null) return null;

        return new CharacterDrawable.Builder()
                .setCharacter(albumName.length() == 0 ? ' ' : albumName.charAt(0))
                .setBackgroundColor(ContextCompat.getColor(context , R.color.mp_characterView_background))
                .setCharacterTextColor(ContextCompat.getColor(context , R.color.mp_characterView_textColor))
                .setCharacterPadding(context.getResources().getDimensionPixelSize(R.dimen.mp_characterView_padding))
                .build();

    }
}

package com.zhu.muiscplayer.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;

import com.zhu.muiscplayer.data.model.Song;

import java.io.File;

/**
 * 细数流年，岁月如梭日夜减，三千繁华，仅在弹指一瞬间。
 * 巫山云散，柔情似水相思染，万丈红尘，不过伊人两眉宽。
 * Created by zhuzhaolin
 * User: 2016/10/25
 * Time: 11:04
 * Desc:com.zhu.muiscplayer.utils
 */

public class AlbumUtils {

    public static Bitmap parseAlbum(Song song){
        return parseAlbum(new File(song.getPath()));
    }

    public static Bitmap parseAlbum(File file) {
        MediaMetadataRetriever metadataRetriever = new MediaMetadataRetriever();
        metadataRetriever.setDataSource(file.getAbsolutePath());
        byte[] albumData = metadataRetriever.getEmbeddedPicture();
        if (albumData != null) {
            return BitmapFactory.decodeByteArray(albumData, 0 , albumData.length);
        }
        return null;
    }

    public static Bitmap getCroppedBitmap(Bitmap bitmap) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth() ,
                bitmap.getHeight() , Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0 , 0 ,bitmap.getWidth() , bitmap.getHeight());

        paint.setAntiAlias(true);
        canvas.drawARGB(0 , 0 , 0 , 0);
        paint.setColor(color);

        canvas.drawCircle(bitmap.getWidth()/2 , bitmap.getHeight()/2 ,
                bitmap.getWidth() / 2 , paint);

        canvas.drawBitmap(bitmap , rect , rect , paint);

        return output;
    }
}

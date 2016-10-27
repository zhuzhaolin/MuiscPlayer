package com.zhu.muiscplayer.utils;

import android.database.Cursor;
import android.media.MediaMetadataRetriever;
import android.provider.MediaStore;

import com.zhu.muiscplayer.data.model.Folder;
import com.zhu.muiscplayer.data.model.Song;

import java.io.File;
import java.io.FileFilter;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * 细数流年，岁月如梭日夜减，三千繁华，仅在弹指一瞬间。
 * 巫山云散，柔情似水相思染，万丈红尘，不过伊人两眉宽。
 * Created by zhuzhaolin
 * User: 2016/10/21
 * Time: 11:00
 * Desc:com.zhu.muiscplayer.utils
 */

public class FileUtils {

    /**
     * http://stackoverflow.com/a/5599842/2290191
     *
     * @param size Original file size in byte
     * @return Readable file size in formats
     */
    public static String readableFileSize(long size) {
        if (size <= 0) {
            return "0";
        }
        final String[] units = new String[]{"b", "kb", "M", "G", "T"};
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
        return new DecimalFormat("# , ##0.##").format(size / Math.pow(1024 , digitGroups))+"" + units[digitGroups];
    }

    public static boolean isMusic(File file){
        final String REGEX = "(.*/)*.+\\.(mp3|ogg|wav|aac)$";
        return file.getName().matches(REGEX);
    }

    public static boolean isLyric(File file){
        return file.getName().toLowerCase().endsWith(".lrc");
    }

    public static List<Song> musicFiles(File dir){
        List<Song> songs = new ArrayList<>();
        if (dir != null && dir.isDirectory()) {
            final File[] files = dir.listFiles(new FileFilter() {
                @Override
                public boolean accept(File pathname) {
                    return pathname.isFile() && isMusic(pathname);
                }
            });
            for (File file : files) {
                Song song = fileToMusic(file);
                if (song != null) {
                    songs.add(song);
                }
            }
        }
        if (songs.size() > 1) {
            Collections.sort(songs, new Comparator<Song>() {
                @Override
                public int compare(Song left , Song right) {
                    return left.getTitle().compareTo(right.getTitle());
                }
            });
        }
        return songs;
    }

    public static Song fileToMusic(File file) {
        if (file.length() == 0) {
            return null;
        }
        MediaMetadataRetriever metadataRetriever = new MediaMetadataRetriever();
        metadataRetriever.setDataSource(file.getAbsolutePath());

        String title = metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
        String displayName = metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
        String artist = metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
        String album = metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM);
        int duration = Integer.parseInt(
                metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION));

        if (duration == 0) return null;

        Song song = new Song();
        song.setTitle(title);
        song.setDisplayName(displayName);
        song.setArtist(artist);
        song.setPath(file.getAbsolutePath());
        song.setAlbum(album);
        song.setDuration(duration);
        song.setSize((int) file.length());
        return song;
    }

    public static Song parseMuiscPath(Cursor cursor){
        String realPath = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));
        File file = new File(realPath);
        if (file.length() == 0) {
            return null;
        }
        MediaMetadataRetriever metadataRetriever = new MediaMetadataRetriever();
        metadataRetriever.setDataSource(file.getAbsolutePath());

        String title = metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
        String displayName = metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
        //有些歌曲如果从MediaMetadataRetriever获取displayName为空的情况时，转为用一下方法获取
        String artist = metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
        if (displayName == null){
            displayName = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME));
            artist = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST));
        }
        String album = metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM);
        int duration = Integer.parseInt(
                metadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION));

        if (duration == 0) return null;

        Song song = new Song();
        song.setTitle(title);
        song.setDisplayName(displayName);
        song.setArtist(artist);
        song.setPath(file.getAbsolutePath());
        song.setAlbum(album);
        song.setDuration(duration);
        song.setSize((int) file.length());
        return song;
    }

    public static Folder folderFromDir(File dir) {
        Folder folder = new Folder(dir.getName(), dir.getAbsolutePath());
        List<Song> songs = musicFiles(dir);
        folder.setSongs(songs);
        folder.setNumOfSongs(songs.size());
        return folder;
    }
}

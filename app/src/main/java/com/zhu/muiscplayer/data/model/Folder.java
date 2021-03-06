package com.zhu.muiscplayer.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.litesuits.orm.db.annotation.Column;
import com.litesuits.orm.db.annotation.MapCollection;
import com.litesuits.orm.db.annotation.Mapping;
import com.litesuits.orm.db.annotation.PrimaryKey;
import com.litesuits.orm.db.annotation.Unique;
import com.litesuits.orm.db.enums.AssignType;
import com.litesuits.orm.db.enums.Relation;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 细数流年，岁月如梭日夜减，三千繁华，仅在弹指一瞬间。
 * 巫山云散，柔情似水相思染，万丈红尘，不过伊人两眉宽。
 * Created by zhuzhaolin
 * User: 2016/10/20
 * Time: 22:21
 * Desc:com.zhu.muiscplayer.data.model
 */

public class Folder implements Parcelable{

    public static final String COLUMN_NAME = "name";

    @PrimaryKey(AssignType.AUTO_INCREMENT)
    private int id;

    @Column(COLUMN_NAME)
    private String name;

    @Unique
    private String path;

    private int numOfSongs;

    @MapCollection(ArrayList.class)
    @Mapping(Relation.OneToMany)
    private List<Song> songs = new ArrayList<>();

    private Date createdAt;

    public Folder(){

    }

    public Folder(String name, String path) {
        this.name = name;
        this.path = path;
    }

    public static String getColumnName() {
        return COLUMN_NAME;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public static Creator<Folder> getCREATOR() {
        return CREATOR;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNumOfSongs() {
        return numOfSongs;
    }

    public void setNumOfSongs(int numOfSongs) {
        this.numOfSongs = numOfSongs;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public List<Song> getSongs() {
        return songs;
    }

    public void setSongs(List<Song> songs) {
        this.songs = songs;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.name);
        dest.writeString(this.path);
        dest.writeInt(this.numOfSongs);
        dest.writeTypedList(this.songs);
        dest.writeLong(this.createdAt != null ? this.createdAt.getTime() : -1);
    }

    protected Folder(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
        this.path = in.readString();
        this.numOfSongs = in.readInt();
        this.songs = in.createTypedArrayList(Song.CREATOR);
        long tmpCreatedAt = in.readLong();
        this.createdAt = tmpCreatedAt == -1 ? null : new Date(tmpCreatedAt);
    }

    public static final Creator<Folder> CREATOR = new Creator<Folder>() {
        @Override
        public Folder createFromParcel(Parcel source) {
            return new Folder(source);
        }

        @Override
        public Folder[] newArray(int size) {
            return new Folder[size];
        }
    };
}

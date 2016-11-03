package com.zhu.muiscplayer.ui.local.filesystem;

import android.support.annotation.UiThread;

import java.io.File;
import java.util.List;


/**
 * 细数流年，岁月如梭日夜减，三千繁华，仅在弹指一瞬间。
 * 巫山云散，柔情似水相思染，万丈红尘，不过伊人两眉宽。
 * Created by zhuzhaolin
 * User: 2016/10/30
 * Time: 20:46
 * Desc:com.zhu.muiscplayer.ui.local.filesystem
 */
@UiThread
public class FileTreeStack {

    private Node mFirstNode;
    private int mSize;

    public FileTreeSnapshot pop() {
        if (mFirstNode == null) return null;
        FileTreeSnapshot snapshot = mFirstNode.snapshot;
        mFirstNode = mFirstNode.next;
        mSize--;
        return snapshot;
    }

    public void push(FileTreeSnapshot snapshot) {
        Node node = new Node();
        node.snapshot = snapshot;
        node.next = mFirstNode;
        mFirstNode = node;
        mSize++;
    }

    public int size() {
        return mSize;
    }

    static class Node {
        FileTreeSnapshot snapshot;
        Node next;
    }


    static class FileTreeSnapshot {
        public File parent;
        public List<FileWrapper> files;
        public int scrollOffset;
    }

}


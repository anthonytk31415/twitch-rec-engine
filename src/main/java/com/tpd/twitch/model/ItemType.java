package com.tpd.twitch.model;

public enum ItemType {        // enum is a special class
    STREAM, VIDEO, CLIP
}
/* This enum also can be written as

public class ItemType {
    public static final int STREAM_TYPE = 0;
    public static final int VIDEO_TYPE = 1;
    public static final int CLIP_TYPE = 2;
}

public static void main(String[] args) {
    ItemType myFavoriteType = 0; // STREAM_TYPE

    // can be rewritten
    myFavoriteType = 3;
    // This way of writing is prone to errors. Enums are better because they restrict the options to only those
    // listed in the enum and prevent selecting options outside of them.
}

*/

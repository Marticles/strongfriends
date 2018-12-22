package com.strongfriends.util;

public class RedisKeyUtil {
    private static String SPLIT = ":";
    private static String BIZ_LIKE = "LIKE";
    private static String BIZ_DISLIKE = "DISLIKE";
    private static String BIZ_EVENT = "EVENT";
    private static String BIZ_COMMENT = "COMMENT";
    private static String BIZ_TRACK = "TRACK_TASK";


    public static String getEventQueueKey() {
        return BIZ_EVENT;
    }

    public static String getTrackTaskKey(){
        return BIZ_TRACK;
    }

    public static String getLikeKey(int entityId, int entityType) {
        // "Like:1:user_id"
        return BIZ_LIKE + SPLIT + String.valueOf(entityType) + SPLIT + String.valueOf(entityId);
    }

    public static String getDisLikeKey(int entityId, int entityType) {
        return BIZ_DISLIKE + SPLIT + String.valueOf(entityType) + SPLIT + String.valueOf(entityId);
    }

    public static String getCommentKey(int entityId, int entityType) {
        // "COMMENT:0:user_id"
        return BIZ_COMMENT + SPLIT + String.valueOf(entityType) + SPLIT + String.valueOf(entityId);
    }
}

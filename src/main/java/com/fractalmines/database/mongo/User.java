package com.fractalmines.database.mongo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class User {
    private boolean registered = false;

    private long id = 5;
    private long accountID = 5;
    private String username;

    private long lastPlayed;

    private int coins = 0;
    private int userCoins = 0;

    private int primaryColor = 0;
    private int secondaryColor = 3;
    private int glowColor = 0;

    private int stars = 0;
    private int diamonds = 0;
    private int orbs = 0;
    private int moons = 0;
    private int demons = 0;
    private int creatorPoints = 0;
    private int completedLevels = 0;

    private int messageState = 0;
    private int requestsState = 0;
    private int commentState = 0;
    private int friendState = 0;

    private String youtubeUrl = "";
    private String twitterUrl = "";
    private String twitchUrl = "";

    private int cube = 0;
    private int ship = 0;
    private int ball = 0;
    private int ufo = 0;
    private int wave = 0;
    private int robot = 0;
    private int spider = 0;
    private int swing = 0;
    private int jetpack = 0;
    private int glow = 0;
    private int explosion = 0;

    private int rank = -1;
    private int badge = 0;

    private String demonInfo = "0,0,0,0,0";
    private String starInfo = "0,0,0,0,0,0";
    private String platformerInfo = "0,0,0,0,0,0";

    private String ip;

    private boolean leaderboardBanned = false;
    private boolean creatorBanned = false;

    public String toUserDataString(boolean self) {
        int messages = 0;
        int newFriends = 0;
        int friendRequests = 0;

        String appendix = self ? ":38:%d:39:%d:40:%d".formatted(messages, newFriends, friendRequests) : "";

        return "1:%s:2:%s:13:%d:17:%d:10:%d:11:%d:51:%d:3:%d:46:%d:52:%d:4:%d:8:%d:18:%d:19:%d:50:%d:20:%s:21:%d:22:%d:23:%d:24:%d:25:%d:26:%d:28:%d:43:%d:48:%d:53:%d:54:%d:30:%d:16:%d:31:%d:44:%s:45:%s:49:%d:55:%s:56:%s:57:%s%s:29:1".formatted(
                username, id, coins, userCoins, primaryColor, secondaryColor, glowColor, stars, diamonds, moons, demons, creatorPoints,
                messageState, requestsState, commentState, youtubeUrl,
                cube, ship, ball, ufo, wave, robot, glow, spider, explosion, swing, jetpack,
                rank, accountID, friendState, twitterUrl, twitchUrl, badge, demonInfo, starInfo, platformerInfo, appendix
        );
    }
}
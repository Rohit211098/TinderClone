package com.example.tinder.Matches;

public class MatchPojo {
    private String matchId,matchName,matchImage;


    public MatchPojo(String matchId, String matchName, String matchImage) {
        this.matchId = matchId;
        this.matchName = matchName;
        this.matchImage = matchImage;
    }

    public String getMatchId() {
        return matchId;
    }

    public String getMatchName() {
        return matchName;
    }

    public String getMatchImage() {
        return matchImage;
    }
}

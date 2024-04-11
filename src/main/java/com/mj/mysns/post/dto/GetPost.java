package com.mj.mysns.post.dto;

import lombok.Builder;

@Builder
public record GetPost(

    String username,

    String targetUsername,

    Double latitude,

    Double longitude,

    String addressCode,

    Double distance,

    Integer limit,

    Integer offset
) {

    public GetPost(String username, String targetUsername, Double latitude, Double longitude, String addressCode) {
        this(username, targetUsername, latitude, longitude, addressCode, null, null, null);
    }

    public GetPost(String username, String targetUsername, Double latitude, Double longitude, String addressCode,
        Double distance, Integer limit, Integer offset) {

        this.username = username;;
        this.targetUsername = targetUsername;
        this.latitude = latitude;
        this.longitude = longitude;
        this.addressCode = addressCode;
        this.distance = distance == null ? 1000.0 : distance;
        this.limit = limit == null ? 10 : limit;
        this.offset = offset == null ? 0 : offset;
    }
}

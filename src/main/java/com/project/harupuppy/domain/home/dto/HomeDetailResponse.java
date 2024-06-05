package com.project.harupuppy.domain.home.dto;

import com.project.harupuppy.domain.home.domain.Home;

public record HomeDetailResponse(
        String homeId,
        String homeName
) {
    public static HomeDetailResponse of(Home home){
        return new HomeDetailResponse(
                home.getHomeId(),
                home.getHomeName());
    }
}

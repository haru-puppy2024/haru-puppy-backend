package com.project.harupuppy.domain.home.dto;

import com.project.harupuppy.domain.dog.dto.DogDetailResponse;

import java.util.List;

public record HomeResponse(
        DogDetailResponse dogDetailResponse,
        List<MateDto> mateDto,
        ReportDto reportDto,
        List<RankingDto> rankingDto
) {
    public static HomeResponse of(DogDetailResponse dog, List<MateDto> mates, ReportDto report, List<RankingDto> rankings) {
        return new HomeResponse(
                dog,
                mates,
                report,
                rankings
        );
    }
}

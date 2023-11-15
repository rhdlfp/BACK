package com.example.springsocial.comunity.heart.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HeartDto {
	private int recommendNum;
	private boolean isRecommended;
	
	public static HeartDto noOne() {
		return HeartDto.builder()
				.recommendNum(0)
				.isRecommended(false)
				.build();
	}
}

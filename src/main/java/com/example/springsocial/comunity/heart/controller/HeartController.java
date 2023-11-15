package com.example.springsocial.comunity.heart.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;

import com.example.springsocial.comunity.heart.dto.DeleteHeartDto;
import com.example.springsocial.comunity.heart.dto.HeartDto;
import com.example.springsocial.comunity.heart.dto.MessageDto;
import com.example.springsocial.comunity.heart.dto.PostHeartDto;
import com.example.springsocial.comunity.heart.service.HeartService;



@RestController
@RequiredArgsConstructor
@RequestMapping("/heart")
public class HeartController {
	private final HeartService heartService;
	
	@GetMapping("/list")
	public ResponseEntity<HeartDto> getHeart(@RequestParam(name="id") Long id){
		return ResponseEntity.ok(heartService.allHeart(id));
	}
	
	@PostMapping("/addlike")
	public ResponseEntity<MessageDto> postHeart(@RequestBody PostHeartDto dto) {
	    heartService.createHeart(dto.getUserId(), dto.getComunityId());
	    return ResponseEntity.ok(new MessageDto("Success"));
	}


	@DeleteMapping("/deletelike")
	public ResponseEntity<MessageDto> deleteHeart(@RequestParam Long userId, @RequestParam Long comunityId){
	  heartService.removeHeart(userId, comunityId);
	  return ResponseEntity.ok(new MessageDto("Success"));
	}

}
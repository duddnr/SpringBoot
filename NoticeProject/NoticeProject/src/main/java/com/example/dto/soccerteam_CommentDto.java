package com.example.dto;

import com.example.entity.soccerteams_Comment;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class soccerteam_CommentDto 
{
	private Long id;
	private String nickname;
	private String body;
	
	public soccerteams_Comment toEntity()
	{
		return new soccerteams_Comment(id, nickname, body, null);
	}
}
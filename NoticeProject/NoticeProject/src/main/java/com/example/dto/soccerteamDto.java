package com.example.dto;

import com.example.entity.soccerteams;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@ToString
@Getter
public class soccerteamDto 
{
	private Long id;
	private String writer;
	private String title;
	private String content;
	
	public soccerteams toEntity()
	{
		return new soccerteams(id, writer, title, content, 0L, 0L, null);
	}
}
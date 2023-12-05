package com.example.entity;

import com.example.dto.soccerteam_CommentDto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
@Entity
public class soccerteams_Comment 
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column
	private String nickname;
	@Column
	private String body;
	@ManyToOne
	@JoinColumn(name="soccerteams_id")
	private soccerteams soccerteamsEntity;
	
	public static soccerteams_Comment createComment(soccerteam_CommentDto dto, soccerteams soccer)
	{
		if(dto.getId() != null)
		{
			throw new IllegalArgumentException("댓글 생성 실패! 댓글의 id가 없어야 합니다.");
		}
		
		return new soccerteams_Comment(dto.getId(), dto.getNickname(), dto.getBody(), soccer);
	}
	
	public void patch(soccerteams_Comment commentEntity)
	{
		if(commentEntity.getNickname() != null)
		{
			this.nickname = commentEntity.getNickname();
		}
		
		if(commentEntity.getBody() != null)
		{
			this.body = commentEntity.getBody();
		}
	}
}
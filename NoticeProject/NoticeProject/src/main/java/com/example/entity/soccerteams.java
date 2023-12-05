package com.example.entity;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Getter
@Setter
@Entity
public class soccerteams 
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column
	private String writer;
	@Column
	private String title;
	@Column
	private String content;
	@Column
	private Long views;
	@Column
	private Long comment_;
	@Column
	private String created;
	
	@PrePersist // 해당 엔티티를 저장하기 이전에 실행되는 메서드
	public void onPrePersist()
	{
		this.created = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yy-MM-dd HH:mm"));
	}
	
	public void patch(soccerteams entity)
	{
		if(entity.getTitle() != null)
		{
			this.title = entity.getTitle();
		}
		
		if(entity.getContent() != null)
		{
			this.content = entity.getContent();
		}
	}
}
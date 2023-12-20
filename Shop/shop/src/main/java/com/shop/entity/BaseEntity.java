package com.shop.entity;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;

@Getter
@MappedSuperclass
@EntityListeners(value= {AuditingEntityListener.class})
public class BaseEntity extends BaseTimeEntity
{
	@CreatedBy
	@Column(updatable=false)
	private String createdBy;
	
	@LastModifiedBy
	private String modifiedBy;
}
package com.security.constant;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum Gender 
{
	MAN, GIRL;
	
	@JsonCreator
	public static Gender fromString(String s)
	{
		for(Gender gender : Gender.values())
		{
			if(gender.name().equals(s))
			{
				return gender;
			}
		}
		return null;
	}
}
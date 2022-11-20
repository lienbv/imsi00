package com.vibee.model.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.bind.DefaultValue;

@Setter
@Getter
public class BaseRequest {
	@Value("vi")
    private String language = "";

	@Override
	public String toString() {
		return "BaseRequest [language=" + language + ", getLanguage()=" + getLanguage() + ", getClass()=" + getClass()
				+ ", hashCode()=" + hashCode() + ", toString()=" + super.toString() + "]";
	}
	
	
}

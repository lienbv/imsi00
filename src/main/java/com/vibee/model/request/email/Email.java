package com.vibee.model.request.email;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Email {
	private String from, to, content, subject, frompassword;
}

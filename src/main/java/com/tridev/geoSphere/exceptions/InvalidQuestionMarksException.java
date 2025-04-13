package com.tridev.geoSphere.exceptions;

public class InvalidQuestionMarksException extends Exception{
	private static final long serialVersionUID = 1L;

	public InvalidQuestionMarksException(String ids) {
		super(ids);
	}
}

package com.tridev.geoSphere.response;

import lombok.Data;

@Data
public class InputKeyError {

	 private Integer responseCode;
	 private String key;
	 private String errorDescription;
}

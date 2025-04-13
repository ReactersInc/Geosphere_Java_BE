package com.tridev.geoSphere.response;

import lombok.Data;

@Data
public class ErrorBaseResponse {

	private Object data;
    private InputKeyError result;
}

package com.csg.learning.tool.app.elastic.service;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "person not found")
public class TradeMappingNotFoundException extends RuntimeException  {
}

package com.github.muehmar.reactive.data;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;

public class HttpResponse extends ResponseEntity<Object> {
  public HttpResponse(HttpStatus status) {
    super(status);
  }

  public HttpResponse(Object body, HttpStatus status) {
    super(body, status);
  }

  public HttpResponse(MultiValueMap<String, String> headers, HttpStatus status) {
    super(headers, status);
  }

  public HttpResponse(Object body, MultiValueMap<String, String> headers, HttpStatus status) {
    super(body, headers, status);
  }

  public HttpResponse(Object body, MultiValueMap<String, String> headers, int rawStatus) {
    super(body, headers, rawStatus);
  }
}

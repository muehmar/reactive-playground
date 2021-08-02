package com.github.muehmar.reactive.data;

import java.util.Collections;
import org.springframework.http.HttpStatus;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

public class HttpResponses {
  private HttpResponses() {}

  public static HttpResponse okJson(Object object) {
    final MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
    headers.put("Content-Type", Collections.singletonList("application/json"));
    return new HttpResponse(object, headers, HttpStatus.OK);
  }
}

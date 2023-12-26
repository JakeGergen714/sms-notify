package com.jake.frontendapi.service;

import com.jake.frontendapi.dto.WaitListItemDTO;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@RequiredArgsConstructor
@Component
public class WaitListService {
  private final RestTemplate restTemplate;
  @Value("${url.waitlist-service}")
  private String waitListServiceUrl;
  public List<WaitListItemDTO> getAllWaitListItems(String accessToken) {
    RequestEntity<Void> requestEntity =
        RequestEntity.get(waitListServiceUrl + "/waitList")
            .header("accessToken", accessToken)
            .build();
    ResponseEntity<List<WaitListItemDTO>> response =
        restTemplate.exchange(
            requestEntity, new ParameterizedTypeReference<List<WaitListItemDTO>>() {});
    if (response.getStatusCode().isSameCodeAs(HttpStatus.OK)) {
      return response.getBody();
    }

    return List.of();
  }

  public boolean addWaitListItem(String accessToken, WaitListItemDTO waitListItemDTO) {
    RequestEntity<WaitListItemDTO> requestEntity =
        RequestEntity.post(waitListServiceUrl + "/waitList")
            .header("accessToken", accessToken)
            .body(waitListItemDTO);
    ResponseEntity<Void> response = restTemplate.exchange(requestEntity, Void.class);
    return response.getStatusCode().isSameCodeAs(HttpStatus.OK);
  }
}

package com.jake.auth.dto;

import com.jake.auth.domain.Business;
import lombok.Data;

@Data
public class BusinessDto {
  private String name;

  public static BusinessDto from(Business business) {
    BusinessDto dto = new BusinessDto();

    dto.setName(business.getName());
    return dto;
  }
}

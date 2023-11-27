package com.jake.waitlistservice.service;

import com.jake.waitlistservice.dto.WaitListItemDTO;
import com.jake.waitlistservice.jpa.domain.WaitListItem;
import com.jake.waitlistservice.jpa.repository.WaitListItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Component
public class WaitListService {
    private final WaitListItemRepository repo;

    public List<WaitListItem> findAllForUser(String userName) {
        return repo.findAllByBusinessId(userName, Sort.by(Sort.Direction.ASC, "checkInTime"));
    }

    public void add(WaitListItemDTO waitListItemDTO, String businessId) {
        WaitListItem waitListItem = new WaitListItem();
        waitListItem.setBusinessId(businessId);
        waitListItem.setCustomerName(waitListItemDTO.getCustomerName());
        waitListItem.setPartySize(waitListItemDTO.getPartySize());
        waitListItem.setNotes(waitListItemDTO.getNotes());
        waitListItem.setQuotedTimeMinutes(waitListItemDTO.getQuotedTimeMinutes());
        waitListItem.setWaitTimeMinutes(waitListItemDTO.getWaitTimeMinutes());
        repo.save(waitListItem);
    }
}

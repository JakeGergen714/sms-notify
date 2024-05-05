package com.jake.guestservice.service;

import com.jake.datacorelib.guest.jpa.Guest;
import com.jake.datacorelib.guest.jpa.GuestRepository;
import com.jake.datacorelib.guest.jpa.GuestStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.util.List;

@RequiredArgsConstructor
@Log4j2
public class GuestService {
    private final GuestRepository guestRepo;

    public List<Guest> findAllForBusiness(long businessId) {
        return guestRepo.findAllByBusinessId(businessId);
    }

    public List<Guest> findGuestsByStatus(long businessId, GuestStatus guestStatus) {
        return guestRepo.findAllByBusinessIdAndGuestStatus(businessId, guestStatus);
    }
}

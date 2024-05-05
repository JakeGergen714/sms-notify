package com.jake.datacorelib.guest.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GuestRepository extends JpaRepository<Guest, Long> {

    List<Guest> findAllByBusinessId(Long businessId);

    List<Guest> findAllByBusinessIdAndStatus(Long businessId, GuestStatus guestStatus);
}

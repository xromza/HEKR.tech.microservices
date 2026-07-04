package ru.xromza.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ru.xromza.user.model.IndividualDetails;

public interface IndividualDetailsRepository extends JpaRepository<IndividualDetails, Long> {
}

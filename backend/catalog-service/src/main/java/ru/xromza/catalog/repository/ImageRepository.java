package ru.xromza.catalog.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ru.xromza.catalog.model.Image;

public interface ImageRepository extends JpaRepository<Image, Long> {

    
}
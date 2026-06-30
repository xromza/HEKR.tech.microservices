package ru.xromza.catalog.repository;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import ru.xromza.catalog.model.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    List<Category> findAllByIdIn(Collection<Long> ids);

    Boolean existsByName(String name);
}

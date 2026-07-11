package ru.xromza.order_worker.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import ru.xromza.order_worker.model.Order;

public interface OrderRepository extends JpaRepository<Order, String> {

    Page<Order> findAll(Pageable pageable);
    @Query("SELECT DISTINCT o FROM Order o LEFT JOIN FETCH o.items WHERE o.id = :id")
    Optional<Order> findByIdWithItems(String id);

    @Query("SELECT DISTINCT o FROM Order o LEFT JOIN FETCH o.items LEFT JOIN FETCH o.history WHERE o.id = :id")
    Optional<Order> findByIdWithItemsAndHistory(String id);

    @Query("SELECT DISTINCT o FROM Order o LEFT JOIN FETCH o.items WHERE o.id = :id")
    Optional<Order> findByIdWithItemsAndUser(String id);

    @Query("SELECT DISTINCT o FROM Order o WHERE o.userId = :userId")
    @EntityGraph(attributePaths = { "items", "history" })
    List<Order> findByUserIdVerbose(Long userId);

    @Query("SELECT DISTINCT o FROM Order o WHERE o.userId = :userId")
    List<Order> findByUserIdWithItems(Long userId);

    @Query("SELECT DISTINCT o FROM Order o WHERE o.userId = :userId")
    List<Order> findByUserIdSimple(Long userId);

    @Query("SELECT DISTINCT o FROM Order o LEFT JOIN FETCH o.items LEFT JOIN FETCH o.history WHERE o.userId = :userId")
    List<Order> findByUserIdWithItemsAndHistory(Long userId);
}

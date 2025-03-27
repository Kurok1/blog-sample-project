package org.example.order.repository;

import org.example.order.entity.Order;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author <a href="mailto:khanc.dev@gmail.com">韩超</a>
 * @since 1.0
 */
@Repository
public interface OrderRepository extends CrudRepository<Order, Long> {

    Optional<Order> findByCode(String code);

    @Modifying
    @Query("update \"order\" set \"status\" = :newStatus where \"id\" = :id")
    int updateStatus(@Param("id") Long id, @Param("newStatus") int status);

}

package org.example.stock.repository;

import org.example.stock.entity.Stock;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

/**
 * @author <a href="mailto:khanc.dev@gmail.com">韩超</a>
 * @since 1.0
 */
@Repository
public interface StockRepository extends CrudRepository<Stock, Long> {

    Stock findByItemName(String itemName);

    @Modifying
    @Query("UPDATE \"stock\" SET \"locked_qty\" = \"locked_qty\" + :qty WHERE \"id\" = :id")
    int lockStock(@Param("id") Long id,@Param("qty") BigDecimal qty);;

    @Modifying
    @Query("UPDATE \"stock\" SET \"locked_qty\" = \"locked_qty\" - :qty, \"total_qty\" = \"total_qty\" - :qty WHERE \"id\" = :id")
    int reduceStock(@Param("id") Long id,@Param("qty") BigDecimal qty);
}

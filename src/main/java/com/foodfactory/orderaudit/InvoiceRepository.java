
package com.foodfactory.orderaudit;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface InvoiceRepository
        extends JpaRepository<Invoice, Long> {

    Optional<Invoice> findBySalesOrderId(Long salesOrderId);

    boolean existsBySalesOrderId(Long salesOrderId);
}

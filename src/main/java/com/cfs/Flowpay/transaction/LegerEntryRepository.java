package com.cfs.Flowpay.transaction;

import com.cfs.Flowpay.refund.Refund;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface LegerEntryRepository extends JpaRepository<LegerEntity,String> {


    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select e from Refund e where e.id=:id")
    Optional<LegerEntity> lockById(@Param("id") String id);

    List<LegerEntity> findByMerchantIdOrderByCreatedAtDesc(String m);


}

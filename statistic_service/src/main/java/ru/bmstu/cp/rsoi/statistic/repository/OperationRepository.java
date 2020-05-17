package ru.bmstu.cp.rsoi.statistic.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.bmstu.cp.rsoi.statistic.domain.Operation;

import java.util.List;


@Repository
@Transactional
public interface OperationRepository extends CrudRepository<Operation, String> {
    public Page<Operation> findAllByEntityNameAndDateBetweenOrParentEntityNameAndDateBetween(
            String entityName, Long start1, Long end1, String parentEntityName, Long start2, Long end2, Pageable pageable);
    public long countAllByParentEntityIdAndDateBetweenAndOperationName(String parentEntityId, Long start, Long end, String operationName);
}

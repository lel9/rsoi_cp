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
    public Page<Operation> findAllByEntityNameOrParentEntityNameAndDateBetween(String entityName, String parentEntityName, Long start, Long end, Pageable pageable);
    public long countAllByParentEntityIdAndDateBetweenAndOperationName(String parentEntityId, Long start, Long end, String operationName);
}

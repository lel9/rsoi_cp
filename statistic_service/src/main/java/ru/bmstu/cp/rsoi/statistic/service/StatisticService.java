package ru.bmstu.cp.rsoi.statistic.service;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.bmstu.cp.rsoi.statistic.domain.Operation;
import ru.bmstu.cp.rsoi.statistic.model.EntityStatisticOut;
import ru.bmstu.cp.rsoi.statistic.model.ListStatisticOut;
import ru.bmstu.cp.rsoi.statistic.repository.OperationRepository;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class StatisticService {

    @Autowired
    private OperationRepository operationRepository;

    @Value( "${service.max-operation-count}" )
    private Integer maxOperationsCount;

    public void saveOperation(Operation operation) {
        operationRepository.save(operation);
    }

    public ListStatisticOut getStatistic(Long dateStart, Long dateEnd, String entityName) {
        Page<Operation> operations = operationRepository.
                findAllByEntityNameAndDateBetweenOrParentEntityNameAndDateBetween(entityName, dateStart, dateEnd,
                        entityName, dateStart, dateEnd,
                        PageRequest.of(0, maxOperationsCount));

        String errMessage = null;
        long totalElements = operations.getTotalElements();
        if (totalElements > maxOperationsCount)
            errMessage = String.format("Превышено максимальное число операций. " +
                            "Найдено операций %d, обработано операций %d. " +
                            "Рекомендуется уменьшить временной диапазон подсчета статистики.", totalElements, maxOperationsCount);

        List<String> groupedOperations = operations
                .stream()
                .filter(operation -> operation.getEntityName().equals(entityName))
                .map(Operation::getEntityId)
                .distinct()
                .collect(Collectors.toList());

        ListStatisticOut statistic = new ListStatisticOut();
        List<EntityStatisticOut> list = new ArrayList<>();
        groupedOperations.forEach(operation -> {
            EntityStatisticOut row = getRow(operation, operations.getContent());
            list.add(row);
            statistic.setTotalCreateCount(statistic.getTotalCreateCount() + row.getCreateCount());
            statistic.setTotalReadCount(statistic.getTotalReadCount() + row.getReadCount());
            statistic.setTotalUpdateCount(statistic.getTotalUpdateCount() + row.getUpdateCount());
            statistic.setTotalDeleteCount(statistic.getTotalDeleteCount() + row.getDeleteCount());

            statistic.setChild1TotalCreateCount(statistic.getChild1TotalCreateCount() + row.getChild1CreateCount());
            statistic.setChild1TotalReadCount(statistic.getChild1TotalReadCount() + row.getChild1ReadCount());
            statistic.setChild1TotalUpdateCount(statistic.getChild1TotalUpdateCount() + row.getChild1UpdateCount());
            statistic.setChild1TotalDeleteCount(statistic.getChild1TotalDeleteCount() + row.getChild1DeleteCount());

        });

        statistic.setErrMessage(errMessage);
        statistic.setEntitiesStatistic(list);
        return statistic;
    }

    private EntityStatisticOut getRow(String entityId, List<Operation> operations) {
        EntityStatisticOut statistic = new EntityStatisticOut();
        statistic.setEntityId(entityId);
        List<Operation> entityOperations = operations
                .stream()
                .filter(operation -> entityId.equals(operation.getEntityId()))
                .collect(Collectors.toList());
        Counts counts = getCounts(entityOperations);
        statistic.setCreateCount(counts.getCreateCount());
        statistic.setReadCount(counts.getReadCount());
        statistic.setUpdateCount(counts.getUpdateCount());
        statistic.setDeleteCount(counts.getDeleteCount());

        Map<String, List<Operation>> childrenMap = operations
                .stream()
                .filter(operation -> entityId.equals(operation.getParentEntityId()))
                .collect(Collectors.groupingBy(Operation::getEntityName));

        Iterator<Map.Entry<String, List<Operation>>> iterator = childrenMap.entrySet().iterator();
        if (iterator.hasNext()) {
            Map.Entry<String, List<Operation>> childEntry = iterator.next();
            Counts childCounts = getCounts(childEntry.getValue());
            statistic.setChild1CreateCount(childCounts.getCreateCount());
            statistic.setReadCount(childCounts.getReadCount());
            statistic.setUpdateCount(childCounts.getUpdateCount());
            statistic.setDeleteCount(childCounts.getDeleteCount());
        }

        return statistic;

    }

    private Counts getCounts(List<Operation> operations) {
        long create = operations.stream().filter(operation -> "C".equals(operation.getOperationName())).count();
        long read = operations.stream().filter(operation -> "R".equals(operation.getOperationName())).count();
        long update = operations.stream().filter(operation -> "U".equals(operation.getOperationName())).count();
        long delete = operations.stream().filter(operation -> "D".equals(operation.getOperationName())).count();
        return new Counts(create, delete, read, update);
    }
}

@Data
@AllArgsConstructor
class Counts {
    private Long createCount;
    private Long deleteCount;
    private Long readCount;
    private Long updateCount;
}

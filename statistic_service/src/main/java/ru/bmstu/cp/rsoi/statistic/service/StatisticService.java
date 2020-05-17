package ru.bmstu.cp.rsoi.statistic.service;

import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.bmstu.cp.rsoi.statistic.domain.Operation;
import ru.bmstu.cp.rsoi.statistic.model.Counts;
import ru.bmstu.cp.rsoi.statistic.model.EntityStatisticOut;
import ru.bmstu.cp.rsoi.statistic.model.ListStatisticOut;
import ru.bmstu.cp.rsoi.statistic.repository.OperationRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

        List<EntityStatisticOut> list = new ArrayList<>();

        Counts totalCounts = new Counts();
        List<Counts> childrenTotalCounts = new ArrayList<>();
        groupedOperations.forEach(operation -> {
            EntityStatisticOut row = getRow(operation, operations.getContent());
            list.add(row);
            Counts counts = row.getCounts();
            totalCounts.setReadCount(totalCounts.getReadCount() + counts.getReadCount());
            totalCounts.setCreateCount(totalCounts.getCreateCount() + counts.getCreateCount());
            totalCounts.setDeleteCount(totalCounts.getDeleteCount() + counts.getDeleteCount());
            totalCounts.setUpdateCount(totalCounts.getUpdateCount() + counts.getUpdateCount());

            List<Counts> childrenStatistic = row.getChildrenStatistic();
            int size = childrenStatistic.size();
            for (int i = 0; i < size; i++) {
                if (i == 0)
                    childrenTotalCounts.add(new Counts());
                Counts childCounts = childrenTotalCounts.get(i);
                childCounts.setUpdateCount(childCounts.getUpdateCount() + childrenStatistic.get(i).getUpdateCount());
                childCounts.setReadCount(childCounts.getReadCount() + childrenStatistic.get(i).getReadCount());
                childCounts.setDeleteCount(childCounts.getDeleteCount() + childrenStatistic.get(i).getDeleteCount());
                childCounts.setReadCount(childCounts.getReadCount() + childrenStatistic.get(i).getReadCount());
            }
        });

        return new ListStatisticOut(totalCounts, childrenTotalCounts, list, errMessage);
    }

    private EntityStatisticOut getRow(String entityId, List<Operation> operations) {
        EntityStatisticOut statistic = new EntityStatisticOut();
        statistic.setEntityId(entityId);
        List<Operation> entityOperations = operations
                .stream()
                .filter(operation -> entityId.equals(operation.getEntityId()))
                .collect(Collectors.toList());
        statistic.setCounts(getCounts(null, entityOperations));

        Map<String, List<Operation>> childrenMap = operations
                .stream()
                .filter(operation -> entityId.equals(operation.getParentEntityId()))
                .collect(Collectors.groupingBy(Operation::getEntityName));

        for (Map.Entry<String, List<Operation>> childEntry : childrenMap.entrySet()) {
            Counts counts = getCounts(childEntry.getKey(), childEntry.getValue());
            statistic.getChildrenStatistic().add(counts);
        }

        return statistic;

    }

    private Counts getCounts(String entityName, List<Operation> operations) {
        long create = operations.stream().filter(operation -> "C".equals(operation.getOperationName())).count();
        long read = operations.stream().filter(operation -> "R".equals(operation.getOperationName())).count();
        long update = operations.stream().filter(operation -> "U".equals(operation.getOperationName())).count();
        long delete = operations.stream().filter(operation -> "D".equals(operation.getOperationName())).count();
        return new Counts(entityName, create, delete, read, update);
    }

}

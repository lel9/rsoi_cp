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
                findAllByEntityNameOrParentEntityNameAndDateBetween(entityName, entityName, dateStart, dateEnd, PageRequest.of(0, maxOperationsCount));

        String errMessage = null;
        long totalElements = operations.getTotalElements();
        if (totalElements > maxOperationsCount)
            errMessage = String.format("Превышено максимальное число операций. " +
                            "Найдено операций %d, обработано операций %d. " +
                            "Рекомендуется уменьшить временной диапазон подсчета статистики.", totalElements, maxOperationsCount);

        Map<String, List<Operation>> groupedOperations = operations
                .stream()
                .collect(Collectors.groupingBy(Operation::getEntityId));

        List<EntityStatisticOut> list = new ArrayList<>();

        Counts totalCounts = new Counts();
        List<Counts> childrenTotalCounts = new ArrayList<>();
        int iter = 0;
        for (Map.Entry<String, List<Operation>> childEntry : groupedOperations.entrySet()) {
            EntityStatisticOut row = getRow(childEntry.getKey(), childEntry.getValue());
            list.add(row);
            Counts counts = row.getCounts();
            totalCounts.setReadCount(totalCounts.getReadCount() + counts.getReadCount());
            totalCounts.setCreateCount(totalCounts.getCreateCount() + counts.getCreateCount());
            totalCounts.setDeleteCount(totalCounts.getDeleteCount() + counts.getDeleteCount());
            totalCounts.setUpdateCount(totalCounts.getUpdateCount() + counts.getUpdateCount());

            List<Counts> childrenStatistic = row.getChildrenStatistic();
            int size = childrenStatistic.size();
            for (int i = 0; i < size; i++) {
                if (iter == 0)
                    childrenTotalCounts.add(new Counts());
                Counts childCounts = childrenTotalCounts.get(i);
                childCounts.setUpdateCount(childCounts.getUpdateCount() + childrenStatistic.get(i).getUpdateCount());
                childCounts.setReadCount(childCounts.getReadCount() + childrenStatistic.get(i).getReadCount());
                childCounts.setDeleteCount(childCounts.getDeleteCount() + childrenStatistic.get(i).getDeleteCount());
                childCounts.setReadCount(childCounts.getReadCount() + childrenStatistic.get(i).getReadCount());
            }
            iter++;
        }

        return new ListStatisticOut(totalCounts, childrenTotalCounts, list, errMessage);
    }

    private EntityStatisticOut getRow(String entityId, List<Operation> operations) {
        EntityStatisticOut statistic = new EntityStatisticOut();
        statistic.setEntityId(entityId);
        Stream<Operation> entityOperations = operations
                .stream()
                .filter(operation -> entityId.equals(operation.getEntityId()));
        statistic.setCounts(getCounts(null, entityOperations));

        Map<String, List<Operation>> childrenMap = operations
                .stream()
                .filter(operation -> entityId.equals(operation.getParentEntityId()))
                .collect(Collectors.groupingBy(Operation::getEntityName));

        for (Map.Entry<String, List<Operation>> childEntry : childrenMap.entrySet()) {
            Counts counts = getCounts(childEntry.getKey(), childEntry.getValue().stream());
            statistic.getChildrenStatistic().add(counts);
        }

        return statistic;

    }

    private Counts getCounts(String entityName, Stream<Operation> stream) {
        long create = stream.filter(operation -> "C".equals(operation.getOperationName())).count();
        long read = stream.filter(operation -> "R".equals(operation.getOperationName())).count();
        long update = stream.filter(operation -> "U".equals(operation.getOperationName())).count();
        long delete = stream.filter(operation -> "D".equals(operation.getOperationName())).count();
        return new Counts(entityName, create, delete, read, update);
    }

}

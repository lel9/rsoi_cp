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
import java.util.List;
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
        groupedOperations.forEach(entityId -> {
            EntityStatisticOut row = getRow(entityId, operations.getContent());
            list.add(row);
            statistic.setTotalCreateCount(statistic.getTotalCreateCount() + row.getCreateCount());
            statistic.setTotalReadCount(statistic.getTotalReadCount() + row.getReadCount());
            statistic.setTotalUpdateCount(statistic.getTotalUpdateCount() + row.getUpdateCount());
            statistic.setTotalDeleteCount(statistic.getTotalDeleteCount() + row.getDeleteCount());

            statistic.setChildTotalCreateCount(statistic.getChildTotalCreateCount() + row.getChildCreateCount());
            //statistic.setChildTotalReadCount(statistic.getChildTotalReadCount() + row.getChildReadCount());
            statistic.setChildTotalUpdateCount(statistic.getChildTotalUpdateCount() + row.getChildUpdateCount());
            statistic.setChildTotalDeleteCount(statistic.getChildTotalDeleteCount() + row.getChildDeleteCount());

        });

        statistic.setErrMessage(errMessage);
        statistic.setEntitiesStatistic(list);
        return statistic;
    }

    private EntityStatisticOut getRow(String entityId, List<Operation> operations) {
        EntityStatisticOut statistic = new EntityStatisticOut();
        statistic.setId(entityId);

        List<Operation> entityOperations = operations
                .stream()
                .filter(operation -> entityId.equals(operation.getEntityId()))
                .collect(Collectors.toList());

        Counts counts = getCounts(entityOperations);
        statistic.setCreateCount(counts.getCreateCount());
        statistic.setReadCount(counts.getReadCount());
        statistic.setUpdateCount(counts.getUpdateCount());
        statistic.setDeleteCount(counts.getDeleteCount());

        List<Operation> childOperations = operations
                .stream()
                .filter(operation -> entityId.equals(operation.getParentEntityId()))
                .collect(Collectors.toList());

        Counts childCounts = getCounts(childOperations);
        statistic.setChildCreateCount(childCounts.getCreateCount());
        //statistic.setChildReadCount(childCounts.getReadCount());
        statistic.setChildUpdateCount(childCounts.getUpdateCount());
        statistic.setChildDeleteCount(childCounts.getDeleteCount());

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

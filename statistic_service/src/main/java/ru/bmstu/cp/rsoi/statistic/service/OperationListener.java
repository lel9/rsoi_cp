package ru.bmstu.cp.rsoi.statistic.service;

import org.modelmapper.ModelMapper;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import ru.bmstu.cp.rsoi.statistic.model.OperationIn;

public class OperationListener {

    @Autowired
    private StatisticService service;

    @Autowired
    private ModelMapper modelMapper;

    @RabbitListener(queues="operationServiceQueue")
    public void receive(OperationIn message) {
        service.saveOperation(modelMapper.map(message, ru.bmstu.cp.rsoi.statistic.domain.Operation.class));
    }

}

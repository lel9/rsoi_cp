package ru.bmstu.cp.rsoi.statistic.service;

import org.modelmapper.ModelMapper;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import ru.bmstu.cp.rsoi.statistic.model.OperationIn;

import java.util.logging.Level;
import java.util.logging.Logger;

public class OperationListener {

    @Autowired
    private StatisticService service;

    @Autowired
    private ModelMapper modelMapper;

    private Logger log = Logger.getLogger(OperationListener.class.getName());

    @RabbitListener(queues="operation-queue")
    public void receive(OperationIn message) {
        service.saveOperation(modelMapper.map(message, ru.bmstu.cp.rsoi.statistic.domain.Operation.class));
        log.log(Level.INFO, "Operation was received: " + message.toString());
    }

}

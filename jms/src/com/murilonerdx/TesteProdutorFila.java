package com.murilonerdx;

import javax.jms.*;
import javax.naming.InitialContext;
import java.util.Scanner;

public class TesteProdutorFila {

    public static void main(String[] args) throws Exception {

        //        Properties properties = new Properties();
        //        properties.setProperty("java.naming.factory.initial", "org.apache.activemq.jndi.ActiveMQInitialContextFactory");
        //        properties.setProperty("java.naming.provider.url", "tcp://192.168.0.94:61616");
        //        properties.setProperty("queue.financeiro", "fila.financeiro");

        //        InitialContext context = new InitialContext(properties);

        InitialContext context = new InitialContext();


        ConnectionFactory factory = (ConnectionFactory) context.lookupLink("ConnectionFactory");
        Connection connection = factory.createConnection();
        connection.start();

        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Destination fila = (Destination) context.lookup("financeiro");

        MessageProducer producer = session.createProducer(fila);

        Message message = session.createTextMessage("<pedido><id><123></id></pedido>");
        producer.send(message);

        new Scanner(System.in).nextLine();

        session.close();
        connection.close();
        context.close();
    }
}

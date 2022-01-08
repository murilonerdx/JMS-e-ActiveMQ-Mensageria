package com.murilonerdx;

import javax.jms.*;
import javax.naming.InitialContext;
import java.util.Enumeration;
import java.util.Scanner;

public class TesteConsumidorFilaDLQ {

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
        Destination fila = (Destination) context.lookup("DLQ");

        QueueBrowser browser = session.createBrowser((Queue) fila);

        Enumeration msgs = browser.getEnumeration();
        while (msgs.hasMoreElements()) {
            TextMessage msg = (TextMessage) msgs.nextElement();
            System.out.println("Message: " + msg.getText());
        }

        MessageConsumer consumer = session.createConsumer(fila);
//        Message message = consumer.receive();

        consumer.setMessageListener(message -> {
            TextMessage textMessage = (TextMessage) message;

            try {
                System.out.println("Recebendo msg: " + textMessage.getText());
            } catch (JMSException e) {
                e.printStackTrace();
            }

        });

        new Scanner(System.in).nextLine();

        session.close();
        connection.close();
        context.close();
    }
}

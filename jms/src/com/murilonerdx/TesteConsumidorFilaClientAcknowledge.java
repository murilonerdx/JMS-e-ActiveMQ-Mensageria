package com.murilonerdx;

import javax.jms.*;
import javax.naming.InitialContext;
import java.util.Enumeration;
import java.util.Scanner;

public class TesteConsumidorFilaClientAcknowledge {

    public static void main(String[] args) throws Exception {

        InitialContext context = new InitialContext();


        ConnectionFactory factory = (ConnectionFactory) context.lookupLink("ConnectionFactory");
        Connection connection = factory.createConnection();
        connection.start();



        Session session = connection.createSession(false, Session.CLIENT_ACKNOWLEDGE);
        Destination fila = (Destination) context.lookup("financeiro");

        /*
            Dependendo da nossa aplicação,
            podemos precisar apenas checar (monitoramento)
            as mensagens que chegaram para uma determinada fila sem consumi-la.
            Ou seja, apenas queremos ver sem tirá-las da fila.
            Para isso podemos usar um componente do JMS chamado QueueBrowser,
            para navegar sobre as mensagens sem consumi-las.
         */

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
                textMessage.acknowledge();
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

package com.murilonerdx;

import javax.jms.*;
import javax.naming.InitialContext;
import java.util.Enumeration;
import java.util.Scanner;

public class TesteConsumidorTopico {

    public static void main(String[] args) throws Exception {
        //Topico só recebe caso ele estiver online, caso contrario não, então precisa de uma configuração adicional

        InitialContext context = new InitialContext();


        ConnectionFactory factory = (ConnectionFactory) context.lookupLink("ConnectionFactory");
        Connection connection = factory.createConnection();
        connection.setClientID("estoque");
        connection.start();


        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Topic topico = (Topic) context.lookup("loja");

        MessageConsumer consumer = session.createDurableSubscriber(topico, "assinatura");

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

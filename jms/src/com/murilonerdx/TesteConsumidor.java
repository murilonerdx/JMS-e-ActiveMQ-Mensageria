package com.murilonerdx;

import javax.jms.*;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.Scanner;

public class TesteConsumidor {

    public static void main(String[] args) throws Exception {
        InitialContext context = new InitialContext();

        ConnectionFactory factory = (ConnectionFactory) context.lookupLink("ConnectionFactory");
        Connection connection = factory.createConnection();
        connection.start();

        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Destination fila = (Destination) context.lookup("financeiro");

        MessageConsumer consumer = session.createConsumer(fila);
        Message receive = consumer.receive();

        System.out.println("Recebendo msg: " + receive);

        new Scanner(System.in).nextLine();

        session.close();
        connection.close();
        context.close();
    }
}

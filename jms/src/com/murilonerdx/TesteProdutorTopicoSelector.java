package com.murilonerdx;

import javax.jms.*;
import javax.naming.InitialContext;

public class TesteProdutorTopicoSelector {

    public static void main(String[] args) throws Exception {

        InitialContext context = new InitialContext();


        ConnectionFactory factory = (ConnectionFactory) context.lookupLink("ConnectionFactory");
        Connection connection = factory.createConnection();
        connection.start();

        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Destination topico = (Destination) context.lookup("loja");

        MessageProducer producer = session.createProducer(topico);


        Message message = session.createTextMessage("<pedido><id><123></id><ebook>true</ebook></pedido>");
        message.setBooleanProperty("ebook", true);
        producer.send(message);

        session.close();
        connection.close();
        context.close();
    }
}

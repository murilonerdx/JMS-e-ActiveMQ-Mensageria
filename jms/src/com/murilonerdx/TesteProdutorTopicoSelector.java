package com.murilonerdx;

import com.murilonerdx.modelo.Pedido;
import com.murilonerdx.modelo.PedidoFactory;

import javax.jms.*;
import javax.naming.InitialContext;
import javax.xml.bind.JAXB;
import java.io.StringWriter;

public class TesteProdutorTopicoSelector {

    public static void main(String[] args) throws Exception {

        InitialContext context = new InitialContext();


        ConnectionFactory factory = (ConnectionFactory) context.lookupLink("ConnectionFactory");
        Connection connection = factory.createConnection();
        connection.start();

        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

        Destination topico = (Destination) context.lookup("loja");

        MessageProducer producer = session.createProducer(topico);


        Pedido pedido = new PedidoFactory().geraPedidoComValores();

//        StringWriter stringWriter = new StringWriter();
//        JAXB.marshal(pedido, stringWriter);
//        String xml = stringWriter.toString();
//        message.setBooleanProperty("ebook", true);

//        Message message = session.createTextMessage(xml);

        Message message = session.createObjectMessage(pedido);

        producer.send(message);

        session.close();
        connection.close();
        context.close();
    }
}

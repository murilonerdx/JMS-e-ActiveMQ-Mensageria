package com.murilonerdx;

import com.murilonerdx.modelo.Pedido;

import javax.jms.*;
import javax.naming.InitialContext;
import java.io.Serializable;
import java.util.Scanner;

public class TesteConsumidorTopicoSelector {

    public static void main(String[] args) throws Exception {
        System.setProperty("org.apache.activemq.SERIALIZABLE_PACKAGES","*");

        //Topico só recebe caso ele estiver online, caso contrario não, então precisa de uma configuração adicional

        InitialContext context = new InitialContext();


        ConnectionFactory factory = (ConnectionFactory) context.lookupLink("ConnectionFactory");

        Connection connection = factory.createConnection();
        connection.setClientID("estoque");
        connection.start();


        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Topic topico = (Topic) context.lookup("loja");

        /*
            Tipos de assinatura e query para mensagems como ebook = false ele só vai aceitar caso se o produtor ter uma propriedade ebook=false
            OR se o ebook for nullo ou false
         */
        MessageConsumer consumer = session.createDurableSubscriber(topico, "assinatura-selector","ebook is null OR ebook=false",false);

        consumer.setMessageListener(message -> {
//            TextMessage textMessage = (TextMessage) message;
            ObjectMessage objectMessage = (ObjectMessage) message;


            try {
                Pedido object = (Pedido) objectMessage.getObject();
                System.out.println("Recebendo msg: " + object.getCodigo());
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

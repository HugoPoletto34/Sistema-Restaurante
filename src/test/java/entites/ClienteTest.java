package entites;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import dao.ClienteDAO;
import entities.Cliente;
import entities.Data;
import entities.Pedido;
import entities.Pizza;
import entities.Sanduiche;

public class ClienteTest {
    
    @Test
    public void testAddClient() throws IOException{

        Cliente c = new Cliente("Pedro");
        ClienteDAO.add(c);
        assertEquals("Pedro", ClienteDAO.getReferenceById("Pedro").getNome());
    
    }

    @Test
    public void testRelatorioPedidos() throws IOException{

        Cliente c = new Cliente("Pedro");

        c.addPedido(new Pedido(new Data(), new Pizza()));
        c.addPedido(new Pedido(new Data(), new Sanduiche()));

        ClienteDAO.add(c);

        assertFalse(ClienteDAO.getReferenceById("Pedro").relatorioPedidos().isEmpty(), "O relatório do pedido está vazio!");
    
    }

    @Test
    public void testVlrMaiorPedido() throws IOException{

        Cliente c = new Cliente("Pedro");

        c.addPedido(new Pedido(new Data(), new Pizza()));
        c.addPedido(new Pedido(new Data(), new Sanduiche()));

        ClienteDAO.add(c);

        assertEquals(25.00, ClienteDAO.getReferenceById("Pedro").pedidoMaiorValor().getValorPago());
    
    }

    @Test
    public void testVrlTotalPedido() throws IOException{

        Cliente c = new Cliente("Pedro");

        c.addPedido(new Pedido(new Data(), new Pizza()));
        c.addPedido(new Pedido(new Data(), new Sanduiche()));

        ClienteDAO.add(c);

        assertEquals(38.00, ClienteDAO.getReferenceById("Pedro").totalEmPedidos());
    
    }

    @Test
    public void testPedidoRecente() throws IOException{

        Cliente c = new Cliente("Pedro");

        Pedido pedido1 = new Pedido(new Data(), new Pizza());
        Pedido pedido2 = new Pedido(new Data(), new Sanduiche());

        c.addPedido(pedido1);
        c.addPedido(pedido2);

        ClienteDAO.add(c);

        assertEquals(pedido2.getId(), ClienteDAO.getReferenceById("Pedro").pedidoRecente().getId());
    
    }

    @Test
    public void testAddItemPedido() throws IOException{

        Cliente c = new Cliente("Pedro");

        Pedido pedido1 = new Pedido(new Data(), new Pizza());
        pedido1.addItem(new Pizza());

        Pedido pedido2 = new Pedido(new Data(), new Sanduiche());

        c.addPedido(pedido1);
        c.addPedido(pedido2);

        ClienteDAO.add(c);

        assertEquals(3, ClienteDAO.getReferenceById("Pedro")
        .getPedidos()
        .stream()
        .mapToInt(Pedido::getQuantItens)
        .sum());
    
    }


}

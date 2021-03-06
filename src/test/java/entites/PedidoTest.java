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
import entities.Tropeiro;

public class PedidoTest {
    
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

    
    @Test
    public void testAddCancelaPedido() throws IOException{

        Cliente c = new Cliente("Pedro");

        Pedido pedido1 = new Pedido(new Data(), new Pizza());
        pedido1.addItem(new Tropeiro());
        pedido1.addItem(new Sanduiche());
        pedido1.cancelaItem(new Pizza());

        c.addPedido(pedido1);
    
        ClienteDAO.add(c);

        assertEquals(2, ClienteDAO.getReferenceById("Pedro")
        .getPedidos()
        .stream()
        .mapToInt(Pedido::getQuantItens)
        .sum());
    
    }


    @Test
    public void testTotalPorPedido() throws IOException{

        Cliente c = new Cliente("Pedro");

        Pedido pedido1 = new Pedido(new Data(), new Pizza());
        pedido1.addItem(new Tropeiro());

        c.addPedido(pedido1);
    
        ClienteDAO.add(c);

        assertEquals(35.00, ClienteDAO.getReferenceById("Pedro").getPedidos().getFirst().valorTotal());
    
        assertFalse(ClienteDAO.getReferenceById("Pedro").relatorioPedidos().isEmpty(), "O relatório do pedido está vazio!");
    
    }

    @Test
    public void testRelatorioPorPedido() throws IOException{

        Cliente c = new Cliente("Pedro");

        Pedido pedido1 = new Pedido(new Data(), new Pizza());
        pedido1.addItem(new Tropeiro());

        c.addPedido(pedido1);
    
        ClienteDAO.add(c);

        assertFalse(ClienteDAO.getReferenceById("Pedro").getPedidos().getFirst().relatorio().isEmpty(), "O relatório do pedido está vazio!");
    
    }
    
}

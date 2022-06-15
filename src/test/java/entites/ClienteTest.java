package entites;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import dao.ClienteDAO;
import entities.Cliente;

public class ClienteTest {
    
    @Test
    public void testAddClient() throws IOException{

        Cliente c = new Cliente("Pedro");
        ClienteDAO.add(c);
        assertEquals("Pedro", ClienteDAO.getReferenceById("Pedro").getNome());
    
    }


}

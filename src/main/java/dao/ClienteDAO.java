package dao;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.TreeSet;

import entities.Cliente;

public class ClienteDAO {
    
    private static final String DADOS = "./fake-database/clientes.txt";
  
    public TreeSet<Cliente> loadClients() throws IOException {
        Scanner leitor = new Scanner(new File(DADOS), "UTF-8");
        TreeSet<Cliente> clientes = new TreeSet<>();
        while (leitor.hasNextLine()) {
            String nome = leitor.nextLine();
            Cliente novo = new Cliente(nome);
            clientes.add(novo);
        }

        leitor.close();
        return clientes;
    }

    public static Cliente getReferenceById(String nome) throws IOException{
       
        List<Cliente> clientes = new ArrayList<>();
        
        try {
            FileInputStream dados = new FileInputStream(DADOS);
            ObjectInputStream obj = new ObjectInputStream(dados);
            while (dados.available() != 0) {
                Cliente novo = (Cliente) obj.readObject();
                clientes.add(novo);
            }
            obj.close();
        }catch(FileNotFoundException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

       return clientes.stream().filter(c -> c.getNome().equals(nome)).findFirst().orElse(null);
    }

    public static void add(Cliente c) throws IOException {
        ObjectOutputStream obj = new ObjectOutputStream(new FileOutputStream(DADOS));
      
        obj.writeObject(c); 
        obj.close();

    }



}


/** 
 * MIT License
 *
 * Copyright(c) 2022 João Caram <caram@pucminas.br>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
import entities.*;
import enums.Ingrediente;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.LocalDate;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;


/**
 * App para gerar um arquivo binário de clientes e seus pedidos.
 */
public class AppGerador {
    static Random aleat = new Random(42);
    static String nomeArq = "clientes.txt";
    static LocalDate today = LocalDate.now();
    static Data hoje = new Data(today.getDayOfMonth(), today.getMonthValue());

    /**
     * Gravação serializada do conjunto de clientes
     * 
     * @param clientes Conjunto de clientes a salvar
     * @throws IOException Em caso de erro na escrita ou abertura do arquivo
     *                     (propagação de exceção)
     */
    public static void gravarDados(Set<Cliente> clientes) throws IOException {
        ObjectOutputStream obj = new ObjectOutputStream(new FileOutputStream("./fake-database/tudo.bin"));
        for (Cliente cliente : clientes) {
            obj.writeObject(cliente);
        }
        obj.close();
    }

    /**
     * Carrega dados do arquivo de clientes serialiado. Tratamento de diversas
     * exceções
     * 
     * @return Um TreeSet com os clientes e seus pedidos
     */

    public static Set<Cliente> carregarDados() throws IOException, ClassNotFoundException {
        FileInputStream dados = new FileInputStream("tudo.bin");
        ObjectInputStream obj = new ObjectInputStream(dados);
        TreeSet<Cliente> todos = new TreeSet<>();

        while (dados.available() != 0) {
            Cliente novo = (Cliente) obj.readObject();
            todos.add(novo);
        }
        obj.close();
        return todos;
    }

    /**
     * Carrega o arquivo de clientes em um tree map, criando ainda uma lista de
     * pedidos aleatórios para cada
     * 
     * @param nomeArq Arquivo com os clientes
     * @return HashMap com o nome como chave e o cliente como valor
     * @throws IOException Arquivo não encontrado
     */
    private static TreeSet<Cliente> carregarClientes(String nomeArq) throws IOException {
        Scanner leitor = new Scanner(new File(nomeArq), "UTF-8");
        TreeSet<Cliente> clientes = new TreeSet<>();
        while (leitor.hasNextLine()) {
            String nome = leitor.nextLine();
            Cliente novo = new Cliente(nome);

            int quantosPed = aleat.nextInt(10) + 1;
            for (int i = 0; i < quantosPed; i++) {
                int quantasComidas = aleat.nextInt(6) + 1;
                novo.addPedido(criarPedido(quantasComidas));
            }

            clientes.add(novo);
        }

        leitor.close();
        return clientes;
    }

    /**
     * Cria uma refeição aleatoriamente (pizza ou sanduíche) com ingredientes
     * aleatórios
     * 
     * @return entities.Comida com ingredientes
     */
    public static Comida criarRefeicao() {
        Ingrediente[] ingr = Ingrediente.values();

        Comida nova = null;
        int tipo = aleat.nextInt(3);
        int quantos = aleat.nextInt(3);

        switch (tipo) {
            case 0:
                nova = new Pizza();
                break;
            case 1:
                nova = new Pizza(true);
                break;
            case 2:
                nova = new Sanduiche();
        }
        for (int i = 0; i < quantos; i++) {
            int pos = aleat.nextInt(3);
            nova.addIngrediente(ingr[pos]);
        }
        return nova;
    }

    /**
     * Cria um pedido com 'quantasComidas' aleatórias nos itens
     * 
     * @param quantasComidas Quantidade de itens do pedido
     * @return entities.Pedido com diversas comidas aleatórias
     */
    public static Pedido criarPedido(int quantasComidas) {
        Comida nova = criarRefeicao();
        Data qualquerData = new Data(aleat.nextInt(28) + 1, aleat.nextInt(12) + 1);
        Pedido novo = new Pedido(qualquerData, nova);
        for (int i = 1; i < quantasComidas; i++) {
            novo.addItem(criarRefeicao());
        }
        return novo;
    }

    /**
     * Utilizade para 'limpar' o console (terminal VT-100)
     */
    public static void limparTela() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    public static void main(String[] args) throws Exception {
        Scanner teclado = new Scanner(System.in);
        TreeSet<Cliente> todosOsClientes = (TreeSet<Cliente>) carregarClientes("./fake-database/clientes.txt");
        gravarDados(todosOsClientes);
        System.out.println("FIM");
        teclado.close();
    }

}

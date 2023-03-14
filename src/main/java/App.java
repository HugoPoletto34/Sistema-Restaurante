
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

import entities.Cliente;
import entities.Comida;
import entities.Data;
import entities.Pedido;
import enums.Ingrediente;
import factories.*;
import interfaces.IFabrica;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidParameterException;
import java.time.LocalDate;
import java.util.InputMismatchException;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;



public class App {

    // #region atributos
    // para evitar digitar a data a cada vez que iniciar o sistema
    static LocalDate x = LocalDate.now();
    static Data hoje = new Data(x.getDayOfMonth(), x.getMonthValue());

    static String arqDados = "./fake-database/tudo.bin"; // arquivo serializado com os dados
    static Pedido pedidoAtual = null; // pedido ativo no restaurante
    static Comida itemAtual = null; // última comida criada
    static Cliente clienteAtual = null; // cliente ativo no pedido;
    static Scanner teclado;
    static TreeSet<Cliente> conjuntoClientes; // ED com todos os clientes, carregados do arquivo

    static FabricaPizza pizza = new FabricaPizza();
    static FabricaBaconBurger baconBurger = new FabricaBaconBurger();
    static FabricaSanduiche sanduiche = new FabricaSanduiche();
    static FabricaTropeiro tropeiro = new FabricaTropeiro();

    static ColecaoFabrica todasAsFabricas;
    // #endregion

    // #region Utilidades
    /**
     * "Limpa" a tela (códigos de terminal VT-100)
     */
    public static void limparTela() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    /**
     * Gravação serializada do conjunto de clientes
     * 
     * @param clientes Conjunto de clientes a salvar
     * @throws IOException Em caso de erro na escrita ou abertura do arquivo
     *                     (propagação de exceção)
     */
    public static void gravarDados() throws IOException {
        ObjectOutputStream obj = new ObjectOutputStream(new FileOutputStream(arqDados));
        for (Cliente cliente : conjuntoClientes) {
            obj.writeObject(cliente);
        }
        obj.close();
    }


    
    /**
     * Gravação serializada do conjunto de clientes
     * 
     * @param clientes Conjunto de clientes a salvar
     * @throws IOException Em caso de erro na escrita ou abertura do arquivo
     *                     (propagação de exceção)
     */
    public static void gravarDados(Cliente c) throws IOException {
        ObjectOutputStream obj = new ObjectOutputStream(new FileOutputStream(arqDados));
            obj.writeObject(c);
        obj.close();
    }


    /**
     * Carrega dados do arquivo de clientes serialiado. Tratamento de diversas
     * exceções
     * 
     * @param nomeArq nome do arquivo a ser carregado
     * @return Um TreeSet com os clientes e seus pedidos
     */
    public static Set<Cliente> carregarDados(Scanner teclado) {
        FileInputStream dados;
        TreeSet<Cliente> todos = new TreeSet<>();

        try {
            dados = new FileInputStream(arqDados);
            ObjectInputStream obj = new ObjectInputStream(dados);
            while (dados.available() != 0) {
                Cliente novo = (Cliente) obj.readObject();
                todos.add(novo);
            }
            obj.close();
        } catch (FileNotFoundException e) {
            System.out.println("Arquivo não encontrado.");
            System.out.println("Clientes e pedidos em branco.");
            System.out.print("Nome do arquivo de dados: ");
            arqDados = teclado.nextLine();
            pausa(teclado);
        } catch (IOException ex) {
            System.out.println("Problema no uso do arquivo.");
            System.out.println("Favor reiniciar o sistema.");
            pausa(teclado);
        } catch (ClassNotFoundException cex) {
            System.out.println("Classe não encontrada: avise ao suporte.");
            System.out.println("Clientes e pedidos em branco.");
            todos = new TreeSet<>();
            pausa(teclado);
        }

        return todos;
    }

    /**
     * Cabeçalho do sistema impresso em vários lugares.
     */
    public static void cabecalho() {
        limparTela();
        System.out.println("XULAMBS BURGER & PIZZA");
        System.out.println("==========================");
    }

    /**
     * Rodapé do sistema impresso em vários lugares
     */
    public static void rodape() {
        System.out.println("0 - Finalizar");
        System.out.print("Digite sua opção: ");
    }

    /**
     * Submenu genérico: lê opções de um arquivo e monta o submenu
     * 
     * @param arquivo Arquivo texto com as opções
     * @param opcao   Valor da opção no menu base
     * @return Valor composto: opção do menu base + opção do submenu
     */
    public static int subMenu(String arquivo, int opcao) {
        int opcaoMenu = 0;
        cabecalho();
        try {
            Scanner arqMenu = new Scanner(new File(arquivo));
            int op = 1;
            while (arqMenu.hasNextLine()) {
                System.out.println(op + " - " + arqMenu.nextLine());
                op++;
            }
            rodape();
            opcaoMenu = teclado.nextInt();
            teclado.nextLine();
        } catch (FileNotFoundException fe) {
            System.out.println("Arquivo " + arquivo + " não encontrado. Entre em contato com o suporte");

        } catch (InputMismatchException ie) {
            return -1;
        }
        return opcao + opcaoMenu;
    }

    /**
     * Menu principal para o restaurante. Chama os submenus desejados.
     * 
     * @param teclado Scanner de leitura
     * @return Opção do usuário (int)
     */
    public static int menuPrincipal(int subnivel) {
        cabecalho();
        int opcao;
        System.out.println("1 - Pedidos");
        System.out.println("2 - Clientes");
        System.out.println("3 - Relatórios");
        System.out.println("0 - Finalizar");
        System.out.print("Digite sua opção: ");
        if ((subnivel % 10) == 0) {
            opcao = teclado.nextInt();
            teclado.nextLine();
        } else
            opcao = subnivel / 10;
        try {
            switch (opcao) {
                case 1:
                    return subMenu("./fake-database/menuPedido.txt", 10);
                case 2:
                    return subMenu("./fake-database/menuCliente.txt", 20);
                case 3:
                    return subMenu("./fake-database/menuRelatorios.txt", 30);
                default:
                    return 0;
            }
        } catch (InputMismatchException ie) {
            return -1;
        }
    }

    /**
     * Pausa para leitura de mensagens em console
     * 
     * @param teclado Scanner de leitura
     */
    static void pausa(Scanner teclado) {
        System.out.println("Enter para continuar.");
        teclado.nextLine();
    }
    // #endregion

    // #region Métodos de negócio

    /**
     * Grava uma lista em um arquivo. Direcionado à lista de pedidos
     * 
     * @param arquivo Nome do arquivo a salvar
     * @param pedidos Lista com os pedidos a salvar
     * @throws IOException Caso o arquivo não seja encontrado.
     */
    private static void relatorioVendas(String arquivo, List<Pedido> pedidos) throws IOException {
        FileWriter escritor = new FileWriter(new File(arquivo), StandardCharsets.UTF_8);
        escritor.append("XULAMBS BUERGER & PIZZA\n");
        escritor.append("Relatório em " + hoje.dataFormatada() + "\n\n");
        escritor.append(pedidos.toString());
        escritor.close();
    }

    /**
     * Cria um relatório, em String, com informações resumidas: nome, quantidade de
     * pedidos e valor total de cada cliente
     * 
     * @return String com o relatório
     */
    public static String resumoClientes() {
        StringBuilder relat = new StringBuilder("Resumo dos clientes: \n");
        for (Cliente cliente : conjuntoClientes) {
            int quantPedidos = cliente.getPedidos().size();
            relat.append(cliente + " com " + quantPedidos + " pedidos e R$"
                    + String.format("%.2f", cliente.totalEmPedidos()) + "\n");
        }
        return relat.toString();
    }

    /**
     * Cria uma comida, utilizando padrão Factory Method. Fábricas carregadas pelo
     * método configurarMenu(). Cardápio lido a partir de arquivo texto.
     * 
     * @return Uma comida criada de acordo com a opção escolhida.
     * @throws InvalidParameterException
     */
    public static Comida criarComida() {
        Comida novaComida = null;
        ;
        try {
            Scanner leitor = new Scanner(new File("./fake-database/cardapio.txt"));
            String[] produtos = leitor.nextLine().split(",");
            int opcao;
            limparTela();
            System.out.println("XULAMBS BURGER & PIZZA");
            System.out.println("==========================");
            System.out.println("INCLUIR COMIDA");
            for (int i = 1; i <= produtos.length; i++) {
                System.out.println(i + " - " + produtos[i - 1]);
            }

            System.out.print("Escolha: ");
            opcao = Integer.parseInt(teclado.nextLine());
            String tipo = produtos[opcao - 1];

            novaComida = todasAsFabricas.criar(tipo);
            for (Ingrediente adicional : adicionarIngredientes()) {
                if (adicional != null)
                    novaComida.addIngrediente(adicional);
            }
        } catch (InvalidParameterException ipe) {
            System.out.println("Tipo de comida inválido.");
            pausa(teclado);
        } catch (IOException ex) {
            System.out.println("TProblema na leitura do arquivo de cardápio. Favor conferir e reiniciar.");
            pausa(teclado);
        }
        return novaComida;
    }

    /**
     * Carrega todas as fábricas de comidas a partir de arquivo texto. Utiliza
     * reflexão para localizar criar as fábricas configuradas
     */
    static void configurarMenu() {
        todasAsFabricas = new ColecaoFabrica();
        Scanner leitor = null;
        try {
            leitor = new Scanner(new File("./fake-database/fabricas.txt"));
            while (leitor.hasNextLine()) {
                String[] dadosFabrica = leitor.nextLine().split(";");
                todasAsFabricas.acrescentarFabrica(dadosFabrica[0],
                        (IFabrica) Class.forName(dadosFabrica[1]).getConstructor().newInstance());
            }

        } catch (FileNotFoundException e) {
            System.out.println("Arquivo de configuração de fábricas não encontrado. Favor verificar e reiniciar.");
            pausa(teclado);
            return;

        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException
                | InvocationTargetException | NoSuchMethodException | SecurityException
                | ClassNotFoundException e) {
                System.out.println("Problema na criação de fábricas e comida. Favor contactar o suporte.");
                pausa(teclado);
        }
    }

    /**
     * Menu para ingredientes de uma comida
     * 
     * @param teclado Scanner para leitura
     * @return Vetor de ingredientes a ser enviado para a comida
     */
    public static List<Ingrediente> adicionarIngredientes() {
        limparTela();

        int opcao = 0;
        int quantidade = 0;
        List<Ingrediente> adicionais = new LinkedList<>();

        do {
            limparTela();
            System.out.println("XULAMBS BURGER & PIZZA");
            System.out.println("==========================");
            System.out.println("Deseja adicionais?");
            System.out.println("1 - Bacon");
            System.out.println("2 - Palmito");
            System.out.println("3 - Queijo cheddar");
            System.out.println("4 - Molho bolonhesa");
            System.out.println("0 - Não desejo. Sair.");
            System.out.print("Escolha: ");
            opcao = Integer.parseInt(teclado.nextLine());
            if (opcao != 0) {
                System.out.print("Quantas unidades? ");
                quantidade = Integer.parseInt(teclado.nextLine());
                Ingrediente novo = null;
                switch (opcao) {
                    case 1:
                        novo = Ingrediente.BACON;
                        break;
                    case 2:
                        novo = Ingrediente.PALMITO;
                        break;
                    case 3:
                        novo = Ingrediente.CHEDDAR;
                        break;
                    case 4:
                        novo = Ingrediente.BOLONHESA;
                        break;

                    default:
                        break;
                }
                for (int i = 0; i < quantidade; i++) {
                    adicionais.add(novo);
                }
            }
        } while (opcao != 0);

        return adicionais;

    }

    /**
     * Cria e retorna um novo cliente. Método desprotegido quanto a exceções
     * 
     * @param teclado Scanner do teclado
     * @return entities.Cliente criado (código de cliente automático)
     */
    public static Cliente cadastrarCliente() {
        Cliente novoCliente;
        String nome;

        limparTela();
        System.out.println("XULAMBS BURGER & PIZZA");
        System.out.println("==========================");
        System.out.println("NOVO CLIENTE");
        System.out.print("NOME: ");

        nome = teclado.nextLine();
        novoCliente = new Cliente(nome);
        conjuntoClientes.add(novoCliente);
        return novoCliente;

    }

    /**
     * Localiza um cliente em um TreeSet
     * 
     * @return Objeto do cliente encontrado ou null, se não o encontrar
     */
    private static Cliente localizarCliente() {
        String nome;
        Cliente mock;
        limparTela();
        System.out.println("XULAMBS BURGER & PIZZA");
        System.out.println("==========================");
        System.out.println("LOCALIZAR CLIENTE");
        System.out.print("NOME: ");

        nome = teclado.nextLine();
        mock = new Cliente(nome); // objeto mock para busca (equals pelo nome)

        Cliente achou = conjuntoClientes.floor(mock); // floor pega o primeiro maior OU IGUAL ao objeto de busca
        if (achou.getNome().equals(nome)) // tendo o mesmo nome, retorna. Senão, nulo
            return achou;
        else
            return cadastrarCliente();
    }

    // #endregion

    /**
     * Criação de novo pedido (modularização de função do main)
     * 
     * @return entities.Pedido criado, ou pedido atual se já existir
     */
    static Pedido criarPedido() {
        if (pedidoAtual != null) {
            System.out.println("Existe pedido em aberto.");
            pausa(teclado);
            return pedidoAtual;
        }
        try {

            itemAtual = criarComida();
            System.out.println(itemAtual.descricao + " adicionado ao pedido.");
            pedidoAtual = new Pedido(hoje, itemAtual);
        } catch (NullPointerException ex) {
            System.out.println("entities.Cliente não encontrado. Cadastrar cliente.");
            pausa(teclado);
            clienteAtual = cadastrarCliente()   ;
            conjuntoClientes.add(clienteAtual);
        }

        return pedidoAtual;
    }

    /**
     * Adiciona uma nova comida ao pedido atual. (modularização de função do main)
     */
    static void adicionarComidaAoPedido() {

        itemAtual = criarComida();
        pedidoAtual.addItem(itemAtual);
        System.out.println(itemAtual.descricao + " adicionado ao pedido.");

    }

    /**
     * Aplicativo demonstração do restaurante, versão 5: incluindo exceções e
     * serialização. A ser melhorado como tarefa de estudo: tratamento de exceções,
     * reorganização do loop de menu para uso de exceções e simplificação da lógica.
     * 
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        int opcao = 0;
        teclado = new Scanner(System.in, "UTF-8");

        conjuntoClientes = (TreeSet<Cliente>) carregarDados(teclado);
        configurarMenu();
        Cliente.setProximoCodigo(conjuntoClientes.stream() // próximo código disponível para criar cliente
                .mapToInt(Cliente::getCod)
                .max()
                .orElse(1));

        List<Pedido> todosOsPedidos = conjuntoClientes.stream() // pego um stream de clientes da árvore
                .flatMap(c -> c.getPedidos().stream()) // mapeia cada cliente para um stream dos seus pedidos
                .collect(Collectors.toList()); // coleta o mapa e transforma numa lista.

        Pedido.setProximoPedido(todosOsPedidos.stream() // próximo código disponível para novo pedido
                .mapToInt(Pedido::getId)
                .max()
                .orElse(1));

        do {
            opcao = menuPrincipal(opcao);
            limparTela();
            switch (opcao) {
                case 11:
                    if (clienteAtual == null) {
                        clienteAtual = localizarCliente();
                    }
                    if (pedidoAtual == null) {
                        pedidoAtual = criarPedido();
                    } else
                        System.out.println("Já há pedido em aberto.");
                    break;

                case 12:
                    if (clienteAtual == null) {
                        System.out.println("Não há cliente ativo.");
                    } else if (pedidoAtual == null) {
                        System.out.println("Não há pedido em aberto");
                    } else {
                        adicionarComidaAoPedido();
                    }

                    break;

                case 13:
                    try {
                        limparTela();
                        cabecalho();
                        System.out.println("entities.Cliente: " + clienteAtual.getNome());
                        System.out.println(pedidoAtual.relatorio());
                    } catch (NullPointerException nex) {
                        System.out.println("Não há cliente ativo ou pedido em aberto.");
                    }
                    break;

                case 14:
                    if (pedidoAtual != null) {
                        try {
                            limparTela();
                            cabecalho();
                            System.out.println("PEDIDO FECHADO.");
                            clienteAtual.addPedido(pedidoAtual);
                            todosOsPedidos.add(pedidoAtual);
                            System.out.println(pedidoAtual.relatorio());
                            pedidoAtual = null;
                            clienteAtual = null;
                        } catch (NullPointerException nex) {
                            System.out.println("Não há cliente ativo");
                        }
                    } else {
                        System.out.println("Não há pedido em aberto.");
                    }
                    break;

                case 21:
                    limparTela();
                    cabecalho();
                    try {
                        clienteAtual = localizarCliente();
                        System.out.println("CLIENTE: " + clienteAtual);
                        System.out.println("ÚLTIMO PEDIDO: " + clienteAtual.getPedidos().getLast());
                    } catch (NullPointerException ne) {
                        System.out.println("entities.Cliente não encontrado.");
                    } catch (NoSuchElementException nse) {
                        System.out.println("entities.Cliente não tem pedidos.");
                    }
                    break;

                case 22:
                    limparTela();
                    cabecalho();
                    try {
                        clienteAtual = localizarCliente();

                        System.out.println("CLIENTE: " + clienteAtual);
                        System.out.println("TOTAL DE PEDIDOS: " + clienteAtual.getPedidos().size());
                        System.out.println("GASTO TOTAL COM PEDIDOS: " +
                                String.format("%.2f", clienteAtual.totalEmPedidos()));
                        System.out.println("MÉDIA POR PEDIDO: " +
                                String.format("%.2f,", clienteAtual.getPedidos().stream()
                                        .mapToDouble(Pedido::getValorPago)
                                        .average()
                                        .getAsDouble()));
                        System.out.println("ÚLTIMO PEDIDO: " + clienteAtual.getPedidos().getLast());
                    } catch (NullPointerException ne) {
                        System.out.println("entities.Cliente não encontrado.");
                    }

                    break;

                case 31:
                    limparTela();
                    cabecalho();
                    Optional<Pedido> maiorDeHoje = todosOsPedidos.stream()
                            .filter(ped -> ped.getData().equals(hoje))
                            .max((p1, p2) -> p1.compareTo(p2));
                    maiorDeHoje.ifPresentOrElse(
                            (p) -> System.out.println("Maior pedido de hoje:\n" + p),
                            () -> System.out.println("Sem pedidos hoje até agora."));

                    break;

                case 32:
                    limparTela();
                    cabecalho();
                    System.out.print("Total arrecadado pelo restauraurante: R$ ");
                    System.out.println(String.format("%.2f",
                            todosOsPedidos.stream()
                                    .mapToDouble(Pedido::valorTotal)
                                    .sum()));
                    break;
                case 33:
                    limparTela();
                    cabecalho();
                    System.out.println("Cliente com maior valor total em pedidos.");
                    try {
                        Cliente maiorTotal = conjuntoClientes.stream()
                                .max((c1, c2) -> c1.totalEmPedidos() > c2.totalEmPedidos() ? 1 : -1)
                                .orElseThrow();

                        System.out
                                .println(maiorTotal + " com R$ " + String.format("%.2f", maiorTotal.totalEmPedidos()));
                    } catch (NoSuchElementException e) {
                        System.out.println("Não há clientes cadastrados");
                    }
                    break;
                case 34:
                    try {
                        limparTela();
                        System.out.print("Digite o nome do arquivo para saída:");
                        String arquivo = teclado.nextLine();
                        relatorioVendas(arquivo, todosOsPedidos);
                    } catch (IOException e) {
                        System.out.println("Problemas no uso do arquivo: " + e.getMessage());
                        System.out.println("Favor tentar novamente com outro nome de arquivo.");
                    }

                    break;

                case 35:
                    limparTela();
                    System.out.println(resumoClientes());
                    break;
            }
            pausa(teclado);
        } while (opcao != 0);
        gravarDados();
    }
}
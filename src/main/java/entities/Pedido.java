package entities;

import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedList;

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

 /**
  * Classe pedido: composição com "comida"
  */
public class Pedido implements Comparable<Pedido>, Serializable{
    static final long serialVersionUID = 20221L;
    static final Data hoje = new Data(11,05);
    public static final int MAX_ITENS=10;       //por enuanto, pedido limitado
    private static int proximo_id = 1;          //para id automático
    private int idPedido;
    private Data dataPedido;
    private Collection<Comida> itens;
    private double valorPago;


    public static void setProximoPedido(int id){
        if(id>=proximo_id)
            proximo_id = id+1;
    }
    
    public Data getData(){
        return this.dataPedido;
    }
    
    public int getId(){
        return this.idPedido;
    }

    public double getValorPago(){
        return this.valorPago;
    }

    /**
     * Valor efetivamente pago pelo pedido, considerando possíveis descontos no preço original.
     * @param valor Valor a ser registrado como pago. 
     */
    public void setValorPago (double valor){
        if(valor <= this.valorTotal())
            this.valorPago = valor;
    }

    /**
     * Construtor. Um pedido só pode ser iniciado se já houver uma comida para ele
     * @param quando  entities.Data de criação do pedido
     * @param primeira entities.Comida inicial do pedido
     */
    public Pedido(Data quando, Comida primeira) throws IllegalArgumentException{
        this.idPedido = proximo_id;
        proximo_id++;
        this.itens = new LinkedList<Comida>();
        this.addItem(primeira);
        this.dataPedido = quando;
    }

    /**
     * Adiciona uma comida ao pedido, usando JCF
     * @param novoItem entities.Comida a ser adicionada
     */
    public void addItem(Comida novoItem){
       this.itens.add(novoItem);
    }

    /**
     * Remove uma comida do pedido
     * @param novoItem entities.Comida a ser removida, usando JCF
     */
    public void cancelaItem(Comida novoItem){
        this.itens.remove(novoItem);
     }

    /**
     * Valor total do pedido
     * @return Double com o valor total do pedido
     */
    public double valorTotal(){
        double valor=0d;
        
        for (Comida comida : itens) {
            valor += comida.precoFinal();
        }

        return valor;
    }

    /**
     * Classe de relatório do pedido. (a ser melhorado)
     * @return String com detalhamento do pedido. 
     */
    public String relatorio(){
        StringBuilder relat = new StringBuilder("");
        relat.append("\n=====================\n");
        relat.append("entities.Pedido nº "+this.idPedido+" - "+this.dataPedido.dataFormatada()+"\n-----\n");
        
        for (Comida comida : itens) {
            relat.append(comida+"\n");
        }
        
        
        relat.append("TOTAL DO PEDIDO: R$"+this.valorTotal());
        relat.append("\nValor pago pelo cliente: R$"+this.getValorPago());
        relat.append("\n=====================\n");
        return relat.toString();
        
    }

    @Override 
    public String toString(){
        return this.relatorio();
    }
    
    @Override
    public int compareTo(Pedido o) {
        if(this.valorTotal()> o.valorTotal()) return 1;
        else if(this.valorTotal() < o.valorTotal()) return -1;
        return 0;
    }











}

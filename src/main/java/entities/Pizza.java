package entities; /**
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

import enums.Ingrediente;

/**
 * Classe entities.Pizza: especialização da classe abstrata entities.Comida
 */
public class Pizza extends Comida {
    //atributos
    private static final double MULTIP_INGREDIENTE = 2.0;
    private static final double VALOR_BORDA = 7.5;
    private static final double PRECO_BASE = 25.00;
    private String id = "pizza";
    private boolean temBordaRecheada;

    public String getId() {
        return id;
    }

    /**
     * inicializador privado
     * @param temBordaRecheada V/F para existência de borda recheada
     */
    private void init(boolean temBordaRecheada){
        this.temBordaRecheada = temBordaRecheada;
        this.precoBase = Pizza.PRECO_BASE;
    }
    
    /**
     * Constutor padrão, sem borda recheada
     */
    public Pizza(){
        super("entities.Pizza");     //construtor da classe mãe, configurando a descrição
        init(false);
    }

    /**
     * Construtor para informar pedido com borda recheada
     * @param temBordaRecheada True/False para inclusão de borda recheada. 
     */
    public Pizza(boolean temBordaRecheada){
        super("entities.Pizza");
        init(temBordaRecheada);
    }

    

    /**
     * Calcula o valor dos adicionais, aproveitando e estendendo o método-base (que tem valor dos ingredientes)
     * @return double Valor de todos os adicionais da pizza
     */
    @Override
    protected double precoAdicionais(){
        double aux = super.precoAdicionais();
        
        aux*=MULTIP_INGREDIENTE;
        if(this.temBordaRecheada)
            aux+=VALOR_BORDA;

        return aux;
    }

    @Override
    /**
     * Aproveita a descrição da classe mãe e inclui borda recheada, se for o caso
     */
    public String toString(){
        double preco = this.precoBase;
        StringBuilder sb = new StringBuilder();
        sb.append(this.descricao);
        if(this.temBordaRecheada){
            sb.append(" com borda recheada. ");
            preco +=VALOR_BORDA;
        }
        
        sb.append(" - \tR$"+ preco+"\n");
       
       for (Ingrediente ingrediente : ingredientes) {
            if(ingrediente!=null)
                sb.append(ingrediente+" x2\n");
       }
       
        sb.append("Valor a pagar: \tR$"+this.precoFinal()+"\n");
        sb.append("-----");
        return sb.toString();
        
       
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Pizza other = (Pizza) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

    

    
}

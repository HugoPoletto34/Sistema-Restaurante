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

/** Classe entities.Tropeiro, herda de comida (inovação no cardápio no final do semestre) */
public class Tropeiro extends Comida {

    private static final double PRECO_BASE = 10d;

    
    public Tropeiro() {
        super("entities.Tropeiro");
        this.precoBase = PRECO_BASE;
    }

    @Override
    /**
     * Aproveita a descrição da classe mãe
     */
    public String toString(){

        double preco = this.precoBase;
        StringBuilder sb = new StringBuilder();
        sb.append(this.descricao);
        sb.append(" - \tR$"+ preco+"\n");
       
       for (Ingrediente ingrediente : ingredientes) {
            if(ingrediente!=null)
                sb.append(ingrediente+"\n");
       }
       
        sb.append("Valor a pagar: \tR$"+this.precoFinal()+"\n");
        sb.append("-----");
        return sb.toString();
    }
    
}

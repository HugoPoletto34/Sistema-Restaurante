package enums; /**
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

 /** Enumerador para ingredientes, substituindo a antiga classe */
public enum Ingrediente {
    BACON(3.0, "Bacon"),
    CHEDDAR(2.0, "Cheddar"),
    BOLONHESA(4.5,"Bolonhesa"),
    PALMITO(4.0, "Palmito"),
    CALABRESA(2.50, "Calabresa");

    double precoUnitario;               //atributos dos enumeradores (Java)
    String descricao;

    Ingrediente(double precoUnitario, String desc){
        this.precoUnitario = precoUnitario;
        this.descricao = desc;
    }

    
    public double getPreco(){
        return this.precoUnitario;
    }

    @Override
    public String toString(){
        return this.descricao+ " - R$ "+this.precoUnitario;
    }
    
}

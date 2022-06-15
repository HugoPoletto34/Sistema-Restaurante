package factories;

import entities.Comida;
import entities.Pizza;
import entities.Sanduiche;
import entities.Tropeiro;
import enums.Ingrediente;

import javax.management.InvalidAttributeValueException;

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

/** Método-fábrica de comida: versão simplificada de fábrica sem obedecer ao padrão Factory Method. Não é usada na versão final. */
public class FabricaComida {
    
    static Comida criar(String tipo) throws InvalidAttributeValueException{
        switch (tipo.toLowerCase()){
            case "pizza": return new Pizza();
            case "sanduiche": return new Sanduiche();
            case "tropeiro": return new Tropeiro();
            case "bacon burger":
                    Comida nova = new Sanduiche();
                    nova.addIngrediente(Ingrediente.BACON);
                    return nova;
            default: throw new InvalidAttributeValueException("Tipo de comida inexistente");
        }
    }   
}

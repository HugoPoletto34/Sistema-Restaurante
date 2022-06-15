package factories; /**
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

import entities.Comida;
import interfaces.IFabrica;

import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.Optional;

/** Classe que encapsula todas as fábricas de comida (factory method) em um hash map*/
public class ColecaoFabrica {
    private HashMap<String, IFabrica> cardapio;


    public ColecaoFabrica(){
        cardapio = new HashMap<>();
    }

    /**
     * Acrescenta uma fábrica à coleção e associa esta fábrica a um produto
     * @param produto Nome do produto associado e fabricado
     * @param fabrica Objeto da classe fábrica correspondente
     */
    public void acrescentarFabrica(String produto, IFabrica fabrica){
        if(produto!="" && fabrica!=null)
            this.cardapio.put(produto.toLowerCase(), fabrica);
    }

    /**
     * Encapsula a criação de uma comida. Utiliza Optional para o caso do tipo ser inválido e lançar uma exceção de parâmetro inválido
     * @param tipo O tipo de comida a ser criado
     * @return Uma comida de acordo com o tipo pedido
     * @throws InvalidParameterException em caso de tipo de comida inválido e, portanto, fábrica inexistente.
     */
    public Comida criar(String tipo){
        
        Optional<IFabrica> fabrica = 
                                    Optional.ofNullable(
                                        cardapio.get(tipo.toLowerCase())
                                    );

        return fabrica.map(IFabrica::criar)
                      .orElseThrow(InvalidParameterException::new);
    }
}

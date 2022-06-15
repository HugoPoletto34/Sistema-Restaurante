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
import interfaces.IFidelizavel;

import java.time.LocalDate;
import java.util.List;

/**
 * Implementa o cliente com 10% de desconto
 */
public class Cliente10 implements IFidelizavel {
    static LocalDate x = LocalDate.now();
    static Data hoje = new Data(x.getDayOfMonth(), x.getMonthValue());
    static double DESCONTO = 0.25;
    static double VALOR_MUDANCA = 250;
    static int PEDIDOS_MUDANCA = 20;

    /**
     * Próxima categoria (acima desta)
     * @return Objeto de entities.Cliente25
     */
    private IFidelizavel getProximo() {
        return new Cliente25();
    }

    /**
     * Categoria anterior (abaixo desta)
     * @return Objeto de entities.ClienteRegular
     */
    private IFidelizavel getAnterior(){
        return new ClienteRegular();
    
    }

    @Override
    /**
     * Aplica o desconto do cliente no valor recebido como parâmetro
     * @param valor Valor original do produto
     * @return double Valor a descontar no preço pago
     */
    public double desconto(double valor) {
        return valor*DESCONTO;
    
    }

    @Override
    /**
     * Verifica se o cliente permaneceu, subiu ou desceu de categoria
     * @param pedidos Lista de pedidos do cliente
     * @return interfaces.IFidelizavel com a categoria atual do cliente
     */
    public IFidelizavel verificarCategoria(List<Pedido> pedidos) {
        
        double valorPedidos = pedidos.stream()
                                    .filter(p -> p.getData().acrescentaDias(IFidelizavel.PRAZO_FIDELIDADE).maisFutura(hoje))
                                    .mapToDouble(Pedido::getValorPago)
                                    .sum();

        long contPedidos = pedidos.stream()
                                .filter(p -> p.getData().acrescentaDias(IFidelizavel.PRAZO_FIDELIDADE).maisFutura(hoje))
                                .count();


        if((valorPedidos>=VALOR_MUDANCA) || (contPedidos>=PEDIDOS_MUDANCA))
            return this.getProximo();
        else
            return this.getAnterior().verificarCategoria(pedidos);

    }
    
    
}

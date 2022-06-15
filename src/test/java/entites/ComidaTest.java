package entites;

import entities.Comida;
import entities.Pizza;
import entities.Sanduiche;
import entities.Tropeiro;
import enums.Ingrediente;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ComidaTest {
    @Test
    void doisBaconsPrecoFinalTest() {
        Comida c = new Pizza();
        c.addIngrediente(Ingrediente.BACON);
        c.addIngrediente(Ingrediente.BACON);

        assertEquals(37.0, c.precoFinal());
    }

    @Test
    void precoFinalBasePizza() {
        Comida c = new Pizza();
        assertEquals(25.0, c.precoFinal());
    }

    @Test
    void precoFinalBaseSanduiche() {
        Comida c = new Sanduiche();
        assertEquals(13.0, c.precoFinal());
    }

    @Test
    void precoFinalBaseTropeiro() {
        Comida c = new Tropeiro();
        assertEquals(10.0, c.precoFinal());
    }

    @Test
    void adicionarIgredientesTest() {
        Comida c = new Pizza();
        c.addIngrediente(Ingrediente.BACON);
        c.addIngrediente(Ingrediente.CHEDDAR);
        c.addIngrediente(Ingrediente.CALABRESA);

        assertArrayEquals(new Ingrediente[] {Ingrediente.BACON, Ingrediente.CHEDDAR, Ingrediente.CALABRESA}, c.ingredientes.toArray());
    }

    @Test
    void precoAdicionaisPizzaTest() {
        Pizza c = new Pizza();
        c.addIngrediente(Ingrediente.BACON);
        c.addIngrediente(Ingrediente.CHEDDAR);
        c.addIngrediente(Ingrediente.CALABRESA);

        assertEquals(15.0, c.precoAdicionais());
    }


    @Test
    void precoAdicionaisSanduicheTest() {
        Sanduiche c = new Sanduiche();
        c.addIngrediente(Ingrediente.BACON);
        c.addIngrediente(Ingrediente.CHEDDAR);
        c.addIngrediente(Ingrediente.CALABRESA);

        assertEquals(7.5, c.precoAdicionais());
    }

    @Test
    void removerIngredientesTest() {
        Tropeiro c = new Tropeiro();
        c.addIngrediente(Ingrediente.BACON);
        c.addIngrediente(Ingrediente.CHEDDAR);
        c.addIngrediente(Ingrediente.CALABRESA);

        c.removeIngrediente(Ingrediente.BACON);

        assertFalse(c.ingredientes.contains(Ingrediente.BACON));
    }

}

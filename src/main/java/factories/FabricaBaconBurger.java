package factories;

import entities.Comida;
import entities.Sanduiche;
import enums.Ingrediente;
import interfaces.IFabrica;

public class FabricaBaconBurger implements IFabrica {
    
    public Comida criar(){
        Sanduiche s = new Sanduiche();
        s.addIngrediente(Ingrediente.BACON);
        return s;
    }
}

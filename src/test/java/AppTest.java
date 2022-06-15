import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

public class AppTest {

    @Test
    public void testApp() {

        App.configurarMenu();
        assertNotNull(App.todasAsFabricas, "Fabrica não foi inicializada!");
    }

}

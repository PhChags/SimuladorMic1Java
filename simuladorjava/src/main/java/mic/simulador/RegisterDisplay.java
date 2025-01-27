package mic.simulador;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

public class RegisterDisplay {
    private final SimpleStringProperty name; // Nome do registrador
    private final SimpleObjectProperty<Short> value; // Valor do registrador
    
    // Construtor que inicializa o nome e o valor do registrador
    public RegisterDisplay(String name, short value) {
        this.name = new SimpleStringProperty(name); // Nome do registrador
        this.value = new SimpleObjectProperty<>((short) value); // Valor armazenado no registrador
    }
    
    // Retorna a propriedade do nome do registrador
    public SimpleStringProperty nameProperty() {
        return name;
    }
    
    // Retorna a propriedade do valor armazenado no registrador
    public SimpleObjectProperty<Short> valueProperty() {
        return value;
    }
    
}

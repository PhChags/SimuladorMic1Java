package mic.simulador;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;

public class MemoryRow {
    private final SimpleIntegerProperty address; // Endereço da memória
    private final SimpleObjectProperty<Short> value; // Valor armazenado no endereço da memória
    
    // Construtor que inicializa o endereço e o valor da memória
    public MemoryRow(int address, short value) {
        this.address = new SimpleIntegerProperty(address); // Endereço da memória
        this.value = new SimpleObjectProperty<>((short) value); // Valor armazenado no endereço
    }
    
    // Retorna a propriedade do endereço
    public SimpleIntegerProperty addressProperty() {
        return address;
    }
    
    // Retorna a propriedade do valor armazenado
    public SimpleObjectProperty<Short> valueProperty() {
        return value;
    }
    
}

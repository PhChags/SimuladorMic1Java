package mic.simulador.backend;

import javafx.beans.property.SimpleStringProperty;

public class Register {
    // Valor armazenado no registrador
    private short value;
    // Propriedade que armazena o nome do registrador
    private final SimpleStringProperty name;
    // Propriedade que armazena o valor do registrador como string (em hexadecimal)
    private final SimpleStringProperty stringValue;
    
    // Construtor que inicializa o nome e o valor do registrador
    public Register(String name, short value) {
        this.name = new SimpleStringProperty(name); // Inicializa o nome do registrador
        this.value = value; // Inicializa o valor do registrador
        this.stringValue = new SimpleStringProperty(NumericFactory.getStringValue16(value)); // Converte o valor para string (hexadecimal)
    }
    
    // Retorna o nome do registrador
    public String getName() {
        return name.get();
    }
    
    // Retorna a propriedade que representa o nome do registrador, permitindo a vinculação de dados
    public SimpleStringProperty nameProperty() {
        return name;
    }
    
    // Define o nome do registrador
    public void setName(String name) {
        this.name.set(name);
    }
    
    // Retorna o valor do registrador
    public short getValue() {
        return value;
    }
    
    // Define o valor do registrador e atualiza a representação em string
    public void setValue(short value) {
        this.value = value;
        // Atualiza a representação em string do valor do registrador
        stringValue.setValue(NumericFactory.getStringValue16(value));
    }
    
    // Retorna o valor do registrador como uma string (em formato hexadecimal)
    public String getStringValue() {
        return stringValue.get();
    }
    
    // Retorna a propriedade que representa o valor do registrador como string
    public SimpleStringProperty stringValueProperty() {
        return stringValue;
    }
    
    // Define o valor do registrador como string
    public void setStringValue(String stringValue) {
        this.stringValue.set(stringValue);
    }
    
    // Retorna uma representação em string do registrador (nome e valor)
    @Override
    public String toString() {
        return "Register{" +
                "name=" + name +
                ", value=" + value +
                '}';
    }
}

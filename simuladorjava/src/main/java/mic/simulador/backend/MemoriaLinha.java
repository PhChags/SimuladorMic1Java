package mic.simulador.backend;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class MemoriaLinha {
    // Valor armazenado na linha de memória
    private short value;
    // Endereço da linha de memória (representado como uma propriedade de inteiro simples)
    private final SimpleIntegerProperty address;
    // Valor da linha de memória em formato de string (representação hexadecimal)
    private final SimpleStringProperty stringValue;
    
    // Construtor que inicializa o endereço e o valor da linha de memória
    public MemoriaLinha(short address, short value) {
        this.address = new SimpleIntegerProperty(address); // Inicializa a propriedade do endereço
        this.value = value;
        // Inicializa a propriedade do valor como a representação hexadecimal do valor
        stringValue = new SimpleStringProperty(NumericFactory.getStringValue16(value));
    }
    
    // Retorna o endereço da linha de memória
    public int getAddress() {
        return address.get();
    }
    
    // Retorna a propriedade do endereço (para permitir a vinculação de dados)
    public SimpleIntegerProperty addressProperty() {
        return address;
    }
    
    // Define o endereço da linha de memória
    public void setAddress(int address) {
        this.address.set(address);
    }
    
    // Retorna o valor da linha de memória
    public short getValue() {
        return value;
    }
    
    // Define o valor da linha de memória e atualiza a representação em string
    public void setValue(short value) {
        this.value = value;
        // Atualiza a representação hexadecimal do valor
        stringValue.setValue(NumericFactory.getStringValue16(value));
    }
    
    // Retorna o valor da linha de memória como uma string
    public String getStringValue() {
        return stringValue.get();
    }
    
    // Retorna a propriedade de string (para permitir a vinculação de dados)
    public SimpleStringProperty stringValueProperty() {
        return stringValue;
    }
    
    // Define o valor da linha de memória como uma string
    public void setStringValue(String stringValue) {
        this.stringValue.set(stringValue);
    }
    
}

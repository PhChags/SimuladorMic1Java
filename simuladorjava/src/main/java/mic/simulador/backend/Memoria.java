package mic.simulador.backend;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Memoria {
    // Lista observável que armazena as linhas de memória
    private final ObservableList<MemoriaLinha> memory;
    // Endereço atual da memória
    private short address;
    // Contadores para leitura e escrita
    private byte readCounter, writeCounter;

    // Construtor que inicializa a memória
    public Memoria() {
        // Inicializa a lista de memória com 4096 linhas
        memory = FXCollections.observableArrayList();
        // Preenche as primeiras 2048 linhas com um valor inicial de 0x7000
        for (short i = 0; i < 2048; i++)
            memory.add(new MemoriaLinha(i, (short) 0x7000));
        // Preenche as linhas restantes com valor 0
        for (short i = 2048; i < 4096; i++)
            memory.add(new MemoriaLinha(i, (short) 0));

        // Inicializa os contadores e o endereço
        address = 0;
        readCounter = 0;
        writeCounter = 0;
    }

    // Método para reiniciar a memória para os valores iniciais
    public void setMemoryInitial() {
        // Reinicia as primeiras 2048 linhas para o valor 0x7000
        for (short i = 0; i < 2048; i++)
            memory.get(i).setValue((short) 0x7000);
        // Reinicia as linhas restantes para o valor 0
        for (short i = 2048; i < 4096; i++)
            memory.get(i).setValue((short) 0);

        // Reinicia o endereço e os contadores
        address = 0;
        readCounter = 0;
        writeCounter = 0;
    }

    // Verifica se a memória está pronta para leitura (contador de leitura igual a 2)
    public boolean isReadReady() {
        return readCounter == 2;
    }

    // Incrementa o contador de leitura
    public void incrementReadCounter() {
        readCounter++;
    }

    // Realiza a leitura da memória no endereço atual e zera o contador de leitura
    public short read() {
        readCounter = 0;
        return memory.get(address).getValue();
    }

    // Verifica se a memória está pronta para escrita (contador de escrita igual a 2)
    public boolean isWriteReady() {
        return writeCounter == 2;
    }

    // Incrementa o contador de escrita
    public void incrementWriteCounter() {
        writeCounter++;
    }

    // Realiza a escrita na memória no endereço atual e zera o contador de escrita
    public void write(short value) {
        writeCounter = 0;
        memory.get(address).setValue(value);
    }

    // Verifica se o endereço passado é válido (dentro do intervalo de 0 a 4095)
    private boolean isValidAddress(short address) {
        return address >= 0 && address <= 4095;
    }

    // Define o endereço de memória, caso seja válido
    public void setAddress(short address) {
        if (!isValidAddress(address))
            return;
        this.address = address;
    }

    // Realiza a escrita em um endereço específico, caso o endereço seja válido
    public void write(short address, short value) {
        if (!isValidAddress(address))
            return;
        memory.get(address).setValue(value);
    }

    // Escreve um array de dados de código na memória, até o limite de 4096
    public void write(short[] codeData) {
        int length = codeData.length;
        if (length > 4096) {
            length = 4096;
        }
        for (short i = 0; i < length; i++) {
            memory.get(i).setValue(codeData[i]);
        }
    }

    // Realiza a leitura de um endereço específico, retornando 0 se o endereço não for válido
    public short read(short address) {
        if (!isValidAddress(address))
            return 0;
        return memory.get(address).getValue();
    }

    // Retorna a lista observável de linhas de memória
    public ObservableList<MemoriaLinha> getMemory() {
        return memory;
    }
}

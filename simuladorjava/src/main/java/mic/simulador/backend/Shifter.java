package mic.simulador.backend;

public class Shifter {
    // Variável que armazena o valor de saída após a operação de deslocamento
    private short output = 0;
    
    // Retorna o valor da saída
    public short getOutput() {
        return output;
    }
    
    // Define o valor da saída
    public void setOutput(short output) {
        this.output = output;
    }
    
    // Realiza a operação de deslocamento (shift) dependendo do valor do controle
    // Se o controle for 1, realiza um deslocamento à direita
    // Se o controle for 2, realiza um deslocamento à esquerda
    public void shift(byte control, short input) {
        output = input; // Inicializa a saída com o valor de entrada
        // Se o controle for 1, realiza o deslocamento à direita (divisão por 2)
        if (control == 1)
            output = (short) (output >> 1);
        // Se o controle for 2, realiza o deslocamento à esquerda (multiplicação por 2)
        else if (control == 2)
            output = (short) (output << 1);
    }
    
    // Retorna uma representação em string da classe Shifter, incluindo o valor da saída
    @Override
    public String toString() {
        return "Shifter{" +
                "output=" + output +
                '}';
    }

}

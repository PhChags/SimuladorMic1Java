package mic.simulador.backend;

public class Mux {
    // Variável que armazena a saída do multiplexador
    private short output = 0;
    
    // Retorna o valor da saída
    public short getOutput() {
        return output;
    }
    
    // Define o valor da saída
    public void setOutput(short output) {
        this.output = output;
    }
    
    // Decida qual valor será a saída, baseado no controle e nas entradas
    // Se o controle for verdadeiro, a saída será o valor de inputB
    // Caso contrário, a saída será o valor de inputA
    public void decideOutput(boolean control, short inputA, short inputB) {
        if (control)
            output = inputB; // Se control for true, escolhe inputB
        else
            output = inputA; // Se control for false, escolhe inputA
    }
    
    // Retorna uma representação em string da classe Mux, incluindo o valor da saída
    @Override
    public String toString() {
        return "Mux{" +
                "output=" + output +
                '}';
    }
}

package mic.simulador.backend;

// Classe que simula uma Unidade Lógica e Aritmética (ALU) de um processador MIC-1
public class ALU {
    // Atributos da classe: saída da ALU e os bits de status (N e Z)
    private short output = 0; // Saída da ALU
    private boolean NBit = false; // Bit de sinal negativo (N)
    private boolean ZBit = false; // Bit de zero (Z)

    // Método para obter a saída atual da ALU
    public short getOutput() {
        return output;
    }

    // Método para definir manualmente a saída da ALU
    public void setOutput(short output) {
        this.output = output;
    }

    // Método para obter o valor do bit de negativo (N)
    public boolean getNBit() {
        return NBit;
    }

    // Método para definir manualmente o bit de negativo (N)
    public void setNBit(boolean nBit) {
        this.NBit = nBit;
    }

    // Método para obter o valor do bit de zero (Z)
    public boolean getZBit() {
        return ZBit;
    }

    // Método para definir manualmente o bit de zero (Z)
    public void setZBit(boolean zBit) {
        this.ZBit = zBit;
    }

    /**
     * Método que realiza cálculos baseados no valor do controle e entradas fornecidas.
     * 
     * @param control Código de controle que define a operação:
     *                - 0: Soma (inputA + inputB)
     *                - 1: AND bit a bit entre inputA e inputB
     *                - 2: Passa diretamente o valor de inputA
     *                - 3: NOT bit a bit de inputA
     * @param inputA Primeira entrada da operação
     * @param inputB Segunda entrada da operação (usada apenas para soma e AND)
     */
    public void calculate(byte control, short inputA, short inputB) {
        // Operação padrão: soma
        output = (short) (inputA + inputB);

        // Define a operação com base no código de controle
        if (control == 1) { // AND bit a bit
            output = (short) (inputA & inputB);
        } else if (control == 2) { // Passa diretamente inputA
            output = inputA;
        } else if (control == 3) { // NOT bit a bit de inputA
            output = (short) ~inputA;
        }

        // Define o bit Z (Zero) como verdadeiro se a saída for 0
        ZBit = output == 0;
        // Define o bit N (Negativo) como verdadeiro se a saída for menor que 0
        NBit = output < 0;
    }

    // Representação da ALU como String, mostrando os valores da saída e dos bits de status
    @Override
    public String toString() {
        return "out: " + output + "\nN: " + NBit + "\nZ: " + ZBit;
    }
}

package mic.simulador.backend;

public class MSeqLogic {
    // Variável que armazena a saída da lógica
    private boolean output = false;
    
    // Retorna o valor da saída
    public boolean isOutput() {
        return output;
    }
    
    // Define o valor da saída
    public void setOutput(boolean output) {
        this.output = output;
    }
    
    // Gera a saída com base no controle e nos valores n e z
    // control é um valor que determina a lógica da saída
    // n e z são condições adicionais que podem influenciar o resultado
    public void generateOutput(byte control, boolean n, boolean z) {
        // A lógica define a saída como verdadeira (true) em condições específicas:
        // - control == 3
        // - control == 1 e n é verdadeiro
        // - control == 2 e z é verdadeiro
        output = control == 3 || control == 1 && n || control == 2 && z;
    }
    
    // Retorna uma representação em string da classe MSeqLogic, incluindo o valor da saída
    @Override
    public String toString() {
        return "MSeqLogic{" +
                "output=" + output +
                '}';
    }
}

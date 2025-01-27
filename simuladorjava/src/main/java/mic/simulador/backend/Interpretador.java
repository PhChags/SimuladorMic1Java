package mic.simulador.backend;

import java.util.Map;

// não conseguimos implementar o uso dessa função como gostariamos, além de como está sendo utilizada atualmente pretendiamos utiliza-la na parte gráfica
public class Interpretador {
    // Mapa contendo as instruções suportadas e seus códigos de operação correspondentes
    private final Map<String, String> supportedInstructions;
    
    // Instância única da classe (Singleton)
    private static Interpretador instance;
    
    // Método para obter a instância única da classe
    public static Interpretador getInstance() {
        if (instance == null) instance = new Interpretador();
        return instance;
    }
    
    // Construtor privado que inicializa o mapa de instruções suportadas
    private Interpretador() {
        supportedInstructions = LeitorArquivos.getSupportedInstructionsMap();
    }
    
    // Retorna a string de instrução formatada com base no opcode e no operando
    public String getInstructionString(short bytes) {
        short opcode, operand;
    
        // Determina o formato do opcode e operando com base nos bits mais significativos
        if ((bytes & 0xF000) == 0xF000) {
            opcode = (short) ((bytes & 0xFF00) >> 8); // Extrai o opcode
            operand = (short) (bytes & 0x00FF);       // Extrai o operando
        } else {
            opcode = (short) ((bytes & 0xF000) >> 12); // Extrai o opcode
            operand = (short) (bytes & 0x0FFF);        // Extrai o operando
        }
    
        // Retorna a instrução com ou sem argumento, dependendo da necessidade
        if (instructionRequiresArgument(getMnemonic(opcode)))
            return getMnemonic(opcode) + " " + operand;
        return getMnemonic(opcode);
    }
    
    // Retorna o mnemônico associado a um opcode
    public String getMnemonic(short opcode) {
        String opcodeString = getOpcodeBytesString(opcode); // Converte o opcode em uma string de bits
        for (String mnemonic : supportedInstructions.keySet()) {
            if (supportedInstructions.get(mnemonic).equals(opcodeString))
                return mnemonic;
        }
        return null; // Retorna null caso o opcode não seja encontrado
    }
    
    // Converte um opcode em uma string binária (representação de bytes)
    public String getOpcodeBytesString(short opcode) {
        StringBuilder result = new StringBuilder();
        short delimiter = 4; // Define o limite padrão de 4 bits
    
        // Ajusta o limite para 8 bits caso necessário
        if ((opcode & 0x00F0) == 0x00F0) {
            delimiter = 8;
        }
    
        // Converte o opcode para sua representação binária
        for (short i = 0; i < delimiter; i++) {
            result.insert(0, opcode & 0x0001); // Insere o bit menos significativo no início
            opcode >>= 1;                     // Desloca o opcode para a direita
        }
    
        return result.toString();
    }
    
    // Retorna a string de bytes associada a um mnemônico
    public String getOpcodeBytesString(String mnemonic) {
        return supportedInstructions.get(mnemonic.toUpperCase());
    }
    
    // Verifica se a instrução requer um argumento
    public boolean instructionRequiresArgument(String mnemonic) {
        // Retorna false para instruções que não necessitam de argumento
        return !(mnemonic.equalsIgnoreCase("PSHI") || mnemonic.equalsIgnoreCase("POPI")
                || mnemonic.equalsIgnoreCase("PUSH") || mnemonic.equalsIgnoreCase("POP")
                || mnemonic.equalsIgnoreCase("RETN") || mnemonic.equalsIgnoreCase("SWAP"));
    }
    
    // Verifica se a instrução é do tipo de salto (jump)
    public boolean instructionIsJump(String mnemonic) {
        return mnemonic.equalsIgnoreCase("JPOS") || mnemonic.equalsIgnoreCase("JZER")
                || mnemonic.equalsIgnoreCase("JUMP") || mnemonic.equalsIgnoreCase("JNEG")
                || mnemonic.equalsIgnoreCase("JNZE");
    }
    
    // Verifica se a instrução é suportada
    public boolean instructionIsSupported(String mnemonic) {
        return supportedInstructions.containsKey(mnemonic.toUpperCase());
    }
    
}

package mic.simulador.backend;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

// Classe responsável por interpretar e traduzir o código assembly de um processador MIC-1 para código de máquina.
public class Compilador {
    // Instância única da classe (implementação de Singleton).
    private static Compilador instance;
    
    // Instância do Interpretador (agora renomeado para intptdr) para auxiliar na análise das instruções.
    private final Interpretador intptdr;
    
    // Método estático para obter a instância única de Compilador.
    public static Compilador getInstance() {
        if (instance == null) instance = new Compilador();
        return instance;
    }
    
    // Construtor privado para evitar instâncias externas e inicializar o Interpretador.
    private Compilador() {
        intptdr = Interpretador.getInstance();
    }
    
    /**
     * Método principal que traduz o código assembly para código de máquina.
     * 
     * @param code Código assembly em formato de string, com cada instrução separada por linhas.
     * @return Array de shorts contendo as instruções em código de máquina.
     * @throws CompiladorException Caso ocorra algum erro durante a análise ou tradução.
     */
    public short[] parseCode(String code) throws CompiladorException {
        Map<String, Integer> labels = new HashMap<>(); // Mapa para armazenar rótulos e seus endereços de memória.
    
        // Divide o código em linhas.
        String[] codeLines = code.split("\n");
    
        // Limpa o código e processa rótulos.
        purifyCode(codeLines, labels);
    
        // Inicializa a memória de código de máquina com instruções padrão (0x7000).
        short[] machineCode = new short[2048];
        Arrays.fill(machineCode, (short) 0x7000);
    
        int lineNumber = 1; // Contador para a linha atual (usado para erros).
        int blankLinesCounter = 0; // Contador de linhas em branco.
        int memAddress = 0; // Endereço atual na memória.
    
        for (String line : codeLines) {
            // Verifica se o limite de memória foi atingido.
            if (memAddress > 2047)
                return machineCode;
    
            // Ignora linhas em branco.
            if (line.equals("")) {
                blankLinesCounter++;
                lineNumber++;
                continue;
            }
    
            // Divide a linha em elementos da instrução.
            String[] instructionElements = line.split(" ");
    
            // Verifica se a instrução é suportada.
            if (!intptdr.instructionIsSupported(instructionElements[0]))
                throw new CompiladorException(lineNumber + "=unknown-instr-mnemonic");
    
            // Inicializa o argumento com valor padrão.
            String argumentBinaryString = "00000000";
    
            // Verifica se a instrução requer argumento.
            if (intptdr.instructionRequiresArgument(instructionElements[0])) {
                // Verifica se o argumento está ausente.
                if (instructionElements.length < 2)
                    throw new CompiladorException(lineNumber + "=missing-arg");
    
                try {
                    // Trata argumentos relacionados a saltos (jumps).
                    if (intptdr.instructionIsJump(instructionElements[0])) {
                        try {
                            Integer.parseInt(instructionElements[1]); // Tenta interpretar como número.
                        } catch (NumberFormatException ex) {
                            // Verifica se o argumento é um rótulo válido.
                            if (!labels.containsKey(instructionElements[1]))
                                throw new CompiladorException(lineNumber + "=unknown-label");
                            instructionElements[1] = String.valueOf(labels.get(instructionElements[1]));
                        }
                    }
    
                    // Valida o valor do argumento.
                    int paramValue = Integer.parseInt(instructionElements[1]);
                    int boundary = 4095; // Limite padrão para valores de argumentos.
    
                    // Ajusta o limite para instruções específicas.
                    if (instructionElements[0].equalsIgnoreCase("INSP")
                            || instructionElements[0].equalsIgnoreCase("DESP"))
                        boundary = 255;
    
                    if (paramValue < 0 || paramValue > boundary)
                        throw new CompiladorException(lineNumber + "=arg-out-of-bounds");
    
                    // Converte o argumento para uma string binária.
                    argumentBinaryString = Integer.toBinaryString(paramValue);
    
                } catch (NumberFormatException ex) {
                    throw new CompiladorException(lineNumber + "=invalid-arg");
                }
    
                // Verifica se há argumentos extras não permitidos.
                if (instructionElements.length > 2)
                    throw new CompiladorException(lineNumber + "=too-many-args");
            } else if (instructionElements.length > 1) {
                throw new CompiladorException(lineNumber + "=too-many-args");
            }
    
            // Obtém o opcode da instrução.
            String opcodeBinaryString = intptdr.getOpcodeBytesString(instructionElements[0]);
    
            // Adiciona a instrução montada ao array de código de máquina.
            addInstructionToAssembledInstructions(machineCode, memAddress, opcodeBinaryString, argumentBinaryString);
    
            lineNumber++;
            memAddress++;
        }
    
        // Verifica se o código está completamente vazio.
        if (blankLinesCounter == codeLines.length)
            throw new CompiladorException("no-code-err");
    
        return machineCode;
    }
    
    /**
     * Adiciona uma instrução montada ao array de código de máquina.
     * 
     * @param machineCode Array de código de máquina.
     * @param memAddress Endereço de memória onde a instrução será armazenada.
     * @param opcodeBinaryString Parte do opcode da instrução.
     * @param argumentBinaryString Parte do argumento da instrução.
     */
    private void addInstructionToAssembledInstructions(short[] machineCode, int memAddress, String opcodeBinaryString, String argumentBinaryString) {
        // Formata o argumento binário para completar 16 bits.
        String format = "%" + (16 - opcodeBinaryString.length()) + "s";
        argumentBinaryString = String.format(format, argumentBinaryString).replace(' ', '0');
    
        // Concatena o opcode e o argumento e converte para short.
        String binaryInstruction = opcodeBinaryString + argumentBinaryString;
        machineCode[memAddress] = (short) Integer.parseInt(binaryInstruction, 2);
    }
    
    /**
     * Purifica o código removendo comentários, espaços extras e processando rótulos.
     * 
     * @param codeLines Linhas de código assembly.
     * @param labels Mapa para armazenar rótulos e seus endereços de memória.
     * @throws CompiladorException Caso um rótulo se repita ou seja inválido.
     */
    private void purifyCode(String[] codeLines, Map<String, Integer> labels) throws CompiladorException {
        int memoryAddress = 0; // Endereço de memória atual.
    
        for (int i = 0; i < codeLines.length; i++) {
            // Remove comentários e espaços extras.
            codeLines[i] = codeLines[i].replaceAll(";.*", ""); 
            codeLines[i] = codeLines[i].trim(); 
            codeLines[i] = codeLines[i].replaceAll(" +", " "); 
    
            if (codeLines[i].equals("")) continue; // Ignora linhas vazias.
    
            String[] elements = codeLines[i].split(" ");
            // Processa rótulos (labels).
            if (elements.length >= 1 && elements[0].endsWith(":")) {
                String newLabel = elements[0].substring(0, elements[0].length() - 1);
                if (labels.containsKey(newLabel))   
                    throw new CompiladorException((i + 1) + "=recurring-label");
    
                labels.put(newLabel, memoryAddress); // Adiciona o rótulo ao mapa.
    
                // Remove o rótulo da linha, mantendo a instrução, se existir.
                if (elements.length > 1) {
                    codeLines[i] = codeLines[i].replace(newLabel + ": ", "");
                    i--;    
                } else {
                    codeLines[i] = codeLines[i].replace(newLabel + ":", "");
                }
                continue;
            }
    
            memoryAddress++; // Incrementa o endereço de memória para a próxima instrução.
        }
    }    
}

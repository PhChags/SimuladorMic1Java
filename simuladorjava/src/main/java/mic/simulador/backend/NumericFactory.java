package mic.simulador.backend;

public class NumericFactory {
    // Radix (base numérica) utilizada para conversão (padrão é 10)
    private static int radix = 10;
    
    // Método que converte uma string para um valor short, baseado na base atual (radix)
    public static short getShortValue(String value) {
        // Se a base for 10, chama o método de conversão para decimal
        if (radix == 10)
            return getShortValue10(value);
        // Caso contrário, chama o método de conversão para binário
        return getShortValue2(value);
    }
    
    // Método que converte uma string para short, assumindo que a base é binária (radix = 2)
    private static short getShortValue2(String value) {
        return (short) Integer.parseUnsignedInt(value, 2); // Converte para inteiro sem sinal e depois para short
    }
    
    // Método que converte uma string para short, assumindo que a base é decimal (radix = 10)
    private static short getShortValue10(String value) {
        return (short) Integer.parseInt(value); // Converte diretamente para inteiro e depois para short
    }
    
    // Método que converte um short para uma string em formato hexadecimal
    public static String getStringValue16(short value) {
        return getStringValue(value, 16); // Chama o método de conversão com base 16 (hexadecimal)
    }
    
    // Método que converte um inteiro para uma string em formato binário de 32 bits
    public static String getStringValue32(int value) {
        return getStringValue(value, 32); // Chama o método de conversão com base 32
    }
    
    // Método que converte um short para uma string em formato binário de 8 bits
    public static String getStringValue8(short value) {
        // Aqui usamos short porque precisamos de um byte sem sinal
        return getStringValue(value, 8); // Chama o método de conversão com base 8 (binário de 8 bits)
    }
    
    // Método genérico que converte um valor inteiro para uma string, usando a base especificada
    public static String getStringValue(int value, int length) {
        if (radix == 10)
            return String.valueOf(value); // Se a base for decimal, retorna o valor como string
    
        // Se a base não for decimal, converte o valor para binário
        String res = Integer.toBinaryString(value);
        // Se o comprimento do valor binário for maior que o esperado, corta o excesso
        if (res.length() > length)
            return res.substring(res.length() - length);
        // Se o comprimento for menor, completa com zeros à esquerda
        return "0".repeat(length - res.length()) + res;
    }
    
    // Método que converte uma string para short, utilizando uma base específica
    public static short getShortValue(String value, int radix) {
        try {
            return (short) Integer.parseInt(value, radix); // Converte a string para short com a base fornecida
        } catch (NumberFormatException e) {
            System.out.println("Error in NumericFactory getShortValue"); // Captura e exibe erro em caso de falha
        }
        return 0; // Retorna 0 em caso de erro
    }
    
    // Retorna a base numérica atual
    public static int getRadix() {
        return radix;
    }
    
    // Define a base numérica a ser utilizada para conversões
    public static void setRadix(int radix) {
        NumericFactory.radix = radix; // Atualiza a base numérica
    }
    
}

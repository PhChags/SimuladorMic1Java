package mic.simulador.backend;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

// não conseguimos implementar o uso dessa função como gostariamos, além de como está sendo utilizada atualmente pretendiamos utiliza-la na parte gráfica
public class LeitorArquivos {
    // Singleton para gerenciar recursos observáveis
    private static final ObservableResourceFactory resourceFactory = ObservableResourceFactory.getInstance();

    // Carrega dados da tabela de instruções suportadas a partir de um arquivo CSV
    public static ObservableList<Map<String, String>> loadSupportedInstructionsTableData() {
        ObservableList<Map<String, String>> items = FXCollections.observableArrayList();
    
        try {
            // Lê o arquivo CSV de instruções suportadas
            BufferedReader csvReader = getBufferedReader("/dataFiles/supportedInstructions.csv");
            String row;
            while ((row = csvReader.readLine()) != null) {
                String[] data = row.split(",");
    
                // Mapeia as colunas do arquivo CSV para chaves e valores
                Map<String, String> rawData = new HashMap<>();
                rawData.put("mnemonic", data[0]);
                rawData.put("instruction" , resourceFactory.getResources().getString(data[0]));
                rawData.put("meaning", data[2]);
                rawData.put("binaryCode" , data[3]);
    
                items.add(rawData);
            }
            csvReader.close();
        } catch (IOException e) {
            // Mensagem de erro caso ocorra problema ao ler o arquivo
            System.out.println("Problemas ao ler supportedInstructions.csv!");
            e.printStackTrace();
        }
    
        return items;
    }
    
    // Carrega dados da tabela de memória de controle a partir de um arquivo TXT
    public static ObservableList<Map<String, String>> loadControlMemoryTableData() {
        ObservableList<Map<String, String>> items = FXCollections.observableArrayList();
    
        try {
            BufferedReader reader = getBufferedReader("/dataFiles/controlMemory.txt");
            String row;
            int i = 0;
            while ((row = reader.readLine()) != null) {
                String[] data = row.split(" ");
                if (data.length != 13) continue; // Ignora linhas com menos de 13 elementos
    
                // Mapeia cada valor do arquivo para as chaves correspondentes
                Map<String, String> rawData = new HashMap<>();
                rawData.put("address", String.valueOf(i++));
                rawData.put("amux", data[0]);
                rawData.put("cond" , data[1]);
                rawData.put("alu", data[2]);
                rawData.put("sh" , data[3]);
                rawData.put("mbr", data[4]);
                rawData.put("mar", data[5]);
                rawData.put("rd", data[6]);
                rawData.put("wr", data[7]);
                rawData.put("enc", data[8]);
                rawData.put("c", data[9]);
                rawData.put("b", data[10]);
                rawData.put("a", data[11]);
                rawData.put("addr", data[12]);
    
                items.add(rawData);
            }
            reader.close();
        } catch (IOException e) {
            System.out.println("Problemas ao ler controlMemory.txt!");
            e.printStackTrace();
        }
    
        return items;
    }
    
    // Carrega o microcódigo de um arquivo TXT e formata o conteúdo
    public static String loadMicroCode() {
        StringBuilder result = new StringBuilder();
        try {
            BufferedReader micReader = getBufferedReader("/dataFiles/microprogram.txt");
            String row;
            short i = 0;
            String insert = " 0";
            while ((row = micReader.readLine()) != null) {
                // Ajusta a formatação com base no número de linhas
                if (i >= 10 && i < 100)
                    insert = " ";
                else if (i >= 100)
                    insert = "";
                result.append(insert).append(i).append("|  ").append(row).append("\n");
                i++;
            }
            micReader.close();
        } catch (IOException e) {
            System.out.println("Problemas ao ler microprogram.txt!");
            e.printStackTrace();
        }
    
        return result.toString();
    }
    
    // Cria um mapa de instruções suportadas com base no CSV
    public static Map<String, String> getSupportedInstructionsMap() {
        Map<String, String> result = new HashMap<>();
    
        try {
            BufferedReader csvReader = getBufferedReader("/dataFiles/supportedInstructions.csv");
            String row;
            while ((row = csvReader.readLine()) != null) {
                String[] data = row.split(",");
                String[] opcodeLine = data[3].split(" ");
                String opcode = opcodeLine[0];
                if (opcode.equals("1111"))   
                    opcode += opcodeLine[1];
                result.put(data[0], opcode);
            }
            csvReader.close();
        } catch (IOException e) {
            System.out.println("Problemas ao ler supportedInstructions.csv!");
            e.printStackTrace();
        }
    
        return result;
    }
    
    // Carrega a memória de controle de um arquivo para um array de inteiros
    public static int[] getControlMemory() {
        int[] result = new int[256];
        try {
            BufferedReader bufferedReader = getBufferedReader("/dataFiles/controlMemoryINT.txt");
            String row;
            int i = 0;
            while ((row = bufferedReader.readLine()) != null) {
                result[i++] = Integer.parseInt(row);
            }
            bufferedReader.close();
        } catch (IOException e) {
            System.out.println("Problemas ao ler controlMemoryINT.txt!");
            e.printStackTrace();
        }
        return result;
    }
    
    // Obtém um BufferedReader para um arquivo com base no caminho especificado
    private static BufferedReader getBufferedReader(String path) {
        InputStream inputStream = LeitorArquivos.class.getResourceAsStream(path);
        assert inputStream != null;
        return new BufferedReader(new InputStreamReader(inputStream));
    }
    
    // Lê o conteúdo de um arquivo e retorna como uma string
    public static String readFile(File file) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            StringBuilder res = new StringBuilder();
            String row;
            if (file.exists())
                while ((row = reader.readLine()) != null)
                    res.append(row).append("\n");
            reader.close();
            return res.toString();
        } catch (IOException e) {
            System.out.println("Erro no método readFile do Leitor de Arquivos");
            e.printStackTrace();
        }
        return "";
    }
    
    // Escreve conteúdo em um arquivo, criando-o se não existir
    public static void writeFile(File file, String content) {
        try {
            if (file.exists() || file.createNewFile()) {
                FileWriter writer = new FileWriter(file);
                writer.write(content);
                writer.close();
            }
        } catch (IOException e) {
            System.out.println("Erro no método writeFile do Leitor de Arquivos");
            e.printStackTrace();
        }
    }
    
}

package mic.simulador;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import mic.simulador.backend.CPU;
import mic.simulador.backend.Compilador;
import mic.simulador.backend.CompiladorException;
import mic.simulador.backend.MemoriaLinha;

public class MIC1Frontend extends Application{
    // Instâncias do CPU e do Compilador
    private final CPU cpu = new CPU();
    private final Compilador comp = Compilador.getInstance(); 
    private TextArea codeInput;  
    
    @Override
    public void start(Stage primaryStage) {
        // Configuração da interface gráfica
        BorderPane root = new BorderPane();
    
        // Área de texto para inserir o código assembly
        codeInput = new TextArea();  // Atribui à variável de instância
        codeInput.setPromptText("Digite o código assembly");
    
        // Botões para interagir com a aplicação
        Button btnCarregar = new Button("Carregar Código");
        Button btnExecutar = new Button("Executar");
        Button btnPasso = new Button("Passo a Passo");
        Button btnReiniciar = new Button("Reiniciar");
    
        // Controles com os botões
        HBox controles = new HBox(5, btnCarregar, btnExecutar, btnPasso, btnReiniciar);
        controles.setPadding(new Insets(10));
    
        // Tabela para exibir os registradores
        TableView<RegisterDisplay> tabelaRegistradores = new TableView<>();
        tabelaRegistradores.setPrefHeight(200);
        tabelaRegistradores.setPlaceholder(new Label("Nenhum registrador carregado ainda..."));
    
        // Colunas da tabela de registradores
        TableColumn<RegisterDisplay, String> colNomeReg = new TableColumn<>("Registrador");
        colNomeReg.setCellValueFactory(cell -> cell.getValue().nameProperty());
    
        TableColumn<RegisterDisplay, Short> colValorReg = new TableColumn<>("Valor"); // mudei o tipo para short
        colValorReg.setCellValueFactory(cell -> cell.getValue().valueProperty());
    
        tabelaRegistradores.getColumns().addAll(colNomeReg, colValorReg);
    
        // Área de texto para exibir mensagens de erro
        TextArea logs = new TextArea();
        logs.setEditable(false);
        logs.setPrefHeight(100);
    
        // Tabela para exibir a memória
        TableView<MemoryRow> tabelaMemoria = new TableView<>();
        tabelaMemoria.setPrefHeight(200);
        tabelaMemoria.setPlaceholder(new Label("Nenhuma memória carregada ainda..."));
    
        // Colunas da tabela de memória
        TableColumn<MemoryRow, Integer> colEndereco = new TableColumn<>("Endereço");
        colEndereco.setCellValueFactory(cell -> cell.getValue().addressProperty().asObject());
    
        TableColumn<MemoryRow, Short> colValorMemoria = new TableColumn<>("Valor");
        colValorMemoria.setCellValueFactory(cell -> cell.getValue().valueProperty());
    
        tabelaMemoria.getColumns().addAll(colEndereco, colValorMemoria);
    
        // VBox para exibir o estado da CPU
        VBox estadosCpu = new VBox(5);
        estadosCpu.setPadding(new Insets(10));
        Label pcLabel = new Label("PC: 0");
        Label irLabel = new Label("IR: 0");
        Label flagsLabel = new Label("Flags (N/Z): false / false");
    
        estadosCpu.getChildren().addAll(pcLabel, irLabel, flagsLabel);
    
        // Ações para os botões
        btnCarregar.setOnAction(e -> {
            String code = codeInput.getText();
            try {
                cpu.setCPUInitial(); // Reinicia o processador
                short[] machineCode = comp.parseCode(code); // Converte o código assembly para código de máquina
                logs.appendText("Código carregado com sucesso!\n");
            } catch (CompiladorException ex) {
                logs.appendText("Erro ao carregar o código: " + ex.getMessage());
            }
        });
    
        btnExecutar.setOnAction(e -> {
            logs.appendText("Executando o código...\n");
            cpu.runCycle(); // Executa um ciclo de instrução da CPU
    
            // Atualiza as tabelas e o estado da CPU
            updateRegistersTable(tabelaRegistradores);
            updateMemoryTable(tabelaMemoria);
            updateCPUState(pcLabel, irLabel, flagsLabel);
        });
    
        btnPasso.setOnAction(e -> {
            logs.appendText("Executando um passo...\n");
            cpu.runSubCycle(); // Executa um passo da CPU
    
            // Atualiza as tabelas e o estado da CPU
            updateRegistersTable(tabelaRegistradores);
            updateMemoryTable(tabelaMemoria);
            updateCPUState(pcLabel, irLabel, flagsLabel);
        });
    
        btnReiniciar.setOnAction(e -> {
            logs.clear(); // Limpa os logs
            logs.appendText("Reiniciando o processador...\n");
            cpu.setCPUInitial(); // Reinicia o processador
    
            // Atualiza as tabelas e o estado da CPU
            updateRegistersTable(tabelaRegistradores);
            updateMemoryTable(tabelaMemoria);
            updateCPUState(pcLabel, irLabel, flagsLabel);
        });
    
        // Layout central que exibe os registradores
        VBox layoutCentral = new VBox(10, new Label("Registradores"), tabelaRegistradores);
        layoutCentral.setPadding(new Insets(10));
    
        root.setTop(codeInput); // Define a área de entrada de código no topo
        root.setBottom(new VBox(controles, estadosCpu, logs)); // Define os controles, estado da CPU e logs na parte inferior
        root.setCenter(layoutCentral); // Define a tabela de registradores no centro
    
        // Janela
        Scene scene = new Scene(root, 900, 600);
        primaryStage.setScene(scene);
        primaryStage.setTitle("MIC-1");
        primaryStage.show();
    }
    
    // Atualiza a tabela de registradores
    private void updateRegistersTable(TableView<RegisterDisplay> table) {
        table.getItems().clear(); // Limpa a tabela
        cpu.getRegisters().forEach(register -> {
            table.getItems().add(new RegisterDisplay(register.getName(), register.getValue())); // Adiciona os registradores
        });
    }
    
    // Atualiza a tabela de memória
    private void updateMemoryTable(TableView<MemoryRow> table) {
        table.getItems().clear(); // Limpa a tabela
        // Adiciona as linhas de memória na tabela
        for (MemoriaLinha MemoriaLinha : cpu.getMemory().getMemory()) {
            table.getItems().add(new MemoryRow(MemoriaLinha.getAddress(), MemoriaLinha.getValue()));
        }
    }
    
    // Atualiza o estado da CPU
    private void updateCPUState(Label pcLabel, Label irLabel, Label flagsLabel) {
        pcLabel.setText("PC: " + cpu.MPCProperty().get()); // Atualiza o contador de programa (PC)
        irLabel.setText("IR: " + cpu.MIRProperty().get()); // Atualiza o registrador de instrução (IR)
        flagsLabel.setText("Flags (N/Z): " + cpu.getRegisters().get(0).getValue() + " / " + cpu.getRegisters().get(1).getValue()); // Atualiza as flags (N/Z)
    }
    
    public static void main(String[] args) {
        launch(args); // Inicia a aplicação
    }
}

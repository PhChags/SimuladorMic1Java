package mic.simulador.backend;

// Classe para mensagens de erro relacionadas a compilação/tradução
public class CompiladorException extends Exception{
    public CompiladorException(String message){
        super(message);
    }
}

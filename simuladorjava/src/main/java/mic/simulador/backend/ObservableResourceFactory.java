package mic.simulador.backend;

import javafx.beans.binding.StringBinding;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

import java.util.Locale;
import java.util.ResourceBundle;

public class ObservableResourceFactory {
    // Instância única da fábrica (padrão Singleton)
    private static ObservableResourceFactory instance;
    // Propriedade que armazena o ResourceBundle (conjunto de traduções)
    private final ObjectProperty<ResourceBundle> resources;
    
    // Retorna a instância única da classe (Singleton)
    public static ObservableResourceFactory getInstance() {
        // Se a instância ainda não foi criada, cria uma nova
        if (instance == null) instance = new ObservableResourceFactory();
        return instance;
    }
    
    // Construtor privado para inicializar o ResourceBundle
    private ObservableResourceFactory() {
        resources = new SimpleObjectProperty<>();
        // Define o ResourceBundle com as traduções em inglês (Estados Unidos)
        setResources(ResourceBundle.getBundle("Translation", new Locale("en", "US")));
    }
    
    // Retorna o ResourceBundle atual
    public ResourceBundle getResources() {
        return resources.get();
    }
    
    // Retorna a propriedade que representa o ResourceBundle, permitindo a vinculação de dados
    public ObjectProperty<ResourceBundle> resourcesProperty() {
        return resources;
    }
    
    // Define um novo ResourceBundle
    public void setResources(ResourceBundle resources) {
        this.resources.set(resources);
    }
    
    // Retorna um StringBinding que será atualizado automaticamente quando o ResourceBundle mudar
    // O StringBinding obtém o valor da chave (key) no ResourceBundle
    public StringBinding getStringBinding(String key) {
        return new StringBinding() {
            // Vincula o StringBinding à propriedade resources (para atualizar automaticamente)
            { bind(resourcesProperty()); }
            
            // Método que retorna o valor associado à chave no ResourceBundle
            @Override
            public String computeValue() {
                return getResources().getString(key);
            }
        };
    }
    
}

# Simulador-MIC-1
Uma aplicação voltada à simulação do funcionamento de um processador MIC-1, arquitetura descrita por Andrew S. Tanenbaum em Structured Computer Organization, livro de sua autoria. 
Desenvolvido apenas para fins educacionais por graduandos em Ciência da Computação na Universidade Federal Fluminense (Brasil, Rio de Janeiro).

## Como rodar

Para rodar o Simulador-MIC-1, siga os passos abaixo:

### 1. Instale o Java e o JavaFX

Certifique-se de ter o **JDK 22** ou versão superior e o **JavaFX SDK 23.0.1** ou versão mais recente instalados na sua máquina.

- **Java**: Baixe e instale o JDK a partir de [Oracle](https://www.oracle.com/java/technologies/javase-jdk22-downloads.html).
- **JavaFX**: Baixe o JavaFX SDK em [openjfx.io](https://openjfx.io/).

### 2. Defina as variáveis de ambiente

- **JAVA_HOME**: Aponte para o diretório onde o JDK está instalado.
  - Exemplo: `C:\Program Files\Java\jdk-22`
- **JAVA_FX_HOME**: Aponte para o diretório onde o JavaFX SDK foi extraído.
  - Exemplo: `C:\JavaFX\javafx-sdk-23.0.1`

### 3. Compile o projeto

Abra o terminal no diretório do projeto e execute o seguinte comando para compilar o projeto:

```bash
mvn clean install

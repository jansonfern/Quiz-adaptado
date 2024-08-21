package fernandes.ifpr;

import java.util.ArrayList;
import java.util.List;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * JavaFX App
 */
public class App extends Application {

    private ControladorQuiz controladorQuiz;
    private VBox root;
    private Scene cena;
    private Text enunciado;
    private Button alternativa1;
    private Button alternativa2;
    private Button alternativa3;
    private Button alternativa4;
    private Button alternativa5;
    private Text resultado;
    private Button proxima;
    private Button reiniciar;
    private Button iniciarJogo; // Novo botão para iniciar o jogo
    private boolean respostaDada = false; 

    @Override
    public void init() throws Exception {
        super.init();

        ArrayList<Questao> lista = new ArrayList<>();

        lista.add(new Questao("Qual é a capital de São Paulo?", "São Paulo",
                new String[] { "São Paulo", "Rio de Janeiro", "Brasília", "Belo Horizonte", "Curitiba" }));
        lista.add(new Questao("Qual é a capital do Paraná?", "Curitiba",
                new String[] { "São Paulo", "Curitiba", "Rio de Janeiro", "Belo Horizonte", "Brasília" }));
        lista.add(new Questao("Qual é a capital do Rio de Janeiro?", "Rio de Janeiro",
                new String[] { "São Paulo", "Curitiba", "Rio de Janeiro", "Belo Horizonte", "Brasília" }));
        lista.add(new Questao("Qual é a capital de Minas Gerais?", "Belo Horizonte",
                new String[] { "São Paulo", "Curitiba", "Rio de Janeiro", "Belo Horizonte", "Brasília" }));
        lista.add(new Questao("Qual é a capital do Brasil?", "Brasília",
                new String[] { "São Paulo", "Curitiba", "Rio de Janeiro", "Belo Horizonte", "Brasília" }));

        controladorQuiz = new ControladorQuiz(lista);
    }

    @Override
    public void start(Stage stage) throws Exception {
        inicializaComponentes();
        cena = new Scene(root, 500, 500);
        cena.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
        stage.setScene(cena);
        stage.show();
    }

    private void inicializaComponentes() {
        root = new VBox();
        root.setAlignment(Pos.CENTER);
        root.setSpacing(10.0);
        root.setPrefSize(500, 500); // Definindo o tamanho preferido para o VBox

        iniciarJogo = new Button("Iniciar Jogo");
        iniciarJogo.setPrefWidth(200);
        iniciarJogo.setOnAction(iniciarJogo());

        enunciado = new Text("Enunciado");
        enunciado.getStyleClass().add("enunciado");
        enunciado.setVisible(false);

        alternativa1 = new Button("Questão 1");
        alternativa2 = new Button("Questão 2");
        alternativa3 = new Button("Questão 3");
        alternativa4 = new Button("Questão 4");
        alternativa5 = new Button("Questão 5");

        alternativa1.setPrefWidth(200);
        alternativa2.setPrefWidth(200);
        alternativa3.setPrefWidth(200);
        alternativa4.setPrefWidth(200);
        alternativa5.setPrefWidth(200);

        alternativa1.getStyleClass().add("botao");
        alternativa1.setTooltip(new Tooltip("Clique para responder..."));
        alternativa2.getStyleClass().add("botao");
        alternativa3.getStyleClass().add("botao");
        alternativa4.getStyleClass().add("botao");
        alternativa5.getStyleClass().add("botao");

        // Inicialmente escondemos os botões de resposta
        alternativa1.setVisible(false);
        alternativa2.setVisible(false);
        alternativa3.setVisible(false);
        alternativa4.setVisible(false);
        alternativa5.setVisible(false);

        alternativa1.setOnAction(respondeQuestao());
        alternativa2.setOnAction(respondeQuestao());
        alternativa3.setOnAction(respondeQuestao());
        alternativa4.setOnAction(respondeQuestao());
        alternativa5.setOnAction(respondeQuestao());

        resultado = new Text("Resultado");
        proxima = new Button("Próxima");
        reiniciar = new Button("Reiniciar");

        resultado.setVisible(false);
        proxima.setVisible(false);
        reiniciar.setVisible(false);

        proxima.setOnAction(proximaQuestao());
        reiniciar.setOnAction(reiniciarQuiz());

        root.getChildren().addAll(iniciarJogo, enunciado, alternativa1, alternativa2, alternativa3, alternativa4, alternativa5, resultado, proxima, reiniciar);
    }

    private EventHandler<ActionEvent> iniciarJogo() {
        return new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                iniciarJogo.setVisible(false);
                enunciado.setVisible(true);
                alternativa1.setVisible(true);
                alternativa2.setVisible(true);
                alternativa3.setVisible(true);
                alternativa4.setVisible(true);
                alternativa5.setVisible(true);
                resultado.setVisible(true);
                proxima.setVisible(false);
                reiniciar.setVisible(false);
                atualizaComponentes();
            }
        };
    }

    private EventHandler<ActionEvent> reiniciarQuiz() {
        return new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                controladorQuiz.reiniciar();
                respostaDada = false; 
                enunciado.setVisible(false);
                alternativa1.setVisible(false);
                alternativa2.setVisible(false);
                alternativa3.setVisible(false);
                alternativa4.setVisible(false);
                alternativa5.setVisible(false);
                resultado.setVisible(false);
                proxima.setVisible(false); 
                reiniciar.setVisible(false);
                iniciarJogo.setVisible(true); // Volta a mostrar o botão iniciar jogo
            }
        };
    }

    public void atualizaComponentes() {
        Questao objQuestao = controladorQuiz.getQuestao();
        if (objQuestao != null) {
            List<String> questoes = objQuestao.getTodasAlternativas(); 

            enunciado.setText(objQuestao.getEnunciado());
            alternativa1.setText(questoes.get(0));
            alternativa2.setText(questoes.get(1));
            alternativa3.setText(questoes.get(2));
            alternativa4.setText(questoes.get(3));
            alternativa5.setText(questoes.get(4));

            resultado.setVisible(false);
            proxima.setVisible(respostaDada); 
            reiniciar.setVisible(false);
        }
    }

    private EventHandler<ActionEvent> respondeQuestao() {
        return new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Button clicado = (Button) event.getSource();
                String alternativa = clicado.getText();

                boolean result = controladorQuiz.respondeQuestao(alternativa);

                if (result) {
                    resultado.setText("Acertou!!");
                } else {
                    resultado.setText("Errou!!!");
                }

                resultado.setVisible(true);
                respostaDada = true;
                proxima.setVisible(true);
            }
        };
    }

    private EventHandler<ActionEvent> proximaQuestao() {
        return new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (controladorQuiz.temProximaQuestao()) {
                    controladorQuiz.proximaQuestao();
                    atualizaComponentes();
                } else {
                    resultado.setVisible(true);
                    int acertos = controladorQuiz.getAcertos();
                    int erros = controladorQuiz.getErros();
                    if (acertos >= 3) {
                        resultado.setText("Fim, você ganhou! Acertos: " + acertos + ", Erros: " + erros);
                    } else {
                        resultado.setText("Fim, você perdeu. Acertos: " + acertos + ", Erros: " + erros);
                    }
                    proxima.setVisible(false);
                    reiniciar.setVisible(true);
                }
            }
        };
    }

    public static void main(String[] args) {
        launch(args);
    }
}

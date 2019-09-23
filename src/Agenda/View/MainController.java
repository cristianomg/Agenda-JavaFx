package Agenda.View;

import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import Agenda.Entities.Contato;
import Agenda.Repository.AgendaRepository;
import Agenda.Repository.ContatoRepository;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;

public class MainController implements Initializable {
	@FXML
	private TableView<Contato> tabelaContatos;
	@FXML
	private Button botaoInserir;
	@FXML
	private Button botaoAtualizar;
	@FXML
	private Button botaoExcluir;
	@FXML
	private Button botaoCancelar;
	@FXML
	private Button botaoSalvar;
	@FXML
	private TextField txfNome;
	@FXML
	private TextField txfIdade;
	@FXML
	private TextField txfTelefone;
	private boolean ehInserir;
	private Contato contatoSelecionado;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		tabelaContatos.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		tabelaContatos.getSelectionModel().selectedItemProperty()
				.addListener((observador, contatoAntigo, contatoNovo) -> {
					if (contatoNovo != null) {
						txfNome.setText(contatoNovo.getNome());
						txfIdade.setText(String.valueOf(contatoNovo.getIdade()));
						txfTelefone.setText(contatoNovo.getTelefone());
						this.contatoSelecionado = contatoNovo;
					}
				});
		carregarTabela();
		habilitarBotoes(false);
	}

	private void carregarTabela() {
		AgendaRepository<Contato> repositorioContatos = ContatoRepository.getInstance();
		List<Contato> contatos = repositorioContatos.getLista();
		ObservableList<Contato> contatosObservados = FXCollections.observableList(contatos);
		this.tabelaContatos.getItems().setAll(contatosObservados);
	}

	private void habilitarBotoes(boolean habilitar) {
		txfNome.setDisable(!habilitar);
		txfIdade.setDisable(!habilitar);
		txfTelefone.setDisable(!habilitar);
		botaoInserir.setDisable(habilitar);
		botaoExcluir.setDisable(habilitar);
		botaoAtualizar.setDisable(habilitar);
		botaoCancelar.setDisable(!habilitar);
		botaoSalvar.setDisable(!habilitar);
	}

	public void botaoInserir_Action() {
		ehInserir = true;
		habilitarBotoes(true);
		txfNome.setText("");
		txfIdade.setText("");
		txfTelefone.setText("");
	}

	public void botaoAtualizar_Action() {
		ehInserir = false;
		habilitarBotoes(true);
		txfNome.setDisable(true);
	}

	public void botaoRemover_Action() {
		Alert confirmacao = new Alert(AlertType.CONFIRMATION);
		confirmacao.setTitle("Confirmação");
		confirmacao.setHeaderText("Confirmação de exclução do contato");
		confirmacao.setContentText("Tem certeza deseja excluir o contato?");
		Optional<ButtonType> resultadoConfirmacao = confirmacao.showAndWait();
		if (resultadoConfirmacao.isPresent() && resultadoConfirmacao.get() == ButtonType.OK) {
			AgendaRepository<Contato> repositorioContatos = ContatoRepository.getInstance();
			repositorioContatos.excluir(this.contatoSelecionado);
			carregarTabela();
			this.tabelaContatos.getSelectionModel().selectFirst();
		}
	}

	public void botaoSalvar_Action() {
		AgendaRepository<Contato> contatos = ContatoRepository.getInstance();
		Contato contato = new Contato();
		contato.setNome(txfNome.getText());
		contato.setIdade(Integer.parseInt(txfIdade.getText()));
		contato.setTelefone(txfTelefone.getText());
		if (ehInserir) {
			contatos.inserir(contato);
		} else {
			contatos.atualizar(contato);
		}
		habilitarBotoes(false);
		carregarTabela();
		this.tabelaContatos.getSelectionModel().selectFirst();
	}

	public void botaoCancelar_Action() {
		habilitarBotoes(false);
		ehInserir = false;
		carregarTabela();
		this.tabelaContatos.getSelectionModel().selectFirst();
	}

}

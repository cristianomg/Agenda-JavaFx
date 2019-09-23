package Agenda.Repository;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import Agenda.Entities.Contato;

public class ContatoRepository implements AgendaRepository<Contato> {
	private static ContatoRepository uniqueInstance;
	private List<Contato> contatos;

	private ContatoRepository() {
		this.contatos = this.load();
	}

	public static synchronized ContatoRepository getInstance() {

		if (uniqueInstance == null) {
			uniqueInstance = new ContatoRepository();
		}
		return uniqueInstance;
	}

	@Override
	public List<Contato> getLista() {
		return contatos;
	}

	@Override
	public void inserir(Contato entitie) {
		contatos.add(entitie);
		this.save();
	}

	@Override
	public void atualizar(Contato entitie) {
		var original = contatos.stream().filter(contato -> contato.getNome().equals(entitie.getNome())).findFirst();
		if (original.isPresent()) {
			original.get().setIdade(entitie.getIdade());
			original.get().setTelefone(entitie.getTelefone());
			this.save();
		}
	}

	@Override
	public void excluir(Contato entitie) {
		contatos.remove(entitie);
		this.save();

	}

	@Override
	public void save() {
		try {
			FileOutputStream out = new FileOutputStream("contatos");
			ObjectOutputStream objOut = new ObjectOutputStream(out);

			objOut.writeObject(this.contatos);
			objOut.close();
		} catch (FileNotFoundException e) {
			System.out.println(e.getMessage());
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Contato> load() {
		if (new File("contatos").canRead() == true) {
			try {
				FileInputStream input = new FileInputStream("contatos");
				ObjectInputStream objIn = new ObjectInputStream(input);
				List<Contato> contatos = (List<Contato>) objIn.readObject();
				objIn.close();
				return contatos;
			} catch (FileNotFoundException e) {
				System.out.println(e.getMessage());
			} catch (ClassNotFoundException e) {
				System.out.println(e.getMessage());
			} catch (IOException e) {
				System.out.println(e.getMessage());
			}
		}
		return new ArrayList<Contato>();

	}

}

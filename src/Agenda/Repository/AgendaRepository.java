package Agenda.Repository;

import java.util.List;

public interface AgendaRepository <T>{
	public List<T> getLista();
	public void inserir(T entitie);
	public void atualizar(T entitie);
	public void excluir(T entitie);
	public void save();
	public List<T> load();
	
}

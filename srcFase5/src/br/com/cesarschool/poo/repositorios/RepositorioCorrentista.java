package br.com.cesarschool.poo.repositorios;

import br.com.cesarschool.poo.entidades.Correntista;
import br.com.cesarschool.poo.excecoes.ExcecaoObjetoJaExistente;
import br.com.cesarschool.poo.excecoes.ExcecaoObjetoNaoExistente;

public class RepositorioCorrentista extends RepositorioGenerico {
	private static final int TAMANHO_MAX_CORRENTISTAS = 1000;
	private static RepositorioCorrentista instancia;
	
	private RepositorioCorrentista() {		
	}
	
	public static RepositorioCorrentista getInstancia() {
		if (instancia == null) {
			instancia = new RepositorioCorrentista();
		}
		return instancia; 
	}
	
	public void incluir(Correntista correntista) {
		try{
			super.incluir(correntista);
		}catch(ExcecaoObjetoJaExistente e){
			e.printStackTrace();
		}
	}
	public void alterar(Correntista correntista) throws ExcecaoObjetoNaoExistente {
		try{
			super.alterar(correntista);
		}catch(ExcecaoObjetoNaoExistente e){
			e.printStackTrace();
		}
	}
	
	public Correntista buscar(String cpf) {
		return (Correntista)super.buscar(cpf);
	}
	
	public void excluir(String cpf) throws ExcecaoObjetoNaoExistente {
		try{
			super.excluir(cpf);
		}catch(ExcecaoObjetoNaoExistente e){
			e.printStackTrace();
		}
	}
	
	public Correntista[] buscarTodos() {
		return (Correntista[])super.buscarTodos();
	}

	@Override
	public int getTamanhoMaximoRepositorio() {
		return TAMANHO_MAX_CORRENTISTAS;
	}

	@Override
	public String getNomeCadastro() {
		return null;
	}		
}
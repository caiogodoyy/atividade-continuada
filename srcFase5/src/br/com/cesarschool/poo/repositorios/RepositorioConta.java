package br.com.cesarschool.poo.repositorios;

import br.com.cesarschool.poo.entidades.Conta;
import br.com.cesarschool.poo.excecoes.ExcecaoObjetoJaExistente;
import br.com.cesarschool.poo.excecoes.ExcecaoObjetoNaoExistente;

public class RepositorioConta extends RepositorioGenerico {

	private static final int TAMANHO_MAX_CONTAS = 10000;
	private static RepositorioConta instancia = null;
	
	
	public RepositorioConta() {
	}
	
	public static RepositorioConta getInstancia() {
		if (instancia == null) {
			instancia = new RepositorioConta();
		}
		return instancia; 
	}
	
	public void incluir(Conta conta) throws ExcecaoObjetoJaExistente {
		try{
			super.incluir(conta);
		}catch(ExcecaoObjetoJaExistente e){
			e.printStackTrace();
		}
	}
	public void alterar(Conta conta) throws ExcecaoObjetoNaoExistente{
		try{
			super.alterar(conta);
		}catch(ExcecaoObjetoNaoExistente e){
			e.printStackTrace();
		}
	}
	
	public Conta buscar(long numero) {
		return (Conta)super.buscar("" + numero);
		
	}
	
	public void excluir(long numero) throws ExcecaoObjetoNaoExistente{
		 try{
			 super.excluir("" + numero);
		 }catch(ExcecaoObjetoNaoExistente e){
			 e.printStackTrace();
		 }
	}

	public Conta[] buscarTodos() {
		return (Conta[])super.buscarTodos();
	}

	@Override
	public int getTamanhoMaximoRepositorio() {
		return TAMANHO_MAX_CONTAS;
	}

	@Override
	public String getNomeCadastro() {
		return null;
	}
}
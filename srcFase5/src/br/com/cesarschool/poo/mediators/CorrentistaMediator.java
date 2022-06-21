package br.com.cesarschool.poo.mediators;

import br.com.cesarschool.poo.entidades.Correntista;
import br.com.cesarschool.poo.excecoes.ExcecaoObjetoJaExistente;
import br.com.cesarschool.poo.excecoes.ExcecaoObjetoNaoExistente;
import br.com.cesarschool.poo.repositorios.RepositorioCorrentista;
import br.com.cesarschool.poo.utils.Ordenador;

public class CorrentistaMediator {
	
	private static final String MSG_CORRENTISTA_NAO_INFORMADO = "Correntista não informado";
	private static final String MSG_CPF_INVALIDO = "CPF inválido";
	private static final String MSG_NOME_NAO_INFORMADO = "Nome n�o informado";
	private static final String MSG_CORRENTISTA_NAO_INCLUIDO = "Correntista já cadastrado ou repositório cheio";
	private static final String MSG_CORRENTISTA_NAO_ENCONTRADO = "Correntista não cadastrado";
	private static final int ZERO = 0;
	
	private RepositorioCorrentista repositorio = RepositorioCorrentista.getInstancia();
	
	public StatusValidacaoConta incluir(Correntista correntista) throws ExcecaoObjetoNaoExistente, ExcecaoObjetoJaExistente{
		StatusValidacaoConta status = validar(correntista);
		if (status.isValido()) {
			repositorio.incluir(correntista);
		}
		return status;
	}
	public StatusValidacaoConta alterar(Correntista correntista) throws ExcecaoObjetoNaoExistente{
		StatusValidacaoConta status = validar(correntista);
		if (status.isValido()) {
			try {
				repositorio.alterar(correntista);
			}catch(ExcecaoObjetoNaoExistente e){
				status.getCodigosErros()[0] = StatusValidacaoConta.CORRENTISTA_NAO_ENCONTRADO;
				throw new ExcecaoObjetoNaoExistente(MSG_CORRENTISTA_NAO_ENCONTRADO);
			}
		}
		return status;
	}
	public void excluir(String cpf) {
		try {
			repositorio.excluir(cpf);
		} catch (ExcecaoObjetoNaoExistente e) {
			e.printStackTrace();
		}
	}	
	public Correntista buscar(String cpf) {
		return repositorio.buscar(cpf);
	}
	
	private StatusValidacaoConta validar(Correntista correntista) throws ExcecaoObjetoNaoExistente{
		int[] codigoStatus = new int[StatusValidacaoConta.QTD_SITUACOES_EXCECAO];
		String[] mensagensStatus = new String[StatusValidacaoConta.QTD_SITUACOES_EXCECAO];
		int contErros = 0;
		if (correntista == null) {
			throw new ExcecaoObjetoNaoExistente(MSG_CORRENTISTA_NAO_INFORMADO);		
		} else {
			if (StatusValidacaoCPF.isCPF(correntista.getCpf()) != true) {
				throw new ExcecaoObjetoNaoExistente(MSG_CPF_INVALIDO);	
			} 
			if (correntista.getNome() == null) {
				throw new ExcecaoObjetoNaoExistente(MSG_NOME_NAO_INFORMADO);			
			}			
		}	
		return new StatusValidacaoConta(codigoStatus, mensagensStatus, contErros == ZERO);		
	}
	
	public Correntista[] consultarTodosOrdenadoPorNome() {
		Correntista[] todos = repositorio.buscarTodos();
		if (todos != null && todos.length > 0) {
			ordenarCorrentistaPorNome(todos);
		}
		return todos;
	}
	private void ordenarCorrentistaPorNome(Correntista[] correntistas) {
		Ordenador.ordenar(correntistas);
	}
}
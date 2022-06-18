package br.com.cesarschool.poo.mediators;

import br.com.cesarschool.poo.entidades.Correntista;
import br.com.cesarschool.poo.repositorios.RepositorioCorrentista;

public class CorrentistaMediator {
	
	private static final String MSG_CORRENTISTA_NAO_INFORMADO = "Correntista não informado";
	private static final String MSG_CPF_INVALIDO = "CPF inválido";
	private static final String MSG_NOME_NAO_INFORMADO = "Nome não informado";
	private static final String MSG_CORRENTISTA_NAO_INCLUIDO = "Correntista já cadastrado ou repositório cheio";
	private static final String MSG_CORRENTISTA_NAO_ENCONTRADO = "Correntista não cadastrado";
	private static final int ZERO = 0;
	
	private RepositorioCorrentista repositorio = new RepositorioCorrentista();
	
	public StatusValidacaoCorrentista incluir(Correntista correntista) {
		StatusValidacaoCorrentista status = validar(correntista);
		if (status.isValido()) {
			boolean retornoRepositorio = repositorio.incluir(correntista);
			if (!retornoRepositorio) {
				status.getCodigosErros()[0] = StatusValidacaoCorrentista.CORRENTISTA_NAO_INCLUIDO;
				status.getMensagens()[0] = MSG_CORRENTISTA_NAO_INCLUIDO;
				status.setValido(false);
			}
		}
		return status;
	}
	public StatusValidacaoCorrentista alterar(Correntista correntista) {
		StatusValidacaoCorrentista status = validar(correntista);
		if (status.isValido()) {
			boolean retornoRepositorio = repositorio.alterar(correntista);
			if (!retornoRepositorio) {
				status.getCodigosErros()[0] = StatusValidacaoCorrentista.CORRENTISTA_NAO_ENCONTRADO;
				status.getMensagens()[0] = MSG_CORRENTISTA_NAO_ENCONTRADO;
				status.setValido(false);
			}
		}
		return status;
	}
	public boolean excluir(String cpf) {
		return repositorio.excluir(cpf);
	}	
	public Correntista buscar(String cpf) {
		return repositorio.buscar(cpf);
	}
	
	private StatusValidacaoCorrentista validar(Correntista correntista) {
		int[] codigoStatus = new int[StatusValidacaoCorrentista.QTD_SITUACOES_EXCECAO];
		String[] mensagensStatus = new String[StatusValidacaoCorrentista.QTD_SITUACOES_EXCECAO];
		int contErros = 0;
		if (correntista == null) {
			codigoStatus[contErros++] = StatusValidacaoCorrentista.CORRENTISTA_NAO_INFORMADO;
			mensagensStatus[contErros] = MSG_CORRENTISTA_NAO_INFORMADO;			
		} else {
			if (StatusValidacaoCPF.isCPF(correntista.getCpf()) != true) {
				codigoStatus[contErros++] = StatusValidacaoCorrentista.CPF_INVALIDO;
				mensagensStatus[contErros] = MSG_CPF_INVALIDO;
			} 
			if (correntista.getNome() == null) {
				codigoStatus[contErros++] = StatusValidacaoCorrentista.NOME_NAO_INFORMADO;
				mensagensStatus[contErros] = MSG_NOME_NAO_INFORMADO;				
			}			
		}		
		return new StatusValidacaoCorrentista(codigoStatus, mensagensStatus, contErros == ZERO);		
	}
}

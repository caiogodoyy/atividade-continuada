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
	
	public StatusValidacaoConta incluir(Correntista correntista) {
		StatusValidacaoConta status = validar(correntista);
		if (status.isValido()) {
			boolean retornoRepositorio = repositorio.incluir(correntista);
			if (!retornoRepositorio) {
				status.getCodigosErros()[0] = StatusValidacaoConta.CORRENTISTA_NAO_INCLUIDO;
				status.getMensagens()[0] = MSG_CORRENTISTA_NAO_INCLUIDO;
				status.setValido(false);
			}
		}
		return status;
	}
	public StatusValidacaoConta alterar(Correntista correntista) {
		StatusValidacaoConta status = validar(correntista);
		if (status.isValido()) {
			boolean retornoRepositorio = repositorio.alterar(correntista);
			if (!retornoRepositorio) {
				status.getCodigosErros()[0] = StatusValidacaoConta.CORRENTISTA_NAO_ENCONTRADO;
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
	
	private StatusValidacaoConta validar(Correntista correntista) {
		int[] codigoStatus = new int[StatusValidacaoConta.QTD_SITUACOES_EXCECAO];
		String[] mensagensStatus = new String[StatusValidacaoConta.QTD_SITUACOES_EXCECAO];
		int contErros = 0;
		if (correntista == null) {
			codigoStatus[contErros++] = StatusValidacaoConta.CORRENTISTA_NAO_INFORMADO;
			mensagensStatus[contErros] = MSG_CORRENTISTA_NAO_INFORMADO;			
		} else {
			if (StatusValidacaoCPF.isCPF(correntista.getCpf()) != true) {
				codigoStatus[contErros++] = StatusValidacaoConta.CPF_INVALIDO;
				mensagensStatus[contErros] = MSG_CPF_INVALIDO;
			} 
			if (correntista.getNome() == null) {
				codigoStatus[contErros++] = StatusValidacaoConta.NOME_NAO_INFORMADO;
				mensagensStatus[contErros] = MSG_NOME_NAO_INFORMADO;				
			}			
		}		
		return new StatusValidacaoConta(codigoStatus, mensagensStatus, contErros == ZERO);		
	}
}

package br.com.cesarschool.poo.mediators;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import br.com.cesarschool.poo.entidades.Conta;
import br.com.cesarschool.poo.entidades.ContaPoupanca;
import br.com.cesarschool.poo.entidades.Correntista;
import br.com.cesarschool.poo.entidades.StatusConta;
import br.com.cesarschool.poo.repositorios.RepositorioConta;
import br.com.cesarschool.poo.utils.Ordenador;
import br.com.cesarschool.poo.utils.conta.ComparadorContaDataAbertura;
import br.com.cesarschool.poo.utils.conta.ComparadorContaSaldo;

public class ContaMediator {
	
	public static final int ESCORE_NAO_CALCULADO = -1;
	public static final int ESCORE_ZERO = 0;
	public static final int BRONZE = 1;
	public static final int PRATA = 2;
	public static final int OURO = 3;
	public static final int DIAMANTE = 4;
	
	private static final String MSG_CONTA_NAO_INFORMADA = "Conta não informada"; 
	private static final String MSG_NUMERO_MENOR_IGUAL_A_ZERO = "Número da conta negativo ou zero"; 
	private static final String MSG_STATUS_NAO_INFORMADO = "Status não informado"; 
	private static final String MSG_DATA_ABERTURA_NAO_INFORMADA = "Data de abertura não informada";
	private static final String MSG_DATA_ABERTURA_MAIOR_DATA_ATUAL = "Data de abertura menor que a data atual";
	private static final String MSG_DATA_ABERTURA_MENOR_UM_MES = "Data de abertura muito antiga";
	private static final String MSG_CREDITO_NP_CONTA_ENCERRADA = "Crédito não permitido: conta encerrada";
	private static final String MSG_VALOR_CREDITO_INVALIDO = "Valor de crédito inválido";
	private static final String MSG_DEBITO_NP_CONTA_BLOQUEADA = "Débito não permitido: conta bloqueada";
	private static final String MSG_VALOR_DEBITO_INVALIDO = "Valor de dédito inválido";
	private static final String MSG_CONTA_NAO_INCLUIDA = "Conta já cadastrada ou repositório cheio";
	private static final String MSG_CONTA_NAO_ENCONTRADA = "Conta não cadastrada";
	private static final String MSG_CORRENTISTA_NAO_ENCONTRADO = "Correntista não cadastrado";
	private static final String MSG_TAXA_JUROS_MENOR_IGUAL_A_ZERO = "Taxa de Juros da conta negativo ou zero";
	private static final int TRINTA_DIAS = 30;
	private static final int LIMITE_BRONZE = 5800;
	private static final int LIMITE_PRATA = 13000;
	private static final int LIMITE_OURO = 39000;
	private static final int ZERO = 0;
	private static final ComparadorContaDataAbertura COMP_PRODUTO_POR_DATA_ABERTURA = new ComparadorContaDataAbertura();
	private static final ComparadorContaSaldo COMP_PRODUTO_POR_SALDO = new ComparadorContaSaldo();
	
	private RepositorioConta repositorio = RepositorioConta.getInstancia();
	private CorrentistaMediator correntistaMediator = new CorrentistaMediator();
	
	public StatusValidacaoConta incluir(Conta conta) {
		StatusValidacaoConta status = validar(conta);
		if (status.isValido()) {
			boolean retornoRepositorio = repositorio.incluir(conta);
			if (!retornoRepositorio) {
				status.getCodigosErros()[0] = StatusValidacaoConta.CONTA_NAO_INCLUIDA;
				status.getMensagens()[0] = MSG_CONTA_NAO_INCLUIDA;
				status.setValido(false);
			}
			else {
				status = correntistaMediator.incluir(conta.getCorrentista());
			}
		}
		return status;
	}
	public StatusValidacaoConta alterar(Conta conta) {
		StatusValidacaoConta status = validar(conta);
		if (status.isValido()) {
			boolean retornoRepositorio = repositorio.alterar(conta);
			if (!retornoRepositorio) {
				status.getCodigosErros()[0] = StatusValidacaoConta.CONTA_NAO_ENCONTRADA;
				status.getMensagens()[0] = MSG_CONTA_NAO_ENCONTRADA;
				status.setValido(false);
			}
			else {
				status = correntistaMediator.alterar(conta.getCorrentista());
			}
		}
		return status;
	}
	public boolean excluir(long numero) {
		return repositorio.excluir(numero);
	}	
	public Conta buscar(long numero) {
		return repositorio.buscar(numero);
	}
	
	public StatusValidacaoConta creditar(long numero, double valor) {
		int[] codigoStatus = new int[StatusValidacaoConta.QTD_SITUACOES_EXCECAO]; 
		String[] mensagensStatus = new String[StatusValidacaoConta.QTD_SITUACOES_EXCECAO];
		int contErros = 0;
		Conta conta = buscar(numero);
		if (valor <= 0) {
			codigoStatus[contErros++] = StatusValidacaoConta.VALOR_CREDITO_INVALIDO;
			mensagensStatus[contErros] = MSG_VALOR_CREDITO_INVALIDO;						
		}		
		if (conta == null) {
			codigoStatus[contErros++] = StatusValidacaoConta.CONTA_NAO_ENCONTRADA;
			mensagensStatus[contErros] = MSG_CONTA_NAO_ENCONTRADA;			
		} else if (StatusConta.ENCERRADA.equals(conta.getStatus())) {
			codigoStatus[contErros++] = StatusValidacaoConta.CREDITO_NP_CONTA_ENCERRADA;
			mensagensStatus[contErros] = MSG_CREDITO_NP_CONTA_ENCERRADA;							
		}
		StatusValidacaoConta statusVal = new StatusValidacaoConta(codigoStatus, mensagensStatus, contErros == ZERO);	
		if (statusVal.isValido()) {
			if (conta instanceof ContaPoupanca) {
				ContaPoupanca contaPoupanca = (ContaPoupanca)conta;
				valor = (1 + contaPoupanca.getTaxaJuros() / 100) * valor;
				contaPoupanca.setTotalDepositos(contaPoupanca.getTotalDepositos()+1);
				contaPoupanca.creditar(valor);
				repositorio.alterar(contaPoupanca);
			}
			else {
				conta.creditar(valor);
				repositorio.alterar(conta);
			}
		}
		return statusVal;
	}
	public StatusValidacaoConta debitar(long numero, double valor) {
		int[] codigoStatus = new int[StatusValidacaoConta.QTD_SITUACOES_EXCECAO]; 
		String[] mensagensStatus = new String[StatusValidacaoConta.QTD_SITUACOES_EXCECAO];
		int contErros = 0;
		Conta conta = buscar(numero);
		if (valor <= 0) {
			codigoStatus[contErros++] = StatusValidacaoConta.VALOR_DEBITO_INVALIDO;
			mensagensStatus[contErros] = MSG_VALOR_DEBITO_INVALIDO;						
		}		
		if (conta == null) {
			codigoStatus[contErros++] = StatusValidacaoConta.CONTA_NAO_ENCONTRADA;
			mensagensStatus[contErros] = MSG_CONTA_NAO_ENCONTRADA;			
		} else if (StatusConta.BLOQUEADA.equals(conta.getStatus())) {
			codigoStatus[contErros++] = StatusValidacaoConta.DEBITO_NP_CONTA_BLOQUEADA;
			mensagensStatus[contErros] = MSG_DEBITO_NP_CONTA_BLOQUEADA;							
		}
		StatusValidacaoConta statusVal = new StatusValidacaoConta(codigoStatus, mensagensStatus, contErros == ZERO);	
		if (statusVal.isValido()) {
			conta.debitar(valor);
			repositorio.alterar(conta);
		}
		return statusVal;
	}
	public int calcularEscoreConta(long numero) {
		Conta conta = buscar(numero);
		if (conta == null || StatusConta.BLOQUEADA.equals(conta.getStatus())) {
			return ESCORE_NAO_CALCULADO;
		} else if (StatusConta.ENCERRADA.equals(conta.getStatus())) {
			return ESCORE_ZERO;
		} else {
			long tempoVidaConta = calcularTempoVidaConta(conta);
			double f = conta.getSaldo()*3 + tempoVidaConta*2;
			if (f < LIMITE_BRONZE) {
				return BRONZE;
			} else if (f >= LIMITE_BRONZE && f < LIMITE_PRATA) {
				return PRATA;
			} else if (f >= LIMITE_PRATA && f < LIMITE_OURO) {
				return OURO;
			} else {
				return DIAMANTE;
			}
		}
	}
	
	public boolean encerrarConta(long numero) {
		StatusConta[] statusProibidos = {StatusConta.ENCERRADA};
		return validarAlterarStatusConta(numero, statusProibidos, StatusConta.ENCERRADA);				
	}
	
	public boolean bloquearConta(long numero) {
		StatusConta[] statusProibidos = {StatusConta.ENCERRADA, StatusConta.BLOQUEADA};
		return validarAlterarStatusConta(numero, statusProibidos, StatusConta.BLOQUEADA);		
	}
	public boolean desbloquearConta(long numero) {
		StatusConta[] statusProibidos = {StatusConta.ENCERRADA, StatusConta.ATIVA};
		return validarAlterarStatusConta(numero, statusProibidos, StatusConta.ATIVA);
	}
	
	private boolean validarAlterarStatusConta(long numero, StatusConta[] statusProibidios, StatusConta statusFinal) {
		Conta conta = buscar(numero);
		if (conta == null) {
			return false;
		} else {
			for (StatusConta statusConta : statusProibidios) {
				if (statusConta.equals(conta.getStatus())) {
					return false;
				}
			}
			conta.setStatus(statusFinal);
			repositorio.alterar(conta);
			return true; 
		}
	}
	
	private long calcularTempoVidaConta(Conta conta) {
		LocalDate dataAbertura = conta.getDataAbertura();
		LocalDate dataAtual = LocalDate.now();
		long dias = dataAbertura.until(dataAtual, ChronoUnit.DAYS);
		return dias; 
	}
	
	private StatusValidacaoConta validar(Conta conta) {
		int[] codigoStatus = new int[StatusValidacaoConta.QTD_SITUACOES_EXCECAO]; 
		String[] mensagensStatus = new String[StatusValidacaoConta.QTD_SITUACOES_EXCECAO];
		int contErros = 0;
		if (conta == null) {
			codigoStatus[contErros++] = StatusValidacaoConta.CONTA_NAO_INFORMADA;
			mensagensStatus[contErros] = MSG_CONTA_NAO_INFORMADA;			
		} else {
			if (conta.getNumero() <= 0) {
				codigoStatus[contErros++] = StatusValidacaoConta.NUMERO_MENOR_IGUAL_A_ZERO;
				mensagensStatus[contErros] = MSG_NUMERO_MENOR_IGUAL_A_ZERO;
			} 
			if (conta.getStatus() == null) {
				codigoStatus[contErros++] = StatusValidacaoConta.STATUS_NAO_INFORMADO;
				mensagensStatus[contErros] = MSG_STATUS_NAO_INFORMADO;				
			}			
			if (conta.getDataAbertura() == null) {
				codigoStatus[contErros++] = StatusValidacaoConta.DATA_ABERTURA_NAO_INFORMADA;
				mensagensStatus[contErros] = MSG_DATA_ABERTURA_NAO_INFORMADA;				
			}			
			if (dataAberturaMaiorDataAtual(conta.getDataAbertura())) {
				codigoStatus[contErros++] = StatusValidacaoConta.DATA_ABERTURA_MAIOR_DATA_ATUAL;
				mensagensStatus[contErros] = MSG_DATA_ABERTURA_MAIOR_DATA_ATUAL;								
			}
			if (dataAberturaMenorDataAtualUmMes(conta.getDataAbertura())) {
				codigoStatus[contErros++] = StatusValidacaoConta.DATA_ABERTURA_MENOR_UM_MES;
				mensagensStatus[contErros] = MSG_DATA_ABERTURA_MENOR_UM_MES;												
			}
			if (conta.getCorrentista() != null) {
				Correntista correntista = correntistaMediator.buscar(conta.getCorrentista().getCpf());
				if (correntista == null) {
					codigoStatus[contErros++] = StatusValidacaoConta.CORRENTISTA_NAO_ENCONTRADO;
					mensagensStatus[contErros] = MSG_CORRENTISTA_NAO_ENCONTRADO;																	
				} else {
					conta.setCorrentista(correntista);
				}
			}
			if (conta instanceof ContaPoupanca) {
				ContaPoupanca contaPoupanca = (ContaPoupanca)conta; 
				if (contaPoupanca.getTaxaJuros() <= 0) {
					codigoStatus[contErros++] = StatusValidacaoConta.TAXA_JUROS_MENOR_IGUAL_A_ZERO;
					mensagensStatus[contErros] = MSG_TAXA_JUROS_MENOR_IGUAL_A_ZERO;																	
				}
			}
		}		
		return new StatusValidacaoConta(codigoStatus, mensagensStatus, contErros == ZERO);		
	}	
	private boolean dataAberturaMaiorDataAtual(LocalDate dataAbertura) {
		if (dataAbertura != null) {
			LocalDate dataAtual = LocalDate.now();
			return dataAbertura.isAfter(dataAtual);
		}
		return false;
	}
	private boolean dataAberturaMenorDataAtualUmMes(LocalDate dataAbertura) {
		if (dataAbertura != null) {
			LocalDate dataAtual = LocalDate.now();
			return dataAbertura.isBefore(dataAtual.minusDays(TRINTA_DIAS));
		}
		return false;
	}
	private void ordenarPorDataAbertura(Conta[] contas) {
		Ordenador.ordenar(contas, COMP_PRODUTO_POR_DATA_ABERTURA);
	}
	
	private void ordenarPorSaldo(Conta[] contas) {
		Ordenador.ordenar(contas, COMP_PRODUTO_POR_SALDO);
	}
	public Conta[] consultarTodosOrdenadoPorDataAbertura() {
		Conta[] todos = repositorio.buscarTodos();
		if (todos != null && todos.length > 0) {
			ordenarPorDataAbertura(todos);
		}
		return todos;
	}	
	public Conta[] consultarTodosOrdenadoPorSaldo() {
		Conta[] todos = repositorio.buscarTodos();
		if (todos != null && todos.length > 0) {
			ordenarPorSaldo(todos);
		}
		return todos;
	}
}

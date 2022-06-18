package br.com.cesarschool.poo.mediators;

/**
 * @author Anônimo 
 */
public class StatusValidacaoCorrentista {
	
	public static final int CORRENTISTA_NAO_INFORMADO = 1; 
	public static final int CPF_INVALIDO = 2; 
	public static final int NOME_NAO_INFORMADO = 3; 
	public static final int DATA_ABERTURA_NAO_INFORMADA = 4;
	public static final int DATA_ABERTURA_MAIOR_DATA_ATUAL = 5;
	public static final int DATA_ABERTURA_MENOR_UM_MES = 6;
	public static final int CREDITO_NP_CONTA_ENCERRADA = 7;
	public static final int VALOR_CREDITO_INVALIDO = 8;
	public static final int DEBITO_NP_CONTA_BLOQUEADA = 9;
	public static final int VALOR_DEBITO_INVALIDO = 10;
	public static final int CORRENTISTA_NAO_INCLUIDO = 11;
	public static final int CORRENTISTA_NAO_ENCONTRADO = 12;
	public static final int QTD_SITUACOES_EXCECAO = 12;
	
	private int[] codigosErros;
	private String[] mensagens;
	private boolean valido;
	
	public StatusValidacaoCorrentista(int[] codigosErros, String[] mensagens, boolean valido) {
		super();
		this.codigosErros = codigosErros;
		this.mensagens = mensagens;
		this.valido = valido;
	}
	
	public int[] getCodigosErros() {
		return codigosErros;
	}
	public String[] getMensagens() {
		return mensagens;
	}
	public boolean isValido() {
		return valido;
	}
	void setValido(boolean valido) {
		this.valido = valido;
	}	
}

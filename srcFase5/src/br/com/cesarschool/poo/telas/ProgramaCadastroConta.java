package br.com.cesarschool.poo.telas;

import br.com.cesarschool.poo.excecoes.ExcecaoNegocio;
import br.com.cesarschool.poo.excecoes.ExcecaoObjetoJaExistente;
import br.com.cesarschool.poo.excecoes.ExcecaoObjetoNaoExistente;

public class ProgramaCadastroConta {

	public static void main(String[] args) throws ExcecaoNegocio, ExcecaoObjetoJaExistente, ExcecaoObjetoNaoExistente {
		TelaConta tela = new TelaConta();
		tela.executaTela();
	}
}
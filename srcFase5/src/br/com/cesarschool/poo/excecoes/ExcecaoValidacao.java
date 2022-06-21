package br.com.cesarschool.poo.excecoes;

import java.util.ArrayList;
import java.util.List;

public class ExcecaoValidacao extends Exception {
        private List erros = new ArrayList();

        public ExcecaoValidacao() {
		
	}
	
	public ExcecaoValidacao(List erros) {
		super();
		this.erros = erros;
	}
	
	public List getErros() {
		return erros;
	}

	public void addErro(Erro erro) {
		erros.add(erro);
	}

	public Object getStatusValidacao() {
		return null;
	}
        
}
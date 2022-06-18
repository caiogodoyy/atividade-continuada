package br.com.cesarschool.poo.repositorios;

import br.com.cesarschool.poo.entidades.Correntista;

public class RepositorioCorrentista {
	private static final int TAMANHO_MAX_CORRENTISTAS = 1000;
	private static RepositorioCorrentista instancia; 
	
	private Correntista[] cadastroCorrentista = new Correntista[TAMANHO_MAX_CORRENTISTAS];
	private int tamanhoAtual = 0;
	
	public static RepositorioCorrentista getInstancia() {
		if (instancia == null) {
			instancia = new RepositorioCorrentista();
		}
		return instancia; 
	}
	
	public boolean incluir(Correntista Correntista) {
		if (buscarIndice(Correntista.getCpf()) != -1) {
			return false;
		} else if (tamanhoAtual == TAMANHO_MAX_CORRENTISTAS - 1) {
			return false;
		} else {
			for (int i = 0; i < cadastroCorrentista.length; i++) {
				if (cadastroCorrentista[i] == null) {
					cadastroCorrentista[i] = Correntista; 
					break;
				}
			}
			tamanhoAtual++; 
			return true; 
		}
	}
	public boolean alterar(Correntista Correntista) {
		int indice = buscarIndice(Correntista.getCpf()); 
		if (indice == -1) {
			return false;
		} else {
			cadastroCorrentista[indice] = Correntista;
			return true; 
		}
	}
	
	public Correntista buscar(String cpf) {
		int indice = buscarIndice(cpf);
		if (indice == -1) {
			return null;
		} else {
			return cadastroCorrentista[indice];
		}
	}
	
	public boolean excluir(String cpf) {
		int indice = buscarIndice(cpf);
		if (indice == -1) {
			return false;
		} else {
			cadastroCorrentista[indice] = null;
			tamanhoAtual--;
			return true;
		}		
	}
	
	public Correntista[] buscarTodos() {
		Correntista[] resultado = new Correntista[tamanhoAtual];
		int indice = 0;
		for (Correntista Correntista : cadastroCorrentista) {
			if (Correntista != null) {
				resultado[indice++] = Correntista; 
			}
		}
		return resultado;
	}
	
	private int buscarIndice(String cpfCnpj) {		
		for (int i = 0; i < cadastroCorrentista.length; i++) {
			Correntista Correntista = cadastroCorrentista[i];
			if (Correntista != null && Correntista.getCpf().equals(cpfCnpj)) {
				return i; 				
			}
		}
		return -1;
	}		
}

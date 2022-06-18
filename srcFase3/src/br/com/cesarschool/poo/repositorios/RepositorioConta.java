package br.com.cesarschool.poo.repositorios;

import br.com.cesarschool.poo.entidades.Conta;
import br.com.cesarschool.poo.entidades.ContaPoupanca;

/**
 * @author Anônimo
 *
 * Implementação primária para efeito didático.
 * Será enormemente melhorada!!!
 */
public class RepositorioConta {

	private static final int TAMANHO_MAX_CONTAS = 1000;
	
	private Conta[] cadastroConta = new Conta[TAMANHO_MAX_CONTAS];
	private int tamanhoAtual = 0;
	
	public boolean incluir(Conta conta) {
		if (buscarIndice(conta.getNumero()) != -1) {
			return false;
		} else if (tamanhoAtual == TAMANHO_MAX_CONTAS - 1) {
			return false;
		} else if (conta instanceof ContaPoupanca) {
			ContaPoupanca contaPoupanca = (ContaPoupanca)conta;
			if (contaPoupanca.getTotalDepositos() != 0)
				return false;
			else {
				for (int i = 0; i < cadastroConta.length; i++) {
					if (cadastroConta[i] == null) {
						cadastroConta[i] = contaPoupanca;
						break;
					}
				}
				tamanhoAtual++; 
				return true; 
			}
		} else {
			for (int i = 0; i < cadastroConta.length; i++) {
				if (cadastroConta[i] == null) {
					cadastroConta[i] = conta;
					break;
				}
			}
			tamanhoAtual++; 
			return true; 
		}
	}
	public boolean alterar(Conta conta) {
		int indice = buscarIndice(conta.getNumero()); 
		if (indice == -1) {
			return false;
		} else {
			cadastroConta[indice] = conta;
			return true; 
		}
	}
	
	public Conta buscar(long numero) {
		int indice = buscarIndice(numero);
		if (indice == -1) {
			return null;
		} else {
			return cadastroConta[indice];
		}
	}
	
	public boolean excluir(long numero) {
		int indice = buscarIndice(numero);
		if (indice == -1) {
			return false;
		} else {
			cadastroConta[indice] = null;
			tamanhoAtual--;
			return true;
		}		
	}
	
	private int buscarIndice(long numero) {		
		for (int i = 0; i < cadastroConta.length; i++) {
			Conta conta = cadastroConta[i];
			if (conta != null && conta.getNumero() == numero) {
				return i; 				
			}
		}
		return -1;
	}
}

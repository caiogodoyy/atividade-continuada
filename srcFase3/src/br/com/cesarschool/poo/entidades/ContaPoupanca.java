package br.com.cesarschool.poo.entidades;

import java.time.LocalDate;

public class ContaPoupanca extends Conta{
	private double taxaJuros;
	private int totalDepositos;

	public ContaPoupanca(long numero, LocalDate dataAbertura, double saldo, StatusConta status, double taxaJuros, int totalDepositos) {
		super(numero, dataAbertura, saldo, status);
		this.taxaJuros = taxaJuros;
		this.totalDepositos = totalDepositos;
	}

	public double getTaxaJuros() {
		return taxaJuros;
	}

	public void setTaxaJuros(double taxaJuros) {
		this.taxaJuros = taxaJuros;
	}

	public int getTotalDepositos() {
		return totalDepositos;
	}

	public void setTotalDepositos(int totalDepositos) {
		this.totalDepositos = totalDepositos;
	}
}

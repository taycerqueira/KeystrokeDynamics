package uefs;

public class Registro {
	
	String senha;
	double[] intervalos;
	double saida;
	
	public Registro(String senha, double[] intervalos, double saida) {
		this.senha = senha;
		this.intervalos = intervalos;
		this.saida = saida;
	}

	public String getSenha() {
		return senha;
	}
	
	public double[] getIntervalos() {
		return intervalos;
	}

	public double getSaida() {
		return saida;
	}


}

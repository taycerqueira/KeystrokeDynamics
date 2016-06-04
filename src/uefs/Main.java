package uefs;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

import org.jfree.ui.RefineryUtilities;


public class Main {
	
	private static ArrayList<Registro> conjTreinamento;
	private static ArrayList<Registro> conjValidacao;
	private static ArrayList<Registro> conjTeste;

	public static void main(String[] args) throws FileNotFoundException, IOException {
		
		/*try {
			boolean escreverFinalArquivo = true; //true = escreve no fim do arquivo | false = sobreescreve o arquivo
			//NativeHooke capturaSenha = new NativeHooke(20, 0.9, escreverFinalArquivo); //para inserir testes verdadeiros
			NativeHooke capturaSenha = new NativeHooke(15, 0.1, escreverFinalArquivo); //para inserir testes falsos
			capturaSenha.init();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
			
		ArrayList<Registro> dados = getDataFromFile("conjunto-teste.txt");
		setConjuntos(dados, 0.6, 0.3, 0.1); //porcentagem de dados que serao destinados a cada conjunto
		//Pega o tamanho da primeira senha pra passar a quantidade de entradas para a rede
		int quantEntradas = dados.get(0).senha.length();
		//System.out.println("tamanho do vetor de entrada: " + quantEntradas);
		RedeNeural rede = new RedeNeural(quantEntradas, conjTreinamento, conjValidacao, conjTeste); // 3 = quantidade de entradas. depende do tamanho da senha (quantidade de intervalos)
		rede.executa();

	}
	
	private static ArrayList<Registro> getDataFromFile(String arquivo) throws FileNotFoundException, IOException{
		
		ArrayList<Registro> dados = new ArrayList<Registro>();
		ArrayList<Registro> dadosEmbaralhados = new ArrayList<Registro>();
		
		System.out.println("Lendo arquivo...");
		
		try(InputStream in = new FileInputStream(arquivo) ){
			
		  Scanner scan = new Scanner(in);
		  
		  while(scan.hasNext()){
			  
		    String linha = scan.nextLine();
		    
		    if(linha.equals("#")){
		    	
		    	String senha = scan.nextLine();
		    	//System.out.println("Senha: " + senha);
		    	
		    	String intervalos = scan.nextLine();
		    	String[] valores = intervalos.split(" ");
		    	
		    	double[] entradas = new double[senha.length()];
		    	
		    	for(int i = 0; i < valores.length; i++){
		    		
		    		entradas[i] = Integer.parseInt(valores[i]);
		    	}
		    	
		    	//int saida = Integer.parseInt(scan.nextLine());
		    	double saida = Double.parseDouble(scan.nextLine());
		    	
		    	Registro registro = new Registro(senha, entradas, saida);
		    	dados.add(registro);
		    }
		    
		  }
		  
		  scan.close();
		  
		  //Embaralha os dados
		  int tamanhoConjTreinamento = dados.size();
			int[] ordem = new int[tamanhoConjTreinamento];
			
			ArrayList<Integer> numeros = new ArrayList<Integer>();
			for (int i = 0; i < tamanhoConjTreinamento; i++) { 
			    numeros.add(i);
			}
			//Embaralhamos os números:
			Collections.shuffle(numeros);
			//Adicionamos os números aleatórios no vetor
			for (int i = 0; i < tamanhoConjTreinamento; i++) {
				ordem[i] = numeros.get(i);
			}
			
			//Cria um dataset com registros aleatórios
			for(int i = 0; i < tamanhoConjTreinamento; i++){
				int indice = ordem[i];
				Registro registro = dados.get(indice);
				dadosEmbaralhados.add(registro);
			}
		  
		  System.out.println("Arquivo lido com sucesso. \nConjunto de treinamento criado.");
		
		  return dadosEmbaralhados;
		
		}
	}
	
	//Cria os conjuntos de treinamento, validação e teste
	//Recebe as % de cada um
	private static void setConjuntos(ArrayList<Registro> dados, double treinamento, double validacao, double teste){
		
		conjTreinamento = new ArrayList<Registro>();
		conjValidacao = new ArrayList<Registro>();
		conjTeste = new ArrayList<Registro>();
		
		int tr = (int) ((int)dados.size()*treinamento);
		int va = (int) ((int)dados.size()*validacao);
		int te = (int) ((int)dados.size()*teste);
		
		int tamDados = dados.size();
/*		System.out.println("tamDados " + tamDados);
		System.out.println("tr " + tr);
		System.out.println("va " + va);
		System.out.println("te " + te);*/

		for(int i = 0; i < tamDados; i++){	
			if(tr >= 0){
				tr--;
				//System.out.println("tr: " + i);
				conjTreinamento.add(dados.get(i));
			}
			else if(va >= 0){
				va--;
				//System.out.println("va: " + i);
				conjValidacao.add(dados.get(i));
				
			}
			else {
				//System.out.println("te: " + i);	
				conjTeste.add(dados.get(i));
			}
		}
		
	}
	


}

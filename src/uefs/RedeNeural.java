package uefs;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.nnet.MultiLayerPerceptron;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;
import org.neuroph.util.TransferFunctionType;

public class RedeNeural {
	
	private DataSet conjTreinamento;
	private int quantEntradas;
	
	public RedeNeural(int quantEntradas){
		
		this.quantEntradas = quantEntradas;
		
		System.out.println("Inicializando rede neural...");
		
	}
	
	private void criaDataSet(){
		
		System.out.println("Criando conjunto de treinamento");
		//LER DO ARQUIVO
		
		this.conjTreinamento = new DataSet(this.quantEntradas, 1);
		
		File arquivo = new File("conjunto-teste-oficial.txt");
		
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
		    	
		    	int saida = Integer.parseInt(scan.nextLine());
		    	
		    	this.conjTreinamento.addRow(new DataSetRow(entradas, new double[]{saida}));
		    }
		    
		  }
		  
		  scan.close();
		  
		  System.out.println("Arquivo lido com sucesso. \nConjunto de treinamento criado.");
		  
		}catch(IOException ex){
		  ex.printStackTrace();
		}

	}

	public void executa() {
		
		System.out.println("Executando rede");
		
		criaDataSet();
		
		// create multi layer perceptron
		
		// ====> ERRO AQUI. AJUSTAR OS PARAMETROS
		MultiLayerPerceptron perceptron = new MultiLayerPerceptron(TransferFunctionType.TANH, 2, 3, 1); 
		
		// learn the training set
		perceptron.learn(this.conjTreinamento);

		// test perceptron
		System.out.println("Testing trained neural network");
		testNeuralNetwork(perceptron, this.conjTreinamento);

		// save trained neural network
		perceptron.save("myMlPerceptron.nnet");

		// load saved neural network
		NeuralNetwork loadedMlPerceptron = NeuralNetwork.createFromFile("myMlPerceptron.nnet");

		// test loaded neural network
		System.out.println("Testing loaded neural network");
		testNeuralNetwork(loadedMlPerceptron, this.conjTreinamento);

	}
	
	
	public static void testNeuralNetwork(NeuralNetwork nnet, DataSet testSet) {

		for(DataSetRow dataRow : testSet.getRows()) {
			nnet.setInput(dataRow.getInput());
			nnet.calculate();
			double[ ] networkOutput = nnet.getOutput();
			System.out.print("Input: " + Arrays.toString(dataRow.getInput()) );
			System.out.println(" Output: " + Arrays.toString(networkOutput) );
		}

	}

}
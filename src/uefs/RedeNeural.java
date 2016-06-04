package uefs;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Scanner;

import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.RefineryUtilities;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.nnet.MultiLayerPerceptron;
import org.neuroph.nnet.Perceptron;
import org.neuroph.nnet.learning.BackPropagation;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;
import org.neuroph.core.events.LearningEvent;
import org.neuroph.core.events.LearningEventListener;
import org.neuroph.util.TransferFunctionType;

public class RedeNeural {
	
	private DataSet conjTreinamento;
	private ArrayList<Registro> dadosTreinamento;
	private DataSet conjValidacao;
	private ArrayList<Registro> dadosValidacao;
	private DataSet conjTeste;
	private ArrayList<Registro> dadosTeste;
	private int quantEntradas;
	
	public RedeNeural(int quantEntradas, ArrayList<Registro> dadosTreinamento, ArrayList<Registro> dadosValidacao, ArrayList<Registro> dadosTeste){
		
		this.quantEntradas = quantEntradas;
		this.dadosTreinamento = dadosTreinamento;
		this.dadosValidacao = dadosValidacao;
		this.dadosTeste = dadosTeste;
		
	}
	
	private void criaDataSets(){
		
		System.out.println("Criando conjunto de treinamento...");
		
		this.conjTreinamento = new DataSet(this.quantEntradas, 1);
		for (Registro registro : dadosTreinamento) {
			this.conjTreinamento.addRow(new DataSetRow(registro.getIntervalos(), new double[]{registro.saida}));
		}
		
		System.out.println("Criando conjunto de validacao...");
		
		this.conjValidacao = new DataSet(this.quantEntradas, 1);
		for (Registro registro : dadosValidacao) {
			this.conjValidacao.addRow(new DataSetRow(registro.getIntervalos(), new double[]{registro.saida}));
		}
		
		System.out.println("Criando conjunto de teste...");
		
		this.conjTeste = new DataSet(this.quantEntradas, 1);
		for (Registro registro : dadosTeste) {
			this.conjTeste.addRow(new DataSetRow(registro.getIntervalos(), new double[]{registro.saida}));
		}


	}

	public void executa() {
		
		System.out.println("Executando rede...");
		
		criaDataSets();
		
		//System.out.println(this.conjTreinamento.getRowAt(0).toString());
		
		// create multi layer perceptron
		
		// ====> ERRO AQUI. AJUSTAR OS PARAMETROS
		MultiLayerPerceptron perceptron = new MultiLayerPerceptron(TransferFunctionType.SIGMOID, this.quantEntradas, 10, 1); 
		
		// learn the training set
		System.out.println("Treinando rede...");
		//perceptron.learn(this.conjTreinamento);
		
        BackPropagation backPropagation = new BackPropagation();
        
        //backPropagation.setMaxIterations(100);
        backPropagation.setLearningRate(0.01);
        
        Listener listener = new Listener(perceptron, backPropagation, this.conjValidacao);
		
		backPropagation.addListener(listener);
		
        perceptron.learn(this.conjTreinamento, backPropagation);
        
        System.out.println("Iterações: " + listener.contIteracao);
		
		// save trained neural network
		perceptron.save("myMlPerceptron.nnet");
		
		//Gera gráfico
		XYSeriesCollection dataset = new XYSeriesCollection();
		dataset.addSeries(listener.erros);
		dataset.addSeries(listener.errosValidacao);
		
	    Grafico chart = new Grafico("Gráfico" , "Erro x Época", dataset);
	    chart.pack();
	    RefineryUtilities.centerFrameOnScreen(chart);
	    chart.setVisible(true);

		// test perceptron
		System.out.println("=> Testando a rede neural... ");
		testNeuralNetwork(perceptron, this.conjTeste);

		// load saved neural network
		NeuralNetwork loadedMlPerceptron = NeuralNetwork.createFromFile("myMlPerceptron.nnet");

		// test loaded neural network
		//System.out.println("Testing loaded neural network");
		//testNeuralNetwork(loadedMlPerceptron, this.conjTreinamento);

	}
	
	
	public static void testNeuralNetwork(NeuralNetwork nnet, DataSet testSet) {
		
		//Teste automatico:
		for(DataSetRow dataRow : testSet.getRows()) {
			
			nnet.setInput(dataRow.getInput());
			nnet.calculate();
			
			double[] networkOutput = nnet.getOutput();
			
			System.out.println(" -------------------------------------------------------------------- ");
			System.out.println("Input: " + Arrays.toString(dataRow.getInput()) );
			System.out.println("Output: " + Arrays.toString(networkOutput) );
			System.out.println("Output Desejada: " + dataRow.getDesiredOutput()[0]);
		}
		
		//Teste manual: 

	}

}
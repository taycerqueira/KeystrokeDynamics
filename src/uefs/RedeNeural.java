package uefs;


import java.util.ArrayList;
import java.util.Arrays;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.nnet.MultiLayerPerceptron;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;
import org.neuroph.util.TransferFunctionType;

public class RedeNeural {
	
	private static ArrayList<Integer> senhaCode;
	private static ArrayList<String> senhaCaractere;
	private static ArrayList<Long> duracoes;
	private DataSet conjuntoTreinamento;
	
	public RedeNeural(ArrayList<Integer> senhaCode, ArrayList<String> senhaCaractere, ArrayList<Long> duracoes){
		
		this.senhaCode = senhaCode;
		this.senhaCaractere = senhaCaractere;
		this.duracoes = duracoes;
		
		System.out.println("Inicializando atributos da rede neural");
		
	}
	
	private void criaDataSet(){
		
		System.out.println("Criando conjunto de treinamento");
		
		this.conjuntoTreinamento = new DataSet(this.duracoes.size(), 1);
		
		//AINDA ESTOU FAZENDO...
		
		// create training set (logical XOR function)
		DataSet trainingSet = new DataSet(2, 1); // Colocar para ser uma variável do tamanho da senha-1 para a entrada
		trainingSet.addRow(new DataSetRow(new double[]{0, 0}, new double[]{0})); // o treinamento vai vim de um conjunto de dados
																				 //
		trainingSet.addRow(new DataSetRow(new double[]{0, 1}, new double[]{1}));
		trainingSet.addRow(new DataSetRow(new double[]{1, 0}, new double[]{1}));
		trainingSet.addRow(new DataSetRow(new double[]{1, 1}, new double[]{0}));


	}

	public void executa() {
		
		System.out.println("Executando rede");
		
		criaDataSet();
		
		// create multi layer perceptron
		MultiLayerPerceptron perceptron = new MultiLayerPerceptron(TransferFunctionType.TANH, 2, 3, 1);
		// learn the training set
		perceptron.learn(conjuntoTreinamento);

		// test perceptron
		System.out.println("Testing trained neural network");
		testNeuralNetwork(perceptron, conjuntoTreinamento);

		// save trained neural network
		perceptron.save("myMlPerceptron.nnet");

		// load saved neural network
		NeuralNetwork loadedMlPerceptron = NeuralNetwork.createFromFile("myMlPerceptron.nnet");

		// test loaded neural network
		System.out.println("Testing loaded neural network");
		testNeuralNetwork(loadedMlPerceptron, conjuntoTreinamento);

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
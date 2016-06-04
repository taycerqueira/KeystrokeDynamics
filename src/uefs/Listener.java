package uefs;

import java.util.Arrays;

import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;
import org.neuroph.core.events.LearningEvent;
import org.neuroph.core.events.LearningEventListener;
import org.neuroph.nnet.learning.BackPropagation;

public class Listener implements LearningEventListener {
	
	NeuralNetwork rede;
	BackPropagation back;
	DataSet data;
	int valida;
	int naoValida;
	double erro= 0.0;
	double mse = 0.0;
	
	public Listener(NeuralNetwork rede, BackPropagation back, DataSet data){
		this.rede = rede;
		this.back = back;
		this.data = data;
	}

	@Override
	public void handleLearningEvent(LearningEvent arg0) {
		
		Double[] pesos = rede.getWeights();
		/*for(int i = 0; i < pesos.length; i++){
			System.out.println("Peso: " + pesos[i]);
		}*/
		double erro = back.getPreviousEpochError();
		//System.out.println("Erro: " + erro);
		
		validacao();
		
		
	}
	
	public void validacao (){
		double mseFinal = 0.0;
		valida = 0;
		naoValida = 0;
		erro = 0.0;
		
		//double[] saida = rede.getOutput();
		//System.out.println("Saida : " + data.getRowAt(0).getDesiredOutput()[0]);
		//System.out.println("Saida : " + data.getRowAt(1).getDesiredOutput()[0]);
		
		for(DataSetRow dataRow : data.getRows()) {
			rede.setInput(dataRow.getInput());
			rede.calculate();
			double[ ] saida = rede.getOutput();
			System.out.print("Etrada Validacao: " + Arrays.toString(dataRow.getInput()) );
			System.out.println(" Output Validacao: " + Arrays.toString(saida) );
			if (data.getRowAt(0).getDesiredOutput()[0] == 0.9 && saida[0] >= 0.8){
				valida ++;				
			}
			else if(data.getRowAt(0).getDesiredOutput()[0] == 0.1 && saida[0] <= 0.4) {
				naoValida++;
			}
			erro=(Math.pow(dataRow.getDesiredOutput()[0],2)-Math.pow(saida[0],2));
            mse=mse+erro;
			
		}
		
		System.out.println("Validas: " + valida);
		System.out.println("Nao Validas: " + naoValida);
		
		mseFinal= mse/10; //colocar para ser o total de amostras
		
		System.out.println("MSE: " + mseFinal);
		
				
	}


}

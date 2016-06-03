package uefs;

import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.events.LearningEvent;
import org.neuroph.core.events.LearningEventListener;
import org.neuroph.nnet.learning.BackPropagation;

public class Listener implements LearningEventListener {
	
	NeuralNetwork rede;
	BackPropagation back;
	
	public Listener(NeuralNetwork rede, BackPropagation back){
		this.rede = rede;
		this.back = back;
	}

	@Override
	public void handleLearningEvent(LearningEvent arg0) {
		
		Double[] pesos = rede.getWeights();
		/*for(int i = 0; i < pesos.length; i++){
			System.out.println("Peso: " + pesos[i]);
		}*/
		
		double erro = back.getPreviousEpochError();
		//System.out.println("Erro: " + erro);
	}

}

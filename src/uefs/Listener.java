package uefs;

import org.jfree.data.xy.XYSeries;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;
import org.neuroph.core.events.LearningEvent;
import org.neuroph.core.events.LearningEventListener;
import org.neuroph.nnet.learning.BackPropagation;

public class Listener implements LearningEventListener {
	
	private NeuralNetwork rede;
	private BackPropagation back;
	private DataSet data;
	int valida;
	int naoValida;
	double erro= 0.0;
	public Integer contIteracao = 0;
	//public ArrayList<Double> erros;
	//public ArrayList<Double> errosValidacao;
	
	//public DefaultCategoryDataset erros = new DefaultCategoryDataset();
	//public DefaultCategoryDataset errosValidacao = new DefaultCategoryDataset();
	
	XYSeries erros = new XYSeries("Erro Treinamento");
	XYSeries errosValidacao = new XYSeries("Erro Validação");
	
	public Listener(NeuralNetwork rede, BackPropagation backPropagation, DataSet conjValidacao){
		this.rede = rede;
		this.back = backPropagation;
		this.data = conjValidacao;
		
		//erros = new ArrayList<Double>();
		//errosValidacao = new ArrayList<Double>();
	}

	@Override
	public void handleLearningEvent(LearningEvent arg0) {
		
		contIteracao++;
		
		String cont = contIteracao.toString();
		
		System.out.println("\n=> ITERAÇÃO: " + contIteracao);
		
		Double[] pesos = rede.getWeights();
		/*for(int i = 0; i < pesos.length; i++){
			System.out.println("Peso: " + pesos[i]);
		}*/
		double erro = back.getPreviousEpochError();
		System.out.println("Erro do Treinamento: " + erro);
		erros.add(contIteracao.doubleValue(), erro); //Comentar essa linha se quiser que NÃO apareca o erro de treino no gráfico
		
		//double erroValidacao = validacao();
		//System.out.println("Erro da Validação: " + erroValidacao);
		//errosValidacao.add(contIteracao.doubleValue(), erroValidacao); //Comentar essa linha se quiser que NÃO apareca o erro de validação no gráfico
	}
	
	public double validacao (){
		
		double mse = 0.0;
		
		valida = 0;
		naoValida = 0;
		erro = 0.0;
		double somatorio = 0;
		
		//double[] saida = rede.getOutput();
		//System.out.println("Saida : " + data.getRowAt(0).getDesiredOutput()[0]);
		//System.out.println("Saida : " + data.getRowAt(1).getDesiredOutput()[0]);
		
		for(DataSetRow dataRow : data.getRows()) {
			
			rede.setInput(dataRow.getInput());
			rede.calculate();
			
			double[] saida = rede.getOutput();
			
			//System.out.print("Entrada Validacao: " + Arrays.toString(dataRow.getInput()));
			//System.out.println(" Output Validacao: " + Arrays.toString(saida) );
			
			if (data.getRowAt(0).getDesiredOutput()[0] == 0.9 && saida[0] >= 0.8){
				valida ++;				
			}
			else if(data.getRowAt(0).getDesiredOutput()[0] == 0.1 && saida[0] <= 0.4) {
				naoValida++;
			}
			
			//erro = (Math.pow(dataRow.getDesiredOutput()[0],2) - Math.pow(saida[0],2));
			erro = Math.pow((dataRow.getDesiredOutput()[0] - saida[0]), 2);
			//System.out.println("erro validacao: " + erro);
            somatorio += erro;
			
		}
		
		//System.out.println("Validas: " + valida);
		//System.out.println("Nao Validas: " + naoValida);
		mse = somatorio/data.size(); //colocar para ser o total de amostras
		//System.out.println("erro total (mse): " + mse);
		
		return mse;
		
		//System.out.println("MSE: " + mseFinal);
					
	}
	
}

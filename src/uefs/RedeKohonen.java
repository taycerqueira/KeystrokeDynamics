package uefs;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import javax.imageio.ImageIO;

import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.Neuron;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;
import org.neuroph.nnet.Kohonen;
import org.neuroph.nnet.MultiLayerPerceptron;
import org.neuroph.nnet.learning.BackPropagation;
import org.neuroph.nnet.learning.KohonenLearning;
import org.neuroph.util.TransferFunctionType;

public class RedeKohonen {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		BufferedImage image = null;
        
        try {
            image = ImageIO.read(new File("teste3.png"));
            //image = ImageIO.read(new File("inu.jpg"));
        } catch (IOException e) {
            System.out.println("Não foi possivel abrir a imagem");
        }
        
        DataSet trainingSet = new DataSet(3); 
        
        int rgb[] = new int[3];
        double[] r = new double[image.getHeight()*image.getWidth()];
        double g[] = new double[image.getHeight()*image.getWidth()];
        double b[] = new double[image.getHeight()*image.getWidth()];
        
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                rgb = getPixelRGB(image.getRGB(x, y));
               
                trainingSet.addRow(new DataSetRow(r[x+y] = rgb[0], g[x+y] = rgb[1],b[x+y] = rgb[2] ));
               // System.out.println("R: " + r[x] + " G: " + g[x] + " B: " + b[x] );
            }
        }
				// create kohonen
				Kohonen kohonen = new Kohonen(3,50);
				// 
				
				// learn the training set
				kohonen.learn(trainingSet);
				KohonenLearning kl = new KohonenLearning();
				kl.setTrainingSet(trainingSet);
				kl.setNeuralNetwork(kohonen);
				//kohonen.get
				//kohonen.get
				
				Neuron[] neuron= kohonen.getOutputNeurons();
				
				//System.out.println(">>Testing" + kohonen.getOutput()[0]);
				//System.out.println(" Output: " +kl.getMapSize() );
				System.out.println(" Output: " +Arrays.toString(kohonen.getOutput()) );
				
				//System.out.println(" Output: " + neuron[0].getOutput());
				
				
				// test perceptron
				//System.out.println("Testing trained neural network");
				//testNeuralNetwork(myMlPerceptron, trainingSet);

				// save trained neural network
				//myMlPerceptron.save("myMlPerceptron.nnet");

				// load saved neural network
				//NeuralNetwork loadedMlPerceptron = NeuralNetwork.createFromFile("myMlPerceptron.nnet");

				// test loaded neural network
				//System.out.println("Testing loaded neural network");
				//testNeuralNetwork(loadedMlPerceptron, trainingSet);

	}
	
	 public static int[] getPixelRGB(int pixel) {
	        int rgb [] = new int[3]; // argb: alpha, red, green, blue
	        //argb[0] = (pixel >> 24) & 0xff;
	        rgb[0] = (pixel >> 16) & 0xff;
	        rgb[1] = (pixel >> 8) & 0xff;
	        rgb[2] = (pixel) & 0xff;
	       // System.out.println("argb: " + argb[0] + ", " + argb[1] + ", " + argb[2] + ", " + argb[3]);
	        return rgb;
	    }

}

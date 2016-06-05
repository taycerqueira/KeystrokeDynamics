package uefs;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;
import org.neuroph.core.NeuralNetwork;

public class Senha implements NativeKeyListener{
	
	private long initTime = 0;
	private long endTime = 0;
	private boolean primeiraTecla = true;
	private static ArrayList<Long> intervalos;
	private static ArrayList<String> senha;
	private Senha listener;
	private static NeuralNetwork rede;
	private static String senhaVerdadeira;
	
	public Senha(NeuralNetwork rede, String senha){
		this.rede = rede;
		this.senhaVerdadeira = senha;
	}
	
	public void capturarSenha(){
		
		this.intervalos = new ArrayList<Long>();
		this.senha = new ArrayList<String>();
		
		try {
			
			GlobalScreen.registerNativeHook();
			listener = new Senha(rede, senhaVerdadeira);
			GlobalScreen.addNativeKeyListener(listener);
			System.out.println("SENHA: ");
			
			// Clear previous logging configurations.
			LogManager.getLogManager().reset();

			// Get the logger for "org.jnativehook" and set the level to off.
			Logger logger = Logger.getLogger(GlobalScreen.class.getPackage().getName()); // Usei para desabilitar o log
			logger.setLevel(Level.OFF);
			
		} catch (NativeHookException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	@Override
	public void nativeKeyPressed(NativeKeyEvent e) {
		
		if(primeiraTecla){
			primeiraTecla = false;
		}
		else{
			
			endTime = System.currentTimeMillis();
			
			int teclaPressionada = e.getKeyCode();
			if(e.getKeyCode() != NativeKeyEvent.VC_CAPS_LOCK && e.getKeyCode() != NativeKeyEvent.VC_BACKSPACE){
				
				//System.out.println("TECLA PRESSIONADA: " + NativeKeyEvent.getKeyText(teclaPressionada)); 	//imprime a tecla que foi pressionada
				
				long intervalo = endTime - initTime;
				intervalos.add(intervalo);
				
				//System.out.println("Intervalo:   " + intervalo); 
				
			}
			
		}
		
	}

	@Override
	public void nativeKeyReleased(NativeKeyEvent e) {
		
		initTime = System.currentTimeMillis();
		
		if(e.getKeyCode() == 28){ //Se for enter
			
			//System.out.println("ENTER PRESSIONADO");
			//System.out.println("TESTAR REDE");
			testarRede();
			primeiraTecla = true;
			
			
		}
		else if(e.getKeyCode() != NativeKeyEvent.VC_CAPS_LOCK && e.getKeyCode() != NativeKeyEvent.VC_BACKSPACE){
			
			int teclaSolta = e.getKeyCode();
			senha.add(NativeKeyEvent.getKeyText(teclaSolta));

		}
		
	}

	@Override
	public void nativeKeyTyped(NativeKeyEvent arg0) {
		// TODO Auto-generated method stub
		
	} 
	
	private void testarRede(){
		
		String password = new String("");
		for (int i = 0; i < senha.size(); i++) {
			password += senha.get(i);
		}
		//System.out.println("SENHA INSERIDA: " + password);
		if(password.equals(senhaVerdadeira)){
			//System.out.println("Vamos ver se é você mesmo...");
			//System.out.println("intervalos size: " + intervalos.size());
			double[] input = new double[intervalos.size()];
			
			for (int i = 0; i < intervalos.size(); i++) {
				//System.out.println("i = " + intervalos.get(i));
				input[i] = intervalos.get(i);
			}
			
			rede.setInput(input);
			rede.calculate();
			
			double output = rede.getOutput()[0];
			System.out.println("OUTPUT: " + output);
			if(output < 0.4){
				System.out.println("Tentou me enganar hein, fake!");
			}
			else{
				System.out.println("Ok, é você mesmo.");
			}
		}
		else{
			System.out.println("Senha incorreta!");
			//System.out.println("A senha verdadeira é: " + senhaVerdadeira);
		}
		
		this.intervalos = new ArrayList<Long>();
		this.senha = new ArrayList<String>();
		
	}

}

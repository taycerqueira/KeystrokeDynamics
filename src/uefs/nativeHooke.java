package uefs;

import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;


public class nativeHooke implements NativeKeyListener {
	long initTime = 0;
	long endTime = 0;
	long totalTime = 0;
	long[] time = new long [4];
	long[] time2 = new long [3];
	int ind = 0;

	public static void main(String[] args) {
		
		try {
			GlobalScreen.registerNativeHook();
		} catch (NativeHookException ex) {
			System.err.println("There was a problem registering the native hook.");
			System.err.println(ex.getMessage());

			System.exit(1);
		}

		// Construct the example object and initialze native hook.
		// Clear previous logging configurations.
		LogManager.getLogManager().reset();

		// Get the logger for "org.jnativehook" and set the level to off.
		Logger logger = Logger.getLogger(GlobalScreen.class.getPackage().getName()); // Usei para desabilitar o log
		logger.setLevel(Level.OFF);
		GlobalScreen.addNativeKeyListener(new nativeHooke());
	}



	public void nativeKeyPressed(NativeKeyEvent e) {	
				
		//initTime = 0;
		//initTime = System.currentTimeMillis(); // basta descomentar que ele pega o moemento que a tecla é pressionada
		
		
			time[ind] = System.currentTimeMillis(); // tava querendo pegar todos os valores digitados e depois subtrair 
													// de doi em dois
															//for (int i=0; i<3; i++){
																
															//	time2[i] = time[i + 1] - time[i];
																
															//}
		ind = ind + 1; 
		//System.out.println("Tempo inicial: " + initTime);
	}

	
	@Override
	public void nativeKeyTyped(NativeKeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void nativeKeyReleased(NativeKeyEvent arg0) {
		// TODO Auto-generated method stub
		endTime = 0;
		endTime = System.currentTimeMillis();
		
		totalTime = endTime - initTime;
		
		//System.out.println("Tempo da tecla:   " + totalTime); // imprime o valor do tempo depois que a tecla é solta
		
		//System.out.print(NativeKeyEvent.getKeyText(e.getKeyCode())); 	//imprime a tecla que foi pressionada
		
	}

}

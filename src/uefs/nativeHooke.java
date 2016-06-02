package uefs;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

public class NativeHooke implements NativeKeyListener {
	
	private long initTime = 0;
	private long endTime = 0;
	
	private static ArrayList<Integer> senhaCode;
	private static ArrayList<String> senhaCaractere;
	private static ArrayList<Long> duracoes;
	
	public void init(){
		
		try {
			
			GlobalScreen.registerNativeHook();
			GlobalScreen.addNativeKeyListener(new NativeHooke());
			
			senhaCode = new ArrayList<Integer>();
			senhaCaractere = new ArrayList<String>();
			duracoes = new ArrayList<Long>();
			
			// Clear previous logging configurations.
			LogManager.getLogManager().reset();

			// Get the logger for "org.jnativehook" and set the level to off.
			Logger logger = Logger.getLogger(GlobalScreen.class.getPackage().getName()); // Usei para desabilitar o log
			logger.setLevel(Level.OFF);
			
		} catch (NativeHookException ex) {
			
			System.err.println("There was a problem registering the native hook.");
			System.err.println(ex.getMessage());
			System.exit(1);
		}
		
	}


	public void nativeKeyPressed(NativeKeyEvent e) {	
		
		if(e.getKeyCode() != 28){ //Se não for enter
			
			int teclaPressionada = e.getKeyCode();
			senhaCode.add(teclaPressionada);
			senhaCaractere.add(NativeKeyEvent.getKeyText(teclaPressionada));
			System.out.println("TECLA PRESSIONADA: " + NativeKeyEvent.getKeyText(teclaPressionada));
			initTime = System.currentTimeMillis();
			
		}
		else{
			System.out.println("Enter pressionado. Fim da aplicação.");
			System.out.println("Quantidade de caracteres da senha: " + senhaCode.size());
			
			RedeNeural rede = new RedeNeural(this.senhaCode, this.senhaCaractere, this.duracoes);
			rede.executa();
			
			/*try {
				GlobalScreen.unregisterNativeHook();
				
			} catch (NativeHookException e1) {
				e1.printStackTrace();
			}*/
		}
		
	}

	
	@Override
	public void nativeKeyReleased(NativeKeyEvent e) {
		
		int teclaSolta = e.getKeyCode();
		System.out.println("TECLA SOLTA: " + NativeKeyEvent.getKeyText(teclaSolta)); 	//imprime a tecla que foi pressionada
		
		endTime = System.currentTimeMillis();
		
		long duracao = endTime - initTime;
		duracoes.add(duracao);
		
		System.out.println("Duração:   " + duracao); // imprime o valor do tempo depois que a tecla é solta
		
	}


	@Override
	public void nativeKeyTyped(NativeKeyEvent e) {
		
			
	}

}

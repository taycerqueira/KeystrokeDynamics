package uefs;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;
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
	private int quantTestes;
	static private FileWriter arquivo;
	private boolean primeiraTecla = true;
	private double outputEntrada;
	private boolean escreverFinalArquivo;
	
	private static ArrayList<Integer> senhaCode;
	private static ArrayList<String> senhaCaractere;
	private static ArrayList<Long> intervalos;
	
	private static NativeHooke keyListener;
	
	public NativeHooke(int quantTestes, double outputEntrada, boolean escreverFinalArquivo){
		this.quantTestes = quantTestes;
		this.outputEntrada = outputEntrada;
		this.escreverFinalArquivo = escreverFinalArquivo;
	}
	
	public void init() throws IOException{
		
		try {
			
			//System.out.println("Teste: " + quantTestes);
			arquivo = new FileWriter("conjunto-teste.txt", escreverFinalArquivo);
			
			senhaCode = new ArrayList<Integer>();
			senhaCaractere = new ArrayList<String>();
			intervalos = new ArrayList<Long>();
			
			GlobalScreen.registerNativeHook();
			this.keyListener = new NativeHooke(this.quantTestes, this.outputEntrada, this.escreverFinalArquivo);
			GlobalScreen.addNativeKeyListener(this.keyListener);
			System.out.println("Insira sua senha: ");
			
			// Clear previous logging configurations.
			LogManager.getLogManager().reset();

			// Get the logger for "org.jnativehook" and set the level to off.
			Logger logger = Logger.getLogger(GlobalScreen.class.getPackage().getName()); // Usei para desabilitar o log
			logger.setLevel(Level.OFF);
			
		} catch (NativeHookException ex) {
			
			arquivo.close();
			System.err.println("There was a problem registering the native hook.");
			System.err.println(ex.getMessage());
			System.exit(1);
		}
		
	}


	public void nativeKeyPressed(NativeKeyEvent e) {
		
		//initTime = System.currentTimeMillis();
		
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
		
		//endTime = System.currentTimeMillis();
		initTime = System.currentTimeMillis();
		
		if(e.getKeyCode() == 28){ //Se for enter
			
			primeiraTecla = true;
			
			//System.out.println("Enter pressionado");
			this.quantTestes--;
			//System.out.println("Teste: " + quantTestes);
			
			PrintWriter texto = new PrintWriter(arquivo, escreverFinalArquivo);
			
			//Salva no arquivo
			texto.println("#");
			for (String caractere : senhaCaractere) {
				texto.print(caractere);
			}
			texto.print("\r\n");
			for (Long duracao : intervalos) {
				texto.print(duracao + " ");
			}
			texto.print("\r\n");

			texto.println(this.outputEntrada);
			
			if(this.quantTestes > 0){
				
				System.out.println("Repita a senha: ");
				//System.out.println("Quantidade de caracteres da senha: " + senhaCode.size());
				//System.out.println("Quantidade de entradas da rede: " + intervalos.size());
				
				senhaCode = new ArrayList<Integer>();
				senhaCaractere = new ArrayList<String>();
				intervalos = new ArrayList<Long>();
				
			}
			else{
				System.out.println("Ok, that is enough. Bora treinar a rede. ");
				try {
					arquivo.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				try {
					GlobalScreen.unregisterNativeHook();
					
				} catch (NativeHookException e1) {
					e1.printStackTrace();
				}
				//RedeNeural rede = new RedeNeural();
				//rede.executa();
			}	
			
		}
		else if(e.getKeyCode() != NativeKeyEvent.VC_CAPS_LOCK && e.getKeyCode() != NativeKeyEvent.VC_BACKSPACE){
			
			int teclaSolta = e.getKeyCode();
			senhaCode.add(teclaSolta);
			senhaCaractere.add(NativeKeyEvent.getKeyText(teclaSolta));
			//System.out.println("TECLA PRESSIONADA: " + NativeKeyEvent.getKeyText(teclaPressionada));

		}
		
	}


	@Override
	public void nativeKeyTyped(NativeKeyEvent e) {
		
			
	}

}

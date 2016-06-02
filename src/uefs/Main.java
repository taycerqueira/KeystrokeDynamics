package uefs;

import java.io.IOException;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) {
		
		NativeHooke capturaSenha = new NativeHooke(5);
		try {
			capturaSenha.init();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}

package uefs;

import java.io.IOException;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) {
		
		/*try {
			NativeHooke capturaSenha = new NativeHooke(5);
			capturaSenha.init();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
		//Por enquanto vamos usar o arquivo que já gerei com alguns exemplos. Não precisar pegar as entradas de novo.
		
		RedeNeural rede = new RedeNeural(3); // 3 = quantidade de entradas. depende do tamanho da senha (quantidade de intervalos)
		rede.executa();

	}

}

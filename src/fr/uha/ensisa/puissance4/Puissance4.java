package fr.uha.ensisa.puissance4;

import fr.uha.ensisa.puissance4.ui.Console;
import fr.uha.ensisa.puissance4.ui.GraphiqueScene;
import fr.uha.ensisa.puissance4.util.Constantes;

public abstract class Puissance4 {

	public static void main(String[] args) {

		
		int mode = Constantes.MODE_INTERFACE_GRAPHIQUE;

		switch(mode)
		{
		case Constantes.MODE_INTERFACE_GRAPHIQUE:
			GraphiqueScene.main(null);
			break;
		default :
			Console console = new Console();
			console.start();
			break;
		}

	}

}

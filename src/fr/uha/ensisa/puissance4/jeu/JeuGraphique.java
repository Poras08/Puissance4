package fr.uha.ensisa.puissance4.jeu;

import fr.uha.ensisa.puissance4.data.Joueur;
import fr.uha.ensisa.puissance4.data.Partie;
import fr.uha.ensisa.puissance4.ui.Console;
import fr.uha.ensisa.puissance4.ui.GridController;

public class JeuGraphique extends Thread{
	
	private Partie partie;
	private Console console;
	private GridController gridController;
	
	public JeuGraphique(Joueur joueur1, Joueur joueur2, Console console)
	{
		this.partie=new Partie(joueur1, joueur2);
		this.console=console;
	}
	
	/**
	 * attribution de la grille graphique à notre jeu graphique
	 * @param m
	 */
	public void setMainGridControler(GridController m) {
		this.gridController=m;
	}
	
	public Partie getPartie() {
		return this.partie;
	}
	
	public void run()
	{
		console.lancementPartie(partie.getJoueur1(), partie.getJoueur2());
		while(!partie.isPartieFinie()){
			console.lancementTour(partie.getTour(), partie.getJoueurCourant(), partie.getGrille());
			long tempsReflexion=System.currentTimeMillis();
			int coup= partie.getJoueurCourant().joue(partie.getGrille(), console, partie.getTour());
			gridController.setColonneJouer(coup);
			gridController.setLineJouer(this.getPartie().getGrille().getNombreVideParColonne(coup));			
			gridController.updateGrille();
			tempsReflexion=System.currentTimeMillis()-tempsReflexion;
			console.afficherCoup(partie.getJoueurCourant(), coup, tempsReflexion);
			if(!partie.jouerCoup(coup, tempsReflexion)){
				System.out.println("COUP INVALIDE : Recommencez !");
			}
		}
		console.closeScanner();
		gridController.afficherFinGame();
		console.afficherFinPartie(partie);
	}
}

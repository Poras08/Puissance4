package fr.uha.ensisa.puissance4.data;

import fr.uha.ensisa.puissance4.util.Constantes;
import fr.uha.ensisa.puissance4.util.Constantes.Case;

public class Grille {
	
	private Case[][] grille;
	
	public Grille()
	{
		grille= new Case[Constantes.NB_COLONNES][Constantes.NB_LIGNES];
		for(int i=0;i<Constantes.NB_COLONNES;i++)
			for(int j=0;j<Constantes.NB_LIGNES;j++)
			{
				grille[i][j] = Case.V;
			}
	}
	
	/**
	 * Constructeur permettant de crÃ©er une copie de la grille donnÃ©e en argument
	 * @param original
	 */
	private Grille(Grille original)
	{
		grille= new Case[Constantes.NB_COLONNES][Constantes.NB_LIGNES];
		for(int i=0;i<Constantes.NB_COLONNES;i++)
			for(int j=0;j<Constantes.NB_LIGNES;j++)
			{
				grille[i][j] = original.grille[i][j];
			}	
	}
	
	/**
	 * Renvoie le contenu de la case aux coordonnÃ©es donnÃ©es en argument
	 * @param ligne
	 * @param colonne
	 * @return
	 */
	public Case getCase(int ligne, int colonne)
	{
		return grille[colonne][ligne];
	}

	/**
	 * Indique s'il y a encore de la place dans la colonne indiquÃ©e
	 * @param colonne
	 * @return
	 */
	public boolean isCoupPossible(int colonne) {
		if(colonne>=0&&colonne<Constantes.NB_COLONNES)
		{
			return grille[colonne][Constantes.NB_LIGNES-1]==Case.V;
		}
		else
		{
			return false;
		}
	}

	/**
	 * Ajoute le symbole indiquÃ© dans la colonne indiquÃ©e
	 * ce qui permet de jouer ce coup
	 * @param colonne
	 * @param symbole
	 */
	public void ajouterCoup(int colonne, Case symbole) {
		for(int j=0;j<Constantes.NB_LIGNES;j++)
		{
			if(grille[colonne][j] == Case.V)
			{
				grille[colonne][j]= symbole;
				break;
			}
		}	
	}
	
	/**
	 * renvoie le nombre de case libre sur la colonne passée
	 * en paremettre . Utile pour la représentation graphique
	 * @param col
	 * @return
	 */
	public int getNombreVideParColonne(int col) {
		int colonneVides=0;
		for(int i=0;i<Constantes.NB_LIGNES;i++){
			if(grille[col][i]==Constantes.Case.V){
				colonneVides++;
			}
		}
		return colonneVides;
	}
	
	/**
	 * Renvoie l'Ã©tat de la partie
	 * @param symboleJoueurCourant
	 * @param tour
	 * @return
	 */
	public int getEtatPartie(Case symboleJoueurCourant, int tour)
	{
		int victoire;
		if(symboleJoueurCourant==Constantes.SYMBOLE_J1)
		{
			victoire=Constantes.VICTOIRE_JOUEUR_1;
		}
		else
		{
			victoire=Constantes.VICTOIRE_JOUEUR_2;
		}
		int nbAlignes=0;
		//Horizontal
		for(int i=0;i<Constantes.NB_LIGNES;i++)
		{
			for(int j=0;j<Constantes.NB_COLONNES;j++)
			{
				if(grille[j][i]==symboleJoueurCourant)
					nbAlignes++;
				else
					nbAlignes=0;
				if(nbAlignes==4)
				{
					return victoire;
				}
			}
			nbAlignes=0;
		}
		//Vertical 
		for(int j=0;j<Constantes.NB_COLONNES;j++)
		{
			for(int i=0;i<Constantes.NB_LIGNES;i++)
			{
				if(grille[j][i]==symboleJoueurCourant)
					nbAlignes++;
				else
					nbAlignes=0;
				if(nbAlignes==4)
				{
					return victoire;
				}
			}
			nbAlignes=0;
		}
		//Diagonal (bottom-right to top-left)
		for(int i=0;i<Constantes.NB_LIGNES-3;i++)
			for(int j=0;j<Constantes.NB_COLONNES-3;j++)
			{
				for(int x=0;i+x<Constantes.NB_LIGNES&&j+x<Constantes.NB_COLONNES;x++)
				{
					if(grille[j+x][i+x]==symboleJoueurCourant)
						nbAlignes++;
					else
						nbAlignes=0;
					if(nbAlignes==4)
					{
						return victoire;
					}
				}
				nbAlignes=0;
			}
		
		//Diagonal (bottom-left to top-right)
		for(int i=0;i<Constantes.NB_LIGNES-3;i++)
			for(int j=Constantes.NB_COLONNES-1;j>=3;j--)
			{
				for(int x=0;i+x<Constantes.NB_LIGNES&&j-x>=0;x++)
				{
					if(grille[j-x][i+x]==symboleJoueurCourant)
						nbAlignes++;
					else
						nbAlignes=0;
					if(nbAlignes==4)
					{
						return victoire;
					}
				}
				nbAlignes=0;
			}
		
		if(tour==Constantes.NB_TOUR_MAX)
		{
			return Constantes.MATCH_NUL;
		}
		return Constantes.PARTIE_EN_COURS;
	}
	
	/**
	 * Les alignements de pions du programme sont comptés positivement, et ceux de l'adversaire négativement, de
	 * sorte que la fonction d'évaluation soit symétrique.
	 * @param symboleJoueurCourant
	 * @return
	 */
	public double evaluer(Case symboleJoueurCourant) {
		Case symboleAdversaire;
		if(symboleJoueurCourant==Constantes.SYMBOLE_J1){
			symboleAdversaire=Constantes.SYMBOLE_J2;
		}else{
			symboleAdversaire=Constantes.SYMBOLE_J1;
		}
		double result=evaluationParSymbole(symboleJoueurCourant)-evaluationParSymbole(symboleAdversaire);
		return result;
	}
	
	
	/**
	 * Donne un score Ã  la grille en fonction du joueur donné en parametre , en comptant aussi le nombre de case vides
	 * de telles sorte que pour un alignement moins on a de cases vides plus le score augmente.
	 * 1 pions=coefSymb
	 * 2 pions=coefSymb^2
	 * 3 pions=coefSymb^3
	 * @param symboleJoueur
	 * @return
	 */
	public double evaluationParSymbole(Case symboleJoueur)
	{
		Case symboleAdversaire;
		double evaluation=0;
		int nbAlignes=0;
		int nbVides=0;
		boolean isAlignementPossible=true;
		//valeur d'une case contenant le symbole du joueur courant.
		int coefSymb=150;
		//valeur d'une case vide.
		int coefV=75;
		double[] scoreAlignes = {0,coefSymb,coefSymb*coefSymb,coefSymb*coefSymb*coefSymb,coefSymb*coefSymb*coefSymb*coefSymb};
		double[] scoreCaseVide= {0,coefV*coefV*coefV*coefV,coefV*coefV*coefV,coefV*coefV,coefV};
		if(symboleJoueur==Constantes.SYMBOLE_J1){
			symboleAdversaire=Constantes.SYMBOLE_J2;
		}else{
			symboleAdversaire=Constantes.SYMBOLE_J1;
		}
		//Evaluation des alignements possibles
		//Alignements horizontaux
		for(int i=0;i<Constantes.NB_LIGNES;i++){
			for(int j=0;j<Constantes.NB_COLONNES-3;j++){
				for(int x=0;x<4;x++){
					if(grille[j+x][i]==symboleAdversaire) {
						isAlignementPossible=false;
					}
					if(grille[j+x][i]==symboleJoueur){
						nbAlignes++;
					}
					if(grille[j+x][i]==Constantes.Case.V){
						nbVides++;
					}
				}
				if (isAlignementPossible){
					evaluation+=scoreAlignes[nbAlignes]+scoreCaseVide[nbVides];
				}
				nbVides=0;
				nbAlignes=0;
				isAlignementPossible=true;
			}
		}
		//Alignements verticaux
		for(int i=0;i<Constantes.NB_LIGNES-3;i++){
			for(int j=0;j<Constantes.NB_COLONNES;j++){
				for(int x=0;x<4;x++){
					if(grille[j][i+x]==symboleAdversaire){
						isAlignementPossible=false;
					}
					if(grille[j][i+x]==symboleJoueur){
						nbAlignes++;
					}
					if(grille[j][i+x]==Constantes.Case.V){
						nbVides++;
					}
				}
				if (isAlignementPossible){
					evaluation+=scoreAlignes[nbAlignes]+scoreCaseVide[nbVides];
				}
				nbVides=0;
				nbAlignes=0;
				isAlignementPossible=true;
			}
		}
		//Alignements diagonaux (haut-gauche vers bas-droit)
		for(int i=0;i<Constantes.NB_LIGNES-3;i++){
			for(int j=0;j<Constantes.NB_COLONNES-3;j++){
				for(int x=0;x<4;x++){
					if(grille[j+x][i+x]==symboleAdversaire){
						isAlignementPossible=false;
					}
					if(grille[j+x][i+x]==symboleJoueur){
						nbAlignes++;
					}
					if(grille[j+x][i+x]==Constantes.Case.V){
						nbVides++;
					}
				}
				if (isAlignementPossible){
					evaluation+=scoreAlignes[nbAlignes]+scoreCaseVide[nbVides];
				}
				nbVides=0;
				nbAlignes=0;
				isAlignementPossible=true;
			}
		}
		//Alignements diagonaux (haut-droit vers bas-gauche)
		for(int i=0;i<Constantes.NB_LIGNES-3;i++){
			for(int j=3;j<Constantes.NB_COLONNES;j++){
				for(int x=0;x<4;x++){
					if(grille[j-x][i+x]==symboleAdversaire){
						isAlignementPossible=false;
					}
					if(grille[j-x][i+x]==symboleJoueur){
						nbAlignes++;
					}
					if(grille[j-x][i+x]==Constantes.Case.V){
						nbVides++;
					}
				}
				if (isAlignementPossible){
					evaluation+=scoreAlignes[nbAlignes]+scoreCaseVide[nbVides];
				}
				nbVides=0;
				nbAlignes=0;
				isAlignementPossible=true;
			}
		}
		return evaluation;
	}

	/**
	 * Clone la grille
	 */
	public Grille clone()
	{
		Grille copy = new Grille(this);
		return copy;
	}

}

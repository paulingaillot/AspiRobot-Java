import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {

	public static boolean locker;
	public static int size;
	public static Case[][] tab;
	public static Aspirateur aspi;
	public static int[][] visited;
	public static int interface_used;
	public static int actual_perf;

	public static void main(String[] args) throws Exception {

		initialisation();

		choixInterface();

		affichage();

		Thread myThread;
		myThread = new Thread(new ThreadEnvironnement());
		myThread.start();

		Thread myThread2;
		myThread2 = new Thread(new ThreadAgent());
		myThread2.start();

		Thread myThread3;
		myThread3 = new Thread(new ThreadPerf());
		myThread3.start();

	}

	// return 1 pour BFS et 2 pour glouton
	private static int choixInterface() throws IOException {
		System.out.println("Choisir l'algorithme de recherche a utiliser : \n 1- BFS \n 2- Glouton");

		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

		String choix_interface = reader.readLine();

		if (choix_interface == "BFS" || choix_interface == "1") {
			System.out.println("L'algorithme de recherche utilisé sera le BFS");
			return 1;
		} else {
			System.out.println("L'algorithme de recherche utilisé sera l'algorithme glouton");
			return 2;
		}

	}

	private static void initialisation() {
		locker = false;
		size = 5; // définit la taille du manoir (ici carré 5x5)
		tab = new Case[size][size]; // crée la map de l'environnement
		aspi = new Aspirateur(); // créé l'objet aspirateur
		visited = new int[size][size]; // crée la map des cases visitées
		interface_used = 1; // interface utilisée par défaut
		actual_perf = 0; // stocke la performance en temps réel de l'aspirateur

		// On crée toutes les cases du tableau
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				tab[i][j] = new Case(i, j);
			}
		}

	}

	public static void affichage() {
		while (locker == true) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {

			}
		}
		locker = true;
		System.out.println("----- Mise a Jour de l'Etat de la piece ----- Performance : " + actual_perf);
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				String salete = tab[i][j].salete;
				String bijou = tab[i][j].bijou;

				if (aspi.posx == i && aspi.posy == j) {

					System.out.print("\u001B[44m A ");
				} else if (salete == "O" && bijou == "O") {

					System.out.print("\u001B[43m T ");
				} else if (salete == "O") {

					System.out.print("\u001B[41m S ");
				} else if (bijou == "O") {

					System.out.print("\u001B[45m B ");
				} else {

					System.out.print("\u001B[42m P ");
				}
				System.out.print("|");

			}
			System.out.print("\u001B[0m \n");
		}
		locker = false;

	}

}

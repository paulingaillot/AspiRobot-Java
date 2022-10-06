
public class ThreadEnvironnement extends Thread {

	public ThreadEnvironnement() {
	}

	public void run() {

		while (Thread.currentThread().isAlive()) {
			try {

				// Attente de 500 ms
				Thread.sleep(1500);
				
				generationObjet();

				Main.affichage();

			} catch (InterruptedException e) {

			}
		}

	}

	private void generationObjet() {
		// Coordonnées d'une case aléatoire
		int x = (int) (Math.random() * 5);
		int y = (int) (Math.random() * 5);

		int objet = (int) (Math.random() * 10);
		int proba = (int) (Math.random() * 100);

		if (proba < 20) { // 20% de chance qu'un item apparaisse
			if (objet < 7) // si oui 70% de chance qu'il s'agisse d'une saleté
				Main.tab[x][y].salete = true;
			else // 30% de chance qu'il s'agisse d'un bijou
				Main.tab[x][y].bijou = true;
		}

	}

}


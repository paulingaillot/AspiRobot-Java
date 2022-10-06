import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

public class ThreadAgent extends Thread {

	static Queue<String> STACK = new LinkedList<>();
	static int[][] tabHeuristique = new int[5][5];

	public ThreadAgent() {
	}

	public void run() {

		while (Thread.currentThread().isAlive()) {
			try {

				Thread.sleep(2500);

				majVueAspirateur();

				if (Main.aspi.carteAspi[Main.aspi.posx][Main.aspi.posy].bijou == true) {
					actionRamasser();
				} else if (Main.aspi.carteAspi[Main.aspi.posx][Main.aspi.posy].salete == true) {
					actionAspirer();
				}

				if (!STACK.isEmpty()) {
					String pos = STACK.remove();
					String[] popos = pos.split("-");
					int posx = Integer.parseInt(popos[0]);
					int posy = Integer.parseInt(popos[1]);

					Main.aspi.posx = posx;
					Main.aspi.posy = posy;

					Main.aspi.energie_consomme++;
					Main.affichage();
				}

			} catch (InterruptedException e) {

			}
		}

	}

	private void majVueAspirateur() {
		Main.aspi.update_rate++;
		if (Main.aspi.update_rate >= Main.aspi.update_rate_value || STACK.isEmpty()) {
			if (!STACK.isEmpty())
				Main.aspi.update_rate_value++;
			else
				Main.aspi.update_rate_value--;

			Main.aspi.carteAspi = new Case[5][5];

			for (int i = 0; i < 5; i++) {
				for (int j = 0; j < 5; j++) {
					Main.aspi.carteAspi[i][j] = new Case(i, j);
					Main.aspi.carteAspi[i][j].salete = Main.tab[i][j].salete;
					Main.aspi.carteAspi[i][j].bijou = Main.tab[i][j].bijou;
				}
			}

			Main.aspi.energie_consomme++;

			STACK = new LinkedList<String>();
			tabHeuristique = new int[5][5];

			boolean sale = false;
			List<String> arraytab = new ArrayList<>();
			for (int i = 0; i < 5; i++) {
				for (int j = 0; j < 5; j++) {
					if (Main.aspi.carteAspi[i][j].salete == true) {
						arraytab.add(i + "-" + j);
						sale = true;
					}
				}
			}

			if (sale == true) {

				if (Main.interface_used == 2) {
					for (int i = 0; i < 5; i++) {
						for (int j = 0; j < 5; j++) {
							Main.visited[i][j] = 0;
						}
					}

					CalculHeuristique3(arraytab);

					for (int i = 0; i < 5; i++) {
						for (int j = 0; j < 5; j++) {
							Main.visited[i][j] = 0;
						}
					}

					recherche_gloutone(Main.aspi.posx, Main.aspi.posy);
				} else {
					List<Case> chemin = BFS(Main.aspi.posx, Main.aspi.posy);
					STACK = new LinkedList<>();
					for (int i = 0; i < chemin.size(); i++) {
						STACK.add(chemin.get(i).x + "-" + chemin.get(i).y);
					}
				}
			}
			Main.aspi.update_rate = 0;
		}

	}

	private void actionRamasser() {
		Main.tab[Main.aspi.posx][Main.aspi.posy].bijou = false;
		Main.aspi.carteAspi[Main.aspi.posx][Main.aspi.posy].bijou = false;
		Main.aspi.bijouramasse++;
		Main.aspi.energie_consomme++;
	}

	private void actionAspirer() {
		Main.tab[Main.aspi.posx][Main.aspi.posy].salete = false;

		if (Main.tab[Main.aspi.posx][Main.aspi.posy].bijou == true) {
			Main.tab[Main.aspi.posx][Main.aspi.posy].bijou = false;
			ThreadPerf.bijou_aspiré++;
		}

		Main.aspi.carteAspi[Main.aspi.posx][Main.aspi.posy].salete = false;
		Main.aspi.energie_consomme++;
	}

	static void CalculHeuristique3(List<String> arraytab) {

		for (int k = 0; k < arraytab.size(); k++) {
			String result = arraytab.get(k);
			String[] results = result.split("-");
			int x = Integer.parseInt(results[0]);
			int y = Integer.parseInt(results[1]);

			for (int i = 0; i < 5; i++) {
				for (int j = 0; j < 5; j++) {
					int dist = (int) Math.sqrt(Math.pow(i - x, 2) + Math.pow(j - y, 2));
					if (tabHeuristique[i][j] > dist
							|| (tabHeuristique[i][j] == 0 && Main.aspi.carteAspi[i][j].salete == false))
						tabHeuristique[i][j] = dist;
				}
			}
		}
	}

	static void recherche_gloutone(int x, int y) {

		Main.visited[x][y] = 1;
		if (Main.aspi.carteAspi[x][y].salete == false) {

			int k1 = 99999;
			if (x + 1 < 5 && Main.visited[x + 1][y] == 0)
				k1 = (int) tabHeuristique[x + 1][y];
			int k2 = 99999;
			if (x - 1 >= 0 && Main.visited[x - 1][y] == 0)
				k2 = (int) tabHeuristique[x - 1][y];
			int k3 = 99999;
			if (y + 1 < 5 && Main.visited[x][y + 1] == 0)
				k3 = (int) tabHeuristique[x][y + 1];
			int k4 = 99999;
			if (y - 1 >= 0 && Main.visited[x][y - 1] == 0)
				k4 = (int) tabHeuristique[x][y - 1];

			int min = Math.min(Math.min(k1, k2), Math.min(k3, k4));

			if (min == k1 && min != 99999) {
				STACK.add((x + 1) + "-" + y);
				recherche_gloutone(x + 1, y);
			} else if (min == k2 && min != 99999) {
				STACK.add((x - 1) + "-" + y);
				recherche_gloutone(x - 1, y);
			} else if (min == k3 && min != 99999) {
				STACK.add(x + "-" + (y + 1));
				recherche_gloutone(x, y + 1);
			} else if (min == k4 && min != 99999) {
				STACK.add(x + "-" + (y - 1));
				recherche_gloutone(x, y - 1);
			}
		} else {
			STACK.add(x + "-" + y);
		}
	}

	static List<Case> BFS(int xCoord, int yCoord) {

		Case caseDepart = new Case(xCoord, yCoord);
		int x = caseDepart.x;
		int y = caseDepart.y;
		int[][] tabverif = new int[5][5];
		Map<Case, Case> cheminsParcourus = new HashMap<Case, Case>();

		tabverif[x][y] = 1;

		if (x + 1 < 5 && tabverif[x + 1][y] == 0) {
			changerChemin(cheminsParcourus, caseDepart, x + 1, y, tabverif);
		}
		if (x - 1 >= 0 && tabverif[x - 1][y] == 0) {
			changerChemin(cheminsParcourus, caseDepart, x - 1, y, tabverif);
		}
		if (y + 1 < 5 && tabverif[x][y + 1] == 0) {
			changerChemin(cheminsParcourus, caseDepart, x, y + 1, tabverif);
		}
		if (y - 1 >= 0 && tabverif[x][y - 1] == 0) {
			changerChemin(cheminsParcourus, caseDepart, x, y - 1, tabverif);
		}

		while (!STACK.isEmpty()) {
			String elem = STACK.remove();
			String[] elems = elem.split("-");

			x = Integer.parseInt(elems[0]);
			y = Integer.parseInt(elems[1]);

			if (Main.aspi.carteAspi[x][y].salete == true) {

				List<Case> shortestPath = new ArrayList<Case>();
				Case caseF = new Case(x, y);

				while (caseF != null) {
					shortestPath.add(caseF);
					caseF = cheminsParcourus.get(caseF);
				}
				Collections.reverse(shortestPath);
				shortestPath.remove(0);

				return shortestPath;

			} else {

				caseDepart = new Case(x, y);
				if (x + 1 < 5 && tabverif[x + 1][y] == 0) {
					changerChemin(cheminsParcourus, caseDepart, x + 1, y, tabverif);
				}
				if (x - 1 >= 0 && tabverif[x - 1][y] == 0) {
					changerChemin(cheminsParcourus, caseDepart, x - 1, y, tabverif);
				}
				if (y + 1 < 5 && tabverif[x][y + 1] == 0) {
					changerChemin(cheminsParcourus, caseDepart, x, y + 1, tabverif);
				}
				if (y - 1 >= 0 && tabverif[x][y - 1] == 0) {
					changerChemin(cheminsParcourus, caseDepart, x, y - 1, tabverif);
				}
			}
		}

		return new ArrayList<Case>();
	}

	private static void changerChemin(Map<Case, Case> savedPaths, Case depart, int eX, int eY, int[][] tabVerif) {
		STACK.add((eX + "-" + eY));
		Case caseSuivante = new Case(eX, eY);
		savedPaths.put(caseSuivante, depart);
		tabVerif[eX][eY] = 1;
	}
}

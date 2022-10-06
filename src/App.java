
import java.util.*;


public class App {

    static class Case {
        // public string etat = "P";
        public String salete = "N";
        public String bijou = "N";
        public int x;
		public int y;

		public Case() {
		};

		public Case(int x, int y) {
			this.x = x;
			this.y = y;
		}

        @Override
		public int hashCode() {
			return Objects.hash(x, y);
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Case other = (Case) obj;
			return x == other.x && y == other.y;
		}


    }

    static class Aspirateur {
        public int posx = 0;
        public int posy = 0;
        public int energie_consomme = 0;
        public int bijouramasse = 0;
        public Case[][] carteAspi = new Case[5][5];
        public int last_dist = -1;
        public int update_rate = 15;
        public int update_rate_value = 15;
    }

    static boolean locker = false;
    static int size = 5;
    static Case[][] tab = new Case[size][size];
    static Aspirateur aspi = new Aspirateur();
    static int[][] visited = new int[5][5];

    public static void affichage() {
        while (locker == true) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {

            }
        }
        locker = true;
        System.out.println("----- Mise a Jour de l'Etat de la piece ----- Performance : "+actual_perf);
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

    public static void main(String[] args) throws Exception {

        tab = new Case[5][5];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                tab[i][j] = new Case();
            }
        }

        Thread myThread;
        myThread = new Thread(new ThreadEnvironement());
        myThread.start();

        Thread myThread2;
        myThread2 = new Thread(new ThreadAgent());
        myThread2.start();

        Thread myThread3;
        myThread3 = new Thread(new ThreadPerf());
        myThread3.start();

    }

    static int temps=1;
    static int actual_perf=0; 
    static int bijou_aspiré=0;
    static class ThreadPerf extends Thread {
        public void run() {

            while (Thread.currentThread().isAlive()) {
                try {

                    // Attente de 500 ms
                    Thread.sleep(1000);

                    int case_propre =0;
                    for(int i=0; i<5; i++) {
                        for(int j=0; j<5; j++) {
                            if(tab[i][j].salete == "N") case_propre++;
                        }
                    }
                    temps++;

                   actual_perf = (int)((100*case_propre)/25-20*bijou_aspiré - (15*aspi.energie_consomme)/(temps));
                   if(actual_perf>100) actual_perf=100;

                } catch (InterruptedException e) {

                }
            }

        }
    }

    static class ThreadEnvironement extends Thread {
        public void run() {

            while (Thread.currentThread().isAlive()) {
                try {

                    // Attente de 500 ms
                    Thread.sleep(1500);

                    int x = (int) (Math.random() * 5);
                    int y = (int) (Math.random() * 5);
                    int objet = (int) (Math.random() * 10);
                    int proba = (int) (Math.random() * 100);

                    if (proba < 20) {
                        if (objet <7)
                            tab[x][y].salete = "O";
                        else
                            tab[x][y].bijou = "O";

                        affichage();
                    }

                } catch (InterruptedException e) {

                }
            }

        }
    }

    static int[][] tabHeuristique = new int[5][5];
    static void CalculHeuristique3(List<String> arraytab) {

        for(int k=0; k<arraytab.size(); k++) {
            String result = arraytab.get(k);
            String[] results = result.split("-");
            int x = Integer.parseInt(results[0]);
            int y = Integer.parseInt(results[1]);
          //  System.out.print(x+"-"+y+"\n");
            for(int i=0; i<5; i++) {
                for(int j=0; j<5; j++) {
                    int dist = (int)Math.sqrt(Math.pow(i - x, 2) + Math.pow(j - y, 2));
                    if(tabHeuristique[i][j] > dist || (tabHeuristique[i][j] == 0 && aspi.carteAspi[i][j].salete == "N"))tabHeuristique[i][j] = dist;
                }
            }
        }
    }


    static Queue<String> STACK = new LinkedList<>();
    static void recherche_gloutone(int x, int y) {

        // STACK.push(Integer.toString(x)+"-"+Integer.toString(y));
        visited[x][y] = 1;
        if (aspi.carteAspi[x][y].salete == "N") {

            int k1 = 99999;
            if (x + 1 < 5 && visited[x + 1][y] == 0)
                k1 = (int) tabHeuristique[x+1][y];
            int k2 = 99999;
            if (x - 1 >= 0 && visited[x - 1][y] == 0)
                k2 = (int) tabHeuristique[x-1][y];
            int k3 = 99999;
            if (y + 1 < 5 && visited[x][y + 1] == 0)
                k3 =(int) tabHeuristique[x][y+1];
            int k4 = 99999;
            if (y - 1 >= 0 && visited[x][y - 1] == 0)
                k4 = (int) tabHeuristique[x][y-1];

            int min = Math.min(Math.min(k1, k2), Math.min(k3, k4));
           // System.out.println(min + "");

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
		Map<Case, Case> savedPaths = new HashMap<Case, Case>();
		Case caseSuivante;

		tabverif[x][y] = 1;

		if (x + 1 < 5 && tabverif[x + 1][y] == 0) {
			STACK.add((int) (x + 1) + "-" + y);
			caseSuivante = new Case(x + 1, y);
			savedPaths.put(caseSuivante, caseDepart);
			// System.out.println(caseSuivante.toString() + "--" +
			// savedPaths.get(caseSuivante).toString());
			tabverif[x + 1][y] = 1;
		}
		if (x - 1 >= 0 && tabverif[x - 1][y] == 0) {
			STACK.add((int) (x - 1) + "-" + y);
			caseSuivante = new Case(x - 1, y);
			savedPaths.put(caseSuivante, caseDepart);
			// System.out.println(caseSuivante.toString() + "--" +
			// savedPaths.get(caseSuivante).toString());
			tabverif[x - 1][y] = 1;
		}
		if (y + 1 < 5 && tabverif[x][y + 1] == 0) {
			STACK.add(x + "-" + (y + 1));
			caseSuivante = new Case(x, y + 1);
			savedPaths.put(caseSuivante, caseDepart);
			// System.out.println(caseSuivante.toString() + "--" +
			// savedPaths.get(caseSuivante).toString());
			tabverif[x][y + 1] = 1;
		}
		if (y - 1 >= 0 && tabverif[x][y - 1] == 0) {
			STACK.add(x + "-" + (y - 1));
			caseSuivante = new Case(x, y - 1);
			savedPaths.put(caseSuivante, caseDepart);
			// System.out.println(caseSuivante.toString() + "--" +
			// savedPaths.get(caseSuivante).toString());
			tabverif[x][y - 1] = 1;
		}

		while (!STACK.isEmpty()) {
			String elem = STACK.remove();
			String[] elems = elem.split("-");
			// System.out.print(elem + " ");
			x = Integer.parseInt(elems[0]);
			y = Integer.parseInt(elems[1]);

			if (aspi.carteAspi[x][y].salete == "O") {
				// System.out.println(STACK);
				List<Case> shortestPath = new ArrayList<Case>();
				Case caseF = new Case(x, y);
				
				  //for (Map.Entry<Case, Case> entry : savedPaths.entrySet()) { Case key =
				  //entry.getKey(); Case value = entry.getValue(); 
                  //System.out.println("Cle: " +
				  //key.toString() + ", Valeur: " + value.toString()); }
				 

				while (caseF != null) {
					shortestPath.add(caseF);
					 //System.out.println("Ajout du noeud" + caseF.toString());
					caseF = savedPaths.get(caseF);
				}
				Collections.reverse(shortestPath);
				shortestPath.remove(0);
				//System.out.println("Chemin a parcourir :");
				//for (Case caseT : shortestPath) {
				// System.out.println(caseT.toString());
				//}
				return shortestPath;
				// System.out.println(solut);
			} else {

				caseDepart = new Case(x, y);
				if (x + 1 < 5 && tabverif[x + 1][y] == 0) {
					STACK.add((int) (x + 1) + "-" + y);
					caseSuivante = new Case(x + 1, y);
					savedPaths.put(caseSuivante, caseDepart);
					// System.out.println(caseSuivante.toString() + "--" +
					// savedPaths.get(caseSuivante).toString());
					tabverif[x + 1][y] = 1;
				}
				if (x - 1 >= 0 && tabverif[x - 1][y] == 0) {
					STACK.add((int) (x - 1) + "-" + y);
					caseSuivante = new Case(x - 1, y);
					savedPaths.put(caseSuivante, caseDepart);
					// System.out.println(caseSuivante.toString() + "--" +
					 //savedPaths.get(caseSuivante).toString());
					tabverif[x - 1][y] = 1;
				}
				if (y + 1 < 5 && tabverif[x][y + 1] == 0) {
					STACK.add(x + "-" + (y + 1));
					caseSuivante = new Case(x, y + 1);
					savedPaths.put(caseSuivante, caseDepart);
					// System.out.println(caseSuivante.toString() + "--" +
					// savedPaths.get(caseSuivante).toString());
					tabverif[x][y + 1] = 1;
				}
				if (y - 1 >= 0 && tabverif[x][y - 1] == 0) {
					STACK.add(x + "-" + (y - 1));
					caseSuivante = new Case(x, y - 1);
					savedPaths.put(caseSuivante, caseDepart);
					// System.out.println(caseSuivante.toString() + "--" +
					// savedPaths.get(caseSuivante).toString());
					tabverif[x][y - 1] = 1;
				}
			}

		}
		return new ArrayList<Case>();
	}

    
    static class ThreadAgent extends Thread {
        public void run() {

            while (Thread.currentThread().isAlive()) {
                try {

                    Thread.sleep(2500);

                    // Mise a jour vue aspirateur

                    aspi.update_rate++;
                    if (aspi.update_rate >= aspi.update_rate_value || STACK.isEmpty()) {
                        if (!STACK.isEmpty())
                            aspi.update_rate_value++;
                        else
                            aspi.update_rate_value--;

                        aspi.carteAspi = new Case[5][5];

                        for (int i = 0; i < 5; i++) {
                            for (int j = 0; j < 5; j++) {
                                aspi.carteAspi[i][j] = new Case();
                                aspi.carteAspi[i][j].salete = tab[i][j].salete;
                                aspi.carteAspi[i][j].bijou = tab[i][j].bijou;
                            }
                        }

                        aspi.energie_consomme++;

                        STACK = new LinkedList<String>();
                        tabHeuristique = new int[5][5];
      
                        boolean sale = false;
                        List<String> arraytab = new ArrayList<>();
                        for (int i = 0; i < 5; i++) {
                            for (int j = 0; j < 5; j++) {
                                if (aspi.carteAspi[i][j].salete == "O") {
                                    arraytab.add(i+"-"+j);
                                    sale = true;
                                }
                            }
                        }

                        if (sale == true) {
                            for (int i = 0; i < 5; i++) {
                                for (int j = 0; j < 5; j++) {
                                    visited[i][j] = 0;
                                }
                            }

                            CalculHeuristique3(arraytab);

                            for (int i = 0; i < 5; i++) {
                                for (int j = 0; j < 5; j++) {
                                    visited[i][j] = 0;
                                    //System.out.print(tabHeuristique[i][j]+" |");
                                }
                              //  System.out.print("\n");
                            }

                            recherche_gloutone(aspi.posx, aspi.posy);
                            
                            /*List<Case> chemin = BFS(aspi.posx, aspi.posy);
                            STACK = new LinkedList<>();
                            for(int i=0; i<chemin.size(); i++) {
                                STACK.add(chemin.get(i).x+"-"+chemin.get(i).y);
                            }*/
                        }
                        aspi.update_rate = 0;
                    }

                    // Poussiere trouvé = suppression

                    if (aspi.carteAspi[aspi.posx][aspi.posy].bijou == "O") {
                        tab[aspi.posx][aspi.posy].bijou = "N";
                        aspi.carteAspi[aspi.posx][aspi.posy].bijou = "N";
                        aspi.bijouramasse++;
                        aspi.energie_consomme++;
                    } else if (aspi.carteAspi[aspi.posx][aspi.posy].salete == "O") {
                        tab[aspi.posx][aspi.posy].salete = "N";

                        if( tab[aspi.posx][aspi.posy].bijou == "O") {
                            tab[aspi.posx][aspi.posy].bijou = "N";
                            bijou_aspiré++;
                        }
                        
                        aspi.carteAspi[aspi.posx][aspi.posy].salete = "N";
                        aspi.energie_consomme++;
                    }

                    if (!STACK.isEmpty()) {
                        String pos = STACK.remove();
                        String[] popos = pos.split("-");
                        int posx = Integer.parseInt(popos[0]);
                        int posy = Integer.parseInt(popos[1]);

                        aspi.posx = posx;
                        aspi.posy = posy;

                        aspi.energie_consomme++;
                        affichage();
                    }

                } catch (InterruptedException e) {

                }
            }

        }
    }

}


public class App {

    static class Case {
        // public string etat = "P";
        public String salete = "N";
        public String bijou = "N";
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

    public static void affichage() {
        while (locker == true) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {

            }
        }
        locker = true;
        System.out.println("----- Mise a Jour de l'Etat de la piece -----");
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                String salete = tab[i][j].salete;
                String bijou = tab[i][j].bijou;

                if (aspi.posx == i && aspi.posy == j) {

                    System.out.print("\u001B[44m A ");
                } else if (salete == "O" && bijou == "O") {

                    System.out.print("\u001B[8m T ");
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

    }

    static class ThreadEnvironement extends Thread {
        public void run() {

            while (Thread.currentThread().isAlive()) {
                try {

                    // Attente de 500 ms
                    Thread.sleep(1000);

                    int x = (int) (Math.random() * 5);
                    int y = (int) (Math.random() * 5);
                    int objet = (int) (Math.random() * 2);
                    int proba = (int) (Math.random() * 100);

                    if (proba < 20) {
                        if (objet == 0)
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

    static class ThreadAgent extends Thread {
        public void run() {

            while (Thread.currentThread().isAlive()) {
                try {

                    Thread.sleep(2500);

                    // Mise a jour vue aspirateur

                    aspi.update_rate++;
                    if (aspi.update_rate >= aspi.update_rate_value || aspi.last_dist == -1) {
                        if (aspi.last_dist != -1)
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
                        // System.out.print(aspi.update_rate_value);
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
                        tab[aspi.posx][aspi.posy].bijou = "N";

                        aspi.carteAspi[aspi.posx][aspi.posy].salete = "N";
                        aspi.carteAspi[aspi.posx][aspi.posy].bijou = "N";

                        aspi.energie_consomme++;
                    }

                    // Calcul distances poussière

                    int x = aspi.posx;
                    int y = aspi.posy;

                    int butx = 0;
                    int buty = 0;
                    int dist = -1;

                    for (int i = 0; i < size; i++) {
                        for (int j = 0; j < size; j++) {
                            if (x - i >= 0 && y - j >= 0) {
                                int dist1 = (int) Math.sqrt(Math.pow(x - (x - i), 2) + Math.pow(y - (y - j), 2));
                                if (aspi.carteAspi[x - i][y - j].salete == "O"
                                        || aspi.carteAspi[x - i][y - j].bijou == "O") {
                                    if (dist == -1 || dist1 < dist) {
                                        butx = x - i;
                                        buty = y - j;
                                        dist = dist1;
                                    }

                                }
                            }
                            if (x - i >= 0 && y + j < size) {
                                int dist1 = (int) Math.sqrt(Math.pow(x - (x - i), 2) + Math.pow(y - (y + j), 2));
                                if (aspi.carteAspi[x - i][y + j].salete == "O"
                                        || aspi.carteAspi[x - i][y + j].bijou == "O") {
                                    if (dist == -1 || dist1 < dist) {
                                        butx = x - i;
                                        buty = y + j;
                                        dist = dist1;
                                    }
                                }
                            }
                            if (x + i < size && y + j < size) {
                                int dist1 = (int) Math.sqrt(Math.pow(x - (x + i), 2) + Math.pow(y - (y + j), 2));
                                if (aspi.carteAspi[x + i][y + j].salete == "O"
                                        || aspi.carteAspi[x + i][y + j].bijou == "O") {
                                    if (dist == -1 || dist1 < dist) {
                                        butx = x + i;
                                        buty = y + j;
                                        dist = dist1;
                                    }
                                }
                            }
                            if (x + i < size && y - j >= 0) {
                                int dist1 = (int) Math.sqrt(Math.pow(x - (x + i), 2) + Math.pow(y - (y - j), 2));
                                if (aspi.carteAspi[x + i][y - j].salete == "O"
                                        || aspi.carteAspi[x + i][y - j].bijou == "O") {
                                    if (dist == -1 || dist1 < dist) {
                                        butx = x + i;
                                        buty = y - j;
                                        dist = dist1;
                                    }
                                }
                            }

                        }
                    }
                    // Deplacement

                    aspi.last_dist = dist;
                    if (dist != -1) {
                        if (butx < x) {
                            aspi.posx--;
                            aspi.energie_consomme++;
                            affichage();
                        } else if (butx > x) {
                            aspi.posx++;
                            aspi.energie_consomme++;
                            affichage();
                        } else if (buty < y) {
                            aspi.posy--;
                            aspi.energie_consomme++;
                            affichage();
                        } else if (buty > y) {
                            aspi.energie_consomme++;
                            aspi.posy++;
                            affichage();
                        }

                    }

                } catch (InterruptedException e) {

                }
            }

        }
    }

}

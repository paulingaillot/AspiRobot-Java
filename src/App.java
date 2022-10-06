import java.io.File;
import java.util.*;
import java.util.Stack;

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
    static int[][] visited = new int[5][5];

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

                    System.out.print("\u001B[33m T ");
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

    static int[][] tabHeuristique = new int[5][5];
    static Stack<String> stack5 = new Stack<String>();
    static int CalculHeuristique2(int x, int y) {

        

        stack5.push(x+"-"+y);
        while (!stack5.isEmpty()) {
            String value = stack5.pop();
            String[] values = value.split("-");

            x = Integer.parseInt(values[0]);
            y = Integer.parseInt(values[1]);

            visited[x][y] = 1;

            if (x + 1 < 5 && visited[x+1][y] == 0)
                stack5.push((x+1)+"-"+y);
            if (x - 1 >= 0 && visited[x-1][y] == 0)
               stack5.push((x-1)+"-"+y);
            if (y + 1 < 5 && visited[x][y+1] == 0)
                stack5.push((x)+"-"+(y+1));
            if (y - 1 >= 0&& visited[x][y-1] == 0)
                stack5.push((x)+"-"+(y-1));

            if(aspi.carteAspi[x][y].salete == "O") tabHeuristique[x][y] = 0;
            else {
                int k1 = 9999;
            int k2 = 9999;
            int k3 = 9999;
            int k4 = 9999;

            if (x + 1 < 5)
                k1 = tabHeuristique[x + 1][ y];
            if (x - 1 >= 0)
                k2 = tabHeuristique[x - 1][ y];
            if (y + 1 < 5)
                k3 = tabHeuristique[x][y + 1];
            if (y - 1 >= 0)
                k4 = tabHeuristique[x][ y - 1];

            tabHeuristique[x][y] = 1 + Math.min(Math.min(k1, k2), Math.min(k3, k4));
            }
        }
        return 0;

        /*if (visited[x][y] == 1) {
            if(tabHeuristique[x][y] == -1) return 9999; 
            else return tabHeuristique[x][y];
        }

        visited[x][y] = 1;
        if (aspi.carteAspi[x][y].salete == "N") {

            int k1 = 9999;
            int k2 = 9999;
            int k3 = 9999;
            int k4 = 9999;

            if (x + 1 < 5)
                k1 = CalculHeuristique2(x + 1, y);
            if (x - 1 >= 0)
                k2 = CalculHeuristique2(x - 1, y);
            if (y + 1 < 5)
                k3 = CalculHeuristique2(x, y + 1);
            if (y - 1 >= 0)
                k4 = CalculHeuristique2(x, y - 1);

            tabHeuristique[x][y] = 1 + Math.min(Math.min(k1, k2), Math.min(k3, k4));
            return tabHeuristique[x][y];
        } else {

            int k1 = 9999;
            int k2 = 9999;
            int k3 = 9999;
            int k4 = 9999;
            if (x + 1 < 5)
            k1 = CalculHeuristique2(x + 1, y);
        if (x - 1 >= 0)
            k2 = CalculHeuristique2(x - 1, y);
        if (y + 1 < 5)
            k3 = CalculHeuristique2(x, y + 1);
        if (y - 1 >= 0)
            k4 = CalculHeuristique2(x, y - 1);


            tabHeuristique[x][y] = 0;
            return 0;
        }*/

    }

    static void CalculHeuristique3(int x, int y) {
        for(int i=0; i<5; i++) {
            for(int j=0; j<5; j++) {
                int dist = (int)Math.sqrt(Math.pow(i - x, 2) + Math.pow(j - y, 2));
                tabHeuristique[i][j] = dist;
            }
        }
    }

    static void CalculHeuristique() {
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                if (aspi.carteAspi[i][j].salete == "O")
                    tabHeuristique[i][j] = 0;
                else if (((i + 1) < 5 && aspi.carteAspi[i + 1][j].salete == "O")
                        || ((i - 1) >= 0 && aspi.carteAspi[i - 1][j].salete == "O")
                        || ((j + 1) < 5 && aspi.carteAspi[i][j + 1].salete == "O")
                        || ((j - 1) >= 0 && aspi.carteAspi[i][j - 1].salete == "O")) {
                    int value = 5 - ((((i + 1) < 5 && aspi.carteAspi[i + 1][j].salete == "O") ? 1 : 0)
                            + (((i - 1) >= 0 && aspi.carteAspi[i - 1][j].salete == "O") ? 1 : 0)
                            + (((j + 1) < 5 && aspi.carteAspi[i][j + 1].salete == "O") ? 1 : 0)
                            + (((j - 1) >= 0 && aspi.carteAspi[i][j - 1].salete == "O") ? 1 : 0));
                    tabHeuristique[i][j] = value;
                } else
                    tabHeuristique[i][j] = 5;

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
            System.out.println(min + "");

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

    static Queue<String> STACK2 = new LinkedList<>();

    static void BFS(int x, int y) {

        int[][] tabverif = new int[5][5];

        tabverif[x][y] = 1;
        if (x + 1 < 5 && tabverif[x + 1][y] == 0)
            STACK2.add((int) (x + 1) + "-" + y);
        if (x - 1 >= 0 && tabverif[x - 1][y] == 0)
            STACK2.add((int) (x - 1) + "-" + y);
        if (y + 1 < 5 && tabverif[x][y + 1] == 0)
            STACK2.add(x + "-" + (y + 1));
        if (y - 1 >= 0 && tabverif[x][y - 1] == 0)
            STACK2.add(x + "-" + (y - 1));

        while (!STACK2.isEmpty()) {
            String elem = STACK2.remove();
            String[] elems = elem.split("-");
            // System.out.print(elem + " ");

            x = Integer.parseInt(elems[0]);
            y = Integer.parseInt(elems[1]);

            tabverif[x][y] = 1;

            if (aspi.carteAspi[x][y].salete == "O") {
                // System.out.println(STACK2);
            } else {
                if (x + 1 < 5 && tabverif[x + 1][y] == 0) {
                    STACK2.add((x + 1) + "-" + y);

                }
                if (x - 1 >= 0 && tabverif[x - 1][y] == 0) {
                    STACK2.add((x - 1) + "-" + y);
                }

                if (y + 1 < 5 && tabverif[x][y + 1] == 0) {
                    STACK2.add(x + "-" + (y + 1));
                }

                if (y - 1 >= 0 && tabverif[x][y - 1] == 0) {
                    STACK2.add(x + "-" + (y - 1));
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
                        // System.out.print(aspi.update_rate_value);

                        STACK = new LinkedList<String>();

                        tabHeuristique = new int[5][5];


                        for (int i = 0; i < 5; i++) {
                            for (int j = 0; j < 5; j++) {
                                visited[i][j] = 0;
                                tabHeuristique[i][j] = -1;
                            }
                        }

                       
                        int x = aspi.posx;
                        int y = aspi.posy;
                        boolean sale = false;
                        for (int i = 0; i < 5; i++) {
                            for (int j = 0; j < 5; j++) {
                                if (aspi.carteAspi[i][j].salete == "O") {
                                    x = i;
                                    y = j;
                                    sale = true;
                                }
                            }
                        }
                        System.out.println(sale);
                        if (sale == true) {
                            for (int i = 0; i < 5; i++) {
                                for (int j = 0; j < 5; j++) {
                                    visited[i][j] = 0;
                                }
                            }

                            System.out.println(x+"-"+y);
                            CalculHeuristique3(x, y);

                            for (int i = 0; i < 5; i++) {
                                for (int j = 0; j < 5; j++) {
                                    visited[i][j] = 0;
                                    System.out.print(tabHeuristique[i][j] + "| ");
                                }
                                System.out.print("1\n");
                            }

                            for (int i = 0; i < 5; i++) {
                                for (int j = 0; j < 5; j++) {
                                    visited[i][j] = 0;
                                }
                            }

                            recherche_gloutone(aspi.posx, aspi.posy);
                            System.out.println(STACK);
                        }
                        // BFS(aspi.posx, aspi.posy);

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

                    // Calcul distances poussière

                    /*
                     * int x = aspi.posx;
                     * int y = aspi.posy;
                     * 
                     * int butx = 0;
                     * int buty = 0;
                     * int dist = -1;
                     * 
                     * for (int i = 0; i < size; i++) {
                     * for (int j = 0; j < size; j++) {
                     * if (x - i >= 0 && y - j >= 0) {
                     * int dist1 = (int) Math.sqrt(Math.pow(x - (x - i), 2) + Math.pow(y - (y - j),
                     * 2));
                     * if (aspi.carteAspi[x - i][y - j].salete == "O"
                     * || aspi.carteAspi[x - i][y - j].bijou == "O") {
                     * if (dist == -1 || dist1 < dist) {
                     * butx = x - i;
                     * buty = y - j;
                     * dist = dist1;
                     * }
                     * 
                     * }
                     * }
                     * if (x - i >= 0 && y + j < size) {
                     * int dist1 = (int) Math.sqrt(Math.pow(x - (x - i), 2) + Math.pow(y - (y + j),
                     * 2));
                     * if (aspi.carteAspi[x - i][y + j].salete == "O"
                     * || aspi.carteAspi[x - i][y + j].bijou == "O") {
                     * if (dist == -1 || dist1 < dist) {
                     * butx = x - i;
                     * buty = y + j;
                     * dist = dist1;
                     * }
                     * }
                     * }
                     * if (x + i < size && y + j < size) {
                     * int dist1 = (int) Math.sqrt(Math.pow(x - (x + i), 2) + Math.pow(y - (y + j),
                     * 2));
                     * if (aspi.carteAspi[x + i][y + j].salete == "O"
                     * || aspi.carteAspi[x + i][y + j].bijou == "O") {
                     * if (dist == -1 || dist1 < dist) {
                     * butx = x + i;
                     * buty = y + j;
                     * dist = dist1;
                     * }
                     * }
                     * }
                     * if (x + i < size && y - j >= 0) {
                     * int dist1 = (int) Math.sqrt(Math.pow(x - (x + i), 2) + Math.pow(y - (y - j),
                     * 2));
                     * if (aspi.carteAspi[x + i][y - j].salete == "O"
                     * || aspi.carteAspi[x + i][y - j].bijou == "O") {
                     * if (dist == -1 || dist1 < dist) {
                     * butx = x + i;
                     * buty = y - j;
                     * dist = dist1;
                     * }
                     * }
                     * }
                     * 
                     * }
                     * }
                     * // Deplacement
                     * 
                     * aspi.last_dist = dist;
                     * if (dist != -1) {
                     * if (butx < x) {
                     * aspi.posx--;
                     * aspi.energie_consomme++;
                     * affichage();
                     * } else if (butx > x) {
                     * aspi.posx++;
                     * aspi.energie_consomme++;
                     * affichage();
                     * } else if (buty < y) {
                     * aspi.posy--;
                     * aspi.energie_consomme++;
                     * affichage();
                     * } else if (buty > y) {
                     * aspi.energie_consomme++;
                     * aspi.posy++;
                     * affichage();
                     * }
                     * 
                     * }
                     */

                } catch (InterruptedException e) {

                }
            }

        }
    }

}

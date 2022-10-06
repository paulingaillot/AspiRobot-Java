
public class ThreadPerf extends Thread {
		
	private int temps;
	public static int bijou_aspiré;
	    
	public ThreadPerf() {
		temps = 1;
		Main.actual_perf = 0;
		bijou_aspiré = 0;
	}

	public void run() {

		while (Thread.currentThread().isAlive()) {
			try {
				// Attente de 500 ms
				Thread.sleep(1000);
				int case_propre = nombreDeCasesPropres();
				temps++;

				Main.actual_perf = calculPerformance(case_propre, Main.aspi.energie_consomme);
			} catch (InterruptedException e) {

			}
		}
	}

	private int nombreDeCasesPropres() {
		int case_propre = 0;
		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < 5; j++) {
				if (Main.tab[i][j].salete == "N")
					case_propre++;
		}
	}
	return case_propre;
}

	private int calculPerformance(int case_propre, int energie_conso) {
		Main.actual_perf = (int) ((100 * case_propre) / 25 - 20 * bijou_aspiré
				- (15 * Main.aspi.energie_consomme) / (temps));
		if (Main.actual_perf > 100)
			Main.actual_perf = 100;
		return Main.actual_perf;
	}
}

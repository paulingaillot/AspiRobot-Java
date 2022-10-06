
public class Aspirateur {
	public int posx;
	public int posy;
	public int energie_consomme;
	public int bijouramasse;
	public Case[][] carteAspi;
	public int last_dist;
	public int update_rate;
	public int update_rate_value;

	public Aspirateur() {
		posx = 0;
		posy = 0;
		energie_consomme = 0;
		bijouramasse = 0;
		carteAspi = new Case[5][5];
		last_dist = -1;
		update_rate = 15;
		update_rate_value = 15;
	}

}

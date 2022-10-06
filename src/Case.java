import java.util.Objects;

public class Case {
	public boolean salete;
	public boolean bijou;
	public int x;
	public int y;

	public Case(int posX, int posY) {
		this.salete = false;
		this.bijou = false;
		this.x = posX;
		this.y = posY;
	};

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

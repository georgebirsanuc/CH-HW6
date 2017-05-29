
public class ElementMatrice implements Comparable<ElementMatrice> {

	double valoare;
	int linie;
	int coloana;

	public ElementMatrice(double valoare, int linie, int coloana) {
		this.valoare = valoare;
		this.linie = linie;
		this.coloana = coloana;
	}

	public double getValoare() {
		return valoare;
	}

	public void setValoare(double valoare) {
		this.valoare = valoare;
	}

	public int getLinie() {
		return linie;
	}

	public void setLinie(int linie) {
		this.linie = linie;
	}

	public int getColoana() {
		return coloana;
	}

	public void setColoana(int coloana) {
		this.coloana = coloana;
	}

	@Override
	public int compareTo(ElementMatrice element) {
		// ElementMatrice element = (ElementMatrice) o;
		if (this.linie > element.linie) {
			return 1;
		} else if (this.linie < element.linie) {
			return -1;
		} else {
			if (this.coloana > element.coloana) {
				return 1;
			}
			return -1;
		}
	}

	public String toString() {
		return "Linie:  " + this.linie + "  ; coloana: " + this.coloana + "  ; valoare: " + this.valoare;
	}

}

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class Main {

	public static MatriceRara AFis, ARand;
	public static double epsilon = 0.00000000001;

	public static void main(String[] args) {

	}

	public static MatriceRara initMatriceFisier() throws NumberFormatException, IOException {
		// TODO remove '= 0'
		int n, maxNN = 0, i, j, lastI = 0;
		double[] d, val, col;
		double valoare;
		String line;

		BufferedReader br = new BufferedReader(new FileReader(new File("m_rar_sim_2017.txt")));

		n = Integer.parseInt(br.readLine());

		while ((line = br.readLine()) != null) {
			String[] values = line.split(", ");
			valoare = Double.parseDouble(values[0]);
			i = Integer.parseInt(values[1]);
			j = Integer.parseInt(values[2]);
			if (Math.abs(valoare) > epsilon) {
//				if (val[) {
//					
//				}
			}
		}

		br.close();
		return new MatriceRara();
		// return new MatriceRara(n, maxNN, d, val, col);
	}
}

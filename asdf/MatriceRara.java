
public class MatriceRara {

	double[] d, val, col;
	int n, maxNN;
	int[] colStart;

	MatriceRara(){}
	
	MatriceRara(int n, int maxNN, double[] d, double[] val, double[] col) {
		this.n = n;
		this.maxNN = maxNN;
		this.d = d;
		this.val = val;
		this.col = col;
	}

}

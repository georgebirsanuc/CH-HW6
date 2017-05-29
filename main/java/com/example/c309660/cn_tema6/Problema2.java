package com.example.c309660.cn_tema6;

import android.content.Context;
import android.content.res.AssetManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.*;
import java.io.*;

import Jama.*;

public class Problema2 extends AppCompatActivity {

    public static TextView resultTextView;
    public static TextView normaTextView;
    public static TextView valoriSingulareTextView;

    public static TextView matriceaAsView;
    public static EditText sColoaneEditText;

    public static int sColoane;
    public static double epsilonExponent = -9;
    public static double epsilon = Math.pow(10, epsilonExponent);
    public static MatriceRara Afisier = new MatriceRara();
    public static MatriceRara Arandom = new MatriceRara();
    public static Random rand = new Random();
    public static double[][] AmatriceClasica;
    public static double[][] As;

    public static int linieMinElementNenule = 1;
    public static int linieMaxElementNenule = 10;
    public static int pGlobal, nGlobal;
    public static Matrix valoriSingulare;
    public static Matrix U;
    public static Matrix V;
    public static Matrix S;
    public static double[][] UoriS;
    public static double[][] Vt;
    public static double[][] UoriSoriVt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        pGlobal = Variabile.p;
        nGlobal = Variabile.n;

        Context context = this.getApplicationContext();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_problema2);

        resultTextView = (TextView) findViewById(R.id.resultTextView);
        normaTextView = (TextView) findViewById(R.id.normaTextView);
        sColoaneEditText = (EditText) findViewById(R.id.sColoaneEditText);
        matriceaAsView = (TextView) findViewById(R.id.matriceaAsView);
        valoriSingulareTextView = (TextView) findViewById(R.id.valoriSingulareTextView);

        try {
            descompunereValoriSingulare(AmatriceClasica, resultTextView);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static int getRandomInteger(int linieMinElementNenule, int linieMaxElementNenule) {
        return (linieMinElementNenule + (int)(Math.random() * ((linieMaxElementNenule - linieMinElementNenule) + 1)));
    }

    public static double getRandomDouble(Random rand, double rangeMin, double rangeMax ) {
        return (rangeMin + (rangeMax - rangeMin) * rand.nextDouble());
    }

    public static void afiseazaMatrice(Matrix matrix) {
        int n = matrix.getRowDimension();
        int m = matrix.getColumnDimension();
        double[][] matrice = matrix.getArray();
        for(int i=0; i<n; i++) {
            for(int j=0; j<m; j++) {
                System.out.print("" + matrice[i][j] + "  ;  ");
            }
            System.out.println();
        }
        System.out.println();
    }

    public static boolean isMatriceaSimetrica(MatriceRara A) {

        int n = A.getN();
        int[] inceputLinii = A.getInceputLinii();
        double[] val = A.getVal();
        int[] col = A.getCol();

        int liniaCurenta = 0;
        int coloanaCurenta = 0;
        double valoareCurenta = 0;

        boolean coloanaGasita = false;
        boolean valoareGasita = false;

        while(liniaCurenta < n) {

            for(int i=inceputLinii[liniaCurenta]; (liniaCurenta < n) && i<inceputLinii[liniaCurenta+1]-1; i++) {

                coloanaGasita = false;
                valoareGasita = false;

                valoareCurenta = val[i];
                coloanaCurenta = col[i];
                int inceputulLinieiB = inceputLinii[coloanaCurenta-1];

                for(int j=inceputulLinieiB; j<inceputLinii[coloanaCurenta]; j++) {
                    if((col[j]-1)==liniaCurenta) {
                        coloanaGasita = true;
                        if(valoareCurenta == val[j]) {
                            valoareGasita = true;
                            break;
                        }
                    }
                }

                if(coloanaGasita==false || valoareGasita==false) {
                    break;
                }

            }

            if(coloanaGasita==false || valoareGasita==false) {
                break;
            }
            liniaCurenta++;

        }

        if(coloanaGasita==false || valoareGasita==false) {
            return false;
        }

        return true;

    }

    public void calculeazaNorma(View v) {
        int p = pGlobal;
        int n = nGlobal;
        sColoane = Integer.parseInt(sColoaneEditText.getText().toString());

        for(int j=0; j<p; j++) {
            for(int k=0; k<n; k++) {
                As[j][k] = 0;
            }
        }

        String matriceaAsViewString = "";

        for(int i=0; i<sColoane; i++) {
            for(int j=0; j<p; j++) {
                for(int k=0; k<n; k++) {
                    As[j][k] +=  valoriSingulare.getArray()[0][i] * (U.getArray()[j][i] * V.getArray()[k][i]);

                }
            }
        }

        for(int j=0; j<p; j++) {
            for(int k=0; k<n; k++) {
                matriceaAsViewString +=  As[j][k] + "  ;  ";
            }
            matriceaAsViewString += "\n\n";
        }

        int sumaCurenta = 0;
        System.out.println("Matricea As : ");
        afiseazaMatrice(new Matrix(As));

        double max2 = -1;
        for(int i=0; i<p; i++) {
            sumaCurenta = 0;
            for(int j=0; j<n; j++) {
                sumaCurenta += Math.abs(AmatriceClasica[i][j] - As[i][j]);
            }
            if(sumaCurenta > max2) {
                max2 = sumaCurenta;
            }
        }

        System.out.println("Norma ||A - As|| este  :  " + max2);
        normaTextView.setText("Norma ||A - As|| este  :  " + max2);
        matriceaAsView.setText(matriceaAsViewString);
        matriceaAsView.setMovementMethod(new ScrollingMovementMethod());

        System.out.println();
    }

    public static void descompunereValoriSingulare(double[][] A, TextView resultTextView) throws NumberFormatException, IOException {

        String resultString = "";

        int n = nGlobal;
        int p = pGlobal;

        int rangMatrice = 0;
        double numarConditionare = 0;


        A = new double[p][n];
        As = new double[p][n];

        for(int i=0; i<p; i++) {

            for(int j=0; j<n; j++) {

                A[i][j] = getRandomDouble(rand, 1, 100);

            }
        }

		/*
		int n;
		String line = "";

		FileInputStream inputStream = new FileInputStream(filename);
		Scanner scanner = new Scanner(inputStream);
		Scanner input = new Scanner(System.in);
		DataInputStream in = new DataInputStream(inputStream);
		BufferedReader bf = new BufferedReader(new InputStreamReader(in));

		n = Integer.parseInt(bf.readLine());

		bf.readLine(); //empty line

		A = new double[n][n];

		while ((line = bf.readLine()) != null)
        {
        	String[] values = line.split(", ");
        	double valoareCurenta = Double.parseDouble(values[0]);
        	int linieCurenta = Integer.parseInt(values[1]);
        	int coloanaCurenta = Integer.parseInt(values[2]);

        	A[linieCurenta][coloanaCurenta] = valoareCurenta;
        }
        */

        Matrix matrixA = new Matrix(A);

        SingularValueDecomposition s = matrixA.svd();
        U = s.getU();
        S = s.getS();
        V = s.getV();

        valoriSingulare = new Matrix(s.getSingularValues(), 1);

        System.out.println("Matricea A generata aleator : ");
        afiseazaMatrice(matrixA);

        System.out.println();
        System.out.println("Matricea U : ");
        afiseazaMatrice(U);

        System.out.println();
        System.out.println("Matricea S : ");
        afiseazaMatrice(S);

        System.out.println();
        System.out.println("Matricea V : ");
        afiseazaMatrice(V);

        System.out.println();
        System.out.println("Valorile singulare : ");

        String valoriSingulareString = "";

        afiseazaMatrice(valoriSingulare);

        rangMatrice = s.rank();
        numarConditionare = s.cond();

        for(int q=0; q<rangMatrice; q++) {
            valoriSingulareString += String.valueOf(valoriSingulare.getArray()[0][q]) + "  ;  ";
        }

        resultString = "Rangul matricii A : " + rangMatrice + "\n";

        System.out.println();
        System.out.println("Rangul matricii A : " + rangMatrice);

        System.out.println();
        System.out.println("Numarul de conditionare a matricii A : " + numarConditionare);
        resultString += "Numarul de conditionare a matricii A : " + numarConditionare + "\n\n";

        UoriS = inmultireMatriciClasice(U.getArray(), S.getArray());
        Vt = getMatriceTranspusa(V.getArray());
        UoriSoriVt = inmultireMatriciClasice(UoriS, Vt);

        double max = -1;
        double sumaCurenta = 0;

        for(int i=0; i<p; i++) {
            sumaCurenta = 0;
            for(int j=0; j<n; j++) {
                sumaCurenta += Math.abs(A[i][j] - UoriSoriVt[i][j]);
            }
            if(sumaCurenta > max) {
                max = sumaCurenta;
            }
        }

        System.out.println("Norma ||A - USVt|| este  :  " + max);

        resultString += "Norma ||A - USVt|| este  :  " + max + "\n\n";

        resultTextView.setText(resultString);

        valoriSingulareTextView.setText(valoriSingulareString);
        valoriSingulareTextView.setMovementMethod(new ScrollingMovementMethod());

        System.out.println();

        AmatriceClasica = A;

    }

    public static double[][] inmultireMatriciClasice(double a[][], double b[][]){

        if(a.length == 0) return new double[0][0];
        if(a[0].length != b.length) return null; //invalid dims

        int n = a[0].length;
        int m = a.length;
        int p = b[0].length;
        double[][] rezultat = new double[m][p];

        for(int i = 0;i < m;i++){
            for(int j = 0;j < p;j++){
                for(int k = 0;k < n;k++){
                    rezultat[i][j] += a[i][k] * b[k][j];
                }
            }
        }
        return rezultat;

    }

    public static void aplicaMetodaPuterii(MatriceRara A) {

        int n = A.getN();
        int[] inceputLinii = A.getInceputLinii();
        double[] d = A.getD();
        double[] val = A.getVal();
        int[] col = A.getCol();
        double[] v = new double[n];
        double[] w = new double[n];
        double[] x = new double[n];

        double sumaNormaX = 0;
        double normaX = 0;

        double sumaNormaVverificare = 0;
        double normaVverificare = 0;

        int kMax = 1000000;

        for(int i=0; i<n; i++) {
            double valoareRandom = getRandomDouble(rand, 1, n-1);
            x[i] = valoareRandom;
            sumaNormaX += Math.pow((Math.abs(x[i])), 2);
        }

        normaX = Math.sqrt(sumaNormaX);

        for(int i=0; i<n; i++) {
            v[i] = (1/normaX) * x[i];
            sumaNormaVverificare += Math.pow((Math.abs(v[i])), 2);
        }

        normaVverificare = Math.sqrt(sumaNormaVverificare);

        System.out.println("Vectorul V initial are norma euclidiana :  " + normaVverificare);

        int liniaCurenta = 0;
        double sumaCurenta = 0, produsCurent = 0;

        while(liniaCurenta<n) {

            sumaCurenta = 0;

            sumaCurenta = sumaCurenta + d[liniaCurenta] * v[liniaCurenta];

            for(int index=inceputLinii[liniaCurenta]; (liniaCurenta < n) && index<inceputLinii[liniaCurenta+1]-1; index++) {
                produsCurent = val[index] * v[col[index]-1];
                sumaCurenta = sumaCurenta + produsCurent;
            }

            w[liniaCurenta] = sumaCurenta;
            liniaCurenta++;

        }

        double landa = 0;

        for(int i=0; i<n; i++) {
            landa += w[i] * v[i];
        }

        int k=0;
        double sumaNorma = 0;
        double norma = 0;
        double normaVerificare = 0;
        double sumaNormaVerificare = 0;
        boolean verificare1 = false;
        boolean verificare2 = false;
        boolean reluareCalcul = true;


        while(reluareCalcul && epsilonExponent < (-5)) {

            do {
                sumaNorma = 0;
                sumaNormaVerificare = 0;
                for(int i=0; i<n; i++) {
                    sumaNorma += Math.pow((Math.abs(w[i])), 2);
                }
                norma = Math.sqrt(sumaNorma);
                for(int i=0; i<n; i++) {
                    v[i] = (1/norma) * w[i];
                }

                liniaCurenta = 0;
                sumaCurenta = 0; produsCurent = 0;

                while(liniaCurenta<n) {

                    sumaCurenta = 0;

                    sumaCurenta = sumaCurenta + d[liniaCurenta] * v[liniaCurenta];

                    for(int index=inceputLinii[liniaCurenta]; (liniaCurenta < n) && index<inceputLinii[liniaCurenta+1]-1; index++) {
                        produsCurent = val[index] * v[col[index]-1];
                        sumaCurenta = sumaCurenta + produsCurent;
                    }

                    w[liniaCurenta] = sumaCurenta;
                    liniaCurenta++;

                }

                landa = 0;

                for(int i=0; i<n; i++) {
                    landa += w[i] * v[i];
                }

                k++;

                for(int i=0; i<n; i++) {
                    sumaNormaVerificare += Math.pow((Math.abs(w[i] - landa * v[i])), 2);
                }

                normaVerificare = Math.sqrt(sumaNormaVerificare);

                verificare1 = (normaVerificare > n*epsilon);
                verificare2 = (k<=kMax);

            } while(verificare1 && verificare2);

            if(verificare2 == false) {
                reluareCalcul = true;
                epsilonExponent++;
                epsilon = Math.pow(10, epsilonExponent);
            }
            else {
                reluareCalcul = false;
            }

        }

        System.out.println("Cea mai mare valoare proprie a matricii este  :  " + landa);
        System.out.println("Vectorul propriu asociat :  ");

        for(int i=0; i<n; i++) {
            System.out.print(v[i] + "  ;  ");
        }

        System.out.println();

    }

    public static void initStructuresRandom(MatriceRara A) throws IOException {
        int n = 600;
        double[] d = new double[n];
        int[] inceputLinii = new int[n+1];
        double[] b = new double[n];
        double[] x = new double[n];
        int[] col;
        double[] val;

        List<ElementMatrice> elementeMatrice = new ArrayList<ElementMatrice>();
        ArrayList<HashMap<Integer, Double>> elementeMap = new ArrayList<HashMap<Integer, Double>>();
        for(int i=0; i<n; i++) {
            elementeMap.add(new HashMap<Integer, Double>());
        }
        for(int i=0; i<n; i++) {
            int numarElemNenule = getRandomInteger(1, 10);
            for(int j=elementeMap.get(i).size(); j < numarElemNenule; j++) {
                int coloanaRandom = -1;
                do {
                    coloanaRandom = getRandomInteger(0, n-1);
                } while((elementeMap.get(i).get(new Integer(coloanaRandom)) != null) || (elementeMap.get(coloanaRandom).size() >= 10));
                double valoareRandom = getRandomDouble(rand, 1, 50);
                elementeMap.get(i).put(new Integer(coloanaRandom), new Double(valoareRandom));
                elementeMatrice.add(new ElementMatrice(valoareRandom, i, coloanaRandom));

                if(i != coloanaRandom) {
                    elementeMap.get(coloanaRandom).put(new Integer(i), new Double(valoareRandom));
                    elementeMatrice.add(new ElementMatrice(valoareRandom, coloanaRandom, i));
                } else {
                    d[i] = valoareRandom;
                }
            }
        }
        Collections.sort(elementeMatrice);

        val = new double[elementeMatrice.size() + n + 1];
        col = new int[elementeMatrice.size() + n + 1];

        int ultimaLinieParcursa = -1;

        int indexLinie = 0;
        val[0] = 0;
        col[0] = -1;

        int f = 0;

        int ultimIndex = elementeMatrice.size() + n + 1;

        for(int i=0; i<elementeMatrice.size(); i++) {

            ElementMatrice elementCurent = elementeMatrice.get(i);

            f++;

            if(elementCurent.linie != ultimaLinieParcursa) {

                for(int z=0; z<(elementCurent.linie-ultimaLinieParcursa); z++) {
                    if(indexLinie > 0) {
                        val[f] = 0;
                        col[f] = -(indexLinie + 1);
                        f++;
                    }

                    inceputLinii[indexLinie] = f;

                    indexLinie++;

                }
                ultimaLinieParcursa = elementCurent.linie;
            }


            val[f] = elementCurent.getValoare();
            col[f] = elementCurent.getColoana() + 1;

            if(i == (elementeMatrice.size()-1) && (f < elementeMatrice.size() + n - 1)) {

                for(int u=f+1; u<elementeMatrice.size() + n + 1; u++) {
                    inceputLinii[indexLinie] = u+1;
                    indexLinie++;
                    col[u] = -indexLinie;
                }
                indexLinie--;
            }


        }

        inceputLinii[indexLinie] = ultimIndex;
        col[elementeMatrice.size() + n] = -(n+1);

        A.setN(n);
        A.setMaxNenuleLinie(10);
        A.setInceputLinii(inceputLinii);
        A.setB(b);
        A.setX(x);
        A.setD(d);
        A.setCol(col);
        A.setVal(val);

    }

    public static void initStructuresFisier(String filename, MatriceRara A, boolean verificaElementeNenule, Context context) throws IOException {

        int n;
        int maxNenuleLinie = -1;
        List<ElementMatrice> elemente = new ArrayList<ElementMatrice>();
        int[] inceputLinii;
        int[] col;
        double[] val;
        double[] b;
        double[] x;
        double[] d;
        String line = "";

        AssetManager am = context.getAssets();
        InputStream inputStream = am.open(filename);
        Scanner scanner = new Scanner(inputStream);
        Scanner input = new Scanner(System.in);
        DataInputStream in = new DataInputStream(inputStream);
        BufferedReader bf = new BufferedReader(new InputStreamReader(in));

        elemente = new ArrayList<ElementMatrice>();

        n = Integer.parseInt(bf.readLine());

        bf.readLine(); //empty line

        b = new double[n];
        x = new double[n];
        d = new double[n];

        inceputLinii = new int[n+1];

        while ((line = bf.readLine()) != null)
        {
            String[] values = line.split(", ");
            double valoareCurenta = Double.parseDouble(values[0]);
            int linieCurenta = Integer.parseInt(values[1]);
            int coloanaCurenta = Integer.parseInt(values[2]);

            if(Math.abs(valoareCurenta) > epsilon) { //diferit de 0
                if(linieCurenta == coloanaCurenta) {
                    d[linieCurenta] += valoareCurenta;
                }
                else {
                    elemente.add(new ElementMatrice(valoareCurenta, linieCurenta, coloanaCurenta));
                }
            }
            else {
                System.out.println("Elementele din fisier nu sunt nenule. EXIT");
                return;
            }

        }

        val = new double[elemente.size() + n + 1];
        col = new int[elemente.size() + n + 1];

        Collections.sort(elemente);

        int y = 0;

        while(y<elemente.size()) { //elementele cu acelasi linie si coloana sunt adunate
            int q = y+1;
            while((q<elemente.size()) && (elemente.get(y).getLinie() == elemente.get(q).getLinie())) {
                if(elemente.get(y).getColoana() == elemente.get(q).getColoana()) {
                    elemente.get(y).setValoare(elemente.get(y).getValoare() + elemente.get(q).getValoare());
                    elemente.remove(q);
                    q--;
                }

                q++;
            }
            y++;
        }

        int ultimaLinieParcursa = -1;

        int indexLinie = 0;
        val[0] = 0;
        col[0] = -1;

        int f = 0;

        int ultimIndex = elemente.size() + n + 1;

        for(int i=0; i<elemente.size(); i++) {

            ElementMatrice elementCurent = elemente.get(i);

            f++;

            if(elementCurent.linie != ultimaLinieParcursa) {

                for(int z=0; z<(elementCurent.linie-ultimaLinieParcursa); z++) {
                    if(indexLinie > 0) {
                        val[f] = 0;
                        col[f] = -(indexLinie + 1);
                        f++;
                    }

                    inceputLinii[indexLinie] = f;


                    if((indexLinie>0)) {
                        int numarElementeNenuleLiniaCurenta = inceputLinii[indexLinie] - inceputLinii[indexLinie-1];
                        if(numarElementeNenuleLiniaCurenta > 10 && verificaElementeNenule) {
                            System.out.println("Sunt mai mult de 10 elemente nenule pe linia " + indexLinie);
                        }
                        if(numarElementeNenuleLiniaCurenta > maxNenuleLinie) {
                            maxNenuleLinie = numarElementeNenuleLiniaCurenta;
                        }
                    }

                    indexLinie++;

                }
                ultimaLinieParcursa = elementCurent.linie;
            }


            val[f] = elementCurent.getValoare();
            col[f] = elementCurent.getColoana() + 1;

            if(i == (elemente.size()-1) && (f < elemente.size() + n - 1)) {

                for(int u=f+1; u<elemente.size() + n + 1; u++) {
                    inceputLinii[indexLinie] = u+1;
                    indexLinie++;
                    col[u] = -indexLinie;
                }
                indexLinie--;
            }


        }

        inceputLinii[indexLinie] = ultimIndex;

        A.setN(n);
        A.setMaxNenuleLinie(maxNenuleLinie);
        A.setInceputLinii(inceputLinii);
        A.setB(b);
        A.setX(x);
        A.setD(d);
        A.setCol(col);
        A.setVal(val);

    }

    public static double[][] getMatriceTranspusa(double [][] m) {
        double[][] temp = new double[m[0].length][m.length];
        for (int i = 0; i < m.length; i++)
            for (int j = 0; j < m[0].length; j++)
                temp[j][i] = m[i][j];
        return temp;
    }


}

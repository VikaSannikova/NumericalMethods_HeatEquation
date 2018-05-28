package HeatEquation;

import static cern.clhep.PhysicalConstants.pi;
import static java.lang.Math.cos;
import static java.lang.Math.pow;
import static java.lang.Math.sin;

public class Implicit {
    public int cols;
    public int rows;
    public double[][] U;
    public double[][] A;
    public double[] alfa;
    public double L;
    public double T;
    public double D;
    public double h;
    public double s;
    public double q;
    public double lambda;

    public double U_xt(double x, double[] alfa, double lambda){
        double result = 0;
        for (int i = 0; i < alfa.length ; i++) {
            result += alfa[i] * cos(i*lambda*x);
        }
        return result;
    }

    public double b_x(double x){
        //return sin(x)*cos(x);
        //return x;
        return sin(x)*cos(x);
        //return 0;
    }

    public double[] gaussMethod(double[][] _A, double[] _B){
        double [][] A = new double [_A.length][_A[0].length];
    	double [] B = new double [_B.length];
        for (int i = 0; i < _A.length; i++) {
		    for (int j = 0; j < _A.length; j++) {
	    		A[i][j] = _A[i][j];
	    	}
	    	B[i] = _B[i];
	    }
	    double d, s;
	    double [] x = new double[B.length];
	    double [] tmp = A[0].clone();
	    for (int k = 0; k < B.length; k++) {
		    for (int j = k + 1; j < B.length; j++) {
			    d = A[j][k] / A[k][k];
			    for (int i = k; i < B.length; i++) {
				    A[j][i] = A[j][i] - d * A[k][i];
		    	}
			    B[j] = B[j] - d*B[k];
		    }
    	}
	    for (int k = B.length - 1; k >= 0; k--) {
		    d = 0;
		    for (int j = k + 1; j <= B.length - 1; j++) {
			    s = A[k][j] * x[j];
			    d = d + s;
		    }
		    x[k] = (B[k] - d) / A[k][k];
	    }
	    return x;
    }

    public void printMatrix(double[][] matr){
        for (int i = 0; i < matr.length; i++) {
            for (int j = 0; j < matr[0].length; j++) {
                System.out.print(matr[i][j]+" ");
            }
            System.out.println();
        }
    }

    public void printArray(double[] array){
        for(int i =0;i<array.length;i++){
            System.out.print(array[i]+" ");
        }
        System.out.println();
    }

    public void createMatrixA(int col, double q, double D){
        A = new double [col][col];
        for (int i = 0; i < col; i++) {
            for (int j = 0; j < col; j++) {
                if (i == j) {
                    A[i][j] = 1 + 2 * q*D - s*b_x(j*h);
                }
                if ((i == 0) && (j == 0)) {
                    A[i][j] = 1 + 2 * q*D;// - s*b_x(j*h);
                    break;
                }
                if ((i == col - 1) && (j == col - 1)) {
                    A[i][j] = 1;// + 2 * q*D - s*b_x(j*h);
                    break;
                }
            }
        }
        for (int i = 1; i < col - 1; i++) {
            for (int j = 1; j < col - 1; j++) {
                if (i == j) {
                    A[i][j - 1] = -q*D;
                    A[i][j + 1] = -q*D;
                }
            }
        }
    }

    public Implicit(double L, double T, double D, double[]alfa, double h, double s) {
        this.L = L;
        this.T = T;
        this.D = D;
        this.h = h;
        this.s = s;
        this.alfa = alfa;
        this.cols = (int) (L/h);
        this.rows = (int) (T/s);
        this.q = s/(pow(h,2));
        this.lambda = pi/L;
        this.U = new double[rows][cols];

        for(int i = 0; i<this.rows;i++){
            for(int j = 0; j < this.cols;j++){
                this.U[i][j]=0.0;
            }
        }
        //printArray(U[0]);
        for (int j = 0; j < U[0].length; j++) { //инициализация начальных условий
            U[0][j] = U_xt(j*h, alfa, lambda); //наша входная функция для начальных условий
        }
        //printArray(U[0]);
        U[0][0] = U[0][1];
        U[0][U[0].length - 1] = U[0][U[0].length - 2];

        createMatrixA(cols, this.q, this.D);
        //printArray(U[0]);
        for (int i = 1; i < rows; i++) {
            double[] vec = U[i - 1];
//            for(int j =0;j<vec.length;j++){
//                    vec[j] += s*b_x(j*h);//*vec[j];
//            }
            U[i] = gaussMethod(A, vec);
        }
        //printMatrix(U);

        for (int i = 0; i < U.length; i++) { //в силу производной
            U[i][0] = U[i][1];
            U[i][cols - 1] = U[i][cols - 2];
        }
        printArray(U[0]);
    }
}

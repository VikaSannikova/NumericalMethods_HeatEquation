package HeatEquation;

import static cern.clhep.PhysicalConstants.pi;
import static java.lang.Math.cos;
import static java.lang.Math.pow;
import static java.lang.Math.sin;

public class Implicit {
    public int cols;
    public int rows;
    public double[][] U;
    public double [][] U1;
    public double [][] _U1;
    public double[][] A;
    public double[][] A1;
    public double [][] _matrix;
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
                    A[i][j] = 1;// + 2 * q*D;// - s*b_x(j*h);
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
        System.out.println("матрица А");
        printMatrix(A);
        System.out.println("\n");
    }

    public void createMatrix(int col, double q, double D){
        A1 = new double[col][col];
        for(int i =0; i<col;i++){
            for(int j =0;j<col;j++){
                A1[i][j]=0;
            }
        }
        A1[0][0]=1 + 2*q*D - b_x(h);
        A1[0][1]=-q*D;
        for (int i = 1;i<col-1;i++){
            A1[i][i-1] = -q*D;
            A1[i][i]=1+2*q*D - b_x((i+1)*h);
            A1[i][i+1]=-q*D;
        }
        A1[col-1][col-2]=-q*D;
        A1[col-1][col-1]=1 + 2*q*D - b_x((col)*h);
        System.out.println("матрица A1");
        printMatrix(A1);
        System.out.println("\n");
    }

//    public void Pasha(int rows, int cols){
//        double[][] matr = new double[cols-2][cols-2];
//        double[] f = new double [cols-2];
//        for (int j = 0; j <= rows - 2; ++j)
//        { // заполняем трехдиагональную матрицу
//            matr[0][0] = 1.0 / s + 1.0 / (h*h) - b_x(h);
//            matr[0][1] = -1.0 / (h*h);
//            f[0] = u2[1][j] / s;
//            for (int i = 1; i <= cols - 4; ++i)
//            {
//                matr[i][i - 1] = -1.0 / (h*h);
//                matr[i][i] = 1.0 / s + 2.0 / (h*h) - b_x((i + 1) * h);
//                matr[i][i + 1] = -1.0 / (h*h);
//                f[i] = u2[i + 1][j] / s;
//            }
//            matr[cols - 3][cols - 4] = -1.0 / (h*h);
//            matr[cols - 3][cols - 3] = 1.0 / s + 1.0 / (h*h) - b_x((cols - 2) * h);
//            f[cols - 3] = u2[cols - 2][j] / s;
//
//// прямой ход метода прогонки
//            av[1] = (-1) * matr[0][1] / matr[0][0];
//            bv[1] = f[0] / matr[0][0];
//            for (int i = 1; i < m - 3; ++i)
//            {
//                tmp = (matr[i][i] + matr[i][i - 1] * av[i]);
//                av[i + 1] = (-1) * matr[i][i + 1] / tmp;
//                bv[i + 1] = (f[i] - matr[i][i - 1] * bv[i]) / tmp;
//            }
//            bv[m - 2] = (f[m - 3] - matr[m - 3][m - 4] * bv[m - 3]) / (matr[m - 3][m - 3] + matr[m - 3][m - 4] * av[m - 3]);
//// обратный ход метода прогонки
//            u2[m - 2][j + 1] = bv[m - 2];
//            for (int i = m - 3; i >= 1; —i)
//            {
//                u2[i][j + 1] = av[i] * u2[i + 1][j + 1] + bv[i];
//            }
//// краевые точки
//            u2[0][j + 1] = u2[1][j + 1];
//            u2[m - 1][j + 1] = u2[m - 2][j + 1];
//        }
//    }

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
        for (int j = 0; j < U[0].length; j++) { //инициализация начальных условий
            U[0][j] = U_xt(j*h, alfa, lambda); //наша входная функция для начальных условий
        }
        U[0][0] = U[0][1];
        U[0][U[0].length - 1] = U[0][U[0].length - 2];
///паша

        double [][] matrix = new double[rows][cols-2];
        for(int j = 0;j<cols-2;j++){
            matrix[0][j]=U[0][j+1];
        }
        printMatrix(U);
        printMatrix(matrix);
        System.out.println();

        double[][] matrA = new double[cols-2][cols-2];
        double[]f = new double[cols-2];
        for (int j = 0; j <= rows - 2; ++j)
        {
            // заполняем трехдиагональную матрицу
            matrA[0][0] = 1.0 / s + 1.0 / (h*h) - b_x(h);
            matrA[0][1] = -1.0 / (h*h);
            f[0] = matrix[j][0] / s;
            for (int i = 1; i <= cols - 4; ++i)
            {
                matrA[i][i - 1] = -1.0 / (h*h);
                matrA[i][i] = 1.0 / s + 2.0 / (h*h) - b_x((i + 1) * h);
                matrA[i][i + 1] = -1.0 / (h*h);
                f[i] = matrix[j][i] / s;
            }
            matrA[cols - 3][cols - 4] = -1.0 / (h*h);
            matrA[cols - 3][cols - 3] = 1.0 / s + 1.0 / (h*h) - b_x((cols - 2) * h);
            f[cols - 3] = matrix[j][cols-3] / s;
            System.out.println();
            matrix[j+1]=gaussMethod(matrA,f);
            System.out.println("!");
            printArray(matrix[j+1]);
            System.out.println("!");
            printArray(f);
            System.out.println();
            printMatrix(matrix);
        }
        System.out.println("матрица Паши Gauss");
        printMatrix(matrA);
        System.out.println();
        printMatrix(matrix);
        System.out.println("\n");
        //printArray(gaussMethod(matr,f));
        _matrix=new double[rows][cols];
        for(int i = 0;i<rows;i++){
            for(int j = 1;j<cols-1;j++){
                _matrix[i][j]=matrix[i][j-1];
            }
            _matrix[i][0]=_matrix[i][1];
            _matrix[i][cols-1]=_matrix[i][cols-2];
        }
        System.out.println("govno");
        printMatrix(_matrix);
        System.out.println("end");





        createMatrixA(cols, this.q, this.D);
        //printMatrix(A);
        //printArray(U[0]);
        createMatrix(cols-2,this.q,this.D);
        U1 = new double[rows][cols-2];
        for(int j = 0;j<cols-2;j++){
            U1[0][j]=U[0][j+1];
        }
        for (int i = 1; i < rows; i++) {
            double[] vec = U[i - 1];
            double[] vec1 = new double[cols-2];
            for(int j=0;j<vec1.length;j++){
                vec1[j]=vec[j+1];
            }
            double[] tmp = new double[cols-2];
            tmp = gaussMethod(A1,vec1);
            U1[i]=tmp;


            U[i] = gaussMethod(A, vec);
        }
        //printMatrix(U);
        System.out.println("\n");
        //printMatrix(U1);
        System.out.println("U1");
        printMatrix(U1);
        System.out.println();
        for (int i = 0; i < U.length; i++) { //в силу производной
            U[i][0] = U[i][1];
            U[i][cols - 1] = U[i][cols - 2];
        }
        _U1=new double[rows][cols];
        for(int i = 0;i<rows;i++){
            for(int j = 1;j<cols-1;j++){
                _U1[i][j]=U1[i][j-1];
            }
            _U1[i][0]=_U1[i][1];
            _U1[i][cols-1]=_U1[i][cols-2];
        }
        //printMatrix(_U1);

        //printArray(U[0]);
    }
}

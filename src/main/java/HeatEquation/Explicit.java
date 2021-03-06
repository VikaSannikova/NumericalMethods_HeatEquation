package HeatEquation;

//import java.util.Array;
import java.util.ArrayList;
import java.util.Arrays;

import static cern.clhep.PhysicalConstants.pi;
import static java.lang.Math.cos;
import static java.lang.Math.pow;
import static java.lang.Math.sin;

public class Explicit {
    public int cols;
    public int rows;
    public double[][] U;
    public double[] U0;
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
        //return sin(x);
        return cos(x);
    }
    public void printArray(double[] array){
        for(int i =0;i<array.length;i++){
            System.out.print(array[i]+" ");
        }
        System.out.println();
    }
    public void printMatrix(double[][] matr){
        for (int i = 0; i < matr.length; i++) {
            for (int j = 0; j < matr[0].length; j++) {
                System.out.print(matr[i][j]+" ");
            }
            System.out.println();
        }
    }
    public void setEntryConditions(){
        for(int i = 0; i<this.rows;i++){
            for(int j = 0; j < this.cols;j++){
                this.U[i][j]=0.0;
            }
        }
        for (int j = 0; j < U[0].length; j++) { //инициализация начальных условий
            U[0][j] = U_xt(j*h, alfa, lambda); //наша входная функция для начальных условий
        }
        //U[0][0] = U[0][1];
        //U[0][U[0].length - 1] = U[0][U[0].length - 2];
    }
    public void createU(){
        for (int i = 1; i < rows; i++) { //цикл по строчкам
            for (int j = 1; j < cols - 1; j++) { //цикл по столбцам
                U[i][j] = q*D*U[i - 1][j - 1] + (1 - 2 * q*D)*U[i - 1][j] + q*D*U[i - 1][j + 1] +s*b_x(j*h)*U[i - 1][j]; //проверить
            }
            U[i][0] = U[i][1];
            U[i][U[0].length - 1] = U[i][U[0].length - 2];
        }
    }

    public Explicit(double L, double T, double D, double[]alfa, double h, double s) {
        this.L = L;
        this.T = T;
        this.D = D;
        this.h = h;
        this.s = s;
        this.alfa = alfa;
        this.cols = (int) (L/h) + 1;
        this.rows = (int) (T/s) + 1;
        this.q = s/(pow(h,2));
        this.lambda = pi/L;
        this.U = new double[rows][cols];

        setEntryConditions();
        createU();
    }
}

package HeatEquation;

public class Main {
    public static void main(String[] args) {
        double L = 5;
        double T = 2;
        double D = 1;
        double h = 1;
        double t = 0.5;
        double[]alfa = {1,2,3,4,5};
        Explicit U1 =  new Explicit(L,T,D,alfa,h,t);
        Implicit U2 = new Implicit(L,T,D,alfa,h,t);
    }
}

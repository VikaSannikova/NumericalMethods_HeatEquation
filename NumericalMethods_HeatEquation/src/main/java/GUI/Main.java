package GUI;


import HeatEquation.Explicit;
import HeatEquation.Implicit;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Main {
    public static void main(String[] args) {
        double L = 5;
        double T = 2;
        final double D = 1;
        final double h = 0.25;
        double t = 0.03;
        double[]alfa = {1,2,3,4,5};
//        final XYSeries explicit = new XYSeries("явная");
//        Explicit U = new Explicit(L,T,D,alfa,h,t);
//        double[] exvec = U.U[U.rows-1];
//        for(float i = 0; i < U.L; i+=U.h){
//            explicit.add(i, exvec[(int)(i/h)]);
//        }

//        XYSeries base = new XYSeries("начальное");
//        double[] basevec = U.U[0];
//        for(float i = 0; i <U.L; i+=U.h){
//            base.add(i,basevec[(int)(i/h)]);
//        }


//        XYSeries implicit = new XYSeries("неявная");
//        Implicit U2 = new Implicit(L,T,D,alfa,h,t);
//        double[] imvec = U2.U[U2.rows-1];
//        for(float i = 0; i < U2.L; i+=U2.h){
//            implicit.add(i, imvec[(int)(i/h)]);
//        }

        final XYSeriesCollection dataset = new XYSeriesCollection();
        //dataset.addSeries(explicit);
        //dataset.addSeries(implicit);
        //dataset.addSeries(base);

//        JFreeChart chart = ChartFactory.createXYLineChart(
//                "",
//                "x",
//                "y",
//                dataset,
//                PlotOrientation.VERTICAL,
//                true,
//                true,
//                true);
        JFrame frame =
                new JFrame("Уравнение теплопроводности");

        JPanel dataFields = new JPanel(new GridLayout(2,1,0,5));
        JPanel dataFieldsANN = new JPanel(new GridLayout(1,2,15,5));

        JPanel dataLTXYC = new JPanel(new GridLayout(5,2,0,5));
        final JTextField lengthField = new JTextField();
        final JTextField timeField = new JTextField();
        final JTextField deltaXField = new JTextField();
        final JTextField deltaYField = new JTextField();
        final JTextField alfas = new JTextField();
        dataLTXYC.add(new Label("Set length:"));
        dataLTXYC.add(lengthField);
        dataLTXYC.add(new Label("Set time:"));
        dataLTXYC.add(timeField);
        dataLTXYC.add(new Label("Set delta X: "));
        dataLTXYC.add( deltaXField);
        dataLTXYC.add(new Label("Set delta Y: "));
        dataLTXYC.add(deltaYField);
        dataLTXYC.add(new Label("Set coeffs: "));
        dataLTXYC.add(alfas);
        alfas.setSize(deltaXField.getSize());

//        JPanel dataLTXYCANN = new JPanel(new GridLayout(2,1,0,5));
//        JPanel g1 = new JPanel(new GridLayout(1,6,0,5));
//        JPanel g2 = new JPanel(new GridLayout(1,6,0,5));
//        g1.add(new Label("Set length:"));
//        g1.add(lengthField);
//        g1.add(new Label("Set delta X: "));
//        g1.add( deltaXField);
//        g1.add(new Label("Set coeffs: "));
//        g1.add(alfas);
//        g2.add(new Label("Set time:"));
//        g2.add(timeField);
//        g2.add(new Label("Set delta Y: "));
//        g2.add(deltaYField);
//        g2.add(new JLabel());
//        g2.add(new JLabel());
//        dataLTXYCANN.add(g1);
//        dataLTXYCANN.add(g2);

        JPanel coefbutton = new JPanel(new GridLayout(1,3,0,5));


        JPanel dataAD = new JPanel(new GridLayout(3,1,0,5));
        JButton drawBase = new JButton("draw base");
        JButton drawEI = new JButton("draw EI");
        drawBase.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                double L = Double.valueOf(lengthField.getText());
                double T =Double.valueOf(timeField.getText());
                double D = 1;
                double deltaX  = Double.valueOf(deltaXField.getText());
                double deltaY = Double.valueOf(deltaYField.getText());
                String alfa = alfas.getText();
                String[] alfas = alfa.split(" ");
                double[] alfanum = new double[alfas.length];
                for(int i =0;i<alfanum.length;i++){
                    alfanum[i]=Double.parseDouble(alfas[i]);
                }
                XYSeries base = new XYSeries("начальная");
                XYSeries explicit = new XYSeries("явная");
                XYSeries implicit = new XYSeries("неявная");
                Explicit U1 = new Explicit(L,T,D,alfanum,deltaX,deltaY);
                //Implicit U2 = new Implicit(L,T,D,alfanum,deltaX,deltaY);
                double[] basevec = U1.U[0];
                System.out.println("graf");
                U1.printArray(basevec);

                double[] exvec = U1.U[U1.rows-1];
                //double[] imvec = U2.U[U2.rows-1];
                for (float i = 0; i < U1.L; i+=U1.h){
                    System.out.print((int)(i/U1.h)+" @ ");
                    base.add(i,basevec[(int)(i/U1.h)]);
                }
                dataset.addSeries(base);
                for(float i = 0; i < U1.L; i+=U1.h){
                    explicit.add(i, exvec[(int)(i/U1.h)]);
                }
//                for(float  i =0; i<U2.L; i+=U2.h){
//                    implicit.add(i, imvec[(int)(i/U2.h)]);
//                }
                //dataset.addSeries(explicit);
                //dataset.addSeries(implicit);
            }
        });
        drawEI.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                double L = Double.valueOf(lengthField.getText());
                double T =Double.valueOf(timeField.getText());
                double D = 1;
                double deltaX  = Double.valueOf(deltaXField.getText());
                double deltaY = Double.valueOf(deltaYField.getText());
                String alfa = alfas.getText();
                String[] alfas = alfa.split(" ");
                double[] alfanum = new double[alfas.length];
                for(int i =0;i<alfanum.length;i++){
                    alfanum[i]=Double.parseDouble(alfas[i]);
                }
                //XYSeries base = new XYSeries("начальная");
                XYSeries explicit = new XYSeries("явная");
                XYSeries implicit = new XYSeries("неявная");
                Explicit U1 = new Explicit(L,T,D,alfanum,deltaX,deltaY);
                Implicit U2 = new Implicit(L,T,D,alfanum,deltaX,deltaY);
//                double[] basevec = U1.U[0];
//                System.out.println("graf");
//                U1.printArray(basevec);

                double[] exvec = U1.U[U1.rows-1];
                double[] imvec = U2.U[U2.rows-1];
//                for (float i = 0; i < U1.L; i+=U1.h){
//                    System.out.print((int)(i/U1.h)+" @ ");
//                    base.add(i,basevec[(int)(i/U1.h)]);
//                }
                //dataset.addSeries(base);
                for(float i = 0; i < U1.L; i+=U1.h){
                    explicit.add(i, exvec[(int)(i/U1.h)]);
                }
                for(float  i =0; i<U2.L; i+=U2.h){
                    implicit.add(i, imvec[(int)(i/U2.h)]);
                }
                dataset.addSeries(explicit);
                dataset.addSeries(implicit);
            }
        });
        JButton clear = new JButton("clear");
        clear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dataset.removeAllSeries();
            }
        });


        dataAD.add(drawBase);
        dataAD.add(drawEI);
        dataAD.add(clear);

        //coefbutton.add(drawBase);
        //coefbutton.add(drawEI);
        //coefbutton.add(clear);

        dataFields.add(dataLTXYC);
        dataFields.add(dataAD);

        //dataFieldsANN.add(dataLTXYCANN);
        dataFieldsANN.add(coefbutton);
        JPanel dataANN = new JPanel(new FlowLayout(FlowLayout.CENTER));
        dataANN.add(dataFieldsANN);

        JPanel data = new JPanel(new FlowLayout(FlowLayout.CENTER));
        data.add(dataFields);
        frame.add(data, BorderLayout.WEST);
        //frame.add(dataANN, BorderLayout.NORTH);



        JFreeChart chart = ChartFactory.createXYLineChart(
                "",
                "x",
                "y",
                dataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                true);
        chart.setBackgroundPaint(Color.WHITE);
        final XYPlot plot = chart.getXYPlot();
        plot.setBackgroundPaint(new Color(232, 232, 232));

        plot.setDomainGridlinePaint(Color.gray);
        plot.setRangeGridlinePaint (Color.gray);
        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        renderer.setSeriesPaint  (2, Color.orange);
        //renderer.setSeriesStroke (2, new BasicStroke(2.5f));
        plot.setRenderer(renderer);


        frame.getContentPane()
                .add(new ChartPanel(chart));
        frame.setSize(400,300);
        frame.show();
    }
}
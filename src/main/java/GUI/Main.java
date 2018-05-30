package GUI;

import HeatEquation.Explicit;
import HeatEquation.Implicit;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static java.lang.Math.pow;

public class Main {
    public static void main(String[] args) {
        double L = 5;
        double T = 2;
        final double D = 1;
        final double h = 0.25;
        double t = 0.03;
        double[]alfa = {1,2,3,4,5};

        final XYSeriesCollection dataset = new XYSeriesCollection();

        final JFrame frame =
                new JFrame("Уравнение теплопроводности");
        frame.setLocationRelativeTo(null);


        JPanel dataFields = new JPanel(new GridLayout(2,1,0,5));

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

        JPanel buttons = new JPanel(new GridLayout(4,1,0,5));
        JButton drawBase = new JButton("draw base");
        JButton drawExplicit = new JButton("draw explicit");
        JButton drawImplicit = new JButton("draw implicit");
        JButton clear = new JButton("clear");

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
                Explicit U1 = new Explicit(L,T,D,alfanum,deltaX,deltaY);
                double[] basevec = U1.U[0];
                for (float i = 0; i <= U1.L; i+=U1.h){
                    base.add(i,basevec[(int)(i/U1.h)]);
                }
                dataset.addSeries(base);
            }
        });
        drawExplicit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                double L = Double.valueOf(lengthField.getText());
                double T =Double.valueOf(timeField.getText());
                double D = 1;
                double deltaX  = Double.valueOf(deltaXField.getText());
                final double deltaY = Double.valueOf(deltaYField.getText());
                if(deltaY>pow(deltaX,2)/(2*D)){
                    JPanel panel = new JPanel(new GridLayout(2,1));
                    final JDialog error = new JDialog();
                    error.setSize(500,200);
                    error.setLocationRelativeTo(frame);
                    panel.add(new JLabel("Явная схема неустойчива. Шаг по времени должен быть ≤"+pow(deltaX,2)/(2*D)));
                    JButton ok = new JButton("OK");
                    ok.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            deltaYField.setText("");
                            error.dispose();
                        }
                    });
                    panel.add(ok);
                    error.add(panel);
                    error.setModal(true);
                    error.setVisible(true);
                }else{
                    String alfa = alfas.getText();
                    String[] alfas = alfa.split(" ");
                    double[] alfanum = new double[alfas.length];
                    for(int i =0;i<alfanum.length;i++){
                        alfanum[i]=Double.parseDouble(alfas[i]);
                    }
                    XYSeries explicit = new XYSeries("явная");
                    Explicit U = new Explicit(L,T,D,alfanum,deltaX,deltaY);
                    double[] exvec = U.U[U.rows-1];
                    for(float i = 0; i <= U.L; i+=U.h){
                        explicit.add(i, exvec[(int)(i/U.h)]);
                    }
                    dataset.addSeries(explicit);
                }
            }
        });
        drawImplicit.addActionListener(new ActionListener() {
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
                XYSeries implicit = new XYSeries("неявная");
                Implicit U = new Implicit(L,T,D,alfanum,deltaX,deltaY);
                double[] imvec = U.U[U.rows-1];
                for(float  i =0; i<=U.L; i+=U.h){
                    implicit.add(i, imvec[(int)(i/U.h)]);
                }
                dataset.addSeries(implicit);
            }
        });
        clear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dataset.removeAllSeries();
            }
        });


        buttons.add(drawBase);
        buttons.add(drawExplicit);
        buttons.add(drawImplicit);
        buttons.add(clear);

        dataFields.add(dataLTXYC);
        dataFields.add(buttons);

        JPanel data = new JPanel(new FlowLayout(FlowLayout.CENTER));
        data.add(dataFields);
        frame.add(data, BorderLayout.WEST);



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
        renderer.setSeriesPaint(3,Color.BLACK);
        plot.setRenderer(renderer);

        frame.getContentPane().add(new ChartPanel(chart));
        frame.setSize(400,300);
        frame.show();
    }
}
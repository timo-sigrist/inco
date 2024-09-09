/*
============================================================================
(C) Zurich University of Applied Sciences / Institute of Embedded Systems 
============================================================================

Uebung "Discrete Cosine Transformation"

============================================================================

Version      
Date         	25-10-2019
Author       	kmno/muth@zhaw.ch

System       	Java (getestet unter Windows 10)
openjdk version "11.0.3" 2019-04-16 LTS
OpenJDK Runtime Environment Corretto-11.0.3.7.1
OpenJDK 64-Bit Server VM Corretto-11.0.3.7.1 
============================================================================

Aufgabe: Programmmieren der Funktion dct_inverse

(1) Kompilieren direkt con der Kommandozeile:
>> javac Jpeg_Aufgabe.java

(2) Ausfuehren Jpeg_Aufgabe
>> java Jpeg_Aufgabe

============================================================================ 
 */

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.util.Arrays;
import java.util.BitSet;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class DCT_Aufgabe {

    //   Initialisierungswerte der Frequenzkoeffizienten
    //
    //   Fvu ist als Frequenz[v][u] implementiert; d.h. die
    //   folgende Anordnung entspricht der Darstellung in der Theorie:

    static int[][] Frequenz = {
            //		Frequenzmatrix fuer Test:
            { 1200, 23, -142, 77, -191, 415, -10, -239 }, 
            { -18, 32, 46, 50, 68, 3, -19, -19 },
            { 264, -200, 13, 27, 35, -203, -96, 13 }, 
            { 50, -76, -82, -72, -139, 5, 34, 30 },
            { 255, 164, 73, -98, 191, -59, 177, 222 }, 
            { -75, 79, 16, -40, 93, -37, -7, 7 },
            { 305, 138, 96, -26, -83, -168, -77, 0 }, 
            { 88, -64, 69, 170, -13, 68, -29, -51 }};

    /*
    static int[][] Frequenz = {
            //		Frequenzmatrix fuer Test:$

            {1020, 0, 0, 0, 0, 0, 0, 0},
        { 0, 0, 0, 0, 0, 0, 0, 0},
    { 0, 0, 0, 0, 0, 0, 0, 0},
    { 0, 0, 0, 0, 0, 0, 0, 0},
    { 0, 0, 0, 0, -1020, 0, 0, 0},
    { 0, 0, 0, 0, 0, 0, 0, 0},
    { 0, 0, 0, 0, 0, 0, 0, 0},
    { 0, 0, 0, 0, 0, 0, 0, 0}};
*/
    /*
    //	  Vorbereitete leere Frequenzmatrix
    {1020, 0, 0, 0, 0, 0, 0, 255}, 
    { 0, 0, 0, 0, 0, 0, 0, 0}, 
    { 0, 0, 0, 0, 0, 0, 0, 0}, 
    { 0, 0, 0, 0, 0, 0, 0, 0}, 
    { 0, 0, 0, 0, 0, 0, 0, 0}, 
    { 0, 0, 0, 0, 0, 0, 0, 0}, 
    { 0, 0, 0, 0, 0, 0, 0, 0}, 
    { 0, 0, 0, 0, 0, 0, 0, 0}};
     */

    //  Buffer fuer Bild-Pixel: 
    //  Byx ist als Bild[y][x] implementiert.

    static int[][] Bild = new int[8][8];

    private static double C(int p) {
        // Faktor fuer die Randwerte bestimmen
        if (p == 0) {
            return (1.0 / Math.sqrt(2));
        } else {
            return 1.0;
        }
    }

    public static void dct_forward(int[][] Bild, int[][] Freq) {
        // Funktion: Discrete Cosine Transformation
        // Nur zur Illustration: Wird nicht verwendet
        // Input: Bild mit 8x8 Pixeln
        // Output: Freq-Block mit 8x8 Koeffizienten, enthaelt
        // den DC- und die AC-Werte

        for (int v = 0; v < 8; v++) {
            for (int u = 0; u < 8; u++) {
                double Summe = 0.0;
                for (int x = 0; x < 8; x++) {
                    for (int y = 0; y < 8; y++) {
                        Summe = Summe + Bild[y][x] * 
                        Math.cos((2.0 * x + 1.0) * u * Math.PI / 16.0) *
                        Math.cos((2.0 * y + 1.0) * v * Math.PI / 16.0);
                    }
                }
                Freq[v][u] = (int) (Math.round((C(u) * C(v) * Summe / 4.0)));
            }
        }
    }

    public static void dct_inverse(int[][] Freq, int[][] c) {

        // Aufgabe: Inverse Cosinus Transformation
        // Input: Freq-Block mit 8x8 Koeffizienten, enthaelt
        // den DC- und die AC-Werte
        // Output: Bild mit 8x8 Pixeln

        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                double Summe = 0.0;
                for (int v = 0; v < 8; v++) {
                    for (int u = 0; u < 8; u++) {
                        Summe = Summe + Freq[v][u] *
                                Math.cos((2.0 * x + 1.0) * u * Math.PI / 16.0) *
                                Math.cos((2.0 * y + 1.0) * v * Math.PI / 16.0) *
                                (C(u) * C(v));
                    }
                }
                Bild[y][x] = (int) (Summe / 4.0);
            }
        }
    }

    //======================== Programm Entry Point ===============================

    public static void main(String[] args) {

        // dct_forward(Test, Frequenz); // Fuer Zusatzaufgabe
        dct_inverse(Frequenz, Bild);
        System.out.println(Bild[1][0]);
        //=============================================================================
        // Ab hier sollte der Code nicht abgeaendert werden.
        // Funktion: oeffnen eines Fensters und Bild-Anzeige
        //=============================================================================

        JFrame frame = new JFrame("INCO - Inverse DCT");
        JPanel idctPanel = new JPanel();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        idctPanel.setBorder(BorderFactory.createLineBorder(Color.black, 2));
        idctPanel.setPreferredSize(new Dimension(321, 321));
        frame.add(idctPanel);
        frame.pack();
        frame.setVisible(true);

        int[][] idctColors = new int[8][8];
        copyBildToColors(Bild, idctColors, 8);

        idctPanel.setLayout(new GridLayout());
        idctPanel.add(new Field(idctColors, 8));
        idctPanel.validate();
    }

    private static void copyBildToColors(int[][] Bild, int[][] idctColors, int N) {
        // Kopiert inverse-transformiertes Bild ins Anzeigefeld mit Clipping

        for (int x = 0; x < N; x++) {
            for (int y = 0; y < N; y++) {
                if (Bild[y][x] > 255) {
                    idctColors[y][x] = 255;
                } else if (Bild[y][x] < 0) {
                    idctColors[y][x] = 0;
                } else {
                    idctColors[y][x] = Bild[y][x];
                }
            }
        }
    }

    private static class Field extends JComponent {
        // Grafische Ausgabe des 8x8-Bildes: Pixel werden 40x vergroessert

        int[][] idctColors;
        int N;

        Field(int[][] idctColors, int N) {
            this.idctColors = idctColors;
            this.N = N;
        }

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);

            for (int col = 0; col < N; col++) {
                for (int row = 0; row < N; row++) {	
                    int gray = idctColors[col][row];
                    g.setColor(new Color(gray, gray, gray));
                    g.fillRect(row*40, col*40, 40, 40);
                }

            }
        }
    }
}

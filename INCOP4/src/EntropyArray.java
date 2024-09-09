// ============================================================================
//
//      Entropy 
//
// ============================================================================
//
//      Version      using Arrays
//      Date         2019-10-07
//      Author       J. M. Stettbacher / Muth
//
//      System       Java (tested on Windows / Eclipse and IntelliJ)
//
// ============================================================================



import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Calculates the entropy of the given file.
 * Reads symbols from the data file and determines:
 * <ul>
 * <li>number of different character types in file.</li>
 * <li>total number of characters in file.</li>
 * <li>probability of each character type.</li>
 * <li>information of each character type.</li>
 * <li>entropy of entire file.</li>
 * </ul>
 */

public class EntropyArray {

    private final int[] charCount = new int[256]; // Limited to 8-Bit-Characters

    // Global counter variable:
    private int fileCharactersCount = 0;
    private int fileSymbolCount = 0;


    public EntropyArray(File file) {
        readInputTextFileBytes(file);
        computeEntropy();
    }

    // Main method. The program starts here.
    public static void main(String[] args) {
        printWelcomeMessage();

        File file = getFileFromArguments(args);
        System.out.printf("Data file '%s' exists.%n", file.getName());

        EntropyArray entropyArray = new EntropyArray(file);
        entropyArray.printOutCharProps();

        printDoneMessage();
    }


    private static void printWelcomeMessage() {
        // Print hello message:
        System.out.println("======================================================");
        System.out.println("Starting ...");
    }

    private static void printDoneMessage() {
        System.out.println("Done.");
        System.out.println("======================================================");
    }

    private static File getFileFromArguments(String[] args) {
        // ------------------------------------------------------------
        // Check if a valid filename has been supplied on command line:
        // ------------------------------------------------------------
        // Is there a command line argument at all?
        if (args.length <= 0) {
            throw new IllegalArgumentException("ERROR: You have to supply a filename on the command line!");
        }
        // Yes, there is at least one argument:
        String filename = args[0];
        File file = new File(filename);
        if (!file.exists()) { // Check if file with that name exists:
            throw new RuntimeException(String.format("ERROR: Data file: '%s' does not exist!", filename));
        }
        return file;
    }


    private void readInputTextFileBytes(File file) {

        System.out.println("Reading file ...");

        // Open file and read byte by byte:
        try (BufferedInputStream in = new BufferedInputStream(new FileInputStream(file))) {
            ArrayList<Integer> charList = new ArrayList<Integer>();
            int c;
            // Read characters c until there are no more:
            while ((c = in.read()) != -1) {
                /*
                 * TODO: Anzahl der unterschiedlichen Symbole (fileSymbolCount) nachführen
                 * Wie viele verschriedene Zeichen kommen vor
                 * */
                if (!charList.contains(c) && c!= 32 && c!= 10 && c!= 13) {
                    charList.add(c);
                    fileSymbolCount++;
                }


                /*
                 * TODO: Häufigkeit des Symbols im Array charCount[] nachführen
                 * */
                if(c!= 32 && c!= 10 && c!= 13) {
                    charCount[c]++;
                }


                /*
                 * TODO: Gesamtzahl der Zeichen im File nachführen in Variable fileCharactersCount
                 *  Wie viele Zeichen hat es insgesamt
                 * */
                fileCharactersCount++;

            }
        } catch (IOException e) {
            throw new RuntimeException("IO-Fehler");
        }
    }

    /*
     * Calculate base 2 logarithm
     */
    private double log2(double d) {

        /*
         * TODO: Logarithmus zur Basis 2 berechnen
         * 		 mit Math.log()
         * */
        return Math.log(d) / Math.log(2);
    }

    /*
     * Compute entropy of all characters stored in the array charCount
     */
    private double computeEntropy() {

        System.out.println("Computing entropy...");
        double entropy = 0;

        /*
         * TODO: Entropie H der Quelle berechnen
         * */

        for (int occurrence : charCount) {
            if (occurrence != 0) {
                double auftretenswahrscheinlichkeit = (double) occurrence / fileCharactersCount;
                double informationsgehalt = log2(1.0/auftretenswahrscheinlichkeit);
                entropy += auftretenswahrscheinlichkeit * informationsgehalt;
            }
        }
        return entropy;

    }


    /**
     * Print result table with occurrence, probability and information.
     */
    public void printOutCharProps() {
        printGeneralStatistics();
        printCharacterStatistics();
    }

    private void printGeneralStatistics() {
        System.out.println("Number of symbols in file: " + fileSymbolCount);
        System.out.println("Number of character in file: " + fileCharactersCount);
        System.out.println("Entropy of file: " + computeEntropy());
        System.out.println();
    }

    private void printCharacterStatistics() {
        String charRepresentation = "";
        for (int c = 0; c < charCount.length; c++) {
            if (charCount[c] > 0) {
                if (isPrintable(c)) {
                    charRepresentation = Character.toString((char) c);
                } else {
                    charRepresentation = "(" + c + ")";
                }

                /*
                 * TODO: Wahrscheinlichkeit des Symbols bestimmen
                 * */
                double probability = (double) charCount[c] / fileCharactersCount;

                /*
                 * TODO: Informationsgehalt des Symbols bestimmen
                 * */
                double information = log2(1.0/probability);

                System.out.format("  %5s : o=%8d  p=%14.10f  i=%14.10f%n",
                        charRepresentation, charCount[c], probability, information);
            }
        }
    }


    private boolean isPrintable(int charCode) {
        return charCode > 32 && charCode < 127 || charCode > 160;
    }
}
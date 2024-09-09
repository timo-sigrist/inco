/* ------------------------------------------------------------------
 * --  _____       ______  _____                                    -
 * -- |_   _|     |  ____|/ ____|                                   -
 * --   | |  _ __ | |__  | (___    Institute of Embedded Systems    -
 * --   | | | '_ \|  __|  \___ \   Zurich University of             -
 * --  _| |_| | | | |____ ____) |  Applied Sciences                 -
 * -- |_____|_| |_|______|_____/   8401 Winterthur, Switzerland     -
 * ------------------------------------------------------------------
 * --
 * -- File: AudioTest.java
 * -- Date: 31.10.2019
 * -- Author:   rosn
 * --
 * ------------------------------------------------------------------
 */

import java.lang.Math;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

public class AudioTest 
{
    static RandomAccessFile raw;
    static int byteCount = 0;

    public static void main(String[] args)
    {
        int sampleRate = 44100;                                                         // Sampling Rate
        int bitsPerSample = 16;                                                         // Bits per Sample
        int nChannels = 1;                                                              // Number of Channels
        int duration = 3;                                                               // Duration in seconds 

        try
        {
            raw = new RandomAccessFile("AudioFile.wav", "rw");
            raw.setLength(0); // Set file length to 0, to prevent unexpected behavior in case the file already existed

            raw.writeBytes("RIFF");
            raw.writeInt(0); // Final file size not known yet, write 0. This is = sample count + 36 bytes from header.
            raw.writeBytes("WAVE");                                                     // Keyword "WAVE"
            raw.writeBytes("fmt ");                                                     // Keyword "fmt "
            raw.writeInt(Integer.reverseBytes(16));                                     // Sub-chunk size, 16 for PCM
            raw.writeShort(Short.reverseBytes((short) 1));                              // AudioFormat, 1 for PCM

            /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            // TODO: Hier fehlende Header Eintraege ergaenzen

            raw.writeShort(Short.reverseBytes((short) nChannels));
            raw.writeInt(Integer.reverseBytes(sampleRate));
            raw.writeInt(Integer.reverseBytes(sampleRate * bitsPerSample * nChannels / 8));
            raw.writeShort(Short.reverseBytes((short) (bitsPerSample * nChannels / 8)));
            raw.writeShort(Short.reverseBytes((short) bitsPerSample));
            raw.writeBytes("data");

                                                                                        // Number of channels, 1 for mono, 2 for stereo
                                                                                        // Sample rate
                                                                                        // Byte rate
                                                                                        // Block align, nChannels*bitsPerSample/8
                                                                                        // Bits per Sample
                                                                                        // Keyword "data"

                                                                                        
            /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            raw.writeInt(0);                                                            // Data chunk size not known yet, write 0
        }
        catch(IOException e)
        {
            System.out.println("I/O exception occured while writing data");
        }

        // Audio Samples generieren
        for (int i = 0; i < sampleRate*duration; i++)
        {
            /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            // TODO: Hier Audio Samples erzeugen
           double skalierungsfaktor = Math.pow(2, 15) -1;
           int frequenz = 7000;
           double sinus = Math.sin((i    * 2.0 * Math.PI * frequenz) / sampleRate);
           double ton = skalierungsfaktor * sinus;
            
            writeSample(ton);
            
            
            /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        }

        closeFile();
        System.out.println("Finished");
    }

    static void writeSample(double doubleValue)
    {
        try 
        {
            short shortSample = (short)(doubleValue);
            raw.writeShort(Short.reverseBytes(shortSample));
            byteCount += 2;
        }
        catch(IOException e)
        {
            System.out.println("I/O exception occured while writing data");
        }
    }

    static void closeFile() 
    {
        try 
        {
            raw.seek(4); 
            raw.writeInt(Integer.reverseBytes(byteCount + 36));
            raw.seek(40); 
            raw.writeInt(Integer.reverseBytes(byteCount));
            raw.close();
        }
        catch(IOException e)
        {
            System.out.println("I/O exception occured while closing output file");
        }
    }
}
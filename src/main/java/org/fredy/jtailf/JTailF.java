/* 
 * Copyright 2014 Fredy Wijaya
 *
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package org.fredy.jtailf;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * A simple tool to do tail -f in Java.
 * 
 * @author fredy
 */
public class JTailF {
    /**
     * Follows the stream of a file.
     * 
     * @param file the file name to be followed
     * @param sleepInterval the sleep interval in ms
     * @param keepReading if set to true, it will keep waiting until there is
     *                    a new stream
     * @param func the callbcak function
     * @throws FileNotFoundException thrown if a file is not found
     * @throws IOException thrown for anything related to I/O
     */
    public static void tail(String file, int sleepInterval, boolean keepReading,
        IJTailFCallback func)
        throws FileNotFoundException, IOException {
        try (RandomAccessFile raf = new RandomAccessFile(new File(file), "r")) {
            while (true) {
                String line = null;
                if ((line = raf.readLine()) != null) {
                    func.readLine(line);
                } else {
                    try {
                        Thread.sleep(sleepInterval);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                    if (!keepReading) {
                        break;
                    }
                }
            }
            
        }
    }
    
    private static void printUsage() {
        System.out.println("Usage: java -jar jtailf.jar <filename>");
    }
    
    private static boolean validateArgs(String[] args) {
        if (args.length != 1) {
            printUsage();
            return false;
        }
        if (!new File(args[0]).isFile()) {
            System.err.println("Error: " + new File(args[0]).getAbsolutePath() +
                " does not exist");
            return false;
        }
        return true;
    }
    
    public static void main(String[] args) {
        if (!validateArgs(args)) {
            System.exit(1);
        }
        
        try {
            JTailF.tail(args[0], 100, true, new JTailFCallback(System.out));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

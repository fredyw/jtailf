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
import java.io.IOException;
import java.io.PrintWriter;

import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author fredy
 */
public class JTailFTest {
    @BeforeClass
    public static void init() throws IOException {
        PrintWriter pw = new PrintWriter("test.txt");
        try {
            for (int i = 1; i <= 1000; i++) {
                pw.println(i + " Hello World");
            }
        } finally {
            pw.close();
        }
    }
    
    @Test
    public void testFollowCustomCallback() throws Exception {
        JTailF.tail("test.txt", 100, new IJTailFCallback() {
            @Override
            public void readLine(String line) {
                int idx = line.indexOf("Hello World");
                if (idx != -1) {
                    int lineNumber = Integer.parseInt(line.substring(0, idx-1));
                    // only print even line number
                    if (lineNumber % 2 == 0) {
                        System.out.println(line);
                    }
                }
            }
            @Override
            public boolean keepReading() {
                return false;
            }
        });
    }
    
    @Test
    public void testFollowDefaultCallbackWriteToConsole() throws Exception {
        PrintWriter writer = new PrintWriter(System.out, true);
        try {
            final JTailFCallback callback = new JTailFCallback(writer, false);
            JTailF.tail("test.txt", 100, callback);
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }
    
    @Test
    public void testFollowDefaultCallbackWriteToFile() throws Exception {
        PrintWriter writer = new PrintWriter(new File("abc.txt"));
        try {
            final JTailFCallback callback = new JTailFCallback(writer, false);
            JTailF.tail("test.txt", 100, callback);
        } finally {
            writer.close();
        }
    }
    
    @Test
    public void testFollowDefaultCallbackStopReading() throws Exception {
        PrintWriter writer = new PrintWriter(new File("def.txt"));
        final JTailFCallback callback = new JTailFCallback(writer, true);
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    JTailF.tail("test.txt", 100, callback);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
        
        Thread.sleep(3000);
        callback.stopReading();
        writer.close();
    }
}

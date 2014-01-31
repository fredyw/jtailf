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

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author fredy
 */
public class JTailFTest {
    @BeforeClass
    public static void init() throws IOException {
        try (PrintWriter pw = new PrintWriter("test.txt")) {
            for (int i = 1; i <= 1000; i++) {
                pw.println(i + " Hello World");
            }
        }
    }
    
    @AfterClass
    public static void cleanUp() {
        new File("test.txt").delete();
    }
    
    @Test
    public void testFollow() throws Exception {
        JTailF.follow("test.txt", 100, false, new IJTailFCallback() {
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
        });
    }
}

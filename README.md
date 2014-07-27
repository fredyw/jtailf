jtailf
======

jtailf is both Java library and executable that performs tail -f.

How to build
------------
jtailf uses Gradle build system (http://www.gradle.org/) and requires minimum Java 6 to build.

### Building jtailf JAR ###
    gradle
The JAR will be created in build/libs directory.

### Running jtailf ###
    java -jar build/libs/jtailf.jar <filename>

Download
--------
https://github.com/fredyw/jtailf/releases

Examples
--------
```java
// output to console
PrintWriter writer = new PrintWriter(System.out, true);
try {
    final JTailFCallback callback = new JTailFCallback(writer, false);
    JTailF.tail("test.txt", 100, callback);
} finally {
    if (writer != null) {
        writer.close();
    }
}


// output to a file
PrintWriter writer = new PrintWriter(new File("abc.txt"));
try {
    final JTailFCallback callback = new JTailFCallback(writer, false);
    JTailF.tail("test.txt", 100, callback);
} finally {
    writer.close();
}


// run in a background thread
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


// with a custom callback
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
```

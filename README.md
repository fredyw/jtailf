jtailf
======

jtailf is both Java library and executable that performs tail -f.

How to build
------------
jtailf uses Gradle build system (http://www.gradle.org/) and requires Java 7 to build.

### Building jtailf JAR ###
    gradle
The JAR will be created in build/libs directory.

### Running jtailf ###
    java -jar build/libs/jtailf.jar <filename>

Download
--------
https://raw.github.com/fredyw/jtailf/master/jtailf.jar

Examples
--------
```java
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
```

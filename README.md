Vanitas
=======
A simple multi-threaded bitcoin vanity address generator written in Java.

Prerequisites:
-------------------------

* JDK 1.5 or higher
* Maven
* For additional dependency information refer to pom.xml

Build Instructions:
-------------------------

Building an Executable Jar:
> mvn package

The standalone executable jar can be found at: ./target/vanitas-1.0-SNAPSHOT-jar-with-dependencies.jar

Usage:
-------------------------

Running as an executable jar:

> java -jar target/vanitas-1.0-SNAPSHOT-jar-with-dependencies.jar 1Love

Log information will be output to ./logs/error.log

Example Output:
-------------------------

Searching for a bitcoin address that contains: 1Love
Status is available at: ./logs/error.log
Found in 1 minutes
Address: 1LoveEbV9B5iRzKU63PKh1tXNk7vh865B7
Private Key: 45777959218638374115925337441855471702901360693577031567674250991838132852058

Test Execution:
-------------------------

Execute all unit tests:
> mvn test

Limitations:
-------------------------

* It's slow. Ideally this type of work would be perfomed on a GPU.

License
-------------------------

Copyright (c) [2015] [com.gmail.lifeofreilly]

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.

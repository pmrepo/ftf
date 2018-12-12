# ftf
Find current open food trucks in San Francisco using Socrata API at https://data.sfgov.org/resource/bbb8-hzi6.json

Compiled and tested on MacOS with Java version '9.0.1'


To compile:  

javac -d bin -cp lib/&ast;: src/com/pm/ftf/&ast;.java

To run:  

java -cp bin:lib/&ast; com.pm.ftf.FoodTruckFinder

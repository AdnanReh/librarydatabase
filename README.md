# librarydatabase
An example library database designed and built as a part of a database course

This requires oracle 11g to be installed and needs the jdbc driver (ojdbc6.jar). To run the application, use the -cp command when compiling libraryDatabase.java and the jdbc driver. 

replace SYSTEM/PASSWORD on line 20 of libraryDatabaseUI.java with the username and password of your localhost database. 

To compile (Windows CMD):
javac --release 8 -cp ojdbc6.jar; libraryDatabaseUI.java
java -cp ojdbc6.jar; libraryDatabaseUI.java                  

for UNIX/LINUX use ":" instead of ";" 

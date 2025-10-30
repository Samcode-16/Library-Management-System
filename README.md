Compile:
javac -cp ".;lib/mysql-connector-j-9.5.0.jar" -d bin src\library\models\*.java src\library\services\*.java src\library\main\LibraryApp.java

OR

javac -d bin -cp "lib/mysql-connector-j-9.5.0.jar" (Get-ChildItem -Recurse -Filter *.java | ForEach-Object { $_.FullName })

Run:
java -cp ".;bin;lib/mysql-connector-j-9.5.0.jar" library.main.LibraryApp  

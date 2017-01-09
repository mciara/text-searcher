# text searcher

A command line driven text search engine for text files.   

It reads all the text files in the given directory, builds an in memory representation of the files and their content, and then give a command prompt at which interactive searches can be performed.
The search takes the words given on the command prompt and returns a list of the top 10 (max) matching filenames in rank order, giving the rank score against each match. 

#Requirements
* Java 8
* Gradle

## Development
### Build
```
./gradlew build
```
### Run
```
./gradlew run pathToDirectoryContainingTextFiles
```
Type `:quit` to stop the application

## JAR file
### Create JAR file
```
./gradlew shadowJar
```
It will create a JAR file in `build/libs` folder.
### Run JAR file example
```
java ­jar build/libs/textsearcher-1.0-all.jar pathToDirectoryContainingTextFiles
```

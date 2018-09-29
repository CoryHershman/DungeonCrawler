# DungeonCrawler
A networked Dungeon Crawler for a class project. Created in java. Climb up the 100 floors of the tower, fighting a battle of attrition against a demon.
# Getting Started
These instructions will allow you to get a copy of the project up and running.
# Prerequisites
Java 1.8 will be necessary to install and run the project. Java 1.8 can be downloaded from http://www.oracle.com/technetwork/java/javase/downloads/jre8-downloads-2133155.html
# Installing
Clone or download the DungeonCrawler repository. Then extract all files from the zip to your preferred location. 
# Deploying

If deploying on a single machine:

Open the command prompt. Navigate to the directory cd command(s). If the directory is in downloads then the necessary command is: cd Downloads\DungeonCrawler-master
Then compile the code with: javac DungeonCrawler.java
Then run the game with: java DungeonCrawler
Type in "Server"
Then open another command prompt. Enter the same commands and type in "Client" instead of "Server"
The game is played from the client.

If deploying on two machines

First, in line 35 of Client.java replace "localhost" with the IP address of the server
On the server side, follow the same instructions as deploying on a single machine but do not open a second command prompt.
On the client side, follow the same instructions again but enter the command "Client" instead of "Server"

# Built With
Java 1.8 jdk
# Author
Cory Hershman - Design and Coding
# License
This project is licensed under the MIT license - see the LICENSE.md file for details

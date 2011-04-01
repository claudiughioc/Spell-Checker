build: Main.class

Main.class: Main.java

	javac Main.java

run: Main.class

	java Main

clean:

	rm -f *.class 

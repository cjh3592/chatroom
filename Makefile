all:
	javac ./*.java -d ./work
run:
	java -cp ./work Chatroom
lg:
	java -cp ./work Login
rg:
	java -cp ./work Register
sr:
	java -cp ./work Server
cli:
	java -cp ./work SocketClient
run_c:
	java -cp ./work Client
clean:
	rm ./work/Client.class

import java.io.*;
import java.net.*;
public class Server {

	public Server()throws IOException{
		ServerSocket serverSock = new ServerSocket(6789);
		
		Socket sock = serverSock.accept();
		System.out.println("Client connesso!");
		BufferedReader bfKeyboard = new BufferedReader(new InputStreamReader(System.in));
		BufferedReader bfInput = new BufferedReader(new InputStreamReader(sock.getInputStream()));
		BufferedWriter out = new BufferedWriter(new OutputStreamWriter(sock.getOutputStream()));
		String toSend;
		String received;
		while(true){
			/*toSend = bfKeyboard.readLine();
			out.write(toSend+"\n");
			out.flush();
			System.out.println("Server> "+toSend);*/
			received = bfInput.readLine();
			System.out.println("Client> "+received);
		
		}
		
		
		
	}
	
	public static void main(String args[]) throws IOException{
		Server s = new Server();
	}
}

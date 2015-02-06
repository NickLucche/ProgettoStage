import java.io.*;
import java.net.*;

public class ClientTester {

	public static void main(String[] args) {
		try{
		Socket socket = new Socket("localhost",1800);
		BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
		BufferedReader inputFromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
		String s = input.readLine();
		System.out.println(s);
		out.write(s+"\n");
		out.flush();
		s = inputFromServer.readLine();
		System.out.println(s);
		
		out.close();
		input.close();
		inputFromServer.close();
		}
		catch(IOException e){
			e.printStackTrace();
			
		}
	}

}

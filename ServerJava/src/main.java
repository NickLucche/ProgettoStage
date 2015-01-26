import java.net.*;
import java.io.*;

public class main {

	public static void main(String[] args) {

		try(
				ServerSocket s = new ServerSocket(6789);
				
				
				 
				){
			while(true){
				
				Socket sock = s.accept();
				BufferedReader bfKeyboard = new BufferedReader(new InputStreamReader(System.in));
				BufferedReader bfInput = new BufferedReader(new InputStreamReader(sock.getInputStream()));
				BufferedWriter out = new BufferedWriter(new OutputStreamWriter(sock.getOutputStream()));
				
				String stringa = s();
				int conta = stringa.getBytes().length;
				
				System.out.println("Connection found!");
				//HTTP Response
				out.write("HTTP/1.0 200 OK\r\n");
		        out.write("Date: Fri, 31 Dec 2015 23:59:59 GMT\r\n");
		        out.write("Server: Apache/0.8.4\r\n");
		        out.write("Content-Type: text/html\r\n");
		        out.write("Content-Length: "+conta+"\r\n");
		        out.write("Expires: Sat, 01 Jan 2000 00:59:59 GMT\r\n");
		        out.write("Last-modified: Fri, 09 Aug 1996 14:21:40 GMT\r\n");
		        out.write("\r\n");
		        out.write(stringa);
		        
		        String snull;
		        while((snull = bfInput.readLine())!=null){
		        	System.out.println(snull);
		        	if(snull.isEmpty()==true)
		        		break;
		        }
		        
		        out.close();
		        sock.close();
		        bfInput.close();
		        
		       
			}
		}
		catch(IOException e){
			e.printStackTrace();
			
		}
		

	}
	
	public static String s(){
	
		String s = "<html><title>Welcome</title>"
				+ "<head><style>btn {"
				+ "width:300px;"
				+ "height:300px;"
				+ "color : red;"
				+ "}"
				+ "txt {"
				+ "color:red;"
				+ "}"
				+ "</style></head>"
				+ "<body><button id ='btn' class = 'btn' >BTN</button><br><br>"
				+ "<font size = 30 id ='txt' class = 'txt'>Welcome!</font>"
				+ "</body>"
				+ "</html>";
		
		return s;
	}

}

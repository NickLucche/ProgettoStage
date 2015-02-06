import java.net.*;
import java.io.*;

public class ServerClassTimetable implements Runnable{


	public void run(){
		try{
			//this class is gonna receive a class name, and find the correspondent time schedule among the files saved as className.txt
			String className = "";
			ServerSocket server = new ServerSocket(1801);
			while(true){
				Socket socket = new Socket();
				System.out.println("Waiting for class schedule request from client(1801)..");
				socket = server.accept();
				BufferedReader inputFromApp = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				DataOutputStream output = new DataOutputStream(socket.getOutputStream());
				OutputStream out2 = socket.getOutputStream();
				System.out.println("Connessione accettata  client..");
				className = inputFromApp.readLine();
				className=className.replace("\n", "").trim();
				File fileClassSchedule = new File("TimeSchedules\\"+className+".txt");
				//File fileClassSchedule = new File("C:/Users/nick__000/workspace/SchoolsAppServer/TimeSchedules/"+className+".txt");

				if(!fileClassSchedule.exists()){
					output.writeUTF("Il file richiesto non è ancora presente nei nostri archivi, ci scusiamo\n");
				}
				else{
					System.out.println("class requested is "+className);


					BufferedInputStream bis = new BufferedInputStream(new FileInputStream(fileClassSchedule));

					byte[] mybytearray = new byte[(int)fileClassSchedule.length()];

					bis.read(mybytearray, 0, mybytearray.length);

					out2.write(mybytearray, 0, mybytearray.length);
					out2.flush();
				}


				/*    InputStream fileout = new FileInputStream(fileClassSchedule);

		        int len = 0;
		        while ((len = fileout.read(buf)) != -1) {
		        	out2.write(buf, 0, len);
		        }*/
				out2.close();

				inputFromApp.close();
				output.close();
			}


		}
		catch(IOException e){
			e.printStackTrace();


		}

	}
}

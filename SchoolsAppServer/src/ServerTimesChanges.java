import java.net.*;
import java.util.Scanner;
import java.util.HashMap;
import java.io.*;

import org.apache.poi.POITextExtractor;
import org.apache.poi.extractor.ExtractorFactory;
import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.xmlbeans.XmlException;

public class ServerTimesChanges implements Runnable{


	public void run(){



		try{
			ServerSocket server = new ServerSocket(1800);
			while(true){
				//Getting the current day, so that i download this file just once per day, when it changes
				//Date updating_date = new Date();
				Socket socket = new Socket();
				System.out.println("Waiting for connection(1800)..");
				socket = server.accept();
				System.out.println("Client found!");
				String fileName = "Times.doc"; //The file that will be saved on your computer
				
				//BufferedWriter output = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

				URL url;
				InputStream is = null;
				BufferedReader br;
				String line = "";

				try {
					url = new URL("http://www.istitutofermi.it/");
					is = url.openStream();  // throws an IOException
					br = new BufferedReader(new InputStreamReader(is));
					String contains_start = "VARIAZIONE ORARIO:<a href=";//change here if the html is rewritten 
					String contains_end = ".doc";
					while ((line = br.readLine()) != null) {
						
						if(line.contains(contains_start)){

							line = line.substring(line.indexOf("http://www.istitutofermi"),line.indexOf(contains_end)+4);
							break;
						}

					}
							
						
						System.out.println(line);
					
				} catch (MalformedURLException mue) {
					mue.printStackTrace();
				} catch (IOException ioe) {
					ioe.printStackTrace();
				} finally {
					try {
						if (is != null) is.close();
					} catch (IOException ioe) {
						// nothing to see here
					}
				}
				
				//Code to download
				URL link = new URL(line);
				InputStream in = new BufferedInputStream(link.openStream());
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				byte[] buf = new byte[1024];
				int n = 0;
				while ((n =in.read(buf))!=-1)
				{
					out.write(buf, 0, n);
				}
				out.close();
				in.close();
				byte[] response = out.toByteArray();

				FileOutputStream fos = new FileOutputStream(fileName);
				fos.write(response);
				fos.close(); //End download code
				System.out.println("File downloaded!");

				InputStream fis = new FileInputStream(fileName);
				POITextExtractor extractor;
				//fileName = fis.toString();

				if (fileName.toLowerCase().endsWith(".docx")) {
					System.out.println("Converting .DOCX file to .txt");
					XWPFDocument doc = new XWPFDocument(fis);
					extractor = new XWPFWordExtractor(doc);

				} else {
					System.out.println("Converting .DOC file to .txt");
					POIFSFileSystem fileSystem = new POIFSFileSystem(fis);
					extractor = ExtractorFactory.createExtractor(fileSystem);
				}

				String extractedText = extractor.getText();
				PrintWriter saveOnDisk = new PrintWriter("TimesChanges.txt");
				saveOnDisk.println(extractedText);
				System.out.println("File Converted!");
				saveOnDisk.close();
				fis.close();
				//putting the elements of the txt into an hashmap
				
				Scanner scanner = new Scanner(extractedText);
				HashMap<String, String> timetables = new HashMap<String, String>();
				String searchFor = "Classe";

				while(scanner.hasNextLine()){
					line = scanner.nextLine();
					if(line.startsWith(searchFor)){

						while(!line.startsWith("La presente")& scanner.hasNextLine()){
							line = scanner.nextLine();
							line = line.replace("^ ", "");         //replacing 1^ E with 1E
							String[] splitted_line = line.split("\t");
							String res="";
							if(splitted_line.length>2){
								res =splitted_line[2];
							}
							if(splitted_line.length>1){
								timetables.put(splitted_line[0].trim(), splitted_line[1]+res);
							}
						}

					}


				}
				//Listen to the client's demand( a class), then im gonna give him that
				BufferedReader input= new BufferedReader(new InputStreamReader(socket.getInputStream()));
				System.out.println("Inserisci stringa da ricercare");
				String received = input.readLine();
				received = received.replace("\n", "").trim();
				System.out.println(received);
				//Putting the timeschanges file in a scanner, so i can read into it and look for the class i am given


				  DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
	              
	                
				System.out.println(timetables);
				
				if(timetables.containsKey(received))
					dataOutputStream.writeUTF(received+"\t"+timetables.get(received) + "\n");
					
				else
					dataOutputStream.writeUTF("Mi spiace, la classe selezionata non è presente nel nostro database\n");

			}
		}
		catch(IOException e){
			e.printStackTrace();}
		catch(XmlException xml){}
		catch(OpenXML4JException xmle){}

	}
}







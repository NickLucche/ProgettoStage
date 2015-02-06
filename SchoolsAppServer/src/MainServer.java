import java.net.*;
import java.io.*;
public class MainServer {

	public static void main(String[] args) {
		try{
			ServerTimesChanges s1800 = new ServerTimesChanges();
			Thread t1800 = new Thread(s1800);
			t1800.start();
			ServerClassTimetable s1801 = new ServerClassTimetable();
			Thread t1801 = new Thread(s1801);
			t1801.start();



		}
		catch(Exception e){
			e.printStackTrace();
		}

	}

}

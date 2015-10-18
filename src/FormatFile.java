import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;


public class FormatFile {

	public static void main(String args[]) throws URISyntaxException, IOException{
			
			String folder="Movies";
		
			try {
				System.setIn(new FileInputStream(folder + "\\nodes"));
				//System.setOut(new PrintStream("nodeoutput"));
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			PrintStream stdout = System.out;

			ArrayList<String> unique=new ArrayList<String>();
			ArrayList<String> all=new ArrayList<String>();
			Scanner in=new Scanner(System.in);
			//System.out.println(in.nextInt());
			int i=0; int count=0; int copies=0; String text = new String(Files.readAllBytes(Paths.get(folder + "\\adj_list")), StandardCharsets.UTF_8);
			while(in.hasNext())
			{
				String url=in.nextLine();
				if(url.startsWith("http:"))
					{
					URL netUrl = new URL(url);
				    String host = netUrl.getHost();
				    if(host.startsWith("www"))
				        host = host.substring(4);
				    if(unique.indexOf(host)==-1)
				    	unique.add(host);
				    else
				    {
				    	int id=unique.indexOf(host);
				    	System.out.println("Actual ID = " +i);
				    	System.out.println("Copy OF = " +id);
				    	text=text.replace(" " +i+" ", " " + id + " ");
				    	text=text.replace(i+":", id + ":");
				    	text=text.replace(" " +i+"\n", " " + id + "\n");
				    	
				    	copies++;
				    }
				    all.add(host);
					//System.out.println(i +" "+ host);
					i++;
					}
				
			}
			System.out.println("No. of Copies = " +copies);
			System.out.println("No. of Unique = " +unique.size());
			System.out.println("Total = " +all.size());
			//System.out.println(text);
			PrintWriter writer = new PrintWriter(folder + "\\adj_list3", "UTF-8");
	    	writer.println(text);
	    	writer.close();
			try {
				System.setOut(new PrintStream(folder + "\\uniquenodes"));
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println(unique.size());
			for(i=0;i<unique.size();i++)
				System.out.println(i+" " +unique.get(i));
			
			System.setOut(new PrintStream(folder + "\\finaladj"));
			//System.setOut(stdout);
			text = new String(Files.readAllBytes(Paths.get(folder + "\\adj_list3")), StandardCharsets.UTF_8);
			System.setIn(new FileInputStream(folder + "\\adj_list3"));
			in=new Scanner(System.in); count=0;
			ArrayList <Integer> xs=new ArrayList<Integer>();
			while(in.hasNext())
			{
				String temp=in.next();
				if(temp.indexOf(":")!=-1){
					temp=temp.substring(0, temp.indexOf(":"));
					count++;
				}
				int x=Integer.parseInt(temp);
				if(x==-1) {continue;}
				//System.out.println(x< unique.size() && all.get(x).equals(unique.get(x)));
				if(x< unique.size() && all.get(x).equals(unique.get(x)))
				{
					
					continue;
				}
					
				else
				{
					xs.add(x);
					int id=unique.indexOf(all.get(x));
					String x1=x+"";
					String id1=id+"";
					text=text.replace(" " +x1+" ", " " + id1 + " ");
					text=text.replace(x1+":", id1 + ":");
					text=text.replace(" " +x1+"\n", " " + id1 + "\n");
					
				}
				
			}
			
			System.out.println(text);
			//System.out.println(xs.size() + "Size" );
			//System.setOut(stdout);
			for(i=0;i<xs.size();i++)
			{
				//System.out.println(xs.get(i) + " ");
			}
	}
}

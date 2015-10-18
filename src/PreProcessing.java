// For input, see testurls.txt
//For output, see outputurl.txt


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;

public class PreProcessing {
	
	public static void main(String args[])
	{
		try{
			System.setIn(new FileInputStream("testurls.txt"));
			System.setOut(new PrintStream("outputurl.txt"));
		}
		catch(FileNotFoundException e){
			e.printStackTrace();
		}
		Scanner in=new Scanner(System.in);
		String text="";
		ArrayList<String> list=new ArrayList<String>();
		int count=0;
		while(in.hasNext()){
			String url;
			url=in.next();
			count++;
			if(list.indexOf(url)==-1)
				list.add(url);
		}
		count=count/2;
		 try {
			text = new String(Files.readAllBytes(Paths.get("testurls.txt")), StandardCharsets.UTF_8);
			//System.out.println(text);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 int length=list.size();
		 System.out.println("No. of Pages= " +length);
		 System.out.println("No. of Edges= " +count);
		for(int i=0;i<length;i++)
		{
			//System.out.println(list.get(i));
			//System.out.println(text.lastIndexOf(list.get(i)));
			
			text=text.replaceAll(list.get(i), i+" ");
		}
		System.out.println(text);
		
	}

}

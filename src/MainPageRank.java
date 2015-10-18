
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;

//java -Xms64m -Xmx2048m Transition
public class MainPageRank {

	static HashMap<Integer, String> map;
	static double[][] counts; 
	static int[] outDegree;
	static Scanner in;
	static TreeMap<Double, String> urls=new TreeMap<Double,String>(Collections.reverseOrder());
	 
    public static void main(String[] args) throws FileNotFoundException {
    	
    	
    	try {
			System.setIn(new FileInputStream("finalinput.txt"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
        in=new Scanner(System.in);
        int numcat=in.nextInt();
        String[] cat=new String[numcat];
        int[] pages=new int[numcat];
        int N=0;
        for(int k=0;k<numcat;k++)
        {
        	cat[k]=in.next();
        	pages[k]=in.nextInt();
        	N+=pages[k];
        }
        String category=in.next();
        counts = new double[N][N];		// counts[i][j] = # links from page j to page i
        outDegree = new int[N];		// outDegree[i] = # links from page j to anywhere
        map= new HashMap<Integer, String>();
        int sum=0;
        for(int k=0;k<numcat;k++)
        {
        System.setIn(new FileInputStream(cat[k] + "\\finaladj"));
        in=new Scanner(System.in);
        takeInput(sum,sum+pages[k],cat[k]);
        sum=sum+pages[k];
        }
        
        double d=0.85;		//Damping Factor
        
        // Making the transition matrix into the required Markov form.
        stochasticMatrix(counts,N,outDegree,d);
        
        
        int count=0; int i=0;
        double[] v=new double[N];
        Iterator iter=map.entrySet().iterator();
        while(iter.hasNext()){
        	Map.Entry pair= (Map.Entry)iter.next(); 
        	//System.out.println("Page "+ i +" is in Category " + pair.getValue());
        	if(pair.getValue().equals(category))
        	{
        		count++;
        		v[i]=1;
        	}
        	else
        		v[i]=0;
        	i++;
        	iter.remove();
        }
        for(i=0;i<N;i++)
        	v[i] = (1-d) * v[i]/count;
        
        int iterations=100;
        double[] rank;
        if(!category.equals("None"))
        {
        	 System.out.println("Enhanced Page Rank for Category '" + category + "' is ");
             
             rank=computeCategorizedPageRank(counts,v,N,iterations);
        }
       
        else
        {
        	System.out.println("Regular Page Rank :");
        	rank=normalPageRank(counts,d,N,iterations);
        }
        
        sum=0;
        for(int k=0;k<numcat;k++)
        {
        	System.setIn(new FileInputStream(cat[k] + "\\uniquenodes"));
        	in=new Scanner(System.in);
        	populateTreeMap(sum,sum+pages[k],rank);
        	sum=sum+pages[k];
        }

        int max=10; 
        count=0;
        Iterator itertree=urls.entrySet().iterator();
        for (Map.Entry<Double, String> entry : urls.entrySet())
        {
        	if(count++ ==max)
        		break;
            //System.out.println(count+". PR=" + 100*entry.getKey() + " /URL = " + entry.getValue());
            System.out.printf("%d. PR = %7.5f /URL = %s\n",count,100*entry.getKey(),entry.getValue());
        }
    } 
 
 public static void populateTreeMap(int start,int N, double rank[])
 {
	 in=new Scanner(System.in);
     in.nextInt();
     for(int i=start;i<N;i++)
     {
    	in.nextInt();
     	urls.put(rank[i], in.next());
     }
 }

 public static void stochasticMatrix(double [][]a, int N, int out[],double d)
 {
	 for (int i = 0; i < N; i++)  {
         if(out[i]!=0)
         {
             for (int j = 0; j < N; j++) {
                 a[j][i] = d * a[j][i]/out[i]; 
             } 
         }
         else
         {
             for(int j=0;j<N;j++){
                 a[j][i]=d/(double)N;
             }    
         }
     } 
	 
 }
    
 public static void display(double a[][],int N)
 {
	 for(int i=0;i<N;i++)
	 {
		 for(int j=0;j<N;j++)
             System.out.printf("%7.4f ", a[i][j]); 
		 System.out.println();
	 }
	 
 }
 
 public static void display(double a[],int N)
 {
	 for(int i=0;i<N;i++)
	 {
         System.out.printf("%7.4f \n", a[i]); 
	 }
	 
 }
 
 public static double[] computeCategorizedPageRank(double [][]a, double []v, int N, int clicks)
 {
	 double[] rank = new double[N]; 
	 
	 //Initializing rank equally to all pages at the beginning.
	 
	 for(int i=0;i<N;i++)
		 rank[i]=1/(double)N;
	 
	 for (int i = 0; i < clicks; i++) {
		 	
         // Compute effect of next move on page ranks. 
         double[] newRank = new double[N]; 
         for(int j=0;j<N;j++)
         {
        	 for(int k=0;k<N;k++)
        		 newRank[j] += a[j][k]*rank[k];
        	 newRank[j]+= v[j];
         }
         // Update page ranks.
         rank = newRank;
         //System.out.println("Page Rank for Category A after " +(i+1)+" iteration(s) is as follows : ");
    	 //display(rank,N);
     } 
     
	 
	 //display(rank,N);
	 return rank;

 }
 
 public static double[] normalPageRank(double [][]a, double damp, int N, int clicks)
 {
	 for(int i=0;i<N;i++)
		 for(int j=0;j<N;j++)
			 a[i][j]+= (1-damp)/N;
	 double[] rank = new double[N]; 
	 
	 //Initializing rank equally to all pages at the beginning.
	 
	 for(int i=0;i<N;i++)
		 rank[i]=1/(double)N;
	 
     for (int t = 0; t < clicks; t++) {

         double[] newRank = new double[N]; 
         for (int j = 0; j < N; j++) {
             for (int k = 0; k < N; k++) 
                newRank[j] += rank[k] * a[j][k]; 
         } 

         // Update page ranks.
         rank = newRank;
     }
     
     //System.out.println("Normal PageRank is ");
    
     //display(rank,N);
     return rank;
 }
 
 
 public static void takeInput(int start, int end , String category)
 {
	 in=new Scanner(System.in);
	 for(int i=start;i<end;i++)
     {
     	
     	map.put(i, category);
     }
	 int count=0;
	 while (in.hasNext())  {
     	String temp=in.next();
     	count++;
		if(temp.indexOf(":")!=-1){
			temp=temp.substring(0, temp.indexOf(":"));
		}
		int i=Integer.parseInt(temp) + start;
		if(i>end) {
			in.nextLine(); continue;
		}
		int j=in.nextInt();
		while( j!= -1)
		{			
			if((j+start)>end)
			{   
				j = in.nextInt();
					continue;
			}
			outDegree[i]++; 
         	counts[j+start][i]++;
         	j = in.nextInt() ;
		}
     } 
	 
	 System.out.println(category + " Edge Count = " + count);
 }
 
} 


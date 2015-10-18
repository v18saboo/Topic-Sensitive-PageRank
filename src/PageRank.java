/* Input Format : 
 * First line contains an integer N which is the number of pages in the dataset.
 * This is followed by N lines with ith line giving the category of ith page in the dataset.
 * After N lines, an integer K gives the number of edges, and it is followed by K lines, and each of those K lines contains two integers
 * which give the direction of the edge. 
 * The next line contains a category name followed by the number of iterations based on which the page rank is computed. 
 * Ex:
 * 3
 * Category A
 * Category A
 * Category B
 * 4
 * 0 1
 * 0 2
 * 1 2
 * 2 1
 * Category B
 * 10
 */

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;

//java -Xms64m -Xmx2048m Transition
public class PageRank {


    public static void main(String[] args) {
    	/*
    	try {
			System.setIn(new FileInputStream("input.txt"));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
        Scanner in=new Scanner(System.in);
        int N = in.nextInt();		//number of pages
        
        double[][] counts = new double[N][N];		// counts[i][j] = # links from page j to page i
        int[] outDegree = new int[N];		// outDegree[i] = # links from page j to anywhere
        HashMap<Integer, String> map= new HashMap<Integer, String>();
        for(int i=0;i<N;i++)
        {
        	String category=in.next();		// category of ith page
        	map.put(i, category);
        }
        int k= in.nextInt(); 		// number of edges
        double d=0.85;		//Damping Factor
        while (k-->0)  {
            int i = in.nextInt(); 
            int j = in.nextInt(); 
            outDegree[i]++; 
            counts[j][i]++; 
        } 
        //display(counts,N);
        //System.out.println(N + " x " + N + " Adjacency Matrix : "); 
        
        for(int p=0;p<N;p++)
        {
            System.out.println("Out Degree of Page " + p + " is " + outDegree[p] );
        }
      
        // Making the transition matrix into the required Markov form.
        stochasticMatrix(counts,N,outDegree,d);
        
        String category=in.next();
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
        	v[i] = 0.15 * v[i]/count;
        //System.out.println("Category "+category+" vector = ");
        //display(v,N);
        int iterations=in.nextInt(); 		// number of clicks
        computeCategorizedPageRank(counts,v,N,iterations);
        normalPageRank(counts,d,N,iterations);
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
 
 public static void computeCategorizedPageRank(double [][]a, double []v, int N, int clicks)
 {
	 double[] rank = new double[N]; 
	 
	 //Initializing rank equally to all pages at the beginning.
	 
	 for(int i=0;i<N;i++)
		 rank[i]=1/(double)N;
	 //System.out.println("Initially, Rank is ");
	 //display(rank,N);
	 for (int i = 0; i < clicks; i++) {
		 	
         // Compute effect of next move on page ranks. 
         double[] newRank = new double[N]; 
         for(int j=0;j<N;j++)
         {
        	 for(int k=0;k<N;k++)
        		 newRank[j] += a[j][k]*rank[k];
        	 newRank[j]+=v[j];
         }
         // Update page ranks.
         rank = newRank;
         //System.out.println("Page Rank for Category A after " +(i+1)+" iteration(s) is as follows : ");
    	 //display(rank,N);
     } 
	 
	 System.out.println("Enhanced Page Rank for chosen category is ");
	 display(rank,N);

 }
 
 public static void normalPageRank(double [][]a, double damp, int N, int clicks)
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
     
     System.out.println("Normal PageRank is ");
     display(rank,N);
 }
 
} 


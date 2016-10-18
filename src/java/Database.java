package secure_top_k_query;



//import java.util.Base64;
import java.io.*;
//import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


public class Database {
    public List [] lists;
    public int m;
    public int n;
    public DataItemPositions[] dataPositions;
    private int maxIntRandom = 10000;
    Map<String,int[]> positions_elem=new HashMap<String,int[]>();

    public Database(int in_m, int in_n) throws FileNotFoundException, IOException{
        m = in_m;
        n = in_n;
        lists = new List[m];
        int i, j;
        for (i=0; i<m; ++i) {
            lists[i] = new List();
            lists[i].elements = new ListElement[n];
            for (j=0; j<n; ++j){
                lists[i].elements[j] = new ListElement();
            }
        }
        InputStream flux=new FileInputStream("C:\\Users\\sakina.m\\Documents\\NetBeansProjects\\resultat100million5.txt"); 
        InputStreamReader lecture=new InputStreamReader(flux);
        BufferedReader buff=new BufferedReader(lecture);
        String ligne;
        for (i=0; i<m; ++i) {
            lists[i] = new List();
            lists[i].elements = new ListElement[n];
            for (j=0; j<n; ++j) {
                lists[i].elements[j] = new ListElement();
                lists[i].elements[j].dataID=""+j;
                lists[i].elements[j].score=Double.parseDouble(ligne=buff.readLine());
            }
        }

        buff.close(); 

        dataPositions = new DataItemPositions [n];
        for (j=0; j<n; ++j) {
            dataPositions[j] = new DataItemPositions();
            dataPositions[j].dataID = ""+j;
            dataPositions[j].positionsInLists = new int [m];
        }
    
    }
  

  


    public void sortLists() {
    
        for (int i = 0; i < n; i++) {
            positions_elem.put(lists[0].elements[i].dataID, new int [m]);
        }
     
	for (int i=0; i<m; ++i) {
            long debut=System.currentTimeMillis();
            if((i==0)||(i==1)){
                QSort(0,n-1,i); 
            }else{
                QSort(0,n-1,i); 
            }
            long fin=System.currentTimeMillis();
            System.out.println("liste ["+i+"] est triée dans "+(fin-debut));
        }
    }
    
    public void  QSort  ( int  G,  int  D,int k  ) {  
        int  i ;
        if( D > G ) {
            i  =  partition ( G,D,k);
            QSort ( G,i - 1,k );
            QSort ( i + 1,D,k );
        }
    }

 
    public int  partition( int  G,  int  D  ,int k){  
        String dataID ;
        int  i, j ,x=0,y=0;
        double piv;
        ListElement temp,piv_score;
    
        piv  =  lists[k].elements[G].score ;
        piv_score=lists[k].elements[G];
        i  =  G + 1 ;
        j  =  D ;
        while (i < j) {
            while(i < j && lists[k].elements[j].score <= piv){ 
                if(x==0){
                    dataID = ""+lists[k].elements[j].dataID;
                    positions_elem.get(dataID)[k]=j;
                    x=1;
                }
                j--;
        
            }
            while(i < j && lists[k].elements[i].score >= piv){ 
                if(y==0){
                    dataID =""+ lists[k].elements[i].dataID;
                    positions_elem.get(dataID)[k] =i;
                    y=1;
                }
                i++;
            }
            temp = lists[k].elements[i];
            lists[k].elements[i]= lists[k].elements[j];
            lists[k].elements[j] = temp;
            dataID = ""+lists[k].elements[i].dataID;
            positions_elem.get(dataID)[k] =i;
            dataID =""+ lists[k].elements[j].dataID;
            positions_elem.get(dataID)[k] =j;
        }
        if (lists[k].elements[i].score < piv) i--;
        lists[k].elements[G] = lists[k].elements[i];
        dataID =""+ lists[k].elements[G].dataID;
        positions_elem.get(dataID)[k] =G;
        lists[k].elements[i] = piv_score;
        dataID =""+ lists[k].elements[i].dataID;
        positions_elem.get(dataID)[k] =i;
        
        return i;
    }
  

    public void printDatabase() {
        int i, j;
        double temp;
        String dataID;
        for (j=0; j<n; ++j) {
            System.out.print((j+1) + "  ->  " );
            for (i=0; i<m; ++i){
                temp = Math.round ((lists[i].elements[j].score) * 1000);
                temp = temp / 1000;
                dataID = lists[i].elements[j].dataID;
                System.out.print (dataID + "   :   " + temp + "        ");
            }
            System.out.println();
        }
    }



}




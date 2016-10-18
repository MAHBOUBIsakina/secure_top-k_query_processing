package secure_top_k_query;



import java.util.Arrays;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author smahboub
 */
public class EncryptedDatabase {
    
    public ListEncryptedDB [] listsCR;
    public int m;
    public int n;
    
    
    public EncryptedDatabase(int in_m, int in_n) {
        m = in_m;
        n = in_n;
        listsCR = new ListEncryptedDB[m];
        int i, j;
        for (i=0; i<m; ++i) {
            listsCR[i] = new ListEncryptedDB();
            listsCR[i].elementsCR = new ListElement_EncryptedDB[n];
            for (j=0; j<n; ++j) {
                listsCR[i].elementsCR[j] = new ListElement_EncryptedDB();
            }
        }
    }
    
    public void printDatabase() {
        int i, j;
        String dataID;
        for (j=0; j<n; ++j) {
            System.out.print((j+1) + "  -> " );
            for (i=0; i<m; ++i){
                dataID = listsCR[i].elementsCR[j].dataID;
                System.out.print (dataID + "  :  " + Arrays.toString(listsCR[i].elementsCR[j].score )+ "    "+listsCR[i].elementsCR[j].PID+"   ");
            }
        System.out.println();
        }
    } 

}

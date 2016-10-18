package secure_top_k_query;




//import proposition_1_quicksort_optimise.*;
//import proposition_1_optimisee.*;
//import proposition_1_hash_function_byte.*;
//import propo1_pqt_tll_eg_vector2.*;
//import propo1_pqt_tll_eg_vector.*;
//import proposition1_equal_size_packets.*;
import proposition_1_quicksort_non_optimise_final.*;
import java.util.Arrays;
import java.util.Iterator;
import javax.crypto.KeyGenerator;

import topk.*;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author smahboub
 */
public class proposition1main {

    /**
     * @param args the command line arguments
     */
    public static topk.Database convertdb(Database dbs, int numC,int numR){
        topk.Database dbc=new topk.Database(numC,numR);
        for (int i = 0; i < numC; i++) {
            for (int j = 0; j < numR; j++) {
                dbc.lists[i].elements[j].dataID=Integer.parseInt(dbs.lists[i].elements[j].dataID);
                dbc.lists[i].elements[j].score=dbs.lists[i].elements[j].score;
            }
        }
        return dbc;
    }
    
    public static int ieme_nbr_premier(int max){
        int divis, nbr, compt = 1 ;
         boolean Est_premier;
         
         for( nbr = 3; compt < max; nbr += 2 )
         { Est_premier = true;
           for (divis = 2; divis<= nbr/2; divis++ )
             if ( nbr % divis == 0 )
             { Est_premier = false;
                break;
             }
           if (Est_premier)
           {
               compt++;
               
           }
         }
         return nbr;
    }
    
    public static String generate(int length)
{
	    String chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890"; // Tu supprimes les lettres dont tu ne veux pas
	    String pass = "";
	    for(int x=0;x<length;x++)
	    {
	       int i = (int)Math.floor(Math.random() * 62); // Si tu supprimes des lettres tu diminues ce nb
	       pass += chars.charAt(i);
	    }
	    //System.out.println(pass);
	    return pass;
}
    
    
    public static void main(String[] args) {
        try{
        // TODO code application logic here
       // int max_values_score=1000;
        double a=Math.random()*1000;
        System.out.println("    "+a);
        int e=ieme_nbr_premier(1000);
        System.out.println("    "+e);
        int size=10;//il faut que numR soit un multiple de size
        int k=20;
        int numC=5,numR=100;
        ListElement [] final_result=new ListElement[k];
        for (int i = 0; i < k; i++) {
            final_result[i]=new ListElement();
            final_result[i].dataID="";
            final_result[i].score=0;
        }
        //System.out.println("a= "+a+" e= "+e+"  ");
         
        FirstProposition fp = new FirstProposition(numC,numR);
        System.out.println("creation de la base est terminée");
        //fp.db.printDatabase();
        fp.db.sortLists();
        System.out.println("sort fini");
        //fp.db.printDatabase();
        
        
        String key_string=generate(64);
        //"huyfhksdlkopijuhygtfreazdfdgoizeuydfuyifkiudf";
        byte [] key_xor=key_string.getBytes();
        
        //System.out.println("clé ="+Arrays.toString(key_xor));
        fp.encode_using_xor(key_xor);    
        System.out.println("chiffrement XOR FINI");
        //System.out.println("hhhhhhhhhhhhhhhhhhh"+j );
        //fp.db1.printDatabase();
        //fp.decode_using_xor(key_xor);
        //fp.db.printDatabase();
        MyBlowfish bf = new MyBlowfish();
        bf.generateKey();
        byte[] secretKey = bf.getSecretKeyInBytes();
        fp.encode_using_Blowfish(bf);
        System.out.println("CHIFFREMENT bLOWFISH fini");
        fp.baquetization_equal_packets(size,a,e);
        System.out.println("baquetization fini");
        //fp.printDatabase();
        long begin_run_time=System.currentTimeMillis();
        fp.get_result_algorithm_by_packet(k,size);
        long end_run_time=System.currentTimeMillis();

        System.out.println("le temps écouler pour calculer le résultat est"+(end_run_time-begin_run_time));
        //fp.db1.printDatabase();
        begin_run_time=System.currentTimeMillis();	
        fp.decode_DB(key_xor,bf);
        //fp.decode_using_Blofish(bf);
        end_run_time=System.currentTimeMillis();
        System.out.println("le temps écouler pour calculer le dechiffrement est"+(end_run_time-begin_run_time));
        begin_run_time=System.currentTimeMillis();	
        fp.top_k_elem_extraction(final_result,k);
        end_run_time=System.currentTimeMillis();
        System.out.println("le temps écouler pour calculer l'extraction des k elements est"+(end_run_time-begin_run_time));
         
         
         /*System.out.println("les reponses aprés l'élimination des elements");
    for (int i=0;i<fp.Answers.size();i++){
        if (fp.Answers.elementAt(i).dataID!=null){
        System.out.print("  "+fp.Answers.elementAt(i).dataID+"   "+fp.Answers.elementAt(i).PeriScoreinf+"   "+fp.Answers.elementAt(i).periscoresup+"   ");
        for (int j = 0; j < fp.Answers.elementAt(i).local_scores.length; j++) {
            System.out.print(Arrays.toString(fp.Answers.elementAt(i).local_scores[j].CR_score));
        }
        System.out.println();
    }}*/
    
  /*System.out.println("le tableau des Answers après le déchiffrement est");
        for (int i=0;i<fp.Answer_clair.size();i++){
           
       
                System.out.print("  "+fp.Answer_clair.elementAt(i).dataID+"     "+fp.Answer_clair.elementAt(i).PeriScoreinf+ "      "+fp.Answer_clair.elementAt(i).periscoresup);
                for (int h=0;h<fp.Answer_clair.elementAt(i).local_scores.length;h++){
                    System.out.print("     "+(fp.Answer_clair.elementAt(i).local_scores[h]));
            }
            System.out.println();
        }*/
        
        System.out.println("le resultat final de l'algorithme proposition1 ");
        for (int i = 0; i < k; i++) {
          System.out.println("  "+final_result[i].dataID+ "     "+final_result[i].score);
      }
       // int nbr_of_answers=0;
        /*for (int i = 0; i < numR; i++) {
          if(fp.Answers.elementAt(i).dataID.equals("")==false){
              nbr_of_answers++;
          }
      }*/
        
        //System.out.println("le nombre des éléments dans Answers ="+fp.Answers.size());
        System.out.println("le nombre de faux positif est= "+(fp.answers_client.size()-k));
        
//        
//    topk.Database db_copy= convertdb(fp.db,numC,numR);
//////         
////         System.out.println("le khiljhhjlo");
////         begin_run_time=System.currentTimeMillis();
//       topk.UseBPAExample.TA(db_copy,k);
//          end_run_time=System.currentTimeMillis();
//         System.out.println("temps="+(end_run_time-begin_run_time));
// 
// 
        begin_run_time=System.currentTimeMillis();
        fp.TA(k);
        end_run_time=System.currentTimeMillis();
      System.out.println("le resultat final de l'algorithme TA ");
for (int i = 0; i < k; i++) {
          System.out.println("  "+fp.top_k_AnswersTA[i].dataID+ "     "+fp.top_k_AnswersTA[i].PeriScoreinf);
      }
        System.out.println("temps="+(end_run_time-begin_run_time));

       
        }
        catch(Exception e){
            e.printStackTrace();
        }
       
    }
    
}

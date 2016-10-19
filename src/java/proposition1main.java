package secure_top_k_query;






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
	    String chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890"; 
	    String pass = "";
	    for(int x=0;x<length;x++)
	    {
	       int i = (int)Math.floor(Math.random() * 62);
	       pass += chars.charAt(i);
	    }
	    return pass;
}
    
    
    public static void main(String[] args) {
        try{
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
            FirstProposition fp = new FirstProposition(numC,numR);
            System.out.println("creation de la base est terminée");
            fp.db.sortLists();
            System.out.println("tri des listes est terminé");
        
            String key_string=generate(64);
            byte [] key_xor=key_string.getBytes();
        
            fp.encode_using_xor(key_xor);    
            System.out.println("chiffrement XOR terminé");
            MyBlowfish bf = new MyBlowfish();
            bf.generateKey();
            byte[] secretKey = bf.getSecretKeyInBytes();
            fp.encode_using_Blowfish(bf);
            System.out.println("chiffrement BLOWFISH terminé");
            fp.baquetization_equal_packets(size,a,e);
            System.out.println("baquetization terminé");
            long begin_run_time=System.currentTimeMillis();
            fp.get_result_algorithm_by_packet(k,size);
            long end_run_time=System.currentTimeMillis();

            System.out.println("le temps écouler pour calculer le résultat est"+(end_run_time-begin_run_time));
            begin_run_time=System.currentTimeMillis();	
            fp.decode_DB(key_xor,bf);
            end_run_time=System.currentTimeMillis();
            System.out.println("le temps écouler pour calculer le dechiffrement est"+(end_run_time-begin_run_time));
            begin_run_time=System.currentTimeMillis();	
            fp.top_k_elem_extraction(final_result,k);
            end_run_time=System.currentTimeMillis();
            System.out.println("le temps écouler pour extraire les k elements est"+(end_run_time-begin_run_time));
        
            System.out.println("le resultat final de l'algorithme BukTop ");
            for (int i = 0; i < k; i++) {
                System.out.println("  "+final_result[i].dataID+ "     "+final_result[i].score);
            }
            System.out.println("le nombre de faux positif est= "+(fp.answers_client.size()-k));
         
            begin_run_time=System.currentTimeMillis();
            fp.TA(k);
            end_run_time=System.currentTimeMillis();
            System.out.println("le resultat final de l'algorithme TA ");
            for (int i = 0; i < k; i++) {
                System.out.println("  "+fp.top_k_AnswersTA[i].dataID+ "     "+fp.top_k_AnswersTA[i].PeriScoreinf);
            }
            System.out.println("temps de l'algorithm TA est "+(end_run_time-begin_run_time));
        }catch(Exception e){
            e.printStackTrace();
        }
       
    }
    
}

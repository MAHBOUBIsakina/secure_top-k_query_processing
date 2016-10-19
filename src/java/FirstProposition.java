package secure_top_k_query;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */



import java.io.*;
import java.util.Arrays;
import java.util.Vector;
import java.util.HashMap;
import java.util.Map;


/**
 *
 * @author smahboub
 */
public class FirstProposition {
    int numColumns;
    int numRows;
    Database db;
    EncryptedDatabase db1;
    Vector<tempon_ansewer_ele> Answers;
    Vector<AnswerElementCR> answers_client;
    Vector<AnswerElementCR> AnswersTA;
    top_k_answer [] top_k_Answers;
    top_k_answer [] top_k_AnswersTA;
    Vector<AnswerElement> Answer_clair;
    Map<String,int[]> positions = new HashMap<String,int[]>();
    Map<String,double[]> score_sup = new HashMap<String,double[]>();
    
    public FirstProposition(int numC, int numR) throws IOException {
        numColumns=numC;
        numRows=numR;
        
        db= new Database(numColumns, numRows);
        db1=new EncryptedDatabase(numColumns, numRows);
        Answers=new Vector<tempon_ansewer_ele>();
        answers_client=new  Vector<AnswerElementCR> (numColumns);
        AnswersTA=new  Vector<AnswerElementCR> (numColumns);
        Answer_clair= new Vector<AnswerElement> (numColumns);
    }
    
   
    public void encode_using_xor(byte [] secret_key) {
        try{
            for(int i=0;i<numColumns;i++ ){
                for (int j=0;j<numRows;j++){
                    byte [] plaintext=db.lists[i].elements[j].dataID.getBytes("UTF-8");  
                    byte [] ciphertext = new byte[plaintext.length];
                    int spos = 0;
                    for (int k=0;k<plaintext.length;k++){
                        ciphertext[k]=(byte) (plaintext[k]^secret_key[spos]);
                        spos++;
                        if (spos >= secret_key.length) {
                            spos = 0;
                        }
                    }
                    db1.listsCR[i].elementsCR[j].dataID=new sun.misc.BASE64Encoder().encode(ciphertext);
                }
            }
        
        }catch(UnsupportedEncodingException e){
            System.out.println("erreur");
        }
    }
    
    public void encode_using_Blowfish(MyBlowfish blf){
       for(int i=0;i<numColumns;i++){
           for (int j=0;j<numRows;j++){
                double plaintext=db.lists[i].elements[j].score;
                byte [] ciphertext=blf.crypt(plaintext);
                db1.listsCR[i].elementsCR[j].score=Arrays.copyOf(ciphertext, ciphertext.length);
            }
        }

    }
    
    
    public void decode_DB(byte [] secret_key,MyBlowfish blf){
        try{
            int i=0;
            while (i<answers_client.size()){
                AnswerElement temp = new AnswerElement(numColumns);
                int spos = 0;
                byte [] ciphertext=(new sun.misc.BASE64Decoder().decodeBuffer(answers_client.elementAt(i).dataID));
                byte [] ciphertext1=new byte [ciphertext.length];
                for (int pos = 0; pos < ciphertext.length; ++pos) {
                    ciphertext1[pos] = (byte) (ciphertext[pos] ^ secret_key[spos]);
                    ++spos;
                    if (spos >= secret_key.length) {
                        spos = 0;
                    }
                }
                temp.dataID=new String(ciphertext1, "UTF-8");
                for(int k=0;k<numColumns;k++){
                    temp.local_scores[k]=blf.decryptInDouble(answers_client.elementAt(i).local_scores[k].CR_score);             
                }
                Answer_clair.addElement(temp);
                i++;
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
 
    
   
    public void BuckTop_bucketization(int size,double z,int y) {
        int last_elem;
        double pid;
        double score_supr;
        for(int i=0;i<numColumns;i++ ){
            double x1;
            double x2;
            double[] tab=new double[(numRows/size)*2];
            int f=size;
            int l=0;
            while(l < tab.length) {
                if(l==0){
                    tab[l]=db.lists[i].elements[0].score+Math.random()*((db.lists[i].elements[0].score+100)-db.lists[i].elements[0].score);
                    l++;
                }else{
                    if(l>0&&l<tab.length-1){
                        x1=db.lists[i].elements[f].score+Math.random()*(db.lists[i].elements[f-1].score-db.lists[i].elements[f].score);
                        x2=db.lists[i].elements[f].score+Math.random()*(db.lists[i].elements[f-1].score-db.lists[i].elements[f].score);
                        if(x1>x2){
                            tab[l]=x1;
                            tab[l+1]=x2;
                        }else{
                            tab[l]=x2;
                            tab[l+1]=x1;
                        }
                        l=l+2;
                        f=f+size;
                    }else{
                        if(l==tab.length-1){
                            tab[l]=Math.random()*(db.lists[i].elements[numRows-1].score);
                            l=l+1;
                        }
                    }
                }
            }
            l=0;
            for (int j=0;j<numRows;j=j+size){
                score_supr=tab[l]*z+y;
                l++;
                last_elem=j+size;
                pid=tab[l]*z+y;
                l++;
                for(int k=j;k< last_elem;k++){
                    int x=k+(int)(Math.random()*( (j+size-1)-k));
                    ListElement_EncryptedDB temp=db1.listsCR[i].elementsCR[k];
                    db1.listsCR[i].elementsCR[k]=db1.listsCR[i].elementsCR[x];
                    db1.listsCR[i].elementsCR[x]=temp;
                    db1.listsCR[i].elementsCR[k].PID=pid;
                    if(i==0){
                        positions.put(db1.listsCR[i].elementsCR[k].dataID, new int[numColumns]);
                        score_sup.put(db1.listsCR[i].elementsCR[k].dataID, new double[numColumns]);
                    }
                    positions.get(db1.listsCR[i].elementsCR[k].dataID)[i]=k;
                    score_sup.get(db1.listsCR[i].elementsCR[k].dataID)[i]=score_supr;
                }
                if(j<numRows-size){
                    score_supr=Math.random()*(db1.listsCR[i].elementsCR[last_elem-1].PID-db.lists[i].elements[last_elem].score)*z-y;
                }
            }
            tab=null;
        }
        
    }
    
   
    private double computeOverallScore (double [] localScores) {
        double overallScore =0;
        
        for (int k=0; k<numColumns; ++k)
            overallScore = overallScore + localScores[k];
        
        return overallScore;

    }
 
    
    public void BuckTop_query_processing(int k_elements,int size){
        long begintime=System.currentTimeMillis();
        Map<String,Integer> exist_in_answers = new HashMap<String,Integer>();
        double [] seenDatalocalScores = new double [numColumns];
        double [] currentPositionScores = new double [numColumns];
        int last_position=0;
        double min_periscore=0;
        double seenDataPeriScore;
        String seenDataID;
        double threshold;
        int minIndex = 0;
        double min;
        top_k_Answers=new top_k_answer[k_elements];
        for (int k=0;k<k_elements;k++){
            top_k_Answers[k]=new top_k_answer();
            top_k_Answers[k].dataID="";
            top_k_Answers[k].PeriScoreinf=-1;
        }
        boolean stop1=false;
        boolean stop2=false;
        int j;
        int i=0;
        int limit;
        while((stop1==false)&&(stop2==false)){
            for (j=0;j<numColumns;j++){
                currentPositionScores[j]=db1.listsCR[j].elementsCR[last_position+size-1].PID;
                limit=last_position+size;
                for (i=last_position;i<limit;i++){
                    seenDataID = db1.listsCR[j].elementsCR[i].dataID;
                    if(exist_in_answers.get(seenDataID)==null){
                        tempon_ansewer_ele temp = new tempon_ansewer_ele();
                        int [] posi=positions.get(seenDataID);
                        for (int g=0;g<numColumns;g++){
                            seenDatalocalScores[g] =db1.listsCR[g].elementsCR[posi[g]].PID;
                        }
                        seenDataPeriScore = computeOverallScore (seenDatalocalScores);
                        temp.dataID = seenDataID;
                        temp.PeriScoreinf = seenDataPeriScore;
                        temp.periscoresup = computeOverallScore (score_sup.get(seenDataID));
                        Answers.add(temp);
                        exist_in_answers.put(seenDataID, 1);
                        if (seenDataPeriScore > min_periscore){
                            top_k_Answers[minIndex].dataID = seenDataID;
                            top_k_Answers[minIndex].PeriScoreinf = seenDataPeriScore;
                            min=top_k_Answers[0].PeriScoreinf;
                            minIndex = 0;
                            for (int h=1; h<k_elements; ++h){
                                if (top_k_Answers[h].PeriScoreinf < min) {
                                    min = top_k_Answers[h].PeriScoreinf;
                                    minIndex = h;
                                }
                            }
                            min_periscore = min;
                        }
                    }
                    
                }
            }
            last_position=last_position+size;
            threshold = computeOverallScore (currentPositionScores);
            if(threshold<=min_periscore){
                stop1=true;
            }
            if(last_position>=numRows){
                stop2=true;
            }
        }
        
        long endtime=System.currentTimeMillis();
        System.out.println("le temps du proposition1 est "+(endtime-begintime));
        System.out.println("la stop position est "+(i));
        long x=System.currentTimeMillis();
        j = Answers.size();
        System.out.println("la taille des réponses est "+j);
        for( i=0;i<j;i++){
            if(Answers.elementAt(i).periscoresup>=min_periscore){
                AnswerElementCR temp = new AnswerElementCR(numColumns);
                temp.dataID=Answers.elementAt(i).dataID;
                int [] posi=positions.get(Answers.elementAt(i).dataID);
                for (int k = 0; k < numColumns; k++) {
                    int s=db1.listsCR[k].elementsCR[posi[k]].score.length;
                    temp.local_scores[k]= new CryptedScore(s);
                    for (int l = 0; l < s; l++) {
                        temp.local_scores[k].CR_score[l]=db1.listsCR[k].elementsCR[posi[k]].score[l];
                    }
                }
                answers_client.add(temp);
            }
        }
        long m=System.currentTimeMillis();
        System.out.println("le temps d'elimination des éléments est "+(m-x)+"la taille des réponses aprés le filtrage est"+answers_client.size());
      
    }
    
  
  
  public void BuckTop_pre_processing(ListElement [] final_result, int k){
        double global_score;
        double min;
        int index_min;
        double min_glb_scr=0;
        for (int i = 0; i <Answer_clair.size(); i++) {
            global_score=computeOverallScore(Answer_clair.elementAt(i).local_scores);
            if (global_score > min_glb_scr){
                min=final_result[0].score;
                index_min=0;
                for (int j = 0; j < k; j++) {
                    if(final_result[j].score<min){
                        min = final_result[j].score;
                        index_min=j;
                    }
                }
                if (global_score > min){
                    final_result[index_min].score=global_score;
                    final_result[index_min].dataID=Answer_clair.elementAt(i).dataID;
                }
                min=final_result[0].score;
                    for (int j = 0; j < k; j++) {
                    if(final_result[j].score<min){
                        min = final_result[j].score;
                    }
                }
                min_glb_scr = min;
            }
        }
    }
  
  
  
  
  public void TA (int k_elements){
      
        Map<String,Integer> exist_in_answersTA = new HashMap<String,Integer>();
        double [] seenDatalocalScores = new double [numColumns];
        double [] currentPositionScores = new double [numColumns];
        double min_periscore=0;
        double seenDataOverallScore;
        String seenDataID;
        double threshold;
        int minIndex = 0;
        double min;
        int dataPosition = 0;
        top_k_AnswersTA=new top_k_answer[k_elements];
        for (int k=0;k<k_elements;k++){
            top_k_AnswersTA[k]=new top_k_answer();
            top_k_AnswersTA[k].dataID="";
            top_k_AnswersTA[k].PeriScoreinf=-1;
        }
        boolean stop=false;
        int j=0;
        int i;
        while((stop==false)&&(j<numRows)){
            for (i=0;i<numColumns;i++){
                currentPositionScores[i]=db.lists[i].elements[j].score;
                seenDataID = db.lists[i].elements[j].dataID;
                int [] posi=db.positions_elem.get(seenDataID);
                for (int g=0;g<numColumns;g++){
                    dataPosition =posi[g];
                    seenDatalocalScores[g] =db.lists[g].elements[dataPosition].score;
                }
                seenDataOverallScore = computeOverallScore (seenDatalocalScores);
                if(exist_in_answersTA.get(seenDataID)==null){
                    if (seenDataOverallScore > min_periscore){
                        top_k_AnswersTA[minIndex].dataID = seenDataID;
                        top_k_AnswersTA[minIndex].PeriScoreinf = seenDataOverallScore;
                        min=top_k_AnswersTA[0].PeriScoreinf;
                        minIndex = 0;
                        for (int h=1; h<k_elements; ++h){
                            if (top_k_AnswersTA[h].PeriScoreinf < min) {
                                min = top_k_AnswersTA[h].PeriScoreinf;
                                minIndex = h;
                            }
                        }
                        min_periscore = min;
                    }
                    exist_in_answersTA.put(seenDataID, 1);
                }
            }
            threshold = computeOverallScore (currentPositionScores);
            if(threshold<=min_periscore){
                stop=true;
            }
            j++;
        }
        System.out.println("stop position ="+j);
    }
  
    
    public void printDatabase() {
        int i, j;
        double temp;
        String dataID;
        for (j=0; j<numRows; ++j) {
            System.out.print((j+1) + "  -> " );
            for (i=0; i<numColumns; ++i){
                temp = Math.round ((db.lists[i].elements[j].score) * 1000);
                temp = temp / 1000;
                dataID = db.lists[i].elements[j].dataID;
                System.out.print (dataID + "   :   " + temp + "        "+   db1.listsCR[i].elementsCR[j].PID+"   ");
            }
            System.out.println();
        }
    }
   
   
}

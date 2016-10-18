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
public class AnswerElementCR {
    public String dataID;
    public CryptedScore [] local_scores;
    public AnswerElementCR(int numc){
        local_scores=new CryptedScore[numc];
    }
 
    
}

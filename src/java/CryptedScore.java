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
public class CryptedScore {
    public byte [] CR_score;
    public CryptedScore(int taille){
        CR_score = new byte[taille];
    }
    
}

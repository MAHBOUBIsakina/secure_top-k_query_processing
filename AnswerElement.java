package secure_top_k_query;




public class AnswerElement {
  public String dataID;
  public double PeriScoreinf;
  public double periscoresup;
  public double [] local_scores ;
  public AnswerElement (int numc){
       local_scores=new double[numc] ;
  }
}

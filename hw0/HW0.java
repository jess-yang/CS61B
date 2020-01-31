public class HW0{
  public static void main(String[] args){
    int[] a= {1,-2};
    int[] b= {1,0,0};
    int[] c={1,-1,2,-3,1};
    //System.out.println(max(a));
    //System.out.println(max(b));
    System.out.println(threeSumDistinct(a));
    System.out.println(threeSumDistinct(b));
    System.out.println(threeSumDistinct(c));
  }
  public static int max(int[] a){
    int largest=a[0];
    for(int i=0; i<a.length; i++){
      if (a[i]>largest){
        largest=a[i];
      }
    }
    return largest;
  }
  public static boolean threeSum(int[] a){
    for(int i=0; i<a.length; i++){
      for(int j=0; j<a.length; j++){
        for(int k=0; k<a.length; k++){
          if(a[i]+a[j]+a[k]==0){
            //System.out.print(i,j,k); why cant this print?
            return true;
          }
        }
      }
    }
    return false;
  }
  public static boolean threeSumDistinct(int[] a){
    for(int i=0; i<a.length-2; i++){
      for(int j=i+1; j<a.length-1; j++){
        for(int k=j+1; k<a.length; k++){
          if(a[i]+a[j]+a[k]==0){
            return true;
          }
        }
      }
    }
    return false;
  }
}

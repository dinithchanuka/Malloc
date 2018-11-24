
package mymalloc;

/**
 *
 * @author Dinith
 */
public class MyMalloc {

    static int Mem_Size = 25000; //define the maximum size of memory
    static int init = 0;
    static char[] arr = new char[Mem_Size];
    
    static int bsize(int index ){
        return((arr[index] & 0x7f << 8) | (arr[index + 1] & 0xff));
    }
    
    static boolean bisfree(int index){
        return (arr[index] & 0x80) == 0x80;
    }
    
    static void writeheader(int index, int size, int isfree){
        arr[index] = (char) ((isfree<<7) | ((size & 0x7f00)>>8));
        arr[index+1] = (char) (size & 0xff);
    }
    
    static int mymalloc(int size)
    {
        int cursor , bindex ;
        
        if(size < 0 || size > Mem_Size - 2 || size > 32768){
            return -1;
        }
        
        if(init == 0) {
            writeheader(0, Mem_Size-2, 1);
            init = 1;
        }
        
        // best fit free block finding
        for(bindex = cursor = 0 ; cursor< arr.length; cursor+= bsize(cursor)+2)
            if(bisfree(cursor)&& bsize(cursor)>= size && bsize(cursor)<= bsize(cursor)){
                bindex = cursor;
            }
        
        //memory allocation
        if (bsize(bindex) == size) {
            writeheader(bindex, bsize(bindex), 0);
            return bindex + 2;
            
        } else if (bsize(bindex) >size){
            int prevsize = bsize(bindex);
            writeheader(bindex,size,0);
            writeheader(bindex+size+2,prevsize-size-2,1);
            return bindex +2;
        }
        
        //if not enough memory
        return -1;
    }
    
    static void myfree(int index){
        int nextcursor;
        
        if(index < 2 || index > Mem_Size){
            return;
        }
        
        index -=2;
        
        //Identifying as a free
        writeheader(index, bsize(index),1);
        
        //if next block also free; merge them
        nextcursor = index + bsize(index) + 2;
        
        if(bisfree(nextcursor)){
            writeheader(index, bsize(index)+bsize(nextcursor)+2,1);
        }
    }
    
    public static void main(String[] args) {
           
        //Test case . . .
           int i;
           int m = mymalloc(4);
           System.out.println(m);
           arr[m] =1;
           arr[m+1] =2;
           arr[m+2] =3;
           arr[m+3] =4;
           
           for(i=0;i<Mem_Size;i++){
               System.out.print((int) arr[i] + " ");
           }
           System.out.println();
           
           myfree(m);
           
           for(i=0;i<Mem_Size;i++){
               System.out.print((int) arr[i] + " ");
           }
           System.out.println();
    } 
}

import java.io.PrintWriter;
import java.io.FileNotFoundException;


public class PrintWriter {
    private String filename; //nama file yang akan disimpan
    private PrintWriter pw; //variabel untuk printwriter
    private char[][] solution; //hasil dari solusi berbentuk array 2d
    

    //contructor kelas PrintWriter menerima parameter string dan char[][]
    public PrintWriter(String fn, char[][] sol){
        this.filename = fn;
        this.solution = sol;
    }

    //Method untuk menyimpan jadwal ke file txt
    public void save (){
        try{
            pw = new PrintWriter(filename); //instasiasi variabel pw dengan nama file

            //iterasi untuk menulis masing-masing karakter ke file
            for(int i = 0;i < tempSol.length; i++){
                for(int j = 0; j<solution[i].length; j++){
                    pw.write(solution[i][j]); //tulis masing-masing karakter pada array 2d
                }
                pw.write("\n"); // pindah baris
            }
            pw.flush(); //membersihkan stream

        } catch(FileNotFoundException e){
            System.out.println("Error: " + e.getMessage());	//print error message
        }
    }
}

import java.io.PrintWriter;
import java.io.FileNotFoundException;

public class PrintWriter {
    private String filename;
    private PrintWriter pw;
    private char[][] solution;
    
    public PrintWriter(String fn, char[][] sol){
        this.filename = fn;
        this.solution = sol;
    }

    public void save (){
        try{
            pw = new PrintWriter(filename);

            for(int i = 0;i < tempSol.length; i++){
                for(int j = 0; j<solution[i].length; j++){
                    pw.write(solution[i][j]);
                }
                pw.write("\n");
            }
            pw.flush();
        } catch(FileNotFoundException e){
            System.out.println("Error: " + e.getMessage());			
        }
    }
}

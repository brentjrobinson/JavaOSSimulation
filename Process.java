import java.util.Stack;

/**
 * Created by brent on 2/28/2017.
 */
public class Process {

   private int jobNum;  //Labeled number for job
   private int priority; // priority for scheduling
   private int buffer;  //temp buffer for instruction size
   private int location; // location in PCB
   private int inputBuffer;
   private int outputBuffer;
   private int tempBuffer;
   private String status;

    public Process(int j, int p, int b, int l)
    {
        this.jobNum =j;
        this.priority = p;
        this.buffer = b;
        this.location = l;
        this.status = "NEW";
    }



    public void addDataInfo (int inputBuffer, int outputBuffer, int tempBuffer)
    {
        this.inputBuffer = inputBuffer;
        this.outputBuffer = outputBuffer;
        this.tempBuffer = tempBuffer;
    }
    public String toString (){
        return "Job " + jobNum + " has priority " + priority + " and buffer size: " + buffer + " in location " + location;
    }

    public int getJobNum() {
        return jobNum;
    }

    public int getPriority() {
        return priority;
    }

    public int getBuffer() {
        return buffer;
    }

    public int getLocation() {
        return location;
    }

    public int getInputBuffer() {
        return inputBuffer;
    }

    public void setInputBuffer(int inputBuffer) {
        this.inputBuffer = inputBuffer;
    }

    public int getOutputBuffer() {
        return outputBuffer;
    }

    public void setOutputBuffer(int outputBuffer) {
        this.outputBuffer = outputBuffer;
    }

    public int getTempBuffer() {
        return tempBuffer;
    }

    public void setTempBuffer(int tempBuffer) {
        this.tempBuffer = tempBuffer;
    }
}

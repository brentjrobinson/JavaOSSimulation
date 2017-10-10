import java.util.Queue;
import java.util.Stack;

/**
 * Created by brent on 3/14/2017.
 */
public class PCB {


    public Stack<Process> processes;
    private int counter;
    public Queue<Process> waiting;
    public Queue<Process> blocked;
    public String state ="READY";

    public PCB()
    {

        counter = 0;
        processes = new Stack<>();

    }

    public void setState(String s){
        this.state = s;
    }
    public void addWaiting(Process p)
    {
        this.waiting.add(p);
    }
    public void addBlocked(Process p)
    {
        this.blocked.add(p);
    }


    public void addProcess(Process p)
    {
        this.processes.push(p);
        counter++;
    }

    public Process peekProcess()
    {
        return processes.peek();
    }

    public int getCounter() {
        return counter;
    }
    public Process nextProcess()
    {
        processes.pop();
        return processes.peek();
    }

    public void PCBDump()
    {

        while (!processes.empty())
        {
            System.out.println(processes.peek());
            processes.pop();
        }
    }

    public void popProcess() {
        processes.pop();
    }
}

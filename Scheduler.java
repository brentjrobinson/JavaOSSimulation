
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by brent on 3/14/2017.
 */
public class Scheduler {
    int ramRemaining = 1024; // ram empty at start
    ArrayList<Process> ready;
    int jobServed;



    public Scheduler(){
            ready = new ArrayList<>();
            jobServed = 0;

    }

    public void transfer()
    {
        for (int i = 0; i < ready.size(); i++)
        {
            Driver.PCB.processes.push(ready.get(i));
        }
    }

    public void acceptJobs()
    {
        ready.add(Driver.PCB.peekProcess());
        for (int i = 0; i < Driver.PCB.getCounter()-1; i++)
        {
            Driver.PCB.popProcess();
            ready.add(Driver.PCB.peekProcess());
        }

    }

    public void sortPriority(){
        String instruction;
        Collections.sort(ready, (o1, o2) -> {
            if (o1.getPriority() > o2.getPriority())
                return 1;
            if (o1.getPriority() < o2.getPriority())
                return -1;
            return 0;
        });
    }
    public void shortTermPriority()
    {

       sortPriority();
        int counter =0;
        for (int j = 0; j < ready.size(); j++) {
            for (int i = ready.get(j).getLocation(); i < jobEND(ready.get(j)); i++) {
                //System.out.println(hexToBinary(Driver.memory.pullDisk(i).substring(2, 10)));
                Driver.memory.writeRAM(hexToBinary(Driver.memory.pullDisk(i).substring(2, 10)), counter);
                counter++;
                ramRemaining--;
            }
            jobServed++;
        }


    }
    public void shortTermFIFO()
    {
        int counter =0;
        for (int j = 0; j < ready.size(); j++) {
            for (int i = ready.get(j).getLocation(); i < jobEND(ready.get(j)); i++) {
                //System.out.println(hexToBinary(Driver.memory.pullDisk(i).substring(2, 10)));
                Driver.memory.writeRAM(hexToBinary(Driver.memory.pullDisk(i).substring(2, 10)), counter);
                counter++;
            }
            jobServed++;

        }



    }

    public static String hexToBinary(String hex) {
        return new BigInteger(hex, 16).toString(2);
    }

    public int jobEND(Process p)
    {
        return (p.getLocation() + p.getBuffer());
    }
}

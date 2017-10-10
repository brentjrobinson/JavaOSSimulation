import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

/**
 * Created by brent on 2/28/2017.
 */
public class Driver {
     static Loader loader;
     static Memory memory;
     static PCB PCB;
     static Scheduler sched;
     static CPU CPU;
     static Dispatcher dispatcher;
 
 
 
 
 
    public static void main(String[] args) throws IOException {


        Scanner sc = new Scanner(System.in);String path = "";
        System.out.print("Please enter the file path for the 'Program-File.txt: ");
        path = sc.next();
        FileReader instructions = new FileReader(path);
        loader = new Loader(instructions);
        memory = new Memory(2048, 1024);
        PCB = new PCB();
        Driver.loader.load();
        sched = new Scheduler();
        sched.acceptJobs();
        //sched.shortTermFIFO();
        sched.shortTermPriority();
        sched.transfer();
        dispatcher = new Dispatcher();
        dispatcher.dispatch();
        CPU = new CPU();
        memory.percentUsed();

        //memory.dumpRam();
 

 
    }
}
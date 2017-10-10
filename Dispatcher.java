/**
 * Created by Kyle on 04/18/2017
 */
public class Dispatcher {

    public void dispatch()
    {
       Driver.PCB.processes.pop();
       while(!Driver.PCB.processes.isEmpty()) {
           CPU.PC = Driver.PCB.processes.peek().getLocation();
           CPU.IR = Long.parseLong(Driver.memory.pullRAM(Driver.PCB.processes.firstElement().getLocation()), 2);
           CPU.setInputBuffer(Driver.PCB.peekProcess().getInputBuffer());
           CPU.setOutputBuffer(Driver.PCB.peekProcess().getOutputBuffer());
           CPU.setTempBuffer(Driver.PCB.peekProcess().getTempBuffer());
           Driver.PCB.setState("READY");
          // System.out.println("Dispatched a process!!");
           Driver.PCB.processes.pop();
       }
    }
}

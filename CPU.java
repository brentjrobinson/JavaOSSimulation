/**
 * Created by Kyle on 04/18/2017
 */
public class CPU {
	//CPU registers set by Decode and executed by Execute
	private static int SourceReg1, SourceReg2, DestinationReg, BaseReg, Address, Data, OC;
	private static int InputBuffer, OutputBuffer, TempBuffer;
	private static boolean END;
	// Global PC which is set initially by the Dispatcher 
	public static long PC, IR;
	// CPU registers. 0 is Accumulator. 1 is ZERO. Others are general purpose.
    public int[] registers = new int[16];

    public int wait =0;
	// Masks
	private final static int TYPE_MASK = 0xC0000000;
	private final static int OPCODE_MASK = 0x3F000000;
	private final static int SREG1_MASK = 0x00F00000;
	private final static int SREG2_MASK = 0x000F0000;
	private final static int DREG_ARITH_MASK = 0x0000F000;
	private final static int BREG_MASK = 0x00F00000;
	private final static int DREG_COND_MASK = 0x000F0000;
	private final static int SIXTEEN_BIT_MASK = 0x0000FFFF;
	private final static int TWENTYFOUR_BIT_MASK = 0x00FFFFFF;
	
	
	public CPU(){ //Sets registers and Buffers to 0
		for (int i = 0; i < registers.length; i++){
			registers[i] = 0;
		}
		SourceReg1 = 0;
		SourceReg2 = 0;
		DestinationReg = 0;
		BaseReg = 0;
		OC = 0;
		Address = 0;
		Data = 0;
		InputBuffer = 0;
		OutputBuffer = 0;
		TempBuffer = 0;
		END = false;
		Driver.PCB.setState("READY");
		while(Driver.PCB.state.equals("READY")) {
            ComputeOnly();
            PC++;
        }
	}

    public static int getInputBuffer() {
        return InputBuffer;
    }

    public static void setInputBuffer(int inputBuffer) {
        InputBuffer = inputBuffer;
    }

    public static void setOutputBuffer(int out) {
         OutputBuffer = out;
    }

    public static int getOutputBuffer() {
        return OutputBuffer;
    }

    public static int getTempBuffer() {
        return TempBuffer;
    }

    public static void setTempBuffer(int tempBuffer) {
        TempBuffer = tempBuffer;
    }

    private void ComputeOnly(){


	    while (!END) {
	        long start = System.nanoTime();
	        //System.out.println("Current STATE: " +Driver.PCB.state);
            Fetch(); //Pulls the instruction at the PC
            Decode(); //Decodes the instruction and sets registers
            Execute(); //Executes the code
            long end = System.nanoTime();
            System.out.println(end-start);
        }

	}
	
	private void DMA (int OC){

        switch(OC){
		case 0: // I/O Read contents of I/P buffer and store in accumulator
			if (registers[SourceReg2] != 0)
				registers[SourceReg1] = registers[SourceReg2];
			else
    			registers[SourceReg1] = (int) Long.parseLong(Driver.memory.pullRAM(Address), 2);
			break;
		case 1: // I/O Write contents of accumulator into O/P buffer
			if (SourceReg2 != 0)
				registers[SourceReg2] = SourceReg1;
			else
				Driver.memory.writeRAM(Integer.toBinaryString(registers[SourceReg1]), Address);
			break;
		}
	}
	
	private void Fetch(){
		IR = Long.parseLong(Driver.memory.pullRAM((int) PC), 2);
		PC++;
	}
	
	private void Decode(){
		long instrType = ((IR & TYPE_MASK) >> 30);
		switch ((int)instrType){
		case 0: //Arithmetic
			OC = (int)((IR & OPCODE_MASK) >> 24);
			SourceReg1 = (int)((IR & SREG1_MASK) >> 20);
			SourceReg2 = (int)((IR & SREG2_MASK) >> 16);
			DestinationReg = (int)((IR & DREG_ARITH_MASK) >> 12);
		case 1: //Conditional Branch & Immediate
			OC = (int)((IR & OPCODE_MASK) >> 24);
			BaseReg = (int)((IR & BREG_MASK) >> 20);
			DestinationReg = (int)((IR & DREG_COND_MASK) >> 16);
			if (DestinationReg == 0)
				Data = (int)(IR & SIXTEEN_BIT_MASK);
			else 
				Address = (int)(IR & SIXTEEN_BIT_MASK);
		case 2: // Unconditional Jump
			OC = (int)((IR & OPCODE_MASK) >> 24);
			Address = (int)(IR & TWENTYFOUR_BIT_MASK);
		case 3: // IO
			OC = (int)((IR & OPCODE_MASK) >> 24);
			SourceReg1 = (int)((IR & SREG1_MASK) >> 20);
			SourceReg2 = (int)((IR & SREG2_MASK) >> 16);
			Address = (int)(IR & SIXTEEN_BIT_MASK);
		}
	}
	
	private void Execute(){
		Driver.PCB.setState("RUNNING");
	    switch (OC) {
            case 0:
                DMA(OC);
                break;
            case 1:
                DMA(OC);
                break;
            case 2: // Store reg. into an address
                ST();
                break;
            case 3: // Loads address into a reg.
                LW();
                break;
            case 4: // Transfer one register into another
                MOV();
                break;
            case 5: // Add two S-regs and store in D-reg
                ADD();
                break;
            case 6: // Subtract two S-regs and store in D-reg
                SUB();
                break;
            case 7: // Multiply two S-regs and store in D-reg
                MUL();
                break;
            case 8: // Divide two S-regs and store in D-reg
                DIV();
                break;
            case 9: // Logical And of two S-regs and store in D-reg
                AND();
                break;
            case 10: // Logical OR of two S-regs and store in D-reg
                OR();
                break;
            case 11: // Transfer address/data to register
                MOVI();
                break;
            case 12: // Adds a data value to a register
                ADDI();
                break;
            case 13: // Multiplies a data value with the contents of a register
                MULI();
                break;
            case 14: // Divides the contents of a register by data value
                DIVI();
                break;
            case 15: // Loads a data/address into a register
                LDI();
                break;
            case 16: // Sets D-reg to 1 if(S-reg < B-reg); else 0
                SLT();
                break;
            case 17: // Sets D-reg to 1 if(S-reg < data); else 0
                SLTI();
                break;
            case 18: // End of program
                END = true;
                break;
            case 19: // Do nothing, move to next instruction
                break;
            case 20: // Jump to location
                JMP();
            case 21: // Branch to address if (B-reg = D-reg)
                BEQ();
                break;
            case 22: // Branch to address if (B-reg != D-reg)
                BNE();
                break;
            case 23: // Branch to address if (B-reg = 0)
                BEZ();
                break;
            case 24: // Branch to address if (B-reg != 0)
                BNZ();
                break;
            case 25: // Branch to address if (B-reg > 0)
                BGZ();
                break;
            case 26: // Branch to address if (B-reg < 0)
                BLZ();
                break;
            default: Driver.PCB.blocked.add(Driver.PCB.processes.firstElement());
                     while (!Driver.PCB.state.equals("RUNNING"))
                     {
                         wait++;
                     }

                System.out.println("Time spent waiting: "+ wait);
            wait = 0;
            Driver.PCB.setState("WAITING");
            break;

        }

		//Driver.PCB.addBlocked(Driver.PCB.processes.firstElement());
	    //Driver.PCB.setState("WAITING");
	   // Driver.PCB.waiting.add(Driver.PCB.processes.firstElement());
	}
	
	private void ST(){
		if (DestinationReg == 0)
			Driver.memory.writeRAM(Integer.toBinaryString(registers[BaseReg]), Address);
		else
			registers[DestinationReg] = registers[BaseReg];
	}
	
	private void LW(){
		if (Address != 0)
			registers[DestinationReg] = (int) Long.parseLong(Driver.memory.pullRAM(Address), 2);
		else 
			registers[DestinationReg] = registers[BaseReg];
	}
	
	private void MOV(){
		if (SourceReg1 != 0)
			registers[DestinationReg] = registers[SourceReg1];
		else if (SourceReg2 != 0)
			registers[DestinationReg] = registers[SourceReg2];
		else //Moving accumulator to destination
			registers[DestinationReg] = registers[0];
	}
	
	private void ADD(){
		registers[DestinationReg] = registers[SourceReg1] + registers[SourceReg2];
	}
	
	private void SUB(){
		registers[DestinationReg] = registers[SourceReg1] - registers[SourceReg2];
	}
	
	private void MUL(){
		registers[DestinationReg] = registers[SourceReg1] * registers[SourceReg2];
	}
	
	private void DIV(){
        if (registers[SourceReg2] != 0) {
            registers[DestinationReg] = registers[SourceReg1] / registers[SourceReg2];
        }
	}
	
	private void AND(){
		registers[DestinationReg] = registers[SourceReg1] & registers[SourceReg2];
	}
	
	private void OR(){
		registers[DestinationReg] = registers[SourceReg1] | registers[SourceReg2];
	}
	
	private void MOVI(){
		if (Address != 0)
			registers[DestinationReg] = (int) Long.parseLong(Driver.memory.pullRAM(Address), 2);
		else 
			registers[DestinationReg] = registers[BaseReg];
	}
	
	private void ADDI(){
		registers[DestinationReg] = registers[DestinationReg] + Data;
	}
	
	private void MULI(){
		registers[DestinationReg] = registers[DestinationReg] * Data;
	}
	
	private void DIVI(){
		registers[DestinationReg] = registers[DestinationReg] / Data;
	}
	
	private void LDI(){
		if (Data != 0)
			registers[DestinationReg] = Data;
		else 
			registers[DestinationReg] = (int) Long.parseLong(Driver.memory.pullRAM(Address), 2);
	}
	
	private void SLT(){
		if (registers[SourceReg1] < registers[SourceReg2])
			registers[DestinationReg] = 1;
		else 
			registers[DestinationReg] = 0;
	}
	
	private void SLTI(){
		if (registers[SourceReg1] < Data)
			registers[DestinationReg] = 1;
		else 
			registers[DestinationReg] = 0;
	}
	
	private void JMP(){
		
	}
	private void BEQ(){
		if (registers[BaseReg] == registers[DestinationReg])
			PC = Long.parseLong(Driver.memory.pullRAM(Address), 2); // branch to address
	}
	
	private void BNE(){
		if (registers[BaseReg] != registers[DestinationReg])
			PC = Long.parseLong(Driver.memory.pullRAM(Address), 2);
	}
	
	private void BEZ(){
		if (registers[BaseReg] == 0)
			PC = Long.parseLong(Driver.memory.pullRAM(Address), 2);
	}
	
	private void BNZ(){
		if (registers[BaseReg] != 0)
			PC = Long.parseLong(Driver.memory.pullRAM(Address), 2);
	}
	
	private void BGZ(){
		if (registers[BaseReg] > 0)
			PC = Long.parseLong(Driver.memory.pullRAM(Address), 2);
	}
	
	private void BLZ(){
		if (registers[BaseReg] < 0)
			PC = Long.parseLong(Driver.memory.pullRAM(Address), 2); 
	}
}

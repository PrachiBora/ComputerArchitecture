import java.util.Arrays;

class Instruction {

	String label;
	String opcode;
	String[] operands;
	int IF;
	int ID;
	int EX;
	int WB;
	int[] states;
	char RAW = 'N';
	char WAR = 'N';
	char WAW = 'N';
	char StructHazard;
	State s = State.FT;
	int cycle;

	public Instruction()
	{
		this.IF = 0;
		this.ID = 0;
		this.EX = 0;
		this.WB = 0;
		this.RAW = 'N';
		this.WAR = 'N';
		this.StructHazard = 'N';
		this.s = State.FT;
		
	}
	
	public Instruction(Instruction instr) {
		this.label = instr.label;
		this.opcode = instr.opcode;
		this.operands = instr.operands;
		this.IF = 0;
		this.ID = 0;
		this.EX = 0;
		this.WB = 0;
		this.RAW = 'N';
		this.WAR = 'N';
		this.StructHazard = 'N';
		this.s = State.FT;
		this.cycle = 1;
	}
	public String toString()
	{
		return (this.label + "\t" + this.opcode  + "\t" + Arrays.asList(operands) + "\t" + this.IF + "\t" + this.ID + "\t" + this.EX + "\t" + this.WB + "\t" + this.s);
	}		

	void fetch(){
	//	Global.PC += 1;
		Global.functionalUnitStatus.put("Fetch", true);
		if(this.cycle > 0)
			this.cycle -= 1;
		if (opcode == "HLT")
			Global.flag = false;
	}

	void decode(){
		Global.functionalUnitStatus.put("Decode", true);
		if(this.cycle > 0)
			this.cycle -= 1;
		
	}

	void writeBack(){
		Global.functionalUnitStatus.put("WriteBack", true);
		if(this.cycle > 0){
			this.cycle -= 1;
		}
	}

	State getNextIfFree(){
		String key = null;
		State next = this.s.getNext();
//		System.out.println(this.s + " " + next);
		
		if (next == null)
		{
			this.WB = Global.clockCycle;
//			System.out.println("in null");
			return null;
		}
		else if (next.equals(State.FT))
			key = "Fetch";
		else if (next.equals(State.IDT))
			key = "Decode";
		else if (next.equals(State.EXT))
			key = "IntegerUnit";
		else if (next.equals(State.WBT))
		{
//			System.out.print("in wB");
			key = "WriteBack";
		}
		if (this.cycle == 0  && !Global.functionalUnitStatus.get(key)){
			Global.functionalUnitStatus.put(key, true);
			if(this.s == State.FT)
				this.IF = Global.clockCycle;
			else if(this.s == State.IDT)
				this.ID = Global.clockCycle;
			else if(this.s == State.EXT)
				this.EX = Global.clockCycle;
			else if(this.s == State.WBT)
			{
				System.out.println("writing number of cycles to WB " + Global.clockCycle);
				this.WB = Global.clockCycle;
			}
				
			this.cycle = getCycles(next);
//			System.out.println("cycle is *************" + this.cycle);
			return next;
		}
		else
			return this.s;
		
	}
	
	int getCycles(State s){
		if(s.equals(State.FT))
			return 3;
		if (s.equals(State.EXT))
		{
			return 2;
		}
		else if (s.equals(State.WBT))
			return 1;
		else
			return 1;
	}
	
	Instruction fetchNext(Instruction currInstr)
	{

		if(currInstr == null)
			return Global.instruction.get(0);
		
		Instruction nextInstr = null;

		if(currInstr.opcode.equalsIgnoreCase("BNE"))
		{
			System.out.println("in BNE ");
			if(Global.RegisterValuePair.get(currInstr.operands[0]) == Global.RegisterValuePair.get(currInstr.operands[1]))
			{
				System.out.println("in BNE if ");
				if(Global.instruction.indexOf(currInstr)+1 == Global.instruction.size())
					return null;
				else
					return Global.instruction.get(Global.instruction.indexOf(currInstr)+1);
			}	
			else 
			{
				System.out.println("in BNE else ");
				System.out.println(Global.RegisterValuePair.get(currInstr.operands[0]) + " " + Global.RegisterValuePair.get(currInstr.operands[1]));
				for(Instruction inst : Global.instruction)
				{
					if(inst.label != null && inst.label.equals(currInstr.operands[currInstr.operands.length - 1]))
					{
						nextInstr = inst;
						return nextInstr;
					}
				}
			}
		}
		else if(currInstr.opcode.equalsIgnoreCase("BEQ"))
		{
			if(Global.RegisterValuePair.get(currInstr.operands[0]) == Global.RegisterValuePair.get(currInstr.operands[1]))
			{
				for(Instruction inst : Global.instruction)
				{
					if(inst.label != null && inst.label.equals(currInstr.operands[currInstr.operands.length - 1]))
					{
						nextInstr = inst;
						return nextInstr;
					}
				}
			}
			else
			{
				if(Global.instruction.indexOf(currInstr)+1 == Global.instruction.size())
					return null;
				else
					return Global.instruction.get(Global.instruction.indexOf(currInstr)+1);
			}
		}
		else if(currInstr.opcode.equalsIgnoreCase("J"))
		{
			for(Instruction inst : Global.instruction)
			{
				if(inst.label != null && (inst.label.equalsIgnoreCase(currInstr.operands[currInstr.operands.length - 1])))
				{
					nextInstr = inst;
					return nextInstr;
				}
			}

		}
		else
		{
			if((Global.instruction.indexOf(currInstr)+1) == Global.instruction.size())
				return null;
			else
				return Global.instruction.get(Global.instruction.indexOf(currInstr)+1);
		}
		return null;
	}

	void execute()
	{
		
//		Global.functionalUnitStatus.put("IntegerUnit", true);
		if(this.cycle > 0){
			System.out.println("%%%%%%%%%%%%%%%%%" + this.cycle);
			this.cycle -= 1;
		}	

		if(this.opcode.equalsIgnoreCase("DADD") || this.opcode.equalsIgnoreCase("ADD"))
		{		
			String reg2 = this.operands[2];
			String reg1 = this.operands[1];
			int reg1Value = Global.RegisterValuePair.get(reg1);
			int reg2Value = Global.RegisterValuePair.get(reg2);
			int destValue = reg1Value + reg2Value;
			String dest = this.operands[0];
			Global.RegisterValuePair.put(dest, destValue);
		}
		else if(this.opcode.equalsIgnoreCase("DADDI"))
		{ 
			String imm = this.operands[2];
			String reg = this.operands[1];
			int regValue = Global.RegisterValuePair.get(reg);
			int destValue = regValue + Integer.parseInt(imm);
			String dest = this.operands[0];
			Global.RegisterValuePair.put(dest, destValue);
		}
		else if(this.opcode.equalsIgnoreCase("DSUB") || this.opcode.equalsIgnoreCase("SUB"))
		{
			String reg2 = this.operands[2];
			String reg1 = this.operands[1];
			int reg1Value = Global.RegisterValuePair.get(reg1);
			int reg2Value = Global.RegisterValuePair.get(reg2);
			int destValue = reg1Value + reg2Value;
			String dest = this.operands[0];
			Global.RegisterValuePair.put(dest, destValue);
				}
		else if(this.opcode.equalsIgnoreCase("DSUBI"))
		{ 
			System.out.println("*****************in DSUBI************");
			String imm = this.operands[2];
			String reg = this.operands[1];
			int regValue = Global.RegisterValuePair.get(reg);
			int destValue = regValue - Integer.parseInt(imm);
			String dest = this.operands[0];
			Global.RegisterValuePair.put(dest, destValue);
		}
		else if(this.opcode.equalsIgnoreCase("ANDI"))
		{
			String imm = this.operands[2];
			String reg = this.operands[1];
			int regValue = Global.RegisterValuePair.get(reg);
			int destValue = regValue & Integer.parseInt(imm);
			String dest = this.operands[0];
			Global.RegisterValuePair.put(dest, destValue);
		}
		else if(this.opcode.equalsIgnoreCase("ORI"))
		{
			String imm = this.operands[2];
			String reg = this.operands[1];
			int regValue = Global.RegisterValuePair.get(reg);
			int destValue = regValue | Integer.parseInt(imm);
			String dest = this.operands[0];
			Global.RegisterValuePair.put(dest, destValue);
		}
	}
}

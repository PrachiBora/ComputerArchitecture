import java.util.Arrays;
import java.util.HashMap;

class Instruction {

	String label;
	String opcode;
	String[] operands;
	int IF;
	int ID;
	int EX;
	int WB;
	int[] states;
	int RAW;
	int WAR;
	int WAW;
	int StructHazard;
	int ClockCycle;
	//String state;
	State s = State.FT;
	int cycle;
	
	public String toString()
	{
		return (this.label + "\t" + this.opcode  + "\t" + Arrays.asList(operands) + "\t" + this.IF + "\t" + this.ID + "\t" + this.EX + "\t" + this.WB);
	}		

	void fetch(){
		Global.PC += 1;
		this.cycle = 5;
		Global.functionalUnitStatus.put("Fetch", true);
		if (opcode == "HLT")
				Global.flag = false;
	}
	
	void decode(){
		Global.functionalUnitStatus.put("Decode", true);
		this.cycle = 1;
	}
	
	void execute(){
		Global.functionalUnitStatus.put("IntegerUnit", true);
		this.cycle = 2;
	}
	
	void writeBack(){
		Global.functionalUnitStatus.put("WriteBack", true);
		this.cycle = 1;
	}
	
//	void fetchInstruction()
//	{
//		
//		Instruction prevInstr = null;
//		Instruction nextInstr = null;
//		for(int index = 0; index < Global.instruction.size() ; index ++)
//		{	
//			Instruction currInstr = Global.instruction.get(index);
//			
//			nextInstr = fetchNext(currInstr);
//			if(nextInstr != null)
//				index = Global.instruction.indexOf(nextInstr)-1;
//
//			if(prevInstr != null)
//				currInstr.ClockCycle = prevInstr.ID;
//			else
//				currInstr.ClockCycle = 1;
//
//			currInstr.IF = currInstr.ClockCycle ;
//			currInstr.ID = currInstr.ClockCycle + 5;
//			currInstr.ClockCycle += 5;
//			if(!(currInstr.opcode.equalsIgnoreCase("BNE")|| currInstr.opcode.equalsIgnoreCase("BEQ") || currInstr.opcode.equalsIgnoreCase("J")))
//			{
//				currInstr.EX = currInstr.ClockCycle + 1;
//				currInstr.ClockCycle += 1;
//				currInstr.WB = currInstr.ClockCycle + 6;
//				currInstr.ClockCycle += 6;
//			}
//			if ((index+1) == Global.instruction.size())
//				nextInstr = null;
//			else 
//			{
//				nextInstr = Global.instruction.get(index +1);
//				nextInstr.IF = currInstr.ClockCycle + 5;
//			}
//		
//			executeInstruction(currInstr);
//			prevInstr = currInstr;
//		}
//
//	}

	Instruction fetchNext(Instruction currInstr)
	{

		if(currInstr == null)
			return Global.instruction.get(0);
		Instruction nextInstr = null;

		if(currInstr.opcode.equalsIgnoreCase("BNE"))
		{
			if(Global.RegisterValuePair.get(currInstr.operands[0]) == Global.RegisterValuePair.get(currInstr.operands[1]))
			{
				if(Global.instruction.indexOf(currInstr)+1 == Global.instruction.size())
				{
					System.out.println("in if");
					return null;
				}
				else
					return Global.instruction.get(Global.instruction.indexOf(currInstr)+1);
			}	
			else 
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

	void testFetch()
	{
		Instruction nextInstr = null;
		nextInstr = fetchNext(Global.instruction.get(10));
		if(nextInstr == null)
			System.out.println("null");
		else
			System.out.println("next instr is " + nextInstr);

	}

	void executeInstruction(Instruction inst)
	{
		int i = Global.instruction.indexOf(inst);

		if(Global.instruction.get(i).opcode.equalsIgnoreCase("DADD") || Global.instruction.get(i).opcode.equalsIgnoreCase("ADD"))
		{
			if(!Global.functionalUnitStatus.get(Global.opcodeFunctionalUnit.get(Global.instruction.get(i).opcode)))
			{
				Global.functionalUnitStatus.put(Global.opcodeFunctionalUnit.get(Global.instruction.get(i).opcode),true);
				String reg2 = Global.instruction.get(i).operands[2];
				String reg1 = Global.instruction.get(i).operands[1];
				int reg1Value = Global.RegisterValuePair.get(reg1);
				int reg2Value = Global.RegisterValuePair.get(reg2);
				int destValue = reg1Value + reg2Value;
				String dest = Global.instruction.get(i).operands[0];
				Global.RegisterValuePair.put(dest, destValue);
				Global.functionalUnitStatus.put(Global.opcodeFunctionalUnit.get(Global.instruction.get(i).opcode),false);
			}
		}
		else if(Global.instruction.get(i).opcode.equalsIgnoreCase("DADDI"))
		{ 
			if(!Global.functionalUnitStatus.get(Global.opcodeFunctionalUnit.get(Global.instruction.get(i).opcode)))
			{

				Global.functionalUnitStatus.put(Global.opcodeFunctionalUnit.get(Global.instruction.get(i).opcode),true);
				String imm = Global.instruction.get(i).operands[2];
				String reg = Global.instruction.get(i).operands[1];
				int regValue = Global.RegisterValuePair.get(reg);
				int destValue = regValue + Integer.parseInt(imm);
				String dest = Global.instruction.get(i).operands[0];
				Global.RegisterValuePair.put(dest, destValue);
				Global.functionalUnitStatus.put(Global.opcodeFunctionalUnit.get(Global.instruction.get(i).opcode),false);
			}
		}
		else if(Global.instruction.get(i).opcode.equalsIgnoreCase("DSUB") || Global.instruction.get(i).opcode.equalsIgnoreCase("SUB"))
		{
			if(!Global.functionalUnitStatus.get(Global.opcodeFunctionalUnit.get(Global.instruction.get(i).opcode)))
			{
				Global.functionalUnitStatus.put(Global.opcodeFunctionalUnit.get(Global.instruction.get(i).opcode),true);
				String reg2 = Global.instruction.get(i).operands[2];
				String reg1 = Global.instruction.get(i).operands[1];
				int reg1Value = Global.RegisterValuePair.get(reg1);
				int reg2Value = Global.RegisterValuePair.get(reg2);
				int destValue = reg1Value + reg2Value;
				String dest = Global.instruction.get(i).operands[0];
				Global.RegisterValuePair.put(dest, destValue);
				Global.functionalUnitStatus.put(Global.opcodeFunctionalUnit.get(Global.instruction.get(i).opcode),false);
			}
		}
		else if(Global.instruction.get(i).opcode.equalsIgnoreCase("DSUBI"))
		{ 
			if(Global.functionalUnitStatus.get(Global.opcodeFunctionalUnit.get(Global.instruction.get(i).opcode))== false)
			{

				Global.functionalUnitStatus.put(Global.opcodeFunctionalUnit.get(Global.instruction.get(i).opcode),true);
				String imm = Global.instruction.get(i).operands[2];
				String reg = Global.instruction.get(i).operands[1];
				int regValue = Global.RegisterValuePair.get(reg);
				int destValue = regValue - Integer.parseInt(imm);
				String dest = Global.instruction.get(i).operands[0];
				Global.RegisterValuePair.put(dest, destValue);
				Global.functionalUnitStatus.put(Global.opcodeFunctionalUnit.get(Global.instruction.get(i).opcode),false);
			}
		}
		else if(Global.instruction.get(i).opcode.equalsIgnoreCase("ANDI"))
		{
			if(!Global.functionalUnitStatus.get(Global.opcodeFunctionalUnit.get(Global.instruction.get(i).opcode)))
			{
				Global.functionalUnitStatus.put(Global.opcodeFunctionalUnit.get(Global.instruction.get(i).opcode),true);
				String imm = Global.instruction.get(i).operands[2];
				String reg = Global.instruction.get(i).operands[1];
				int regValue = Global.RegisterValuePair.get(reg);
				int destValue = regValue & Integer.parseInt(imm);
				String dest = Global.instruction.get(i).operands[0];
				Global.RegisterValuePair.put(dest, destValue);
				Global.functionalUnitStatus.put(Global.opcodeFunctionalUnit.get(Global.instruction.get(i).opcode),false);
			}

		}
		else if(Global.instruction.get(i).opcode.equalsIgnoreCase("ORI"))
		{
			if(!Global.functionalUnitStatus.get(Global.opcodeFunctionalUnit.get(Global.instruction.get(i).opcode)))
			{
				Global.functionalUnitStatus.put(Global.opcodeFunctionalUnit.get(Global.instruction.get(i).opcode),true);
				String imm = Global.instruction.get(i).operands[2];
				String reg = Global.instruction.get(i).operands[1];
				int regValue = Global.RegisterValuePair.get(reg);
				int destValue = regValue | Integer.parseInt(imm);
				String dest = Global.instruction.get(i).operands[0];
				Global.RegisterValuePair.put(dest, destValue);
			}

		}
	}
}

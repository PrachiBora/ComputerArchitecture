import java.util.Collections;
import java.util.Comparator;

class InstructionQueue {

	Instruction iUse = new Instruction();
	void parseAtEachClockCycle()
	{
		boolean isTermInst = false;
		Instruction ins = Global.instruction.get(0);
		Instruction copyIns = copyInstruction(ins);
		Global.queue.add(copyIns);
		Global.functionalUnitStatus.put("Fetch",true);
		while(!Global.queue.isEmpty())
		{
			int size = Global.queue.size();
			while(size > 0)
			{
				Instruction inst = Global.queue.remove();
				
				parseInstruction(inst);

				if(inst.cycle == 0){
					if(inst.s == State.FT)
						Global.functionalUnitStatus.put("Fetch",false);
					else if(inst.s == State.IDT){
						Global.functionalUnitStatus.put("Decode",false);
						if (inst.opcode.equalsIgnoreCase("HLT") || inst.opcode.equalsIgnoreCase("BEQ") ||inst.opcode.equalsIgnoreCase("BNE") || inst.opcode.equalsIgnoreCase("J")){
							inst.ID = Global.clockCycle;
							isTermInst = true;
							inst.s = null;
						}
					}	
						
					else if(inst.s == State.EXT)
					{	
						Global.functionalUnitStatus.put(Global.opcodeFunctionalUnit.get(inst.opcode),false);
					}
					else if(inst.s == State.WBT)
						Global.functionalUnitStatus.put("WriteBack",false);

					if(!isTermInst)
						inst.s = inst.getNextIfFree();
					isTermInst = false;
					
					
					
					
				}
				if(inst.s == null)
					Global.result.add(inst);
				else
					Global.queue.add(inst);
				size--;
			}
			if(!( Global.functionalUnitStatus.get("Fetch"))) 
			{
				ins = iUse.fetchNext(ins);
				copyIns = copyInstruction(ins);
				if(copyIns != null)
				{
				//	System.out.println(copyIns.opcode + " " + copyIns.operands[0] + " " + Global.clockCycle);
					Global.queue.add(copyIns);
				}
			}
			Global.clockCycle += 1;
		}

		Collections.sort(Global.result,new Comparator<Instruction>()
				{
			@Override public int compare(Instruction i1,Instruction i2)
			{
				return i1.IF - i2.IF;
			}
				});
		System.out.println("Label\topcode\tOperands\tIF\tID\tEX\tWB" );
		for(Instruction it : Global.result)
			System.out.println(it);	
	}

	Instruction copyInstruction(Instruction curr)
	{
		if(curr != null)
		{
			Instruction copyInstr = new Instruction(curr);
			return copyInstr;
		}
		return null;
	}
	void parseInstruction(Instruction ins)
	{
		//		System.out.println("insss->" + ins.opcode + " " + ins.operands[0] + " " + ins.s + " " + Global.clockCycle);
		if(ins != null)
		{
			if(ins.s == State.FT)
			{
//				if(!Global.functionalUnitStatus.get("Fetch"))
					ins.fetch();
			}
			else if(ins.s == State.IDT)
			{

//				if(!Global.functionalUnitStatus.get("Decode"))
				{
//					ins.IF = Global.clockCycle;
					ins.decode();
				}
			}
			else if(ins.s == State.EXT)
			{
//				if(!Global.functionalUnitStatus.get("IntegerUnit"))
//				{
//					ins.ID = Global.clockCycle;
					ins.execute();
//				}

			}
			else if(ins.s == State.WBT)
			{
				
//				if(!Global.functionalUnitStatus.get("WriteBack"))
				{
//					System.out.println("&&&&&&&&&&&&&");
//					ins.EX = Global.clockCycle;
					ins.writeBack();
				}
					
			}
			else {
//				ins.WB = Global.clockCycle;

			}
		}
	}
}
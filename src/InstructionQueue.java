class InstructionQueue {

	Instruction iUse = new Instruction();
	void parseAtEachClockCycle()
	{
		Instruction ins = null;
		while(Global.flag)
		{
			int size = Global.queue.size();
			while(size > 0)
			{
				Instruction inst = Global.queue.remove();
				inst.cycle -= 1;
				if(inst.cycle == 0){
					
					if(inst.s == State.FT)
						Global.functionalUnitStatus.put("Fetch",false);
					else if(inst.s == State.IDT)
						Global.functionalUnitStatus.put("Decode",false);
					else if(inst.s == State.EXT)
						Global.functionalUnitStatus.put("IntegerUnit",false);
					else if(inst.s == State.WBT){
						Global.functionalUnitStatus.put("WriteBack",false);
//						System.out.println(inst.opcode);
					}
					inst.s = inst.getNextIfFree();
					parseInstruction(inst);
				}
				if(inst.s == null)
				{
//					System.out.println("insss" + inst.opcode + inst.operands[0] + " "+ Global.clockCycle);
					Global.result.add(inst);
				}
				else
					Global.queue.add(inst);
				size--;

			}
			if(!( Global.functionalUnitStatus.get("Fetch"))) 
			{
				ins = iUse.fetchNext(ins);
				Instruction copyIns = copyInstruction(ins);
				parseInstruction(copyIns);

				if(copyIns == null)
					break;
				else{
//					System.out.println(copyIns.opcode + " " + copyIns.operands[0] + " " + Global.clockCycle);
//					copyIns.fetch();
					Global.queue.add(copyIns);
				}
			}
			Global.clockCycle += 1;
		}
		
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
				if(!Global.functionalUnitStatus.get("Fetch"))
					ins.fetch();
			}
			else if(ins.s == State.IDT)
			{
				ins.IF = Global.clockCycle;
				
				if(!Global.functionalUnitStatus.get("Decode"))
					ins.decode();
			}
			else if(ins.s == State.EXT)
			{
				ins.ID = Global.clockCycle;
				
				if(!Global.functionalUnitStatus.get("IntegerUnit"))
					ins.execute();
			}
			else if(ins.s == State.WBT)
			{
				ins.EX = Global.clockCycle;
				
				if(!Global.functionalUnitStatus.get("WriteBack"))
					ins.writeBack();
			}
			else {
				ins.WB = Global.clockCycle;
				
			}
		}
	}
}
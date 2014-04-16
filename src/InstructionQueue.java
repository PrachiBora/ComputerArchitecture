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
					else if(inst.s == State.WBT)
						Global.functionalUnitStatus.put("WriteBack",false);
					inst.s = inst.s.getNext();
					parseInstruction(inst);	
				}
				if(inst.s == null)
					Global.queue.remove(inst);
				else
					Global.queue.add(inst);
				size--;

			}
			if (Global.PC == Global.instruction.size())
				break;
			else if(!( Global.functionalUnitStatus.get("Fetch"))) 
			{
				ins = Global.instruction.get(Global.PC);
				parseInstruction(ins);
				Global.queue.add(ins);
			}
			Global.clockCycle += 1;
		}
		System.out.println("Label\topcode\tOperands\tIF\tID\tEX\tWB" );
		for(Instruction it : Global.instruction)
			System.out.println(it);	
	}

	void parseInstruction(Instruction ins)
	{
		if(ins != null)
		{
			if(ins.s == State.FT)
			{
				ins.IF = Global.clockCycle;
				if(!Global.functionalUnitStatus.get("Fetch"))
					ins.fetch();
			}
			else if(ins.s == State.IDT)
			{
				ins.ID = Global.clockCycle;
				if(!Global.functionalUnitStatus.get("Decode"))
					ins.decode();
			}
			else if(ins.s == State.EXT)
			{
				ins.EX = Global.clockCycle;
				if(!Global.functionalUnitStatus.get("IntegerUnit"))
					ins.execute();
			}
			else
			{
				ins.WB = Global.clockCycle;
				if(!Global.functionalUnitStatus.get("WriteBack"))
					ins.writeBack();
			}
		}
	}
}
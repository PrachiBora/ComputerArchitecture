class InstructionQueue {
	
	Instruction i = new Instruction();

	void parseAtEachClockCycle()
	{
		Instruction ins = null;
		while(Global.flag)
		{

			int size = Global.queue.size();
			while(size > 0)
			{
				System.out.println(Global.queue.peek().opcode);
				Instruction inst = Global.queue.remove();
				inst.cycle -= 1;
				if(inst.cycle == 0){
					inst.s = inst.s.getNext();
				}
				//parseInstruction(inst);
				Global.queue.add(inst);
				System.out.println(inst.s);
				
				size--;
				
			}
			
			System.out.println("CLOCK CYCLE:" + Global.clockCycle);
			System.out.println(Global.PC);
			ins = Global.instruction.get(Global.PC);
			Global.queue.add(ins);
			ins.fetch();
			Global.clockCycle += 1;
		}
	}
	
	void parseInstruction(Instruction ins)
	{
		if(ins != null)
		{
			if(ins.s == State.FT)
			{
				System.out.println("calling fetch");
				ins.IF = Global.clockCycle;
				ins.fetch();
			}
			else if(ins.s == State.IDT)
			{
				System.out.println("calling decode");
				ins.ID = Global.clockCycle;
				ins.decode();
			}
			else if(ins.s == State.EXT)
			{
				System.out.println("calling execute");
				ins.EX = Global.clockCycle;
				ins.execute();
			}
			else
			{
				System.out.println("calling writeback");
				ins.WB = Global.clockCycle;
				ins.writeBack();
			}
		}
	}
}
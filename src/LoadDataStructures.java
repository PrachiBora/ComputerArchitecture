import java.util.Iterator;
import java.util.Map;
import java.util.Set;

class LoadDataStructures {
	
	void assignRegisterUnitStatus()
	{
		for(int i = 0; i < 32; i++ )
		{
			String key = "R" + String.valueOf(i);
			Register r = new Register();
			r.reg = key;
			r.readBusy = false;
			r.writeBusy = false;
			r.readInstr = null;
			r.writeInstr = null;
		}
	}
	void displayRegisterUnitStatus()
	{
//		Set<String> keys = Global.regUnitStatus.keySet();
		//for(String k: keys)
	//		System.out.println("Value of " + k +" is: "+ Global.regUnitStatus.get(k));
	}
	void assignFunctionalUnitStatus()
	{
		Iterator<Map.Entry<String,String>> itr=  Global.opcodeFunctionalUnit.entrySet().iterator();
		while(itr.hasNext())
		{
			String FU = itr.next().getValue();
			Global.functionalUnitStatus.put(FU,false);
		}
		Global.functionalUnitStatus.put("Fetch", false);
		Global.functionalUnitStatus.put("Decode", false);
		Global.functionalUnitStatus.put("Execute",false);
		Global.functionalUnitStatus.put("WriteBack",false);
	}
	
	void displayFunctionalUnitStatus()
	{

		Set<String> keys = Global.functionalUnitStatus.keySet();
		for(String k: keys)
			System.out.println("Value of " + k +" is: "+ Global.functionalUnitStatus.get(k));

	}

	void displayFunctionalUnitsCycle()
	{
		Set<String> keys = Global.functionalUnitCycle.keySet();
		for(String k: keys)
			System.out.println("Value of " + k +" is: "+ Global.functionalUnitCycle.get(k));
	}
	void loadStateCycle()
	{
		Global.stateCycle.put("IF", 5);
	}
	
	void functionalUnitForOpcode(String opcode)
	{
		if(opcode.equalsIgnoreCase("LW") || opcode.equalsIgnoreCase("SW") || opcode.equalsIgnoreCase("L.D") || opcode.equalsIgnoreCase("S.D") || opcode.equalsIgnoreCase("DADD") || opcode.equalsIgnoreCase("DADDI") || opcode.equalsIgnoreCase("DSUB") || opcode.equalsIgnoreCase("DSUBI") || opcode.equalsIgnoreCase("AND")|| opcode.equalsIgnoreCase("ANDI") || opcode.equalsIgnoreCase("OR") || opcode.equalsIgnoreCase("ORI"))
			Global.opcodeFunctionalUnit.put(opcode,"IntegerUnit");
		else if(opcode.equalsIgnoreCase("ADD.D") || opcode.equalsIgnoreCase("SUB.D"))
			Global.opcodeFunctionalUnit.put(opcode,"FPAdder");
		else if(opcode.equalsIgnoreCase("MUL.D"))
			Global.opcodeFunctionalUnit.put(opcode,"FPMult");
		else if(opcode.equalsIgnoreCase("DIV.D"))
			Global.opcodeFunctionalUnit.put(opcode,"FPDivision");
	}

	void displayOpcodeFunctionalUnit()
	{
		Set<String> keys = Global.opcodeFunctionalUnit.keySet();
		for(String k: keys)
			System.out.println("Value of " + k +" is: "+ Global.opcodeFunctionalUnit.get(k));
	}
}

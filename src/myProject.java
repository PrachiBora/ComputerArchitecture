public class myProject
{
	public static void main(String[] args)
	{
		if(args.length != 2)
			System.out.println("Insufficient arguments");
		else	
		{	
			ParseInputFile pif = new ParseInputFile();
			LoadDataStructures ld = new LoadDataStructures();
			Instruction in = new Instruction();
			InstructionQueue iq = new InstructionQueue();
			
			pif.ReadInputFile(args[0]);
			pif.loadRegisters(args[1]);

			System.out.println("The functional units are-");
			System.out.println("*****************************");
			ld.displayOpcodeFunctionalUnit();
			System.out.println("*****************************");

			System.out.println("The register values are-");
			System.out.println("*****************************");
			pif.displayRegisterValuePair();
			System.out.println("*****************************");

			ld.assignFunctionalUnitStatus();
			ld.assignRegisterUnitStatus();
//			in.fetchInstruction();
//			System.out.println("Label\topcode\tOperands\tIF\tID\tEX\tWB" );
//			for(Instruction inst : Global.instruction)
//				System.out.println(inst);	

			System.out.println("The functional units status are-");
			System.out.println("*****************************");
			ld.displayFunctionalUnitStatus();
			System.out.println("*****************************");

			System.out.println("The register values are-");
			System.out.println("*****************************");
			pif.displayRegisterValuePair();
			System.out.println("*****************************");

//			System.out.println("The register unit status are-");
//			System.out.println("*****************************");
//			ld.displayRegisterUnitStatus();
//			System.out.println("*****************************");

			iq.parseAtEachClockCycle();
		}
	}
}

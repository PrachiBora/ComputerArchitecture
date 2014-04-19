public class myProject
{
	public static void main(String[] args)
	{
		ParseInputFile pif = new ParseInputFile();
		LoadDataStructures ld = new LoadDataStructures();
		InstructionQueue iq = new InstructionQueue();

		if(args.length != 3)
			System.out.println("Insufficient arguments");
		else	
		{	
			pif.ReadInputFile(args[0]);
			pif.loadRegisters(args[1]);
			pif.readConfig(args[2]);
//			System.out.println("The functional units are-");
//			System.out.println("*****************************");
//			ld.displayOpcodeFunctionalUnit();
//			System.out.println("*****************************");
//
//			System.out.println("The register values are-");
//			System.out.println("*****************************");
//			pif.displayRegisterValuePair();
//			System.out.println("*****************************");

			ld.assignFunctionalUnitStatus();
			ld.assignRegisterUnitStatus();

//			System.out.println("The functional units cycles are-");
//			System.out.println("*****************************");
//			ld.displayFunctionalUnitsCycle();
//			System.out.println("*****************************");
//
//			
//			System.out.println("The functional units status are-");
//			System.out.println("*****************************");
//			ld.displayFunctionalUnitStatus();
//			System.out.println("*****************************");
//
			iq.parseAtEachClockCycle();
//			
//			System.out.println("The register values are-");
//			System.out.println("*****************************");
//			pif.displayRegisterValuePair();
//			System.out.println("*****************************");


		}
	}
}

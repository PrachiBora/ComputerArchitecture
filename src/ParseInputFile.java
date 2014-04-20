import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Set;

class ParseInputFile
{
	LoadDataStructures lds = new LoadDataStructures();
	DecodeInstruction di = new DecodeInstruction();

	BufferedReader br = null;

	void ReadInputFile(String instrFile)
	{
		try{

			String sCurrentLine , temp;
			br = new BufferedReader(new FileReader(instrFile));
			while ((sCurrentLine = br.readLine()) != null)
			{
				Instruction ist = new Instruction();
				if(sCurrentLine.contains(":"))
				{
					ist.label = sCurrentLine.substring(0, sCurrentLine.indexOf(":"));
					temp = sCurrentLine.substring(sCurrentLine.indexOf(":")+ 1,sCurrentLine.length());
				}
				else 
				{
					ist.label = null;
					temp = sCurrentLine;
				}
				
				String[] tempArray = temp.split("\\s+");
				ist.opcode = tempArray[0];
				if(tempArray.length > 1)
				{
					String[] operands = tempArray[1].split(",");
//					String[] processOpr;
//					for (int i=0; i < operands.length; i++){
//						processOpr[i] = 
//					}
					ist.operands = operands;
				}
				lds.functionalUnitForOpcode(ist.opcode);
				Global.instruction.add(ist);
			}
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
				
////				sCurrentLine = sCurrentLine.trim();
//				if(sCurrentLine != null)
//				{
//					String [] line = sCurrentLine.split("\\s");
//					ist.opcode = line[0].trim();
//					if(ist.opcode.contains(":"))
//					{
//						String[] opcodes = ist.opcode.split(":");
//						ist.label = opcodes[0];
//						ist.opcode = opcodes[1];
//
//					}
//					if(line.length > 1)
//					{
//						String operands = line[1];
//						if(operands != null)
//						{
//							String[] operand = operands.split(",");
//							ist.operands = operand;
//						}
//					}
//					else 
//						ist.operands = null;
//					lds.functionalUnitForOpcode(ist.opcode);
//					Global.instruction.add(ist);
//				}
//			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null)br.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}

	void readConfig(String configFile)
	{
		BufferedReader b = null;
		try
		{
			String line;
			b = new BufferedReader(new FileReader(configFile));
			while((line = b.readLine()) != null)
			{
				if(line.equals(null))
					break;
				String[] lines = line.split(":");
				Global.functionalUnitStatus.put(lines[0],false);
				String[] line2 = lines[1].split(",");
				Integer cycle = Integer.parseInt(line2[0].trim());
				System.out.println(lines[0] + " " + cycle);
				Global.functionalUnitCycle.put(lines[0],cycle);
			}
			Global.functionalUnitCycle.put("IntegerUnit", 2);
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				if(b != null)
					b.close();
			}
			catch(IOException ex)
			{
				ex.printStackTrace();
			}
		}
	}
	void loadRegisters(String regFile)
	{  
		BufferedReader b = null;
		try
		{
			String register;
			b = new BufferedReader(new FileReader(regFile));
			int i = 0;
			while((register = b.readLine()) != null)
			{
				String key = "R";
				if(register.equals(null))
					break;
				key += i;
				int val = Integer.parseInt(register, 2);
				Global.RegisterValuePair.put(key,val);
				i++;
				key = null;
			}
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				if(b != null)
					b.close();
			}
			catch(IOException ex)
			{
				ex.printStackTrace();
			}
		}
	}

	void displayRegisterValuePair()
	{
		Set<String> keys = Global.RegisterValuePair.keySet();
		for(String k: keys)
			System.out.println("Value of " + k +" is: "+ Global.RegisterValuePair.get(k));

	}
}
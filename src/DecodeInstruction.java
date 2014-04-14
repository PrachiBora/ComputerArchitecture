

class DecodeInstruction
{    

     void decodeInstruction(String opcode, String[] operands,int id)
    {
    String op = Global.opcodeFunctionalUnit.get(opcode);
	
	if(operands.length > 2)
	    {
		String operand1 = operands[1];
		String operand2 = operands[2];
		if(opcode.contains(".D"))
		    {
			String oper1 = operand1.replaceAll("[^0-9]","");
			String oper2 = operand2.replaceAll("[^0-9]","");	
		    }
	    }
    }
}

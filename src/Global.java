import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Queue;

public class Global
{
	static HashMap<String,Integer> RegisterValuePair = new HashMap<String,Integer>();
	static HashMap<String,String> opcodeFunctionalUnit = new HashMap<String,String>();
	static HashMap<String,Boolean> functionalUnitStatus = new HashMap<String,Boolean>();
	static ArrayList<Instruction> instruction = new ArrayList<Instruction>();
	static ArrayList<Register> register = new ArrayList<Register>();
	static Queue<Instruction> queue = new ArrayDeque<Instruction>();
	static int clockCycle = 1;
	static HashMap<String,Integer> stateCycle = new HashMap<String,Integer>();
	static int PC;
	static boolean flag = true;
	static ArrayList<Instruction> result = new ArrayList<Instruction>();
	static HashMap<String,Integer> functionalUnitCycle = new HashMap<String,Integer>(); 
	
}

enum State{
	FT, IDT, EXT, WBT;
	
	 public State getNext() {
	     return this.ordinal() < State.values().length - 1
	         ? State.values()[this.ordinal() + 1]
	         : null;
	   }	
};
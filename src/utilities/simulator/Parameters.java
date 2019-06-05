package utilities.simulator;

import javax.swing.JOptionPane;

public class Parameters {
	public enum Operation{NOTHING(0), DELAY(1), DUPLICATE(2),  LOST(3);
		
						private int id;
						
						private Operation(int id) {
							this.id = id;
						}
						
						public int getID() {
							return id;
						}
	};

	 
	private int from;
	private int to;
	private Operation operation;
	
	public Parameters() {
	
	}
	
	public void getInfo() {	
		operation = queryForOperation();
		from = queryForPackets(true);
		to = queryForPackets(false);
	}

	
	private Operation queryForOperation() {
		String stringTemp = JOptionPane.showInputDialog(null, "Enter Operation (Nothing, Delay, Duplicate, Lost)", "Simulator", JOptionPane.DEFAULT_OPTION);
		
		return Operation.valueOf(stringTemp.toUpperCase());
	}



	private int queryForPackets(boolean isFrom) {
		String auxiliar = (isFrom) ? "From: " : "To: ";
		
		String stringTemp = JOptionPane.showInputDialog(null, auxiliar, "Simulator", JOptionPane.DEFAULT_OPTION);
		
		int value = Integer.parseInt(stringTemp);
		
		//cannot lose the very first packet from client size only
		//if we do it would be like if the client did not sent anything in the first instance;
		//only server CAN miss or delay the response to the very first packet
		//if(isFrom && value == 0 && who.getID() == 2) {
			//System.out.println("warning, loosing packet in server side w");
			//value++; 
		//}
		
		return value;
	}
	

	public int getFrom() {		
		return from;
	}

	public int getTo() {
		return to;
	}



	public String getOperationName() {
		return operation.toString();
	}
	
	public int getOperationID() {
		return operation.getID();
	}
	

}
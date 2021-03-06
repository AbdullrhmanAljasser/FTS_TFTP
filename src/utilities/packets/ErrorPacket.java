package utilities.packets;
import utilities.ArrayUtil;
import utilities.BlockNum;

public class ErrorPacket extends Packet{
	
	private BlockNum errorCode;
	private String msg;


	public ErrorPacket(int errorCode, String msg) {
		this.errorCode = new BlockNum(errorCode);
		this.msg = msg;
		this.setPacket(constructPacket());
	}
	
	/*
	 * public ErrorPacket(byte[] packet) { this.setPacket(packet); errorCode =
	 * this.extractErrorCode(packet); msg = this.extractMSG(packet); }
	 */
	
	public ErrorPacket(byte[] array, int length) {
		byte[] packet = ArrayUtil.subArray(array, 0, length);
		this.setPacket(packet);
		//errorCode = new BlockNum(this.extractErrorCode(packet));
		errorCode = new BlockNum(ArrayUtil.subArray(packet, 2, 4));
		msg = this.extractMSG(packet);
	}
	
	public BlockNum getErrorCode() {
		return errorCode;
	}
	
	public int getIntBN() {
		return errorCode.getInt();
	}
	
	public byte[] getBytesBN() {
		return errorCode.getByte();				
	}

	public String getMsg() {
		return msg;
	}
	
	public byte[]  constructPacket() {	
		byte[] id = new byte[2];
		byte[] end = {0};
		byte[] errCodeArray = errorCode.getByte();
		
		id[0] = 0;
		id[1] = 5;
		return  ArrayUtil.makeSimpleArray(id, errCodeArray, msg.getBytes(), end);
	}
	
	private int extractErrorCode(byte[] packet) {
		byte[] errCodeArray =  ArrayUtil.subArray(packet, 2, 4);
		
		return errCodeArray[0] * 128 + errCodeArray[1];
	}
	
	private String extractMSG(byte[] packet) {
		String msg = "";
		
		if(packet.length > 4) {
			byte[] msgArray =  ArrayUtil.subArray(packet, 4, packet.length - 1);
			
			for(byte b : msgArray) msg += (char)b;
		}
		return msg;
	}

}
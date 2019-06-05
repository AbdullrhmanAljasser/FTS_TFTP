package utilities.client;

import utilities.FILEUtil;
import utilities.packets.*;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.Arrays;

import program.*;

public class ClientRR extends Client {

	RequestPacket requestPacket;

	public ClientRR(RequestPacket requestPacket) {
		this.requestPacket = requestPacket;
		transfer();
	}

	private void transfer() {
		this.requestPacket.setDatagramPacket(serverAddress, serverPort);

		try {
			sendReceiveSocket.send( this.requestPacket.getDatagramPacket());
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}

		boolean is512 = true;
		byte[][] data = new byte[1024][];
		int blockNumber = 0;
		int tNum = 0;
		
		while (is512) {

			DatagramPacket dgp = new DatagramPacket(new byte[MAX_CAPACITY], MAX_CAPACITY);

			try {
				sendReceiveSocket.setSoTimeout(500);
				sendReceiveSocket.receive(dgp);
				DataPacket response = null;
				try {
					response = new DataPacket(dgp.getData(), dgp.getLength());
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				ACKPacket ack = new ACKPacket(response.getIntBN());
				ack.setDatagramPacket(dgp.getAddress(), dgp.getPort()); 
				 
				
				sendReceiveSocket.send(ack.getDatagramPacket());
				//TFTPUtil.send(sendReceiveSocket, ack.getDatagramPacket(), "Sending ACK # " + response.getIntBN());

				if (response.getIntBN() == blockNumber) {

					data[blockNumber++] = Arrays.copyOfRange(dgp.getData(), 4, dgp.getLength());
					if (dgp.getLength() < 512) {
						is512 = false;
						System.out.println("FINISHED Reading...");
					}
				}
				
				tNum = 0;
			} catch (SocketTimeoutException e1) {
				System.out.println("DataPacket wait timed-out... retrying");
				tNum++;
				if(tNum > 50) {
					break;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
		
		data = Arrays.copyOfRange(data, 0, blockNumber);

		try {
			FILEUtil file = new FILEUtil(data,PATH + this.requestPacket.getFilename(),true);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
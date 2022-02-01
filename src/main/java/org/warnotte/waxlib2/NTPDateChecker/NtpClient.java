package org.warnotte.waxlib2.NTPDateChecker;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Date;

/**
 * NtpClient - an NTP client for Java.  This program connects to an NTP server
 * and prints the response to the console.
 * 
 * The local clock offset calculation is implemented according to the SNTP
 * algorithm specified in RFC 2030.  
 * 
 * Note that on windows platforms, the curent time-of-day timestamp is limited
 * to an resolution of 10ms and adversely affects the accuracy of the results.
 * 
 * 
 * This code is copyright (c) Adam Buckley 2004
 *
 * This program is free software; you can redistribute it and/or modify it 
 * under the terms of the GNU General Public License as published by the Free 
 * Software Foundation; either version 2 of the License, or (at your option) 
 * any later version.  A HTML version of the GNU General Public License can be
 * seen at http://www.gnu.org/licenses/gpl.html
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT 
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or 
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for 
 * more details.
 *  
 * @author Adam Buckley
 */
public class NtpClient
{
	public static void main(String[] args) throws IOException, NoNTPHostAnswering
	{
		Date serverTime = getServerTime();
		System.err.println("--> "+serverTime);
	}
	
	public static Date getServerTime() throws NoNTPHostAnswering
	{
		
		String serverNames[]=
		{
			"0.be.pool.ntp.org",
			"0.fr.pool.ntp.org",
			"ntp.accelance.net",
			"time.nist.gov",
		};
		
		for (int i = 0; i < serverNames.length; i++)
		{
			String serverUrl = serverNames[i];
			try
			{
				return getServerTime(serverUrl);
			} catch (IOException e)
			{
				System.err.println("Host "+serverUrl+" not answering");
			}
		}
	
		throw new NoNTPHostAnswering();
	}

	/**
	 * @param serverName
	 * @return 
	 * @throws SocketException
	 * @throws UnknownHostException
	 * @throws IOException
	 */
	private static Date getServerTime(String serverName) throws SocketException, UnknownHostException, IOException
	{
		// Send request
		DatagramSocket socket = new DatagramSocket();
		InetAddress address = InetAddress.getByName(serverName);
		byte[] buf = new NtpMessage().toByteArray();
		DatagramPacket packet = new DatagramPacket(buf, buf.length, address, 123);
		
		// Set the transmit timestamp *just* before sending the packet
		// ToDo: Does this actually improve performance or not?
		NtpMessage.encodeTimestamp(packet.getData(), 40,
			(System.currentTimeMillis()/1000.0) + 2208988800.0);
		
		socket.send(packet);
		
		
		// Get response
	//	System.out.println("NTP request sent, waiting for response...\n");
		packet = new DatagramPacket(buf, buf.length);
		socket.receive(packet);
		
		// Immediately record the incoming timestamp
/*		double destinationTimestamp =
			(System.currentTimeMillis()/1000.0) + 2208988800.0;
		
*/		
		// Process response
		NtpMessage msg = new NtpMessage(packet.getData());
		
/*		double roundTripDelay = (destinationTimestamp-msg.originateTimestamp) -
			(msg.receiveTimestamp-msg.transmitTimestamp);
			
		double localClockOffset =
			((msg.receiveTimestamp - msg.originateTimestamp) +
			(msg.transmitTimestamp - destinationTimestamp)) / 2;
	*/	
		
		// Display response
	//	System.out.println("NTP server: " + serverName);
	//	System.out.println(msg.toString());
		
	//	System.out.println("Dest. timestamp:     " +
	//			NtpMessage.timestampToString(destinationTimestamp));
		
	//	System.out.println("Round-trip delay: " +
	//		new DecimalFormat("0.00").format(roundTripDelay*1000) + " ms");
		
	//	System.out.println("Local clock offset: " +
	//		new DecimalFormat("0.00").format(localClockOffset*1000) + " ms");
		
		socket.close();
		
		Date serverTime = NtpMessage.timestampToDate(msg.receiveTimestamp);
	//	Date localTime = NtpMessage.timestampToDate(msg.originateTimestamp);
		
	//	System.err.println("Local == "+localTime);
	//	System.err.println("Server == "+serverTime);
		
		return serverTime;
	}
	
}
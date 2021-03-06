//package pds;
import java.io.*;
import java.util.*;
import javax.comm.*;


public class SimpleRead  implements Runnable, SerialPortEventListener {
    static CommPortIdentifier portId;
    @SuppressWarnings("unchecked")
	static Enumeration portList;

    InputStream inputStream;
    SerialPort serialPort;
    Thread readThread;
	

    public static void main(String[] args) {
        portList = CommPortIdentifier.getPortIdentifiers();

        while (portList.hasMoreElements()) {
            portId = (CommPortIdentifier) portList.nextElement();
            if (portId.getPortType() == CommPortIdentifier.PORT_SERIAL) {
                 if (portId.getName().equals("COM1")) {
			//                if (portId.getName().equals("/dev/term/a")) {
                    new SimpleRead();
                }
            }
        }
    }

    public SimpleRead() {

       
        try {
            serialPort = (SerialPort) portId.open("SimpleReadApp", 2000);
        } catch (PortInUseException e) {System.out.println(e);}
        
        try {
            inputStream = serialPort.getInputStream();
        } catch (IOException e) {System.out.println(e);}
        
	try {
            serialPort.addEventListener(this);
	} catch (TooManyListenersException e) {System.out.println(e);}
	
        serialPort.notifyOnDataAvailable(true);
       
        try {
            serialPort.setSerialPortParams(9600,
                SerialPort.DATABITS_8,
                SerialPort.STOPBITS_1,
                SerialPort.PARITY_NONE);
        } catch (UnsupportedCommOperationException e) {System.out.println(e);}
        readThread = new Thread(this);
        readThread.start();
    }

    public void run() {
        try {
            Thread.sleep(20000);
        } catch (InterruptedException e) 
        {System.out.println(e);}
    }

    public void serialEvent(SerialPortEvent event) {
        switch(event.getEventType()) {
        case SerialPortEvent.BI:
        case SerialPortEvent.OE:
        case SerialPortEvent.FE:
        case SerialPortEvent.PE:
        case SerialPortEvent.CD:
        case SerialPortEvent.CTS:
        case SerialPortEvent.DSR:
        case SerialPortEvent.RI:
        case SerialPortEvent.OUTPUT_BUFFER_EMPTY:
            break;
        case SerialPortEvent.DATA_AVAILABLE:
          byte[] readBuffer = new byte[3];
			 PrintStream fileStream = null;
            try {
			
			
              if (inputStream.available() > 0) {
                  int numBytes = inputStream.read(readBuffer);
                   // System.out.println(numBytes);
             }	
		
		fileStream = new PrintStream(new FileOutputStream("outboxing.txt",true));
		//Redirecting console output to file
		System.setOut(fileStream);
		System.out.print(new String(readBuffer));    
		
            } 
		catch (FileNotFoundException fnfEx)
		{
			System.out.println("Error in IO Redirection");
			fnfEx.printStackTrace();
		}
            	catch (IOException e) 
            	{
            	System.out.println(e);
            	}
		

            break;
        }
    }
}

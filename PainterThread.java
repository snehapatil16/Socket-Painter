package socketPainter;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class PainterThread implements Runnable {

	//	private int[] output;
	private Socket client;
	private Hub hub;
	ObjectInputStream ois;
	ObjectOutputStream oos;
	private String username;
	private boolean isAlive;

	//this arrayList will be used to filter received shapes that this painter created
	private ArrayList<PaintingPrimitive> thisArtist;


	public PainterThread(Socket client) {
		this.client = client;
		this.hub = hub;

		try {
			this.ois = new ObjectInputStream(client.getInputStream());
			this.oos = new ObjectOutputStream(client.getOutputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("PAINTERTHREAD ERROR: oos/ois init failure");
			e.printStackTrace();
		}
	}

	public void run() {
		/*
		 * Threads:
		 * 	1. Listen for messages sent by this Painter
		 * 	2. Listen for shapes drawn by this 
		 */
		try {
			Hub.painters.add(this);
			
			//we know that the first thing sent will be the Painter's username
			this.username = (String) ois.readObject();
			
			//announce entrance
			Hub.broadcastMessage("---------" + this.username + " has entered the studio ---------");
			System.out.println("PainterThread:\t username: " + this.username);
			
			//once username is received from Painter, start threading
			try {
				//Listen for updates from this Painter
				Thread canvasListener = new Thread() {
					public void run() {
						try {
							boolean isConnected = true;
							while(true) {
								Object obj = ois.readObject();
								
								if(obj.getClass().toString().contains("String")) {
									String toBroadcast = (String) obj;
									Hub.broadcastMessage(toBroadcast);
								}else if(obj.getClass().toString().contains("ArrrayList")){
//									System.out.println("PainterThread CanvasListener: ArrayList");
								}else {
									PaintingPrimitive toBroadcast = (PaintingPrimitive) obj;
									Hub.broadcastShape(toBroadcast);
								}
							}
						}catch(ClassNotFoundException e) {
							System.out.println("PAINTERTHREAD ERROR: canvasListener failure");
							e.printStackTrace();
						} catch (EOFException eof) {
							System.out.println("PAINTERTHREAD ERROR: canvasListener EOF");
							eof.printStackTrace();
						}catch (IOException e) {
							e.printStackTrace();
						}
					}
				};
				canvasListener.start();
			}catch(Exception e) {
				System.out.println("PAINTERTHREAD ERROR: Unknown Exception");
				e.printStackTrace();
			}
		}catch(ClassNotFoundException e) {
			System.out.println("PAINTERTHREAD ERROR: run() ClassNotFound failure");
			e.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	
	/*======================Hub Communication====================*/
	public synchronized void initPainterPanel(ArrayList<PaintingPrimitive> canvas) {
		System.out.println("PainterThread: Init canvas");
		try {
			synchronized(this) {
				oos.writeObject(canvas);
			}
		} catch (IOException e) {
			System.out.println("PAINTERTHREAD ERROR: canvas init failure");
			e.printStackTrace();
		}
	}
	
	/**
	 * chatUpdateFromHub -- receives a chat update from the Hub, and forwards it to this.feed
	 * @param message: String
	 */
	public synchronized void chatUpdateFromHub(String message) {
		try {
//			System.out.println("PainterThread received: " + message);
			oos.writeObject(message);
		} catch (IOException e) {
			System.out.println("PainterThread ERROR: chatUpdateFromHub failure");
			e.printStackTrace();
			System.exit(0);
		}
	}
	
	/**
	 * shapeUpdateFromHub -- received a shape from the Hub, and forwards it to to this.PaintingPanel
	 * @param shape
	 */
	public synchronized void shapeUpdateFromHub(PaintingPrimitive shape) {
		try {
//			System.out.println("PainterThread received: " + shape.toString());
			oos.writeObject(shape);
		} catch (IOException e) {
			System.out.println("PainterThread ERROR: shapeUpdateFromHub failure");
			e.printStackTrace();
			System.exit(0);
		}
		
	}
	
	public synchronized void updateFromPainter(ObjectInputStream ois) {
		try {
			Object obj = ois.readObject();
		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
		}
	}

	public String getUsername() {
		return this.username;
	}

	public String toString() {
		return "Controller for: " + this.username;
	}
}
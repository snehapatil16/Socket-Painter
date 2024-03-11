package socketPainter;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashSet;

public class Hub {

	static HashSet<PainterThread> painters = new HashSet<PainterThread>();
	static ArrayList<PaintingPrimitive> masterCanvas = new ArrayList<PaintingPrimitive>();

	/**
	 * broadcastMessage - receives a message from a PainterThread, and broadcasts it to all connected painters
	 * @param message: String
	 */
	public static void broadcastMessage(String message) {
		/*When a new user joins, they do not see the master chat, the first message
		 * they will see is the server's announcement of their entering
		 */
		for(PainterThread pt : painters) {
			pt.chatUpdateFromHub(message + "\n");
		}
	}

	/**
	 * broadcastShape -- receives a shape from a PainterThread, and broadcasts it to all connected painters
	 * @param shape: PaintingPrimitive
	 */
	public static void broadcastShape(PaintingPrimitive shape) {
		//add shape to masterCanvas
		masterCanvas.add(shape);

		//since a shape cannot be deleted, only need to send the most recent shape to each Painter
		for(PainterThread pt : painters) {
			try {
				pt.shapeUpdateFromHub(shape);
			}catch(Exception e) {
				System.out.println("HUB ERROR: shapeUpdateFromHub");
				e.printStackTrace();
				System.exit(0);
			}
		}
	}

	/**
	 * broadcastDisconnect -- receives a shutdown notice from a PainterThread, and removes it from the list of painters
	 * TODO: get this working
	 * @param pt: PainterThread
	 */
	public static void broadcastDisconnect(PainterThread pt) {
		painters.remove(pt);
	}

	/**
	 * startHub -- listens for new Painter connections, assigns a new PainterThread to be the intermediary,
	 * 				forks off a new thread, and resumes listening for new connections
	 */
	private void startHub() {
		System.out.println("Hub started, awaiting Painter connections...");
		ServerSocket ss = null;
		Socket s = null;
		try {
			ss = new ServerSocket(7005);
			while(true) {
				s = ss.accept();
				PainterThread pt = new PainterThread(s);
				Thread thread = new Thread(pt);
				painters.add(pt);
				for(PaintingPrimitive p : masterCanvas) {
					pt.shapeUpdateFromHub(p);
				}
				thread.start();
			}
		}catch(IOException e) {
			e.printStackTrace();
			System.exit(0);
		}
	}

	public static void main(String[] args) {
		new Hub().startHub();
	}
}
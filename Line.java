package socketPainter;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

public class Line extends PaintingPrimitive{

	private Point start;
	private Point end;
	
	public Line(Point start, Point end, Color c) {
		super(c);
		this.start = start;
		this.end = end;
	}

	@Override
	public void drawGeometry(Graphics g) {
		g.drawLine(start.x, start.y, end.x, end.y);
	}
	
	public String toString() {
		return "A Line from: " + this.start.toString() + " to " + this.end.toString();
	}
}

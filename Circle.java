package socketPainter;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

public class Circle extends PaintingPrimitive {

private Point center, radiusPoint;
	
	public Circle(Point center, Point radiusPoint, Color c) {
		super(c);
		this.center = center;
		this.radiusPoint = radiusPoint;
	}

	public void drawGeometry(Graphics g) {
        int radius = (int) Math.abs(center.distance(radiusPoint));
        g.drawOval(center.x - radius, center.y - radius, radius*2, radius*2);   
	}
	
	public String toString() {
		return "A Circle with Center: " + this.center.toString() + ") and RadiusPoint: " + this.radiusPoint.toString();
	}

}
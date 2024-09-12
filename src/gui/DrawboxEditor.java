package gui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Flow.Subscriber;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JTabbedPane;

import events.EntityDrawboxChangedEvent;
import events.EntityDrawboxChangedListener;
import model.Drawbox;
import model.Point;
import repository.Project;

public class DrawboxEditor extends Editable {
	
	private List<Point> drawboxPoints;
	private List<Point> basePoints;
	Logger logger = Logger.getLogger("gui.DrawboxRectangleEditor");
	Point currentPoint = new Point(0, 0);
	private List<EntityDrawboxChangedListener> listeners = new ArrayList<>();
	

    DrawboxEditor(ListGUI listGUI) {
        super(listGUI);
        logger.setLevel(Level.CONFIG);
    }

	@Override
	public void drawing(Graphics2D g) {
		Drawbox drawbox = entity.getDrawbox();
        drawboxPoints = drawbox.getDrawboxlistPoints();
        basePoints = drawbox.getbaseListPoints();
        logger.finest("drawbox point size: "+ drawboxPoints.size());
        for(Point p: drawboxPoints) 
            logger.finest("DrawBoxPoint: ["+ p.x + ":"+p.y+"]");
        for(Point p: basePoints) 
            logger.finest("BasePoint: ["+ p.x + ":"+p.y+"]");
        
		if(drawboxPoints.size() >= 1 && drawboxPoints.size() < 4) {
			Point lastPoint = drawboxPoints.get(drawboxPoints.size()-1);
			g.drawLine((int)lastPoint.x, (int)lastPoint.y, (int)currentPoint.x, (int)currentPoint.y);
	        for(int i = 0; i < drawboxPoints.size()-1;i++) {
	            int x1 = (int)drawboxPoints.get(i).x;
	            int y1 = (int)drawboxPoints.get(i).y;
	            int x2 = (int)drawboxPoints.get(i+1).x;
	            int y2 = (int)drawboxPoints.get(i+1).y;
	            g.drawLine(x1, y1, x2, y2);
	        }
		} else {
	        for(int i = 0; i < drawboxPoints.size();i++) {
	            int x1 = (int)drawboxPoints.get(i % drawboxPoints.size()).x;
	            int y1 = (int)drawboxPoints.get(i % drawboxPoints.size()).y;
	            int x2 = (int)drawboxPoints.get((i+1) % drawboxPoints.size()).x;
	            int y2 = (int)drawboxPoints.get((i+1) % drawboxPoints.size()).y;
	            g.drawLine(x1, y1, x2, y2);
	        }
//	         ОТРИСОВКА ОСНОВАНИЯ
	        g.setColor(Color.BLUE);
	        for(int i = 0; i < basePoints.size()-1;i++) {
	            int x1 = (int)basePoints.get(i).x;
	            int y1 = (int)basePoints.get(i).y;
	            int x2 = (int)basePoints.get(i+1).x;
	            int y2 = (int)basePoints.get(i+1).y;
	            g.drawLine(x1, y1+3, x2, y2+3);
	        }
		}
	}

	@Override
	public void saveDataInEntity() { 
		entity.setDrawbox(new Drawbox(drawboxPoints));
	}


	@Override
	public void mousePressed(MouseEvent e) {
		if(drawboxPoints.size() < 4) {
			Point p = new Point(e.getX(), e.getY());
			drawboxPoints.add(p);
			if(drawboxPoints.size() == 4) {
				Point baseStart = drawboxPoints.get(0), 
					  baseEnd = drawboxPoints.get(1);
				for(Point pseudo: drawboxPoints) { // если не будет цикла в цикле - не выйдет сравнить всех со всеми
					for(Point vertex: drawboxPoints) { // что приведёт к артефактам построения основания
						if(vertex != baseStart && vertex != baseEnd) {
							if(vertex.y >= baseStart.y) baseStart = vertex;
							else if(vertex.y >= baseEnd.y) baseEnd = vertex;
						}
					}
				}
				basePoints.add(baseStart);
				basePoints.add(baseEnd);
				
				saveDataInEntity();
				//при выборе 4-й точки и формировании данных drawbox, уведомляем всех подписчит
				if(listeners!=null) {
					notifySubscribers();	
				}
				
			}
		} 
	}
	/*Точки должны идти в определенном порядке: 
	 * левая верхняя точка, правая верхняя, нижняя правая, нижняя левая;
	 * Обе нижние точки, т.е. 3 и 4 являются основанием - хардкод.
	 * P.s. возможно это не критично, и в сторе предусмотрено, что бы они сортировались,
	 * но на данном этапе на всякий случай стоит так поступить*/
	private void sortingDrawboxPoints() {
		//окей, это написано плохо, но... Похуй, пляшем)
		for(int i = 0; i<3; i++) {
			Point leftTopPoint = drawboxPoints.get(i);
			for(int j = i+1; j<4; j++) {
				if((leftTopPoint.x>drawboxPoints.get(j).x)&&(leftTopPoint.y>drawboxPoints.get(j).y)) {
					 Collections.swap(drawboxPoints, 0, j);
					 
				}
				
			}
		}
		
		
	}
	
	@Override
	public void mouseMoved(MouseEvent e) {
		currentPoint.x = e.getX();
		currentPoint.y = e.getY();
		repaint();
	}
	
	
	public void subscribe(EntityDrawboxChangedListener listener) {
        listeners.add(listener);
	}
	
	public void unsubscribe(EntityDrawboxChangedListener listener) {
        listeners.remove(listener);
	}
	
	private void notifySubscribers() {
		for (EntityDrawboxChangedListener listener : listeners) {
			listener.getEvent(new EntityDrawboxChangedEvent(entity.getDrawbox()));
		}
	}
	
	// эта штука очищает точки при нажатии универскальной кнопки очистки в Main GUI. Это следует рефакторнуть и вместо передачи события сюда,
	// обрабатывать его прямо в Main GUI(лямбдой) вызывая отсюда только метод в духе clearPoints()
	@Override
	public void actionPerformed(ActionEvent e) {
		JTabbedPane parent = (JTabbedPane) getParent();
		if(parent.getSelectedComponent() == this){
			if(entity != null) {
				entity.getDrawbox().getDrawboxlistPoints().clear();
				entity.getDrawbox().getbaseListPoints().clear();
				repaint();
			}
		}
	}
	
}

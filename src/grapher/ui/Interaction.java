/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package grapher.ui;

import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.input.MouseEvent;

/**
 *
 * @author driowyaa
 */
public class Interaction implements EventHandler<MouseEvent>{
    private final GrapherCanvas grapher;
    private enum State{IDLE,WAITING_G,WAITING_D,DRAGGING,SELECTING};
    private State state = State.IDLE;
    private Point2D pointDepart;

    public Interaction(GrapherCanvas grapher) {
        this.grapher = grapher;
    }
    
    @Override
    public void handle(MouseEvent e) {
        switch(state){
            
            case IDLE :
                switch(e.getEventType().getName()){
                    case "MOUSE_PRESSED":
                        if(e.isPrimaryButtonDown()){
                            pointDepart = new Point2D(e.getX(), e.getY());//on recupere le point lors du click, pour calculer si on est tjrs dans l'etat du click ou drag
                            state = State.WAITING_G;
                            System.out.println("WAITING G...");
                        }else if(e.isSecondaryButtonDown()){
                            pointDepart = new Point2D(e.getX(), e.getY());//on recupere le point lors du click, pour calculer si on est tjrs dans l'etat du click ou drag
                            state = State.WAITING_D;
                            System.out.println("WAITING D...");
                        }
                    break; 
                    
                    default :
                    break;
                }
            break;
                
            case WAITING_G :
                switch(e.getEventType().getName()){
                    case "MOUSE_RELEASED":
                        grapher.zoom(pointDepart, 5);
                        state = State.IDLE;
                        System.out.println("WAITING_G -> IDLE");
                    break;
                    
                    case "MOUSE_DRAGGED":
                        Point2D position = new Point2D(e.getX(), e.getY());
                        if(pointDepart.distance(position)>5){
                            grapher.setCursor(Cursor.CLOSED_HAND);
                            grapher.translate(e.getX()-pointDepart.getX(), e.getY()-pointDepart.getY());
                            pointDepart = position;
                            state = State.DRAGGING;
                            System.out.println("WAITING_G -> DRAGGING...");
                        }
                    break;
                }
            break;
                
            case WAITING_D :
                switch(e.getEventType().getName()){
                    case "MOUSE_RELEASED":
                        grapher.zoom(pointDepart, -5);
                        state = State.IDLE;
                        System.out.println("WAITING_D -> IDLE");
                    break;
                    
                    case "MOUSE_DRAGGED":
                        Point2D position = new Point2D(e.getX(), e.getY());
                        if(pointDepart.distance(position)>5){
                            state = State.SELECTING;
                            System.out.println("WAITING_D -> SELECTING...");
                        }
                    break;
                }
            break;
            
                
            case DRAGGING  :
                switch(e.getEventType().getName()){
                    case "MOUSE_RELEASED":
                        grapher.setCursor(Cursor.DEFAULT);
                        state = State.IDLE;
                        System.out.println("DRAGGING -> IDLE");
                    break;
                    
                    case "MOUSE_DRAGGED":
                        grapher.translate(e.getX()-pointDepart.getX(), e.getY()-pointDepart.getY());
                        pointDepart = new Point2D(e.getX(), e.getY());
                        System.out.println("DRAGGING...");
                    break;
                }
                break;
                
            case SELECTING :
              
                switch(e.getEventType().getName()){
                    case "MOUSE_RELEASED":
                        Point2D position = new Point2D(e.getX(), e.getY());
                        grapher.zoom(pointDepart, position);
                        state = State.IDLE;
                        System.out.println("SELECTING -> IDLE");
                    break;
                    
                    case "MOUSE_DRAGGED":
                        Point2D position1 = new Point2D(e.getX(), e.getY());
                        grapher.redraw();
                        grapher.getGraphicsContext2D().setLineDashes(5);
                        double x = pointDepart.getX() < position1.getX() ? pointDepart.getX() : position1.getX() ;
                        double y = pointDepart.getY() < position1.getY() ? pointDepart.getY() : position1.getY() ;
                        grapher.getGraphicsContext2D().strokeRect(x, y, Math.abs(position1.getX()-pointDepart.getX()), Math.abs(position1.getY()-pointDepart.getY()));
                        grapher.getGraphicsContext2D().setLineDashes(0);
                        System.out.println("SELECTING...");
                    break;
                        
                    default : 
                        break;
            }
        
        break;
                
        }
    }
    
}

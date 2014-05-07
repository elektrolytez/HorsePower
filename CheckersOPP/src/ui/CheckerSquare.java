package ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public class CheckerSquare extends JPanel{
        private int _occupant = -1;
        private final Color _BORDER = new Color(127,127,127);
        private final Color _TOPCOLOR = Color.BLACK;
        private final Color _BOTCOLOR = Color.WHITE;
        private final Color _KINGCOLOR = Color.YELLOW;
        private final Color _BGCOLOR;
        private final int _PADDING = 4;
        private final int _BORDERWIDTH = 2;
        private final float[] _kingx = {-0.9f, -0.3f, 0, 0.3f, 0.9f, 0.55f, -0.55f };
        private final float[] _kingy = { 0.35f, -0.1f, 0.35f, -0.1f, 0.35f, -0.55f, -0.55f };
        private final int _kingpointcount = 7;

        /**
         * Creates an individual square for use with a checker board.
         * @param type Boolean variable setting the background color.  TRUE for dark.
         * FALSE for light.
         */
        CheckerSquare(boolean type){
                        if(type){
                                _BGCOLOR = new Color(64, 20, 0);
                        }
                        else{
                                _BGCOLOR = new Color(245, 228, 156);
                        }
        }
        
        /**
         * Sets the occupant of a square.
         * @param o Integer value representing 
         */
        public void setOccupant(int o){
                _occupant = o;
        }
        
        /**
         * Determines what needs to be drawn inside the square and then draws it.
         */
        public void paint(Graphics g){
                Graphics2D g2 = (Graphics2D)g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                g2.setColor(_BGCOLOR);
                g2.fillRect(0, 0, getWidth(), getHeight());
                int decider = _occupant;
                if(decider == 1){
                        circle(g2, _TOPCOLOR, false);
                }
                else if(decider == 2){
                        circle(g2, _BOTCOLOR, false);
                }
                else if(decider == 3){
                        circle(g2, _TOPCOLOR, true);
                }
                else if(decider == 4){
                        circle(g2, _BOTCOLOR, true);
                }
        }
        
        /**
         * Draws a circle within a square if there is a piece occupying it.
         * @param g Graphics class containing the drawing methods.
         * @param c Color of the Circle to be drawn.
         * @param king Whether or not it is a king.
         */
        private void circle(Graphics2D g, Color c, boolean king){
                int width = getWidth()-2*_PADDING;
                int height = getHeight()-2*_PADDING;

                g.setColor(_BORDER);
                g.fillOval(_PADDING, _PADDING, width, height);
                g.setColor(c);
                g.fillOval(_PADDING+_BORDERWIDTH,_PADDING+_BORDERWIDTH,  width-_BORDERWIDTH*2, height-_BORDERWIDTH*2);
                if(king){
                        king(g);
                }
        }
        
        private void king(Graphics2D g){
                int width = (getWidth()-4*_PADDING)/2;
                int height = (getHeight()-4*_PADDING)/2;

                int[] xcoords = new int[_kingpointcount];
                int[] ycoords = new int[_kingpointcount];
                
                for(int i = 0; i < _kingpointcount; i++){
                        xcoords[i] = Math.round(_kingx[i]*width)+(getWidth()/2+_PADDING/2)+1;
                        ycoords[i] = -(Math.round(_kingy[i]*height)-(getHeight()/2+_PADDING/2)+1);
                }
                
                g.setColor(Color.GRAY);
                g.fillPolygon(xcoords, ycoords, _kingpointcount);
                
                for(int i = 0; i < _kingpointcount; i++){
                        xcoords[i] -= 1;
                        ycoords[i] -= 1;
                }
                g.setColor(_KINGCOLOR);
                g.fillPolygon(xcoords, ycoords, _kingpointcount);
        }
}	
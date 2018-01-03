package rocksample;

import burlap.mdp.core.oo.state.OOState;
import burlap.mdp.core.oo.state.ObjectInstance;
import burlap.visualizer.OOStatePainter;
import burlap.visualizer.ObjectPainter;
import burlap.visualizer.StateRenderLayer;
import burlap.visualizer.Visualizer;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.util.HashMap;
import java.util.Map;

import rocksample.state.RoverAgent;
import rocksample.state.RockSampleWall;
import rocksample.state.RockSampleRock;

/**
 * Created by steph on 10/26/2017.
 *
 */
public class RockSampleVisualizer {

    private static Map<String, Color> colors;
    private static int cellsWide, cellsTall;

    // initColors
    // Initializes the colors
    private static void initColors() {
        colors = new HashMap<String, Color>();
        colors.put(RockSample.COLOR_RED, Color.red);
        colors.put(RockSample.COLOR_YELLOW, Color.YELLOW);
        colors.put(RockSample.COLOR_BLUE, Color.BLUE);
        colors.put(RockSample.COLOR_GREEN, Color.GREEN);
        colors.put(RockSample.COLOR_MAGENTA, Color.MAGENTA);
        colors.put(RockSample.COLOR_BLACK, Color.BLACK);
        colors.put(RockSample.COLOR_GRAY, Color.DARK_GRAY);
    }

    // getVisualizer
    // Given a width and a height, returns a visualizer
    public static Visualizer getVisualizer(int w, int h) {
        initColors();
        Visualizer v = new Visualizer(getStateRenderLayer(w, h));
        return v;
    }

    // getStateRenderLayer
    // Given a width and a height, returns a state render layer
    public static StateRenderLayer getStateRenderLayer(int w, int h) {
        StateRenderLayer rl = new StateRenderLayer();
        OOStatePainter oopainter = new OOStatePainter();

        cellsWide = w;
        cellsTall = h;

        // add painters for each object
        oopainter.addObjectClassPainter(RockSample.CLASS_ROVER, new RoverPainter());
        oopainter.addObjectClassPainter(RockSample.CLASS_ROCK, new RockPainter());
        oopainter.addObjectClassPainter(RockSample.CLASS_WALL, new WallPainter());

        rl.addStatePainter(oopainter);

        return rl;
    }

    //these classes add graphics for each of the state objectsS
    public static class RoverPainter implements ObjectPainter {

        @Override
        public void paintObject(Graphics2D g2, OOState s, ObjectInstance ob,
                                float cWidth, float cHeight) {
            // set color and size of rover
            g2.setColor(Color.RED);
            float roverWidth = (float) cWidth / cellsWide;
            float roverHeight = (float) cHeight / cellsTall;

            // make the rover a rover agent and set the location
            RoverAgent rover = (RoverAgent) ob;
            int rx = (int) rover.get(RockSample.ATT_X);
            int ry = (int) rover.get(RockSample.ATT_Y);
            float roverx = rx * roverWidth - cellsWide;
            float rovery = cHeight - (1 + ry) * roverHeight;

            float scale = 0.9f;
            float realWidth = roverWidth * scale;
            float realHeight = roverHeight * scale;
            float realX = roverx + 0.08f * roverWidth;
            float realy = rovery + 0.05f * roverHeight;

            g2.fill(new Ellipse2D.Float(realX, realy, realWidth, realHeight));
        }
    }

    public static class RockPainter implements ObjectPainter {

        @Override
        public void paintObject(Graphics2D g2, OOState s, ObjectInstance ob,
                                float cWidth, float cHeight){
            // set color and size of rocks
            g2.setColor(Color.GRAY);
            float rockWidth = (float) cWidth / cellsWide;
            float rockHeight = (float) cHeight / cellsTall;

            // make rock a rock object and set the location
            RockSampleRock rock = (RockSampleRock) ob;
            int rx = (int) rock.get(RockSample.ATT_X);
            int ry = (int) rock.get(RockSample.ATT_Y);
            float rockx = rx * rockWidth - cellsWide;
            float rocky = cHeight - (1 + ry) * rockHeight;

            float scale = 0.9f;
            float realWidth = rockWidth * scale;
            float realHeight = rockHeight * scale;
            float realX = rockx + 0.08f * rockWidth;
            float realy = rocky + 0.05f * rockHeight;

            g2.fill(new Ellipse2D.Float(realX, realy, realWidth, realHeight));
        }
    }

    public static class WallPainter implements ObjectPainter {

        @Override
        public void paintObject(Graphics2D g2, OOState s, ObjectInstance ob,
                                float cWidth, float cHeight) {
            g2.setColor(Color.BLACK);
            g2.setStroke(new BasicStroke(10));

            float wallWidth = (float) cWidth / cellsWide;
            float wallHeight = (float) cHeight / cellsTall;

            RockSampleWall w = (RockSampleWall) ob;
            int startx = (int) w.get(RockSample.ATT_START_X);
            int starty = (int) w.get(RockSample.ATT_START_Y);
            float wx1 = startx * wallWidth;
            float wy1 = cHeight - starty * wallHeight;
            float wx2, wy2;

            int length = (int) w.get(RockSample.ATT_LENGTH);
            boolean isHoriz = (boolean) w.get(RockSample.ATT_IS_HORIZONTAL);
            if(isHoriz) {
                wx2 = wx1 + length * wallWidth;
                wy2 = wy1;
            } else {
                wx2 = wx1;
                wy2 = wy1 - length * wallHeight;
            }

            g2.drawLine((int) wx1, (int) wy1, (int) wx2, (int) wy2);
        }
    }
}

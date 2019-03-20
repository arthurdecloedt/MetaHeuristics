package itsp;

import geneticalgo.Generation;

import javax.swing.*;
import java.awt.*;
import java.util.Comparator;

public class GuiITSP extends JPanel {

    private final ITSPInstance problemInstance;
    private ITSPIndividual toShow;

    public GuiITSP(ITSPInstance problemInstance){
        this.problemInstance = problemInstance;
        setPreferredSize(new Dimension(800, 600));

        JFrame frame = new JFrame("ITSP");
        frame.add(this);
        frame.pack();
        frame.setVisible(true);
    }

    public void showGeneration(Generation<ITSPIndividual> generation) {
        this.toShow = generation.getIndividuals().stream().sorted(Comparator.comparingDouble(ITSPIndividual::calculateFitness)).findFirst().get();
        repaint();
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);

        if(toShow == null)
            return;

        double xMin = 0, xMax = 20, yMin = 0, yMax = 20, border = 20;
        int nodeSize = 25;

        ProcessingNode prev = null;
        for(ITSPVisit visit: toShow.getVisits()) {
            ProcessingNode node = problemInstance.getNode(visit.getNodeId());
            if(prev != null) {
                int centerX = getScaled(node.getX(), xMin, xMax, getWidth(), border);
                int centerY = getScaled(node.getY(), yMin, yMax, getHeight(), border);
                int prevX = getScaled(prev.getX(), xMin, xMax, getWidth(), border);
                int prevY = getScaled(prev.getY(), yMin, yMax, getHeight(), border);
                graphics.setColor(Color.BLUE);
                drawArrow(graphics, centerX, centerY, prevX, prevY);
            }
            prev = node;
        }

        for(ITSPVisit visit: toShow.getVisits()) {
            ProcessingNode node = problemInstance.getNode(visit.getNodeId());
            int centerX = getScaled(node.getX(), xMin, xMax, getWidth(), border);
            int centerY = getScaled(node.getY(), yMin, yMax, getHeight(), border);
            graphics.setColor(Color.BLACK);
            graphics.fillOval(centerX - nodeSize / 2, centerY - nodeSize / 2, nodeSize, nodeSize);
            graphics.setColor(Color.WHITE);
            graphics.drawString("" + visit.getNodeId(), centerX - 3, centerY + 5);
        }
    }

    private int getScaled(double loc, double min, double max, double size, double border) {
        return (int) Math.round((loc-min) / (max-min) * (size-2*border) + border);
    }

    private void drawArrow(Graphics g, int x1, int y1, int x2, int y2) {
        g.drawLine(x1, y1, x2, y2);
        double pos = 1-10/Math.sqrt((x2-x1)*(x2-x1)+(y2-y1)*(y2-y1));
        int drawx = (int) Math.round(x1+pos*(x2-x1));
        int drawy = (int) Math.round(y1+pos*(y2-y1));
        double incAngle = Math.atan2((y1-y2), (x1-x2));
        double da = Math.PI/12;
        double len = 20;
        g.drawLine(
                drawx+(int) Math.round(Math.cos(incAngle+da)*len),
                drawy+(int) Math.round(Math.sin(incAngle+da)*len),
                drawx, drawy);
        g.drawLine(
                drawx+(int) Math.round(Math.cos(incAngle-da)*len),
                drawy+(int) Math.round(Math.sin(incAngle-da)*len),
                drawx, drawy);
    }
}

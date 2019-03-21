package itsp;

import geneticalgo.Generation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class GuiITSP extends JPanel implements MouseMotionListener {

    private final static int nodeSize = 25;

    private final ITSPInstance problemInstance;
    private List<ITSPIndividual> individuals;
    int current;
    private Point mousePos = new Point(0, 0);

    public GuiITSP(ITSPInstance problemInstance){
        this.problemInstance = problemInstance;
        this.individuals = new ArrayList<>();
        this.current = -1;
        setPreferredSize(new Dimension(800, 600));
        addMouseMotionListener(this);

        JFrame frame = new JFrame("ITSP");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(this);
        frame.pack();
        frame.setVisible(true);

        setLayout(null);
        JButton prev = new JButton("<"), next = new JButton(">");
        JLabel currLabel = new JLabel(""+current);
        int bw = 20, bh = 20;
        prev.setBounds(10, getHeight()-bh-1, bw, bh);
        prev.setMargin(new Insets(0, 0, 0, 0));
        next.setBounds(40+bw+10, getHeight()-bh-1, bw, bh);
        next.setMargin(new Insets(0, 0, 0, 0));
        currLabel.setBounds(10+bw+10, getHeight()-bh-1, 30, bh);
        prev.addActionListener((e) -> {current--;currLabel.setText(""+current);repaint();});
        next.addActionListener((e) -> {current++;currLabel.setText(""+current);repaint();});
        add(prev);
        add(next);
        add(currLabel);
    }

    public void showGeneration(Generation<ITSPIndividual> generation) {
        showIndividual(generation.getIndividuals().stream().min(Comparator.comparingDouble(ITSPIndividual::calculateFitness)).get());
    }

    public void showIndividual(ITSPIndividual individual) {
        this.individuals.add(individual);
        if(current == individuals.size()-2)
            current++;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);

        if(individuals.isEmpty() || current < 0 || current >= individuals.size())
            return;

        ITSPIndividual individual = individuals.get(current);

        double xMin = 0, xMax = 0, yMin = 0, yMax = 0, border = 20;
        for(ProcessingNode node: problemInstance.getNodes()) {
            xMin = Math.min(xMin, node.getX());
            xMax = Math.max(xMax, node.getX());
            yMin = Math.min(yMin, node.getY());
            yMax = Math.max(yMax, node.getY());
        }

        //Hovered node
        ProcessingNode hoveredNode = null;
        for(ITSPVisit visit: individual.getVisits()) {
            ProcessingNode node = problemInstance.getNode(visit.getNodeId());
            int centerX = getScaled(node.getX(), xMin, xMax, getWidth(), border);
            int centerY = getScaled(node.getY(), yMin, yMax, getHeight(), border);
            if((mousePos.getX()-centerX)*(mousePos.getX()-centerX)+(mousePos.getY()-centerY)*(mousePos.getY()-centerY) < nodeSize*nodeSize/4) {
                hoveredNode = node;
            }
        }

        //Arrows
        ProcessingNode prev = null;
        for(ITSPVisit visit: individual.getVisits()) {
            ProcessingNode node = problemInstance.getNode(visit.getNodeId());
            if(prev != null) {
                int centerX = getScaled(node.getX(), xMin, xMax, getWidth(), border);
                int centerY = getScaled(node.getY(), yMin, yMax, getHeight(), border);
                int prevX = getScaled(prev.getX(), xMin, xMax, getWidth(), border);
                int prevY = getScaled(prev.getY(), yMin, yMax, getHeight(), border);
                graphics.setColor(node.equals(hoveredNode) ? Color.RED : prev.equals(hoveredNode) ? Color.GREEN : Color.GRAY);
                drawArrow(graphics, prevX, prevY, centerX, centerY);
            }
            prev = node;
        }

        //Nodes
        for(ProcessingNode node: problemInstance.getNodes()) {
            int centerX = getScaled(node.getX(), xMin, xMax, getWidth(), border);
            int centerY = getScaled(node.getY(), yMin, yMax, getHeight(), border);
            drawNode(graphics, node, centerX, centerY);
        }

        //Node info
        if(hoveredNode != null) {
            int centerX = getScaled(hoveredNode.getX(), xMin, xMax, getWidth(), border);
            int centerY = getScaled(hoveredNode.getY(), yMin, yMax, getHeight(), border);
            drawNodeInfo(graphics, individual, hoveredNode, centerX + nodeSize, centerY - 20);
        }

        //Generation info
        graphics.setColor(Color.BLUE);
        drawString(graphics,
                "Fitness: "+ individual.getFitness()+"\n"+
                        "TT="+ individual.getTravellingTime()+", PT="+ individual.getProcessingTime()+", WT="+ individual.getWaitingTime()
                , 5, 17);
    }

    private void drawNode(Graphics graphics, ProcessingNode node, int x, int centerY) {
        graphics.setColor(Color.BLACK);
        graphics.fillOval(x - nodeSize / 2, centerY - nodeSize / 2, nodeSize, nodeSize);
        graphics.setColor(Color.WHITE);
        graphics.drawString("" + node.getId(), x - 3, centerY + 5);
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

    @Override
    public void mouseDragged(MouseEvent event) {
        mouseMoved(event);
    }

    @Override
    public void mouseMoved(MouseEvent event) {
        mousePos = event.getPoint();
        repaint();
    }

    private void drawString(Graphics g, String s, int x, int y) {
        int yOffs = 0;
        for(String s1: s.split("\n")){
            int xOffs = 0;
            for(String s2: s1.split("\t")){
                g.drawString(s2, x+xOffs, y+yOffs);
                xOffs += Math.ceil(Math.max(1, s2.length())/8.0)*8*6;
            }
            yOffs += 15;
        }
    }

    private void drawNodeInfo(Graphics graphics, ITSPIndividual individual, ProcessingNode node, int x, int y) {
        int rectW = 100, rectH = 200;
        x = Math.min(Math.max(x, 0), getWidth()-rectW);
        y = Math.min(Math.max(y, 0), getHeight()-rectH);
        graphics.setColor(Color.WHITE);
        graphics.fillRect(x, y, 100, 200);
        graphics.setColor(Color.BLACK);
        graphics.drawRect(x, y, 100, 200);
        List<ITSPVisit> visits = individual.getVisits();
        String visitString = IntStream.range(0, visits.size()).filter(i->visits.get(i).getNodeId()==node.getId()).mapToObj(i->i+"\t"+visits.get(i).getTime()).collect(Collectors.joining("\n"));
        drawString(graphics, "Node "+node.getId()+"\nPT: "+node.getProcessingTime()+"\n\nVisits:\nIndex\tTime\n"+visitString, x+5, y+15);
    }

}

import javax.swing.*;
import java.awt.*;

public class DisplayTree extends JFrame {
    JScrollPane scrollPane;
    DisplayPanel panel;


    public DisplayTree(Branch branch) {
        panel = new DisplayPanel(branch);
        panel.setPreferredSize(new Dimension(800, 800));
        panel.setBackground(Color.DARK_GRAY);
        scrollPane = new JScrollPane(panel);
        getContentPane().add(scrollPane, BorderLayout.CENTER);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        pack();  // cleans up the window panel
    }
}

class DisplayPanel extends JPanel {
    Branch branch;
    int xs;
    int ys;

    public DisplayPanel(Branch branch) {
        this.branch = branch; // allows dispay routines to access the tree
        setBackground(Color.white);
        setForeground(Color.black);
    }

    protected void paintComponent(Graphics g) {
        g.setColor(getBackground()); //colors the window
        g.fillRect(0, 0, getWidth(), getHeight());
        g.setColor(getForeground()); //set color and fonts
        Font MyFont = new Font("Arial",Font.BOLD,15);
        g.setFont(MyFont);
        g.setColor(Color.ORANGE);
        xs = 20;   //where to start printing on the panel
        ys = 20;
        g.drawString(branch.toString(), xs, ys);
        MyFont = new Font("Arial",Font.BOLD,12); //bigger font for tree
        g.setFont(MyFont);
        this.drawTree(g, branch.getManager()); // draw the tree
        revalidate(); //update the component panel
    }

    public void drawTree(Graphics g, Distributor d) {//actually draws the tree
        int dx, dy, dx2, dy2;
        int SCREEN_WIDTH = 500;
        int SCREEN_HEIGHT = 500;
        int X_SCALE, Y_SCALE;
        X_SCALE = SCREEN_WIDTH / branch.totalNodes; //scale x by total nodes in tree
        Y_SCALE = (SCREEN_HEIGHT - ys) / (branch.treeHeight(branch.getManager()) + 1); //scale y by tree height

        if (d != null) { // inorder traversal to draw each node
            drawTree(g, d.getLeftLeg()); // do left side of inorder traversal
            dx = d.xpos * X_SCALE; // get x,y coords., and scale them
            dy = d.ypos * Y_SCALE + ys;

            // draw a node
            String s = d.getName() + "  ID:" + d.getID(); //get the word at this node
            g.drawString(s, dx, dy);

            s = d.getCommission() + " VND";
            g.drawString(s, dx, dy + 12);
            // this draws the lines from a node to its children, if any
            if (d.getLeftLeg() != null) { //draws the line to left child if it exists
                dx2 = d.getLeftLeg().xpos * X_SCALE;
                dy2 = d.getLeftLeg().ypos * Y_SCALE + ys;
                g.drawLine(dx, dy, dx2, dy2);
            }
            if (d.getRightLeg() != null) { //draws the line to right child if it exists
                dx2 = d.getRightLeg().xpos * X_SCALE;//get right child x,y scaled position
                dy2 = d.getRightLeg().ypos * Y_SCALE + ys;
                g.drawLine(dx, dy, dx2, dy2);
            }
            drawTree(g, d.getRightLeg()); //now do right side of inorder traversal
        }
    }
}
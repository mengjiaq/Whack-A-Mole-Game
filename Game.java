
import java.awt.Color;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.TitledBorder;
/**
 * Homework7 Whack-a-mole.
 * @author Mengjia Qi
 *
 */
public class Game {
    /**
     * Up color constant.
     */
    private static final Color UP_COLOR = Color.GREEN;
    /**
     * Down color constant.
     */
    private static final Color DOWN_COLOR = Color.LIGHT_GRAY;
    /**
     * Hit color constant.
     */
    private static final Color HIT_COLOR = Color.RED;
    /**
     * Down string constant.
     */
    private static final String DOWN_STRING = "   ";
    /**
     * Up string constant.
     */
    private static final String UP_STRING = ":-)";
    /**
     * Hit string constant.
     */
    private static final String HIT_STRING = ":-(";
    /**
     * Sleep time in milliseconds.
     */
    private long time = -1;
    /**
     * Score.
     */
    private long score = 0;
    /**
     * Array of buttons to show on and off.
     */
    private JButton[] holes;
    /**
     * Button "Start".
     */
    private JButton trigger = new JButton("start");
    /**
     * Label score.
     */
    private JLabel scoreLabel = new JLabel("Score: ");
    /**
     * Text area to show score.
     */
    private JTextArea scoreField = new JTextArea(2, 5);
    /**
     * Label time.
     */
    private JLabel timeLabel = new JLabel("Time Left");
    /**
     * Text area to show time.
     */
    private JTextArea timeField = new JTextArea(2, 5);
    /**
     * Random object to pick a button from the array.
     */
    private Random random = new Random();
    /**
     * Timer thread.
     */
    private Thread t2 = new Thread() {
        public void run() {
            try {
                while (true) {
                    Thread.sleep(1000);
                    if (time > 0) {
                        time--;
                        timeField.setText("" + time);
                    } else if (time == 0) {
                        Thread.sleep(5000);
                        trigger.setEnabled(true);
                        time--;
                    }
                }
            } catch (InterruptedException e) {
                throw new AssertionError(e);
            }
        }
    };
    /**
     * Game Constructor.
     */
    public Game() {
        scoreField.setText("0");
        scoreField.setEditable(false);
        timeField.setText("20");
        timeField.setEditable(false);
        JFrame frame = new JFrame("Whack-a-Mole!!");
        frame.setSize(520, 630);
        frame.getContentPane().setLayout(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JTextArea textArea = new JTextArea(9, 60); textArea.setLineWrap(true); textArea.setEditable(false);
        /* Panel display */
        JPanel display = new JPanel();
        display.setBounds(10, 10, 780, 67);
        display.setLayout(new FlowLayout(FlowLayout.LEFT, 30, 15));
        /* Panel pane */
        JPanel pane = new JPanel();
        pane.setBorder(BorderFactory.createTitledBorder(null, "Let's Whack-A-Mole!!", TitledBorder.CENTER,
                TitledBorder.CENTER, new Font(null, Font.PLAIN, 20)));
        pane.setBounds(30, 100, 460, 500);
        pane.setLayout(new GridLayout(7, 7, 10, 10));
        /* Add components to frame */
        Container c = frame.getContentPane();
        c.add(display);
        c.add(pane);
        display.add(timeLabel);
        display.add(timeField);
        display.add(scoreLabel);
        display.add(scoreField);
        display.add(trigger);
        holes = new JButton[49];
        for (int i = 0; i < holes.length; i++) {
            holes[i] = new JButton();
            holes[i].setSize(1, 1);
            holes[i].setOpaque(true);
            holes[i].setBackground(DOWN_COLOR);
            holes[i].setText(DOWN_STRING);
            pane.add(holes[i]);
            holes[i].addActionListener(l);
        }
        frame.setVisible(true);
        t2.start();
        trigger.addActionListener(e-> {
           
                HoleThread[] holesThreads = new HoleThread[49];
                score = 0;
                time = 20;
                timeField.setText("" + time);
                scoreField.setText("" + score);
                trigger.setEnabled(false);
                for (int i = 0; i < 49; i++) {
                    holesThreads[i] = new HoleThread(holes[i]);
                    holesThreads[i].start();
                
            }
        });
    }
    /**
     * Thread for hole.
     */
    private class HoleThread extends Thread {
        /**
         * The hole that this instance represents.
         */
        private JButton hole;
        /**
         * Constructor of HoleThread.
         * @param hole The hole button that this instance represents
         */
        HoleThread(JButton hole) {
            this.hole = hole;
        }
        @Override
        public void run() {
            try {
                while (time > 0) {
                    long randomStartTime = random.nextInt(3500) + 500;
                    long randomSleepTime = random.nextInt(3500) + 500;
                    Thread.sleep(randomStartTime);
                    synchronized (hole) {
                        if (hole.getBackground() == UP_COLOR) {
                            Thread.sleep(randomSleepTime);
                            hole.setBackground(DOWN_COLOR);
                            hole.setText(DOWN_STRING);
                        } else if (time <= 0) {
                            hole.setBackground(DOWN_COLOR);
                            hole.setText(DOWN_STRING);
                        } else {
                            hole.setBackground(UP_COLOR);
                            hole.setText(UP_STRING);
                        }
                    }

                    Thread.sleep(2000);
                }
            } catch (InterruptedException e) {
                throw new AssertionError(e);
            }
        }
    }
    /**
     * ActionListener l for all hole buttons.
     */
    private ActionListener l = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            JButton hole = (JButton) e.getSource();
            if (hole.getBackground() == UP_COLOR && time > 0) {
                score++;
                hole.setBackground(HIT_COLOR);
                hole.setText(HIT_STRING);
                scoreField.setText("" + score);
            }
        }
    };
    /**
     * Main method.
     * @param args input(optional)
     */
    public static void main(String[] args) {
        new Game();
    }
}

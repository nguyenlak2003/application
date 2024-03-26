package Main;

/**
 *
 * @author Lăk
 */
import Background.ButtonPanel;
import Background.ColorManager;
import Background.MyCanvas;
import Background.MyFormatter;
import Visualizer.Visualizer;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.image.BufferStrategy;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.NumberFormat;
import javax.swing.BorderFactory;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class Main extends JFrame implements MyCanvas.DrawingRequest,
        ButtonPanel.ButtonListener, Visualizer.SortedListener {

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Main().setVisible(true);
            }
        });
    }

    private static final int WIDTH = 1280, HEIGHT = 720;
    private int CAPACITY = 100, FPS = 80;
    private JPanel mainPanel, buttonPanel, inputPanel, sliderPanel, inforPanel;
    private JLabel capacityLabel, fpsLabel, curentFps, timeLabel, swapLabel;
    private JSlider fpsSlider;
    private JFormattedTextField capacityField;
    private MyCanvas canvas;
    private Visualizer visualizer;

    public Main() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMaximumSize(new Dimension(WIDTH, HEIGHT + 200));
        setMinimumSize(new Dimension(WIDTH, HEIGHT + 20));
        setPreferredSize(new Dimension(WIDTH, HEIGHT + 20));
        setLocationRelativeTo(null);
        setResizable(false);
        setTitle("Visualizer sorting application");

        initialize();
    }

    // initialize components
    private void initialize() {
        mainPanel = new JPanel();
        mainPanel.setLayout(null);
        mainPanel.setBackground(ColorManager.BACKGROUND);
        this.add(mainPanel);

        //add butonPanel
        buttonPanel = new ButtonPanel(this);
        buttonPanel.setBounds(0, 150, 250, HEIGHT);
        buttonPanel.setBackground(ColorManager.BACKGROUND);
        mainPanel.add(buttonPanel);

        // add canvas
        canvas = new MyCanvas(this);
        int cWidth = WIDTH - 250;
        int cHeight = HEIGHT - 80;
        canvas.setFocusable(false);
        canvas.setMaximumSize(new Dimension(cWidth, cHeight));
        canvas.setMinimumSize(new Dimension(cWidth, cHeight));
        canvas.setPreferredSize(new Dimension(cWidth, cHeight));
        canvas.setBounds(250, 60, cWidth, cHeight);
        mainPanel.add(canvas);
        pack();

        visualizer = new Visualizer(CAPACITY, FPS, this);

        createCapacityField();
        createSliderFPS();
        createStatistics();
    }

    private void createCapacityField() {
        /* create an input field for capacity */
        // Label
        capacityLabel = new JLabel();
        capacityLabel.setText("Capacity");
        capacityLabel.setForeground(ColorManager.TEXT);
        capacityLabel.setFont(new Font(null, Font.BOLD, 15));

        //capacity input field
        NumberFormat format = NumberFormat.getNumberInstance();
        MyFormatter myFormat = new MyFormatter(format);
        myFormat.setValueClass(Integer.class);
        myFormat.setMaximum(200);
        myFormat.setMinimum(0);
        myFormat.setAllowsInvalid(false);
        myFormat.setCommitsOnValidEdit(true);

        capacityField = new JFormattedTextField(myFormat);
        capacityField.setValue(CAPACITY);
        capacityField.setBackground(ColorManager.CANVAS_BACKGROUND);
        capacityField.setForeground(ColorManager.TEXT);
        capacityField.setCaretColor(ColorManager.BAR_YELLOW);
        capacityField.setFont(new Font(null, Font.PLAIN, 15));
        capacityField.setBorder(BorderFactory.createLineBorder(ColorManager.FIELD_BORDER, 1));

        capacityField.addPropertyChangeListener("value", new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                int value = ((Number) capacityField.getValue()).intValue();
                visualizer.setCapacity(value);
            }
        });

        capacityLabel.setLabelFor(capacityField);

        // input panel
        inputPanel = new JPanel(new GridLayout(1, 0));
        inputPanel.setBounds(30, 20, 190, 35);
        inputPanel.setBackground(ColorManager.BACKGROUND);
        inputPanel.add(capacityLabel);
        inputPanel.add(capacityField);
        
        mainPanel.add(inputPanel);
    }
    
    // Statstics
    private void createStatistics()
    {
        // time label
        timeLabel = new JLabel("Elapsed Time: 0 µs");
        timeLabel.setFont(new Font(null, Font.PLAIN, 15));
        timeLabel.setForeground(ColorManager.TEXT_RED);
        timeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        timeLabel.setVerticalAlignment(SwingConstants.CENTER);
        
        // swap label
        swapLabel = new JLabel("Swaps: 0");
        swapLabel.setFont(new Font(null, Font.PLAIN, 15));
        swapLabel.setForeground(ColorManager.TEXT_GREEN);
        swapLabel.setHorizontalAlignment(SwingConstants.CENTER);
        swapLabel.setVerticalAlignment(SwingConstants.CENTER);
        
        //statistics panel
        inforPanel = new JPanel(new GridLayout(1, 0));
        inforPanel.setAlignmentX(CENTER_ALIGNMENT);
        inforPanel.add(timeLabel);
        inforPanel.add(swapLabel);
        inforPanel.setBackground(ColorManager.BACKGROUND);
        inforPanel.setBounds(350, 20, 800, 30);
        
        mainPanel.add(inforPanel);
    }
    
    // create slider
    private void createSliderFPS()
    { 
        //Label
        fpsLabel = new JLabel("FPS: ");
        fpsLabel.setForeground(ColorManager.TEXT);
        fpsLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        fpsLabel.setVerticalAlignment(JLabel.CENTER);
        fpsLabel.setFont(new Font(null, Font.BOLD, 15));
        
        //CurentFpsLabel
        curentFps = new JLabel();
        curentFps.setText(Integer.toString(FPS));
        curentFps.setFont(new Font(null, Font.BOLD, 15));
        curentFps.setForeground(ColorManager.TEXT);
            
        //Slider
        fpsSlider = new JSlider(JSlider.HORIZONTAL, 0, 350, FPS);
        fpsSlider.setMajorTickSpacing(50);
        fpsSlider.setMinorTickSpacing(25);
        fpsSlider.setPaintTicks(true);
        fpsSlider.setPaintTrack(true);
        fpsSlider.setPaintLabels(false);
        
        fpsSlider.addChangeListener(new ChangeListener()
        {
            public void stateChanged(ChangeEvent e)
            {
                int value = fpsSlider.getValue();
                curentFps.setText(Integer.toString(value));
                visualizer.setFPS(value);
            }
        });
        
        //combinate two label on a row
        JPanel labelsPanel = new JPanel(new GridLayout(1, 0));
        labelsPanel.setBackground(ColorManager.BACKGROUND);
        labelsPanel.add(fpsLabel);
        labelsPanel.add(curentFps);
        
        //Panel
        sliderPanel = new JPanel();
        sliderPanel.setLayout(new GridLayout(2, 0));
        sliderPanel.setBackground(ColorManager.BACKGROUND);
        sliderPanel.add(labelsPanel);
        sliderPanel.add(fpsSlider);
        sliderPanel.setBounds(30, 60, 190, 90);
        
        mainPanel.add(sliderPanel);
    }

    public void onDrawArray() {
        visualizer.drawArray();
    }

    public void sortButtonClicked(int id) {
        switch (id) {
            case 0: // create array
                visualizer.createRandomArray(canvas.getWidth(), canvas.getHeight());
                break;
            case 1:
                visualizer.bubbleSort();
                break;
            case 2:
                visualizer.selectionSort();
                break;
            case 3:
                visualizer.insertionSort();
                break;
            case 4:
                visualizer.quickSort();
                break;
            case 5:
                visualizer.mergeSort();
                break;
        }
    }

    public void onArraySorted(long elapsedTime, int swapping)
    {
        timeLabel.setText("Elapsed Time: " + (int)(elapsedTime/1000.0) + " µs");	
        swapLabel.setText("Swaps: " + swapping);
    }
    
    public BufferStrategy getBufferStrategy() {
        BufferStrategy bs = canvas.getBufferStrategy();
        if (bs == null) {
            canvas.createBufferStrategy(2);
            bs = canvas.getBufferStrategy();
        }
        return bs;
    }
}

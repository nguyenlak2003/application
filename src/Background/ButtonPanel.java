
package Background;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class ButtonPanel extends JPanel 
{
    private static final int ButtonWidth = 200, ButtonHeight = 80;
    private JLabel[] buttons;
    private final int number = 6;
    private ButtonListener listener;
    
    public ButtonPanel(ButtonListener listener)
    {
        super();
        this.listener = listener; 
        
        buttons = new JLabel[number];
        for(int i = 0; i < buttons.length; i++) 
            buttons[i] = new JLabel();
        
        
        // initialize buttons
        initAnimationButton(buttons[0], "create_button", 0);
        initAnimationButton(buttons[1], "bubble_button", 1);
        initAnimationButton(buttons[2], "selection_button", 2);
        initAnimationButton(buttons[3], "insertion_button", 3);
        initAnimationButton(buttons[4], "quick_button", 4);
        initAnimationButton(buttons[5], "merge_button", 5);
        
        // add buttons to the panel
        this.setLayout(null);
        for (int i = 0; i < buttons.length; i++)
        {
            buttons[i].setBounds(20, 20 + (ButtonHeight + 5) * i, ButtonWidth, ButtonHeight);
            this.add(buttons[i]);
        }
    }
    
    private void initAnimationButton(JLabel button, String name, int id)
    {
        button.setIcon(new ImageIcon(String.format("src/buttons/%s.png", name)));
        button.addMouseListener(new MouseListener() {
            public void mouseClicked(MouseEvent e) {}

            public void mousePressed(MouseEvent e)
            {
                button.setIcon(new ImageIcon(String.format("src/buttons/%s_pressed.png", name)));
            }

            public void mouseReleased(MouseEvent e)
            {
                listener.sortButtonClicked(id);
                button.setIcon(new ImageIcon(String.format("src/buttons/%s_entered.png", name)));
            }

            public void mouseEntered(MouseEvent e)
            {
                button.setIcon(new ImageIcon(String.format("src/buttons/%s_entered.png", name)));
            }

            public void mouseExited(MouseEvent e)
            {
                button.setIcon(new ImageIcon(String.format("src/buttons/%s.png", name)));
            }   
        });
    }
    
    public interface ButtonListener
    {
        void sortButtonClicked(int id);
    }
    
}

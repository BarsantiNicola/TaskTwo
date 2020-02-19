package graphicInterface;

import java.awt.Color;
import java.awt.Component;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.border.LineBorder;

public class ImageRenderer extends JLabel implements ListCellRenderer<Image> {
	
	public ImageRenderer() { 
        setOpaque(true); 
    } 
    
    public Component getListCellRendererComponent(JList<? extends Image> list, Image image, int index, 
            boolean isSelected, boolean cellHasFocus) { 
 
    
        ImageIcon imageIcon = new ImageIcon(image.getScaledInstance(155, 115, Image.SCALE_SMOOTH));
 
        setIcon(imageIcon); 
        
        setBorder(new LineBorder(Color.WHITE,2,true));
        
        if (isSelected) { 
            setBackground(list.getSelectionBackground()); 
            setForeground(list.getSelectionForeground()); 
        } else { 
            setBackground(list.getBackground()); 
            setForeground(list.getForeground()); 
        } 
        
        return this; 
    } 
}

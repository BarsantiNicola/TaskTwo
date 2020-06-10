package graphicInterface;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Image;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon; 
import javax.swing.JLabel; 
import javax.swing.JList; 
import javax.swing.ListCellRenderer;
import javax.swing.border.LineBorder;

import logic.data.BufferedGame; 

public class BufferedGameRenderer extends JLabel implements ListCellRenderer<BufferedGame> { 
 
    public BufferedGameRenderer() { 
        setOpaque(true); 
    } 
    
    public Component getListCellRendererComponent(JList<? extends BufferedGame> list, BufferedGame game, int index, 
            boolean isSelected, boolean cellHasFocus) { 
 
        String gameTitle = game.getTitle(); 
        
        setIcon(game.getPreviewPic()); 
        setToolTipText(gameTitle); 
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

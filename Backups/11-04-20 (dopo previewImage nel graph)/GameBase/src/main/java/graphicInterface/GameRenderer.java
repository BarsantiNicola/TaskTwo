package graphicInterface;

import java.awt.Color;
import java.awt.Component;
import java.awt.Image;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon; 
import javax.swing.JLabel; 
import javax.swing.JList; 
import javax.swing.ListCellRenderer;
import javax.swing.border.LineBorder;

import logic.data.PreviewGame; 

/** 
 * Custom renderer to display a country's flag alongside its name 
 * 
 * @author wwww.codejava.net 
 */

public class GameRenderer extends JLabel implements ListCellRenderer<PreviewGame> { 
 
    public GameRenderer() { 
        setOpaque(true); 
    } 
    
    public Component getListCellRendererComponent(JList<? extends PreviewGame> list, PreviewGame game, int index, 
            boolean isSelected, boolean cellHasFocus) { 
 
        String gameTitle = game.getTitle(); 
        ImageIcon imageIcon = null;
        
        try {
        	imageIcon = new ImageIcon(ImageIO.read(new URL(game.getPreviewPicURL())).getScaledInstance(60, 60, Image.SCALE_SMOOTH));
        } catch ( Exception e) {
        	imageIcon = new ImageIcon(new javax.swing.ImageIcon(getClass().getResource("/resources/defaultGameBackground.png")).getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH));
        }
 
        setIcon(imageIcon); 
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
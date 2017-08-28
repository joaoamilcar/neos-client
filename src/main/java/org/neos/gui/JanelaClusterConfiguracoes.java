package org.neos.gui;

import java.awt.Color;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JSlider;

import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.view.mxGraphView;

@SuppressWarnings("serial")
public class JanelaClusterConfiguracoes extends JFrame {
	private mxGraphComponent grafoComponente; 		// atributos para a criação dos clusters
	private GridLayout grid1;
	private JCheckBox checkBox;
	private JButton botaoZoomEnquadra;
	private JSlider slider; //http://alvinalexander.com/java/jwarehouse/argouml/src_new/org/argouml/ui/ZoomSliderButton.java.shtml
							//http://docs.oracle.com/javase/tutorial/uiswing/components/slider.html
	private int tamanhoSlider;
	
	public JanelaClusterConfiguracoes(mxGraphComponent grafoComponente, JSlider slider) {
		this.grafoComponente = grafoComponente;
		this.slider = slider;
		
		grid1 = new GridLayout(2, 2, 70, 5); //configura o leiaute
		checkBox = new JCheckBox(String.valueOf(grafoComponente.isGridVisible()));
		checkBox.setBackground(Color.WHITE);
		checkBox.setSelected(grafoComponente.isGridVisible());
		
		botaoZoomEnquadra = new JButton("Fit");
		botaoZoomEnquadra.setBackground(Color.WHITE);
		
		ButtonHandler handlerButton = new ButtonHandler();
		botaoZoomEnquadra.addActionListener(handlerButton);
		CheckBoxHandler handlerCheckBox = new CheckBoxHandler();
		checkBox.addItemListener(handlerCheckBox);
		
		Container container = getContentPane(); //obtem o painel de conteudo
		container.setLayout(grid1);
		container.add(new JLabel("Grid Visible"));
		container.add(checkBox);
		container.add(new JLabel("Zoom"));
		container.add(botaoZoomEnquadra);
		container.setBackground(Color.WHITE);
		
		this.pack();
		this.setLocationRelativeTo(null);
		this.setResizable(false);
		this.setTitle("Cluster Settings");
		this.setVisible(true);
	}
	
	private void zoomEnquadra() {
		int viewDimensao, componenteDimensao;
		mxGraphView view = grafoComponente.getGraph().getView();				
		
		if ((int) view.getGraphBounds().getHeight() >= (int) view.getGraphBounds().getWidth()) {
			viewDimensao = (int) view.getGraphBounds().getHeight();
			componenteDimensao = grafoComponente.getHeight();
		} else {
			viewDimensao = (int) view.getGraphBounds().getWidth();
			componenteDimensao = grafoComponente.getWidth();
		}
		
		view.setScale((double) componenteDimensao/viewDimensao * view.getScale());
		
		
		/*double newScale = 1;

        Dimension graphSize = grafoComponente.getGraphControl().getSize();
        Dimension viewPortSize = grafoComponente.getViewport().getSize();

        int gw = (int) graphSize.getWidth();
        int gh = (int) graphSize.getHeight();

        if (gw > 0 && gh > 0) {
            int w = (int) viewPortSize.getWidth();
            int h = (int) viewPortSize.getHeight();

            newScale = Math.min((double) w / gw, (double) h / gh);
        }

        grafoComponente.zoom(newScale);*/
		
        
        /*Dimension graphSize = grafoComponente.getGraphControl().getSize();
        Dimension viewPortSize = grafoComponente.getViewport().getSize();

        int x = graphSize.width/2 - viewPortSize.width/2;
        int y = graphSize.height/2 - viewPortSize.height/2;
        int w = viewPortSize.width;
        int h = viewPortSize.height;

        grafoComponente.getGraphControl().scrollRectToVisible(new Rectangle(x, y, w, h));*/
	}
	
	private class ButtonHandler implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == botaoZoomEnquadra) {
				zoomEnquadra();
				                
				slider.setValue(tamanhoSlider/2);
			}
		}
	}
	
	private class CheckBoxHandler implements ItemListener {
		@Override
		public void itemStateChanged(ItemEvent e) {
			if (e.getSource() == checkBox) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					checkBox.setText("true");
					grafoComponente.setGridVisible(true);
					grafoComponente.refresh();
				} else { //ação ao desmarcar
					checkBox.setText("false");
					grafoComponente.setGridVisible(false);
					grafoComponente.refresh();
				}
			}
		}		
	}
}

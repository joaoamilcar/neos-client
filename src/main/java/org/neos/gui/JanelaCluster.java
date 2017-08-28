package org.neos.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.neos.file.ManipuladorDeArquivo;

import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.view.mxGraph;

@SuppressWarnings("serial")
public class JanelaCluster extends JFrame {
	private mxGraphComponent grafoComponente; 		// atributos para a criação dos clusters
	private JSplitPane splitPane;
	private JScrollPane tabelaScrollPane;
	private JSlider slider; //http://alvinalexander.com/java/jwarehouse/argouml/src_new/org/argouml/ui/ZoomSliderButton.java.shtml
							//http://docs.oracle.com/javase/tutorial/uiswing/components/slider.html
	private JButton botaoZoomOriginal, botaoEngrenagem;
	private JMenuItem itemExportaPdf;
	private GridBagLayout grid1;
	private GridBagConstraints constantesLayout;
	private int valorOriginalSlider, tamanhoSlider;
	private String arquivoNome;
	
	public JanelaCluster(mxGraph grafo, JTable tabela, String arquivoNome) {
		this.arquivoNome = arquivoNome;
		tamanhoSlider = 50;
		grid1 = new GridBagLayout();
		constantesLayout = new GridBagConstraints();
		
		grafoComponente = new mxGraphComponent(grafo); // instanciando o atributo passando o grafo
		grafoComponente.setConnectable(false); // evita retirar uma ligação arrastando
		//grafoComponente.setGridVisible(true);
		grafoComponente.getViewport().setBackground(Color.WHITE);
		//grafoComponente.setPreferredSize(new Dimension(780, 510)); // tamanho do componente
		
		// Cria um scroll pane e adiciona a tabela.
		tabelaScrollPane = new JScrollPane(tabela);
		
		//Cria um split pane com dois scroll panes.
        splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, grafoComponente, tabelaScrollPane);
        splitPane.setResizeWeight(1.0d);
        splitPane.setOneTouchExpandable(true);
        splitPane.getRightComponent().setMinimumSize(new Dimension());
        splitPane.getLeftComponent().setMinimumSize(new Dimension());
        
		slider = new JSlider(JSlider.VERTICAL, 0, tamanhoSlider, tamanhoSlider/2);
		setValorOriginalSlider(slider.getValue());
		slider.setMinorTickSpacing(1);
	    slider.setMajorTickSpacing(5);
	    slider.setPaintTicks(true);
	    slider.setPaintLabels(true);
		botaoZoomOriginal = new JButton("\u25A0"); //u25A0 u07db u1680 u220e u22a1 u233c u23f9 u2588 u25a3 http://unicode-table.com/en/
		botaoZoomOriginal.setToolTipText("Zoom Actual Size");
		Icon gearIcone = new ImageIcon("resources/images/iconGear.png"); // 16x16
		botaoEngrenagem = new JButton(gearIcone);
		botaoEngrenagem.setToolTipText("Cluster Settings");
		itemExportaPdf = new JMenuItem("Export As PDF");
		
		MouseHandler mouseHandler = new MouseHandler();
		grafoComponente.getGraphControl().addMouseListener(mouseHandler);
		SliderHandler sliderHandler = new SliderHandler();
		slider.addChangeListener(sliderHandler);
		ButtonHandler handlerButton = new ButtonHandler();
		botaoZoomOriginal.addActionListener(handlerButton);
		botaoEngrenagem.addActionListener(handlerButton);
		itemExportaPdf.addActionListener(handlerButton);
		
		constantesLayout.fill = GridBagConstraints.BOTH; // redimensiona o componente quando a área de exibição for menor que o componente
		constantesLayout.weightx = 1; // pode crescer na largura
		constantesLayout.weighty = 1; // pode crescer na altura
		adicionarComponente(splitPane, 0, 0, 1, 3);
		constantesLayout.weightx = 0; // aqui ainda é constantesLayout.weighty = 1
		adicionarComponente(slider, 0, 1, 1, 1);
		constantesLayout.weighty = 0;
		adicionarComponente(botaoZoomOriginal, 1, 1, 1, 1);
		adicionarComponente(botaoEngrenagem, 2, 1, 1, 1);
		
		Container container = getContentPane();
		container.setLayout(grid1);
		//setLayout(grid1);
		
		this.setSize(800, 600);
		this.setExtendedState(JFrame.MAXIMIZED_BOTH);
		//this.setLocationRelativeTo(null);
		this.setTitle("Cluster View - " + arquivoNome);
		this.setVisible(true);
	}
	
	private void adicionarComponente(Component componente, int linha, int coluna, int largura, int altura) {
		constantesLayout.gridx = coluna;
		constantesLayout.gridy = linha;
		constantesLayout.gridwidth = largura;
		constantesLayout.gridheight = altura;
		grid1.setConstraints(componente, constantesLayout);
		add(componente); // adicionando cada componente botao ao container
	}
	
	public int getValorOriginalSlider() {
		return valorOriginalSlider;
	}

	public void setValorOriginalSlider(int valorOriginalSlider) {
		this.valorOriginalSlider = valorOriginalSlider;
	}

	private class SliderHandler implements ChangeListener {
		@Override
		public void stateChanged(ChangeEvent e) {
			int valorAtualSlider = slider.getValue();
			
			if (slider.getValue() > getValorOriginalSlider()) {
				int delta = valorAtualSlider - getValorOriginalSlider();
				
				for (int i = 0; i < delta; i++) {
					grafoComponente.zoomIn();
				}
			} else {
				int delta = getValorOriginalSlider() - valorAtualSlider;
				
				for (int i = 0; i < delta; i++) {
					grafoComponente.zoomOut();
				}
			}
			
			setValorOriginalSlider(slider.getValue());
		}
	}
	
	private class ButtonHandler implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == botaoZoomOriginal) {
				grafoComponente.zoomActual();
                
				slider.setValue(tamanhoSlider/2);
			} else if (e.getSource() == botaoEngrenagem) {
				new JanelaClusterConfiguracoes(grafoComponente, slider);
			} else if (e.getSource() == itemExportaPdf) {
				ManipuladorDeArquivo manipulador = new ManipuladorDeArquivo();
				
				try {
					manipulador.exportarGrafoPDF(grafoComponente.getGraph(), arquivoNome);
					
					String mensagem = "Graph exported to a PDF file.", titulo = "File exported";					
					JOptionPane.showMessageDialog(null, mensagem, titulo, JOptionPane.INFORMATION_MESSAGE);
				} catch (Exception e1) {
					e1.printStackTrace();
					String mensagem = "Error while exporting graph.", titulo = "File not exported";					
					JOptionPane.showMessageDialog(null, mensagem, titulo, JOptionPane.ERROR_MESSAGE);
				}
			}
		}
	}
	
	private class MouseHandler extends MouseAdapter {
		@Override
		public void mouseClicked(MouseEvent event) {
			if (event.getButton() == MouseEvent.BUTTON3) {
				final JPopupMenu popMenu = new JPopupMenu();
			    popMenu.add(itemExportaPdf);
			    popMenu.show(event.getComponent(), event.getX(), event.getY());
			}
		}
	}
}

package org.neos.gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.plaf.basic.BasicButtonUI;

@SuppressWarnings("serial")
public class ComponenteAba extends JPanel {
	private final JTabbedPane abasPanel;
	
	public ComponenteAba(final JTabbedPane panel) {
		super((LayoutManager) new FlowLayout(FlowLayout.LEFT, 0, 0)); // desconfigurar os espaços padrão do FlowLayout

		if (panel == null) {
			throw new NullPointerException("TabbedPane is null.");
		}

		this.abasPanel = panel;
		setOpaque(false);

		// faz o JLabel ler os t�tulos da JTabbedPane
		JLabel label = new JLabel() {
			public String getText() {
				int i = abasPanel.indexOfTabComponent(ComponenteAba.this);
				
				if (i != -1) {
					return abasPanel.getTitleAt(i);
				}
				
				return null;
			}
		};

		add(label);
		label.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 15)); // adiciona mais espaço entre o label e o botão
		JButton botao = new TabButton(); // botão para a aba
		add(botao);
		setBorder(BorderFactory.createEmptyBorder(2, 0, 0, 0)); // adiciona mais espaço ao topo do componente
	}
	
	public JTabbedPane getAbasPanel() {
		return abasPanel;
	}
	
	private class TabButton extends JButton implements ActionListener {
		public TabButton() {
			int tamanho = 10;
			
			setPreferredSize(new Dimension(tamanho, tamanho));
			setToolTipText("close this tab");
			setUI(new BasicButtonUI()); // faz o botao parecer o mesmo para todos os LookAndFeel
			//setContentAreaFilled(true); // deixa transparente
			//setFocusable(true); // sem necessidade de ser focado
			setBorder(BorderFactory.createEtchedBorder());
			setBorderPainted(false);
			addMouseListener(botaoMouseListener); // efeito do mouse passando (rollover) pelo botao. É usado o mesmo listener para todos os botoes
			setRolloverEnabled(true);
			addActionListener(this); // fechar a aba correspondente clicando o botao
		}

		public void actionPerformed(ActionEvent e) {
			int i = abasPanel.indexOfTabComponent(ComponenteAba.this);
			
			if (i != -1) {
				abasPanel.remove(i);
			}
		}
		
		/*
		// não queremos atualizar a UI para este botao
		public void updateUI() {
		}*/

		// colorir o X
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			Graphics2D g2d = (Graphics2D) g.create();
			
			// desloca a imagem ao pressionar o botao
			if (getModel().isPressed()) {
				g2d.translate(1, 1);
			}
			
			g2d.setStroke(new BasicStroke(2));
			g2d.setColor(Color.BLACK);
			
			if (getModel().isRollover()) {
				g2d.setColor(Color.RED);
			}
			
			int delta = 6;
			g2d.drawLine(delta, delta, getWidth() - delta - 1, getHeight() - delta - 1);
			g2d.drawLine(getWidth() - delta - 1, delta, delta, getHeight() - delta - 1);
			g2d.dispose();
		}
	}
	
	private final static MouseListener botaoMouseListener = new MouseAdapter() {
		public void mouseEntered(MouseEvent e) {
			Component componente = e.getComponent();
			
			if (componente instanceof AbstractButton) {
				AbstractButton botao = (AbstractButton) componente;
				botao.setBorderPainted(true);
			}
		}

		public void mouseExited(MouseEvent e) {
			Component componente = e.getComponent();
			
			if (componente instanceof AbstractButton) {
				AbstractButton botao = (AbstractButton) componente;
				botao.setBorderPainted(false);
			}
		}
	};
}

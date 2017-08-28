package org.neos.gui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;

import org.neos.server.Servidor;

@SuppressWarnings("serial")
public class JanelaConfiguracaoServidor extends JFrame {
	private JPanel gridPanel1, botoesPanel;; // conteiner em que os componentes podem ser alocados
	private JButton botaoOK, botaoCancelar;
	private GridLayout grid1;
	private JLabel[] labels;
	private String[] descricaoLabels = {"Enter NEOS Server Host:", "Enter NEOS Server Port:"};
	private JTextField[] textFields;
	private JMenu jobMenu;
	private JTabbedPane abasPanel;
	private Servidor servidor;
	
	public JanelaConfiguracaoServidor(Servidor servidor) {
		this.servidor = servidor;
		
		gridPanel1 = new JPanel();
		grid1 = new GridLayout(2, 2, 5, 5); //configura o leiaute
		gridPanel1.setLayout(grid1);
		botoesPanel = new JPanel(); //configura o painel e estabelece o layout
		
		labels = new JLabel[descricaoLabels.length];
		textFields = new JTextField[descricaoLabels.length];
		
		for (int count = 0; count < descricaoLabels.length; count++) {
			labels[count] = new JLabel(descricaoLabels[count]);
			textFields[count] = new JTextField();
			gridPanel1.add(labels[count]);
			gridPanel1.add(textFields[count]);
		}
		
		textFields[0].setText(servidor.getHost());
		textFields[1].setText(servidor.getPorta());
		
		//cria e adiciona botoes
		botaoOK = new JButton("OK");
		botaoCancelar = new JButton("Cancel");		
		
		ButtonHandler handlerButton = new ButtonHandler();
		//pode configurar um laço também para estes botões
		botaoOK.addActionListener(handlerButton);
		botaoCancelar.addActionListener(handlerButton);
		
		botoesPanel.add(botaoOK);
		botoesPanel.add(botaoCancelar);
		
		Container container = getContentPane(); //obtem o painel de conteudo		
		container.setLayout(new BorderLayout(5, 5));
		container.add(gridPanel1, BorderLayout.CENTER);
		container.add(botoesPanel, BorderLayout.SOUTH);
		
		this.setSize(260, 110);
		//this.pack(); //gerenciador de leiaute se encarrega de ajustar o tamanho da janela
		this.setLocationRelativeTo(null);
		this.setResizable(false);
		this.setTitle("Connection Settings");
		this.setVisible(true);
	}

	public JMenu getJobMenu() {
		return jobMenu;
	}

	public void setJobMenu(JMenu jobMenu) {
		this.jobMenu = jobMenu;
	}

	public JTabbedPane getAbasPanel() {
		return abasPanel;
	}

	public void setAbasPanel(JTabbedPane abasPanel) {
		this.abasPanel = abasPanel;
	}
	
	private class ButtonHandler implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == botaoOK) {
				servidor.setHost(textFields[0].getText());
				servidor.setPorta(textFields[1].getText());
				
				dispose();
			} else if (e.getSource() == botaoCancelar) {
				dispose();
			}
		}		
	}
}

package org.neos.gui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;

import org.neos.client.Cliente;
import org.neos.file.ManipuladorDeArquivo;

@SuppressWarnings("serial")
public class JanelaJobSenha extends JFrame {
	private JPanel gridPanel1, botoesPanel;; // conteiner em que os componentes podem ser alocados
	private String titulo;
	private JButton botaoOK, botaoCancelar;
	private GridLayout grid1;
	private JLabel labels[];
	private String[] descricaoLabels = {"Enter Job Number: #", "Enter Password:"};
	private JTextField[] textFields;
	private Cliente cliente;
	private JTabbedPane abasPanel;
	
	public JanelaJobSenha(String titulo, Cliente cliente, JTabbedPane abasPanel) {
		this.titulo = titulo;
		this.cliente = cliente;
		this.abasPanel = abasPanel;
		
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
		
		textFields[0].setText(cliente.getCurrentJobNumber().toString());
		textFields[1].setText(cliente.getCurrentPassword());
		
		//cria e adiciona botoes
		botaoOK = new JButton("OK");
		botaoCancelar = new JButton("Cancel");
		
		ButtonHandler handlerButton = new ButtonHandler();
		//pode configurar um laco tambem para estes botoes
		botaoOK.addActionListener(handlerButton);
		botaoCancelar.addActionListener(handlerButton);
		
		botoesPanel.add(botaoOK);
		botoesPanel.add(botaoCancelar);
		
		Container container = getContentPane(); //obtem o painel de conteudo		
		container.setLayout(new BorderLayout(5, 5));
		container.add(gridPanel1, BorderLayout.CENTER);
		container.add(botoesPanel, BorderLayout.SOUTH);
		
		//this.setSize(283, 225);
		this.pack(); //gerenciador de leiaute se encarrega de ajustar o tamanho da janela
		this.setLocationRelativeTo(null);
		this.setResizable(false);
		this.setTitle(titulo);
		this.setVisible(true);
	}
	
	private class ButtonHandler implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == botaoOK) {
				Integer jobNumber = Integer.valueOf(textFields[0].getText());
				String password = textFields[1].getText();
				
				if (titulo == "Retrieve Job") {
					String currentJobResult = cliente.getResult(jobNumber, password);
					
					if (cliente.getCurrentJobStatus() == "done") {
						new ManipuladorDeArquivo().exibirConteudo(currentJobResult, "Job#" + jobNumber.toString(), abasPanel);
					} else if (cliente.getCurrentJobStatus() == "error") {
						String texto = "Default message: " + cliente.getCurrentJobStatus(), titulo = "Job Status";
						JOptionPane.showMessageDialog(null, texto, titulo, JOptionPane.ERROR_MESSAGE);
					} else {
						String texto = "Default message: " + cliente.getCurrentJobStatus(), titulo = "Job Status";
						JOptionPane.showMessageDialog(null, texto, titulo, JOptionPane.INFORMATION_MESSAGE);
					}
					
				} else if (titulo == "Kill Job") {
					
				} else if (titulo == "Job Info") {
					
				}
				
				dispose();
			} else if (e.getSource() == botaoCancelar) {
				dispose();
			}
		}		
	}
}

package org.neos.gui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.MutableComboBoxModel;

import org.apache.xmlrpc.XmlRpcException;
import org.neos.client.Cliente;
import org.neos.exception.ExcecaoNeosXML;
import org.neos.parser.AnalisadorTemplateXML;

@SuppressWarnings({"serial", "rawtypes", "unchecked"})
public class JanelaNovaJob extends JFrame {
	private JPanel gridPanel1, botoesPanel;; // conteiner em que os componentes podem ser alocados
	private JButton botaoOK, botaoCancelar;
	private GridLayout grid1;
	private JLabel[] labels;
	private String[] descricaoLabels = { "Choose Problem Type: ", "Select Appropriate Solver and Input Format: " };
	private JComboBox[] comboBoxes;
	private MutableComboBoxModel[] models;
	private Cliente cliente;
	private JTabbedPane abasPanel;
	private HashMap<?, ?> categorias;
	
	public JanelaNovaJob(Cliente cliente, JTabbedPane abasPanel) throws XmlRpcException {
		this.cliente = cliente;
		this.abasPanel = abasPanel;
		gridPanel1 = new JPanel();
		grid1 = new GridLayout(descricaoLabels.length, 2, 5, 5); //configura o leiaute
		gridPanel1.setLayout(grid1);
		botoesPanel = new JPanel(); //configura o painel e estabelece o layout
		
		labels = new JLabel[descricaoLabels.length];
		comboBoxes = new JComboBox[descricaoLabels.length];
		models = new MutableComboBoxModel[descricaoLabels.length];
		
		ComboBoxHandler handlerComboBox = new ComboBoxHandler();
		
		for (int count = 0; count < descricaoLabels.length; count++) {
			labels[count] = new JLabel(descricaoLabels[count]);
			comboBoxes[count] = new JComboBox();
			comboBoxes[count].addItemListener(handlerComboBox);
			models[count] = (MutableComboBoxModel) comboBoxes[count].getModel();
			gridPanel1.add(labels[count]);
			gridPanel1.add(comboBoxes[count]);
		}
		
		categorias = cliente.listCategories();
		
		Iterator iteradorValoresCategorias = categorias.values().iterator();

		while (iteradorValoresCategorias.hasNext()) {
			String categoria = (String) iteradorValoresCategorias.next();

			if (categoria.compareTo("kestrel") != 0 && categoria.compareTo("Model Analyzers") != 0
					&& categoria.compareTo("Multi-Solvers") != 0) {
				models[0].addElement(categoria);
			}
		}

		botaoOK = new JButton("OK");
		botaoCancelar = new JButton("Cancel");

		ButtonHandler handlerButton = new ButtonHandler();
		botaoOK.addActionListener(handlerButton);
		botaoCancelar.addActionListener(handlerButton);

		botoesPanel.add(botaoOK);
		botoesPanel.add(botaoCancelar);

		Container container = getContentPane();
		container.setLayout(new BorderLayout(5, 5));
		container.add(gridPanel1, BorderLayout.CENTER);
		container.add(botoesPanel, BorderLayout.SOUTH);

		//this.setSize(250, 110);
		this.pack();
		this.setLocationRelativeTo(null);
		this.setResizable(false);
		this.setTitle("Parameters Setting");
		this.setVisible(true);
	}
	
	private void removeTodosOsElementos(MutableComboBoxModel model) {
		int size = model.getSize();
	    
		for (int i = 0; i < size; i++) {
			Object item = model.getElementAt(0);
			model.removeElement(item);
		}
	}
	
	private class ComboBoxHandler implements ItemListener {
		@Override
		public void itemStateChanged(ItemEvent e) {
			if (e.getStateChange() == ItemEvent.SELECTED) {
				if (e.getSource() == comboBoxes[0]) {
					removeTodosOsElementos(models[1]);
					String categoria = (String) e.getItem();
					
					try {
						Object[] results = cliente.listSolversInCategory(categoria);
						
						for (int i = 0; i < results.length; i++) {
							models[1].addElement(results[i]);
						}
					} catch (XmlRpcException e1) {
						e1.printStackTrace();
						String texto = "Error while listing matched solver and input format.", titulo = "Solver:Input";
						JOptionPane.showMessageDialog(null, texto, titulo, JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		}
	}
	
	private class ButtonHandler implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == botaoOK) {
				String categoryFullName = comboBoxes[0].getSelectedItem().toString();
				String categoryAbbreviatedName = "";
				String[] setupComboBox1 = comboBoxes[1].getSelectedItem().toString().split(":");
				String solver = setupComboBox1[0];
				String inputMethod = setupComboBox1[1];
				
				for (Entry<?, ?> string : categorias.entrySet()) {
					if(string.getValue().equals(categoryFullName))
						categoryAbbreviatedName = (String) string.getKey();
				}
				
				try {
					String solverTemplate = cliente.getSolverTemplate(categoryAbbreviatedName, solver, inputMethod);
					
					AnalisadorTemplateXML analisador = new AnalisadorTemplateXML();
					analisador.parse(solverTemplate);
					
					if (inputMethod.equals("AMPL") || inputMethod.equals("default")) {
						new JanelaSubmissaoAMPL(cliente, analisador, abasPanel);
						
						dispose();
					} else {
						String texto = inputMethod + " is not an input format available for this NEOS Client version.", titulo = "Input Format Unavailable";
		            	JOptionPane.showMessageDialog(null, texto, titulo, JOptionPane.INFORMATION_MESSAGE);
					}
				} catch (ExcecaoNeosXML e1) {
					e1.printStackTrace(); //parsing
					//String texto = "Error while parsing solver template.", titulo = "Template parsing";
					//JOptionPane.showMessageDialog(null, texto, titulo, JOptionPane.ERROR_MESSAGE);
				} catch (XmlRpcException e1) {
					e1.printStackTrace(); //getsolvertemplate
					//String texto = "Error while retrieving solver template.", titulo = "Template retrieving";
					//JOptionPane.showMessageDialog(null, texto, titulo, JOptionPane.ERROR_MESSAGE);
				}
			} 
			
			if (e.getSource() == botaoCancelar) {
				dispose();
			}
		}		
	}
}

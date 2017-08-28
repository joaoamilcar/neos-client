package org.neos.parser;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.neos.client.FileUtils;
import org.w3c.dom.Node;

public class ComponenteArquivo extends AComponente {
	private JTextField campoTexto;
	private JButton botaoBrowse;
	private static File ultimoArquivo = null;
	
	public ComponenteArquivo(Node node) {
		super(node);
		// TODO Auto-generated constructor stub
	}

	@Override
	public JPanel construirPanel() {
		JPanel panel = new JPanel();
		campoTexto = new JTextField(30);
		botaoBrowse = new JButton("Browse");
		ButtonHandler handlerButton = new ButtonHandler();
		botaoBrowse.addActionListener(handlerButton);
		
		panel.add(campoTexto);
		panel.add(botaoBrowse);
		
		return panel;
	}

	@Override
	public String getConteudoComponente() throws IOException {
		//create FileUtils object to facilitate reading model file ChemEq.txt into a string called example
		FileUtils fileUtils = FileUtils.getInstance(FileUtils.APPLICATION_MODE);
		String conteudoArquivo = fileUtils.readFile(campoTexto.getText());

		return conteudoArquivo;
	}
	
	private class ButtonHandler implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == botaoBrowse) {
				JFileChooser chooser = new JFileChooser();
				
				if (campoTexto.getText().length() > 0) {
					chooser.setCurrentDirectory(new File(campoTexto.getText()));
				} else {
					chooser.setCurrentDirectory(ultimoArquivo);
				}
				
				int valor = chooser.showOpenDialog(botaoBrowse);
				
				if (valor == 0) {
					ultimoArquivo = chooser.getSelectedFile();
					campoTexto.setText(chooser.getSelectedFile().getAbsolutePath());
				}
			}
		}		
	}
}

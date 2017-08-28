package org.neos.gui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import org.apache.xmlrpc.XmlRpcException;
import org.neos.client.Cliente;
import org.neos.file.ManipuladorDeArquivo;
import org.neos.parser.AComponente;
import org.neos.parser.AnalisadorTemplateXML;

@SuppressWarnings("serial")
public class JanelaSubmissaoAMPL extends JFrame {
	private JPanel gridPanel1, botoesPanel;
	private GridLayout grid1;
	//private GridBagConstraints constantesLayout;
	//private JTextArea areaTexto;
	private JButton botaoSubmit, botaoCancel;
	private Cliente cliente;
	private AnalisadorTemplateXML analisador;
	private JTabbedPane abasPanel;
	
	public JanelaSubmissaoAMPL(Cliente cliente, AnalisadorTemplateXML analisador, JTabbedPane abasPanel) {
		this.cliente = cliente;
		this.analisador = analisador;
		this.abasPanel = abasPanel;
		
		ArrayList<AComponente> listaComponentes = analisador.getListaComponentes();
		
		Container container = getContentPane();
		container.setLayout(new BorderLayout(5, 5));
		gridPanel1 = new JPanel();
		grid1 = new GridLayout(listaComponentes.size(), 1, 5, 5); //configura o leiaute
		//constantesLayout = new GridBagConstraints();
		gridPanel1.setLayout(grid1);
		botoesPanel = new JPanel(); //configura o painel e estabelece o layout
		
		for (int count = 0; count < listaComponentes.size(); count++) {
			JPanel panel = listaComponentes.get(count).construirPanel();
			panel.setBorder(BorderFactory.createTitledBorder(listaComponentes.get(count).getToken()));
			gridPanel1.add(panel);
		}
		
		botaoSubmit = new JButton("Submit");
		botaoCancel = new JButton("Cancel");
		ButtonHandler handlerButton = new ButtonHandler();
		botaoSubmit.addActionListener(handlerButton);
		botaoCancel.addActionListener(handlerButton);

		botoesPanel.add(botaoSubmit);
		botoesPanel.add(botaoCancel);
		
		container.add(gridPanel1, BorderLayout.CENTER);
		container.add(botoesPanel, BorderLayout.SOUTH);
		
		//this.setSize(350, 110);
		this.pack();
		this.setLocationRelativeTo(null);
		this.setResizable(false);
		this.setTitle("Submission - " + analisador.getSolver());
		this.setVisible(true);
	}
	
	private class ButtonHandler implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == botaoSubmit) {
				try {
					String documentoXML = analisador.construirDocumento();
					
					cliente.submitJob(documentoXML);
					Integer job = cliente.getCurrentJobNumber();
					String password = cliente.getCurrentPassword();
					
					Calendar calendar = Calendar.getInstance();
			        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
					
					ManipuladorDeArquivo manipuladorDeArquivo = new ManipuladorDeArquivo();
					manipuladorDeArquivo.exibirConteudo(
							"Submitted at " + sdf.format(calendar.getTime())
									+ "\n\nJob Number : " + job.toString() + "\nPassword : " + password + "\n\n",
							"Job#" + job.toString(), abasPanel);
				} catch (IOException e1) {
					e1.printStackTrace();
					String texto = "Error while building XML template.", titulo = "Job submission";
					JOptionPane.showMessageDialog(null, texto, titulo, JOptionPane.ERROR_MESSAGE);
				} catch (XmlRpcException e1) {
					e1.printStackTrace();
					String texto = "Error while submitting job.", titulo = "Job submission";
					JOptionPane.showMessageDialog(null, texto, titulo, JOptionPane.ERROR_MESSAGE);
				}
				
				dispose();
			}
			
			if (e.getSource() == botaoCancel) {
				dispose();
			}
		}		
	}
}

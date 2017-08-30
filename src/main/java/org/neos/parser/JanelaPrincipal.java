package org.neos.parser;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.NoSuchElementException;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;

import org.apache.xmlrpc.XmlRpcException;
import org.neos.client.Cliente;
import org.neos.cluster.Dataset;
import org.neos.cluster.ManipuladorDeCluster;
import org.neos.cluster.ManipuladorDeClusterCentroid;
import org.neos.cluster.ManipuladorDeClusterPMedian;
import org.neos.cluster.view.VisualizadorDeCluster;
import org.neos.cluster.view.VisualizadorDeClusterCentroid;
import org.neos.cluster.view.VisualizadorDeClusterMinMax;
import org.neos.cluster.view.VisualizadorDeClusterPMedian;
import org.neos.file.ManipuladorDeArquivo;
import org.neos.gui.JanelaAjuda;
import org.neos.gui.JanelaCluster;
import org.neos.gui.JanelaConfiguracaoServidor;
import org.neos.gui.JanelaJobSenha;
import org.neos.gui.JanelaNovaJob;
import org.neos.server.Servidor;

@SuppressWarnings("serial")
public class JanelaPrincipal extends JFrame {
	private JMenuBar menuBarra; // barra de menu
	private JMenu arquivoMenu, neosMenu, jobMenu, viewMenu, ajudaMenu; // opcoes para a barra de menu
	private JMenuItem abrirAcao, salvarAcao, sairAcao, novaAcao, recuperarAcao, matarAcao, infoAcao, filaAcao, clusterAcao, conectarAcao, testarConexaoAcao, neosConfiguracoesAcao, ajudaClasseAcao, ajudaEmailAcao, sobreAcao; // itens de cada opcao do menu
	private JTabbedPane abasPanel;
	private Cliente cliente;
	private Servidor servidor;
	private ManipuladorDeArquivo manipuladorArquivo;
	private static File ultimoArquivoAberto = null;
	
	public JanelaPrincipal() {
		servidor = new Servidor("neos-server.org", "3333");
		manipuladorArquivo = new ManipuladorDeArquivo();
		
		// container eh uma coleção de componentes relacionados. Em aplicativos JFrames, anexamos componentes ao painel de conteudo
        Container container = getContentPane(); // obtem o painel de conteudo
				
		abasPanel = new JTabbedPane();
		abasPanel.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT); // layout com barra de rolagem horizontal
		
		container.add(abasPanel, BorderLayout.CENTER); // adicionando cada componente botao ao container
		
		menuBarra = new JMenuBar(); // cria barra de menu
		setJMenuBar(menuBarra); // adiciona a barra de menu ao frame
		
		// cria opcoes para a barra de menu
        arquivoMenu = new JMenu("File");
        neosMenu = new JMenu("Server");
        jobMenu = new JMenu("Job");
		jobMenu.setEnabled(false);
		viewMenu = new JMenu("View");
		ajudaMenu = new JMenu("Help");
		arquivoMenu.setMnemonic(KeyEvent.VK_F);
		neosMenu.setMnemonic(KeyEvent.VK_N);
		jobMenu.setMnemonic(KeyEvent.VK_J);
		viewMenu.setMnemonic(KeyEvent.VK_V);
		ajudaMenu.setMnemonic(KeyEvent.VK_H);
        
        // adiciona as opcoes na barra de menu
        menuBarra.add(arquivoMenu); 
        menuBarra.add(neosMenu);
        menuBarra.add(jobMenu);
        menuBarra.add(viewMenu);
        menuBarra.add(ajudaMenu);
        
        // Cria itens de opcoes de menu
        abrirAcao = new JMenuItem("Open");
        salvarAcao = new JMenuItem("Save");
        sairAcao = new JMenuItem("Exit");
        novaAcao = new JMenuItem("New");
        recuperarAcao = new JMenuItem("Retrieve");
        matarAcao = new JMenuItem("Kill");
        infoAcao = new JMenuItem("Info");
        filaAcao = new JMenuItem("Queue");
        clusterAcao = new JMenuItem("Cluster");
        conectarAcao = new JMenuItem("Connect");
        testarConexaoAcao = new JMenuItem("Test Connection");
        neosConfiguracoesAcao = new JMenuItem("Settings");
        ajudaClasseAcao = new JMenuItem("NEOS Server Class");
        ajudaClasseAcao.setEnabled(false);
        ajudaEmailAcao = new JMenuItem("Email Users");
        ajudaEmailAcao.setEnabled(false);
        sobreAcao = new JMenuItem("About NEOS Client");
        
        // Adiciona os itens às opções de menu
        arquivoMenu.add(abrirAcao);
        arquivoMenu.add(salvarAcao);
        arquivoMenu.add(sairAcao);
        neosMenu.add(conectarAcao);
        neosMenu.addSeparator();
        neosMenu.add(testarConexaoAcao);
        neosMenu.add(neosConfiguracoesAcao);
        jobMenu.add(novaAcao);
        jobMenu.add(recuperarAcao);
        jobMenu.add(matarAcao);
        jobMenu.add(infoAcao);
        jobMenu.addSeparator();
        jobMenu.add(filaAcao);
        viewMenu.add(clusterAcao);
        ajudaMenu.add(ajudaClasseAcao);
        ajudaMenu.add(ajudaEmailAcao);
        ajudaMenu.addSeparator();
        ajudaMenu.add(sobreAcao);
        
		abrirAcao.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser = new JFileChooser(); // new JFileChooser("C:/");
				fileChooser.setCurrentDirectory(ultimoArquivoAberto);
				fileChooser.showOpenDialog(abrirAcao);
				File arquivo = fileChooser.getSelectedFile();
				
				if (!arquivo.equals(null)) {
					ultimoArquivoAberto = arquivo;	
				}
				
				try {
					manipuladorArquivo.lerEExibirConteudoDoArquivo(arquivo, abasPanel);
				} catch (FileNotFoundException | NullPointerException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
        
        salvarAcao.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {  
            	//JFileChooser fileChooser = new JFileChooser();
				//fileChooser.setSelectedFile(new File(".clu"));
				//fileChooser.showSaveDialog(salvarAcao);
				//File arquivo = fileChooser.getSelectedFile();
            	
            	int selIndex = abasPanel.getSelectedIndex();
            	System.out.println(selIndex);
				//ManipuladorDeArquivo.salvarConteudoParaArquivo(abasPanel, indiceAba);
            }  
        });
        
        sairAcao.addActionListener(new ActionListener() {   
            public void actionPerformed(ActionEvent e) {  
            	Object[] options = {"Yes", "No"};
            	String texto = "Exit NEOS Client?", titulo = "Confirm Exit";
				int i = JOptionPane.showOptionDialog(null, texto, titulo, JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
				
				if (i == JOptionPane.YES_OPTION) {
					System.exit(0);
				}
            }  
        });
        
        conectarAcao.addActionListener(new ActionListener() {   
			public void actionPerformed(ActionEvent e) {
				cliente = new Cliente();
				
				try {
					cliente.conectar(servidor);
					
					String mensagemWelcome = cliente.welcome();
					manipuladorArquivo.exibirConteudo(mensagemWelcome, "Welcome", abasPanel);
					
					jobMenu.setEnabled(cliente.isConectado());
					ajudaClasseAcao.setEnabled(cliente.isConectado());
					ajudaEmailAcao.setEnabled(cliente.isConectado());
				} catch (XmlRpcException ex) {
					ex.printStackTrace();
					String texto = "Connection failed.", titulo = "Connection";
					JOptionPane.showMessageDialog(null, texto, titulo, JOptionPane.ERROR_MESSAGE);
				}
			}
		});
        
        testarConexaoAcao.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					String pingMensagem = cliente.ping();
					String versaoMensagem = cliente.version();
					String mensagem = pingMensagem + versaoMensagem.concat("."), titulo = "Connection test";
					
					JOptionPane.showMessageDialog(null, mensagem, titulo, JOptionPane.INFORMATION_MESSAGE);
				} catch (XmlRpcException e1) {
					// TODO Auto-generated catch block
					String texto = "Server is unreachable.", titulo = "Connection Test";
					JOptionPane.showMessageDialog(null, texto, titulo, JOptionPane.ERROR_MESSAGE);
					//e1.printStackTrace();
				}
			}
		});
        
        neosConfiguracoesAcao.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new JanelaConfiguracaoServidor(servidor);
			}
		});
        
        novaAcao.addActionListener(new ActionListener() {   
            public void actionPerformed(ActionEvent e) {  
            	try {
					new JanelaNovaJob(cliente, abasPanel);
				} catch (XmlRpcException e1) {
					e1.printStackTrace();
					String texto = "Error while listing problem categories.", titulo = "Categories list";
					JOptionPane.showMessageDialog(null, texto, titulo, JOptionPane.ERROR_MESSAGE);
				}
            }  
        });
        
        recuperarAcao.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
        		new JanelaJobSenha("Retrieve Job", cliente, abasPanel);
            } 
        });
        
        infoAcao.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
        		new JanelaJobSenha("Job Info", cliente, abasPanel);
            } 
        });
        
        matarAcao.addActionListener(new ActionListener() {   
            public void actionPerformed(ActionEvent e) {  
        		new JanelaJobSenha("Kill Job", cliente, abasPanel);
            }  
        });
        
        filaAcao.addActionListener(new ActionListener() {   
			public void actionPerformed(ActionEvent e) {  
            	try {
					String mensagem = cliente.printQueue();
					manipuladorArquivo.exibirConteudo(mensagem, "Queue", abasPanel);
				} catch (XmlRpcException ex) {
					ex.printStackTrace();
					//String texto = "Error while retrieving jobs queue.", titulo = "Jobs queue";
					//JOptionPane.showMessageDialog(null, texto, titulo, JOptionPane.ERROR_MESSAGE);
				}
            }  
        });
        
        clusterAcao.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {  
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setCurrentDirectory(ultimoArquivoAberto);
				fileChooser.showOpenDialog(clusterAcao);
				File arquivo = fileChooser.getSelectedFile();
				
				if (!arquivo.equals(null)) {
					ultimoArquivoAberto = arquivo;	
				}
				
				try {
					Dataset dataset = manipuladorArquivo.obterInformacoesDoArquivoParaDataset(arquivo);
					String nomeArquivo = manipuladorArquivo.recuperarNomeDoArquivoSemExtensao(arquivo);
					
					switch (dataset.getAlgoritmo()) {
					case "#min-max":
						VisualizadorDeCluster visualizador1 = new VisualizadorDeClusterMinMax(dataset);
						visualizador1.templateVisualizarCluster();
						new JanelaCluster(visualizador1.getGrafo(), visualizador1.getTabela(), nomeArquivo);
						
						break;
					case "#centroid":
						ManipuladorDeCluster manipulador2 = new ManipuladorDeClusterCentroid(dataset);
						manipulador2.templateMapearCluster();
						VisualizadorDeCluster visualizador2 = new VisualizadorDeClusterCentroid(dataset);
						visualizador2.templateVisualizarCluster();
						new JanelaCluster(visualizador2.getGrafo(), visualizador2.getTabela(), nomeArquivo);
						
						break;
					case "#p-median":
						ManipuladorDeCluster manipulador3 = new ManipuladorDeClusterPMedian(dataset);
						manipulador3.templateMapearCluster();
						VisualizadorDeCluster visualizador3 = new VisualizadorDeClusterPMedian(dataset);
						visualizador3.templateVisualizarCluster();
						new JanelaCluster(visualizador3.getGrafo(), visualizador3.getTabela(), nomeArquivo);
						
						break;
					}
				} catch (FileNotFoundException | NullPointerException e1) {
					e1.printStackTrace();
					//String texto = "Could not load any file. No file selected.", titulo = "Template file missing";
					//JOptionPane.showMessageDialog(null, texto, titulo, JOptionPane.ERROR_MESSAGE);
				} catch (NoSuchElementException e2) { //InputMismatchException que extends NoSuchElementException
					e2.printStackTrace();
					String texto = "Could not load any file. File selected did not match a default cluster template.", titulo = "Template file missing";
					JOptionPane.showMessageDialog(null, texto, titulo, JOptionPane.ERROR_MESSAGE);
				}
			}
        });
        
        ajudaClasseAcao.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {  
				try {
					String texto = cliente.help();
					String titulo = "Help - NEOS Server Class";
					new JanelaAjuda(texto, titulo);
				} catch (XmlRpcException e1) {
					e1.printStackTrace();
				}
            }  
        });
        
        ajudaEmailAcao.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {  
				try {
					String texto = cliente.emailHelp();
					String titulo = "Help - Email Users";
					new JanelaAjuda(texto, titulo);
				} catch (XmlRpcException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
            }  
        });
        
        sobreAcao.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {  
        		String titulo = "About NEOS Client",
        				textoPadrao = "Released on August 26th, 2017.",
        				versaoJgraphx = "   - jgraphx version \"3.4.1.3\".",
        				versaoNeos = "   - neos version 5 (Madison).";
        				
        		JOptionPane.showMessageDialog(null, textoPadrao + "\n" + versaoJgraphx + "\n" + versaoNeos, titulo, JOptionPane.PLAIN_MESSAGE);
            }  
        });
        
        this.setSize(800, 600); // tamanho da janela
        this.setLocationRelativeTo(null); // centraliza a janela
        //this.setResizable(false);
        this.setTitle("NEOS Client"); // da o titulo a janela
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
	}
}

package org.neos.file;

import java.awt.Graphics2D;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.Scanner;

import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;

import org.neos.cluster.Cluster;
import org.neos.cluster.Dataset;
import org.neos.cluster.Ponto;
import org.neos.gui.ComponenteAba;

import com.lowagie.text.Document;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfWriter;
import com.mxgraph.canvas.mxGraphics2DCanvas;
import com.mxgraph.canvas.mxICanvas;
import com.mxgraph.util.mxCellRenderer;
import com.mxgraph.util.mxCellRenderer.CanvasFactory;
import com.mxgraph.util.mxRectangle;
import com.mxgraph.view.mxGraph;

public class ManipuladorDeArquivo {
	public Dataset obterInformacoesDoArquivoParaDataset(File arquivo) throws FileNotFoundException, NoSuchElementException {
		Dataset dataset = povoarDataset(gerarDataset(arquivo), arquivo);

		return dataset;
	}
	
	@SuppressWarnings("resource")
	private Dataset gerarDataset(File arquivo) throws FileNotFoundException, NoSuchElementException, NullPointerException {
		List<Integer> listaDeClusters = new ArrayList<Integer>();

		Scanner scanner = new Scanner(arquivo);
		scanner.useLocale(Locale.US); // interpreta a parte decimal a partir do ponto, não vírgula.
		
		String algoritmo = "";
		
		do {
			algoritmo = scanner.nextLine(); // salta tag algoritmo
		} while(
				!algoritmo.equals("#pMMDAC")
				&& !algoritmo.equals("#min-max")
				&& !algoritmo.equals("#PACH_SMD")
				&& !algoritmo.equals("#pACHSMD")
				&& !algoritmo.equals("#pACHCG")
				&& !algoritmo.equals("#CCCP")
				&& !algoritmo.equals("#centroid")
				&& !algoritmo.equals("#pgpMC")
				&& !algoritmo.equals("#p-median")
				&& !algoritmo.equals("#raw")
				&& scanner.hasNext()
		);

		while (scanner.hasNext()) {
			scanner.nextDouble();
			scanner.nextDouble();
			int grupo = scanner.nextInt();

			if (listaDeClusters.contains(grupo)) {
				continue;
			} else {
				listaDeClusters.add(grupo);
			}
		}

		Dataset dataset = new Dataset(listaDeClusters.size());

		for (Integer integer : listaDeClusters) {
			Cluster cluster = new Cluster();
			cluster.setIdCluster((int) integer);
			dataset.adicionarCluster(cluster);
		}

		return dataset;
	}
	
	private Dataset povoarDataset(Dataset dataset, File arquivo) throws FileNotFoundException, NoSuchElementException, NullPointerException {
		double coordX, coordY;
		int grupo;

		Scanner scanner = new Scanner(arquivo);
		scanner.useLocale(Locale.US); // interpreta a parte decimal a partir de um ponto, não vírgula.
		
		String algoritmo = "";
		
		do {
			algoritmo = scanner.nextLine();
		} while(
				!algoritmo.equals("#pMMDAC")
				&& !algoritmo.equals("#min-max")
				&& !algoritmo.equals("#PACH_SMD")
				&& !algoritmo.equals("#pACHSMD")
				&& !algoritmo.equals("#pACHCG")
				&& !algoritmo.equals("#CCCP")
				&& !algoritmo.equals("#centroid")
				&& !algoritmo.equals("#pgpMC")
				&& !algoritmo.equals("#p-median")
				&& !algoritmo.equals("#raw")
				&& scanner.hasNext()
		);
		
		dataset.setAlgoritmo(algoritmo);
		int contadorIdPonto = 0;

		while (scanner.hasNext()) {
			coordX = scanner.nextDouble(); // coordenada X
			coordY = scanner.nextDouble(); // coordenada Y
			grupo = scanner.nextInt(); // informação do grupo
			Ponto ponto = new Ponto(coordX, coordY, grupo);
			contadorIdPonto++;
			ponto.setId(contadorIdPonto);
			dataset.adicionarPonto(ponto);
		}

		//exibirLogArquivo(dataset);
		scanner.close();

		return dataset;
	}
	
	public void lerEExibirConteudoDoArquivo(File arquivo, JTabbedPane abasPanel) throws FileNotFoundException, NullPointerException {
		String tituloAba = arquivo.getName();
		JTextArea areaTexto = new JTextArea();
		//areaTexto.setLineWrap(true);
		//areaTexto.setWrapStyleWord(true);
		//areaTexto.setEditable(false);
		JScrollPane areaScroll = new JScrollPane(areaTexto);

		Scanner scanner = new Scanner(arquivo);

		while (scanner.hasNext()) {
			areaTexto.append(scanner.nextLine() + "\n");
		}

		abasPanel.addTab(tituloAba, areaScroll);
		abasPanel.setTabComponentAt(abasPanel.getTabCount() - 1, new ComponenteAba(abasPanel));
		scanner.close();
	}
	
	
	public void salvarConteudoParaArquivo(JTabbedPane abasPanel, int indiceAba) {
		
	}
	
	public void exibirConteudo(String mensagem, String tituloAba, JTabbedPane abasPanel) {
		JTextArea areaTexto = new JTextArea();
		areaTexto.setEditable(false);
		//areaTexto.setLineWrap(true);
		//areaTexto.setWrapStyleWord(true);
		areaTexto.append(mensagem);
		JScrollPane areaScroll = new JScrollPane(areaTexto);
		abasPanel.addTab(tituloAba, areaScroll);
		abasPanel.setTabComponentAt(abasPanel.getTabCount() - 1, new ComponenteAba(abasPanel));
	}
	
	public String recuperarNomeDoArquivoSemExtensao(File arquivo) {
		String nomeArquivo = arquivo.getName();
		int pos = nomeArquivo.lastIndexOf(".");
		
		if (pos >= 0) {
		    nomeArquivo = nomeArquivo.substring(0, pos);
		}
		
		return nomeArquivo;
	}
	
	public void exportarGrafoPDF(mxGraph grafo, String clusterNome) throws Exception {
		mxRectangle bounds = grafo.getGraphBounds();
		Document document = new Document(new Rectangle((float) (bounds.getWidth()), (float) (bounds.getHeight())));
		PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(clusterNome + ".pdf"));
		document.open();
		final PdfContentByte cb = writer.getDirectContent();

		mxGraphics2DCanvas canvas = (mxGraphics2DCanvas) mxCellRenderer.drawCells(grafo, null, 1, null,
				new CanvasFactory()	{
					public mxICanvas createCanvas(int width, int height) {
						Graphics2D g2 = cb.createGraphics(width, height);
						return new mxGraphics2DCanvas(g2);
					}
				});

		canvas.getGraphics().dispose();
		document.close();
	}
	
	@SuppressWarnings("unused")
	private void exibirLogArquivo(Dataset dataset) {
		System.out.println(dataset.getAlgoritmo()); // algoritmo
		System.out.println(dataset.getListaClusters().size()); // número de clusters
		
		for (Cluster cluster : dataset.getListaClusters()) { // cada cluster por Id
			System.out.print(cluster.getIdCluster() + " ");
		}
		
		System.out.println("\n" + dataset.getTamanhoDoDataset()); // número de pontos do dataset

		for (Cluster cluster : dataset.getListaClusters()) { // cada ponto
			for (Ponto ponto : cluster.getPontos()) {
				System.out.println(ponto.getId() + " " + ponto.getCoordX() + " " + ponto.getCoordY() + " " + ponto.getGrupo());
			}
		}
	}
}

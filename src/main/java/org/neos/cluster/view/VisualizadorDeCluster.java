package org.neos.cluster.view;

import java.awt.Color;
import java.awt.Component;
import java.util.HashMap;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import org.neos.cluster.Cluster;
import org.neos.cluster.Dataset;
import org.neos.cluster.Ligacao;

import com.mxgraph.model.mxCell;
import com.mxgraph.util.mxConstants;
import com.mxgraph.view.mxGraph;

public abstract class VisualizadorDeCluster { // atributos para a criação dos clusters https://www.vainolo.com/2011/05/13/jgraph-styles/
	protected mxGraph grafo;
	protected Object parent;
	@SuppressWarnings("rawtypes")
	protected static HashMap mapaPontos, mapaLigacoes;
	protected Dataset dataset;
	protected final String[] coresHex = { "#FF0000", "#00FF00", "#0000FF", "#01FFFE", "#FFA6FE", "#FFDB66", 
			"#010067", "#95003A", "#007DB5", "#FF00F6", "#006401", "#774D00", "#90FB92", "#0076FF", "#D5FF00",
			"#FF937E", "#6A826C", "#FF029D", "#FE8900", "#7A4782", "#7E2DD2", "#85A900", "#FF0056", "#A42400",
			"#00AE7E", "#683D3B", "#BDC6FF", "#263400", "#BDD393", "#00B917", "#9E008E", "#001544", "#C28C9F",
			"#FF74A3", "#01D0FF", "#004754", "#E56FFE", "#788231", "#0E4CA1", "#91D0CB", "#BE9970", "#968AE8",
			"#BB8800", "#43002C", "#DEFF74", "#00FFC6", "#FFE502", "#620E00", "#008F9C", "#98FF52", "#7544B1",
			"#B500FF", "#00FF78", "#FF6E41", "#005F39", "#6B6882", "#5FAD4E", "#A75740", "#A5FFD2", "#FFB167",
			"#009BFF", "#E85EBE", }; //http://stackoverflow.com/questions/1168260/algorithm-for-generating-unique-colors
	/*protected final Color[] cores = { Color.lightGray, Color.red, Color.blue,
	Color.green, Color.yellow, Color.darkGray, Color.pink,
	Color.orange, Color.cyan, Color.magenta, Color.gray };*/
	/*protected final String[] coresHex = { "#F0A3FF", "#0075DC", "993F00",		// http://en.wikipedia.org/wiki/Help:Distinguishable_colors
			"4C005C", "005C31", "2BCE48", "FFCC99", "808080", "94FFB5",
			"8F7C00", "9DCC00", "C20088", "003380", "FFA405", "FFA8BB",
			"426600", "FF0010", "5EF1F2", "00998F", "E0FF66", "740AFF",
			"990000", "FFFF00", "FF5005" };*/
	/* http://www.color-hex.com/color-names.html
	 * http://phrogz.net/css/distinct-colors.html
	 * http://en.wikipedia.org/wiki/X11_color_names
	 * http://en.wikipedia.org/wiki/List_of_colors:_A%E2%80%93F
	 * */
	
	@SuppressWarnings("rawtypes")
	public VisualizadorDeCluster(Dataset dataset) {
		setDataset(dataset);
		grafo = new mxGraph();
		mapaPontos = new HashMap();
		mapaLigacoes = new HashMap();
		parent = grafo.getDefaultParent();
		//grafo.setCellsEditable(false); // editar a legenda dos pontos
		grafo.setConnectableEdges(false); // ligar as arestas
		grafo.setAllowDanglingEdges(false); // evita arestas que não estão conectadas
		grafo.setDisconnectOnMove(false); // evita arestas que não estão conectadas
		grafo.setHtmlLabels(true); // habilita customizacao de vertice whiteSpace=wrap, que faz o texto ficar inserido no vertice
		grafo.setCellsMovable(false); // permite o arraste dos vértices
		grafo.setCellsResizable(false); // permite o redimensionamento manual dos vértices
	}
	
	public void templateVisualizarCluster() {
		begin(); // para adicionar grafos ao componente eh necessario dar um begin update
		
		for (Cluster cluster : dataset.getListaClusters()) {
			visualizarPontos(cluster);
			visualizarLigacoes(cluster);
		}
				
		end();
	}
	
	protected abstract void visualizarPontos(Cluster cluster);
	
	@SuppressWarnings("unchecked")
	private void visualizarLigacoes(Cluster cluster) {
		for (Ligacao ligacaoCluster : cluster.getLigacoes()) {
			//Color cor = getCoresPosicao(ligacaoCluster.getOrigem().getGrupo() % getCores().length);
			String corHex = getCoresHexPosicao(ligacaoCluster.getOrigem().getGrupo() % getCoresHex().length);
			
			Object pontoOrigemGrafo = mapaPontos.get(ligacaoCluster.getOrigem().getId());
			Object pontoDestinoGrafo = mapaPontos.get(ligacaoCluster.getDestino().getId());
			Object ligacaoGrafo = grafo.insertEdge(
					parent,
					null,
					null,
					pontoOrigemGrafo,
					pontoDestinoGrafo,
					mxConstants.STYLE_ENDARROW + "=" + mxConstants.NONE + ";"
					//+ mxConstants.STYLE_STROKEWIDTH + "=" + 5 + ";"
					+ mxConstants.STYLE_STROKECOLOR + "=" + corHex);
			
			mapaLigacoes.put(ligacaoCluster.getOrigem().getId() + "para" + ligacaoCluster.getDestino().getId(), ligacaoGrafo);
		}
	}
	
	@SuppressWarnings("serial")
	public JTable getTabela() {
		int contadorVertices = 0;
		String[] colunaNomes = { "Id", "X-coordinate", "Y-coordinate", "Cluster", "Color" };
		Object[][] dadosTabela = new Object[grafo.getChildCells(grafo.getDefaultParent()).length][colunaNomes.length];
		
		for (Object celulaObjeto : grafo.getChildCells(grafo.getDefaultParent())) {
			mxCell celula = (mxCell) celulaObjeto;
			
			if (celula.isVertex() && celula.getValue() != null) { // os centros geométricos em clusters CCCP são vértices que tem Label == null e não tem dimensão.
				String[] rotulo = celula.getValue().toString().split(":G");

				dadosTabela[contadorVertices][0] = rotulo[0];
				dadosTabela[contadorVertices][1] = celula.getGeometry().getX();
				dadosTabela[contadorVertices][2] = celula.getGeometry().getY();
				//dadosTabela[contadorVertices][3] = rotulo[1];
				dadosTabela[contadorVertices][4] = getCorRgbCelula(celula);
				
				contadorVertices++;
			}
		}
		
		final JTable tabela = new JTable(dadosTabela, colunaNomes);
		//tabela.setPreferredScrollableViewportSize(new Dimension(500, 70));
		//tabela.setFillsViewportHeight(true);
		
		tabela.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {  
		    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {  
		        super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);  
		        
		        if (column == 4 && getText().length() > 0) {  // a tabela tem tamanho vertices + ligacoes. Nesta tabela, os espaços vazios (correspondentes as ligacoes), tem texto tamanho 0.
		            setBackground(Color.decode(getText()));
		        } else {  
		            setBackground(null);  
		        }
		        
		        if (isSelected) {
		        	setBackground(Color.BLACK);
		        }
		        
		        return this;  
		    }  
		});
		
		return tabela;
	}
	
	public void begin() {
		this.getGrafo().getModel().beginUpdate();
	}

	public void end() {
		this.getGrafo().getModel().endUpdate();
	}
	
	private String getCorRgbCelula(mxCell celula) {
		int indexInicio = celula.getStyle().indexOf("fillColor");
		String corRgb = celula.getStyle().substring(indexInicio + 10);
		corRgb = corRgb.substring(0, 7);

		return corRgb;
	}
	
	public mxGraph getGrafo() {
		return grafo;
	}

	public void setGrafo(mxGraph grafo) {
		this.grafo = grafo;
	}

	public Object getParent() {
		return parent;
	}

	public void setParent(Object parent) {
		this.parent = parent;
	}

	public Dataset getDataset() {
		return dataset;
	}

	public void setDataset(Dataset dataset) {
		this.dataset = dataset;
	}

	public String[] getCoresHex() {
		return coresHex;
	}
	
	public String getCoresHexPosicao(int posicao) {
		return coresHex[posicao];
	}

	@SuppressWarnings("rawtypes")
	public static HashMap getMapaPontos() {
		return mapaPontos;
	}

	@SuppressWarnings("rawtypes")
	public static HashMap getMapaLigacoes() {
		return mapaLigacoes;
	}
}

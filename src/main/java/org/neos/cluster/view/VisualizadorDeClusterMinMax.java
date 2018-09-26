package org.neos.cluster.view;

import java.awt.Color;

import org.neos.cluster.Cluster;
import org.neos.cluster.Dataset;
import org.neos.cluster.Ponto;

import com.mxgraph.util.mxConstants;
import com.mxgraph.util.mxUtils;

public class VisualizadorDeClusterMinMax extends VisualizadorDeCluster {
	public VisualizadorDeClusterMinMax(Dataset dataset) {
		super(dataset);
		// TODO Auto-generated constructor stub
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void visualizarPontos(Cluster cluster) {
		String corHex = "";
		
		
		if(cluster.getIdCluster() == 46) {
			corHex = getCoresHexPosicao(cluster.getIdCluster() % getCoresHex().length);
			
		} else {
			corHex = "#95003A";
		}
		
		for (Ponto pontoCluster : cluster.getPontos()) {
			Object grupo = null;
			
			if(cluster.getIdCluster() != 46) {
				grupo = pontoCluster.getGrupo();
			}
			
			Object pontoGrafo = grafo.insertVertex(
					parent,
					null,
					grupo,//pontoCluster.getId() + ":G" + pontoCluster.getGrupo(),
					pontoCluster.getCoordX(),
					pontoCluster.getCoordY(),
					25, //0.5, 
					25, //0.5, 
					mxConstants.STYLE_SHAPE + "=" + mxConstants.SHAPE_RECTANGLE	+ ";"
							+ mxConstants.STYLE_FILLCOLOR + "=" + corHex + ";"
							+ mxConstants.STYLE_STROKECOLOR + "=" + corHex + ";"
							+ mxConstants.STYLE_STROKEWIDTH + "=" + 0 + ";"
							+ mxConstants.STYLE_FONTCOLOR + "=" + "#FFFFFF" + ";"
							+ mxConstants.STYLE_FONTSIZE + "=" + 15 + ";"
							+ mxConstants.STYLE_FONTSTYLE + "=" + mxConstants.FONT_BOLD);
			
			
			//"strokeColor=lightgray;whiteSpace=wrap;fillColor=lightgray"
			//shape=ellipse;perimeter=ellipsePerimeter;whiteSpace=wrap;fillColor=lightgray	//perimeter=100
			
			mapaPontos.put(pontoCluster.getId(), pontoGrafo);
		}
	}
}

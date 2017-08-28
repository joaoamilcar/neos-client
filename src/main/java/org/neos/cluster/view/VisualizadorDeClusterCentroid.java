package org.neos.cluster.view;

import java.awt.Color;

import org.neos.cluster.Cluster;
import org.neos.cluster.Dataset;
import org.neos.cluster.Ponto;

import com.mxgraph.util.mxConstants;
import com.mxgraph.util.mxUtils;

public class VisualizadorDeClusterCentroid extends VisualizadorDeCluster {
	public VisualizadorDeClusterCentroid(Dataset dataset) {
		super(dataset);
		// TODO Auto-generated constructor stub
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void visualizarPontos(Cluster cluster) {
		for (Ponto pontoCluster : cluster.getPontos()) {
			String formato, label;
			double dimensao1, dimensao2;
			String corHex = getCoresHexPosicao(pontoCluster.getGrupo() % getCoresHex().length);
						
			if (pontoCluster.isPontoEspecial()) {
				formato = mxConstants.STYLE_FILLCOLOR + "=" + mxUtils.getHexColorString(Color.white) + ";"
						+ mxConstants.STYLE_STROKECOLOR + "=" + mxUtils.getHexColorString(Color.white);
				
				label = null;
				dimensao1 = 0;
				dimensao2 = 0;
			} else {
				formato = mxConstants.STYLE_SHAPE + "=" + mxConstants.SHAPE_RECTANGLE + ";"
						+ mxConstants.STYLE_FILLCOLOR + "=" + corHex + ";"
						+ mxConstants.STYLE_STROKECOLOR + "=" + corHex;
				
				label = null;//label = pontoCluster.getId() + ":G" + pontoCluster.getGrupo();
				dimensao1 = 25; // 0.5
				dimensao2 = 25; // 0.5
			}
			
			Object pontoGrafo = grafo.insertVertex(
					parent,
					null,
					label,
					pontoCluster.getCoordX(),
					pontoCluster.getCoordY(),
					dimensao1,
					dimensao2,
					formato	+ ";"
							+ mxConstants.STYLE_STROKEWIDTH + "=" + 0 + ";"
							+ mxConstants.STYLE_FONTCOLOR + "=" + mxUtils.getHexColorString(Color.black) + ";"
							+ mxConstants.STYLE_FONTSIZE + "=" + 1 + ";"
							+ mxConstants.STYLE_FONTSTYLE + "=" + mxConstants.FONT_BOLD);
			//"strokeColor=lightgray;whiteSpace=wrap;fillColor=lightgray"
			//shape=ellipse;perimeter=ellipsePerimeter;whiteSpace=wrap;fillColor=lightgray	//perimeter=100
			
			mapaPontos.put(pontoCluster.getId(), pontoGrafo);
		}
		
	}
}

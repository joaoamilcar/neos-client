package org.neos.cluster.view;

import java.awt.Color;

import org.neos.cluster.Cluster;
import org.neos.cluster.Dataset;
import org.neos.cluster.Ponto;

import com.mxgraph.util.mxConstants;
import com.mxgraph.util.mxUtils;

public class VisualizadorDeClusterPMedian extends VisualizadorDeCluster {
	public VisualizadorDeClusterPMedian(Dataset dataset) {
		super(dataset);
		// TODO Auto-generated constructor stub
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void visualizarPontos(Cluster cluster) {
		for (Ponto pontoCluster : cluster.getPontos()) {
			String formato;
			String corHex = getCoresHexPosicao(pontoCluster.getGrupo() % getCoresHex().length);
						
			if (pontoCluster.getId() == pontoCluster.getGrupo()) {
				formato = mxConstants.STYLE_SHAPE + "=" + mxConstants.SHAPE_HEXAGON;
			} else {
				formato = mxConstants.STYLE_SHAPE + "=" + mxConstants.SHAPE_RECTANGLE;
			}
			
			Object pontoGrafo = grafo.insertVertex(
					parent,
					null,
					null,//pontoCluster.getId() + ":G" + pontoCluster.getGrupo(),
					pontoCluster.getCoordX(),
					pontoCluster.getCoordY(),
					25, // 0.5
					25, // 0.5
					formato	+ ";"
							+ mxConstants.STYLE_FILLCOLOR + "=" + corHex + ";"
							+ mxConstants.STYLE_STROKECOLOR + "=" + corHex + ";"
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

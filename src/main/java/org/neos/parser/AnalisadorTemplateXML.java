package org.neos.parser;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.neos.client.NeosJobXml;
import org.neos.exception.ExcecaoNeosXML;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class AnalisadorTemplateXML { // http://stackoverflow.com/questions/6604876/parsing-xml-with-nodelist-and-documentbuilder
	private String category;
	private String solver;
	private String inputMethod;
	private LinkedHashMap<String, String> mapaElementos;
	private ArrayList<AComponente> listaComponentes;
	
	public void parse(String solverTemplate) throws ExcecaoNeosXML {
		mapaElementos = new LinkedHashMap<String, String>();
		listaComponentes = new ArrayList<AComponente>();
		DocumentBuilderFactory documentoFactory = null;
		DocumentBuilder builder = null;
		Document documento = null;
			
		try {
			documentoFactory = DocumentBuilderFactory.newInstance();
			builder = documentoFactory.newDocumentBuilder();
			documento = builder.parse(new ByteArrayInputStream(solverTemplate.getBytes()));
		} catch (SAXException | IOException | ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Element elementoEmail = documento.createElement("email");
		documento.getElementsByTagName("document").item(0).appendChild(elementoEmail);
		
		NodeList lista = documento.getElementsByTagName("document").item(0).getChildNodes();
		ComponenteFactory componenteFactory = new ComponenteFactory();
		
		if (lista.getLength() > 0) {
			Node node = lista.item(0);
						
			do {
				if (node.getNodeName() != "#text") {
					if (node.getNodeName() == "category") {
						setCategory(node.getTextContent());
					} else if (node.getNodeName() == "solver") {
						setSolver(node.getTextContent());
					} else if (node.getNodeName() == "inputMethod") {
						setInputMethod(node.getTextContent());
					} else {
						mapaElementos.put(node.getNodeName(), node.getTextContent());
						listaComponentes.add(componenteFactory.criarComponenteGUI(node));
					}
				}
				
				node = node.getNextSibling();
			} while (node != null);
		} else {
			throw new ExcecaoNeosXML("No child nodes over <document> tag in solver XML template.");
		}
	}
	
	public String construirDocumento() throws IOException {
		//cria exJob objeto NeosJobXml com o tipo do problema (nco para otimizacao restrita nao-linear), solver (KNITRO) e input (AMPL)
		NeosJobXml exJob = new NeosJobXml(getCategory(), getSolver(), getInputMethod());
		
		for (int i = 0; i < listaComponentes.size(); i++) {
			exJob.addParam(listaComponentes.get(i).getToken(), listaComponentes.get(i).getConteudoComponente());
		}
		
		String jobString = exJob.toXMLString();
		
		return jobString;
	}
	
	public LinkedHashMap<String, String> getMapaElementos() {
		return mapaElementos;
	}
	
	public ArrayList<AComponente> getListaComponentes() {
		return listaComponentes;
	}
	
	public String getCategory() {
		return category;
	}
	
	public void setCategory(String category) {
		this.category = category;
	}
	
	public String getSolver() {
		return solver;
	}
	
	public void setSolver(String solver) {
		this.solver = solver;
	}
	
	public String getInputMethod() {
		return inputMethod;
	}
	
	public void setInputMethod(String inputMethod) {
		this.inputMethod = inputMethod;
	}
}

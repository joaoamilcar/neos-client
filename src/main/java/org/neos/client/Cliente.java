package org.neos.client;

import java.util.HashMap;
import java.util.Vector;

import org.apache.xmlrpc.XmlRpcException;
import org.neos.server.Servidor;

public class Cliente {
	private boolean conectado;
	private NeosXmlRpcClient serverWrap;
	private Integer currentJobNumber;
	private String currentPassword;
	private String currentJobStatus;
	private String currentJobResult;
	
	public Cliente() {
		this.currentJobNumber = 0;
		this.currentPassword = "";
		//this.result = "";
	}
	
	public void conectar(Servidor servidor) throws XmlRpcException {
		this.serverWrap = new NeosXmlRpcClient(servidor.getHost(), servidor.getPorta());
		serverWrap.connect();
		
		conectado = true;
	}
	
	public Object[] submitJob(String jobString) throws XmlRpcException {
		Vector<Object> params = new Vector<Object>();
		params.add(jobString);
		
		Object[] results = (Object[]) serverWrap.execute("submitJob", params, 5000L);
		
		currentJobNumber = (Integer) results[0];
		currentPassword = (String) results[1];

		return results;
	}
	
	/* inicializa receiver e começa a monitorar o output */
	public String getResult(Integer job, String password) {
		JobReceiver jobReceiver = new JobReceiver();
		ResultReceiver receiver = new ResultReceiver(serverWrap, jobReceiver, job, password);
		receiver.run();
		currentJobStatus = receiver.getStatus();
		currentJobResult = receiver.getResult();
		
		return currentJobResult;
	}
	
	public HashMap<?, ?> listCategories() throws XmlRpcException {
		HashMap<?, ?> results = (HashMap<?, ?>) serverWrap.execute("listCategories", new Vector<Object>(), 5000L);
		
		return results;
	}
	
	public Object[] listSolversInCategory(String category) throws XmlRpcException {
		Vector<Object> params = new Vector<Object>();
		params.add(category);
		
		Object[] results = (Object[]) serverWrap.execute("listSolversInCategory", params, 5000L);
		
		return results;
	}
	
	public String getSolverTemplate(String category, String solver, String inputMethod) throws XmlRpcException {
		Vector<Object> params = new Vector<Object>();
		params.add(category);
		params.add(solver);
		params.add(inputMethod);
		
		String results = (String) serverWrap.execute("getSolverTemplate", params, 5000L);
		
		return results;
	}
	
	public Object[] getJobInfo(Integer jobNumber, String password) throws XmlRpcException {
		Vector<Object> params = new Vector<Object>();
		params.add(jobNumber);
		params.add(password);
		
		Object[] results = (Object[]) serverWrap.execute("getJobInfo", params, 5000L);
		
		return results;
	}
	
	public String executar(String method) throws XmlRpcException {
		String results = (String) serverWrap.execute(method, new Vector<Object>(), 5000L); //invoca método no servidor e espera por resposta por 5000ms
		
		return results;
	}
	
	public String welcome() throws XmlRpcException {
		return executar("welcome");
	}
	
	public String ping() throws XmlRpcException {
		return executar("ping");
	}
	
	public String version() throws XmlRpcException {
		return executar("version");
	}
	
	public String printQueue() throws XmlRpcException {
		return executar("printQueue");
	}
	
	public String help() throws XmlRpcException {
		return executar("help");
	}
	
	public String emailHelp() throws XmlRpcException {
		return executar("emailHelp");
	}

	public boolean isConectado() {
		return conectado;
	}

	public void setConectado(boolean conectado) {
		this.conectado = conectado;
	}
	
	public Integer getCurrentJobNumber() {
		return currentJobNumber;
	}

	public String getCurrentPassword() {
		return currentPassword;
	}

	public String getCurrentJobStatus() {
		return currentJobStatus;
	}

	public String getCurrentJobResult() {
		return currentJobResult;
	}
}

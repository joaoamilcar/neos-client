package org.neos.client;

public class JobReceiver implements ResultCallback {
	@Override
	public void handleJobInfo(int jobNumero, String password) {
		System.out.println("Job Number : " + jobNumero);
		System.out.println("Password   : " + password);
	}
	
	@Override
	public void handleFinalResult(String resultados) {
		System.out.println(resultados);
	}
}

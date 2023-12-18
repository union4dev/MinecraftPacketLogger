package com.cubk.nsc2;

import com.cubk.nsc2.transformer.TransformerManager;

import java.lang.instrument.Instrumentation;

public class Agent {
	public static Instrumentation instrumentation;

	public static void premain(String args, Instrumentation instrumentation) {
		System.out.println("[NSC] Loaded.");
		Agent.instrumentation = instrumentation;
		instrumentation.addTransformer(new TransformerManager());
		NSC.getInstance().start();
	}
}
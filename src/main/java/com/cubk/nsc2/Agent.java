package com.cubk.nsc2;

import java.lang.instrument.Instrumentation;

public class Agent {
	public static void premain(String args, Instrumentation instrumentation) {
		System.out.println("[NSC] Loaded.");
		NSC.getInstance().start();
	}
}
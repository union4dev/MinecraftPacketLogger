package com.cubk.nsc2.transformer;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;

public interface Transformer extends Opcodes {
    void handle(ClassNode classNode);

    String getTarget();
}

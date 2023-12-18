package com.cubk.nsc2.transformer;

import com.cubk.nsc2.Agent;
import com.cubk.nsc2.transformer.transformers.BootstrapTransformer;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.ClassNode;

import java.io.File;
import java.io.FileOutputStream;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;
import java.util.ArrayList;
import java.util.List;

public class TransformerManager implements ClassFileTransformer {

    private final List<Transformer> transformers = new ArrayList<>();

    public TransformerManager() {
        transformers.add(new BootstrapTransformer());

        //retransformClasses();
    }

    public void retransformClasses() {
        for (Transformer transformer : transformers) {
            try {
                Class<?> target = Class.forName(transformer.getTarget().replace("/", "."));
                System.out.println("[NSC] Transforming: " + target.getName());
                Agent.instrumentation.retransformClasses(target);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
        for (Transformer transformer : transformers) {
            if (transformer.getTarget().equals(className)) {

                byte[] data = classfileBuffer;
                try {
                    ClassNode classNode = new ClassNode();
                    ClassReader reader = new ClassReader(classfileBuffer);
                    reader.accept(classNode, 0);

                    transformer.handle(classNode);

                    ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_MAXS);
                    classNode.accept(classWriter);

                    data = classWriter.toByteArray();

                    transformers.remove(transformer);
                    File file = new File("NSC_" + classNode.name.replace("/", ".") + ".class");
                    FileOutputStream fos = new FileOutputStream(file);
                    fos.write(data);
                    fos.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return data;
            }
        }
        return classfileBuffer;
    }
}

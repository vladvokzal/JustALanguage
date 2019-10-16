import jdk.internal.org.objectweb.asm.ClassWriter;
import jdk.internal.org.objectweb.asm.MethodVisitor;
import jdk.internal.org.objectweb.asm.Opcodes;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

public class Writer {

    public Writer(){
        this.outputFile = new File("/home/vladislav/IdeaProjects/JustALanguage/out/Main.class");
        this.baseWriter = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
        this.baseVisitor = baseWriter.visitMethod(Opcodes.ACC_PUBLIC + Opcodes.ACC_STATIC, "main", "([Ljava/lang/String;)V", null, null);
    }

    public MethodVisitor getBaseVisitor() {
        return baseVisitor;
    }

    public void write() throws IOException {
        visitFirst();
        visitLast();
        visitClassInitialization();
        FileUtils.writeByteArrayToFile(outputFile, baseWriter.toByteArray());
    }


    private void visitFirst(){
        baseWriter.visit(Opcodes.V1_8, Opcodes.ACC_PUBLIC, "Main", null, "java/lang/Object", null);
    }

    private void visitLast(){
        baseVisitor.visitInsn(Opcodes.RETURN);
        baseVisitor.visitMaxs(0, 0);
        baseVisitor.visitEnd();
    }

    private void visitClassInitialization(){
        MethodVisitor mVisitor = baseWriter.visitMethod(Opcodes.ACC_PUBLIC, "<init>", "()V", null, null);
        mVisitor.visitCode();
        mVisitor.visitVarInsn(Opcodes.ALOAD, 0);
        mVisitor.visitMethodInsn(Opcodes.INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false);
        mVisitor.visitInsn(Opcodes.RETURN);
        mVisitor.visitMaxs(0, 0);
        mVisitor.visitEnd();
    }

    private File[] matchingFile;
    private File outputFile;
    private ClassWriter baseWriter;
    private MethodVisitor baseVisitor;
}

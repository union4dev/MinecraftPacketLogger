package com.cubk.nsc2.handler;

import com.cubk.nsc2.NSC;
import com.cubk.nsc2.struct.Constant;
import io.netty.util.AttributeKey;

import javax.swing.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class ThreadScanner extends Thread {
	
	@Override
	public void run() {
		Thread[] threads = new Thread[1024];
		long start = System.currentTimeMillis();
		Object minecraftObj = null;
		Class<?> minecraftClass = null;
		do {
			int count = Thread.enumerate(threads);
			for (int i = 0; i < count; i++) {
				if(threads[i].getName().equals("Client thread")) {
					StackTraceElement[] stack = threads[i].getStackTrace();
					for (StackTraceElement element : stack) {
						try {
							Class<?> clazz = Class.forName(element.getClassName(), true, threads[i].getContextClassLoader());
							for(Field f : clazz.getDeclaredFields()) {
								if(Modifier.isStatic(f.getModifiers()) && f.getType() == clazz) {
									f.setAccessible(true);
									minecraftClass = clazz;
									minecraftObj = f.get(null);
									break;
								}
							}
						} catch (ClassNotFoundException e) {
							;
						} catch (Exception e) {
							System.exit(0);
						}
					}
				}
			}
			try {
				Thread.sleep(5L);
			} catch(InterruptedException e) {
				return;
			}
		} while(minecraftObj == null && System.currentTimeMillis() - start < 60000);

		if (minecraftObj == null) {
			try {
				Class<?> clazz = Class.forName("net.minecraft.client.Minecraft");
				for (Field f : clazz.getDeclaredFields()) {
					if (Modifier.isStatic(f.getModifiers()) && f.getType() == clazz) {
						Field.setAccessible(new Field[] { f }, true);
						minecraftObj = f.get(null);
					}
				}
			} catch(Throwable t) {
			}
		}
		if (minecraftObj != null && minecraftClass != null) {
			Constant.object_theMinecraft = minecraftObj;
			Field netMgr = null;

			for(Field field : minecraftClass.getDeclaredFields()){
				try {
					if(field.getType().getSuperclass().getName().equals("io.netty.channel.SimpleChannelInboundHandler")){
						netMgr = field;
					}
				}catch (Exception ignored){

				}
			}

			if(netMgr != null){
				netMgr.setAccessible(true);

				for(Method method : minecraftClass.getMethods()){
					if(method.getReturnType().getInterfaces().length == 1){
						try {
							if(method.getReturnType().getInterfaces()[0].getInterfaces()[0].getMethods().length == 1) {
								Constant.method_getNetHandler = method;
								for(Method method1 : method.getReturnType().getDeclaredMethods()){
									if(Modifier.isPublic(method1.getModifiers())) {
										if (method1.getReturnType().getName().equals(netMgr.getType().getName())) {
											Constant.method_getNetworkManager = method1;
										}
									}
								}
							}
						}catch (Exception e){

						}
					}
				}
				for(Method method : netMgr.getType().getDeclaredMethods()){
					try {
						if(method.getParameters()[0].getType().getSuperclass() == Enum.class)
						{
							Constant.class_EnumConnectionState = method.getParameters()[0].getType();
							for(Method method1 : Constant.class_EnumConnectionState.getMethods()){
								try {
									if(method1.getParameters()[0].getType().getSuperclass() == Enum.class && method1.getReturnType() == Integer.class) {
										Constant.class_EnumPacketDirection = method1.getParameters()[0].getType();
										Constant.method_getPacketId = method1;
									}
								}catch (Exception e){
								}
							}
						}
					}catch (Exception e){
					}
				}

				for(Field field : netMgr.getType().getDeclaredFields()) {
					if (field.getType() == AttributeKey.class) {
						try {
							field.setAccessible(true);
							Constant.object_attribute_key = (AttributeKey) field.get(null);
						} catch (IllegalAccessException e) {
							throw new RuntimeException(e);

						}
					}
				}
			}else {
				JOptionPane.showMessageDialog(null,"Error [0]","Internal error, failed to initialize\nNo network manager found",JOptionPane.ERROR_MESSAGE);
			}

		}else {
			JOptionPane.showMessageDialog(null,"Error [1]","Internal error, failed to initialize\nNo minecraft found",JOptionPane.ERROR_MESSAGE);
		}
	}
}
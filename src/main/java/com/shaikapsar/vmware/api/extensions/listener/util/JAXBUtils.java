/**
 * 
 */
package com.shaikapsar.vmware.api.extensions.listener.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.xml.bind.JAXBElement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vmware.vcloud.api.rest.schema.ObjectFactory;
import com.vmware.vcloud.sdk.JAXBUtil;

/**
 * @author Shaik Apsar | shaik.apsar@live.com
 *
 */
public class JAXBUtils {
	
	private static Logger logger = LoggerFactory.getLogger(JAXBUtils.class);
	
	public static <Type> Type unmarshallResource(byte[] bs){
		if(ApplicationUtils.isNotEmpty(bs)){
			ByteArrayInputStream arrayInputStream=new ByteArrayInputStream(bs);
			return JAXBUtil.unmarshallResource(arrayInputStream);
		}
		return null;
	}
	
	public static <Type> Type unmarshallResource(Object object) throws IOException{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(baos);
		oos.writeObject(object);
		oos.close();
		return unmarshallResource(baos.toByteArray());
	}
	
	public static <Type> byte[] getByteArray(Object object) throws IOException{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(baos);
		oos.writeObject(object);
		oos.close();
		return baos.toByteArray();
	}
	
	public static String getString(Object object) {
		try {
			Class<?> c = Class.forName(object.getClass().getPackage().getName()+"."+"ObjectFactory");			
			ObjectFactory objectFactory=(ObjectFactory)c.newInstance();
			Class<?> partypes[] = new Class[1];
			partypes[0] = object.getClass();
			Method m = c.getMethod("create"+object.getClass().getSimpleName().replaceAll("Type", ""), partypes);
			Object arglist[] = new Object[1];
			arglist[0] = object;
			return JAXBUtil.marshal((JAXBElement<?>) m.invoke(objectFactory, arglist));
		}catch(ClassNotFoundException exception){
			logger.error(exception.getMessage());
		}catch (SecurityException exception) {			
			logger.error(exception.getMessage());
		} catch (IllegalArgumentException exception) {
			logger.error(exception.getMessage());
		} catch (InstantiationException exception) {
			logger.error(exception.getMessage());
		} catch (IllegalAccessException exception) {
			logger.error(exception.getMessage());
		} catch (NoSuchMethodException exception) {
			logger.error(exception.getMessage());
		} catch (InvocationTargetException exception) {
			logger.error(exception.getMessage());
		}
		return null;	
	}	
	
	public static JAXBElement<?> getJAXBElement(Object object) {
		try {
			Class<?> c = Class.forName(object.getClass().getPackage().getName()+"."+"ObjectFactory");			
			ObjectFactory objectFactory=(ObjectFactory)c.newInstance();
			Class<?> partypes[] = new Class[1];
			partypes[0] = object.getClass();
			Method m = c.getMethod("create"+object.getClass().getSimpleName().replaceAll("Type", ""), partypes);
			Object arglist[] = new Object[1];
			arglist[0] = object;
			return (JAXBElement<?>) m.invoke(objectFactory, arglist);
		}catch(ClassNotFoundException exception){
			logger.error(exception.getMessage());
		}catch (SecurityException exception) {			
			logger.error(exception.getMessage());
		} catch (IllegalArgumentException exception) {
			logger.error(exception.getMessage());
		} catch (InstantiationException exception) {
			logger.error(exception.getMessage());
		} catch (IllegalAccessException exception) {
			logger.error(exception.getMessage());
		} catch (NoSuchMethodException exception) {
			logger.error(exception.getMessage());
		} catch (InvocationTargetException exception) {
			logger.error(exception.getMessage());
		}
		return null;	
	}
}

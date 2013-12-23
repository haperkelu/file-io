/**   
* @Title: LargeFileProcessor.java 
* @Package org.brilliance.file 
* @Description: TODO
* @author Pie.Li   
* @date 2013-12-22 下午10:17:41 
* @version V1.0   
*/
package org.brilliance.file;

import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;

import org.apache.log4j.Logger;

/**
 * Guarantee the memory is sufficient
 * @author Pie.Li
 *
 */
public class LargeFileProcessorContainer {
	
	/**
	 * Logger
	 */
	private static final Logger logger =  Logger.getLogger(LargeFileProcessorContainer.class);

	private String path;
	private FileCallBackInterface callback;
	private final int DEFAULT_BATCH_SIZE = 1024 * 1024;  //1Kb
	
	public LargeFileProcessorContainer(String path, FileCallBackInterface callback){
		this.path = path;
		this.callback = callback;
	}
	
	/**
	 * 
	 * @param path
	 * @throws IOException 
	 * @throws NoSuchMethodException 
	 * @throws SecurityException 
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 */
	public void iterate(int blockSize) throws IOException, SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException{
		
		FileInputStream inputSteam = null;
		FileChannel channel = null;
		
		try {
			inputSteam = new FileInputStream(path);
			channel = inputSteam.getChannel();
			
			final long total = channel.size();
			long cursor = 0;
			Method method = callback.getClass().getMethod("process", new Class[]{String.class});			
			
			if(total <= blockSize){
				String result = exatctAllInfoFromBuffer(channel, cursor, total);
				method.invoke(callback, result);
				return;
			}
			
			for(int i = 0; i < total / blockSize; i ++){
				String result = exatctAllInfoFromBuffer(channel, i * blockSize, blockSize);
				method.invoke(callback, result);
			}
			
			if(total % blockSize != 0){				
				String result = exatctAllInfoFromBuffer(channel, total - total % blockSize, total % blockSize);
				method.invoke(callback, result);
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			channel.close();
			inputSteam.close();
		}
		
	}
	
	/**
	 * 
	 * @param channel
	 * @param cursor
	 * @param size
	 * @return
	 * @throws IOException
	 */
	private String exatctAllInfoFromBuffer(FileChannel channel, long cursor, long size) throws IOException {
		
		MappedByteBuffer buffer = channel.map(MapMode.READ_ONLY, cursor, size);
		StringBuffer result = new StringBuffer();
		
		if(size <= DEFAULT_BATCH_SIZE){
			logger.debug("[exatctAllInfoFromBuffer]insufficient size");
			if(buffer.hasRemaining()){
				byte[] b = new byte[(int) size];
				buffer.get(b, (int)cursor, (int)(cursor + size));
				result.append(new String(b));	
				return result.toString();
			}
		}
		
		for(int i = 0; i < size / DEFAULT_BATCH_SIZE; i ++){
			byte[] b = new byte[DEFAULT_BATCH_SIZE];
			buffer.get(b, i * DEFAULT_BATCH_SIZE, DEFAULT_BATCH_SIZE);
			result.append(new String(b));	
		}
		
		if(size % DEFAULT_BATCH_SIZE != 0){
			logger.debug("[exatctAllInfoFromBuffer] uneven space left");
			byte[] b = new byte[(int)(size % DEFAULT_BATCH_SIZE)];;
			buffer.get(b, (int)(size -  size % DEFAULT_BATCH_SIZE), (int)(size % DEFAULT_BATCH_SIZE));
			result.append(new String(b));
		}
		
		return result.toString();
	}
	
	
}

/**   
* @Title: Boot.java 
* @Package org.brilliance.file 
* @Description: TODO
* @author Pie.Li   
* @date 2013-12-22 上午10:27:56 
* @version V1.0   
*/
package org.brilliance.file;


import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;

/**
 * @author Pie.Li
 *
 */
public class Boot {

	/**
	 * @param args
	 * @throws IOException 
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 * @throws NoSuchMethodException 
	 * @throws IllegalArgumentException 
	 * @throws SecurityException 
	 */
	public static void main(String[] args) throws IOException, SecurityException, IllegalArgumentException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
		
		String path = "/mnt/log/mysql-slow.log";
		path = "/Users/user/Downloads/66张.rar";
		
		LargeFileProcessorContainer container = new LargeFileProcessorContainer(path, new FileCallBackInterface(){

			public void process(String str) {
				System.out.println(str);
			}
			
		});
		//container.iterate(1024 * 1024);
		/**
		System.out.println(new Date());
		
		FileInputStream input = new FileInputStream("/Users/user/Downloads/66张.rar");
		FileChannel fc = input.getChannel();
		
		final int slotSize = 1024 * 1024;
		long size = fc.size();
		int cursor = 0;
		MappedByteBuffer buffer = null;
		StringBuffer stringBuffer = new StringBuffer();
		
		for(int i = 0; i < size/slotSize; i ++){
			cursor = i * 1024 * 1024;
			buffer = fc.map(MapMode.READ_ONLY, cursor, slotSize);			
			while(buffer.hasRemaining()){
				byte b = buffer.get();
				//stringBuffer.append(new String(new byte[]{b}));			
			}
		}
		if(size % slotSize != 0){
			buffer = fc.map(MapMode.READ_ONLY, cursor + slotSize, size - cursor - slotSize);			
			while(buffer.hasRemaining()){
				byte b = buffer.get();
				//stringBuffer.append(new String(new byte[]{b}));			
			}
		}
		//System.out.println(stringBuffer.toString());
		System.out.println(new Date());
		fc.close();
		input.close();
		**/
		
		NormalFileReader nr = new NormalFileReader("/mnt/log/mysql-slow.log", new FileCallBackInterface(){

			public void process(String str) {
				System.out.println(str);
			}
			
		});
		nr.iterate();
		
	}

}

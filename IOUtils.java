package cn.edu360;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class IOUtils {
	/**
	 * 把字节数组保存到指定的标准文件中
	 * 
	 * @param byteArray
	 *            字节数组
	 * @param saveFile
	 *            指定文件的对象
	 */
	public static void byteArrayToFile(byte[] byteArray, File saveFile) {
		try ( // public ByteArrayInputStream(byte[] buf)创建一个
				// ByteArrayInputStream，使用 buf作为其缓冲区数组
				ByteArrayInputStream bais = new ByteArrayInputStream(byteArray);
				BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(saveFile));) {
			byte[] buf = new byte[1024];
			int len;
			while ((len = bais.read(buf)) != -1) {
				bos.write(buf, 0, len);
			}
			System.out.println("字节数组保存到指定文件成功");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 把字节数组保存到指定的标准文件中
	 * 
	 * @param byteArray
	 *            字节数组
	 * @param savePath
	 *            指定文件的路径
	 */
	public static void byteArrayToFile(byte[] byteArray, String savePath) {
		byteArrayToFile(byteArray, new File(savePath));
	}

	/**
	 * 将指定标准文件转换成字节数组
	 * 
	 * @param srcFile
	 *            标准文件的对象
	 * @return 返回的字节数组，如果发送异常返回的是null
	 */
	public static byte[] fileToByteArray(File srcFile) {
		try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(srcFile));
				// public ByteArrayOutputStream()创建一个新的 byte 数组输出流。缓冲区的容量最初是 32
				// 字节，如有必要可增加其大小
				ByteArrayOutputStream baos = new ByteArrayOutputStream();) {
			// 自定义容器
			byte[] buf = new byte[1024];
			// 自定义变量保存每次读取的字节长度
			int len;
			// 循环读写
			while ((len = bis.read(buf)) != -1) {
				// 读多少就写出多少
				baos.write(buf, 0, len);
			}
			// public byte[] toByteArray()创建一个新分配的 byte
			// 数组。其大小是此输出流的当前大小，并且缓冲区的有效内容已复制到该数组中
			byte[] byteArray = baos.toByteArray();
			return byteArray;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 将指定标准文件转换成字节数组
	 * 
	 * @param srcPath
	 *            标准文件的路径
	 * @return 返回的字节数组，如果发送异常返回的是null
	 */
	public static byte[] fileToByteArray(String srcPath) {
		return fileToByteArray(new File(srcPath));
	}

	/**
	 * 拷贝多级文件夹
	 * 
	 * @param srcFolderFile
	 *            原文件夹对象
	 * @param destFolderFile
	 *            目标文件夹对象
	 */
	public static void copyMultiFolder(File srcFolderFile, File destFolderFile) {
		// 4.判断当前文件是不是一个文件夹
		if (srcFolderFile.isDirectory()) {
			// 如果是，在目标文件夹中创建一个一模一样的父级文件夹，就遍历该文件夹底下所有的子文件，然后再调用该方法
			destFolderFile = new File(destFolderFile, srcFolderFile.getName());
			destFolderFile.mkdir();
			// 遍历该文件夹底下所有的子文件，然后再调用该方法
			File[] files = srcFolderFile.listFiles();
			for (File f : files) {
				copyMultiFolder(f, destFolderFile);
			}
		} else {// 如果不是，就是一个标准文件
				// 5.在目标文件夹中创建一个一模一样名字的子文件，然后进行拷贝
			File destFile = new File(destFolderFile, srcFolderFile.getName());
			// 5.1调用拷贝文件的方法
			IOUtils.copyFile(srcFolderFile, destFile);
		}

	}

	/**
	 * 拷贝多级文件夹
	 * 
	 * @param srcFolderPath
	 *            原文件夹对象
	 * @param destFolderPath
	 *            目标文件夹对象
	 */
	public static void copyMultiFolder(String srcFolderPath, String destFolderPath) {
		copyMultiFolder(new File(srcFolderPath), new File(destFolderPath));
	}

	/**
	 * 拷贝单级文件夹中指定后缀名文件并修改文件的名称
	 * 
	 * @param suffix
	 *            后缀名
	 * @param srcFolderFile
	 *            原文件夹对象
	 * @param destFolderFile
	 *            目标文件夹对象
	 */
	public static void copySingleFolder(String suffix, File srcFolderFile, File destFolderFile) {
		// 2.1在目标文件中创建一个一模一样的文件夹
		destFolderFile = new File(destFolderFile, srcFolderFile.getName());
		destFolderFile.mkdir();

		// 3.过滤指定的文件
		File[] files = srcFolderFile.listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return new File(dir, name).isFile() ? name.endsWith(suffix) : false;
			}
		});
		// 4.拷贝文件到目标文件夹中并修改名字
		for (File srcFile : files) {
			// 4.1在目标文件夹中创建一个改过名字之后的文件
			File destFile = new File(destFolderFile, getUUID() + suffix);
			// 4.2调用拷贝文件的方法
			IOUtils.copyFile(srcFile, destFile);
		}
		System.out.println("拷贝单级文件夹指定文件成功");
	}

	/**
	 * 拷贝单级文件夹中指定后缀名文件并修改文件的名称
	 * 
	 * @param suffix
	 *            后缀名
	 * @param srcFolderPath
	 *            原文件夹路径
	 * @param destFolderPath
	 *            目标文件夹路径
	 */
	public static void copySingleFolder(String suffix, String srcFolderPath, String destFolderPath) {
		copySingleFolder(suffix, new File(srcFolderPath), new File(destFolderPath));
	}

	/**
	 * 获得一个UUID
	 * 
	 * @return String UUID
	 */
	public static String getUUID() {
		String uuid = UUID.randomUUID().toString();
		// 去掉“-”符号
		return uuid.replaceAll("-", "");
	}

	/**
	 * 从文本文件中读取数据(每一行为一个字符串数据)到集合中
	 * 
	 * @param srcPath
	 *            读取字符串的文件路径
	 * @return 返回的存储字符串的集合
	 */
	public static List<String> fileToList(String srcPath) {
		ArrayList<String> list = new ArrayList<String>();
		try (BufferedReader br = new BufferedReader(new FileReader(srcPath));) {
			// 定义一个变量用于保存每次读取的字符串
			String line;
			// 循环读取
			while ((line = br.readLine()) != null) {
				// 把读取到的内容存储到list集合中
				list.add(line);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	/**
	 * 把ArrayList集合中的字符串数据存储到文本文件
	 * 
	 * @param list
	 *            原集合
	 * @param savePath
	 *            存放的文件路径
	 */
	public static void listToFile(List<String> list, String savePath) {
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(savePath));) {
			// 循环遍历list集合，然后把每一条数据写出到文本文件中
			for (String value : list) {
				bw.write(value);
				// 换行
				bw.newLine();
			}
			System.out.println("ArrayList内容输出到文本文件成功");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 拷贝任何标准文件
	 * 
	 * @param srcFile
	 *            原文件对象
	 * @param destFile
	 *            目标文件对象
	 */
	public static void copyFile(File srcFile, File destFile) {
		try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(srcFile));
				BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(destFile));) {
			// 自定义一个字节数组容器
			byte[] buf = new byte[1024];
			// 自定义以变量用于记录实际每次读取的字节个数
			int len;
			// 循环读写
			while ((len = bis.read(buf)) != -1) {
				// 读多少就写出多少
				bos.write(buf, 0, len);
			}
			System.out.println("拷贝文件成功");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 拷贝任何标准文件
	 * 
	 * @param srcPath
	 *            原文件路径
	 * @param destPath
	 *            目标文件路径
	 */
	public static void copyFile(String srcPath, String destPath) {
		copyFile(new File(srcPath), new File(destPath));
	}

	/**
	 * 拷贝文本文件
	 * 
	 * @param srcFile
	 *            原文件对象
	 * @param destFile
	 *            目标文件对象
	 */
	public static void copyTextFile(File srcFile, File destFile) {
		try (BufferedReader br = new BufferedReader(new FileReader(srcFile));
				BufferedWriter bw = new BufferedWriter(new FileWriter(destFile));) {
			// 自定义一个字符数组容器
			char[] cbuf = new char[1024];
			// 自定义变量用于保存每次实际读取的字符个数
			int len;
			// 循环读写
			while ((len = br.read(cbuf)) != -1) {
				// 读多少就写出多少
				bw.write(cbuf, 0, len);
			}
			System.out.println("拷贝文本文件成功");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 拷贝文本文件
	 * 
	 * @param srcPath
	 *            原文件路径
	 * @param destPath
	 *            目标文件路径
	 */
	public static void copyTextFile(String srcPath, String destPath) {
		copyTextFile(new File(srcPath), new File(destPath));
	}
}

package cn.edu360;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class SplitAndGroupFile {
	public static void main(String[] args) {
		splitFile("E:/a.txt");
		groupFile("D:/b.txt");
	}

	/**
	 * 将指定的碎片文件组成到指定的文件中
	 * 
	 * @param savePath
	 */
	private static void groupFile(String savePath) {
		// 1.创建一个保存字节数组的字节数组输出流
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		for (int i = 0; i < 4; i++) {
			switch (i) {
			case 0:
				readByteArrayFromFile(baos, "D:/temp1.dll");
				break;
			case 1:
				readByteArrayFromFile(baos, "D:/temp2.dll");

				break;
			case 2:
				readByteArrayFromFile(baos, "D:/temp3.dll");

				break;
			case 3:
				// 先判断存不存在第四个文件
				File file = new File("D:/temp4.dll");
				if (file.exists()) {
					readByteArrayFromFile(baos, "D:/temp4.dll");
				}
				break;
			}
		}

		byte[] byteArray = baos.toByteArray();
		// 解密字节数组
		xorByte(byteArray, 66);

		// 将字节数组保存到指定的路径文件中
		IOUtils.byteArrayToFile(byteArray, savePath);
	}

	/**
	 * 从指定的路径文件中将字节写入到baos中
	 * 
	 * @param baos
	 * @param srcPath
	 */
	private static void readByteArrayFromFile(ByteArrayOutputStream baos, String srcPath) {
		try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(srcPath));) {
			byte[] buf = new byte[1024];
			int len;
			while ((len = bis.read(buf)) != -1) {
				baos.write(buf, 0, len);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 将指定的文件最多分割成4个文件，最少3三个文件
	 * 
	 * @param srcPath
	 *            原文件路径
	 */
	private static void splitFile(String srcPath) {
		// 1.将指定的文件转换成字节数组
		byte[] byteArray = IOUtils.fileToByteArray(srcPath);
		// 2.把字节数组加密
		xorByte(byteArray, 66);
		// 3.将字节数组最多保存到四部分文件中
		int size = byteArray.length / 3;
		for (int i = 0; i < 3; i++) {
			switch (i) {
			case 0:
				try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream("D:/temp1.dll"));) {

					bos.write(byteArray, 0, size);
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			case 1:
				try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream("D:/temp2.dll"));) {
					bos.write(byteArray, size, size);
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			case 2:
				try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream("D:/temp3.dll"));) {
					bos.write(byteArray, size * 2, size);
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			}
		}

		// 判断还有没有剩余的字节没有保存
		if (byteArray.length > size * 3) {
			try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream("D:/temp4.dll"));) {
				bos.write(byteArray, size * 3, byteArray.length - size * 3);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		System.out.println("分割加密文件成功");
	}

	/**
	 * 将字节数组中的所有字节异或指定的值
	 * 
	 * @param byteArray
	 * @param key
	 */
	private static void xorByte(byte[] byteArray, int key) {
		for (int i = 0; i < byteArray.length; i++) {
			byteArray[i] = (byte) (byteArray[i] ^ key);
		}
	}

}

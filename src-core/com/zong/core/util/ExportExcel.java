package com.zong.core.util;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;

import jxl.Workbook;
import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.VerticalAlignment;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableImage;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

/**
 * 
 * @desc 基于jxl实现的excel导出工具
 * @author zong
 * @date 2016-4-20
 */
public class ExportExcel {
	/***************************************************************************
	 * @param fileName
	 *            EXCEL文件名称
	 * @param listTitle
	 *            EXCEL文件第一行列标题集合
	 * @param listContent
	 *            EXCEL文件正文数据集合
	 * @return
	 */
	public final static String exportExcel(String fileName, String[] listTitle, List<Map<String, Object>> listContent, HttpServletResponse response) {
		String result = "系统提示：Excel文件导出成功！";
		// 以下开始输出到EXCEL
		try {
			// 定义输出流，以便打开保存对话框______________________begin
			OutputStream os = response.getOutputStream();// 取得输出流
			response.reset();// 清空输出流
			response.setHeader("Content-disposition", "attachment; filename=" + new String(fileName.getBytes("utf-8"), "ISO8859-1") + ".xls");
			// 设定输出文件头
			response.setContentType("application/msexcel");// 定义输出类型
			// 定义输出流，以便打开保存对话框_______________________end

			/** **********创建工作簿************ */
			WritableWorkbook workbook = Workbook.createWorkbook(os);

			/** **********创建工作表************ */

			WritableSheet sheet = workbook.createSheet("Sheet1", 0);

			/** **********设置纵横打印（默认为纵打）、打印纸***************** */
			jxl.SheetSettings sheetset = sheet.getSettings();
			sheetset.setProtected(false);

			/** ************设置单元格字体************** */
			WritableFont NormalFont = new WritableFont(WritableFont.ARIAL, 10);
			WritableFont BoldFont = new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD);

			/** ************以下设置三种单元格样式，灵活备用************ */
			// 用于标题居中
			WritableCellFormat wcf_center = new WritableCellFormat(BoldFont);
			wcf_center.setBorder(Border.ALL, BorderLineStyle.THIN); // 线条
			wcf_center.setVerticalAlignment(VerticalAlignment.CENTRE); // 文字垂直对齐
			wcf_center.setAlignment(Alignment.CENTRE); // 文字水平对齐
			wcf_center.setWrap(false); // 文字是否换行

			// 用于正文居左
			WritableCellFormat wcf_left = new WritableCellFormat(NormalFont);
			wcf_left.setBorder(Border.NONE, BorderLineStyle.THIN); // 线条
			wcf_left.setVerticalAlignment(VerticalAlignment.CENTRE); // 文字垂直对齐
			wcf_left.setAlignment(Alignment.LEFT); // 文字水平对齐
			wcf_left.setWrap(false); // 文字是否换行

			/** ***************以下是EXCEL开头大标题，暂时省略********************* */
			// sheet.mergeCells(0, 0, colWidth, 0);
			// sheet.addCell(new Label(0, 0, "XX报表", wcf_center));
			/** ***************以下是EXCEL第一行列标题********************* */
			for (int i = 0; i < listTitle.length; i++) {
				sheet.addCell(new Label(i, 0, listTitle[i], wcf_center));
			}
			/** ***************以下是EXCEL正文数据********************* */
			int i = 1;
			// 用map封装行数据
			for (Map<String, Object> map : listContent) {
				int j = 0;
				for (String key : listTitle) {
					String value = map.get(key) == null ? "" : map.get(key).toString();
					sheet.addCell(new Label(j, i, value, wcf_left));
					j++;
				}
				i++;
			}
			/** **********将以上缓存中的内容写到EXCEL文件中******** */
			workbook.write();
			/** *********关闭文件************* */
			workbook.close();

		} catch (Exception e) {
			result = "系统提示：Excel文件导出失败，原因：" + e.toString();
			System.out.println(result);
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * @param fileName
	 *            EXCEL文件名称
	 * @param listTitle
	 *            EXCEL文件第一行列标题集合
	 * @param listContent
	 *            EXCEL文件正文数据集合
	 * @return
	 */
	public final static String exportExcelObject(String fileName, String[] Title, List<Object> listContent, HttpServletResponse response) {
		String result = "系统提示：Excel文件导出成功！";
		// 以下开始输出到EXCEL
		try {
			// 定义输出流，以便打开保存对话框______________________begin
			OutputStream os = response.getOutputStream();// 取得输出流
			response.reset();// 清空输出流
			response.setHeader("Content-disposition", "attachment; filename=" + new String(fileName.getBytes("utf-8"), "ISO8859-1"));
			// 设定输出文件头
			response.setContentType("application/msexcel");// 定义输出类型
			// 定义输出流，以便打开保存对话框_______________________end

			/** **********创建工作簿************ */
			WritableWorkbook workbook = Workbook.createWorkbook(os);

			/** **********创建工作表************ */

			WritableSheet sheet = workbook.createSheet("Sheet1", 0);

			/** **********设置纵横打印（默认为纵打）、打印纸***************** */
			jxl.SheetSettings sheetset = sheet.getSettings();
			sheetset.setProtected(false);

			/** ************设置单元格字体************** */
			WritableFont NormalFont = new WritableFont(WritableFont.ARIAL, 10);
			WritableFont BoldFont = new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD);

			/** ************以下设置三种单元格样式，灵活备用************ */
			// 用于标题居中
			WritableCellFormat wcf_center = new WritableCellFormat(BoldFont);
			wcf_center.setBorder(Border.ALL, BorderLineStyle.THIN); // 线条
			wcf_center.setVerticalAlignment(VerticalAlignment.CENTRE); // 文字垂直对齐
			wcf_center.setAlignment(Alignment.CENTRE); // 文字水平对齐
			wcf_center.setWrap(false); // 文字是否换行

			// 用于正文居左
			WritableCellFormat wcf_left = new WritableCellFormat(NormalFont);
			wcf_left.setBorder(Border.NONE, BorderLineStyle.THIN); // 线条
			wcf_left.setVerticalAlignment(VerticalAlignment.CENTRE); // 文字垂直对齐
			wcf_left.setAlignment(Alignment.LEFT); // 文字水平对齐
			wcf_left.setWrap(false); // 文字是否换行

			/** ***************以下是EXCEL开头大标题，暂时省略********************* */
			// sheet.mergeCells(0, 0, colWidth, 0);
			// sheet.addCell(new Label(0, 0, "XX报表", wcf_center));
			/** ***************以下是EXCEL第一行列标题********************* */
			for (int i = 0; i < Title.length; i++) {
				sheet.addCell(new Label(i, 0, Title[i], wcf_center));
			}
			/** ***************以下是EXCEL正文数据********************* */
			Field[] fields = null;
			int i = 1;
			for (Object obj : listContent) {
				fields = obj.getClass().getDeclaredFields();
				int j = 0;
				for (int k = 0; k < fields.length; k++) {
					Field v = fields[k];
					v.setAccessible(true);
					Object va = v.get(obj);
					if (va == null) {
						va = "";
					}
					sheet.addCell(new Label(j, i, va.toString(), wcf_left));
					if (k == fields.length - 1) {
						// sheet.addImage(new WritableImage(200, 25*i, 50, 25,
						// new File(va.toString())));//图片只支持png的。。
					}
					j++;
				}
				i++;
			}
			/** **********将以上缓存中的内容写到EXCEL文件中******** */
			workbook.write();
			/** *********关闭文件************* */
			workbook.close();

		} catch (Exception e) {
			result = "系统提示：Excel文件导出失败，原因：" + e.toString();
			System.out.println(result);
			e.printStackTrace();
		}
		return result;
	}

	public static WritableWorkbook getWorkbook(String fileName, HttpServletResponse response) throws Exception {
		// 定义输出流，以便打开保存对话框______________________begin
		OutputStream os = response.getOutputStream();// 取得输出流
		response.reset();// 清空输出流
		response.setHeader("Content-disposition", "attachment; filename=" + new String(fileName.getBytes("utf-8"), "ISO8859-1"));
		// 设定输出文件头
		response.setContentType("application/msexcel");// 定义输出类型
		// 定义输出流，以便打开保存对话框_______________________end

		/** **********创建工作簿************ */
		WritableWorkbook workbook = Workbook.createWorkbook(os);

		return workbook;
	}

	public static void writeWorkbook(WritableWorkbook workbook) {
		try {
			workbook.write();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (workbook != null) {
				try {
					workbook.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 由于jxl只能导出png格式，所以要转码 从图片里面得到字节数组
	 * 
	 * @param bi
	 * @return
	 */
	public static byte[] getImageData(BufferedImage bi) {
		try {
			// 转化成PNG
			ByteArrayOutputStream bout = new ByteArrayOutputStream();
			ImageIO.write(bi, "PNG", bout);
			return bout.toByteArray();
		} catch (Exception exe) {
			exe.printStackTrace();
			return null;
		}
	}

	public static void main(String[] args) {
		WritableWorkbook wwb = null;
		try {
			wwb = Workbook.createWorkbook(new File("D:/image.xls"));
			WritableSheet ws = wwb.createSheet("图片", 0);
			File dir = new File("F:/goddess/data/goddess");
			File[] files = dir.listFiles();
			for (int i = 0; i < 100; i++) {
				// 前两位是起始格，后两位是图片占多少个格，并非是位置
				WritableImage image = new WritableImage(0, i * 7, 2, 7, getImageData(ImageIO.read(files[i])));
				ws.addImage(image);
				System.out.println("====" + files[i].getAbsolutePath() + "====");
			}
			wwb.write();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (wwb != null) {
				try {
					wwb.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
}

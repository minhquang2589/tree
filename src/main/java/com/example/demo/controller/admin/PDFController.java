package com.example.demo.controller.admin;

import com.example.demo.Utils.PreferencesUtils;
import com.example.demo.model.ProductSearch;
import com.example.demo.model.User;
import com.itextpdf.text.*;
import com.itextpdf.text.Font;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

import static com.example.demo.Utils.Config.*;

public class PDFController {

    public static void printInvoice(List<ProductSearch> cartItems, String payCode) throws IOException {
        String filePath = chooseFilePath(payCode);
        User user = PreferencesUtils.getUser();
        try {
            createInvoicePDF(filePath, payCode, cartItems, user);
        } catch (DocumentException | FileNotFoundException e) {
            e.printStackTrace();
        }
        if (filePath != null) {
            File pd = new File(filePath);
            Desktop.getDesktop().open(pd);
        }
    }


    public static void createInvoicePDF(String filePath, String payCode, List<ProductSearch> cartItems, User user) throws DocumentException, FileNotFoundException {
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(filePath));
        BaseFont baseFont;
        try {
            baseFont = BaseFont.createFont("src/main/resources/assets/images/font/Inter_24pt-Medium.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        Map<String, Double> summary = calculateCartTotal(cartItems, null);
        double totalAmount = summary.get("totalAmount");
        double totalDiscount = summary.get("totalDiscount");
        double totalQuantity = summary.get("totalQuantity");
        int totalQuantityInt = (int) totalQuantity;

        Font font = new Font(baseFont, 8);
        Font boldFont = new Font(baseFont, 10, Font.BOLD);
        Font titleFont = new Font(baseFont, 15, Font.BOLD);
        Font smallFont = new Font(baseFont, 6);

        document.open();

        Paragraph title = new Paragraph("HÓA ĐƠN THANH TOÁN", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);
        document.add(Chunk.NEWLINE);

        Paragraph headerDetails = new Paragraph("Mã hoá đơn: " + payCode + "  |  " + getCurrentDate(), font);
        headerDetails.setAlignment(Element.ALIGN_LEFT);
        document.add(headerDetails);
        document.add(Chunk.NEWLINE);

        document.add(new Paragraph("======================================================================================================================================", smallFont));
        PdfPTable table = new PdfPTable(7);
        table.setWidthPercentage(100);
        table.setSpacingBefore(10);
        float[] columnWidths = {0.09f, 0.3f, 0.2f, 0.15f, 0.2f, 0.15f, 0.2f};
        table.setWidths(columnWidths);
        table.addCell(createTableCell("STT", boldFont));
        table.addCell(createTableCell("Tên", boldFont));
        table.addCell(createTableCell("Phân loại", boldFont));
        table.addCell(createTableCell("Size", boldFont));
        table.addCell(createTableCell("Giá", boldFont));
        table.addCell(createTableCell("Chiết khấu (%)", boldFont));
        table.addCell(createTableCell("Thành tiền", boldFont));

        int serialNo = 1;
        for (ProductSearch item : cartItems) {
            table.addCell(createTableCell(String.valueOf(serialNo++), font));
            table.addCell(createTableCell(item.getTenSanPham(), font));
            table.addCell(createTableCell(item.getLoai(), font));
            table.addCell(createTableCell(item.getSize() + "  x  " + item.getSoLuong(), font));
            table.addCell(createTableCell(formatCurrencyVND(item.getThanhTien()), font));
            table.addCell(createTableCell(String.format("%.2f", item.getChietKhau()), font));
            table.addCell(createTableCell(formatCurrencyVND(item.getGia()), font));
        }
        document.add(table);
        document.add(Chunk.NEWLINE);

        Paragraph qty = new Paragraph("Số lượng:   " + totalQuantityInt + " sản phẩm", boldFont);
        qty.setAlignment(Element.ALIGN_RIGHT);
        document.add(qty);

        Paragraph totalDis = new Paragraph("Giảm giá:   - " + formatCurrencyVND(totalDiscount), boldFont);
        totalDis.setAlignment(Element.ALIGN_RIGHT);
        document.add(totalDis);

        Paragraph totalParagraph = new Paragraph("Tổng số tiền:   " + formatCurrencyVND(totalAmount), boldFont);
        totalParagraph.setAlignment(Element.ALIGN_RIGHT);
        document.add(totalParagraph);
        document.add(new Paragraph("======================================================================================================================================", smallFont));

        document.add(Chunk.NEWLINE);
        document.add(new Paragraph("Tree Shop", boldFont));
        document.add(new Paragraph("Địa chỉ: " + user.getAddress(), font));
        document.add(new Paragraph("Số điện thoại: " + user.getPhone(), font));
        document.add(new Paragraph("Email: " + user.getEmail(), font));

        document.add(Chunk.NEWLINE);
        Paragraph thankYouParagraph = new Paragraph("Cảm ơn quý khách đã mua hàng tại Tree Shop", font);
        thankYouParagraph.setAlignment(Element.ALIGN_CENTER);
        document.add(thankYouParagraph);

        document.close();
    }

    private static PdfPCell createTableCell(String content, Font font) {
        PdfPCell cell = new PdfPCell(new Phrase(content, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setPadding(8);
        cell.setPaddingTop(10);
        cell.setPaddingBottom(10);
        cell.setBorder(PdfPCell.NO_BORDER);
        cell.setNoWrap(false);
        return cell;
    }


    public static String chooseFilePath(String payCode) {
        Path targetDir = Paths.get("src/main/resources/assets/images/invoice");
        if (!Files.exists(targetDir)) {
            try {
                Files.createDirectories(targetDir);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
        String fileName = "Invoice" + payCode + ".pdf";
        File file = targetDir.resolve(fileName).toFile();
        return file.getAbsolutePath();
    }
}

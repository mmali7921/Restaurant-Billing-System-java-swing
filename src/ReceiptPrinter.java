import java.awt.print.*;
import java.awt.*;
import java.io.PrintWriter;
import java.io.IOException;

/**
 * Sends a receipt to a physical printer or saves it as a .txt file.
 */
public class ReceiptPrinter {

    /** Opens a system print dialog and prints the receipt. */
    public static void printReceipt(String receiptText) {
        PrinterJob job = PrinterJob.getPrinterJob();
        job.setPrintable((graphics, pageFormat, pageIndex) -> {
            if (pageIndex > 0) return Printable.NO_SUCH_PAGE;
            Graphics2D g2d = (Graphics2D) graphics;
            g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());
            Font font = new Font("Monospaced", Font.PLAIN, 10);
            g2d.setFont(font);
            FontMetrics fm = g2d.getFontMetrics();
            int lineH = fm.getHeight();
            int y = lineH;
            for (String line : receiptText.split("\n")) {
                g2d.drawString(line, 0, y);
                y += lineH;
                if (y > pageFormat.getImageableHeight()) break;
            }
            return Printable.PAGE_EXISTS;
        });
        if (job.printDialog()) {
            try { job.print(); }
            catch (PrinterException e) { e.printStackTrace(); }
        }
    }

    /** Saves the receipt to a .txt file in the working directory. */
    public static String saveToFile(String receiptText, String orderNumber) {
        String filename = "receipt_" + orderNumber.replace("-", "_") + ".txt";
        try (PrintWriter pw = new PrintWriter(filename)) {
            pw.print(receiptText);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return filename;
    }
}

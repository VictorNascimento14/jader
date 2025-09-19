package com.consultoria.imagem.util;

import com.consultoria.imagem.model.ListaDeCompras;
import com.consultoria.imagem.model.Peca;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import java.io.IOException;
import java.time.format.DateTimeFormatter;

public class PDFGenerator {

    private static final float MARGIN = 50;
    private static final float FONT_SIZE_TITLE = 18;
    private static final float FONT_SIZE_SUBTITLE = 14;
    private static final float FONT_SIZE_NORMAL = 12;
    private static final float LINE_HEIGHT = 15;
    private static final float IMAGE_WIDTH = 100;
    private static final float IMAGE_HEIGHT = 100;

    public static void gerarPDF(ListaDeCompras lista, String caminhoArquivo) throws IOException {
        PDDocument document = new PDDocument();
        PDPage page = new PDPage(PDRectangle.A4);
        document.addPage(page);

        PDPageContentStream contentStream = new PDPageContentStream(document, page);

        float yPosition = page.getMediaBox().getHeight() - MARGIN;

        // --- Cabeçalho ---
        contentStream.beginText();
        contentStream.setFont(PDType1Font.HELVETICA_BOLD, FONT_SIZE_TITLE);
        contentStream.newLineAtOffset(MARGIN, yPosition);
        contentStream.showText("Dossiê de Compras - Consultoria de Imagem");
        contentStream.endText();
        yPosition -= 30;

        contentStream.beginText();
        contentStream.setFont(PDType1Font.HELVETICA_BOLD, FONT_SIZE_SUBTITLE);
        contentStream.newLineAtOffset(MARGIN, yPosition);
        contentStream.showText("Cliente: " + lista.getCliente().getNome());
        contentStream.endText();
        yPosition -= 20;

        contentStream.beginText();
        contentStream.setFont(PDType1Font.HELVETICA, FONT_SIZE_NORMAL);
        contentStream.newLineAtOffset(MARGIN, yPosition);
        contentStream.showText("Data: " + lista.getDataCriacao().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        contentStream.endText();
        yPosition -= 30;

        // --- Mensagem ---
        contentStream.beginText();
        contentStream.setFont(PDType1Font.HELVETICA, FONT_SIZE_NORMAL);
        contentStream.newLineAtOffset(MARGIN, yPosition);
        contentStream.showText("Olá, " + lista.getCliente().getNome() + "!");
        contentStream.endText();
        yPosition -= LINE_HEIGHT;

        contentStream.beginText();
        contentStream.setFont(PDType1Font.HELVETICA, FONT_SIZE_NORMAL);
        contentStream.newLineAtOffset(MARGIN, yPosition);
        contentStream.showText("Preparei uma seleção especial de peças que vão valorizar ainda mais o seu estilo.");
        contentStream.endText();
        yPosition -= 30;

        // Linha separadora
        contentStream.moveTo(MARGIN, yPosition);
        contentStream.lineTo(page.getMediaBox().getWidth() - MARGIN, yPosition);
        contentStream.stroke();
        yPosition -= 20;

        // --- Lista de peças ---
        int contador = 1;
        double valorTotal = 0;

        for (Peca peca : lista.getPecas()) {

            // Se não houver espaço suficiente, criar nova página
            if (yPosition < 150) {
                contentStream.close(); // fecha o contentStream da página anterior
                page = new PDPage(PDRectangle.A4);
                document.addPage(page);
                contentStream = new PDPageContentStream(document, page);
                yPosition = page.getMediaBox().getHeight() - MARGIN;
            }

            // Nome da peça
            contentStream.beginText();
            contentStream.setFont(PDType1Font.HELVETICA_BOLD, FONT_SIZE_SUBTITLE);
            contentStream.newLineAtOffset(MARGIN, yPosition);
            contentStream.showText(contador + ". " + peca.getNome());
            contentStream.endText();
            yPosition -= 20;

            // Imagem
            if (peca.getFotoPath() != null && !peca.getFotoPath().isEmpty()) {
                try {
                    PDImageXObject image = PDImageXObject.createFromFile(peca.getFotoPath(), document);
                    contentStream.drawImage(image, MARGIN, yPosition - IMAGE_HEIGHT, IMAGE_WIDTH, IMAGE_HEIGHT);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                yPosition -= IMAGE_HEIGHT + 10;
            }

            // Descrição
            if (peca.getDescricao() != null && !peca.getDescricao().trim().isEmpty()) {
                contentStream.beginText();
                contentStream.setFont(PDType1Font.HELVETICA, FONT_SIZE_NORMAL);
                contentStream.newLineAtOffset(MARGIN + 20, yPosition);
                contentStream.showText("Descrição: " + peca.getDescricao());
                contentStream.endText();
                yPosition -= LINE_HEIGHT;
            }

            // Loja
            contentStream.beginText();
            contentStream.setFont(PDType1Font.HELVETICA, FONT_SIZE_NORMAL);
            contentStream.newLineAtOffset(MARGIN + 20, yPosition);
            contentStream.showText("Onde encontrar: " + peca.getLoja().getNome());
            contentStream.endText();
            yPosition -= LINE_HEIGHT;

            // Endereço da loja
            if (peca.getLoja().getEndereco() != null && !peca.getLoja().getEndereco().trim().isEmpty()) {
                contentStream.beginText();
                contentStream.setFont(PDType1Font.HELVETICA, FONT_SIZE_NORMAL);
                contentStream.newLineAtOffset(MARGIN + 20, yPosition);
                contentStream.showText("Endereço: " + peca.getLoja().getEndereco());
                contentStream.endText();
                yPosition -= LINE_HEIGHT;
            }

            // Valor
            contentStream.beginText();
            contentStream.setFont(PDType1Font.HELVETICA_BOLD, FONT_SIZE_NORMAL);
            contentStream.newLineAtOffset(MARGIN + 20, yPosition);
            contentStream.showText("Valor: R$ " + String.format("%.2f", peca.getValor()));
            contentStream.endText();
            yPosition -= LINE_HEIGHT;

            // Observações
            if (peca.getObservacoes() != null && !peca.getObservacoes().trim().isEmpty()) {
                contentStream.beginText();
                contentStream.setFont(PDType1Font.HELVETICA_OBLIQUE, FONT_SIZE_NORMAL);
                contentStream.newLineAtOffset(MARGIN + 20, yPosition);
                contentStream.showText("Observações: " + peca.getObservacoes());
                contentStream.endText();
                yPosition -= LINE_HEIGHT;
            }

            // Linha separadora
            yPosition -= 10;
            contentStream.moveTo(MARGIN, yPosition);
            contentStream.lineTo(page.getMediaBox().getWidth() - MARGIN, yPosition);
            contentStream.stroke();
            yPosition -= 20;

            valorTotal += peca.getValor();
            contador++;
        }

        // Total
        yPosition -= 10;
        contentStream.beginText();
        contentStream.setFont(PDType1Font.HELVETICA_BOLD, FONT_SIZE_SUBTITLE);
        contentStream.newLineAtOffset(MARGIN, yPosition);
        contentStream.showText("Valor Total Estimado: R$ " + String.format("%.2f", valorTotal));
        contentStream.endText();
        yPosition -= 40;

        // Rodapé
        contentStream.beginText();
        contentStream.setFont(PDType1Font.HELVETICA, FONT_SIZE_NORMAL);
        contentStream.newLineAtOffset(MARGIN, yPosition);
        contentStream.showText("Atenciosamente,");
        contentStream.endText();
        yPosition -= LINE_HEIGHT;

        contentStream.beginText();
        contentStream.setFont(PDType1Font.HELVETICA_BOLD, FONT_SIZE_NORMAL);
        contentStream.newLineAtOffset(MARGIN, yPosition);
        contentStream.showText("Jader Átila");
        contentStream.endText();
        yPosition -= LINE_HEIGHT;

        contentStream.beginText();
        contentStream.setFont(PDType1Font.HELVETICA, FONT_SIZE_NORMAL);
        contentStream.newLineAtOffset(MARGIN, yPosition);
        contentStream.showText("Sistema de Gestão - Consultoria de Imagem");
        contentStream.endText();

        contentStream.close();
        document.save(caminhoArquivo);
        document.close();
    }
}

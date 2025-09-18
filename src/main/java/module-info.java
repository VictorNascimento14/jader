open module consultoria.imagem.app {
    requires java.sql;
    requires javafx.controls;
    requires javafx.fxml;
    requires org.apache.pdfbox; // se quiser ser explícito, senão pode omitir

    exports com.consultoria.imagem;
    exports com.consultoria.imagem.util;
    exports com.consultoria.imagem.controller;
}

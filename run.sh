#!/bin/bash

# Script para executar a aplicação de Consultoria de Imagem

echo "Iniciando aplicação de Consultoria de Imagem..."

# Verificar se o Java está instalado
if ! command -v java &> /dev/null; then
    echo "Erro: Java não está instalado. Por favor, instale o Java 17 ou superior."
    exit 1
fi

# Verificar versão do Java
JAVA_VERSION=$(java -version 2>&1 | head -n 1 | cut -d'"' -f2 | cut -d'.' -f1)
if [ "$JAVA_VERSION" -lt 17 ]; then
    echo "Erro: Java 17 ou superior é necessário. Versão atual: $JAVA_VERSION"
    exit 1
fi

# Executar a aplicação
java -cp "target/classes:target/lib/*" \
     --module-path "target/lib" \
     --add-modules javafx.controls,javafx.fxml \
     com.consultoria.imagem.App

echo "Aplicação finalizada."


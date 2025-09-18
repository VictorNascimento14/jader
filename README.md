# Sistema de Gestão - Consultoria de Imagem

Este é um sistema completo para consultores de imagem gerenciarem seus clientes, catálogo de peças e gerarem listas de compras personalizadas em PDF.

## Funcionalidades

- **Gerenciamento de Clientes**: Cadastre e gerencie informações dos seus clientes
- **Gerenciamento de Lojas**: Mantenha um cadastro das lojas parceiras
- **Catálogo de Peças**: Organize seu catálogo de roupas e acessórios com fotos, preços e descrições
- **Montagem de Listas**: Crie listas personalizadas de compras para cada cliente
- **Geração de PDF**: Gere dossiês profissionais em PDF para entregar aos clientes

## Requisitos do Sistema

- Java 17 ou superior
- Sistema operacional: Windows, macOS ou Linux

## Como Executar

### Opção 1: Usando o script (Linux/macOS)
```bash
./run.sh
```

### Opção 2: Usando Maven
```bash
mvn javafx:run
```

### Opção 3: Executando diretamente
```bash
java -cp "target/classes:target/lib/*" \
     --module-path "target/lib" \
     --add-modules javafx.controls,javafx.fxml \
     com.consultoria.imagem.App
```

## Como Usar

### 1. Primeira Execução
- Ao executar pela primeira vez, o sistema criará automaticamente o banco de dados SQLite
- A tela principal (Dashboard) será exibida

### 2. Gerenciamento de Lojas
- Clique em "Gerenciar Lojas" no Dashboard
- Adicione as lojas onde seus clientes podem comprar as peças
- Preencha nome, endereço e contato de cada loja

### 3. Gerenciamento de Clientes
- Clique em "Gerenciar Clientes" no Dashboard
- Cadastre seus clientes com nome, contato e observações
- Use o botão "Criar Lista" para montar uma lista de compras para um cliente específico

### 4. Catálogo de Peças
- Clique em "Gerenciar Peças (Catálogo)" no Dashboard
- Adicione peças ao seu catálogo com:
  - Nome e descrição da peça
  - Loja onde pode ser encontrada
  - Valor
  - Caminho para foto da peça
  - Observações

### 5. Montagem de Listas
- Na tela de clientes, selecione um cliente e clique em "Criar Lista"
- Na tela de montagem:
  - À esquerda: catálogo de peças disponíveis
  - À direita: lista sendo montada para o cliente
  - Use o botão ">> Adicionar à Lista" para incluir peças
  - Use "Remover" para retirar peças da lista
  - Acompanhe o valor total em tempo real

### 6. Geração de PDF
- Na tela de montagem de lista, clique em "Gerar PDF"
- Escolha onde salvar o arquivo
- O PDF será gerado com:
  - Informações do cliente
  - Lista detalhada das peças recomendadas
  - Informações das lojas
  - Valores
  - Layout profissional

## Estrutura de Arquivos

```
consultoria-imagem-app/
├── src/main/java/com/consultoria/imagem/
│   ├── App.java                    # Classe principal
│   ├── controller/                 # Controladores das telas
│   ├── model/                      # Classes de modelo (Cliente, Loja, Peça, etc.)
│   └── util/                       # Utilitários (DatabaseManager, PDFGenerator)
├── src/main/resources/com/consultoria/imagem/
│   └── *.fxml                      # Arquivos de interface (FXML)
├── target/                         # Arquivos compilados
├── consultoria.db                  # Banco de dados SQLite (criado automaticamente)
├── run.sh                          # Script de execução
└── README.md                       # Este arquivo
```

## Banco de Dados

O sistema usa SQLite como banco de dados, que é criado automaticamente no arquivo `consultoria.db`. As seguintes tabelas são criadas:

- `lojas`: Informações das lojas
- `clientes`: Dados dos clientes
- `pecas`: Catálogo de peças
- `listas_de_compras`: Listas criadas para clientes
- `lista_pecas`: Relacionamento entre listas e peças

## Solução de Problemas

### Erro: "Module javafx.controls not found"
- Certifique-se de que está usando Java 17 ou superior
- Verifique se as dependências do JavaFX foram baixadas corretamente

### Erro: "Java não encontrado"
- Instale o Java 17 ou superior
- Verifique se o Java está no PATH do sistema

### Problemas com fotos das peças
- Certifique-se de que o caminho para a foto está correto
- Use caminhos absolutos para as imagens
- Formatos suportados: PNG, JPG, JPEG, GIF, BMP

## Desenvolvimento

Este projeto foi desenvolvido usando:
- Java 17
- JavaFX 17.0.2
- Apache PDFBox 2.0.29
- SQLite JDBC 3.42.0.0
- Maven como gerenciador de dependências

## Licença

Este software foi desenvolvido para uso em consultoria de imagem. Todos os direitos reservados.

